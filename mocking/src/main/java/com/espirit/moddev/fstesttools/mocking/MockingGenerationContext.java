package com.espirit.moddev.fstesttools.mocking;

import de.espirit.common.function.UnaryProcedure;
import de.espirit.firstspirit.access.GenerationContext;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.UrlCreatorProvider;
import de.espirit.firstspirit.access.project.Resolution;
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
import de.espirit.firstspirit.client.common.text.CharacterReplacer;
import de.espirit.firstspirit.generate.UrlCreator;
import de.espirit.firstspirit.io.FileHandle;
import de.espirit.firstspirit.parser.Printable;

import org.mockito.Mockito;

import java.awt.Font;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.when;


/**
 * The type Mocking generation context.
 */
public class MockingGenerationContext extends AbstractMockingGenerationScriptContext implements GenerationContext {

    private final Context context;
    private final Collection<Closeable> closeables;
    private final ScheduleContext scheduleContext;
    private final PageParams pageParams;
    private final Dataset dataSet;
    private final Context pageContext;
    private final Deque<String> stack;
    private final Date startTime = new Date();
    private final HashMap<String, FileHandle> fileHandles;
    private final Printable printable;
    private final LinkedList<Context> contextStack;
    private Locale locale;
    private final UrlCreator urlCreator;
    private final UrlCreatorProvider urlCreatorProvider;
    private boolean useMasterLanguageForData;
    private final ContentProducer node;
    private final IDProvider navigationContext;
    private Page page;
    private final String encoding;
    private final Font font;
    private String localKey;
    private final Evaluator evaluator;
    private final CharacterReplacer characterReplacer;
    private Writer writer;
    private Writer errorOut;
    private boolean debugMode;
    private boolean deleteDirectory;
    private Object defaultExpression;
    private String basePath;
    private boolean htmlMode;
    private boolean forceEditorIds;

    /**
     * Instantiates a new Mocking generation context.
     *
     * @param locale                  the locale
     * @param preview                 the preview
     * @param release                 the release
     * @param enableServiceBrokerFake the enable service broker fake
     * @param supportedEnvironments   the supported environments
     */
    public MockingGenerationContext(final Locale locale, final boolean preview, final boolean release, final boolean enableServiceBrokerFake,
                                    final Env supportedEnvironments) {
        super(locale, preview, release, enableServiceBrokerFake, supportedEnvironments);
        this.locale = locale;
        scheduleContext = new MockingScheduleContext(locale, supportedEnvironments);
        fileHandles = new HashMap<>();
        printable = Mockito.mock(Printable.class);
        stack = new LinkedList<>();
        contextStack = new LinkedList<>();
        pageContext = Mockito.mock(Context.class);
        dataSet = Mockito.mock(Dataset.class);
        pageParams = Mockito.mock(PageParams.class);
        closeables = new LinkedList<>();
        context = Mockito.mock(Context.class);
        urlCreator = Mockito.mock(UrlCreator.class);
        urlCreatorProvider = Mockito.mock(UrlCreatorProvider.class);
        node = Mockito.mock(ContentProducer.class);
        navigationContext = Mockito.mock(IDProvider.class);
        encoding = Charset.forName("UTF-8").name();
        font = Font.decode(Font.MONOSPACED);
        evaluator = Mockito.mock(Evaluator.class);
        characterReplacer = Mockito.mock(CharacterReplacer.class);
    }


    @Override
    public UrlCreator getUrlCreator() {
        return urlCreator;
    }

    @Override
    public UrlCreatorProvider getUrlCreatorProvider() {
        return urlCreatorProvider;
    }

    @Override
    public boolean getUseMasterLanguageForData() {
        return useMasterLanguageForData;
    }

    @Override
    public void setUseMasterLanguageForData(final boolean b) {
        useMasterLanguageForData = b;
    }

    @Override
    public ContentProducer getNode() {
        return node;
    }

    @Override
    public IDProvider getNavigationContext() {
        return navigationContext;
    }

    @Override
    public int getPageIndex() {
        return 0;
    }

    @Override
    public Evaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public CharacterReplacer getCharacterReplacer(final boolean b) {
        return characterReplacer;
    }

