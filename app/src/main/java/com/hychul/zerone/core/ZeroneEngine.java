package com.hychul.zerone.core;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.nanoTime;

public class ZeroneEngine {

    private Scene mScene;

    private ThreadPoolExecutor mSimulationExecutor;
    private SimulatorTask mSimulator;

    public ZeroneEngine() {
        mSimulationExecutor = new ThreadPoolExecutor(1,
                                                     1,
                                                     0,
                                                     TimeUnit.MILLISECONDS,
                                                     new SynchronousQueue<Runnable>(),
                                                     new ThreadPoolExecutor.DiscardPolicy());

        mSimulator = new SimulatorTask(60);
    }

    public void loadScene(Scene scene) {
        if (mScene != null) {
            mScene.onPause();
            mScene.onDestroy();
        }

        scene.onCreate();
        scene.onResume();

        mScene = scene;
    }

    public Scene getScene() {
        return mScene;
    }

    public void setSimulationRate(long fps) {
        mSimulator.setSimulationRate(fps);
    }

    public void start() {
        mSimulationExecutor.execute(mSimulator);
    }

    public void update(float deltaTime) {
        mScene.update(deltaTime);
    }

    public void pause() {
        mScene.onPause();
    }

    public void stop() {
        mSimulator.stop();

        mScene.onPause();
        mScene.onDestroy();
    }

    class SimulatorTask implements Runnable {

        private boolean mShutdown;

        private long mSimulationDuration;

        SimulatorTask(long simulationRate) {
            setSimulationRate(simulationRate);
        }

        void setSimulationRate(long fps) {
            mSimulationDuration = TimeUnit.SECONDS.toNanos(1) / fps;
        }

        void stop() {
            mShutdown = true;
        }

        @Override
        public void run() {
            long startTime;
            long elapseTime;

            while (true) {
                startTime = nanoTime();

                // TODO: Use input queue

                update(0);

                elapseTime = nanoTime() - startTime;

                if (elapseTime < mSimulationDuration) {
                    try {
                        Thread.sleep(TimeUnit.NANOSECONDS.toMillis(mSimulationDuration - elapseTime));
                    } catch (InterruptedException ignored) {

                    }
                }

                if (mShutdown)
                    break;
            }

            mShutdown = false;
        }
    }
}
