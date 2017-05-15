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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.misc;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.ServerConfiguration;
import de.espirit.firstspirit.access.project.Project;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ContentCreatorAddressCommandTest {

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    private ContentCreatorAddressParameters parameters = new ContentCreatorAddressParameters("My Project", Locale.GERMAN);

    @Mock
    private Project project;

    @Mock
    private Language language;

    @Mock
    private ServerConfiguration configuration;

    private ContentCreatorAddressCommand testling;

    @Before
    public void setUp() throws Exception {
        testling = new ContentCreatorAddressCommand();

        final Connection connection = parameters.getConnection();

        when(connection.getProjectByName("My Project")).thenReturn(project);
        when(project.getMasterLanguage()).thenReturn(language);
        when(project.getId()).thenReturn(123L);
        when(language.getLocale()).thenReturn(Locale.CANADA);
        when(connection.getServerConfiguration()).thenReturn(configuration);
        when(configuration.getUrl()).thenReturn("http://localhost:8000/");
    }

    @Test
    public void name() throws Exception {
        assertThat("Expect specific value",testling.name(), is(ContentCreatorAddressCommand.CMD_NAME));
    }

    @Test
    public void execute() throws Exception {
        final ContentCreatorAddressResult result = testling.execute(parameters);

        assertThat("Expect a valid URL", result.getContentCreatorAddress(), is("http://localhost:8000/fs5webedit_123/?locale=de&project=123"));
    }

}
