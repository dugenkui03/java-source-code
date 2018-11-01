package concurrent.forkjoin;

import java.util.concurrent.*;

/**ForkJoinTask使用示例：
 *  1.构造任务：有返回值使用RecursiveTask，没有返回值使用RecursiveAction；
 *  2.compute()方法中：
 *      1）：定义边界条件：不可在分割的任务、计算方式并返回结果；
 *      2）：分割任务：构造参数；
 *      3）：fork()执行子任务；
 *      4）：join()阻塞获取子任务结果；
 *      5）：合并子任务结果并返回；
 *  fixme:综上,RecursiveTask采用的是分治递归的思想；
 *
 * @author 杜艮魁
 * @date 2018/11/1
 */
public class CountTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 2;//阈值
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sleepWithCatch(TimeUnit.SECONDS, 1);//模拟计算时长
                sum += i;
            }
        } else {
            int mid = (start + end) / 2;
            //fixme 1.构造子任务
            CountTask t1 = new CountTask(start, mid);
            CountTask t2 = new CountTask(mid + 1, end);
            //fixme 2.执行子任务
            t1.fork();
            t2.fork();
            //fixme 3.阻塞等待任务结束并获取结果
            int r1 = t1.join();
            int r2 = t2.join();

            sum = r1 + r2;
        }
        return sum;
    }

    private void sleepWithCatch(TimeUnit tu, int time) {
        try {
            tu.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool fjp = new ForkJoinPool(10);
        CountTask ct = new CountTask(1, 20);

        Future<Integer> res = fjp.submit(ct);
        System.out.println(res.get());
    }
}
