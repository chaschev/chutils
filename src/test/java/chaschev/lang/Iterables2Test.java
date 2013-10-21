package chaschev.lang;

import org.junit.Test;

import java.util.ArrayList;

import static chaschev.lang.Iterables2.projectField;
import static chaschev.lang.Iterables2.projectMethod;
import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Iterables2Test {
    public static class Foo{
        String s;

        private Foo(String s) {
            this.s = s;
        }

        public String name() {
            return s;
        }
    }

    @Test
    public void testProjectField() throws Exception {
        assertThat(newArrayList(
            projectField(sample3(), Foo.class, String.class, "s")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            projectField(sample3(), "s")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            projectMethod(sample3(), Foo.class, String.class, "name")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            projectMethod(sample3(), "name")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            Lists2.projectField(sample3(), Foo.class, String.class, "s")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            Lists2.projectField(sample3(), "s")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            Lists2.projectMethod(sample3(), Foo.class, String.class, "name")))
            .containsExactly("1", "2", "3");

        assertThat(newArrayList(
            Lists2.projectMethod(sample3(), "name")))
            .containsExactly("1", "2", "3");
    }

    private static ArrayList<Foo> sample3() {
        return newArrayList(new Foo("1"), new Foo("2"), new Foo("3"));
    }

    @Test
    public void testProjectMethod() throws Exception {

    }
}
