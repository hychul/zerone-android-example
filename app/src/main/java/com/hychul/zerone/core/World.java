package com.hychul.zerone.core;

import com.hychul.zerone.annotation.RequireComponent;
import com.hychul.zerone.data.Pool;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private final ArrayList<System> mSystemList;
    private final HashMap<Integer, Entity> mEntityMap;
    private final HashMap<Class, ArrayList<Component>> mComponentsMap;
    // TODO: Add callbacks for system that use specific component

    private Pool<Integer> mEntityIdPool;

    public World() {
        mSystemList = new ArrayList<>();
        mEntityMap = new HashMap<>();
        mComponentsMap = new HashMap<>();

        mEntityIdPool = new Pool<>(new Pool.PoolObjectFactory<Integer>() {
            private final static int MIN_ID = Integer.MIN_VALUE;
            private final static int MAX_ID = Integer.MAX_VALUE;

            private int mIdCounter = MIN_ID;

            @Override
            public Integer create() {
                return mIdCounter++;
            }
        });
    }

    public void add(Entity entity) {
        entity.id = mEntityIdPool.get();
        mEntityMap.put(entity.id, entity);

        ArrayList<Component> components = entity.getComponents();
        for (int i = 0; i < components.size(); i++) {
            addComponent(components.get(i));
            // TODO: Check tuple filter of component and notify it to system
        }
    }

    public void remove(Entity entity) {
        mEntityMap.remove(entity.id);
        mEntityIdPool.put(entity.id);

        ArrayList<Component> components = entity.getComponents();
        for (int i = 0; i < components.size(); i++) {
            removeComponent(components.get(i));
            // TODO: Check tuple filter of component and notify it to system
        }
    }

    private <T extends Component> void addComponent(T component) {
        Class key = component.getClass();

        if (!mComponentsMap.containsKey(key))
            mComponentsMap.put(key, new ArrayList<Component>());

        mComponentsMap.get(key).add(component);
    }

    private <T extends Component> void removeComponent(T component) {
        Class key = component.getClass();

        if (!mComponentsMap.containsKey(key))
            return;

        mComponentsMap.get(key).remove(component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<Component> components = mComponentsMap.get(type);

        if (components == null)
            return null;

        return (ArrayList<T>) components;
    }

    public void add(System system) {
        setComponentFilter(system);
        mSystemList.add(system);
    }

    private void setComponentFilter(System system) {
        RequireComponent annotation = system.getClass().getAnnotation(RequireComponent.class);
        if (annotation == null)
            return;

        Class<? extends Component>[] requireComponents = annotation.value();
        int length = requireComponents.length;
        if (length < 1)
            return;

        // TODO: Set filter callbacks by using system's annotation : RequireComponent
    }

    public void remove(System system) {
        mSystemList.remove(system);
    }

    public final void update() {
        // TODO: If system use in different thread, hold components reference pointer.
        int count = mSystemList.size();
        System system;
        for (int i = 0; i < count; i++) {
            system = mSystemList.get(i);
            system.update();
        }
    }
}
