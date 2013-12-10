package chaschev.lang;

import org.junit.Test;

import static chaschev.lang.LangUtils.toConciseString;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class LangUtilsTest {
    @Test
    public void testToConciseString() throws Exception {
        assertThat(toConciseString(23.323, 2)).isEqualTo("23.32");
        assertThat(toConciseString(23.329, 2)).isEqualTo("23.33");
        assertThat(toConciseString(23.329, 3)).isEqualTo("23.329");
        assertThat(toConciseString(23.3, 2)).isEqualTo("23.3");
        assertThat(toConciseString(23.30001, 2)).isEqualTo("23.3");
        assertThat(toConciseString(23.00001, 2)).isEqualTo("23");
    }
}
