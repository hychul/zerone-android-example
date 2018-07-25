package com.hychul.zerone.data;

import java.util.Stack;

public class Pool<T> {

    public interface PoolObjectFactory<T> {
        T create();
    }

    public interface PoolObjectInitializer<T> {
        void initialize(T object);
    }

    private final Stack<T> freeObjects;

    private final PoolObjectFactory<T> factory;
    private final PoolObjectInitializer<T> initializer;

    public Pool(PoolObjectFactory<T> factory) {
        this(factory, null);
    }

    public Pool(PoolObjectFactory<T> factory, PoolObjectInitializer<T> initializer) {
        this.freeObjects = new Stack<>();

        this.factory = factory;
        this.initializer = initializer;
    }

    public T get() {
        T object;

        if (freeObjects.isEmpty())
            object = factory.create();
        else
            object = freeObjects.pop();

        if (initializer != null)
            initializer.initialize(object);

        return object;
    }

    public void put(T object) {
        freeObjects.push(object);
    }
}
