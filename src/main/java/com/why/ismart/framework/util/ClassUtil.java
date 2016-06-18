package com.why.ismart.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类加载工具，可加载知道类名的类以及加载指定包名的类<br/>
 * 加载初始化框架的Loader以及IoC功能中扫描所有类并过滤出注解类的ClassContext使用到
 * 
 * @author whg
 * @date 2016年6月18日 下午4:07:07
 * @see com.why.ismart.framework.ioc.ClassContext
 * @see com.why.ismart.framework.Loader
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
    
    public static ClassLoader classLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static Class<?> loadClass(String className){
        return loadClass(className, true);
    }
    
    public static Class<?> loadClass(String className, boolean isInitialized){
        try {
            return Class.forName(className, isInitialized, classLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("loadClass error", e);
            throw new RuntimeException(e);
        }
    }
    
    /** packageName such as com.why.ismart*/
    public static Set<Class<?>> loadClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = classLoader().getResources(packageName.replace(".", "/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url == null){
                    continue;
                }
                
                String protocol = url.getProtocol();
                if(protocol.equals("file")){
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                }else if(protocol.equals("jar")){
                    JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                    if(jarURLConnection == null){
                        continue;
                    }
                    JarFile jarFile = jarURLConnection.getJarFile();
                    if(jarFile == null){
                        continue;
                    }
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while(jarEntries.hasMoreElements()){
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if(jarEntryName.endsWith(".class")){
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            doAddClass(classSet, className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("loadClassSet error", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || 
                        (file.isFile() && file.getName().endsWith(".class"));
            }
        });
        for(File file:files){
            String fileName = file.getName();
            if(file.isFile()){
               String className =  fileName.substring(0, fileName.lastIndexOf("."));
               if(StringUtil.isNotEmpty(packageName)){
                   className = packageName + "." + className;
               }
               doAddClass(classSet, className);
            }else if(file.isDirectory()){
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(packagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)){
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }
    
}
