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
package org.eclipse.kapua.model.type;

import java.util.Date;

public class ObjectTypeConverter {

    private static final String TYPE_STRING = "string";
    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_INT = "int";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_BOOLEAN = "boolean";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_BINARY = "binary";

    private ObjectTypeConverter() {
    }

    public static String toString(Class<?> clazz) {
        String value = null;
        if (clazz != null) {
            if (clazz == String.class) {
                value = TYPE_STRING;
            } else if (clazz == Integer.class) {
                value = TYPE_INTEGER;
            } else if (clazz == Long.class) {
                value = TYPE_LONG;
            } else if (clazz == Float.class) {
                value = TYPE_FLOAT;
            } else if (clazz == Double.class) {
                value = TYPE_DOUBLE;
            } else if (clazz == Boolean.class) {
                value = TYPE_BOOLEAN;
            } else if (clazz == Date.class) {
                value = TYPE_DATE;
            } else if (clazz == byte[].class || clazz == Byte[].class) {
                value = TYPE_BINARY;
            } else {
                value = clazz.getName();
            }
        }
        return value;
    }

    public static Class<?> fromString(String value) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (value != null) {
            if (TYPE_STRING.equals(value)) {
                clazz = String.class;
            } else if (TYPE_INTEGER.equals(value) || TYPE_INT.equals(value)) {
                clazz = Integer.class;
            } else if (TYPE_LONG.equals(value)) {
                clazz = Long.class;
            } else if (TYPE_FLOAT.equals(value)) {
                clazz = Float.class;
            } else if (TYPE_DOUBLE.equals(value)) {
                clazz = Double.class;
            } else if (TYPE_BOOLEAN.equals(value)) {
                clazz = Boolean.class;
            } else if (TYPE_DATE.equals(value)) {
                clazz = Date.class;
            } else if (TYPE_BINARY.equals(value)) {
                clazz = byte[].class;
            } else {
                clazz = Class.forName(value);
            }
        }
        return clazz;
    }

}
