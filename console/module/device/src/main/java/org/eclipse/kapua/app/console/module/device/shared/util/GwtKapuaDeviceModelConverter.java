/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionStatus;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel.GwtDeviceAssetChannelMode;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionAttributes;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import java.util.ArrayList;
import java.util.List;

public class GwtKapuaDeviceModelConverter {

    private GwtKapuaDeviceModelConverter() {
    }

    public static DeviceConnectionQuery convertConnectionQuery(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);
        DeviceConnectionQuery query = factory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));
        AndPredicateImpl predicate = new AndPredicateImpl();

        if (gwtDeviceConnectionQuery.getClientId() != null && !gwtDeviceConnectionQuery.getClientId().trim().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(DeviceConnectionAttributes.CLIENT_ID, gwtDeviceConnectionQuery.getClientId(), Operator.LIKE));
        }
        if (gwtDeviceConnectionQuery.getConnectionStatus() != null && !gwtDeviceConnectionQuery.getConnectionStatus().equals(GwtDeviceConnectionStatus.ANY.toString())) {
            predicate.and(new AttributePredicateImpl<DeviceConnectionStatus>(DeviceConnectionAttributes.STATUS, convertConnectionStatus(gwtDeviceConnectionQuery.getConnectionStatus()), Operator.EQUAL));
        }
        if (gwtDeviceConnectionQuery.getClientIP() != null && !gwtDeviceConnectionQuery.getClientIP().trim().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(DeviceConnectionAttributes.CLIENT_IP, gwtDeviceConnectionQuery.getClientIP(), Operator.LIKE));
        }
        if (gwtDeviceConnectionQuery.getUserName() != null && !gwtDeviceConnectionQuery.getUserName().trim().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(DeviceConnectionAttributes.USER_ID, gwtDeviceConnectionQuery.getUserName(), Operator.LIKE));
        }
        if (gwtDeviceConnectionQuery.getGwtDeviceConnectionUser() != null) {
                predicate = predicate.and(new AttributePredicateImpl<KapuaId>(DeviceConnectionAttributes.USER_ID, KapuaEid.parseCompactId(gwtDeviceConnectionQuery.getUserId())));
        }
        if (gwtDeviceConnectionQuery.getGwtDeviceConnectionReservedUser() != null) {
            switch (gwtDeviceConnectionQuery.getGwtDeviceConnectionReservedUser()) {
            case NONE:
                predicate = predicate.and(new AttributePredicateImpl<KapuaId>(DeviceConnectionAttributes.RESERVED_USER_ID, null, Operator.IS_NULL));
                break;
            default:
                predicate = predicate.and(new AttributePredicateImpl<KapuaId>(DeviceConnectionAttributes.RESERVED_USER_ID, KapuaEid.parseCompactId(gwtDeviceConnectionQuery.getReservedUserId())));
            }
        }

        if (gwtDeviceConnectionQuery.getProtocol() != null && !gwtDeviceConnectionQuery.getProtocol().trim().isEmpty()) {
            predicate.and(new AttributePredicateImpl<String>(DeviceConnectionAttributes.PROTOCOL, gwtDeviceConnectionQuery.getProtocol(), Operator.LIKE));
        }

        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? DeviceConnectionAttributes.CLIENT_ID : loadConfig.getSortField();
        if (sortField.equals("connectionUserCouplingMode")) {
            sortField = DeviceConnectionAttributes.USER_COUPLING_MODE;
        } else if (sortField.equals("modifiedOnFormatted")) {
            sortField = DeviceConnectionAttributes.MODIFIED_ON;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        query.setSortCriteria(sortCriteria);
        query.setOffset(loadConfig.getOffset());
        query.setLimit(loadConfig.getLimit());
        query.setPredicate(predicate);

        return query;
    }

    public static DeviceConnectionStatus convertConnectionStatus(String connectionStatus) {
        return DeviceConnectionStatus.valueOf(connectionStatus);
    }

    public static DeviceAssets convertDeviceAssets(GwtDeviceAssets deviceAssets) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssets assets = assetFactory.newAssetListResult();
        List<DeviceAsset> assetList = new ArrayList<DeviceAsset>();
        for (GwtDeviceAsset gwtDeviceAsset : deviceAssets.getAssets()) {
            assetList.add(convertDeviceAsset(gwtDeviceAsset));
        }
        assets.setAssets(assetList);
        return assets;
    }

    public static DeviceAsset convertDeviceAsset(GwtDeviceAsset gwtDeviceAsset) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAsset deviceAsset = assetFactory.newDeviceAsset();
        deviceAsset.setName(gwtDeviceAsset.getName());
        for (GwtDeviceAssetChannel gwtDeviceAssetChannel : gwtDeviceAsset.getChannels()) {
            deviceAsset.getChannels().add(convertDeviceAssetChannel(gwtDeviceAssetChannel));
        }
        return deviceAsset;
    }

    public static DeviceAssetChannel convertDeviceAssetChannel(GwtDeviceAssetChannel gwtDeviceAssetChannel) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssetChannel channel = assetFactory.newDeviceAssetChannel();
        channel.setName(gwtDeviceAssetChannel.getName());
        try {
            channel.setType(ObjectTypeConverter.fromString(gwtDeviceAssetChannel.getType()));
            channel.setValue(ObjectValueConverter.fromString(gwtDeviceAssetChannel.getValue(), channel.getType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        channel.setTimestamp(gwtDeviceAssetChannel.getTimestamp());
        channel.setMode(convertDeviceAssetChannel(gwtDeviceAssetChannel.getModeEnum()));
        channel.setError(gwtDeviceAssetChannel.getError());
        return channel;

    }

    public static DeviceAssetChannelMode convertDeviceAssetChannel(GwtDeviceAssetChannelMode gwtMode) {
        return DeviceAssetChannelMode.valueOf(gwtMode.toString());
    }

    public static DeviceQuery convertDeviceQuery(PagingLoadConfig loadConfig, GwtDeviceQuery gwtDeviceQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

        DeviceQuery deviceQuery = deviceFactory.newQuery(KapuaEid.parseCompactId(gwtDeviceQuery.getScopeId()));
        if (loadConfig != null) {
            deviceQuery.setLimit(loadConfig.getLimit());
            deviceQuery.setOffset(loadConfig.getOffset());
        }

        GwtDeviceQueryPredicates predicates = gwtDeviceQuery.getPredicates();
        AndPredicateImpl andPred = new AndPredicateImpl();

        if (predicates.getClientId() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.CLIENT_ID, predicates.getUnescapedClientId(), Operator.LIKE));
        }
        if (predicates.getDisplayName() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.DISPLAY_NAME, predicates.getUnescapedDisplayName(), Operator.LIKE));
        }
        if (predicates.getSerialNumber() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.SERIAL_NUMBER, predicates.getUnescapedSerialNumber(), Operator.LIKE));
        }
        if (predicates.getDeviceStatus() != null) {
            andPred = andPred.and(new AttributePredicateImpl<DeviceStatus>(DeviceAttributes.STATUS, DeviceStatus.valueOf(predicates.getDeviceStatus())));
        }
        if (predicates.getIotFrameworkVersion() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.APPLICATION_FRAMEWORK_VERSION, predicates.getIotFrameworkVersion(), Operator.LIKE));
        }
        if (predicates.getApplicationIdentifiers() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.APPLICATION_IDENTIFIERS, predicates.getApplicationIdentifiers(), Operator.LIKE));
        }
        if (predicates.getCustomAttribute1() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.CUSTOM_ATTRIBUTE_1, predicates.getCustomAttribute1(), Operator.LIKE));
        }
        if (predicates.getCustomAttribute2() != null) {
            andPred = andPred.and(new AttributePredicateImpl<String>(DeviceAttributes.CUSTOM_ATTRIBUTE_2, predicates.getCustomAttribute2(), Operator.LIKE));
        }
        if (predicates.getDeviceConnectionStatus() != null) {
            switch (predicates.getDeviceConnectionStatusEnum()) {
            case UNKNOWN:
                andPred = andPred.and(new AttributePredicateImpl<DeviceConnectionStatus>(DeviceAttributes.CONNECTION_ID, DeviceConnectionStatus.NULL, Operator.IS_NULL));
                break;
            default:
                andPred = andPred.and(new AttributePredicateImpl<DeviceConnectionStatus>(DeviceAttributes.CONNECTION_STATUS, DeviceConnectionStatus.valueOf(predicates.getDeviceConnectionStatus())));
            }
        }
        if (predicates.getGroupDevice() != null) {
            switch (predicates.getGroupDeviceEnum()) {
            case NO_GROUP:
                andPred = andPred.and(new AttributePredicateImpl<KapuaId>(DeviceAttributes.GROUP_ID, null, Operator.IS_NULL));
                break;
            default:
                if (predicates.getGroupId() != null) {
                    andPred = andPred.and(new AttributePredicateImpl<KapuaId>(DeviceAttributes.GROUP_ID, KapuaEid.parseCompactId(predicates.getGroupId())));
                }
            }
        }
        if(predicates.getTagId() != null) {
            andPred = andPred.and(new AttributePredicateImpl<KapuaId[]>(DeviceAttributes.TAG_IDS, new KapuaId[] { GwtKapuaCommonsModelConverter.convertKapuaId(predicates.getTagId()) }));
        }
        if (predicates.getSortAttribute() != null && loadConfig != null) {
            String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? DeviceAttributes.CLIENT_ID : loadConfig.getSortField();
            SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
            if (sortField.equals("lastEventOnFormatted")) {
                sortField = DeviceAttributes.LAST_EVENT_ON;
            }
            deviceQuery.setSortCriteria(new FieldSortCriteria(sortField, sortOrder));
        } else {
            deviceQuery.setSortCriteria(new FieldSortCriteria(DeviceAttributes.CLIENT_ID, SortOrder.ASCENDING));
        }

        deviceQuery.setPredicate(andPred);

        return deviceQuery;
    }
}
