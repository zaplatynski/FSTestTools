package com.espirit.moddev.fstesttools.rules.firstspirit.commands;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.BaseContext;

import javax.inject.Inject;

/**
 * Created by e-Spirit AG.
 */
public class TestCmdParameters implements FsConnRuleCmdParamBean {

    @Override
    public String getProjectName() {
        return "Test";
    }

}
