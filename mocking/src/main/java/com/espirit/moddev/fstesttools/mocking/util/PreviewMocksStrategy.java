package com.espirit.moddev.fstesttools.mocking.util;

import com.espirit.moddev.fstesttools.mocking.MockingBaseContext;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.agency.OperationAgent;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.ui.operations.DisplayElementOperation;
import de.espirit.firstspirit.ui.operations.OpenElementDataFormOperation;
import de.espirit.firstspirit.ui.operations.OpenElementMetaFormOperation;
import de.espirit.firstspirit.ui.operations.RequestOperation;
import de.espirit.firstspirit.ui.operations.SelectFileOperation;
import de.espirit.firstspirit.ui.operations.ShowFormDialogOperation;

import java.util.Locale;

import static org.mockito.Mockito.when;


/**
 * The type Preview mocks strategy.
 */
public class PreviewMocksStrategy extends AbstractSetupMocksStrategy {


    /**
     * Instantiates a new Preview mocks strategy.
     *
     * @param context the context
     * @param locale  the locale
     */
    public PreviewMocksStrategy(final AbstractMockManager context, final Locale locale) {
        super(context, locale);
    }

    @Override
    public void setupMocks() {
        final UIAgent uiAgent = context.requireSpecialist(UIAgent.TYPE);
        final Language language = mockLanguage();
        when(uiAgent.getDisplayLanguage()).thenReturn(language);

        final OperationAgent operationAgent = context.requireSpecialist(OperationAgent.TYPE);
        when(operationAgent.getOperation(RequestOperation.TYPE)).thenReturn(context.getMock(RequestOperation.class));
        when(operationAgent.getOperation(ShowFormDialogOperation.TYPE)).thenReturn(context.getMock(ShowFormDialogOperation.class));
        when(operationAgent.getOperation(SelectFileOperation.TYPE)).thenReturn(context.getMock(SelectFileOperation.class));
        when(operationAgent.getOperation(OpenElementMetaFormOperation.TYPE)).thenReturn(context.getMock(OpenElementMetaFormOperation.class));
        when(operationAgent.getOperation(OpenElementDataFormOperation.TYPE)).thenReturn(context.getMock(OpenElementDataFormOperation.class));
        when(operationAgent.getOperation(DisplayElementOperation.TYPE)).thenReturn(context.getMock(DisplayElementOperation.class));
    }
}
