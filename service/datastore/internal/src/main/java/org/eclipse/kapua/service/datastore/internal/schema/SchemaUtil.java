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
package org.eclipse.kapua.service.datastore.internal.schema;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.model.StorableId;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Schema utility class
 *
 * @since 1.0
 */
public class SchemaUtil {

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private static final String UNSUPPORTED_OBJECT_TYPE_ERROR_MSG = "The conversion of object [%s] is not supported!";
    private static final String NOT_VALID_OBJECT_TYPE_ERROR_MSG = "Cannot convert date [%s]";

    private SchemaUtil() {

    }

    /**
     * Return a map of map. The contained map has, as entries, the couples subKeys-values.<br>
     * <b>NOTE! No arrays subKeys-values coherence will be done (length or null check)!</b>
     *
     * @param key
     * @param subKeys
     * @param values
     * @return
     */
    public static Map<String, Object> getMapOfMap(String key, String[] subKeys, String[] values) {
        Map<String, String> mapChildren = new HashMap<>();
        for (int i = 0; i < subKeys.length; i++) {
            mapChildren.put(subKeys[i], values[i]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(key, mapChildren);
        return map;
    }

    /**
     * Get the Elasticsearch data index name
     *
     * @param scopeId
     * @return
     */
    public static String getDataIndexName(KapuaId scopeId) {
        return DatastoreUtils.getDataIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     *
     * @param scopeId
     * @return
     */
    public static String getKapuaIndexName(KapuaId scopeId) {
        return DatastoreUtils.getRegistryIndexName(scopeId);
    }

    /**
     * Create a new object node with the provided fields/values
     *
     * @param entries
     * @return
     * @throws DatamodelMappingException
     */
    public static ObjectNode getField(KeyValueEntry[] entries) throws DatamodelMappingException {
        ObjectNode rootNode = FACTORY.objectNode();
        for (int i = 0; i < entries.length; i++) {
            appendField(rootNode, entries[i].getKey(), entries[i].getValue());
        }
        return rootNode;
    }

    /**
     * Create a new object node with the provided field/value
     *
     * @param name
     * @param value
     * @return
     * @throws DatamodelMappingException
     */
    public static ObjectNode getField(String name, Object value) throws DatamodelMappingException {
        ObjectNode rootNode = FACTORY.objectNode();
        appendField(rootNode, name, value);
        return rootNode;
    }

    /**
     * Append the provided field/value to the object node
     *
     * @param node
     * @param name
     * @param value
     * @throws DatamodelMappingException
     */
    public static void appendField(ObjectNode node, String name, Object value) throws DatamodelMappingException {
        if (value instanceof String) {
            node.set(name, FACTORY.textNode((String) value));
        } else if (value instanceof Boolean) {
            node.set(name, FACTORY.booleanNode((Boolean) value));
        } else if (value instanceof Integer) {
            node.set(name, FACTORY.numberNode((Integer) value));
        } else if (value instanceof Long) {
            node.set(name, FACTORY.numberNode((Long) value));
        } else if (value instanceof Double) {
            node.set(name, FACTORY.numberNode((Double) value));
        } else if (value instanceof Float) {
            node.set(name, FACTORY.numberNode((Float) value));
        } else if (value instanceof byte[]) {
            node.set(name, FACTORY.binaryNode((byte[]) value));
        } else if (value instanceof byte[]) {
            node.set(name, FACTORY.binaryNode((byte[]) value));
        } else if (value instanceof Date) {
            try {
                node.set(name, FACTORY.textNode(KapuaDateUtils.formatDate((Date) value)));
            } catch (ParseException e) {
                throw new DatamodelMappingException(String.format(NOT_VALID_OBJECT_TYPE_ERROR_MSG, value), e);
            }
        } else if (value instanceof StorableId) {
            node.set(name, FACTORY.textNode(((StorableId) value).toString()));
        } else {
            throw new DatamodelMappingException(String.format(UNSUPPORTED_OBJECT_TYPE_ERROR_MSG, value != null ? value.getClass() : "null"));
        }
    }

    /**
     * Create a new object node
     *
     * @return
     */
    public static ObjectNode getObjectNode() {
        return FACTORY.objectNode();
    }

    /**
     * Create a new numeric node
     *
     * @param number
     * @return
     */
    public static NumericNode getNumericNode(long number) {
        return FACTORY.numberNode(number);
    }

    /**
     * Create a new array node
     *
     * @return
     */
    public static ArrayNode getArrayNode() {
        return FACTORY.arrayNode();
    }

    /**
     * Create a new text node
     *
     * @param value
     * @return
     */
    public static TextNode getTextNode(String value) {
        return FACTORY.textNode(value);
    }

    /**
     * Convert the provided array to an array node
     *
     * @param fields
     * @return
     */
    public static ArrayNode getAsArrayNode(String[] fields) {
        ArrayNode rootNode = FACTORY.arrayNode(fields.length);
        for (String str : fields) {
            rootNode.add(str);
        }
        return rootNode;
    }

}
