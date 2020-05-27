package com.lisz.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Test2 {
    private static Record record = new Record(new Entity("e0"));
    private static TraceContextAwareExecutorService service = new TraceContextAwareExecutorService(Executors.newCachedThreadPool(), record); // record从这里传进去

    public static void main(String[] args) throws InterruptedException {

        List<Future<String>> list =  service.invokeAll(Arrays.asList(
                                                        generateQueryCallable("query1"),
                                                        generateQueryCallable("query2")
                                                        ));

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

    private static Callable<String> generateQueryCallable(String query) {
        String name = service.getRecord().getEntity().getName();
        if ("query1".equals(query)) {
            service.getRecord().getEntity().setName("q1 - " + name);
        } else {
            service.getRecord().getEntity().setName(name + " - q2");
        }
        // 下面这一句其实是重写Callable.call(){}中的内容，按理说应该根据query返回不同的值，但是这里验证了能用Record和Entity的对象
        return service.getRecord().getEntity()::getName;
    }
}
/*打印两个线程全部修改结束后的结果（因为上面调用的是invokeAll方法）：
q1 - e0 - q2
q1 - e0 - q2
 */
