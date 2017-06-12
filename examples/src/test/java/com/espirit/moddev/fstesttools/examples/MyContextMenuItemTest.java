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

package com.espirit.moddev.fstesttools.examples;

import com.espirit.moddev.fstesttools.mocking.MockingContextMenuContext;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.contentstore.Content2;
import de.espirit.firstspirit.access.store.mediastore.Media;
import de.espirit.firstspirit.access.store.pagestore.Page;
import de.espirit.firstspirit.access.store.pagestore.PageFolder;
import de.espirit.firstspirit.access.store.sitestore.PageRef;
import de.espirit.firstspirit.access.store.templatestore.Template;
import de.espirit.firstspirit.agency.OperationAgent;
import de.espirit.firstspirit.ui.operations.RequestOperation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class MyContextMenuItemTest {

    @Parameterized.Parameters(name = "{0}/execute:{2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{Store.Type.PAGESTORE, mock(Page.class), Boolean.TRUE},
                             new Object[]{Store.Type.PAGESTORE, mock(PageFolder.class), Boolean.FALSE},
                             new Object[]{Store.Type.SITESTORE, mock(PageRef.class), Boolean.FALSE},
                             new Object[]{Store.Type.MEDIASTORE, mock(Media.class), Boolean.FALSE},
                             new Object[]{Store.Type.CONTENTSTORE, mock(Content2.class), Boolean.FALSE},
                             new Object[]{Store.Type.TEMPLATESTORE, mock(Template.class), Boolean.FALSE});
    }

    @Parameterized.Parameter(0)
    public Store.Type storeType;

    @Parameterized.Parameter(1)
    public IDProvider element;

    @Parameterized.Parameter(2)
    public Boolean expectEnabled;

    private MyContextMenuItem testling;
    private MockingContextMenuContext context;

    @Before
    public void setUp() throws Exception {
        context = new MockingContextMenuContext(Locale.ENGLISH, true, storeType, BaseContext.Env.PREVIEW);
        context.addElement(element);
        testling = new MyContextMenuItem();
    }

    @Test
    public void testExecute() throws Exception {
        assumeTrue("Run execute only if enabled and ignore otherwise!", testling.isEnabled(context));

        final OperationAgent operationAgent = context.requestSpecialist(OperationAgent.TYPE);
        final RequestOperation operation = operationAgent.getOperation(RequestOperation.TYPE);

        testling.execute(context);

        verify(operation, times(1)).perform(contains("Selected page:"));
    }

    @Test
    public void testGetLabel() throws Exception {
        final String label = testling.getLabel(context);

        assertThat("", label, is("Foobar"));
    }

    @Test
    public void testGetIcon() throws Exception {
        assertThat("Expect null value",testling.getIcon(context), is(nullValue()));
    }

    @Test
    public void testIsEnabled() throws Exception {
        final boolean enabled = testling.isEnabled(context);

        assertThat("Expect enabled is " + expectEnabled, enabled, is(expectEnabled));
    }

    @Test
    public void testIsVisible() throws Exception {
        final boolean visible = testling.isVisible(context);

        assertThat("Always visible", visible, is(Boolean.TRUE));
    }

}
