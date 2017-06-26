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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.common.util.Listable;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.store.ElementDeletedException;
import de.espirit.firstspirit.access.store.LockException;
import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.StoreElement;
import de.espirit.firstspirit.access.store.mediastore.MediaStoreRoot;
import de.espirit.firstspirit.access.store.templatestore.TemplateStoreRoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import static org.junit.Assert.fail;


/**
 * The enum Zip import commands.
 */
public enum ZipImportCommands implements FsConnRuleCommand<ZipImportParameters, ZipImportResult> {

    /**
     * The Import format templates.
     */
    IMPORT_FORMAT_TEMPLATES {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final TemplateStoreRoot templateStore = getTemplateStoreRoot(project);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), templateStore.getFormatTemplates());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into format template store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }

            }
            return ZipImportResult.VOID;
        }
    },
    /**
     * The Import page templates.
     */
    IMPORT_PAGE_TEMPLATES {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final TemplateStoreRoot templateStore = getTemplateStoreRoot(project);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), templateStore.getPageTemplates());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into page template store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
            return ZipImportResult.VOID;
        }
    },
    /**
     * The Import link templates.
     */
    IMPORT_LINK_TEMPLATES {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final TemplateStoreRoot templateStore = getTemplateStoreRoot(project);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), templateStore.getLinkTemplates());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into link template store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
            return ZipImportResult.VOID;
        }
    },
    /**
     * The Import media.
     */
    IMPORT_MEDIA {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final UserService userService = project.getUserService();
                final MediaStoreRoot mediaStore = (MediaStoreRoot) userService.getStore(Store.Type.MEDIASTORE, false);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), mediaStore.getStore());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into media store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
            return ZipImportResult.VOID;
        }
    },
    /**
     * The Import scripts.
     */
    IMPORT_SCRIPTS {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final TemplateStoreRoot templateStore = getTemplateStoreRoot(project);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), templateStore.getScripts());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into script store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
            return ZipImportResult.VOID;
        }
    },
    /**
     * The Import workflows.
     */
    IMPORT_WORKFLOWS {
        @Override
        public ZipImportResult execute(final ZipImportParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final TemplateStoreRoot templateStore = getTemplateStoreRoot(project);
                try {
                    importFromZipIntoTargetStore(parameters.getZip(), templateStore.getWorkflows());
                } catch (final LockException | IOException | ElementDeletedException | RuntimeException e) {
                    final String message = "Failed to import zip into workflow store: " + e.getMessage();
                    LOGGER.error(message, e);
                    fail(message);
                }
            }
            return ZipImportResult.VOID;
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipImportCommands.class);

    protected static TemplateStoreRoot getTemplateStoreRoot(Project project) {
        final UserService userService = project.getUserService();
        return (TemplateStoreRoot) userService.getStore(Store.Type.TEMPLATESTORE, false);
    }

    private static Project getProject(final ZipImportParameters parameters) {
        return parameters.getConnection().getProjectByName(parameters.getProjectName());
    }

    private static void importFromZipIntoTargetStore(final File zipFile, final StoreElement targetStore)
        throws IOException, LockException, ElementDeletedException {
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
}
