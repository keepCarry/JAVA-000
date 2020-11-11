package com.leopo.week4.bizuo1.simplemethod;

/**
 * 参考了别人的部分代码，自行修改了部分。
 */
public class NoLockMethod {

    private volatile static Integer value = null;

    public static void main(String[] args) {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final NoLockMethod noLockMethod = new NoLockMethod();
        Thread thread = new Thread(() -> {
            noLockMethod.sum(1);
        });
        thread.start();

        int result = noLockMethod.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //然后退出main线程
    }


    private static int sum(int i) {
        //改动了一部分代码
        int result = fibo(36);
        value = result;
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    private  int getValue(){
        while (value == null) {
        }
        return value;
    }
}
