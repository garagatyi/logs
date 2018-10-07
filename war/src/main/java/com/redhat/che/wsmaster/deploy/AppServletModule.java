/*
 * Copyright (c) 2018-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package com.redhat.che.wsmaster.deploy;

import com.google.inject.servlet.ServletModule;
import org.eclipse.che.commons.logback.filter.RequestIdLoggerFilter;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.multiuser.keycloak.server.deploy.KeycloakServletModule;
import org.eclipse.che.multiuser.machine.authentication.server.MachineLoginFilter;
import org.everrest.guice.servlet.GuiceEverrestServlet;

@DynaModule
public class AppServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    filter("/*").through(RequestIdLoggerFilter.class);

    // Matching group SHOULD contain forward slash.
    serveRegex("^(?!/output.?)(.*)").with(GuiceEverrestServlet.class);
    filter("/*").through(MachineLoginFilter.class);
    install(new KeycloakServletModule());
  }
}
