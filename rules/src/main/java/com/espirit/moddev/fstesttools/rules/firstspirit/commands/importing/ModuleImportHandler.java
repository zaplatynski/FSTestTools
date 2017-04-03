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

import de.espirit.firstspirit.access.store.ImportHandler;
import de.espirit.firstspirit.access.store.StoreElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The ModuleImportHandler.
 */
class ModuleImportHandler implements ImportHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleImportHandler.class);

    /**
     * Constructor.
     */
    ModuleImportHandler() {
        //empty
    }

    /**
     * Runs after store element was imported.
     *
     * @param storeElement the store element that was imported.
     */
    @Override
    public void afterStoreElementImport(final StoreElement storeElement) {
        // Do nothing because nothing is enough.
    }

    /**
     * {@inheritDoc}
     *
     * @param storeElement the FS store element to use.
     */
    @Override
    public void afterExternalAttributeImport(final StoreElement storeElement) {
        // Do nothing because nothing is enough.
    }

    /**
     * {@inheritDoc}
     *
     * @param storeElement the FS store element to use.
     */
    @Override
    public void afterImportData(final StoreElement storeElement) {
        // Do nothing because nothing is enough.
    }

    /**
     * {@inheritDoc}
     *
     * @param storeElement the FS store element to use.
     */
    @Override
    public void afterAdjustAttributes(final StoreElement storeElement) {
        // Do nothing because nothing is enough.
    }

    /**
     * {@inheritDoc}
     *
     * @param count of elements imported.
     */
    @Override
    public void setImportElementCount(final int count) {
        LOGGER.info("Importing {} elements", count);
    }
}
