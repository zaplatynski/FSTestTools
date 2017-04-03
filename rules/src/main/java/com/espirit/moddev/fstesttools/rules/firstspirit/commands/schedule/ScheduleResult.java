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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;

import de.espirit.firstspirit.access.schedule.RunState;

import java.util.Objects;

/**
 * The type Schedule result.
 */
public final class ScheduleResult implements FsConnRuleCmdResultBean {

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
        this.scheduleRunState = Objects.requireNonNull(scheduleRunState, "RunState can not be null");
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
