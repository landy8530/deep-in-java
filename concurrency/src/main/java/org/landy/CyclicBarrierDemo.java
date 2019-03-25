package org.landy;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 的字面意思是可循环（Cyclic）使用的屏障（Barrier），又叫同步屏障。
 * 它可以让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，
 * 所有被屏障拦截的线程才会继续干活。线程到达屏障的控制通过CyclicBarrier 的 await() 方法实现。
 * CyclicBarrier 的构造方法有 CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用 await 方法告诉 CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。
 * CyclicBarrier 还提供一个构造函数 CyclicBarrier(int parties, Runnable barrierAction) ，用于在线程都到达屏障时，优先执行barrierAction 这个 Runnable 对象，然后都到达屏障的线程继续执行。
 */
public class CyclicBarrierDemo {

    static class Worker implements Runnable{

        private String name;
        private CyclicBarrier cyclicBarrier;

        public Worker(String name, CyclicBarrier cyclicBarrier){
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        public void run(){
            System.out.println(name + " is working");
            try {
                Thread.sleep(2000);

                //到达屏障出（同步点）
                cyclicBarrier.await();

                //线程都到了后继续向下执行,也可以不要下面代码,什么都不做了
                System.out.println(name + " do other things");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    static class Boss implements Runnable{

        private String name;

        public Boss(String name){
            this.name = name;
        }

        public void run(){
            System.out.println(name + " checks work");

        }
    }

    public static void main(String[] args){

        //其他线程都达到屏障后,再执行 boss 线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Boss("boss"));

        for(int i=0; i<3; i++){
            new Thread(new Worker("worker"+i, cyclicBarrier)).start();
        }
    }
}
