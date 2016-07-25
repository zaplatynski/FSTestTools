package com.espirit.moddev.fstesttools.rules.firstspirit;

import de.espirit.common.util.Listable;
import de.espirit.firstspirit.access.AdminService;
import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.ConnectionManager;
import de.espirit.firstspirit.access.GenerationContext;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.schedule.DeployTask;
import de.espirit.firstspirit.access.schedule.FileTarget;
import de.espirit.firstspirit.access.schedule.GenerateTask;
import de.espirit.firstspirit.access.schedule.RunState;
import de.espirit.firstspirit.access.schedule.ScheduleEntry;
import de.espirit.firstspirit.access.schedule.ScheduleEntryControl;
import de.espirit.firstspirit.access.schedule.ScheduleEntryRunningException;
import de.espirit.firstspirit.access.schedule.ScheduleStorage;
import de.espirit.firstspirit.access.schedule.ScheduleTask;
import de.espirit.firstspirit.access.schedule.TaskResult;
import de.espirit.firstspirit.access.store.ElementDeletedException;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.LockException;
import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.StoreElement;
import de.espirit.firstspirit.access.store.mediastore.MediaStoreRoot;
import de.espirit.firstspirit.access.store.pagestore.PageFolder;
import de.espirit.firstspirit.access.store.pagestore.PageStoreRoot;
import de.espirit.firstspirit.access.store.sitestore.PageRefFolder;
import de.espirit.firstspirit.access.store.sitestore.SiteStoreRoot;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplate;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplates;
import de.espirit.firstspirit.access.store.templatestore.PageTemplate;
import de.espirit.firstspirit.access.store.templatestore.Script;
import de.espirit.firstspirit.access.store.templatestore.Scripts;
import de.espirit.firstspirit.access.store.templatestore.Template;
import de.espirit.firstspirit.access.store.templatestore.TemplateStoreRoot;
import de.espirit.firstspirit.admin.FileTargetImpl;
import de.espirit.firstspirit.agency.BrokerAgent;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.common.MaximumNumberOfSessionsExceededException;
import de.espirit.firstspirit.server.authentication.AuthenticationException;
import de.espirit.firstspirit.server.scheduler.FileTargetDTO;
import de.espirit.firstspirit.server.scheduler.deploy.FileDeployTargetFactory;

