package com.espirit.moddev.fstesttools.rules.firstspirit.commands;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

/**
 * Created by e-Spirit AG.
 */
public class CommandForTesting implements FsConnRuleCommand<TestCmdParameters, TestCmdResult>{

    @Override
    public String name() {
        return "TEST";
    }

    @Override
    public TestCmdResult execute(final TestCmdParameters parameters) {
        return new TestCmdResult();
    }
}
