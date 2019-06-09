package concurrent.cancel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author 杜艮魁
 * @date 2019/6/9
 */
public class PrimeGenerator implements Runnable {

    //fixme 取消策略标志
    private volatile boolean cancel = false;

    private final List<BigInteger> list = new LinkedList<>();

    @Override
    public void run() {
        BigInteger start = BigInteger.ONE;
        //fixme 取消策略检测
        while (!cancel) {
            //返回刚好比当前数值大的素数
            start = start.nextProbablePrime();
            list.add(start);
        }
    }

    //fixme 设置取消策略
    public void cancel() {
        this.cancel = true;
    }

    public List<BigInteger> getList() {
        return new ArrayList(list);
    }

    List<BigInteger> aSecondPrime() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.getList();
    }

    public static void main(String[] args) throws InterruptedException {
        new PrimeGenerator().aSecondPrime().forEach(x -> System.out.println(x));
    }
}
