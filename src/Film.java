import java.util.List;

public class Film {
    private int id;
    private String title;
    private int duration;
    private List<String> screeningTimes;
    private Screen screen;

    public Film(int id, String title, int duration, List<String> screeningTimes, Screen screen) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.screeningTimes = screeningTimes;
        this.screen = screen;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getScreeningTimes() {
        return screeningTimes;
    }

    public Screen getScreen() {
        return screen;
    }

    public void addScreeningTime(String time) {
        screeningTimes.add(time);
    }

    public void removeScreeningTime(String time) {
        screeningTimes.remove(time);
    }
}
