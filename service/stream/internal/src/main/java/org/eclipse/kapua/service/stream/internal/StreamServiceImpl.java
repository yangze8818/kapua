/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.stream.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.kura.exception.KuraMqttDeviceCallException;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.stream.StreamDomains;
import org.eclipse.kapua.service.stream.StreamService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.message.TransportMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@KapuaProvider
public class StreamServiceImpl implements StreamService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    @Override
    public KapuaResponseMessage<?, ?> publish(KapuaDataMessage requestMessage, Long timeout)
            throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(requestMessage.getScopeId(), "scopeId");
        ArgumentValidator.notNull(requestMessage.getDeviceId(), "deviceId");
        ArgumentValidator.notNull(requestMessage.getClientId(), "clientId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(StreamDomains.STREAM_DOMAIN, Actions.write, requestMessage.getScopeId()));

        //
        // Do publish
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade = null;
        try {

            Device device = DEVICE_REGISTRY_SERVICE.find(requestMessage.getScopeId(), requestMessage.getDeviceId());

            if (device == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, requestMessage.getDeviceId());
            }

            String nodeUri = device.getConnection().getServerIp();

            //
            // Borrow a KapuaClient
            transportFacade = borrowClient(nodeUri);

            //
            // Get Kura to transport translator for the request and vice versa
            Translator<KapuaDataMessage, KuraDataMessage> translatorKapuaKura = getTranslator(KapuaDataMessage.class, KuraDataMessage.class);
            Translator<KuraDataMessage, ?> translatorKuraTransport = getTranslator(KuraDataMessage.class, transportFacade.getMessageClass());

            KuraDataMessage kuraDataMessage = translatorKapuaKura.translate(requestMessage);

            //
            // Do send
            try {
                // Set current timestamp
                kuraDataMessage.setTimestamp(new Date());

                // Send
                transportFacade.sendAsync((TransportMessage<?, ?>) translatorKuraTransport.translate(kuraDataMessage));

            } catch (KapuaException e) {
                throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CLIENT_SEND_ERROR,
                        e,
                        (Object[]) null);
            }
        } catch (KapuaException ke) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    ke,
                    (Object[]) null);
        } finally {
            if (transportFacade != null) {
                transportFacade.clean();
            }
        }

        return null;
    }

    //
    // Private methods
    //
    private TransportFacade<?, ?, TransportMessage<?, ?>, ?> borrowClient(String serverUri)
            throws KuraMqttDeviceCallException {
        TransportFacade<?, ?, TransportMessage<?, ?>, ?> transportFacade;
        Map<String, Object> configParameters = new HashMap<>();
        configParameters.put("serverAddress", serverUri);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TransportClientFactory<?, ?, ?, ?, ?, ?> transportClientFactory = locator.getFactory(TransportClientFactory.class);

            transportFacade = (TransportFacade<?, ?, TransportMessage<?, ?>, ?>) transportClientFactory.getFacade(configParameters);
        } catch (Exception e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
        }
        return transportFacade;
    }

    private <T1 extends Message<?, ?>, T2 extends Message<?, ?>> Translator<T1, T2> getTranslator(Class<T1> from, Class<T2> to)
            throws KuraMqttDeviceCallException {
        Translator<T1, T2> translator;
        try {
            translator = Translator.getTranslatorFor(from, to);
        } catch (KapuaException e) {
            throw new KuraMqttDeviceCallException(KuraMqttDeviceCallErrorCodes.CALL_ERROR,
                    e,
                    (Object[]) null);
        }
        return translator;
    }
}
