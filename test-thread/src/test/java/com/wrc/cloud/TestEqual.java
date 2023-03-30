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

    @Test
    public  void testa(){
        Integer a1 = 130;
        int a2 =130;
        Integer a3 = Integer.valueOf(130);
        Integer a4  = new Integer(130);

        Integer a5 = 100;
        Integer a6 = new Integer(130);
        Integer a7 = 100;

        System.out.println(a1==a2);
        System.out.println(a1==a3);
        System.out.println(a3==a4);
        System.out.println(a2==a4);

        System.out.println(a5==a6);
        System.out.println("======");

        String str = "admin";

        System.out.println(str=="admin");

    }
}
