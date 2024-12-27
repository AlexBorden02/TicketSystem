public class Booking {
    private int id;
    private Film film;
    private Screen screen;
    private Seat seat;
    private String customer;

    public Booking(int id, Film film, Screen screen, Seat seat, String customer) {
        this.id = id;
        this.film = film;
        this.screen = screen;
        this.seat = seat;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public Screen getScreen() {
        return screen;
    }

    public Seat getSeat() {
        return seat;
    }

    public String getCustomer() {
        return customer;
    }

    public void confirmBooking() {
        seat.bookSeat();
    }

    public void cancelBooking() {
        seat.cancelBooking();
    }
}