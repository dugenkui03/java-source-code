package nativemethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description 反射调用方法 todo 也可以通过Unsafe获取某个对象私有变量的值
 * -
 * @Author dugenkui
 **/
public class ReflectMethodTest {
    int tag;

    public ReflectMethodTest(int tag){
        this.tag=tag;
    }

    public int refMethod(){
        return tag;
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ReflectMethodTest rmt=new ReflectMethodTest(6);
        //获取指定名称的方法
        Method method=rmt.getClass().getMethod("refMethod");
        //反射调用方法
        System.out.println(method.invoke(rmt));//output:6
    }
}