import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadClassTest {
    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
//        Person person = new Person();
//        Person person2 = new Person("a",22);
//静态代码块
//构造代码块
//无参Person
//构造代码块
//有参Person
//Person.id = 1; Person.staticAction(); Class.forName("Person");也会调用静态代码块

//        以下不进行初始化
//        Class c = Person.class;
        ClassLoader cl = ClassLoader.getSystemClassLoader();
//        Class<?> c = Class.forName("Person", false, cl);
//        System.out.println(cl); 双亲委派
//        Class<?> c = cl.loadClass("Person");
// 继承关系       ClassLoader -> SecureClassLoader -> URLClassLoader -> AppClassLoader
// 调用关系       loadClass -> findClass(重写的方法) -> defineClass(从字节码加载类)

//        URLClassLoader 任意类加载 file http jar
//        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:///D:\\Users\\tel\\Downloads\\test\\bak\\")});
//        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("jar:http://localhost:8000/Test.jar/")});
//        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("http://127.0.0.1:8000/")});
//        Class<?> c = urlClassLoader.loadClass("Test");
//        c.newInstance();
//        用来编译的 javac 版本 要对得上，不然编译报错

//        ClassLoader.defineClass 字节码加载任意类 private
//        Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
//        defineClassMethod.setAccessible(true);
//        byte[] code = Files.readAllBytes(Paths.get("D:\\Users\\tel\\Downloads\\test\\bak\\Test.class"));
//        Class c = (Class) defineClassMethod.invoke(cl,"Test",code,0,code.length);
//        c.newInstance();

//        Unsafe.defineClass 字节码加载 public 但不能直接生成
//        Spring 可以直接生成
        Class c = Unsafe.class;
        Field theUnsafeField = c.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
        byte[] code = Files.readAllBytes(Paths.get("D:\\Users\\tel\\Downloads\\test\\bak\\Test.class"));
        Class c2 = (Class) unsafe.defineClass("Test",code,0,code.length,cl,null);
        c2.newInstance();
    }
}
