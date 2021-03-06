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

import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ContentCreatorAddressParametersTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    private ContentCreatorAddressParameters testling = new ContentCreatorAddressParameters("My Project");

    @Test
    public void testGetProjectName() throws Exception {
        assertThat("Expect specific value", testling.getProjectName(), is(notNullValue()));
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat("Expect default null value", testling.getLocale(), is(nullValue()));

        testling.setLocale(Locale.CANADA);

        assertThat("Expect identity",testling.getLocale(), is(sameInstance(Locale.CANADA)));
    }

    @Test
    public void testGetConnection() throws Exception {
        assertThat("Expect non null value", testling.getConnection(), is(notNullValue()));
    }

}
