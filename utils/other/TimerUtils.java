package ru.neverhook.utils.other;

public class TimerUtils {

    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(double milliseconds) {
        return this.getCurrentMS() - ms > milliseconds;
    }

    public void reset() {
        ms = this.getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - ms;
    }
}
