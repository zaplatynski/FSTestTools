package com.espirit.moddev.fstesttools.tests;

import de.espirit.firstspirit.access.script.Executable;
import de.espirit.firstspirit.access.script.ExecutionException;

import java.io.Writer;
import java.util.Map;

/**
 * Created by Zaplatynski on 08.08.2016.
 */
public class MyExecutable implements Executable {

    @Override
    public Object execute(final Map<String, Object> map) throws ExecutionException {
        return null;
    }

    @Override
    public Object execute(final Map<String, Object> map, final Writer writer, final Writer writer1) throws ExecutionException {
        return null;
    }
}
