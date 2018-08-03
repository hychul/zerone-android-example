package com.hychul.zerone.core;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.nanoTime;

public class ZeroneEngine {

    private ThreadPoolExecutor threadPoolExecutor;
    private SimulatorTask simulatorTask;

    public Object lock;

    public ZeroneEngine() {
        threadPoolExecutor = new ThreadPoolExecutor(1,
                                                    1,
                                                    0,
                                                    TimeUnit.MILLISECONDS,
                                                    new SynchronousQueue<Runnable>(),
                                                    new ThreadPoolExecutor.DiscardPolicy());
        simulatorTask = new SimulatorTask(60);

        lock = new Object();
    }

    public void setSimulationRate(long fps) {
        simulatorTask.setSimulationRate(fps);
    }

    public void start() {
        threadPoolExecutor.execute(simulatorTask);
    }

    public void stop() {
        simulatorTask.stop();
    }

    class SimulatorTask implements Runnable {

        private float NANO_TO_SEC = 1 / 1000000000.0f;

        private Scene scene;

        private boolean shutdown;

        private long duration;

        public SimulatorTask(long simulationRate) {
            setSimulationRate(simulationRate);

            SceneManager.onSceneLoaded.addSubscriber(new Event.Subscriber<Scene>() {
                @Override
                public void onInvoked(Scene param) {
                    scene = param;
                }
            });
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public void setSimulationRate(long fps) {
            duration = TimeUnit.SECONDS.toNanos(1) / fps;
        }

        public void stop() {
            shutdown = true;
        }

        public void onResume() {

        }

        public void onPause() {
            scene.onPause();
            scene.onDestroy();
        }

        @Override
        public void run() {
            onResume();

            long startTime = nanoTime();
            long elapseTime = 0L;

            while (true) {
                Time.setDeltaTime((nanoTime() - startTime) * NANO_TO_SEC);
                startTime = nanoTime();

                synchronized (lock) {
                    // TODO: Use input queue
                    scene.update();

                    lock.notifyAll();
                }

                elapseTime = nanoTime() - startTime;

                if (elapseTime < duration) {
                    try {
                        Thread.sleep(TimeUnit.NANOSECONDS.toMillis(duration - elapseTime));
                    } catch (InterruptedException ignored) {

                    }
                }

                if (shutdown)
                    break;
            }

            shutdown = false;

            onPause();
        }
    }
}
