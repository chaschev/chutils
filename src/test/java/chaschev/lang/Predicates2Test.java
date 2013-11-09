package chaschev.lang;

import org.junit.Test;

import static chaschev.lang.Predicates2.fieldEquals;
import static chaschev.lang.Predicates2.methodEquals;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Predicates2Test {
    class  Foo{
        String name;

        Foo(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
    @Test
    public void testMethodEquals() throws Exception {
        Foo a = new Foo("a");
        assertThat(find(newArrayList(a, new Foo("b")), methodEquals("a", "getName"))).isEqualTo(a);
        assertThat(find(newArrayList(a, new Foo("b")), fieldEquals("a", "name"))).isEqualTo(a);
    }
}
