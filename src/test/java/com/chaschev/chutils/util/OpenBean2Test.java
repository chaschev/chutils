package com.chaschev.chutils.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: chaschev
 * Date: 2/25/13
 */
public class OpenBean2Test {
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

        OpenBean2.copyFields(copy1, copy2);

        assertThat(copy1.s1).isEqualTo(new Copy1().s1);
        assertThat(copy2.s2).isEqualTo(new Copy2().s2);

        ///

        copy1 = new Copy1();
        copy3 = new Copy3();

        OpenBean2.copyFields(copy1, copy3);

        assertThat(copy1.s1).isEqualTo(new Copy3().s1);
        assertThat(copy3.s1).isEqualTo(new Copy3().s1);
        assertThat(copy3.s2).isEqualTo(new Copy3().s2);

        ///

        copy1 = new Copy1();
        copy2 = new Copy2();
        copy3 = new Copy3();

        OpenBean2.copyFields(copy3, copy1);

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
        OpenBean2.copyFields(copy1, map);

        assertThat(copy1.s1).isEqualTo(new Copy1().s1);

        map = new HashMap<String, String>();
        map.put("s1", "v3");
        OpenBean2.copyFields(copy1, map);

        assertThat(copy1.s1).isEqualTo("v3");

    }

    @Test
    public void testStatic() throws Exception {
        assertThat(OpenBean2.getStaticFieldValue(TestBean.class, "staticString")).isEqualTo("ss1");
    }
}
