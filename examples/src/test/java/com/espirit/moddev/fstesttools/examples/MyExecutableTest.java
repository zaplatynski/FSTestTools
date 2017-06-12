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

import com.espirit.moddev.fstesttools.mocking.MockingBaseContext;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.OperationAgent;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.ui.operations.RequestOperation;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


@RunWith(Parameterized.class)
public class MyExecutableTest {

    @Parameterized.Parameters(name = "{0}/{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"SiteArchitect", Locale.ENGLISH, BaseContext.Env.PREVIEW},
                             new Object[]{"SiteArchitect", Locale.GERMAN, BaseContext.Env.PREVIEW},
                             new Object[]{"ContentCreator", Locale.ENGLISH, BaseContext.Env.WEBEDIT},
                             new Object[]{"ContentCreator", Locale.GERMAN, BaseContext.Env.WEBEDIT});
    }

    @Parameterized.Parameter(0)
    public String name;

    @Parameterized.Parameter(1)
    public Locale locale;

    @Parameterized.Parameter(2)
    public BaseContext.Env environment;

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    @Mock
    private RequestOperation.Answer yes;

    @Mock
    private RequestOperation.Answer no;

    @Mock
    private Writer writer;

    private MyExecutable testling;
    private BaseContext context;
    private String message;
    private HashMap<String, Object> args;
    private RequestOperation requestOperation;

    @Before
    public void setUp() throws Exception {
        final ResourceBundle bundle = ResourceBundle.getBundle("com.espirit.moddev.fstesttools.examples.MainResources", locale);
        message = bundle.getString("message");

        context = new MockingBaseContext(locale, true, environment);
        args = new HashMap<String, Object>();
        args.put("context", context);

        testling = new MyExecutable();

        //Use FirstSpirit API even in tests
        final  OperationAgent operationAgent = context.requireSpecialist(OperationAgent.TYPE);
        requestOperation = operationAgent.getOperation(RequestOperation.TYPE);

        when(requestOperation.addNo()).thenReturn(no);
        when(requestOperation.addYes()).thenReturn(yes);
    }

    @After
    public void tearDown() throws Exception {
        verify(requestOperation, times(1)).perform(message);

        //Writer will be never used in any test case
        verifyZeroInteractions(writer);

        //Proof what MockingBaseContext does for you
        if(environment == BaseContext.Env.WEBEDIT){
            final UIAgent uiAgent = context.requestSpecialist(UIAgent.TYPE);
            assertThat("SiteArchitect environment not available in " + name, uiAgent, is(nullValue()));
        } else {
            final WebeditUiAgent uiAgent = context.requestSpecialist(WebeditUiAgent.TYPE);
            assertThat("ContentCreator environment not available in " + name, uiAgent, is(nullValue()));
        }
    }

    @Test
    public void testExecuteYes() throws Exception {
        when(requestOperation.perform(message)).thenReturn(yes);

        final Object result = testling.execute(args);

        assertThat("Expect a yes by user", result, is(Boolean.TRUE));
    }

    @Test
    public void testExecuteYesWithWriter() throws Exception {
        when(requestOperation.perform(message)).thenReturn(yes);

        final Object result = testling.execute(args, writer, writer);

        assertThat("Expect a yes by user", result, is(Boolean.TRUE));
    }

    @Test
    public void testExecuteNo() throws Exception {
        when(requestOperation.perform(message)).thenReturn(no);

        final Object result = testling.execute(args);

        assertThat("Expect a no by user", result, is(Boolean.FALSE));
    }

    @Test
    public void testExecuteNoWithWriter() throws Exception {
        when(requestOperation.perform(message)).thenReturn(no);

        final Object result = testling.execute(args, writer, writer);

        assertThat("Expect a no by user", result, is(Boolean.FALSE));
    }

}
