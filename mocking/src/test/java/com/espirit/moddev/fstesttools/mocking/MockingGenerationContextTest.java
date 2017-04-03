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

package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.store.pagestore.Page;
import de.espirit.firstspirit.io.FileHandle;
import de.espirit.firstspirit.parser.Printable;

import org.junit.Test;

import java.io.Closeable;
import java.io.StringWriter;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockingGenerationContextTest<C extends MockingGenerationContext> extends AbstractMockingGenerationScriptContextTest<C> {

    /**
     * Instantiates a new Mocking project script context test.
     *
     * @param environment the environment
     */
    public MockingGenerationContextTest(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        super(environment, enableServiceBrokerFake);
    }

    @Override
    protected C createTestling(final BaseContext.Env environment, final boolean enableServiceBrokerFake) {
        return (C) new MockingGenerationContext(TEST_LOCALE, false, false, enableServiceBrokerFake, environment);
    }

    @Test
    public void testGetDataSet() throws Exception {
        assertThat("Expect non null value", getTestling().getDataset(), is(notNullValue()));
    }

    @Test
    public void testGetUrlCreator() throws Exception {
        assertThat("Expect non null value", getTestling().getUrlCreator(), is(notNullValue()));
    }

    @Test
    public void testGetUrlCreatorProvider() throws Exception {
        assertThat("Expect non null value", getTestling().getUrlCreatorProvider(), is(notNullValue()));
    }

    @Test
    public void testUseMasterLanguageForData() throws Exception {
        assertFalse("Expect false", getTestling().getUseMasterLanguageForData());

        getTestling().setUseMasterLanguageForData(true);

        assertTrue("Expect true", getTestling().getUseMasterLanguageForData());
    }

    @Test
    public void testGetNode() throws Exception {
        assertThat("Expect non null value", getTestling().getNode(), is(notNullValue()));
    }

    @Test
    public void testGetNavigationContext() throws Exception {
        assertThat("Expect non null value", getTestling().getNavigationContext(), is(notNullValue()));
    }

    @Test
    public void testGetEvaluator() throws Exception {
        assertThat("Expect non null value", getTestling().getEvaluator(), is(notNullValue()));
    }

    @Test
    public void testGetCharacterReplacer() throws Exception {
        assertThat("Expect non null value", getTestling().getCharacterReplacer(true), is(notNullValue()));
    }

    @Test
    public void testGetPage() throws Exception {
        assertThat("Expect null value", getTestling().getPage(), is(nullValue()));

        final Page page = mock(Page.class);
        getTestling().setPage(page);

        assertThat("Expect non null value", getTestling().getPage(), is(page));
    }

    @Test
    public void testGetEncoding() throws Exception {
        assertThat("Expect non null value", getTestling().getEncoding(), is(notNullValue()));
    }

    @Test
    public void testGetFont() throws Exception {
        assertThat("Expect non null value", getTestling().getFont(null), is(notNullValue()));
    }

    @Test
    public void testVariables() throws Exception {
        getTestling().setVariableValue("key", "value");

        assertThat("Expect specific value", getTestling().getVariableValue("key"), is((Object) "value"));
    }

    @Test
    public void testGetContext() throws Exception {
        assertThat("Expect non null value", getTestling().getContext(), is(notNullValue()));
        assertThat("Expect non null value", getTestling().getContext(null), is(notNullValue()));
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat("Expect specific value", getTestling().getLocale(), is(TEST_LOCALE));

        getTestling().setLocale(Locale.GERMAN);

        assertThat("Expect specific value", getTestling().getLocale(), is(Locale.GERMAN));
    }

    @Test
    public void testGetLocaleKey() throws Exception {
        assertThat("Expect null value", getTestling().getLocaleKey(), is(nullValue()));

        getTestling().setLocaleKey("de");

        assertThat("Expect non null value", getTestling().getLocaleKey(), is("de"));
    }

    @Test
    public void testGetOut() throws Exception {
        assertThat("Expect null value", getTestling().getOut(), is(nullValue()));

        getTestling().setOut(new StringWriter());

        assertThat("Expect non null value", getTestling().getOut(), is(notNullValue()));
    }

    @Test
    public void testPrint() throws Exception {
        final Printable printable = mock(Printable.class);

        getTestling().print(printable);

        verify(printable).print(getTestling().getEvaluator());
    }

    @Test
    public void testGetTemplate() throws Exception {
        assertThat("Expect non null value", getTestling().getTemplate(null, null, null), is(notNullValue()));
    }

    @Test
    public void testGetDefaultExpression() throws Exception {
        assertThat("Expect null value", getTestling().getDefaultExpression(), is(nullValue()));

        getTestling().setDefaultExpression("test");

        assertThat("Expect non null value", getTestling().getDefaultExpression(), is(notNullValue()));
    }

    @Test
    public void testGetPageContext() throws Exception {
        assertThat("Expect non null value", getTestling().getPageContext(), is(notNullValue()));
    }

    @Test
    public void testGetDebugMode() throws Exception {
        assertFalse("Expect false", getTestling().getDebugMode());

        getTestling().setDebugMode(true);

        assertTrue("Expect true", getTestling().getDebugMode());
    }

    @Test
    public void testIsHtmlMode() throws Exception {
        assertFalse("Expect false", getTestling().isHtmlMode());

        getTestling().setHtmlMode(true);

        assertTrue("Expect true", getTestling().isHtmlMode());
    }

    @Test
    public void testGetLanguageSpecificPage() throws Exception {
        assertThat("Expect non null value", getTestling().getLanguageSpecificPage(mock(Page.class)), is(notNullValue()));
    }

    @Test
    public void testGetStartTime() throws Exception {
        assertThat("Expect non null value", getTestling().getStartTime(), is(notNullValue()));
    }

    @Test
    public void testGetScheduleContext() throws Exception {
        assertThat("Expect non null value", getTestling().getScheduleContext(), is(notNullValue()));
    }

    @Test
    public void testGetPageParams() throws Exception {
        assertThat("Expect non null value", getTestling().getPageParams(), is(notNullValue()));
    }

    @Test
    public void testGetDataset() throws Exception {
        assertThat("Expect non null value", getTestling().getDataset(), is(notNullValue()));
    }

    @Test
    public void testGetGenerationContext() throws Exception {
        assertThat("Expect non null value", getTestling().getGenerationContext(), is(notNullValue()));
    }

    @Test
    public void testShowWebeditButtons() throws Exception {
        assertThat("Only if webdit", getTestling().showWebeditButtons(), is(getTestling().is(BaseContext.Env.WEBEDIT)));
    }

    @Test
    public void testContextStack() throws Exception {
        assertThat("Expect non null value", getTestling().popContext(), is(nullValue()));

        getTestling().pushContext(getTestling().getContext());

        assertThat("Expect specific value", getTestling().popContext(), is(getTestling().getContext()));
        assertThat("Expect non null value", getTestling().popContext(), is(nullValue()));
    }

    @Test
    public void testGetFileHandle() throws Exception {
        final FileHandle fileHandle = getTestling().getFileHandle("test");

        assertThat("Expect non null value", fileHandle, is(notNullValue()));

        assertThat("Expect identity", fileHandle, is(sameInstance(getTestling().getFileHandle("test"))));
    }

    @Test
    public void testClose() throws Exception {
        final Closeable closeable = mock(Closeable.class);

        getTestling().addCloseable(closeable);

        getTestling().close();

        verify(closeable).close();
    }

    @Test
    public void testGetDeleteDirectory() throws Exception {
        assertFalse("Expect false", getTestling().getDeleteDirectory());
    }

    @Test
    public void testGetBasePath() throws Exception {
        assertThat("Expect null value", getTestling().getBasePath(), is(nullValue()));

        getTestling().setBasePath("test");

        assertThat("Expect non null value", getTestling().getBasePath(), is("test"));
    }
}
