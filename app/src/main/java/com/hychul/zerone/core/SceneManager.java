package com.hychul.zerone.core;

public class SceneManager {

    private static Scene scene;

    public static final Event<Scene> onSceneLoaded;
    public static final Event<Scene> onSceneUnloaded;

    static {
        onSceneLoaded = new Event<>();
        onSceneUnloaded = new Event<>();
    }

    public static void loadScene(Scene newScene) {
        if (newScene == null)
            throw new IllegalArgumentException("Scene must not be null");

        if (scene != null) {
            scene.onPause();
            scene.onDestroy();

            onSceneUnloaded.invoke(scene);
        }

        newScene.onCreate();
        newScene.onResume();

        scene = newScene;

        onSceneLoaded.invoke(scene);
    }

    public static Scene getLoadedScene() {
        return scene;
    }

    static void resume() {
        scene.onResume();
    }

    static void update(boolean draw) {
        scene.update(0);
        if (draw)
            scene.draw();
    }

    static void pause() {
        scene.onPause();
    }
}
