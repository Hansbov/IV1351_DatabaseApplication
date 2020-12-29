package model;

import java.util.Date;

public interface RentalDTO {

    public int getId();

    public Boolean getIsTerminated();

    public int getInstrumentId();

    public int getStudentId();

    public Date getRentalDate();

    public Date getReturnDate();
}
