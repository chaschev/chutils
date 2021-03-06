package chaschev.lang.reflect;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class ConstructorDescTest {

    @Test
    public void testMatchesStrictly() throws Exception {
        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(ArrayList.class)).matchesStrictly(new ArrayList())
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class)).matchesStrictly(new ArrayList())
        ).isFalse();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(ArrayList.class)).matchesStrictly(new LinkedList())
        ).isFalse();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class, ArrayList.class)).matchesStrictly(new ArrayList(), new ArrayList())
        ).isFalse();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class, ArrayList.class)).matchesStrictly(new ArrayList(), new LinkedList())
        ).isFalse();
    }


    @Test
    public void testMatches() throws Exception {
        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(ArrayList.class)).matches(new ArrayList())
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class)).matches(new ArrayList())
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(ArrayList.class)).matches(new LinkedList())
        ).isFalse();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class, ArrayList.class)).matches(new ArrayList(), new ArrayList())
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class, ArrayList.class)).matches(new ArrayList(), new LinkedList())
        ).isFalse();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(List.class, ArrayList.class)).matches(new ArrayList())
        ).isFalse();

        //boxing

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(int.class)).matches(int.class)
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(int.class)).matches(Integer.class)
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(Double.class)).matches(double.class)
        ).isTrue();

        assertThat(
            new ConstructorDesc(Mock.class.getConstructor(String.class, int.class)).matches(String.class, Integer.class)
        ).isTrue();
    }
}
