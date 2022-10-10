public class UserImpl implements User{
    public UserImpl(){

    }

    @Override
    public void show(){
        System.out.println("展示");
    }

    public void create()
    {
        System.out.println("创建");
    }

    public void update(){
        System.out.println("更新");
    }

}
