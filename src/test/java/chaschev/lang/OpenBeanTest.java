package chaschev.lang;

import chaschev.lang.reflect.Mock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static chaschev.lang.Iterables2.projectMethod;
import static chaschev.lang.OpenBean.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: chaschev
 * Date: 2/25/13
 */
public class OpenBeanTest {
    public static class TestBean extends ArrayList{
        public static final String staticString = "ss1";

        private String foo(ArrayList x){
            return x.toString();
        }

        private String foo(Object x, Object y){
            return y.toString();
        }

        private String foo(Object x, int y, Object z){
            return z.toString();
        }

        @Override
        public String toString() {
            return "TB";
        }
    }

    public static class Copy1{
        String s1 = "Copy1s1";
    }

    public static class Copy2{
        String s2 = "Copy2s2";
    }

    public static class Copy3{
        String s1 = "Copy3s1";
        String s2 = "Copy3s2";
    }

    @Test
    public void testInvoke() throws Exception {
//        TestBean bean = new TestBean();
//
//        bean.indexOf(bean);
//        assertThat(new OpenBean(bean).invoke("indexOf", bean)).isEqualTo(-1);
//        assertThat(new OpenBean(bean).invoke("foo", bean)).isEqualTo("TB");
//        assertThat(new OpenBean(bean).invoke("foo", bean, bean)).isEqualTo("TB");
//        assertThat(new OpenBean(bean).invoke("foo", bean, 2)).isEqualTo("2");
//        assertThat(new OpenBean(bean).invoke("foo", bean, 3, "s")).isEqualTo("s");
    }

    @Test
    public void testCopyFields(){
        Copy1 copy1;
        Copy2 copy2;
        Copy3 copy3;

        copy1 = new Copy1();
        copy2 = new Copy2();
        copy3 = new Copy3();

        OpenBean.copyFields(copy1, copy2);

        assertThat(copy1.s1).isEqualTo(new Copy1().s1);
        assertThat(copy2.s2).isEqualTo(new Copy2().s2);

        ///

        copy1 = new Copy1();
        copy3 = new Copy3();

        OpenBean.copyFields(copy1, copy3);

        assertThat(copy1.s1).isEqualTo(new Copy3().s1);
        assertThat(copy3.s1).isEqualTo(new Copy3().s1);
        assertThat(copy3.s2).isEqualTo(new Copy3().s2);

        ///

        copy1 = new Copy1();
        copy2 = new Copy2();
        copy3 = new Copy3();

        OpenBean.copyFields(copy3, copy1);

        assertThat(copy1.s1).isEqualTo(new Copy1().s1);
        assertThat(copy3.s1).isEqualTo(new Copy1().s1);
        assertThat(copy3.s2).isEqualTo(new Copy3().s2);
    }



    @Test
    public void testCopyFieldsFromMap(){
        Copy1 copy1 = new Copy1();

//        OpenBean2.copyFields(copy1, new BasicDBObject("s2", "Copy2s2"));
        HashMap<String, String> map;
        map = new HashMap<String, String>();
        map.put("s2", "Copy2s2");
        OpenBean.copyFields(copy1, map);

        assertThat(copy1.s1).isEqualTo(new Copy1().s1);

        map = new HashMap<String, String>();
        map.put("s1", "v3");
        OpenBean.copyFields(copy1, map);

        assertThat(copy1.s1).isEqualTo("v3");

    }

    @Test
    public void testStatic() throws Exception {
        assertThat(getStaticFieldValue(TestBean.class, "staticString")).isEqualTo("ss1");
    }

    @Test
    public void testNewInstance() throws Exception {
        assertThat(newInstance(Mock.class, false, "test", 15).c).isEqualTo(5);

        assertThat(newInstance(Mock.class, new ArrayList()).c).isEqualTo(1);
        assertThat(newInstance(Mock.class, true, new ArrayList()).c).isEqualTo(1);
        assertThat(newInstance(Mock.class, false, new LinkedList()).c).isEqualTo(3);
        assertThat(newInstance(Mock.class, new LinkedList()).c).isEqualTo(3);
        assertThat(newInstance(Mock.class, new LinkedList(), new ArrayList()).c).isEqualTo(2);

        assertThat(getConstructorDesc(Mock.class, true, new LinkedList())).isNull();
        assertThat(getConstructorDesc(Mock.class, true, LinkedList.class)).isNull();

        assertThat(getConstructorDesc(Mock.class, false, new LinkedList())).isNotNull();
        assertThat(getConstructorDesc(Mock.class, false, LinkedList.class)).isNotNull();

        assertThat(getConstructorDesc(Mock.class, new LinkedList())).isNotNull();
        assertThat(getConstructorDesc(Mock.class, LinkedList.class)).isNotNull();

        assertThat(getConstructorDesc(Mock.class, true, new ArrayList(), new LinkedList())).isNull();
        assertThat(getConstructorDesc(Mock.class, true, ArrayList.class, LinkedList.class)).isNull();
    }

    public static class FieldsOfTypeTest{
        String s1 = "Copy3s1";
        String s2 = "Copy3s2";
        int i = 3;

        List strictField ;

        ArrayList nonStrictField;
    }

    @Test
    public void testFieldsOfType() throws Exception {
        assertThat(
            projectMethod(fieldsOfType(FieldsOfTypeTest.class, String.class), "getName")).containsOnly("s1", "s2");

        assertThat(
            projectMethod(fieldsOfType(FieldsOfTypeTest.class, int.class), "getName")).containsOnly("i");

        assertThat(
            projectMethod(fieldsOfType(FieldsOfTypeTest.class, List.class), "getName")).containsOnly("strictField", "nonStrictField");

        assertThat(
            projectMethod(fieldsOfType(FieldsOfTypeTest.class, List.class, true), "getName")).containsOnly("strictField");
    }

    @Test
    public void testInterface() throws Exception {
        methods(List.class);
    }
}
