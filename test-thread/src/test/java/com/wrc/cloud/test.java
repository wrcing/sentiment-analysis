package com.wrc.cloud;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/31 19:11
 */

public class test {
    @Test
    public void testLambda() {
        System.out.println("1、-----------------------------------");
        //想要打印的参数
        printNum("aaa",s -> System.out.println(s));

        System.out.println("2、-----------------------------------");
        //想要打印该参数字符的个数
        printNum("aaa",s -> System.out.println(s.length()));

        System.out.println("3、-----------------------------------");
        //将参数和其他字符串拼接
        printNum("aaa",s -> System.out.println("999"+s));

        System.out.println("4、-----------------------------------");
        Consumer<String> s1 = s -> System.out.println(s);
        Consumer<String> s2 = s -> System.out.println(s.length());
        //将s1 和 s2 结合到一起
        s1.andThen(s2).accept("aaa");

    }
    //方法的功能：打印参数
    // 打印好处：
    //          如果方法要使用一个参数，具体参数的使用方式不确定，或者有很多种使用方式
    //          可以将该参数交给消费型接口去完成该参数的使用
    //          后续使用该方法时，不仅要传递实际参数，还要传递消费型接口的实现类对象
    //          消费型接口的对象如何定义： 根据使用的具体需求来定义
    public  void printNum(String str, Consumer<String > con){
        System.out.println("inside");
        con.accept(str);
    }

    interface wrc{
        public void say();
    }
    private String unChangeable = "var in class,lambda can't change";
    private Lock LOCK = new ReentrantLock();

    @Test
    public void testNimingneibulei(){
//        LOCK.lock();

        // 直接使用
        new wrc(){
            String word = "1234";
            @Override
            public void say() {
                System.out.println(word);
            }
        }.say();
        // 作为参数传递
        testParam(new wrc() {
            @Override
            public void say() {
                System.out.println("4321");
            }
        });
        // lambda简化
//        String unChangeable = "var in class,lambda can't change";
        wrc w = ()->{
            unChangeable = "test";
            System.out.println(unChangeable);
            testLambda();
        };
        w.say();
//        unChangeable = "124";
    }

    void testParam(wrc rc){
        rc.say();
    }



    @Test
    public void testJoin() throws InterruptedException {
        Thread thread1 = new Thread(()-> {
            System.out.println("th 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(()->{
            System.out.println("th2 start");
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("th2 end");
        });
        thread2.start();
        thread1.start();

        Thread.sleep(5000);
    }

    @Test
    public void testT() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        //获取list中的所有方法
        Method[] methods = list.getClass().getMethods();
        list.add(33);
        for (Method m : methods){
            System.out.println("==="+m.getName()+","+m.getParameterCount());
            if (m.getName().equals("add")){
                if (m.getParameterTypes().length == 1){
                    System.out.println("this");
                    m.invoke(list, "aaaa");
                }
            }
        }

        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

        Class listClass = Class.forName("java.util.ArrayList");
        Method[] methods1 = listClass.getMethods();
        for (Method m : methods1){
            System.out.println(m.getName());
        }
    }

    static class InsideClass{

    }

    @Test
    public void testInsideClass(){
        InsideClass insideClass = new InsideClass();

        System.out.println(insideClass.getClass().getName());
    }


    @Test
    public void testHashTable(){
        Hashtable<Object, Object> hashtable = new Hashtable<>();
        hashtable.put("wrc", "888");
        hashtable.put("w", "7");
        hashtable.put("r", "8");
        hashtable.put("c", "8");
        System.out.println(hashtable.toString());
    }

    static class Job{
        public Job(String name){
            this.name = name;
        }
        public String name;

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public boolean equals(Object obj) {
            Job job = (Job) obj;
            return name.equals(job.name);
        }
    }

    @Test
    public void testEqual() throws ArithmeticException{
        Job a = new Job("wrc");
        Job b = new Job("wrc");
        System.out.println(a == b);
        System.out.println(a.hashCode() == b.hashCode());
        System.out.println(a.equals(b));

        System.out.println(a.name.equals(b.name));
    }


}


