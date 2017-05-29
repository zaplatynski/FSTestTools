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

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores.ModifyStoreCommand;
import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.common.util.Listable;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.store.Store;
import de.espirit.firstspirit.access.store.StoreElement;
import de.espirit.firstspirit.access.store.mediastore.MediaStoreRoot;
import de.espirit.firstspirit.access.store.templatestore.FormatTemplates;
import de.espirit.firstspirit.access.store.templatestore.LinkTemplates;
import de.espirit.firstspirit.access.store.templatestore.PageTemplates;
import de.espirit.firstspirit.access.store.templatestore.Scripts;
import de.espirit.firstspirit.access.store.templatestore.TemplateStoreRoot;
import de.espirit.firstspirit.access.store.templatestore.Workflows;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class ZipImportCommandsTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @Rule
    public MockitoRule injectMocks = MockitoJUnit.rule();

    private File zipFile = File.createTempFile("test",".zip");
    private File txtFile = File.createTempFile("test",".txt");

    @ObjectUnderTest
    private ZipImportParameters parameters = new ZipImportParameters("MyProject", zipFile);

    @Mock
    private Project project;

    @Mock
    private UserService userService;

    @Mock
    private TemplateStoreRoot templateStoreRoot;

    @Mock
    private FormatTemplates formatTemplates;

    @Mock
    private PageTemplates pageTemplates;

    @Mock
    private LinkTemplates linkTemplates;

    @Mock
    private Workflows workflows;

    @Mock
    private Scripts scriptes;

    @Mock
    private MediaStoreRoot mediaStoreRoot;

    @Mock
    private Store mediaStore;

    @Mock
    private Listable<StoreElement> elements;

    @DataPoints
    public static ZipImportCommands[] testCases = ZipImportCommands.values();

    public ZipImportCommandsTest() throws IOException {
        //needed for exception handling
    }

    @Before
    public void setUp() throws Exception {
        fillZipWithContent();
        final Connection connection = parameters.getConnection();
        final String projectName = parameters.getProjectName();

        when(connection.getProjectByName(projectName)).thenReturn(project);

        when(project.getUserService()).thenReturn(userService);

        when(userService.getStore(Store.Type.TEMPLATESTORE, false)).thenReturn(templateStoreRoot);
        when(userService.getStore(Store.Type.MEDIASTORE, false)).thenReturn(mediaStoreRoot);

        when(templateStoreRoot.getFormatTemplates()).thenReturn(formatTemplates);
        when(templateStoreRoot.getPageTemplates()).thenReturn(pageTemplates);
        when(templateStoreRoot.getLinkTemplates()).thenReturn(linkTemplates);
        when(templateStoreRoot.getWorkflows()).thenReturn(workflows);
        when(templateStoreRoot.getScripts()).thenReturn(scriptes);

        when(mediaStoreRoot.getStore()).thenReturn(mediaStore);

        when(formatTemplates.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);
        when(pageTemplates.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);
        when(linkTemplates.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);
        when(workflows.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);
        when(mediaStore.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);
        when(scriptes.importStoreElements(any(ZipFile.class), any(ModuleImportHandler.class))).thenReturn(elements);

        when(elements.iterator()).thenReturn(Collections.emptyIterator());
    }

    private void fillZipWithContent() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(txtFile,"rw");
        raf.writeUTF("Test");
        raf.close();
        try (FileOutputStream fos = new FileOutputStream(zipFile)) {
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                try (FileInputStream fis = new FileInputStream(txtFile)) {
                    ZipEntry zipEntry = new ZipEntry("test.txt");
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                    zos.flush();
                    zos.closeEntry();
                    fos.flush();
                }
            }
        }
        zipFile.deleteOnExit();
        txtFile.deleteOnExit();
    }

    @Theory
    public void testCommand(ZipImportCommands command) throws Exception {
        final ZipImportResult result = command.execute(parameters);

        assertThat("ZipImportResult should never be null", result, is(ZipImportResult.VOID));

        verify(formatTemplates, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
        verify(pageTemplates, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
        verify(linkTemplates, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
        verify(workflows, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
        verify(mediaStore, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
        verify(scriptes, atMost(1)).importStoreElement(any(ZipFile.class), any(ModuleImportHandler.class));
    }

    @Test
    public void testImplementation() throws Exception {
        assertThat("Expect implementation of interface", ZipImportCommands.class.getInterfaces(), hasItemInArray(FsConnRuleCommand.class));
    }
}
