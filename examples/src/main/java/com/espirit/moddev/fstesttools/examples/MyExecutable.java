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


import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.script.Executable;
import de.espirit.firstspirit.agency.OperationAgent;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.ui.operations.RequestOperation;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The type MyExecutable can be used in both environments, ContentCreator and SiteArchitect.
 * It uses for UI interactions with users the OperationAgent's RequestOperation.
 *
 * @see OperationAgent
 * @see RequestOperation
 */
public class MyExecutable implements Executable {

    @Override
    public Object execute(Map<String, Object> map) {

        final BaseContext context = (BaseContext) map.get("context");

        final Locale locale;
        if(context.is(BaseContext.Env.WEBEDIT)) {
            final WebeditUiAgent agent = context.requestSpecialist(WebeditUiAgent.TYPE);
            locale = agent.getLocale();
        } else {
            final UIAgent uiAgent = context.requireSpecialist(UIAgent.TYPE);
            locale = uiAgent.getDisplayLanguage().getLocale();
        }

        final ResourceBundle bundle = ResourceBundle.getBundle("com.espirit.moddev.fstesttools.examples.MainResources", locale);

        final OperationAgent operationAgent = context.requireSpecialist(OperationAgent.TYPE);
        final RequestOperation requestOperation = operationAgent.getOperation(RequestOperation.TYPE);

        requestOperation.addNo();
        final RequestOperation.Answer yes = requestOperation.addYes();
        final String message = bundle.getString("message");
        final RequestOperation.Answer answer = requestOperation.perform(message);

        return answer == yes;
    }

    @Override
    public Object execute(Map<String, Object> map, Writer writer, Writer writer1) {
        return execute(map);
    }
}
