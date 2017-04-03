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

import de.espirit.firstspirit.access.store.pagestore.PageFolder;
import de.espirit.firstspirit.access.store.sitestore.PageRefFolder;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplate;
import de.espirit.firstspirit.access.store.templatestore.Script;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ModifyStoreResultTest {

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Mock
    private PageRefFolder pageRefFolder;

    @Mock
    private PageFolder pageFolder;

    @Mock
    private FormatTemplate formatTemplates;

    private Script script;

    @Test
    public void testGetPageFolder() throws Exception {
        ModifyStoreResult testling = new ModifyStoreResult(pageFolder);
        assertThat("Expect identity",testling.getPageFolder(), is(sameInstance(pageFolder)));
        assertThat("Expect null value",testling.getPageRefFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getFormatTemplate(), is(nullValue()));
        assertThat("Expect null value",testling.getScript(), is(nullValue()));
    }

    @Test
    public void testGetPageRefFolder() throws Exception {
        ModifyStoreResult testling = new ModifyStoreResult(pageRefFolder);
        assertThat("Expect identity",testling.getPageRefFolder(), is(sameInstance(pageRefFolder)));
        assertThat("Expect null value",testling.getPageFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getFormatTemplate(), is(nullValue()));
        assertThat("Expect null value",testling.getScript(), is(nullValue()));
    }

    @Test
    public void testGetFormatTemplate() throws Exception {
        ModifyStoreResult testling = new ModifyStoreResult(formatTemplates);
        assertThat("Expect identity",testling.getFormatTemplate(), is(sameInstance(formatTemplates)));
        assertThat("Expect null value",testling.getPageRefFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getPageFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getScript(), is(nullValue()));
    }

    @Test
    public void testGetScript() throws Exception {
        ModifyStoreResult testling = new ModifyStoreResult(script);
        assertThat("Expect identity",testling.getScript(), is(sameInstance(script)));
        assertThat("Expect null value",testling.getPageRefFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getPageFolder(), is(nullValue()));
        assertThat("Expect null value",testling.getFormatTemplate(), is(nullValue()));
    }

}
