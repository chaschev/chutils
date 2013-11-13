package chaschev.lang;

import com.google.common.base.Function;
import org.junit.Test;

import static chaschev.lang.Functions2.method;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Functions2Test {
    @Test
    public void testMethod() throws Exception {
        Predicates2Test.Foo foo = new Predicates2Test.Foo("x");

        Function<Predicates2Test.Foo, String> getName = method("getName");

        assertThat(getName.apply(foo)).isEqualTo("x");
    }
}
