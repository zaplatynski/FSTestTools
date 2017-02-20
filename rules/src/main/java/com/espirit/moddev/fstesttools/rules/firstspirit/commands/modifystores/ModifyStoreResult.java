package com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;

import de.espirit.firstspirit.access.store.pagestore.PageFolder;
import de.espirit.firstspirit.access.store.sitestore.PageRefFolder;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplate;
import de.espirit.firstspirit.access.store.templatestore.Script;


/**
 * The type Modify store result.
 */
public class ModifyStoreResult implements FsConnRuleCmdResultBean{

    /**
     * The constant VOID.
     */
    public static final ModifyStoreResult VOID = new ModifyStoreResult((PageFolder) null);
    private Script script;
    private FormatTemplate formatTemplate;
    private PageFolder pageFolder;
    private PageRefFolder pageRefFolder;

    /**
     * Instantiates a new Modify store result.
     *
     * @param pageRefFolder the page ref folder
     */
    public ModifyStoreResult(final PageRefFolder pageRefFolder) {
        this.pageRefFolder = pageRefFolder;
    }

    /**
     * Instantiates a new Modify store result.
     *
     * @param pageFolder the page folder
     */
    public ModifyStoreResult(final PageFolder pageFolder) {
        this.pageFolder = pageFolder;
    }

    /**
     * Instantiates a new Modify store result.
     *
     * @param formatTemplate the format template
     */
    public ModifyStoreResult(final FormatTemplate formatTemplate) {
        this.formatTemplate = formatTemplate;
    }

    /**
     * Instantiates a new Modify store result.
     *
     * @param script the script
     */
    public ModifyStoreResult(final Script script) {
        this.script = script;
    }

    /**
     * Gets page folder.
     *
     * @return the page folder
     */
    public PageFolder getPageFolder() {
        return pageFolder;
    }

    /**
     * Gets page ref folder.
     *
     * @return the page ref folder
     */
    public PageRefFolder getPageRefFolder() {
        return pageRefFolder;
    }

    /**
     * Gets format template.
     *
     * @return the format template
     */
    public FormatTemplate getFormatTemplate() {
        return formatTemplate;
    }

    /**
     * Gets script.
     *
     * @return the script
     */
    public Script getScript() {
        return script;
    }
}

