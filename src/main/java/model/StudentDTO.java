package model;

import java.util.ArrayList;

/**
 * Specifies a read only view of Student
 */
public interface StudentDTO {

    public ArrayList<Rental> getRentals();

    public String getStudent_tag();

}
