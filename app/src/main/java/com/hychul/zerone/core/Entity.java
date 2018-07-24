package com.hychul.zerone.core;

import java.util.ArrayList;

public class Entity {

    int id;

    public boolean isActive;

    private ArrayList<Component> mComponentList;

    public Entity() {
        isActive = true;

        mComponentList = new ArrayList<>();
    }

    public ArrayList<Component> getComponents() {
        return mComponentList;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<T> ret = new ArrayList<>();
        int count = mComponentList.size();
        Component comp;
        for (int i = 0; i < count; i++) {
            comp = mComponentList.get(i);
            if (comp.getClass() == type) {
                ret.add((T) comp);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        int count = mComponentList.size();
        Component comp;
        for (int i = 0; i < count; i++) {
            comp = mComponentList.get(i);
            if (comp.getClass() == type) {
                return (T) comp;
            }
        }
        return null;
    }

    // TODO: Block this on running
    public void addComponent(Component component) {
        component.entity = this;
        mComponentList.add(component);
    }
}
