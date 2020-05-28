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

        // 下面写法等效于上面的lambda表达式（除了打印的内容），run方法里的内容这里只是"注册"一下，并不立即执行，execute的时候会t.run()回到这里打印
        // 内部类可以看到外部类的变量，所以这时record在下面的run()里面是可见的，execute的执行路径回到这里的时候也能拿到record
        class UnderlyingRunnable implements Runnable{
            @Override
            public void run() {
                System.out.println("Inside runnable entity name from UnderlyingRunnable: " + record.getEntity().getName());
            }
        }
        System.out.println("-----我最先打印-----"); // 先于上面的"注册"（run）方法打印
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(r);
        //service.submit(r);
        Runnable t = new UnderlyingRunnable();
        service.execute(t);

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
            underlying.run(); // underlying是个内部类的对象，能看到其外部类的record变量
        }
    }
}
/* 打印结果：
-----我最先打印-----
Inside runnable entity name: e0
Inside runnable entity name from UnderlyingRunnable: e0
 */