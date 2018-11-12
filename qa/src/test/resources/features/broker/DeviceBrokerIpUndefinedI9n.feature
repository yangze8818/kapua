###############################################################################
# Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@broker
Feature: Device Broker connection ip not set
  Device Service integration scenarios with running broker service.

  @StartDatastore
  Scenario: Start datastore for all scenarios

  @StartBroker
  Scenario: Start broker for all scenarios

  Scenario: Send BIRTH message and then DC message while broker ip is NOT set
  Effectively this is connect and disconnect of Kura device.
  Basic birth - death scenario. Scenario should fail as broker ip is not set
  as it should be.

    When I start the Kura Mock
    And Device birth message is sent
    And I wait 5 seconds for system to receive and process that message
# TODO Remove this when implementation without local interfaces scan is ready
#    And I login as user with name "kapua-sys" and password "kapua-password"
#    Then Device is connected with "192.168.33.10" server ip
#    Then An exception was thrown
    And I logout
    And Device death message is sent

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios
