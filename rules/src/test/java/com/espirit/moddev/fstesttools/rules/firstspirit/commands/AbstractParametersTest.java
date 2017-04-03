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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands;


import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreParameters;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public abstract class AbstractParametersTest<P extends FsConnRuleCmdParamBean> {

    protected P testling;

    @Before
    public void setUp() throws Exception {
        testling = createTestling();
        injectMocksForCDI(testling);
    }

    protected abstract P createTestling() throws Exception;

    private  void injectMocksForCDI(final P parameters){
        final Field[] declaredFields = parameters.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Inject.class)) {
                final Class<?> type = declaredField.getType();
                final boolean accessible = declaredField.isAccessible();
                if (!accessible) {
                    declaredField.setAccessible(true);
                }
                try {
                    declaredField.set(parameters, mock(type));
                } catch (IllegalAccessException e) {
                    fail(e.toString());
                } finally {
                    declaredField.setAccessible(accessible);
                }
            }
        }
    }

    @Test
    public void testGetProjectName() throws Exception {
        assertThat("Expect specific value", testling.getProjectName(), is(notNullValue()));
    }

}
