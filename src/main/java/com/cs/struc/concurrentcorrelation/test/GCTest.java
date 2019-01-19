package com.cs.struc.concurrentcorrelation.test;

import java.util.ArrayList;

/**
 * @author benjaminChan
 * @date 2018/12/28 0028 上午 11:16
 *
 * 指定堆内存大小，堆内存不够则抛出 java.lang.OutOfMemoryError:Java heap space
 *
 * 递归调用方法，代码会抛出栈溢出 java.lang.StackOverflowError
 */
public class GCTest {

    private static int count = 0;

    public static void main(String[] args) {
        testHeap();
//        recursion();
    }

    private static void recursion() {
        count ++;
        recursion();
    }

    private static void testHeap() {
        ArrayList list = new ArrayList();

        while (true)

        {

            list.add(new GCTest());

        }
    }


    /**
     *
     */
}
