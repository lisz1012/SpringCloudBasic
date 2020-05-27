package com.lisz.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Test {
    private static Record record = new Record(new Entity("e0"));

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        Callable<String> callable1 = () -> {
            System.out.println(Thread.currentThread().getName());
            record.getEntity().setName("e1");
            return record.getEntity().getName();
        };

        Callable<String> callable2 = () -> {
            System.out.println(Thread.currentThread().getName());
            record.getEntity().setName("e2");
            return record.getEntity().getName();
        };

        Callable<String> callable3 = () -> {
            System.out.println(Thread.currentThread().getName());
            record.getEntity().setName("e3");
            return record.getEntity().getName();
        };

        List<Future<String>> list =  service.invokeAll(Arrays.asList(callable1, callable2, callable3));

        list.forEach(f-> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }
}
/*打印结果，顺序可能会跟调用顺序（1，2，3）不一样：
pool-1-thread-1
pool-1-thread-3
pool-1-thread-2
e1
e2
e3
 */
