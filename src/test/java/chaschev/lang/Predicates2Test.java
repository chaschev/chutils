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
    public static class  Foo{
        String name;

        public Foo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    @Test
    public void testMethodEquals() throws Exception {
        Foo a = new Foo("a");
        assertThat(find(newArrayList(a, new Foo("b")), methodEquals("getName", "a"))).isEqualTo(a);
        assertThat(find(newArrayList(a, new Foo("b")), fieldEquals("name", "a"))).isEqualTo(a);
    }
}
