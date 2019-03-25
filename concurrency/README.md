# Java并发框架基础

## 1. Java 并发限制(线程执行顺序)

比如某个线程在其他线程并发执行完毕后最后执行，这里分别用 CountDownLatch、CyclicBarrier 、join()、线程池来实现。

### 1.1 CyclicBarrier

#### 1.1.1 概念

CyclicBarrier 的字面意思是可循环（Cyclic）使用的屏障（Barrier），又叫同步屏障。它可以让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。线程到达屏障的控制通过CyclicBarrier 的 await() 方法实现。

CyclicBarrier 的构造方法有 CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用 await 方法告诉 CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。

CyclicBarrier 还提供一个构造函数 CyclicBarrier(int parties, Runnable barrierAction) ，用于在线程都到达屏障时，优先执行barrierAction 这个 Runnable 对象，然后都到达屏障的线程继续执行。

#### 1.1.2 实现原理

在 CyclicBarrier 的内部定义了一个 ReentrantLock 对象，每当一个线程调用 CyclicBarrier 的 await 方法时，将剩余拦截的线程数减1，然后判断剩余拦截数是否为0，如果不是，进入 Lock 对象的条件队列等待。如果是则执行 barrierAction 对象的 run 方法，然后将锁的条件队列中的所有线程放入锁等待队列中，这些线程会依次的获取锁、释放锁，接着先从 await 方法返回，再从 CyclicBarrier 的 await 方法中返回。

#### 1.1.3 CyclicBarrier 和 CountDownLatch 比较

- CountDownLatch 的作用是允许1或N个线程等待其他线程完成执行；而 CyclicBarrier 则是允许N个线程相互等待。

- CountDownLatch 的计数器无法被重置；CyclicBarrier 的计数器可以被重置后使用，因此它被称为是循环的 barrier。

### 1.2 join

#### 1.2.1 原理

join() 是 Thread 类的一个方法，join() 方法的作用是等待当前线程结束，也即让“主线程”等待“子线程”结束之后才能继续运行。t.join() 方法阻塞调用此方法的线程 (calling thread)，直到线程 t完成，此线程再继续（看起来和同步调用类似）；通常用于在 main 主线程内，等待其它线程完成后再继续执行 main 主线程。

#### 1.2.2 join 实现

Join 方法实现是通过 wait（Object 提供的方法）。 看源代码知会进入 while(isAlive()) 循环；即只要子线程是活的，主线程就不停的等待。

### 1.3 CountDownLatch

#### 1.3.1 原理

Java 的 util.concurrent 包里面的 CountDownLatch 其实可以把它看作一个计数器（倒计时锁），只不过这个计数器的操作是原子操作，同时只能有一个线程去操作这个计数器，也就是同时只能有一个线程去减这个计数器里面的值。

你可以向 CountDownLatch 对象设置一个初始的数字作为计数值，任何调用这个对象上的 await() 方法都会阻塞，直到这个计数器的计数值被其他的线程减为 0 为止。

#### 1.3.2 使用场景

CountDownLatch 的一个非常典型的应用场景是：有一个任务想要往下执行，但必须要等到其他的任务执行完毕后才可以继续往下执行。假如我们这个想要继续往下执行的任务调用一个 CountDownLatch 对象的 await() 方法，其他的任务执行完自己的任务后调用同一个CountDownLatch 对象上的 countDown() 方法，这个调用 await() 方法的任务将一直阻塞等待，直到这个CountDownLatch对象的计数值减到 0 为止。

#### 1.3.3 CountDownLatch 与 join 比较

调用thread.join() 方法必须等thread 执行完毕，当前线程才能继续往下执行，而CountDownLatch通过计数器提供了更灵活的控制，只要检测到计数器为0当前线程就可以往下执行而不用管相应的thread是否执行完毕。

