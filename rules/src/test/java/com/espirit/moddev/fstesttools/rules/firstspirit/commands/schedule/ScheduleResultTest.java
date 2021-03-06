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

import de.espirit.firstspirit.access.schedule.RunState;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class ScheduleResultTest {

    @DataPoints
    public static RunState[] states = RunState.values();

    @Theory
    public void getScheduleRunState(final RunState state) throws Exception {
        ScheduleResult testling = new ScheduleResult(state);

        assertThat("Expect identity", testling.getScheduleRunState(), is(sameInstance(state)));
    }

    @Test
    public void testInstance() throws Exception {
        assertThat("Expect non null value", ScheduleResult.VOID, is(notNullValue()));
    }
}
