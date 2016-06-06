package com.why.ismart.test;

public class HelloImpl implements Hello{

    @Override
    public void say(String str) {
        System.out.println(str);
    }

    @Override
    public void talk(String str) {
        System.out.println("talk: "+str);
    }

}
