package com.hychul.zerone.core;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Event<T> {

    private final ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();

    public void addSubscriber(Subscriber<T> subscriber) {
        subscriberList.add(subscriber);
    }

    public void removeSubscriber(Subscriber<T> subscriber) {
        subscriberList.remove(subscriber);
    }

    public void invoke(T param) {
        int size = subscriberList.size();
        for (int i = 0; i < size; i++) {
            subscriberList.get(i).onInvoked(param);
        }
    }

    private void invoke(Object target, Method method, T param) throws Throwable {
        method.invoke(target, param);
    }

    public interface Subscriber<T> {
        void onInvoked(T param);
    }
}
