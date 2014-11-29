
public class TimerModel {
    private long startTime;
    private long time;

    public TimerModel() {
        time = 0;
    }

    public void setStartTime(long start) {
        this.startTime = start;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getTimeString() {
        int milliseconds = (int)(time % 1000);
        milliseconds = milliseconds - (milliseconds % 10);

        int seconds = (int)(time / 1000) % 60;
        int minutes = (int)(time / 1000) / 60;

        return ""+numToString(minutes)+":"
                 +numToString(seconds)+":"
                 +numToString(milliseconds);
    }

    private String numToString(int num) {
        String str = (num < 10 ? "0" : "") + num;
        return str.substring(0, 2);
    }

    public void updateTime(long time) {
        this.time = time - startTime;
    }
}
