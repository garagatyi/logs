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

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.eclipse.che.api.system.server.ServiceTermination;
import org.eclipse.che.api.system.server.SystemModule;
import org.eclipse.che.commons.auth.token.ChainedTokenExtractor;
import org.eclipse.che.commons.auth.token.RequestTokenExtractor;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.multiuser.api.permission.server.HttpPermissionCheckerImpl;
import org.eclipse.che.multiuser.api.permission.server.PermissionChecker;
import org.eclipse.che.multiuser.api.permission.server.jsonrpc.RemoteSubscriptionPermissionManager;
import org.eclipse.che.multiuser.keycloak.server.deploy.KeycloakModule;
import org.eclipse.che.multiuser.machine.authentication.server.MachineAuthModule;
import org.eclipse.che.multiuser.permission.workspace.server.filters.WorkspaceRemoteSubscriptionPermissionFilter;

@DynaModule
public class Module extends AbstractModule {
  @Override
  protected void configure() {
    // System-wide configuration
    bind(org.eclipse.che.api.core.rest.ApiInfoService.class);
    install(new org.eclipse.che.api.core.util.FileCleaner.FileCleanerModule());
    install(new org.eclipse.che.commons.schedule.executor.ScheduleModule());
    install(new SystemModule());
    Multibinder<ServiceTermination> terminationMultiBinder =
        Multibinder.newSetBinder(binder(), ServiceTermination.class);
    terminationMultiBinder
        .addBinding()
        .to(org.eclipse.che.api.system.server.CronThreadPullTermination.class);

    // Websocket/JSON_RPC configuration
    install(new org.eclipse.che.api.core.websocket.impl.WebSocketModule());
    install(new org.eclipse.che.api.core.jsonrpc.impl.JsonRpcModule());

    // output
    install(new org.eclipse.che.api.logger.deploy.LoggerModule());
    bind(org.eclipse.che.api.workspace.server.event.InstallerLogJsonRpcMessenger.class)
        .asEagerSingleton();
    bind(org.eclipse.che.api.workspace.server.event.RuntimeLogJsonRpcMessenger.class)
        .asEagerSingleton();

    // permissions
    bind(RemoteSubscriptionPermissionManager.class).asEagerSingleton();
    bind(
        org.eclipse.che.multiuser.permission.system.SystemEventsSubscriptionPermissionsCheck.class);
    bind(WorkspaceRemoteSubscriptionPermissionFilter.class).asEagerSingleton();
    bind(org.eclipse.che.multiuser.permission.system.SystemServicePermissionsFilter.class);
    bind(
        org.eclipse.che.multiuser.permission.system.SystemEventsSubscriptionPermissionsCheck.class);
    bind(org.eclipse.che.multiuser.permission.logger.LoggerServicePermissionsFilter.class);

    // auth
    install(new KeycloakModule());
    install(new MachineAuthModule());
    bind(RequestTokenExtractor.class).to(ChainedTokenExtractor.class);

    bind(PermissionChecker.class).to(HttpPermissionCheckerImpl.class);
  }
}
