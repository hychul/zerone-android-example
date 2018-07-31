package com.hychul.zerone.core;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.nanoTime;

public class ZeroneEngine {

    private ThreadPoolExecutor mSimulationExecutor;
    private SimulatorTask mSimulator;

    public Object lock;

    public ZeroneEngine() {
        mSimulationExecutor = new ThreadPoolExecutor(1,
                                                     1,
                                                     0,
                                                     TimeUnit.MILLISECONDS,
                                                     new SynchronousQueue<Runnable>(),
                                                     new ThreadPoolExecutor.DiscardPolicy());
        mSimulator = new SimulatorTask(60);

        lock = new Object();
    }

    public void setSimulationRate(long fps) {
        mSimulator.setSimulationRate(fps);
    }

    public void start() {
        mSimulationExecutor.execute(mSimulator);
    }

    public void stop() {
        mSimulator.stop();
    }

    class SimulatorTask implements Runnable {

        private Scene mScene;

        private boolean mShutdown;

        private long mSimulationDuration;

        SimulatorTask(long simulationRate) {
            setSimulationRate(simulationRate);

            SceneManager.onSceneLoaded.addSubscriber(new Event.Subscriber<Scene>() {
                @Override
                public void onInvoked(Scene param) {
                    mScene = param;
                }
            });
        }

        void setSimulationRate(long fps) {
            mSimulationDuration = TimeUnit.SECONDS.toNanos(1) / fps;
        }

        void stop() {
            mShutdown = true;

            mScene.onPause();
            mScene.onDestroy();
        }

        @Override
        public void run() {

            long startTime = 0L;
            long elapseTime = 0L;

            while (true) {
                // TODO: Move time to static class
                float deltaTime = (nanoTime() - startTime) / 1000000000.0f;
                startTime = nanoTime();

                // TODO: Use input queue

                synchronized (lock) {
                    mScene.update(deltaTime);
                    lock.notifyAll();
                }

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
