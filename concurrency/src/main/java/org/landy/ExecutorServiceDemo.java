package org.landy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 当线程池的线程全部执行完毕后再执行主线程
 */
public class ExecutorServiceDemo {

    public void orderPractice(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 5; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " do something");
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();

        while(true){
            if(executorService.isTerminated()){
                System.out.println("Finally do something ");
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new ExecutorServiceDemo().orderPractice();

    }
}
