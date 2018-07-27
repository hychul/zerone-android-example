package com.hychul.zerone.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hychul.zerone.Audio;
import com.hychul.zerone.FileIO;
import com.hychul.zerone.Input;
import com.hychul.zerone.SimulationUnit;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.audio.AndroidAudio;
import com.hychul.zerone.android.input.AndroidInput;
import com.hychul.zerone.core.Scene;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class ZeroneActivity extends Activity implements Zerone {

    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;

    Graphics graphics;

    Audio audio;
    Input input;
    FileIO fileIO;

    SimulationUnit simulationUnit;

    Scene scene;

    GLGameState state = GLGameState.Initialized;

    final Object stateLock = new Object();
    long startTime = System.nanoTime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glView = new GLSurfaceView(this);
        glView.setRenderer(new RenderTask());
        setContentView(glView);

        graphics = new Graphics(glView);

        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
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
                state = GLGameState.Finished;
            else
                state = GLGameState.Paused;

            while (true) {
                try {
                    stateLock.wait();
                    break;
                } catch (InterruptedException ignored) {

                }
            }
        }
        glView.onPause();
        super.onPause();
    }

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

    public void setScene(Scene newScene) {
        if (newScene == null)
            throw new IllegalArgumentException("Scene must not be null");

        this.scene.onPause();
        this.scene.onDestroy();

        newScene.onResume();
        newScene.update(0);

        this.scene = newScene;
    }

    public Scene getCurrentScene() {
        return scene;
    }

    public void onInitialized() {
        // TODO: Stub
    }

    class RenderTask implements Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            graphics.setGL(gl10);

            synchronized (stateLock) {
                if (state == GLGameState.Initialized)
                    scene = getStartScene();

                scene.onResume();
                state = GLGameState.Running;

                startTime = System.nanoTime();
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
            GLGameState glState;

            synchronized (stateLock) {
                glState = state;
            }

            switch (glState)
            {
                case Running:
                    float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
                    startTime = System.nanoTime();

                    scene.update(deltaTime);
                    // TODO: Fix this
                    getCurrentScene().draw();
                    break;
                case Paused:
                    scene.onPause();

                    synchronized (stateLock) {
                        state = GLGameState.Idle;
                        stateLock.notifyAll();
                    }
                    break;
                case Finished:
                    scene.onPause();
                    scene.onDestroy();

                    synchronized (stateLock) {
                        state = GLGameState.Idle;
                        stateLock.notifyAll();
                    }
                    break;
            }
        }
    }
}
