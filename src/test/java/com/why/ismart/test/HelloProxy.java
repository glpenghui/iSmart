package com.why.ismart.test;

public class HelloProxy implements Hello{

    private final Hello hello;
    
    public HelloProxy(Hello hello) {
        this.hello = hello;
    }

    @Override
    public void say(String str) {
        before();
        hello.say(str);;
        after();
    }
    
    private void before() {
        System.out.println("HelloProxy before");
    }

    private void after() {
        System.out.println("HelloProxy after");
    }

    @Override
    public void talk(String str) {
        before();
        System.out.println("talk: "+str);
        after();
    }

}
