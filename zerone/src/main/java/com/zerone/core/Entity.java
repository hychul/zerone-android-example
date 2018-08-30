package com.zerone.core;

import java.util.ArrayList;

public class Entity {

    int id;

    public boolean isActive;

    private ArrayList<Component> componentList;

    public Entity() {
        isActive = true;

        componentList = new ArrayList<>();
    }

    public ArrayList<Component> getComponents() {
        return componentList;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<T> ret = new ArrayList<>();
        int count = componentList.size();
        Component comp;
        for (int i = 0; i < count; i++) {
            comp = componentList.get(i);
            if (comp.getClass() == type) {
                ret.add((T) comp);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        int count = componentList.size();
        Component comp;
        for (int i = 0; i < count; i++) {
            comp = componentList.get(i);
            if (comp.getClass() == type) {
                return (T) comp;
            }
        }
        return null;
    }

    // TODO: Block this on running
    public void addComponent(Component component) {
        component.entity = this;
        componentList.add(component);
    }
}
