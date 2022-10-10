import java.io.PushbackInputStream;
import java.io.Serializable;

public class Person implements Serializable {

    public String name;
    private int age;

    public static int id;
    static {
        System.out.println("静态代码块");
    }
    public static void staticAction(){
        System.out.println("静态方法");
    }

    {
        System.out.println("构造代码块");
    }
    public Person(){System.out.println("无参Person");}
    public Person(String name, int age) {
        System.out.println("有参Person");
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + "'" +
                ", age=" + age + "}";
    }
}
