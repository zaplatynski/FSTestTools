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

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.AbstractParametersTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ModifyStoreParametersTest extends AbstractParametersTest<ModifyStoreParameters> {

    @Override
    protected ModifyStoreParameters createTestling() {
        return new ModifyStoreParameters("My Project", "my_page", "html", "source-code");
    }

    @Test
    public void getName() throws Exception {
       assertThat("Expect non null value", testling.getName(), is("my_page"));
    }

    @Test
    public void getConnection() throws Exception {
        assertThat("Expect non null value", testling.getConnection(), is(notNullValue()));
    }

    @Test
    public void getChannel() throws Exception {
        assertThat("Expect non null value", testling.getChannel(), is("html"));
    }

    @Test
    public void getContent() throws Exception {
        assertThat("Expect non null value", testling.getContent(), is("source-code"));
    }

}
