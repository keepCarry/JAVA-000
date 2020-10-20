package com.leopo.week1.question1;

/**
 * 为了查看字节码写的demo
 */
public class ByteCodeTestDemo {

    public static void main(String[] args) {

        int sum = 0;

        for (int index = 0; index < 10; index++) {
            if (sum < 20) {
                sum = sum * index;
            } else {
                sum = sum + index;
            }
        }
        sum--;
        sum *= sum;
        sum /= 2;
    }
}
