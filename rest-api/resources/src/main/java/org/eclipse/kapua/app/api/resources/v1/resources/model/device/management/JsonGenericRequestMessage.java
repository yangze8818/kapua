/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.model.device.management;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.app.api.resources.v1.resources.model.message.JsonKapuaPayload;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.UUID;

public class JsonGenericRequestMessage {

    private UUID id;

    private KapuaId scopeId;
    private KapuaId deviceId;
    private String clientId;

    private Date receivedOn;
    private Date sentOn;
    private Date capturedOn;

    private KapuaPosition position;
    private GenericRequestChannel channel;
    private JsonKapuaPayload payload;

    public JsonGenericRequestMessage() {
    }

    public JsonGenericRequestMessage(GenericRequestMessage genericRequestMessage) {
        setId(genericRequestMessage.getId());

        setScopeId(genericRequestMessage.getScopeId());
        setDeviceId(genericRequestMessage.getDeviceId());
        setClientId(genericRequestMessage.getClientId());

        setReceivedOn(genericRequestMessage.getReceivedOn());
        setSentOn(genericRequestMessage.getSentOn());
        setCapturedOn(genericRequestMessage.getCapturedOn());

        setPosition(genericRequestMessage.getPosition());
        setChannel(genericRequestMessage.getChannel());
        setPayload(genericRequestMessage.getPayload());
    }

    @XmlElement(name = "id")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = deviceId;
    }

    @XmlElement(name = "clientId")
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @XmlElement(name = "receivedOn")
    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    @XmlElement(name = "sentOn")
    public Date getSentOn() {
        return sentOn;
    }

    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    @XmlElement(name = "capturedOn")
    public Date getCapturedOn() {
        return capturedOn;
    }

    public void setCapturedOn(Date capturedOn) {
        this.capturedOn = capturedOn;
    }

    @XmlElement(name = "position")
    public KapuaPosition getPosition() {
        return position;
    }

    public void setPosition(KapuaPosition position) {
        this.position = position;
    }

    @XmlElement(name = "channel")
    public GenericRequestChannel getChannel() {
        return channel;
    }

    public void setChannel(GenericRequestChannel channel) {
        this.channel = channel;
    }

    @XmlElement(name = "payload")
    public JsonKapuaPayload getPayload() {
        return payload;
    }

    public void setPayload(JsonKapuaPayload payload) {
        this.payload = payload;
    }

    @XmlTransient
    public void setPayload(KapuaPayload payload) {
        setPayload(new JsonKapuaPayload(payload));
    }
}
