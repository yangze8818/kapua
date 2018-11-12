/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.jms.kura;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsTopic;

/**
 * Messages translator implementation from {@link org.eclipse.kapua.transport.message.jms.JmsMessage} to {@link org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage}
 *
 * @since 1.0
 */
public class TranslatorLifeAppsJmsKura extends Translator<JmsMessage, KuraAppsMessage> {

    @Override
    public KuraAppsMessage translate(JmsMessage jmsMessage)
            throws KapuaException {
        return new KuraAppsMessage(translate(jmsMessage.getTopic()),
                jmsMessage.getReceivedOn(),
                translate(jmsMessage.getPayload().getBody()));
    }

    private KuraAppsChannel translate(JmsTopic jmsTopic)
            throws KapuaException {
        String[] topicTokens = jmsTopic.getSplittedTopic();
        // we shouldn't never get a shorter topic here (because that means we have issues on camel routing)
        if (topicTokens == null || topicTokens.length < 3) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }

        return new KuraAppsChannel(topicTokens[0], topicTokens[1], topicTokens[2]);
    }

    private KuraAppsPayload translate(byte[] jmsBody)
            throws KapuaException {
        KuraAppsPayload kuraAppsPayload = new KuraAppsPayload();
        kuraAppsPayload.readFromByteArray(jmsBody);
        return kuraAppsPayload;
    }

    @Override
    public Class<JmsMessage> getClassFrom() {
        return JmsMessage.class;
    }

    @Override
    public Class<KuraAppsMessage> getClassTo() {
        return KuraAppsMessage.class;
    }

}
