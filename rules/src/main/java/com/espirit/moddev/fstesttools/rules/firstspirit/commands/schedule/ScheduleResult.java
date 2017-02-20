package com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;

import de.espirit.firstspirit.access.schedule.RunState;

/**
 * The type Schedule result.
 */
public class ScheduleResult implements FsConnRuleCmdResultBean {

    /**
     * The constant VOID.
     */
    public static final ScheduleResult VOID = new ScheduleResult(RunState.NOT_STARTED);
    private final RunState scheduleRunState;

    /**
     * Instantiates a new Schedule result.
     *
     * @param scheduleRunState the schedule run state
     */
    public ScheduleResult(final RunState scheduleRunState) {
        this.scheduleRunState = scheduleRunState;
    }

    /**
     * Gets schedule run state.
     *
     * @return the schedule run state
     */
    public RunState getScheduleRunState() {
        return scheduleRunState;
    }
}
