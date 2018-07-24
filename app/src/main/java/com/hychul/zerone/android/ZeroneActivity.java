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
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.audio.AndroidAudio;
import com.hychul.zerone.android.input.AndroidInput;
import com.hychul.zerone.core.Scene;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class ZeroneActivity extends Activity implements Zerone, Renderer {

    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }
    
    GLSurfaceView glView;
    GLGraphics glGraphics;

    Audio audio;
    Input input;
    FileIO fileIO;

    Scene scene;

    GLGameState state = GLGameState.Initialized;

    Object stateLock = new Object();
    long startTime = System.nanoTime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glView = new GLSurfaceView(this);
        glView.setRenderer(this);
        setContentView(glView);
        
        glGraphics = new GLGraphics(glView);
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {        
        glGraphics.setGL(gl);
        
        synchronized(stateLock) {
            if (state == GLGameState.Initialized)
                scene = getStartScene();
            state = GLGameState.Running;
            scene.onResume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLGameState state = null;
        
        synchronized(stateLock) {
            state = this.state;
        }
        
        if (state == GLGameState.Running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            
            scene.update(deltaTime);
            scene.render();
        }
        
        if (state == GLGameState.Paused) {
            scene.onPause();
            synchronized(stateLock) {
                this.state = GLGameState.Idle;
                stateLock.notifyAll();
            }
        }
        
        if (state == GLGameState.Finished) {
            scene.onPause();
            scene.onDestroy();
            synchronized(stateLock) {
                this.state = GLGameState.Idle;
                stateLock.notifyAll();
            }
        }
    }

    @Override
    public void onPause() {
        synchronized(stateLock) {
            if (isFinishing())
                state = GLGameState.Finished;
            else
                state = GLGameState.Paused;

            while(true) {
                try {
                    stateLock.wait();
                    break;
                } catch(InterruptedException e) {

                }
            }
        }
        glView.onPause();
        super.onPause();
    }

    public GLGraphics getGLGraphics() {
        return glGraphics;
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
}
