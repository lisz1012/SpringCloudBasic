package com.lisz.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test3 {
    public static void main(String[] args) {
        Entity entity = new Entity("e0");
        Record record = new Record(entity);
        Runnable r = new TraceRunnable(() -> {
            System.out.println("Inside runnable entity name: " + record.getEntity().getName()); // 这一句会在underlying.run()中执行
        });
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(r);
        //service.submit(r);

        service.shutdown();
    }

    private static class TraceRunnable implements Runnable {
        private final Runnable underlying;
        //private final Record recorder;

        public TraceRunnable(Runnable underlying) {
            this.underlying = underlying;
            //this.recorder = recorder;
        }

        @Override
        public void run() {
        //    System.out.println(recorder.getEntity().getName());
            underlying.run();
        }
    }
}
/* 打印结果：
Inside runnable entity name: e0
 */