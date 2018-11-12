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
package org.eclipse.kapua.app.console.module.device.client.device.packages;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages.GwtPackageUninstallRequest;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceManagementServiceAsync;

public class PackageUninstallDialog extends SimpleDialog {

    protected static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);
    private static final GwtDeviceManagementServiceAsync GWT_DEVICE_MANAGEMENT_SERVICE = GWT.create(GwtDeviceManagementService.class);
    private static final int FORM_LABEL_WIDTH = 150;

    private String scopeId;
    private String deviceId;
    private GwtDeploymentPackage selectedDeploymentPackage;

    private FormPanel operationOptionsForm;
    private CheckBox operationReboot;
    private NumberField operationRebootDelay;

    public PackageUninstallDialog(String scopeId, String deviceId, GwtDeploymentPackage selectedDeploymentPackage) {

        this.scopeId = scopeId;
        this.deviceId = deviceId;
        this.selectedDeploymentPackage = selectedDeploymentPackage;

        DialogUtils.resizeDialog(this, 400, 210);
    }

    @Override
    public void createBody() {
        FormData formData = new FormData("-10");

        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(FORM_LABEL_WIDTH);

        operationOptionsForm = new FormPanel();
        operationOptionsForm.setFrame(false);
        operationOptionsForm.setHeaderVisible(false);
        operationOptionsForm.setBodyBorder(false);
        operationOptionsForm.setBorders(false);
        operationOptionsForm.setLayout(formLayout);

        operationReboot = new CheckBox();
        operationReboot.setName("operationReboot");
        operationReboot.setFieldLabel(DEVICE_MSGS.deviceUnistallAsyncUninstallReboot());
        operationReboot.setBoxLabel("");
        operationReboot.setValue(false);
        operationOptionsForm.add(operationReboot, formData);

        operationReboot.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (operationReboot.getValue()) {
                    operationRebootDelay.enable();
                } else {
                    operationRebootDelay.disable();
                }
            }
        });

        operationRebootDelay = new NumberField();
        operationRebootDelay.setName("operationRebootDelay");
        operationRebootDelay.setFieldLabel(DEVICE_MSGS.deviceUnistallAsyncUninstallRebootDelay());
        operationRebootDelay.setEmptyText("0");
        operationRebootDelay.setAllowDecimals(false);
        operationRebootDelay.setAllowNegative(false);
        operationRebootDelay.disable();
        operationOptionsForm.add(operationRebootDelay, formData);

        bodyPanel.add(operationOptionsForm);
    }

    @Override
    public void submit() {
        GwtPackageUninstallRequest gwtPackageUninstallRequest = new GwtPackageUninstallRequest();

        // General info
        gwtPackageUninstallRequest.setScopeId(scopeId);
        gwtPackageUninstallRequest.setDeviceId(deviceId);

        // Operation configuration
        gwtPackageUninstallRequest.setPackageName(selectedDeploymentPackage.getName());
        gwtPackageUninstallRequest.setPackageVersion(selectedDeploymentPackage.getVersion());

        gwtPackageUninstallRequest.setReboot(operationReboot.getValue());

        Number nValue = operationRebootDelay.getValue();
        if (nValue != null) {
            gwtPackageUninstallRequest.setRebootDelay(nValue.intValue() * 1000);
        }

        GWT_DEVICE_MANAGEMENT_SERVICE.uninstallPackage(xsrfToken, gwtPackageUninstallRequest, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                exitStatus = true;
                exitMessage = DEVICE_MSGS.packageUninstallAsyncSuccess();
                hide();
            }

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                exitMessage = caught.getMessage();
                hide();
            }
        });
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public String getHeaderMessage() {
        return DEVICE_MSGS.packageUninstallPackage();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return new KapuaIcon(IconSet.QUESTION_CIRCLE);
    }

    @Override
    public String getInfoMessage() {
        return DEVICE_MSGS.packageUninstallPackageInfo(selectedDeploymentPackage.getName());
    }

    @Override
    public String getSubmitButtonText() {
        return DEVICE_MSGS.packageUninstallButton();
    }
}
