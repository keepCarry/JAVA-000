package com.leopo.week4.bizuo1.futuremethod;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskMethod {

    static class Get implements Callable<Integer> {
        FutureTask<Integer> sum;

        public Get(FutureTask<Integer> sum) {
            this.sum = sum;
        }

        @Override
        public Integer call() throws Exception {
            return sum.get();
        }
    }

    static class Sum implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return fibo(36);
        }

        private int fibo(int a) {
            if ( a < 2) {
                return 1;
            }
            return fibo(a-1) + fibo(a-2);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        FutureTask<Integer> sum = new FutureTask<>(new Sum());
        FutureTask<Integer> get = new FutureTask<>(new Get(sum));

        Thread sumT = new Thread(sum);
        sumT.start();
        Thread getT = new Thread(get);
        getT.start();

        int result = get.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }


}
