/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.service.datastore.model.xml.ChannelInfoXmlRegistry;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Channel information query result list definition.<br>
 * This object contains the list of the channel information objects retrieved by the search service.
 *
 * @since 1.0
 */
@XmlRootElement(name = "channelInfos")
@XmlType(factoryClass = ChannelInfoXmlRegistry.class, factoryMethod = "newChannelInfoListResult")
public interface ChannelInfoListResult extends StorableListResult<ChannelInfo> {

}
