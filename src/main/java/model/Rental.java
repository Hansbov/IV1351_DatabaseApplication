package model;

import java.util.Date;

public class Rental implements RentalDTO {
    private int id;
    private Date rentalDate;
    private Date returnDate;
    private int studentId;
    private int instrumentId;
    private Boolean isTerminated;

    public Rental(int id, Date rentalDate, Date returnDate, int studentId, int instrumentId, Boolean isTerminated){
        this.id = id;
        this.rentalDate =rentalDate;
        this.returnDate =returnDate;
        this.studentId =studentId;
        this.instrumentId = instrumentId;
        this.isTerminated =isTerminated;
    }

    public void setIsTerminated(Boolean b){
        isTerminated=b;
    }

    public int getId() {
        return id;
    }

    public Boolean getIsTerminated() {
        return isTerminated;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
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
        stringRepresentation.append(", rental date: ");
        stringRepresentation.append(rentalDate);
        stringRepresentation.append(", type: return date");
        stringRepresentation.append(returnDate);
        stringRepresentation.append(", student id: ");
        stringRepresentation.append(studentId);
        stringRepresentation.append(", instrument id: ");
        stringRepresentation.append(instrumentId);
        stringRepresentation.append(", inactive: ");
        stringRepresentation.append(isTerminated);
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }
}
