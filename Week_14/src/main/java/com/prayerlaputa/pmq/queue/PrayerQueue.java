package com.prayerlaputa.pmq.queue;

import java.util.concurrent.TimeUnit;


public interface PrayerQueue<E> {


    E poll(long timeout, TimeUnit unit) throws InterruptedException;

    E poll();

    boolean offer(E e);
}
