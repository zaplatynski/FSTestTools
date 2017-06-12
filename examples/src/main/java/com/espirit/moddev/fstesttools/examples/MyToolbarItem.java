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

import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.pagestore.Page;
import de.espirit.firstspirit.agency.OperationAgent;
import de.espirit.firstspirit.client.plugin.toolbar.ExecutableToolbarItem;
import de.espirit.firstspirit.client.plugin.toolbar.ToolbarContext;
import de.espirit.firstspirit.ui.operations.RequestOperation;

import javax.swing.Icon;

public class MyToolbarItem implements ExecutableToolbarItem {

    @Override
    public void execute(ToolbarContext toolbarContext) {
        Page page = (Page) toolbarContext.getElement();

        final OperationAgent operationAgent = toolbarContext.requestSpecialist(OperationAgent.TYPE);
        final RequestOperation operation = operationAgent.getOperation(RequestOperation.TYPE);

        operation.perform("Selected page: " + page.getUid());
    }

    @Override
    public String getLabel(ToolbarContext toolbarContext) {
        return "Foobar";
    }

    @Override
    public boolean isEnabled(ToolbarContext toolbarContext) {
        return Store.Type.PAGESTORE.equals(toolbarContext.getStoreType())
               && toolbarContext.getElement() instanceof Page;
    }

    @Override
    public boolean isVisible(ToolbarContext toolbarContext) {
        return true;
    }

    @Override
    public Icon getIcon(ToolbarContext toolbarContext) {
        return null;
    }

    @Override
    public Icon getPressedIcon(ToolbarContext toolbarContext) {
        return null;
    }

    @Override
    public Icon getRollOverIcon(ToolbarContext toolbarContext) {
        return null;
    }
}
