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
import de.espirit.firstspirit.client.plugin.contextmenu.ContextMenuContext;
import de.espirit.firstspirit.client.plugin.contextmenu.ExecutableContextMenuItem;
import de.espirit.firstspirit.ui.operations.RequestOperation;

import javax.swing.Icon;

public class MyContextMenuItem implements ExecutableContextMenuItem {

    @Override
    public void execute(ContextMenuContext contextMenuContext) {
        Page page = (Page) contextMenuContext.getElements().getFirst();

        final OperationAgent operationAgent = contextMenuContext.requestSpecialist(OperationAgent.TYPE);
        final RequestOperation operation = operationAgent.getOperation(RequestOperation.TYPE);

        operation.perform("Selected page: " + page.getUid());
    }

    @Override
    public String getLabel(ContextMenuContext contextMenuContext) {
        return "Foobar";
    }

    @Override
    public Icon getIcon(ContextMenuContext contextMenuContext) {
        return null;
    }

    @Override
    public boolean isEnabled(ContextMenuContext contextMenuContext) {
        return Store.Type.PAGESTORE.equals(contextMenuContext.getStoreType())
               && contextMenuContext.getElements().toList().size() == 1 &&
               contextMenuContext.getElements().getFirst() instanceof Page;
    }

    @Override
    public boolean isVisible(ContextMenuContext contextMenuContext) {
        return true;
    }
}
