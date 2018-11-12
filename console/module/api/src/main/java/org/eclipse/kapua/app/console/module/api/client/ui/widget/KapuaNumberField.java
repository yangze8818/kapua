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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.user.client.Element;

public class KapuaNumberField extends NumberField {

    @Override
    public void setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        if (rendered) {
            getInputEl().setElementAttribute("maxLength", maxLength);
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength());
    }

}
