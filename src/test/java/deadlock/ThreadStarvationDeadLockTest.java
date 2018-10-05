package deadlock;

import java.util.concurrent.*;

/**
 * @Description 线程饥饿死锁
 * @Date 2018/10/5 下午3:07
 * -
 * @Author dugenkui
 **/

public class ThreadStarvationDeadLockTest {
    static ExecutorService exec= Executors.newSingleThreadExecutor();

    public static class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName){
            this.fileName=fileName;
        }

        @Override
        public String call() {
            return "after loaded file";
        }
    }

    public static class ReaderPageTask implements Callable<String>{
        @Override
        public String call() throws ExecutionException, InterruptedException {
            Future<String> head,footer;
            head=exec.submit(new LoadFileTask("head.html"));
            footer=exec.submit(new LoadFileTask("footer.html"));

            String page=" readering page ";

            return head.get()+page+footer.get();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //fixme：即使使用的不是单线程线程池，如果所有线程都正好取得第一类任务，此时也会造成死锁
        ReaderPageTask readerPage=new ReaderPageTask();
        Future<String> result=exec.submit(readerPage);

        System.out.println(result.get());
        exec.shutdown();
    }
}
