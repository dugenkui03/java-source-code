package jvm;

/**
 * @Description getStatic会导致static变量所在的类被初始化
 *              fixme:但是如果引用的static变量是final类型，则在编译阶段(java->class文件)通过常量传播优化，
 *              已经将变量的值存储到了引用类的常量池中，即编译后的引用类class类文件没有被引用类class文件的入口。
 * @Date 2018/9/13 下午2:58
 * -
 * @Author dugenkui
 **/

class ConstClass{
    static{
        System.out.println("init ConstClass!");
    }

    public static final String HELLOW_WORLD="hello world";
}

public class FinalFieldTest {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOW_WORLD);
    }
}
