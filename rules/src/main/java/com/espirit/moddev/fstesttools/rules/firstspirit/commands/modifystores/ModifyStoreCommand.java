package com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.store.ElementDeletedException;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.LockException;
import de.espirit.firstspirit.access.store.Store;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.espirit.moddev.fstesttools.rules.firstspirit.FirstSpiritConnectionRule.FAILED_TO_MODIFY_TEMPLATE;
import static org.junit.Assert.fail;


/**
 * The enum Modify store command.
 */
public enum ModifyStoreCommand implements FsConnRuleCommand<ModifyStoreParameters, ModifyStoreResult> {
    /**
     * The Create pagestore folder.
     */
    CREATE_PAGESTORE_FOLDER {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            PageFolder pageFolder = null;
            if (project != null) {
                final UserService userService = project.getUserService();
                final PageStoreRoot pageStoreRoot = (PageStoreRoot) userService.getStore(Store.Type.PAGESTORE, false);
                try {
                    pageStoreRoot.setLock(true, false);
                    pageFolder = pageStoreRoot.createPageFolder(parameters.getName());
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
            return new ModifyStoreResult(pageFolder);
        }
    },
    /**
     * The Create sitestore folder.
     */
    CREATE_SITESTORE_FOLDER {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            PageRefFolder pageRefFolder = null;
            if (project != null) {
                final UserService userService = project.getUserService();
                final SiteStoreRoot siteStoreRoot = (SiteStoreRoot) userService.getStore(Store.Type.SITESTORE, false);
                try {
                    siteStoreRoot.setLock(true, false);
                    pageRefFolder = siteStoreRoot.createPageRefFolder(parameters.getName());
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
            return new ModifyStoreResult(pageRefFolder);
        }
    },
    /**
     * The Create format template.
     */
    CREATE_FORMAT_TEMPLATE {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            final FormatTemplates formatTemplates = templateStore.getFormatTemplates();
            FormatTemplate formatTemplate = null;
            try {
                formatTemplate = formatTemplates.createFormatTemplate(parameters.getName());
                formatTemplate.setLock(true);
                formatTemplate.setChannelSource(getTemplateSet(project, parameters.getChannel()), parameters.getContent());
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
            return new ModifyStoreResult(formatTemplate);
        }
    },
    /**
     * The Create script template.
     */
    CREATE_SCRIPT_TEMPLATE {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            final UserService userService = project.getUserService();
            final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
            final Scripts scripts = templateStore.getScripts();
            Script script = null;
            try {
                script = scripts.createScript(parameters.getName(), "");
                script.setLock(true);
                script.setChannelSource(getTemplateSet(project, parameters.getChannel()), parameters.getContent());
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
            return new ModifyStoreResult(script);
        }
    },
    /**
     * The Modify page template.
     */
    MODIFY_PAGE_TEMPLATE {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            if (project != null) {
                final UserService userService = project.getUserService();
                final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
                final Template template = (Template) templateStore.getStoreElement(parameters.getName(), IDProvider.UidType.TEMPLATESTORE);
                modifyOnlyPageTemplate(parameters, project, template);
            }
            return ModifyStoreResult.VOID;
        }

        private void modifyOnlyPageTemplate(final ModifyStoreParameters parameters, final Project project, final Template template) {
            if (template != null && template.getType() == Template.PAGE_TEMPLATE) {
                final PageTemplate pageTemplate = (PageTemplate) template;
                try {
                    pageTemplate.setLock(true, false);
                    modifyContent(parameters, project, pageTemplate);
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

        private void modifyContent(final ModifyStoreParameters parameters, final Project project, final PageTemplate pageTemplate) {
            if ("gom".equals(parameters.getChannel())) {
                pageTemplate.setGomSource(parameters.getContent());
            } else {
                pageTemplate.setChannelSource(getTemplateSet(project, parameters.getChannel()), parameters.getContent());
            }
        }
    },
    /**
     * The Modify format template.
     */
    MODIFY_FORMAT_TEMPLATE {
        @Override
        public ModifyStoreResult execute(final ModifyStoreParameters parameters) {
            final Project project = parameters.getConnection().getProjectByName(parameters.getProjectName());
            if (project != null) {
                final UserService userService = project.getUserService();
                final TemplateStoreRoot templateStore = (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
                final FormatTemplate
                    formatTemplate =
                    (FormatTemplate) templateStore.getStoreElement(parameters.getName(), IDProvider.UidType.TEMPLATESTORE_FORMATTEMPLATE);
                if (formatTemplate != null) {
                    try {
                        formatTemplate.setLock(true, false);
                        formatTemplate.setChannelSource(getTemplateSet(project, parameters.getChannel()), parameters.getContent());
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
            return ModifyStoreResult.VOID;
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyStoreCommand.class);

    private static TemplateSet getTemplateSet(final Project project, final String name) {
        for (final TemplateSet templateSet : project.getTemplateSets()) {
            if (templateSet.getUid().equals(name)) {
                return templateSet;
            }
        }
        return null;
    }
}
