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

import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ModifyStoreParametersTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    private ModifyStoreParameters testling = new ModifyStoreParameters("My Project", "my_page", "html", "source-code");

    @Test
    public void testGetProjectName() throws Exception {
        assertThat("Expect specific value", testling.getProjectName(), is(notNullValue()));
    }

    @Test
    public void testGetName() throws Exception {
        assertThat("Expect non null value", testling.getName(), is("my_page"));
    }

    @Test
    public void testGetConnection() throws Exception {
        assertThat("Expect non null value", testling.getConnection(), is(notNullValue()));
    }

    @Test
    public void testGetChannel() throws Exception {
        assertThat("Expect non null value", testling.getChannel(), is("html"));
    }

    @Test
    public void testGetContent() throws Exception {
        assertThat("Expect non null value", testling.getContent(), is("source-code"));
    }

}
