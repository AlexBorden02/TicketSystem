import java.util.List;

public class Film {
    private int id;
    private String title;
    private int duration;
    private String screeningTime;
    private Screen screen;

    public Film(int id, String title, int duration, String screeningTime) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.screeningTime = screeningTime;
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

    public Screen getScreen() {
        return screen;
    }

    public String getScreeningTime() {
        return screeningTime;
    }
}
