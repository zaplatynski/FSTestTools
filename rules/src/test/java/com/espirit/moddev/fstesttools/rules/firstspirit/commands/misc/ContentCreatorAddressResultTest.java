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

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ContentCreatorAddressResultTest {

    private ContentCreatorAddressResult testling;
    private String url;

    @Before
    public void setUp() throws Exception {
        long projectId = 123L;
        String language = "de";
        url = "http://localhost:8000/fs5webedit_" + projectId + "/?locale=" + language + "&project=" + projectId;
        testling = new ContentCreatorAddressResult(url);
    }

    @Test
    public void getContentCreatorAddress() throws Exception {
        assertThat(testling.getContentCreatorAddress(), is(url));
    }

    @Test
    public void getContentCreatorUri() throws Exception {
        assertThat(testling.getContentCreatorUri(), is(URI.create("http://localhost:8000/fs5webedit_123/?locale=de&project=" + 123)));
    }

}
