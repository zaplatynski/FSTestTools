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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.pagestore.PageStoreRoot;
import de.espirit.firstspirit.access.store.sitestore.SiteStoreRoot;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplate;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplates;
import de.espirit.firstspirit.access.store.templatestore.PageTemplate;
import de.espirit.firstspirit.access.store.templatestore.Script;
import de.espirit.firstspirit.access.store.templatestore.Scripts;
import de.espirit.firstspirit.access.store.templatestore.TemplateStoreRoot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class ModifyStoreCommandTest {

    private static final String PROJECT = "My Project";

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @DataPoints
    public static ModifyStoreCommand[] commands = ModifyStoreCommand.values();

    @ObjectUnderTest
    private ModifyStoreParameters parameters = new ModifyStoreParameters(PROJECT, "myStoreElementUid", "html", "test");

    @Mock
    private Project project;

    @Mock
    private UserService userService;

    @Mock
    private SiteStoreRoot siteStore;

    @Mock
    private PageStoreRoot pageStore;

    @Mock
    private TemplateStoreRoot templateStore;

    @Mock
    private FormatTemplates formatTemplates;

    @Mock
    private FormatTemplate formatTemplate;

    @Mock
    private Scripts scriptes;

    @Mock
    private Script script;

    @Mock
    private PageTemplate template;

    @Mock
    private TemplateSet html;

    @Before
    public void setUp() throws Exception {

        Connection connection = parameters.getConnection();

        when(connection.getProjectByName(PROJECT)).thenReturn(project);

        when(html.getUid()).thenReturn("html");
        when(project.getUserService()).thenReturn(userService);
        when(project.getTemplateSets()).thenReturn(Arrays.asList(html));

        when(userService.getStore(anyObject(), eq(false))).then(new Answer<Store>() {
            @Override
            public Store answer(InvocationOnMock invocation) throws Throwable {
                Store.Type storeType = (Store.Type) invocation.getArguments()[0];
                switch (storeType){
                    case PAGESTORE:
                        return pageStore;
                    case SITESTORE:
                        return siteStore;
                    case TEMPLATESTORE:
                        when(templateStore.getFormatTemplates()).thenReturn(formatTemplates);
                        when(formatTemplates.createFormatTemplate(parameters.getName())).thenReturn(formatTemplate);

                        when(templateStore.getScripts()).thenReturn(scriptes);
                        when(scriptes.createScript(parameters.getName(), "")).thenReturn(script);

                        when(templateStore.getStoreElement(parameters.getName(),
                                                           IDProvider.UidType.TEMPLATESTORE_FORMATTEMPLATE)).thenReturn(formatTemplate);

                        when(templateStore.getStoreElement(parameters.getName(), IDProvider.UidType.TEMPLATESTORE)).thenReturn(template);

                        return templateStore;
                }
                return null;
            }
        });
    }

    @Theory
    public void testExecute(ModifyStoreCommand command) throws Exception {

        ModifyStoreResult result = command.execute(parameters);

        assertThat("Expect non null value", result, is(notNullValue()));
    }

    @Test
    public void testImplementation() throws Exception {
        assertThat("Expect implementation of interface", ModifyStoreCommand.class.getInterfaces(), hasItemInArray(FsConnRuleCommand.class));
    }
}
