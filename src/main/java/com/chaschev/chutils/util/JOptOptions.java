package com.chaschev.chutils.util;

import joptsimple.*;
import joptsimple.internal.AbbreviationMap;

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

    public String printHelpOn(){
        return printHelpOn(160, 2);
    }

    public String printHelpOn(int desiredOverallWidth, int desiredColumnSeparatorWidth)  {
//        try {
        return new BuiltinHelpFormatter(desiredOverallWidth, desiredColumnSeparatorWidth).format(((AbbreviationMap)OpenBean2.getFieldValue2(
            parser, "recognizedOptions")).toJavaUtilMap());
//            parser.printHelpOn(sink);
//        } catch (IOException e) {
//            throw Exceptions.runtime(e);
//        }
    }

    public boolean has(OptionSpec<?> optionSpec) {
        return optionSet.has(optionSpec);
    }
}
