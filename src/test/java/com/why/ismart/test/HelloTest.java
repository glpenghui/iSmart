package com.why.ismart.test;

import org.junit.Test;

public class HelloTest {

    @Test
    public void testProxy(){
        main(new String[]{});
    }
    
    public static void main(String[] args) {
        Hello hello = new HelloProxy(new HelloImpl());
        hello.say("hello");
        hello.talk("world");
        
        DynamicProxy<Hello> jdkProxy = new DynamicProxy<Hello>(new HelloImpl());
        //Hello helloProxy = (Hello)Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), proxy);
        Hello helloJdkProxy = jdkProxy.getProxy();
        helloJdkProxy.say("why");
        helloJdkProxy.talk("world");
        
        Hello helloCglProxy = CGLibProxy.getProxy(HelloImpl.class);
        helloCglProxy.say("test");
        helloCglProxy.talk("world");
    }
    
}
