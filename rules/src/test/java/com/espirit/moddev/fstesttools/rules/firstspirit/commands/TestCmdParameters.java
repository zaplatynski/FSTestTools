package com.espirit.moddev.fstesttools.rules.firstspirit.commands;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

/**
 * Created by e-Spirit AG.
 */
public class TestCmdParameters implements FsConnRuleCmdParamBean {

    @Override
    public String getProjectName() {
        return "Test";
    }
}
