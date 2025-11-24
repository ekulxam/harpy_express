package dev.doctor4t.trainmurdermystery.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author BackupCup
 */
public final class Scheduler {

    private static final CopyOnWriteArrayList<ScheduledTask> TASKS = new CopyOnWriteArrayList<>();

    private Scheduler() {
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> TASKS.removeIf(ScheduledTask::tick));
    }

    public static ScheduledTask schedule(Runnable action, int delayTicks) {
        ScheduledTask task = new ScheduledTask(delayTicks, action);
        TASKS.add(task);
        return task;
    }

    public static class ScheduledTask {
        private int ticksLeft;
        private final Runnable action;
        private boolean cancelled = false;

        public ScheduledTask(int delayTicks, Runnable action) {
            this.ticksLeft = delayTicks;
            this.action = action;
        }

        /**
         * Ticks the current task
         * @return whether the task should be removed
         */
        public boolean tick() {
            if (this.cancelled) {
                return true;
            }

            if (--ticksLeft > 0) {
                return false;
            }

            action.run();
            return true;
        }

        public void cancel() {
            this.cancelled = true;
        }
    }
}
