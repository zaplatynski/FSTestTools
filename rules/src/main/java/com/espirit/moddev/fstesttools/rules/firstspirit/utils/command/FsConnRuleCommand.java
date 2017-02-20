package com.espirit.moddev.fstesttools.rules.firstspirit.utils.command;


/**
 * The interface Fs conn rule command.
 *
 * @param <P> the type parameter
 * @param <R> the type parameter
 */
public interface FsConnRuleCommand<P extends FsConnRuleCmdParamBean, R extends FsConnRuleCmdResultBean> {

    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Execute r.
     *
     * @param parameters the parameters
     * @return the r
     */
    R execute(final P parameters);
}
