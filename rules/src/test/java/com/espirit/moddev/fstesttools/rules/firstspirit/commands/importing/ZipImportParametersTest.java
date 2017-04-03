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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.importing;

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.AbstractParametersTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Created by zaplatynski on 03.04.2017.
 */
public class ZipImportParametersTest extends AbstractParametersTest<ZipImportParameters> {

    @Rule
    public TemporaryFolder tempFiles = new TemporaryFolder();
    private File zipFile;

    @Override
    protected ZipImportParameters createTestling() throws Exception {
        zipFile = tempFiles.newFile();
        return new ZipImportParameters("MyProject", zipFile);
    }

    @Test
    public void testGetZip() throws Exception {
        assertThat("Expect non null value", testling.getZip(), is(sameInstance(zipFile)));
    }

    @Test
    public void testGetConnection() throws Exception {
        assertThat("Expect non null value", testling.getConnection(), is(notNullValue()));
    }

}
