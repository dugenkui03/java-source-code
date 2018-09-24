package jvm;

/**
 * @Description 初始化、唯一性
 *          1. Class.forName("package.Classname")会初始化类，但是loadClass("package.ClassName")不会初始化类；
 *          2. 类必须和类加载器一块确定类的唯一性。
 * @Date 2018/9/13 下午4:57
 * -
 * @Author dugenkui
 **/
class InitDemo{
    static{
        System.out.println("init InitDemo");
    }
}

class MyClassLoader extends ClassLoader{
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }
}

public class TwoLoadClassMethodTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //Class.forName("jvm.InitDemo"); 初始化类，执行静态代码块代码

        //true代表解析类，但不会初始化类
        Object obj;
        MyClassLoader cl=new MyClassLoader();
        System.out.println((obj=cl.loadClass("jvm.InitDemo", true)) instanceof jvm.InitDemo);
    }
}
