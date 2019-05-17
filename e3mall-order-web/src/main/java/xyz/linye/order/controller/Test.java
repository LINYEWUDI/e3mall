package xyz.linye.order.controller;

public class Test {

    static int x = 10;

    static {
        x+=5;
    }

    public static void main(String[] args) {

        System.out.println(x);

//        String s1 = new String("aaa");
//        String s2 = s1;
//        //s1 = new String("bbbb");
//
//
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s1==s2);


    }

    static {
        x/=5;
    }
}
