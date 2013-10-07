package chaschev.lang;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class LangUtils {
    @Nonnull
    public static <T> T elvis(@Nullable T operand, @Nonnull T fallbackTo){
        return operand == null ? fallbackTo : operand;
    }

    public static <T> List<T> removeDupsInSortedList(List<T> list) {
        int w = 0;
        for (int r = 0, listSize = list.size(); r < listSize; r++) {
            if (r == 0) {
                w++;
                continue;
            }

            if (!list.get(r).equals(list.get(r - 1))) {
                list.set(w, list.get(r));
                w++;
            }
        }

        for (int last = list.size() - 1; last >= w; last--) {
            list.remove(last);
        }

        return list;
    }

    public static <T> List<T> newFilledArrayList(int size, T value) {
        List<T> list = Lists.newArrayListWithExpectedSize(size);

        for (int i = 0; i < size; i++) {
            list.add(value);
        }

        return list;
    }
}
