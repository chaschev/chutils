package com.chaschev.chutils.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class LangUtils {
    @Nonnull
    public static <T> T elvis(@Nullable T operand, @Nonnull T fallbackTo){
        return operand == null ? fallbackTo : operand;
    }
}
