package chaschev.lang;

import chaschev.util.Exceptions;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class LangUtils {
    @Nonnull
    public static <T> T elvis(@Nullable T operand, @Nonnull T fallbackTo){
        return operand == null ? fallbackTo : operand;
    }

    @SuppressWarnings("restriction")
    public static sun.misc.Unsafe getUnsafe() {
        try {

            Field singleoneInstanceField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            return (sun.misc.Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            System.err.println("could not get unsafe!");
            e.printStackTrace(System.err);

            LoggerFactory.getLogger(OpenBean.class).error("could not get unsafe!", e);

            throw Exceptions.runtime(e);
        }
    }

    private static final DecimalFormat[] formats= new DecimalFormat[]{
        null,
        new DecimalFormat("#.#"),
        new DecimalFormat("#.##"),
        new DecimalFormat("#.###"),
        new DecimalFormat("#.####")
    };

    public static String toConciseString(double d, int fractionLength){
        long asLong = (long) d;
        if(Math.abs(d - asLong) < 0.00001d){
            return Long.toString(asLong);
        }

        return formats[fractionLength].format(d);
    }

    public static String millisToSec(long millis){
        return toConciseString(millis / 1000D, 1);
    }
}
