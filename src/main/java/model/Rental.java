package model;

import java.util.Date;

public class Rental {
    private int id;
    private String rental_date;
    private String return_date;
    private int student_id;
    private int instrument_id;
    private Boolean is_terminated;

    public Rental(int id, String rental_date, String return_date, int student_id, int instrument_id, Boolean is_terminated){
        this.id = id;
        this.rental_date=rental_date;
        this.return_date=return_date;
        this.student_id=student_id;
        this.instrument_id=instrument_id;
        this.is_terminated=is_terminated;
    }
}
