package net.swordie.ms.handlers;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;


public class EventManager {

    private static final Logger log = LogManager.getLogger(EventManager.class);
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(10);
    private static final HashedWheelTimer timer = new HashedWheelTimer(
            Executors.defaultThreadFactory(), 30, TimeUnit.MILLISECONDS, 10000, // 5 minutes
            true, -1, scheduler
    );

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * @param callable The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay The time it should (in ms) take between the start of execution n and execution n+1
     * @return The created event (ScheduledFuture)
     */
    public static EventFuture<?> addFixedRateEvent(Callable<?> callable, long initialDelay, long delay) {
        return EventFuture.newFixedRateEvent(callable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public static EventFuture<?> addFixedRateEvent(Runnable runnable, long initialDelay, long delay) {
        return EventFuture.newFixedRateEvent(Executors.callable(runnable), initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * @param callable The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay The time it should (in ms) take between the start of execution n and execution n+1
     * @param executes The amount of times the
     * @return The created event (ScheduledFuture)
     */
    public static EventFuture<?> addFixedRateEvent(Callable<?> callable, long initialDelay, long delay, int executes) {
        return EventFuture.newFixedRateEvent(callable, initialDelay, delay, TimeUnit.MILLISECONDS, executes);
    }

    public static EventFuture<?> addFixedRateEvent(Runnable runnable, long initialDelay, long delay, int executes) {
        return EventFuture.newFixedRateEvent(Executors.callable(runnable), initialDelay, delay, TimeUnit.MILLISECONDS, executes);
    }

    /**
     * Adds and returns an event that executes after a given initial delay, and then after every delay.
     * @param callable The method that should be run
     * @param initialDelay The time that it should take before the first execution should start
     * @param delay The time it should take between the start of execution n and execution n+1
     * @param timeUnit The time unit of the delays
     * @return The created event (ScheduledFuture)
     */
    public static EventFuture<?> addFixedRateEvent(Callable<?> callable, long initialDelay, long delay, TimeUnit timeUnit) {
        return EventFuture.newFixedRateEvent(callable, initialDelay, delay, timeUnit);
    }

    public static EventFuture<?> addFixedRateEvent(Runnable runnable, long initialDelay, long delay, TimeUnit timeUnit) {
        return EventFuture.newFixedRateEvent(Executors.callable(runnable), initialDelay, delay, timeUnit);
    }

    /**
     * Adds and returns an event that executes after a given delay in milliseconds.
     * @param callable The method that should be run
     * @param delay The delay (in ms) after which the call should start
     * @return The created event (ScheduledFuture)
     */
    public static EventFuture<?> addEvent(Callable<?> callable, long delay) {
        return EventFuture.newEvent(callable, delay, TimeUnit.MILLISECONDS);
    }

    public static EventFuture addEvent(Runnable runnable, long delay) {
        return EventFuture.newEvent(Executors.callable(runnable), delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds and returns an event that executes after a given delay.
     * @param callable The method that should be run
     * @param delay The delay after which the call should start
     * @param timeUnit The time unit of the delay
     * @return The created event (ScheduledFuture)
     */
    public static EventFuture<?> addEvent(Callable<?> callable, long delay, TimeUnit timeUnit) {
        return EventFuture.newEvent(callable, delay, timeUnit);
    }

    public static EventFuture<?> addEvent(Runnable runnable, long delay, TimeUnit timeUnit) {
        return EventFuture.newEvent(Executors.callable(runnable), delay, timeUnit);
    }

    public static void shutdown() {
        timer.stop();
        scheduler.shutdown();
    }


    public static class EventFuture<V> implements ScheduledFuture<V> {
        private final Callable<V> callable;
        private final long delay;
        private final TimeUnit unit;
        private TimerTask timerTask;
        private Timeout timeout;
        private int executes = -1;
        private V returnValue;

        private EventFuture(Callable<V> callable, long delay, TimeUnit unit) {
            this.callable = callable;
            this.delay = delay;
            this.unit = unit;
        }

        private TimerTask getTimerTask() {
            return timerTask;
        }

        private void setTimerTask(TimerTask timerTask) {
            this.timerTask = timerTask;
        }

        private void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }

        private int getExecutes() {
            return executes;
        }

        private void setExecutes(int executes) {
            this.executes = executes;
        }

        private V getReturnValue() {
            return returnValue;
        }

        private void setReturnValue(V returnValue) {
            this.returnValue = returnValue;
        }

        private V call() {
            try {
                setReturnValue(callable.call());
            } catch (Exception e) {
                log.error(String.format("error in executing: %s. It will no longer be run!", callable));
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return getReturnValue();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return this.timeout.cancel();
        }

        @Override
        public boolean isCancelled() {
            return this.timeout.isCancelled();
        }

        @Override
        public boolean isDone() {
            return this.timeout.isExpired();
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            this.timeout.wait();
            return call();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            this.timeout.wait(TimeUnit.MILLISECONDS.convert(timeout, unit));
            return call();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(delay, unit);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (getDelay(this.unit) - o.getDelay(this.unit));
        }

        public static EventFuture<?> newEvent(Callable<?> callable, long delay, TimeUnit unit) {
            EventFuture<?> eventFuture = new EventFuture<>(callable, delay, unit);
            eventFuture.setTimerTask((timeout) -> eventFuture.call());
            eventFuture.setTimeout(timer.newTimeout(eventFuture.getTimerTask(), delay, unit));
            return eventFuture;
        }

        public static EventFuture<?> newFixedRateEvent(Callable<?> callable, long initialDelay, long delay, TimeUnit unit) {
            EventFuture<?> eventFuture = new EventFuture<>(callable, delay, unit);
            eventFuture.setTimerTask((timeout) -> {
                eventFuture.setTimeout(timer.newTimeout(eventFuture.getTimerTask(), delay, unit));
                eventFuture.call();
            });
            eventFuture.setTimeout(timer.newTimeout(eventFuture.getTimerTask(), initialDelay, unit));
            return eventFuture;
        }

        public static EventFuture<?> newFixedRateEvent(Callable<?> callable, long initialDelay, long delay, TimeUnit unit, int executes) {
            EventFuture<?> eventFuture = new EventFuture<>(callable, delay, unit);
            eventFuture.setExecutes(executes);
            eventFuture.setTimerTask((timeout) -> {
                int newExecutes = eventFuture.getExecutes() - 1;
                if (newExecutes > 0) {
                    eventFuture.setExecutes(newExecutes);
                    eventFuture.setTimeout(timer.newTimeout(eventFuture.getTimerTask(), delay, unit));
                }
                eventFuture.call();
            });
            eventFuture.setTimeout(timer.newTimeout(eventFuture.getTimerTask(), initialDelay, unit));
            return eventFuture;
        }
    }
}
