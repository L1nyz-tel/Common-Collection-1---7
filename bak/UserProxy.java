public class UserProxy implements User{
    User user;
    public UserProxy(){

    }

    public UserProxy(User user){
        this.user = user;
    }

    @Override
    public void show() {
        user.show();
        System.out.println("调用了show()");
    }
    @Override
    public void create()
    {
        System.out.println("创建");
    }
    @Override
    public void update(){
        System.out.println("更新");
    }


    public void execute(User user){

    }

}
