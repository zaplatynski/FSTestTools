/*
 * ********************************************************************
 * Copyright (C) 2017 e-Spirit AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ********************************************************************
 */

package com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.context.TestContext;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.context.TestGenerationContext;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.ConnectionManager;
import de.espirit.firstspirit.access.GenerationContext;
import de.espirit.firstspirit.agency.BrokerAgent;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.common.MaximumNumberOfSessionsExceededException;
import de.espirit.firstspirit.server.authentication.AuthenticationException;

import org.apache.commons.lang.StringUtils;
import org.junit.rules.ExternalResource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * The type First spirit connection rule.
 */
public class FirstSpiritConnectionRule extends ExternalResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstSpiritConnectionRule.class);
    private static final String ADMIN = "Admin";
    private static final String SYS_PROP_PORT = "port";
    private static final String SYS_PROP_HOST = "host";
    private static final String SYS_PROP_LOGIN = "login";
    private static final String SYS_PROP_PASSWORD = "password"; //NOSONAR
    private static final String NOT_CONNECTED_TO_FS = "not connected to FirstSpirit server";
    public static final String FAILED_TO_MODIFY_TEMPLATE = "Failed to modify template: ";

    private Connection connection;
    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private final ConnectionMode mode;
    private Map<String, FsConnRuleCommand<FsConnRuleCmdParamBean, FsConnRuleCmdResultBean>> commands;

    /**
     * Instantiates a new First spirit connection rule by using SystemProperties and socket mode.
     */
    public FirstSpiritConnectionRule() {
        this(System.getProperty(SYS_PROP_HOST, "localhost"),
             System.getProperty(SYS_PROP_PORT, ConnectionMode.SOCKET.getDefaultPortAsStr()),
             ConnectionMode.SOCKET,
             System.getProperty(SYS_PROP_LOGIN, ADMIN),
             System.getProperty(SYS_PROP_PASSWORD, ADMIN));
    }

    /**
     * Instantiates a new First spirit connection rule.
     *
     * @param mode the mode
     */
    public FirstSpiritConnectionRule(final ConnectionMode mode) {
        this(System.getProperty(SYS_PROP_HOST, "localhost"),
             System.getProperty(SYS_PROP_PORT, mode.getDefaultPortAsStr()),
             Objects.requireNonNull(mode,"mode can not be null"),
             System.getProperty(SYS_PROP_LOGIN, ADMIN),
             System.getProperty(SYS_PROP_PASSWORD, ADMIN));
    }

    /**
     * Instantiates a new First spirit connection rule.
     *
     * @param host     the host, e.g. localhost
     * @param port     the port, e.g. 1088
     * @param mode     the mode
     * @param login    the login, e.g. Admin
     * @param password the password, e.g. Admin
     */
    public FirstSpiritConnectionRule(final String host, final String port, final ConnectionMode mode, final String login, final String password) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("host can not be null or empty");
        }
        if (StringUtils.isBlank(port)) {
            throw new IllegalArgumentException("port can not be null or empty");
        }
        if (!StringUtils.isNumeric(port)) {
            throw new IllegalArgumentException("port must be numeric");
        }
        if (StringUtils.isBlank(login)) {
            throw new IllegalArgumentException("login can not be null or empty");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("password can not be null or empty");
        }
        this.host = host;
        this.port = Integer.parseInt(port);
        this.mode = Objects.requireNonNull(mode, "mode can not be null");
        this.login = login;
        this.password = password;
        commands = new ConcurrentHashMap<>();
        populateMap(commands);
    }

    private static void populateMap(final Map<String, FsConnRuleCommand<FsConnRuleCmdParamBean, FsConnRuleCmdResultBean>> commands) {
        final String commandPackage = "com.espirit.moddev.fstesttools.rules.firstspirit.commands";
        LOGGER.info("Scanning class path in '{}' for command classes...", commandPackage);
        Reflections reflections = new Reflections(commandPackage);
        Set<Class<? extends FsConnRuleCommand>> commandsFromPackage = reflections.getSubTypesOf(FsConnRuleCommand.class);

        int counter = 0;
        for (Class<? extends FsConnRuleCommand> commandClass : commandsFromPackage) {
            if (commandClass.isAnonymousClass()) {
                continue;
            }
            LOGGER.debug("Processing '{}'...", commandClass.getSimpleName());
            if (commandClass.isEnum()) {
                final FsConnRuleCommand[] enumCommands = commandClass.getEnumConstants();
                for (FsConnRuleCommand enumCommand : enumCommands) {
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Add enum command: {}", enumCommand.name());
                    }
                    counter++;
                    commands.put(enumCommand.name(), enumCommand);
                }
            }
            if (!commandClass.isEnum()) {
                final FsConnRuleCommand command;
                try {
                    command = commandClass.newInstance();
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Add class command: {}", command.name());
                    }
                    counter++;
                    commands.put(command.name(), command);
                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("Loaded {} commands!", counter);
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public ConnectionMode getMode() {
        return mode;
    }

    @Override
    public void before() throws Throwable {
        LOGGER.info("Connect to FirstSpirit on '{}' with port '{}' ({}/{})...", new Object[]{host, port, login, password});
        connection = obtainConnection();
        assertThat("FirstSpirit ConnectionManager returned null-connection!", connection, is(notNullValue()));
        try {
            connection.connect();
            LOGGER.info("Connection to FirstSpirit is successful!");
        } catch (final MaximumNumberOfSessionsExceededException e) {
            LOGGER.error("Maximum number of sessions exceeded", e);
            fail(e.toString());
        } catch (final IOException e) {
            LOGGER.error("I/O error: " + e.getMessage(), e);
            fail(e.toString());
        } catch (final AuthenticationException e) {
            LOGGER.error("Authentication failure!", e);
            fail(e.toString());
        }
        assertThat("FirstSpirit ConnectionManager could not connect to server!", connection.isConnected(), is(Boolean.TRUE));
    }

    /**
     * Obtain FirstSpirit connection. Used in Tests.
     *
     * @return the connection
     */
    protected Connection obtainConnection() {
        return ConnectionManager.getConnection(host, port, mode.getCode(), login, password);
    }

    @Override
    public void after() {
        try {
            connection.disconnect();
        } catch (final IOException e) {
            LOGGER.error("Error closing FirstSpirit connection: " + e.getMessage(), e);
            fail(e.toString());
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException(NOT_CONNECTED_TO_FS);
        }
        return connection;
    }

    /**
     * Gets base context for the current project.
     *
     * @param projectName the project name
     * @return the base context for the current project
     */
    public BaseContext getBaseContextForCurrentProject(final String projectName) {
        final BrokerAgent brokerAgent = requireSpecialist(BrokerAgent.TYPE);
        final SpecialistsBroker specialistsBrokerByProject = brokerAgent.getBrokerByProjectName(projectName);
        return new TestContext(specialistsBrokerByProject);
    }

    /**
     * Gets generation context for current project.
     *
     * @param projectName the project name
     * @return the generation context for current project
     */
    public GenerationContext getGenerationContextForCurrentProject(final String projectName) {
        final BrokerAgent brokerAgent = requireSpecialist(BrokerAgent.TYPE);
        final SpecialistsBroker specialistsBrokerByProject = brokerAgent.getBrokerByProjectName(projectName);
        return new TestGenerationContext(specialistsBrokerByProject);
    }

    /**
     * Get a SpecialistBroker that is not bound to a project from the current FirstSpirit connection.
     *
     * @return the SpecialistBroker
     */
    public SpecialistsBroker getBroker() {
        if (connection == null) {
            throw new IllegalStateException(NOT_CONNECTED_TO_FS);
        }
        return connection.getBroker();
    }

    /**
     * Request specialist from current FirstSpirit connection.
     *
     * @param <S>  the type parameter for the specialist.
     * @param type the type of the specialist.
     * @return the specialist or null if not available
     */
    public <S> S requestSpecialist(final SpecialistType<S> type) {
        return getBroker().requestSpecialist(type);
    }

    /**
     * Require specialist from current FirstSpirit connection.
     *
     * @param <S>  the type parameter for the specialist.
     * @param type the type of the specialist.
     * @return the non null specialist or throws a runtime exception
     */
    public <S> S requireSpecialist(final SpecialistType<S> type) {
        return getBroker().requireSpecialist(type);
    }


    /**
     * Invoke a command to carry out client actions with the FirstSpirit server. The parameter bean accepts @Inject annotations for FirstSpirit
     * objects such as Connection, SepecialistBroker, BaseContext and GenerationContext.
     *
     * @param <P>        the type parameter for the parameter bean class
     * @param <R>        the type parameter for the result bean class
     * @param name       the name of the command to carry out
     * @param parameters the parameter bean instance
     * @return the command's result
     * @see Connection
     * @see BaseContext
     */
    public <P extends FsConnRuleCmdParamBean, R extends FsConnRuleCmdResultBean> R invokeCommand(final String name, P parameters) {
        final FsConnRuleCommand<FsConnRuleCmdParamBean, FsConnRuleCmdResultBean> command = commands.get(name);
        P injectedParameters = injectFsObjects(parameters);
        return (R) command.execute(injectedParameters);
    }

    private <P extends FsConnRuleCmdParamBean> P injectFsObjects(final P parameters) {
        final Field[] declaredFields = parameters.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Inject.class)) {
                final Class<?> type = declaredField.getType();
                final boolean accessible = declaredField.isAccessible();
                injectIntoFields(parameters, declaredField, type, accessible);
            }
        }
        return parameters;
    }

    private <P extends FsConnRuleCmdParamBean> void injectIntoFields(final P parameters, final Field declaredField, final Class<?> type,
                                                                     final boolean accessible) {
        if (!accessible) {
            declaredField.setAccessible(true);
        }
        try {
            if (Objects.equals(type, Connection.class)) {
                declaredField.set(parameters, getConnection());
            }
            if (Objects.equals(type, SpecialistsBroker.class)) {
                declaredField.set(parameters, getBroker());
            }
            if (Objects.equals(type, BaseContext.class)) {
                declaredField.set(parameters, getBaseContextForCurrentProject(parameters.getProjectName()));
            }
            if (Objects.equals(type, GenerationContext.class)) {
                declaredField.set(parameters, getGenerationContextForCurrentProject(parameters.getProjectName()));
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        declaredField.setAccessible(accessible);
    }
}
