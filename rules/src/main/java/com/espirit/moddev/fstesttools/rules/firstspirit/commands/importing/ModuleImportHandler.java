package com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing;

import de.espirit.firstspirit.access.store.ImportHandler;
import de.espirit.firstspirit.access.store.StoreElement;

/**
 * The import handler.
 */
class ModuleImportHandler implements ImportHandler {

    /**
     * Constructor.
     */
    ModuleImportHandler() {
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
        // Do nothing because nothing is enough.
    }
}
