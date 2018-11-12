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
package org.eclipse.kapua.app.console.module.user.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;

public class UserFilterPanel extends EntityFilterPanel<GwtUser> {

    private static final ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtUser> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> nameField;
    private final KapuaTextField<String> phoneNumberField;
    private final SimpleComboBox<GwtUser.GwtUserStatus> statusCombo;
    private final KapuaDateField expirationDate;
    private final KapuaTextField<String> emailField;
    private final KapuaTextField<String> displayNameField;


    public UserFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(USER_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label clientIdLabel = new Label(USER_MSGS.filterFieldUsernameLabel());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(clientIdLabel);

        nameField = new KapuaTextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setMaxLength(MAX_LEN);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(nameField);

        Label phoneNumberLabel = new Label(USER_MSGS.filterFieldPhoneNumber());
        phoneNumberLabel.setWidth(WIDTH);
        phoneNumberLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(phoneNumberLabel);

        phoneNumberField = new KapuaTextField<String>();
        phoneNumberField.setName("phoneNumber");
        phoneNumberField.setWidth(WIDTH);
        phoneNumberField.setMaxLength(MAX_LEN);
        phoneNumberField.setStyleAttribute("margin-top", "0px");
        phoneNumberField.setStyleAttribute("margin-left", "5px");
        phoneNumberField.setStyleAttribute("margin-right", "5px");
        phoneNumberField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(phoneNumberField);

        Label userStatusLabel = new Label(USER_MSGS.filterFieldStatusLabel());
        userStatusLabel.setWidth(WIDTH);
        userStatusLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(userStatusLabel);

        statusCombo = new SimpleComboBox<GwtUser.GwtUserStatus>();
        statusCombo.setName("status");
        statusCombo.setWidth(WIDTH);
        statusCombo.setStyleAttribute("margin-top", "0px");
        statusCombo.setStyleAttribute("margin-left", "5px");
        statusCombo.setStyleAttribute("margin-right", "5px");
        statusCombo.setStyleAttribute("margin-bottom", "10px");
        statusCombo.add(GwtUser.GwtUserStatus.ANY);
        statusCombo.add(GwtUser.GwtUserStatus.ENABLED);
        statusCombo.add(GwtUser.GwtUserStatus.DISABLED);
        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        statusCombo.setSimpleValue(GwtUser.GwtUserStatus.ANY);

        fieldsPanel.add(statusCombo);

        Label expirationDateLabel = new Label(USER_MSGS.filterFieldExpirationDate());
        expirationDateLabel.setWidth(WIDTH);
        expirationDateLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(expirationDateLabel);

        expirationDate = new KapuaDateField();
        expirationDate.setName("expirationDate");
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setMaxLength(10);
        expirationDate.setWidth(WIDTH);
        expirationDate.setStyleAttribute("margin-top", "0px");
        expirationDate.setStyleAttribute("margin-left", "5px");
        expirationDate.setStyleAttribute("margin-right", "5px");
        expirationDate.setStyleAttribute("margin-bottom", "10px");

        fieldsPanel.add(expirationDate);

        Label emailLabel = new Label(USER_MSGS.filterFieldEmail());
        emailLabel.setWidth(WIDTH);
        emailLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(emailLabel);

        emailField = new KapuaTextField<String>();
        emailField.setName("email");
        emailField.setWidth(WIDTH);
        emailField.setMaxLength(MAX_LEN);
        emailField.setStyleAttribute("margin-top", "0px");
        emailField.setStyleAttribute("margin-left", "5px");
        emailField.setStyleAttribute("margin-right", "5px");
        emailField.setStyleAttribute("margin-bottom", "10px");

        fieldsPanel.add(emailField);

        Label displayNameLabel = new Label(USER_MSGS.filterFieldDisplayName());
        displayNameLabel.setWidth(WIDTH);
        displayNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(displayNameLabel);

        displayNameField = new KapuaTextField<String>();
        displayNameField.setName("displayName");
        displayNameField.setWidth(WIDTH);
        displayNameField.setMaxLength(MAX_LEN);
        displayNameField.setStyleAttribute("margin-top", "0px");
        displayNameField.setStyleAttribute("margin-left", "5px");
        displayNameField.setStyleAttribute("margin-right", "5px");
        displayNameField.setStyleAttribute("margin-bottom", "10px");

        fieldsPanel.add(displayNameField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        statusCombo.setSimpleValue(GwtUser.GwtUserStatus.ANY);
        displayNameField.setValue(null);
        phoneNumberField.setValue(null);
        expirationDate.setValue(null);
        emailField.setValue(null);
        GwtUserQuery query = new GwtUserQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtUserQuery query = new GwtUserQuery();
        query.setName(nameField.getValue());
        query.setUserStatus(statusCombo.getSimpleValue().toString());
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setDisplayName(displayNameField.getValue());
        query.setPhoneNumber(phoneNumberField.getValue());
        query.setExpirationDate(expirationDate.getValue());
        query.setEmail(emailField.getValue());
        entityGrid.refresh(query);
    }

}
