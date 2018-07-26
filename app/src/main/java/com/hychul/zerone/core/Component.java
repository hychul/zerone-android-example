package com.hychul.zerone.core;

import java.util.ArrayList;

public abstract class Component {

    Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public ArrayList<Component> getComponents() {
        return entity.getComponents();
    }

    public <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        return entity.getComponents(type);
    }

    public <T extends Component> T getComponent(Class<T> type) {
        return entity.getComponent(type);
    }
}
