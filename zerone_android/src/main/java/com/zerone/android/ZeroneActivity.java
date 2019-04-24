package com.zerone.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zerone.android.audio.Audio;
import com.zerone.android.input.Input;
import com.zerone.android.io.FileIO;
import com.zerone.core.Event;
import com.zerone.core.Scene;
import com.zerone.core.SceneManager;
import com.zerone.core.ZeroneEngine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class ZeroneActivity extends Activity {

    enum ActivityState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    ZeroneEngine engine;

    GLSurfaceView glView;

    Graphics graphics;

    Audio audio;
    Input input;
    FileIO fileIO;

    ActivityState state = ActivityState.Initialized;

    final Object stateLock = new Object();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        engine = new ZeroneEngine();

        glView = new GLSurfaceView(this);
        glView.setRenderer(new RenderTask());
        setContentView(glView);

        fileIO = new FileIO(this);
        audio = new Audio(this);
        input = new Input(this, glView);
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onPause() {
        synchronized (stateLock) {
            if (isFinishing())
                state = ActivityState.Finished;
            else
                state = ActivityState.Paused;

            while (true) {
                try {
                    stateLock.wait();
                    break;
                } catch (InterruptedException ignored) {

                }
            }
        }
        engine.stop();
        glView.onPause();
        super.onPause();
    }

    public abstract Scene getStartScene();

    public Graphics getGLGraphics() {
        return graphics;
    }

    public Input getInput() {
        return input;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Audio getAudio() {
        return audio;
    }

    public void onInitialized() {
        // TODO: Stub
    }

    class RenderTask implements Renderer {

        Scene scene;

        public RenderTask() {
            SceneManager.onSceneLoaded.addSubscriber(new Event.Subscriber<Scene>() {
                @Override
                public void onInvoked(Scene param) {
                    scene = param;
                }
            });
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            graphics = new Graphics(glView, gl10);

            synchronized (stateLock) {
                if (state == ActivityState.Initialized)
                    SceneManager.loadScene(getStartScene());
                else
                    scene.onResume();

                state = ActivityState.Running;
                engine.start();
            }

            onInitialized();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            graphics.width = width;
            graphics.height = height;
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            ActivityState stateSync;

            synchronized (stateLock) {
                stateSync = state;
            }

            switch (stateSync) {
                case Running:
                    synchronized (engine.lock) {
                        scene.draw();

                        try {
                            engine.lock.wait();
                        } catch (InterruptedException ignored) {

                        }
                    }
                    break;
                case Paused:
                    scene.onPause();

                    synchronized (stateLock) {
                        state = ActivityState.Idle;
                        stateLock.notifyAll();
                    }
                    break;
                case Finished:
                    scene.onPause();
                    scene.onDestroy();

                    synchronized (stateLock) {
                        state = ActivityState.Idle;
                        stateLock.notifyAll();
                    }
                    break;
            }
        }
    }
}
