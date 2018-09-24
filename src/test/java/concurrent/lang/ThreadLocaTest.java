package concurrent.lang;

/**
 * @Description TODO
 * @Date 2018/9/24 下午1:36
 * -
 * @Author dugenkui
 **/


public class ThreadLocaTest {
    static class DemoClass{
        private String name;
        private String age;
        public DemoClass(String name,String age){
            this.name=name; this.age=age;
        }
        public void setName(String name) { this.name = name; }
        public void setAge(String age) { this.age = age; }
    }

    private static ThreadLocal<DemoClass> threadLocal=new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new DemoClass("name","age");
        }
    };

    private static ThreadLocal<Integer> threadLocalback=new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new Integer(1);
        }
    };

    public static void main(String[] args) {
//        for (int i = 0; i < 2; i++) {
//            new Thread(()->threadLocal.get()).start();
//        }

        /**
         * 以下两行代码使得主线程的ThreadLocal.ThreadLocalMap变量中存放两队kv值，key为ThreadLocal对象，v为默认值；
         */
        threadLocal.get();
        threadLocalback.get();
    }
}
