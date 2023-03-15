package com.wrc.cloud;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/10 20:36
 */
@SpringBootTest
public class TestEqual {

    @Test
    public void showDouble(){
        Double a = 100.0;
        Double b = 100.0;
        System.out.println(a.equals(b));
        System.out.println(a);
        double aa= 100.0;
        double bb = 100.0;
        System.out.println(aa == bb);
        System.out.println(a == aa);
        int t= -1;
        long tt = t;
        System.out.println(tt);
    }


    class A{
        public String name;
    }

    class B implements Cloneable{
        A a;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Object clone = super.clone();
            B b = (B) clone;
            b.a = new A();
            return clone;
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        B b = new B();
        b.a = new A();
        b.a.name = "wrc";

        B cloneB = (B) b.clone();
        b.a.name = "changed";

        System.out.println(cloneB.a.name);

        System.out.println(b);
        System.out.println(cloneB);
        System.out.println("====");
        System.out.println(b.a);
        System.out.println(cloneB.a);
        System.out.println("====");
        System.out.println(b.a.name);
        System.out.println(cloneB.a.name);
    }
}
