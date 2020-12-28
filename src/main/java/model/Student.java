package model;


import java.util.ArrayList;

public class Student implements StudentDTO {
    int id;
    String student_tag;
    ArrayList<Rental> rentals;

    public Student(int id, String student_tag){
        this.id = id;
        this.student_tag = student_tag;
        rentals = new ArrayList<>();
    }

    public void rentInstrument(Instrument instrument){

    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public int getId() {
        return id;
    }

    public String getStudent_tag() {
        return student_tag;
    }
}