import org.apache.commons.lang.StringUtils;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.ZipFile;

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

    /**
     * Execute schedule entry and await termination.
     *
     * @param projectName the project entryName
     * @param entryName   the entryName
     * @return the run state.
     */
    public RunState executeScheduleEntryAndAwaitTermination(final String projectName, final String entryName) {
        if (connection == null) {
            throw new IllegalStateException(NOT_CONNECTED_TO_FS);
        }
        final Project project = connection.getProjectByName(projectName);
        final UserService userService = project.getUserService();
        final AdminService ad = userService.getConnection().getService(AdminService.class);
        RunState scheduleResult = RunState.SUCCESS;
        try {
            final ScheduleEntry scheduleEntry = ad.getScheduleStorage().getScheduleEntry(project, entryName);
            LOGGER.info("Start execution of schedule entry '{}'...", entryName);
            final ScheduleEntryControl control = scheduleEntry.execute();
            control.awaitTermination();
            final StringBuilder sb = new StringBuilder();
            scheduleResult = getRunState(control, sb);
            logResult(entryName, scheduleResult, sb);
        } catch (final ScheduleEntryRunningException e) {
            LOGGER.error(e.getMessage(), e);
            fail(e.toString());
        } catch (final NullPointerException e) {
            LOGGER.error(e.getMessage() + ", perhaps the given schedule entry does not exist: '" + entryName + "'", e);
            fail(e.toString());
        }
        return scheduleResult;
    }

    private static void logResult(final String entryName, final RunState scheduleResult, final StringBuilder sb) {
        switch (scheduleResult) {
            case ERROR:
            case FINISHED_WITH_ERRORS:
                LOGGER.error("Finished execution of schedule entry '{}' with errors:\n{}", entryName, sb.toString());
                break;
            case NOT_STARTED:
            case ABORTED:
            case SUCCESS:
                LOGGER.info("Finished execution of schedule entry '{}' with success:\n{}", entryName, sb.toString());
                break;
            default:
                LOGGER.warn("Finished execution of schedule entry '{}' with warnings:\n{}", entryName, sb.toString());
        }
    }

    private static RunState getRunState(final ScheduleEntryControl control, final StringBuilder sb) {
        RunState scheduleResult = RunState.SUCCESS;
        final List<TaskResult> taskResults = control.getState().getTaskResults();
        for (final TaskResult result : taskResults) {
            sb.append("\t");
            sb.append(result.getTask().getName());
            sb.append(":\t");
            sb.append(result.getState());
            sb.append("\n");
            switch (result.getState()) {
                case NOT_STARTED:
                case ABORTED:
                case SUCCESS:
                    break;
                default:
                    scheduleResult = result.getState();
            }
        }
        return scheduleResult;
    }

    /**
     * Change deployment path of schedule entry.
     *
     * @param projectName the project name
     * @param entryName   the entry name
     * @param deployDir   the deployment directory
     */
    public void changeDeployPathOfScheduleEntry(final String projectName, final String entryName, final File deployDir) {
        final Project project = connection.getProjectByName(projectName);
        boolean found = false;
        if (project != null) {
            final UserService userService = project.getUserService();
            final AdminService ad = userService.getConnection().getService(AdminService.class);
            final ScheduleEntry scheduleEntry = ad.getScheduleStorage().getScheduleEntry(project, entryName);
            if (scheduleEntry != null) {
                final List<ScheduleTask> tasks = scheduleEntry.getTasks();
                for (final ScheduleTask task : tasks) {
                    if (task instanceof DeployTask) {
                        final DeployTask deployTask = (DeployTask) task;
                        final FileDeployTargetFactory factory = new FileDeployTargetFactory();
                        final FileTargetDTO dto = new FileTargetDTO();
                        dto.setTargetDirectory(deployDir.getAbsolutePath());
                        dto.setAppendDateToDirectoryName(false);
                        deployTask.setTarget(factory.create(deployTask, dto));
                        found = true;
                        LOGGER.info("Changed deploy path to '{}'", deployDir.getAbsolutePath());
                    }
                }
            }
        }
        if (!found) {
            LOGGER.warn("Did not change deploy path to '{}'!", deployDir.getAbsolutePath());
        }
    }

    /**
     * Creates a default scheduler.
     *
     * @param projectName the name of the fs project.
     * @param entryName   the name of the new scheduler.
     */
    public void createDefaultScheduler(final String projectName, final String entryName) {
        createDefaultScheduler(projectName, entryName, new SchedulerConfiguration());
    }

    /**
     * Creates a default scheduler.
     *
     * @param projectName   the name of the fs project.
     * @param entryName     the name of the new scheduler.
     * @param configuration hte scheduler configuration
     */
    public void createDefaultScheduler(final String projectName, final String entryName, final SchedulerConfiguration configuration) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final AdminService ad = userService.getConnection().getService(AdminService.class);

            try {
                // create scheduler
                final ScheduleStorage scheduleStorage = ad.getScheduleStorage();
                final ScheduleEntry scheduleEntry = scheduleStorage.createScheduleEntry(entryName);
                scheduleEntry.setProject(project);

                // add tasks
                scheduleEntry.getTasks().add(createGenerateTask(scheduleEntry, project, configuration));
                scheduleEntry.getTasks().add(createDeployTask(scheduleEntry, configuration));

                // save scheduler
                scheduleEntry.save();
                scheduleEntry.unlock();
            } catch (final RuntimeException e) {
                LOGGER.error("Failed to create default scheduler!", e);
            }
        }
    }

    private GenerateTask createGenerateTask(final ScheduleEntry scheduleEntry, final Project project, final SchedulerConfiguration configuration) {
        GenerateTask generateTask = null;
        try {
            generateTask = scheduleEntry.createTask(GenerateTask.class);
            generateTask.setDeleteDirectory(configuration.isGenerateDeleteDirectory());
            generateTask.setGenerateFlag(project.getMasterLanguage(), getTemplateSet(project, "html"), true);
            generateTask.setUrlPrefix(configuration.getGenerateUrlPrefix());
        } catch (final RuntimeException e) {
            LOGGER.error("Failed to create generate task!", e);
        }
        return generateTask;
    }

    private DeployTask createDeployTask(final ScheduleEntry scheduleEntry, final SchedulerConfiguration configuration) {
        DeployTask deployTask = null;
        try {
            deployTask = scheduleEntry.createTask(DeployTask.class);
            deployTask.setType(configuration.getDeployTaskType());

            final FileTarget target = deployTask.createTarget(FileTargetImpl.class);
            target.setPath("/tmp");
            target.setAppendDateToDirectoryName(false);
            deployTask.setTarget(target);
        } catch (final RuntimeException e) {
            LOGGER.error("Failed to create deploy task!", e);
        }
        return deployTask;
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
     */
    public void importFormatTemplatesFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            try {
                importFromZipIntoTargetStore(zip, templateStore.getFormatTemplates());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into format template store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }

        }
    }

    /**
     * Import page templates from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public void importPageTemplatesFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            try {
                importFromZipIntoTargetStore(zip, templateStore.getPageTemplates());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into page template store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
    }

    /**
     * Import link templates from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public void importLinkTemplatesFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            try {
                importFromZipIntoTargetStore(zip, templateStore.getLinkTemplates());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into link template store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
    }

    /**
     * Import media from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public void importMediaFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final MediaStoreRoot mediaStore = (MediaStoreRoot) userService.getStore(Store.Type.MEDIASTORE, false);
            try {
                importFromZipIntoTargetStore(zip, mediaStore.getStore());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into media store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
    }

    /**
     * Import scripts from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public void importScriptsFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            try {
                importFromZipIntoTargetStore(zip, templateStore.getScripts());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into script store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
    }

    /**
     * Import workflows from zip.
     *
     * @param projectName the project name
     * @param zip         the zip
     */
    public void importWorkflowsFromZip(final String projectName, final File zip) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            try {
                importFromZipIntoTargetStore(zip, templateStore.getWorkflows());
            } catch (final LockException | IOException | ElementDeletedException | URISyntaxException | RuntimeException e) {
                final String message = "Failed to import zip into workflow store: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
    }


    private static void importFromZipIntoTargetStore(final File zipFile, final StoreElement targetStore)
        throws URISyntaxException, IOException, LockException, ElementDeletedException {
        if (zipFile != null && zipFile.exists() && zipFile.isFile()) {
            LOGGER.info("Importing elements from '{}' into '{}'", zipFile.getAbsolutePath(), targetStore.getName());

            final ZipFile exportZip = new ZipFile(zipFile, ZipFile.OPEN_READ);
            final Listable<StoreElement> imports = targetStore.importStoreElements(exportZip, new ModuleImportHandler());
            for (final StoreElement imported : imports) {
                LOGGER.debug("Importing '{}' ...", imported.getName());
                imported.setLock(true);
                try {
                    imported.save("Importet from " + zipFile.getName());
                } finally {
                    imported.setLock(false);
                }
            }
        }
    }


    /**
     * Creates a new pageStoreFolder.
     *
     * @param projectName the project name.
     * @param name        the folder name.
     * @return the created folder.
     */
    public PageFolder createPageStoreFolder(final String projectName, final String name) {
        final Project project = connection.getProjectByName(projectName);
        PageFolder pageFolder = null;
        if (project != null) {
            final UserService userService = project.getUserService();
            final PageStoreRoot pageStoreRoot = (PageStoreRoot) userService.getStore(Store.Type.PAGESTORE, false);
            try {
                pageStoreRoot.setLock(true, false);
                pageFolder = pageStoreRoot.createPageFolder(name);
                pageStoreRoot.save();
            } catch (final LockException | ElementDeletedException | RuntimeException e) {
                final String message = "Failed to create PageStoreFolder: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            } finally {
                try {
                    pageStoreRoot.setLock(false, true);
                } catch (final LockException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to create PageStoreFolder: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
        }
        return pageFolder;
    }


    /**
     * Creates a new siteStoreFolder.
     *
     * @param projectName the fs project name.
     * @param name        the folder name.
     * @return the created folder.
     */
    public PageRefFolder createSiteStoreFolder(final String projectName, final String name) {
        final Project project = connection.getProjectByName(projectName);
        PageRefFolder pageRefFolder = null;
        if (project != null) {
            final UserService userService = project.getUserService();
            final SiteStoreRoot siteStoreRoot = (SiteStoreRoot) userService.getStore(Store.Type.SITESTORE, false);
            try {
                siteStoreRoot.setLock(true, false);
                pageRefFolder = siteStoreRoot.createPageRefFolder(name);
                siteStoreRoot.save();
            } catch (final LockException | ElementDeletedException | RuntimeException e) {
                final String message = "Failed to create SiteStoreFolder: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            } finally {
                try {
                    siteStoreRoot.setLock(false, true);
                } catch (final LockException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to create SiteStoreFolder: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
        }
        return pageRefFolder;
    }


    /**
     * Create a new format template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template that will be created.
     * @param channel     the channel to write the content to.
     * @param content     the sourcecode of the template.
     * @return the new template.
     */
    public IDProvider createFormatTemplate(final String projectName, final String uid, final String channel, final String content) {
        final Project project = connection.getProjectByName(projectName);
        final UserService userService = project.getUserService();
        final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
        final FormatTemplates formatTemplates = templateStore.getFormatTemplates();
        FormatTemplate formatTemplate = null;
        try {
            formatTemplate = formatTemplates.createFormatTemplate(uid);
            formatTemplate.setLock(true);
            formatTemplate.setChannelSource(getTemplateSet(project, channel), content);
            formatTemplate.save();
            formatTemplate.setLock(false, false);
        } catch (final LockException | ElementDeletedException | RuntimeException e) {
            final String message = "Failed to create FormatTemplate: " + e.getMessage();
            LOGGER.error(message, e);
            fail(message);
        } finally {
            try {
                if (formatTemplate != null) {
                    formatTemplate.setLock(false, false);
                }
            } catch (final LockException | ElementDeletedException | RuntimeException e) {
                final String message = "Failed to create FormatTemplate: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
        return formatTemplate;
    }

    /**
     * Creates a script template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template that will be created.
     * @param channel     the channel to write the content to.
     * @param content     the sourcecode of the template.
     * @return the new template.
     */
    public IDProvider createScriptTemplate(final String projectName, final String uid, final String channel, final String content) {
        final Project project = connection.getProjectByName(projectName);
        final UserService userService = project.getUserService();
        final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
        final Scripts scripts = templateStore.getScripts();
        Script script = null;
        try {
            script = scripts.createScript(uid, "");
            script.setLock(true);
            script.setChannelSource(getTemplateSet(project, channel), content);
            script.save();
            script.setLock(false, false);
        } catch (final LockException | ElementDeletedException | RuntimeException e) {
            final String message = "Failed to create ScriptTemplate: " + e.getMessage();
            LOGGER.error(message, e);
            fail(message);
        } finally {
            try {
                if (script != null) {
                    script.setLock(false, false);
                }
            } catch (final LockException | ElementDeletedException | RuntimeException e) {
                final String message = "Failed to create ScriptTemplate: " + e.getMessage();
                LOGGER.error(message, e);
                fail(message);
            }
        }
        return script;
    }

    /**
     * Modifies a page template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the page template.
     * @param channel     the channel to modify.
     * @param content     the content to set.
     */
    public void modifyPageTemplate(final String projectName, final String uid, final String channel, final String content) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            final Template template = (Template) templateStore.getStoreElement(uid, IDProvider.UidType.TEMPLATESTORE);
            if (template != null && template.getType() == Template.PAGE_TEMPLATE) {
                final PageTemplate pageTemplate = (PageTemplate) template;
                try {
                    pageTemplate.setLock(true, false);
                    if ("gom".equals(channel)) {
                        pageTemplate.setGomSource(content);
                    } else {
                        pageTemplate.setChannelSource(getTemplateSet(project, channel), content);
                    }
                    pageTemplate.save();
                } catch (final LockException | ElementDeletedException | RuntimeException e) {
                    final String message = FAILED_TO_MODIFY_TEMPLATE + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                } finally {
                    try {
                        pageTemplate.setLock(false, false);
                    } catch (final LockException | ElementDeletedException | RuntimeException e) {
                        final String message = FAILED_TO_MODIFY_TEMPLATE + e.getMessage();
                        LOGGER.error(message, e);
                        fail(message);
                    }
                }
            }
        }
    }

    /**
     * Modifies the specified format template.
     *
     * @param projectName the name of the fs project.
     * @param uid         the uid of the template to manipulate.
     * @param channel     the channel to use.
     * @param content     the sourcecode to set.
     */
    public void modifyFormatTemplate(final String projectName, final String uid, final String channel, final String content) {
        final Project project = connection.getProjectByName(projectName);
        if (project != null) {
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            final FormatTemplate
                formatTemplate =
                (FormatTemplate) templateStore.getStoreElement(uid, IDProvider.UidType.TEMPLATESTORE_FORMATTEMPLATE);
            if (formatTemplate != null) {
                try {
                    formatTemplate.setLock(true, false);
                    formatTemplate.setChannelSource(getTemplateSet(project, channel), content);
                    formatTemplate.save();
                } catch (final LockException | ElementDeletedException | RuntimeException e) {
                    final String message = FAILED_TO_MODIFY_TEMPLATE + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                } finally {
                    try {
                        formatTemplate.setLock(false, false);
                    } catch (final LockException | ElementDeletedException | RuntimeException e) {
                        final String message = FAILED_TO_MODIFY_TEMPLATE + e.getMessage();
                        LOGGER.error(message, e);
                        fail(message);
                    }
                }
            }
        }
    }

    private static TemplateSet getTemplateSet(final Project project, final String name) {
        for (final TemplateSet templateSet : project.getTemplateSets()) {
            if (templateSet.getUid().equals(name)) {
                return templateSet;
            }
        }
        return null;
    }

}
