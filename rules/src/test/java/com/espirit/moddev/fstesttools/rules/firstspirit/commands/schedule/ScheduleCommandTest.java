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

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreCommand;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.firstspirit.access.AdminService;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.schedule.DeployTask;
import de.espirit.firstspirit.access.schedule.FileTarget;
import de.espirit.firstspirit.access.schedule.GenerateTask;
import de.espirit.firstspirit.access.schedule.RunState;
import de.espirit.firstspirit.access.schedule.ScheduleEntry;
import de.espirit.firstspirit.access.schedule.ScheduleEntryControl;
import de.espirit.firstspirit.access.schedule.ScheduleEntryState;
import de.espirit.firstspirit.access.schedule.ScheduleStorage;
import de.espirit.firstspirit.access.schedule.ScheduleTask;
import de.espirit.firstspirit.access.schedule.TaskResult;
import de.espirit.firstspirit.admin.DeployTaskImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class ScheduleCommandTest {

    private static final String PROJECT = "My Project";
    private static final String ENTRY_NAME = "Deployment";

    @Rule
    public TemporaryFolder tempFiles = new TemporaryFolder();

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @DataPoints
    public static ScheduleCommand[] commands = ScheduleCommand.values();

    @ObjectUnderTest
    private ScheduleParameters parameters = new ScheduleParameters(PROJECT, ENTRY_NAME);

    @Mock
    private Project project;

    @Mock
    private AdminService adminService;

    @Mock
    private ScheduleStorage scheduleStorage;

    @Mock
    private ScheduleEntry scheduleEntry;

    @Mock
    private ScheduleEntryControl scheduleEntryControl;

    @Mock
    private ScheduleEntryState scheduleEntryState;

    @Mock
    private TaskResult taskResult;

    @Mock
    private ScheduleTask scheduleTask;

    @Mock
    private DeployTaskImpl deployTask;

    @Mock
    private GenerateTask generateTask;

    @Mock
    private Language masterLanguage;

    @Mock
    private FileTarget fileTarget;

    @Mock
    private TemplateSet xmlTemplateSet;

    @Mock
    private TemplateSet htmlTemplateSet;

    @Before
    public void setUp() throws Exception {

        parameters.setDeployDir(tempFiles.newFolder());

        Connection connection = parameters.getConnection();

        when(connection.getProjectByName(PROJECT)).thenReturn(project);
        when(project.getMasterLanguage()).thenReturn(masterLanguage);
        when(project.getTemplateSets()).thenReturn(Arrays.asList(xmlTemplateSet, htmlTemplateSet));
        when(xmlTemplateSet.getUid()).thenReturn("xml");
        when(htmlTemplateSet.getUid()).thenReturn("html");
        when(connection.getService(AdminService.class)).thenReturn(adminService);
        when(adminService.getScheduleStorage()).thenReturn(scheduleStorage);
        when(scheduleStorage.getScheduleEntry(project, parameters.getEntryName())).thenReturn(scheduleEntry);
        when(scheduleStorage.createScheduleEntry(parameters.getEntryName())).thenReturn(scheduleEntry);
        when(scheduleEntry.execute()).thenReturn(scheduleEntryControl);
        when(scheduleEntryControl.getState()).thenReturn(scheduleEntryState);
        when(scheduleEntryState.getTaskResults()).thenReturn(Arrays.asList(taskResult));
        when(scheduleEntryState.getState()).thenReturn(RunState.SUCCESS);
        when(taskResult.getState()).thenReturn(RunState.SUCCESS);
        when(taskResult.getTask()).thenReturn(scheduleTask);
        when(scheduleTask.getName()).thenReturn("generate");
        when(scheduleEntry.getTasks()).thenReturn(new ArrayList<>(Arrays.asList(scheduleTask, deployTask)));
        when(scheduleEntry.createTask(GenerateTask.class)).thenReturn(generateTask);
        when(scheduleEntry.createTask(DeployTask.class)).thenReturn(deployTask);
        when(deployTask.createTarget(FileTarget.class)).thenReturn(fileTarget);
    }

    @Theory
    public void testExecute(ScheduleCommand command) throws Exception {

        ScheduleResult result = command.execute(parameters);

        assertThat("Expect non null value", result, is(notNullValue()));
    }

    @Test
    public void testImplementation() throws Exception {
        assertThat("Expect implementation of interface", ModifyStoreCommand.class.getInterfaces(), hasItemInArray(FsConnRuleCommand.class));
    }

}
