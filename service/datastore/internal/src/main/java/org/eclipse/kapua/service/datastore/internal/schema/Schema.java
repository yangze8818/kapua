/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;
import org.eclipse.kapua.service.datastore.client.model.IndexRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexResponse;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreErrorCodes;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Datastore schema creation/update
 *
 * @since 1.0
 */
public class Schema {

    private static final Logger LOG = LoggerFactory.getLogger(Schema.class);

    /**
     * Construct the Elasticsearch schema
     */
    public Schema() {
    }

    /**
     * Synchronize metadata
     *
     * @param scopeId
     * @param time
     * @return
     * @throws ClientException
     */
    public Metadata synch(KapuaId scopeId, long time)
            throws ClientException {
        String dataIndexName;
        try {
            String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
            dataIndexName = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
        } catch (KapuaException kaex) {
            throw new ClientException(DatastoreErrorCodes.CONFIGURATION_ERROR, "Error while generating index name", kaex);
        }

        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(dataIndexName);
        if (currentMetadata != null) {
            return currentMetadata;
        }

        LOG.debug("Before entering updating metadata");
        synchronized (Schema.class) {
            LOG.debug("Entered updating metadata");
            DatastoreClient datastoreClient = DatastoreClientFactory.getInstance();
            // Check existence of the data index
            IndexResponse dataIndexExistsResponse = datastoreClient.isIndexExists(new IndexRequest(dataIndexName));
            if (!dataIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(dataIndexName, getMappingSchema(dataIndexName));
                LOG.info("Data index created: " + dataIndexName);
            }

            boolean enableAllField = false;
            boolean enableSourceField = true;

            datastoreClient.putMapping(new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME), MessageSchema.getMesageTypeSchema(enableAllField, enableSourceField));
            // Check existence of the kapua internal index
            String registryIndexName = DatastoreUtils.getRegistryIndexName(scopeId);
            IndexResponse registryIndexExistsResponse = datastoreClient.isIndexExists(new IndexRequest(registryIndexName));
            if (!registryIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(registryIndexName, getMappingSchema(registryIndexName));
                LOG.info("Metadata index created: " + registryIndexExistsResponse);

                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME), ChannelInfoSchema.getChannelTypeSchema(enableAllField, enableSourceField));
                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, MetricInfoSchema.METRIC_TYPE_NAME), MetricInfoSchema.getMetricTypeSchema(enableAllField, enableSourceField));
                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, ClientInfoSchema.CLIENT_TYPE_NAME), ClientInfoSchema.getClientTypeSchema(enableAllField, enableSourceField));
            }

            currentMetadata = new Metadata(dataIndexName, registryIndexName);
            LOG.debug("Leaving updating metadata");
        }

        // Current metadata can only increase the custom mappings
        // other fields does not change within the same account id
        // and custom mappings are not and must not be exposed to
        // outside this class to preserve thread safetyness
        DatastoreCacheManager.getInstance().getMetadataCache().put(dataIndexName, currentMetadata);

        return currentMetadata;
    }

    /**
     * Update metric mappings
     *
     * @param scopeId
     * @param time
     * @param metrics
     * @throws ClientException
     */
    public void updateMessageMappings(KapuaId scopeId, long time, Map<String, Metric> metrics)
            throws ClientException {
        if (metrics == null || metrics.size() == 0) {
            return;
        }
        String newIndex;
        try {
            String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
            newIndex = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
        } catch (KapuaException kaex) {
            throw new ClientException(DatastoreErrorCodes.CONFIGURATION_ERROR, "Error while generating index name", kaex);
        }
        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(newIndex);

        ObjectNode metricsMapping = null;
        Map<String, Metric> diffs = null;

        synchronized (Schema.class) {
            // Update mappings only if a metric is new (not in cache)
            diffs = getMessageMappingDiffs(currentMetadata, metrics);
            if (diffs == null || diffs.isEmpty()) {
                return;
            }
            metricsMapping = getNewMessageMappingsBuilder(diffs);
        }

        LOG.trace("Sending dynamic message mappings: " + metricsMapping);
        DatastoreClientFactory.getInstance().putMapping(new TypeDescriptor(currentMetadata.getDataIndexName(), MessageSchema.MESSAGE_TYPE_NAME), metricsMapping);
    }

    private ObjectNode getNewMessageMappingsBuilder(Map<String, Metric> esMetrics) throws DatamodelMappingException {
        if (esMetrics == null) {
            return null;
        }
        // metrics mapping container (to be added to message mapping)
        ObjectNode typeNode = SchemaUtil.getObjectNode(); // root
        ObjectNode messageNode = SchemaUtil.getObjectNode(); // message
        ObjectNode typePropertiesNode = SchemaUtil.getObjectNode(); // properties
        ObjectNode metricsNode = SchemaUtil.getObjectNode(); // metrics
        ObjectNode metricsPropertiesNode = SchemaUtil.getObjectNode(); // properties (metric properties)
        typeNode.set(SchemaKeys.FIELD_NAME_MESSAGE, messageNode);
        messageNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, typePropertiesNode);
        typePropertiesNode.set(SchemaKeys.FIELD_NAME_METRICS, metricsNode);
        metricsNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, metricsPropertiesNode);

        // metrics mapping
        ObjectNode metricMapping;
        for (Entry<String, Metric> esMetric : esMetrics.entrySet()) {
            Metric metric = esMetric.getValue();
            metricMapping = SchemaUtil.getField(new KeyValueEntry[]{ new KeyValueEntry(SchemaKeys.KEY_DYNAMIC, SchemaKeys.VALUE_TRUE) });

            ObjectNode matricMappingPropertiesNode = SchemaUtil.getObjectNode(); // properties (inside metric name)
            ObjectNode valueMappingNode;

            switch (metric.getType()) {
                case SchemaKeys.TYPE_STRING:
                    valueMappingNode = SchemaUtil
                            .getField(new KeyValueEntry[]{ new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
                    break;
                case SchemaKeys.TYPE_DATE:
                    valueMappingNode = SchemaUtil.getField(
                            new KeyValueEntry[]{ new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
                    break;
                default:
                    valueMappingNode = SchemaUtil.getField(new KeyValueEntry[]{ new KeyValueEntry(SchemaKeys.KEY_TYPE, metric.getType()) });
                    break;
            }

            matricMappingPropertiesNode.set(DatastoreUtils.getClientMetricFromAcronym(metric.getType()), valueMappingNode);
            metricMapping.set(SchemaKeys.FIELD_NAME_PROPERTIES, matricMappingPropertiesNode);
            metricsPropertiesNode.set(metric.getName(), metricMapping);
        }
        return typeNode;
    }

    private Map<String, Metric> getMessageMappingDiffs(Metadata currentMetadata, Map<String, Metric> esMetrics) {
        if (esMetrics == null || esMetrics.isEmpty()) {
            return null;
        }

        Map<String, Metric> diffs = null;
        for (Entry<String, Metric> esMetric : esMetrics.entrySet()) {
            if (!currentMetadata.getMessageMappingsCache().containsKey(esMetric.getKey())) {
                if (diffs == null) {
                    diffs = new HashMap<>(100);
                }
                currentMetadata.getMessageMappingsCache().put(esMetric.getKey(), esMetric.getValue());
                diffs.put(esMetric.getKey(), esMetric.getValue());
            }
        }

        return diffs;
    }

    private ObjectNode getMappingSchema(String idxName) throws DatamodelMappingException {
        String idxRefreshInterval = String.format("%ss", DatastoreSettings.getInstance().getLong(DatastoreSettingKey.INDEX_REFRESH_INTERVAL));
        Integer idxShardNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingKey.INDEX_SHARD_NUMBER, 1);
        Integer idxReplicaNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingKey.INDEX_REPLICA_NUMBER, 0);

        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode refreshIntervaleNode = SchemaUtil.getField(new KeyValueEntry[]{
                new KeyValueEntry(SchemaKeys.KEY_REFRESH_INTERVAL, idxRefreshInterval),
                new KeyValueEntry(SchemaKeys.KEY_SHARD_NUMBER, idxShardNumber),
                new KeyValueEntry(SchemaKeys.KEY_REPLICA_NUMBER, idxReplicaNumber) });

        rootNode.set(SchemaKeys.KEY_INDEX, refreshIntervaleNode);
        LOG.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", idxName, idxRefreshInterval, idxShardNumber, idxReplicaNumber);
        return rootNode;
    }

}
