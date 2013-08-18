package com.chaschev.chutils.util;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
* User: chaschev
* Date: 8/25/12
*/
public class JOptOptions {
    protected final static OptionParser parser = new OptionParser();

    protected final OptionSet optionSet;

    public JOptOptions(String[] args) {
        optionSet = parser.parse(args);
    }

    public <T> T get(OptionSpec<T> optionSpec) {
        return optionSet.valueOf(optionSpec);
    }

    public <T> List<T> getList(OptionSpec<T> optionSpec) {
        return optionSet.valuesOf(optionSpec);
    }

    public void printHelpOn(OutputStream sink)  {
        try {
            parser.printHelpOn(sink);
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public boolean has(OptionSpec<?> optionSpec) {
        return optionSet.has(optionSpec);
    }
}
