package model;


import java.util.ArrayList;

public class Student implements StudentDTO {
    int id;
    String studentTag;
    ArrayList<Rental> rentals;
    int activeRentals;

    public Student(int id, String studentTag){
        this.id = id;
        this.studentTag = studentTag;
        rentals = new ArrayList<>();
        activeRentals = 0;
    }

    public Student(int id, String studentTag, ArrayList<Rental> rentals, int activeRentals){
        this.id=id;
        this.studentTag=studentTag;
        this.rentals=rentals;
        this.activeRentals=activeRentals;
    }

    /**
     * Reduces @activeRentals by one.
     */
    public void reduceActiveRentals(){
        if(activeRentals>=1){
            activeRentals-=1;
        }
    }

    public void rentInstrument(Instrument instrument){

    }

    /**
     * Adds rental to @rentals and adds one to @activeRentals if rental in not flagged as terminated.
     * @param rental the rental to add to ArrayList @rentals
     */
    public void addRental(Rental rental){
        this.rentals.add(rental);
        if(!rental.getIsTerminated())
            activeRentals+=1;
    }

    /**
     * Sets @rentals to specified ArrayList, and sets @activeRentals to correct value.
     * @param rentals
     */
    public void setRentals(ArrayList<Rental> rentals) {
        this.rentals = rentals;
        activeRentals=0;
        for (Rental r: this.rentals){
            if(!r.getIsTerminated())
                this.activeRentals+=1;
        }
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public int getId() {
        return id;
    }

    public String getStudentTag() {
        return studentTag;
    }

    public int getActiveRentals() {
        return activeRentals;
    }

    /**
     * @return A string representation of all relevant fields in this object.
     */
    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Instrument: [");
        stringRepresentation.append("id: ");
        stringRepresentation.append(id);
        stringRepresentation.append(", username: ");
        stringRepresentation.append(studentTag);
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }
}
