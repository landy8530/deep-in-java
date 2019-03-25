package org.landy;

/**
 * Join 方法实现是通过 wait（Object 提供的方法）。
 * 看源代码知会进入 while(isAlive()) 循环；即只要子线程是活的，主线程就不停的等待。
 */
public class JoinDemo {

    static class Worker implements Runnable {

        private String name;

        public Worker(String name){
            this.name = name;
        }

        @Override
        public void run(){
            System.out.println(name + " is working");

        }
    }

    static class Boss implements Runnable{

        private String name;

        public Boss(String name){
            this.name = name;
        }

        @Override
        public void run(){
            System.out.println("boss checks work");
        }
    }

    /**
     * 用 join 方式实现问题如下，
     * 在代码中 main 线程被阻塞直到 thread1，thread2，thread3 执行完，主线程才会顺序的执行thread4.
     * @param args
     */
    public static void main(String[] args){

        Worker worker1 = new Worker("worker1");
        Worker worker2 = new Worker("worker2");
        Worker worker3 = new Worker("worker3");
        Boss boss = new Boss("boss");

        Thread thread1 = new Thread(worker1);
        Thread thread2 = new Thread(worker2);
        Thread thread3 = new Thread(worker3);
        Thread thread4 = new Thread(boss);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread4.start();
    }

}
