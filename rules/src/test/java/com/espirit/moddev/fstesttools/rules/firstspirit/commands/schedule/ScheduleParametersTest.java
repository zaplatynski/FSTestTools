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

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.AbstractParametersTest;

import de.espirit.firstspirit.access.schedule.DeployTask;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ScheduleParametersTest extends AbstractParametersTest<ScheduleParameters> {

    @Rule
    public TemporaryFolder temFiles = new TemporaryFolder();

    @Override
    protected ScheduleParameters createTestling() throws Exception {
        return new ScheduleParameters("MyProject", "Full Generation");
    }

    @Test
    public void testIsGenerateDeleteDirectory() throws Exception {
        assertTrue("Default true", testling.isGenerateDeleteDirectory());
    }

    @Test
    public void testSetGenerateDeleteDirectory() throws Exception {
        testling.setGenerateDeleteDirectory(false);

        assertFalse("set to false", testling.isGenerateDeleteDirectory());
    }

    @Test
    public void testGetDeployTaskType() throws Exception {
        assertThat("expect default", testling.getDeployTaskType(), is(DeployTask.Type.Full));
    }

    @Test
    public void testGetEntry() throws Exception {
        assertThat("expect default", testling.getEntryName(), is("Full Generation"));
    }

    @Test
    public void testDeployDir() throws Exception {
        final File deployDir = temFiles.newFolder();

        testling.setDeployDir(deployDir);

        assertThat("Expect identity", testling.getDeployDir(), is(sameInstance(deployDir)));
    }

    @Test
    public void testSetDeployTaskType() throws Exception {
        testling.setDeployTaskType(DeployTask.Type.Incremental);

        assertThat("set incremental", testling.getDeployTaskType(), is(DeployTask.Type.Incremental));
    }

    @Test
    public void testGetGenerateUrlPrefix() throws Exception {
        assertThat("expect default", testling.getGenerateUrlPrefix(), is("http://$address$"));
    }

    @Test
    public void testSetGenerateUrlPrefix() throws Exception {
        testling.setGenerateUrlPrefix("http://www.e-spirit.com");

        assertThat("expect new address", testling.getGenerateUrlPrefix(), is("http://www.e-spirit.com"));
    }

    @Test
    public void testGetConnection() throws Exception {
        assertThat("Expect non null value", testling.getConnection(), is(notNullValue()));
    }

} 
