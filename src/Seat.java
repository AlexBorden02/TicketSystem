public class Seat {
    private int id;
    private Screen screen;
    private boolean isBooked;
    private boolean isWheelchairAccessible;

    public Seat(int id, Screen screen, boolean isWheelchairAccessible) {
        this.id = id;
        this.screen = screen;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.isBooked = false;
    }

    public int getId() {
        return id;
    }

    public Screen getScreen() {
        return screen;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public void bookSeat() {
        if (!isBooked) {
            isBooked = true;
        } else {
            throw new IllegalStateException("Seat is already booked.");
        }
    }

    public void cancelBooking() {
        if (isBooked) {
            isBooked = false;
        } else {
            throw new IllegalStateException("Seat is not booked.");
        }
    }
}
