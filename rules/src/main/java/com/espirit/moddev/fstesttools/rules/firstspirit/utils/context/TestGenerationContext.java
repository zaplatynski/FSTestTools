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

package com.espirit.moddev.fstesttools.rules.firstspirit.utils.context;

import de.espirit.common.function.UnaryProcedure;
import de.espirit.firstspirit.access.Connection;
import de.espirit.firstspirit.access.GenerationContext;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.ServicesBroker;
import de.espirit.firstspirit.access.UrlCreatorProvider;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.Resolution;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.schedule.ScheduleContext;
import de.espirit.firstspirit.access.store.ContentProducer;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.PageParams;
import de.espirit.firstspirit.access.store.contentstore.Dataset;
import de.espirit.firstspirit.access.store.mediastore.Media;
import de.espirit.firstspirit.access.store.pagestore.DataProvider;
import de.espirit.firstspirit.access.store.pagestore.Page;
import de.espirit.firstspirit.access.template.Context;
import de.espirit.firstspirit.access.template.Evaluator;
import de.espirit.firstspirit.access.template.TemplateDocument;
import de.espirit.firstspirit.agency.SpecialistsBroker;
import de.espirit.firstspirit.client.common.text.CharacterReplacer;
import de.espirit.firstspirit.generate.UrlCreator;
import de.espirit.firstspirit.io.FileHandle;
import de.espirit.firstspirit.parser.Printable;

import java.awt.Font;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * The type TestContext simulates a BaseContext for test environments.
 *
 * @author e-Spirit AG
 */
public class TestGenerationContext extends TestContext implements GenerationContext, Evaluator {

    private Locale locale = Locale.getDefault();
    private Writer writer;

    /**
     * Instantiates a new Test context.
     *
     * @param broker the broker
     */
    public TestGenerationContext(final SpecialistsBroker broker) {
        super(broker);
    }

    @Override
    public boolean isForceEditorIds() {
        return false;
    }

    @Override
    public void setForceEditorIds(final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHtmlMode() {
        return false;
    }

    @Override
    public void setHtmlMode(final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnaryProcedure<TemplateDocument> getTemplateDocumentTransformer() {
        return null;
    }

    @Override
    public UserService getUserService() {
        return broker.requireSpecialist(ServicesBroker.TYPE).getService(UserService.class);
    }

    @Override
    public Project getProject() {
        return null;
    }

    @Override
    public UrlCreator getUrlCreator() {
        return null;
    }

    @Override
    public UrlCreatorProvider getUrlCreatorProvider() {
        return null;
    }

    @Override
    public boolean isRelease() {
        return false;
    }

    @Override
    public GenerationContext getGenerationContext() {
        return this;
    }

    @Override
    public Language getLanguage() {
        return null;
    }

    @Override
    public boolean getUseMasterLanguageForData() {
        return false;
    }

    @Override
    public void setUseMasterLanguageForData(final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TemplateSet getTemplateSet() {
        return null;
    }

    @Override
    public ContentProducer getNode() {
        return null;
    }

    @Override
    public IDProvider getNavigationContext() {
        return null;
    }

    @Override
    public int getPageIndex() {
        return 0;
    }

    @Override
    public boolean isPreview() {
        return false;
    }

    @Override
    public String toString(final Object o) throws Exception {
        return o.toString();
    }

    @Override
    public Evaluator getEvaluator() {
        return null;
    }

    @Override
    public CharacterReplacer getCharacterReplacer(final boolean b) {
        return null;
    }

    @Override
    public void addDataToContext(final DataProvider dataProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page getPage() {
        return null;
    }

    @Override
    public void setPage(final Page page) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEncoding() {
        return "utf-8";
    }

    @Override
    public Font getFont(final String s) {
        return null;
    }

    @Override
    public Object getVariableValue(final String s) {
        return null;
    }

    @Override
    public void setVariableValue(final String s, final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object resolveReference(final List<Object> objects, final Map<String, Object> stringObjectMap) throws Exception {
        return null;
    }

    @Override
    public void include(final String s, final Object o, final Map<String, Object> stringObjectMap) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(final Object o, final String s) throws Exception {
        return null;
    }

    @Override
    public Object invokeMethod(final Object o, final String s, final List<Object> objects) throws Exception {
        return null;
    }

    @Override
    public void print(final Object o) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getLocaleKey() {
        return locale.getISO3Language();
    }

    @Override
    public void setLocaleKey(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void logError(final int i, final int i2, final String s) {
        logError(s);
    }

    @Override
    public void logError(final int i, final int i2, final String s, final Throwable throwable) {
        logError(s, throwable);
    }

    @Override
    public void pushContext(final Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context pushContext(final String s, final String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context pushContext(final String s, final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context pushContext(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context popContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getOut() {
        return writer;
    }

    @Override
    public void setOut(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void setErrorOut(final Writer writer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setErrorOut(final PrintWriter printWriter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final Printable printable) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Printable getTemplate(final String s, final String s2, final Map<String, Object> stringObjectMap) throws IOException {
        return null;
    }

    @Override
    public String getStack() {
        return null;
    }

    @Override
    public String getTopStackElement() {
        return null;
    }

    @Override
    public void setDefaultExpression(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDefaultExpression() {
        return null;
    }

    @Override
    public TemplateDocument parse(final String s) {
        return null;
    }

    @Override
    public void setMaxStackSize(final int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TemplateDocument parse(final Reader reader) throws IOException {
        return null;
    }

    @Override
    public boolean isDebugging() {
        return false;
    }

    @Override
    public boolean isTemplateInspection() {
        return false;
    }

    @Override
    public Context getPageContext() {
        return null;
    }

    @Override
    public Context getContext(final String s) {
        return null;
    }

    @Override
    public boolean getDeleteDirectory() {
        return false;
    }

    @Override
    public void addCloseable(final Closeable closeable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        //do nothing here
    }

    @Override
    public String getBasePath() {
        return null;
    }

    @Override
    public void setDebugMode(final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getDebugMode() {
        return false;
    }

    @Override
    public void mediaReferenced(final Media media, final Language language, final Resolution resolution) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileHandle getFileHandle(final String s) throws IOException {
        return null;
    }

    @Override
    public Page getLanguageSpecificPage(final Page page) {
        return null;
    }

    @Override
    public Date getStartTime() {
        return null;
    }

    @Override
    public boolean showWebeditButtons() {
        return false;
    }

    @Override
    public ScheduleContext getScheduleContext() {
        return null;
    }

    @Override
    public PageParams getPageParams() {
        return null;
    }

    @Override
    public Dataset getDataset() {
        return null;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Object getProperty(final String s) {
        return null;
    }

    @Override
    public void setProperty(final String s, final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeProperty(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getProperties() {
        return new String[0];
    }
}
