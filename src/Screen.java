public class Screen {
    private static int idCounter = 0; // Class variable to track ids
    private int id;
    private int rows;
    private int columns;
    private int totalSeats;
    private boolean is3D;

    public Screen(int rows, int columns, boolean is3D) {
        this.id = ++idCounter;
        this.rows = rows;
        this.columns = columns;
        this.totalSeats = rows * columns;
        this.is3D = is3D;
    }

    // getters
    public int getId() {
        return id;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public boolean is3D() {
        return is3D;
    }

    public int getAvailableSeats() {
        return 0;
    }


}