    @Override
    public void addDataToContext(final DataProvider dataProvider) {

    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(final Page page) {
        this.page = page;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public Font getFont(final String s) {
        return font;
    }

    @Override
    public Object getVariableValue(final String s) {
        return getScheduleContext().getVariable(s);
    }

    @Override
    public void setVariableValue(final String s, final Object o) {
        if (o instanceof Serializable) {
            getScheduleContext().setVariable(s, (Serializable) o);
        }
    }

    @Override
    public Object resolveReference(final List<Object> objects, final Map<String, Object> stringObjectMap) throws Exception {
        return null;
    }

    @Override
    public void include(final String s, final Object o, final Map<String, Object> stringObjectMap) throws IOException {

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
        if (o != null) {
            logInfo(o.toString());
        }
    }

    @Override
    public Context getContext() {
        return context;
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
        return localKey;
    }

    @Override
    public void setLocaleKey(final String s) {
        localKey = s;
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
    public Context pushContext(final String s, final String s2) {
        return null;
    }

    @Override
    public Context pushContext(final String s) {
        return null;
    }

    @Override
    public Context pushContext(final String s, final Object o) {
        return null;
    }

    @Override
    public void pushContext(final Context context) {
        contextStack.push(context);
    }

    @Override
    public Context popContext() {
        if (!contextStack.isEmpty()) {
            return contextStack.pop();
        }
        return null;
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
        errorOut = writer;
    }

    @Override
    public void setErrorOut(final PrintWriter printWriter) {
        errorOut = printWriter;
    }

    @Override
    public void print(final Printable printable) throws IOException {
        printable.print(getEvaluator());
    }

    @Override
    public Printable getTemplate(final String s, final String s2, final Map<String, Object> stringObjectMap) throws IOException {
        return printable;
    }

    @Override
    public String getStack() {
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return null;
    }

    @Override
    public String getTopStackElement() {
        if (!stack.isEmpty()) {
            return stack.getFirst();
        }
        return null;
    }

    @Override
    public void setDefaultExpression(final Object o) {
        defaultExpression = o;
    }

    @Override
    public Object getDefaultExpression() {
        return defaultExpression;
    }

    @Override
    public void setMaxStackSize(final int i) {
        //empty
    }

    @Override
    public TemplateDocument parse(final String s) {
        return null;
    }

    @Override
    public TemplateDocument parse(final Reader reader) throws IOException {
        return null;
    }

    @Override
    public boolean isDebugging() {
        return debugMode;
    }

    @Override
    public boolean isTemplateInspection() {
        return false;
    }

    @Override
    public UnaryProcedure<TemplateDocument> getTemplateDocumentTransformer() {
        return null;
    }

    @Override
    public Context getPageContext() {
        return pageContext;
    }

    @Override
    public Context getContext(final String s) {
        return context;
    }

    @Override
    public boolean getDeleteDirectory() {
        return deleteDirectory;
    }

    @Override
    public void addCloseable(final Closeable closeable) {
        closeables.add(closeable);
    }

    @Override
    public void close() throws IOException {
        for (final Closeable closeable : closeables) {
            closeable.close();
        }
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(final String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void setDebugMode(final boolean b) {
        debugMode = b;
    }

    @Override
    public boolean getDebugMode() {
        return debugMode;
    }

    @Override
    public void setHtmlMode(final boolean b) {
        htmlMode = b;
    }

    @Override
    public boolean isHtmlMode() {
        return htmlMode;
    }

    @Override
    public void mediaReferenced(final Media media, final Language language, final Resolution resolution) throws IOException {

    }

    @Override
    public FileHandle getFileHandle(final String s) throws IOException {
        if (!fileHandles.containsKey(s)) {
            final FileHandle fileHandle = getMock(FileHandle.class);
            when(fileHandle.getName()).thenReturn(s);
            fileHandles.put(s, fileHandle);
        }
        return fileHandles.get(s);
    }

    @Override
    public Page getLanguageSpecificPage(final Page page) {
        return page;
    }

    @Override
    public Date getStartTime() {
        return new Date(startTime.getTime());
    }

    @Override
    public boolean showWebeditButtons() {
        return is(Env.WEBEDIT);
    }

    @Override
    public ScheduleContext getScheduleContext() {
        return scheduleContext;
    }

    @Override
    public PageParams getPageParams() {
        return pageParams;
    }

    @Override
    public Dataset getDataset() {
        return dataSet;
    }

    @Override
    public GenerationContext getGenerationContext() {
        return this;
    }

    @Override
    public void setForceEditorIds(final boolean b) {
        forceEditorIds = b;
    }

    @Override
    public boolean isForceEditorIds() {
        return forceEditorIds;
    }
}
