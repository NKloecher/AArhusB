package model;

import java.time.LocalDateTime;

public class Tour {
    private int persons;
    private LocalDateTime date;
    private double price;
    private double duration;

    public Tour(int persons, LocalDateTime date, double price, double duration) {
        this.persons = persons;
        this.date = date;
        this.price = price;
        this.duration = duration;
    }
}
