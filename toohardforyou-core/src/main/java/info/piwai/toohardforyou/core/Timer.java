package info.piwai.toohardforyou.core;

/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import static forplay.core.ForPlay.currentTime;

import java.util.ArrayList;

import forplay.core.Game;

/**
 * A simplified, browser-safe timer class. This class serves the same purpose as
 * java.util.Timer, but is simplified because of the single-threaded
 * environment.
 * 
 * To schedule a timer, simply create a subclass of it (overriding {@link #run})
 * and call {@link #schedule} or {@link #scheduleRepeating}.
 * 
 * You MUST call Timer.update() from the {@link Game#update(float)} method.
 */
public abstract class Timer {

    private static final ArrayList<Timer> timers = new ArrayList<Timer>();

    public static void update() {
        double currentTime = currentTime();

        for (Timer timer : timers) {
            if (currentTime > timer.nextExecution) {
                if (timer.periodMillis > 0) {
                    timer.nextExecution = currentTime() + timer.periodMillis;
                } else {
                    timer.cancel();
                }
                timer.run();
            }
        }
    }

    private double nextExecution;

    private int periodMillis;

    /**
     * Cancels this timer.
     */
    public void cancel() {
        timers.remove(this);
    }

    /**
     * This method will be called when a timer fires. Override it to implement
     * the timer's logic.
     */
    public abstract void run();

    /**
     * Schedules a timer to elapse in the future.
     * 
     * @param delayMillis
     *            how long to wait before the timer elapses, in milliseconds
     */
    public void schedule(int delayMillis) {
        if (delayMillis <= 0) {
            throw new IllegalArgumentException("must be positive");
        }
        cancel();
        periodMillis = 0;
        nextExecution = currentTime() + delayMillis;
        timers.add(this);
    }

    /**
     * Schedules a timer that elapses repeatedly.
     * 
     * @param periodMillis
     *            how long to wait before the timer elapses, in milliseconds,
     *            between each repetition
     */
    public void scheduleRepeating(int periodMillis) {
        if (periodMillis <= 0) {
            throw new IllegalArgumentException("must be positive");
        }
        cancel();
        this.periodMillis = periodMillis;
        nextExecution = currentTime() + periodMillis;
        timers.add(this);
    }

}