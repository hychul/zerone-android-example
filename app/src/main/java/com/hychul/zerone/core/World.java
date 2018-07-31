package com.hychul.zerone.core;

import com.hychul.zerone.annotation.RequireComponent;
import com.hychul.zerone.data.Pool;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private Pool<Integer> entityIdPool;
    private final HashMap<Integer, Entity> entityMap;

    private final ArrayList<BehaviorComponent> behaviorList;

    private final HashMap<Class, ArrayList<Component>> componentsMap;
    private final ArrayList<System> systemList;

    public World() {
        entityIdPool = new Pool<>(new Pool.PoolObjectFactory<Integer>() {
            private final static int MIN_ID = Integer.MIN_VALUE;
            private final static int MAX_ID = Integer.MAX_VALUE;

            private int mIdCounter = MIN_ID;

            @Override
            public Integer create() {
                return mIdCounter++;
            }
        });
        entityMap = new HashMap<>();

        behaviorList = new ArrayList<>();

        componentsMap = new HashMap<>();
        systemList = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entity.id = entityIdPool.get();
        entityMap.put(entity.id, entity);

        ArrayList<Component> components = entity.getComponents();
        for (int i = 0; i < components.size(); i++) {
            addComponent(components.get(i));
        }
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.id);
        entityIdPool.put(entity.id);

        ArrayList<Component> components = entity.getComponents();
        for (int i = 0; i < components.size(); i++) {
            removeComponent(components.get(i));
        }
    }

    private <T extends Component> void addComponent(T component) {
        Class key = component.getClass();

        if (!componentsMap.containsKey(key))
            componentsMap.put(key, new ArrayList<Component>());

        componentsMap.get(key).add(component);
    }

    private <T extends Component> void removeComponent(T component) {
        Class key = component.getClass();

        if (!componentsMap.containsKey(key))
            return;

        componentsMap.get(key).remove(component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<Component> components = componentsMap.get(type);

        if (components == null)
            return null;

        return (ArrayList<T>) components;
    }

    public void addSystem(System system) {
        setComponentFilter(system);
        systemList.add(system);
    }

    private void setComponentFilter(System system) {
        RequireComponent annotation = system.getClass().getAnnotation(RequireComponent.class);
        if (annotation == null)
            return;

        Class<? extends Component>[] requireComponents = annotation.value();
        int length = requireComponents.length;
        if (length < 1)
            return;

    }

    public void removeSystem(System system) {
        systemList.remove(system);
    }

    public final void update() {
        updateBehaivor();
        updateSystem();
    }

    private final void updateBehaivor() {
        int count = behaviorList.size();
        BehaviorComponent behavior;
        for (int i = 0; i < count; i++) {
            behavior = behaviorList.get(i);
            behavior.update();
        }
    }

    private final void updateSystem() {
        // TODO: If system use in different thread, hold components reference pointer.
        int count = systemList.size();
        System system;
        for (int i = 0; i < count; i++) {
            system = systemList.get(i);
            system.update();
        }
    }
}
