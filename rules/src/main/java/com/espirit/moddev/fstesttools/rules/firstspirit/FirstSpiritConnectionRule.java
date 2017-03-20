package com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing.ZipImportCommands;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing.ZipImportParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreCommand;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreResult;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule.ScheduleCommands;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule.ScheduleParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule.ScheduleResult;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.context.TestContext;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.context.TestGenerationContext;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.ConnectionManager;
import de.espirit.firstspirit.access.GenerationContext;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.schedule.RunState;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.pagestore.PageFolder;
import de.espirit.firstspirit.access.store.sitestore.PageRefFolder;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
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
@SuppressWarnings("squid:S1133")
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
             mode,
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
        if (mode == null) {
            throw new IllegalArgumentException("mode can not be null");
        }
        if (StringUtils.isBlank(login)) {
            throw new IllegalArgumentException("login can not be null or empty");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("password can not be null or empty");
        }
        this.host = host;
        this.port = Integer.parseInt(port);
        this.mode = mode;
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
            if(commandClass.isAnonymousClass()){
                continue;
            }
            LOGGER.debug("Processing '{}'...",commandClass.getSimpleName());
            if(commandClass.isEnum()){
                final FsConnRuleCommand[] enumCommands = commandClass.getEnumConstants();
                for (FsConnRuleCommand enumCommand : enumCommands) {
                    LOGGER.debug("Add enum command: {}", enumCommand.name());
                    counter++;
                    commands.put(enumCommand.name(),enumCommand);
                }
            }
            if(!commandClass.isEnum()){
                final FsConnRuleCommand command;
                try {
                    command = commandClass.newInstance();
                    LOGGER.debug("Add class command: {}", command.name());
                    counter++;
                    commands.put(command.name(), command);
                } catch (InstantiationException|IllegalAccessException e) {
                    LOGGER.error(e.getMessage(),e);
                }
            }
        }
        LOGGER.info("Loaded {} commands!",counter);
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
        connection = ConnectionManager.getConnection(host, port, mode.getCode(), login, password);
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
        final BrokerAgent brokerAgent = getBroker().requestSpecialist(BrokerAgent.TYPE);
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
        final BrokerAgent brokerAgent = getBroker().requestSpecialist(BrokerAgent.TYPE);
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
            if (type == Connection.class) {
                declaredField.set(parameters, getConnection());
            }
            if (type == SpecialistsBroker.class) {
                declaredField.set(parameters, getBroker());
            }
            if (type == BaseContext.class) {
                declaredField.set(parameters, getBaseContextForCurrentProject(parameters.getProjectName()));
            }
            if (type == GenerationContext.class) {
                declaredField.set(parameters, getGenerationContextForCurrentProject(parameters.getProjectName()));
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        declaredField.setAccessible(accessible);
    }

    /**
     * Execute schedule entry and await termination.
     *
     * @param projectName the project entryName
     * @param entryName   the entryName
     * @return the run state.
     * @deprecated
     */
    @Deprecated
    public RunState executeScheduleEntryAndAwaitTermination(final String projectName, final String entryName) {
        if (connection == null) {
            throw new IllegalStateException(NOT_CONNECTED_TO_FS);
        }
        final ScheduleResult result =
            (ScheduleResult) invokeCommand(ScheduleCommands.RUN_SCHEDULE.name(), new ScheduleParameters(projectName, entryName));
        return result.getScheduleRunState();
    }

    /**
     * Change deployment path of schedule entry.
     *
     * @param projectName the project name
     * @param entryName   the entry name
     * @param deployDir   the deployment directory
     * @deprecated
     */
    @Deprecated
    public void changeDeployPathOfScheduleEntry(final String projectName, final String entryName, final File deployDir) {
        final ScheduleParameters parameters = new ScheduleParameters(projectName, entryName);
        parameters.setDeployDir(deployDir);
        invokeCommand(ScheduleCommands.CHG_DEPLOY_DIR.name(), parameters);
    }

    /**
     * Creates a default scheduler.
     *
     * @param projectName the name of the fs project.
     * @param entryName   the name of the new scheduler.
     * @deprecated
     */
    @Deprecated
    public void createDefaultScheduler(final String projectName, final String entryName) {
        final ScheduleParameters parameters = new ScheduleParameters(projectName, entryName);
        createDefaultScheduler(parameters);
    }

    /**
     * Create default scheduler.
     *
     * @param parameters the parameters
     * @deprecated
     */
    @Deprecated
    public void createDefaultScheduler(final ScheduleParameters parameters) {
        invokeCommand(ScheduleCommands.GENERATE_SCHEDULE.name(), parameters);
    }

    /**
     * Creates a default scheduler.
     *
     * @param projectName   the name of the fs project.
     * @param entryName     the name of the new scheduler.
     * @param configuration hte scheduler configuration
     * @deprecated
     */
    @Deprecated
    public void createDefaultScheduler(final String projectName, final String entryName, final SchedulerConfiguration configuration) {
        final ScheduleParameters parameters = new ScheduleParameters(projectName, entryName);
        parameters.setDeployTaskType(configuration.getDeployTaskType());
        parameters.setGenerateDeleteDirectory(configuration.isGenerateDeleteDirectory());
        parameters.setGenerateUrlPrefix(configuration.getGenerateUrlPrefix());
        createDefaultScheduler(parameters);
    }

    /**
     * Gets content creator url for a project and user language.
     *
     * @param projectName the project name
     * @param language    the language
     * @return the content creator url
     */
    public String getContentCreatorUrl(final String projectName, final String language) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final long projectId = project.getId();
            return connection.getServerConfiguration().getUrl() + "fs5webedit_" + projectId + "/?locale=" + language + "&project=" + projectId;
        } else {
            return null;
        }
    }

    /**
     * Import format templates from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importFormatTemplatesFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_FORMAT_TEMPLATES.name(), parameters);
    }

    /**
     * Import page templates from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importPageTemplatesFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_PAGE_TEMPLATES.name(), parameters);
    }

    /**
     * Import link templates from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importLinkTemplatesFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_LINK_TEMPLATES.name(), parameters);
    }

    /**
     * Import media from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importMediaFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_MEDIA.name(), parameters);
    }

    /**
     * Import scripts from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importScriptsFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_SCRIPTS.name(), parameters);
    }

    /**
     * Import workflows from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     * @deprecated
     */
    @Deprecated
    public void importWorkflowsFromZip(final String projectName, final File zip) {
        ZipImportParameters parameters = new ZipImportParameters(projectName, zip);
        invokeCommand(ZipImportCommands.IMPORT_WORKFLOWS.name(), parameters);
    }


    /**
     * Creates a new pageStoreFolder.
     *
     * @param projectName the project name.
     * @param name        the folder name.
     * @return the created folder.
     * @deprecated
     */
    @Deprecated
    public PageFolder createPageStoreFolder(final String projectName, final String name) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, name);
        ModifyStoreResult result = invokeCommand(ModifyStoreCommand.CREATE_PAGESTORE_FOLDER.name(), parameters);
        return result.getPageFolder();
    }

    /**
     * Creates a new siteStoreFolder.
     *
     * @param projectName the fs project name.
     * @param name        the folder name.
     * @return the created folder.
     * @deprecated
     */
    @Deprecated
    public PageRefFolder createSiteStoreFolder(final String projectName, final String name) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, name);
        ModifyStoreResult result = invokeCommand(ModifyStoreCommand.CREATE_SITESTORE_FOLDER.name(), parameters);
        return result.getPageRefFolder();
    }


    /**
     * Create a new format template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template that will be created.
     * @param channel     the channel to write the content to.
     * @param content     the sourcecode of the template.
     * @return the new template.
     * @deprecated
     */
    @Deprecated
    public IDProvider createFormatTemplate(final String projectName, final String uid, final String channel, final String content) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, uid, channel, content);
        ModifyStoreResult result = invokeCommand(ModifyStoreCommand.CREATE_FORMAT_TEMPLATE.name(), parameters);
        return result.getFormatTemplate();
    }

    /**
     * Creates a script template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template that will be created.
     * @param channel     the channel to write the content to.
     * @param content     the sourcecode of the template.
     * @return the new template.
     * @deprecated
     */
    @Deprecated
    public IDProvider createScriptTemplate(final String projectName, final String uid, final String channel, final String content) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, uid, channel, content);
        ModifyStoreResult result = invokeCommand(ModifyStoreCommand.CREATE_SCRIPT_TEMPLATE.name(), parameters);
        return result.getScript();
    }

    /**
     * Modifies a page template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the page template.
     * @param channel     the channel to modify.
     * @param content     the content to set.
     * @deprecated
     */
    @Deprecated
    public void modifyPageTemplate(final String projectName, final String uid, final String channel, final String content) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, uid, channel, content);
        invokeCommand(ModifyStoreCommand.MODIFY_PAGE_TEMPLATE.name(), parameters);
    }

    /**
     * Modifies the specified format template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template to manipulate.
     * @param channel     the channel to use.
     * @param content     the sourcecode to set.
     * @deprecated
     */
    @Deprecated
    public void modifyFormatTemplate(final String projectName, final String uid, final String channel, final String content) {
        ModifyStoreParameters parameters = new ModifyStoreParameters(projectName, uid, channel, content);
        invokeCommand(ModifyStoreCommand.MODIFY_FORMAT_TEMPLATE.name(), parameters);
    }

}
