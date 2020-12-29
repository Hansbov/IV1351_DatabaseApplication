package controller;
import integration.SchoolDAO;
import integration.SchoolDBException;
import model.*;

import java.util.ArrayList;
import java.util.List;


/**
 *The controller of the application. All calls to the model and integration layer go through here.
 */
public class Controller {
private final SchoolDAO schoolDAO;

    /**
     * Creates a new instance of the Controller and retrieves a connection to the database.
     * @throws SchoolDBException if unable to connect to Database
     */
    public Controller() throws SchoolDBException {
    schoolDAO =new SchoolDAO();
    }

    /**
     * Gets a list of instruments of a specified type that are available to rent. The list is empty if there
     * are no instruments available.
     * @param typeOfInstrument the type of instrument (e.i. guitar, trumpet...)
     * @return a List of Instruments
     * @throws InstrumentException if unable to search for instruments.
     */
    public List<? extends InstrumentDTO> getInstrumentsOfType(String typeOfInstrument) throws InstrumentException {
        try {
            return schoolDAO.findInstrumentsToRentByType(typeOfInstrument);
        } catch (SchoolDBException e) {
            throw new InstrumentException("Unable to search for instruments", e);
        }
    }

    /**
     * Creates a rental of a specified instrument on a specified student if student has
     * less than two active rentals. Increases number of active rentals on student by one.
     * @param student The student to rent the instrument
     * @param instrumentToRent The instrument to rent
     * @param numberOfMonth the number of months to rent the instrument
     * @throws RentalException if unable to rent instrument.
     * @throws StudentException if student two or more active rentals.
     */
    public void rentInstrument(Student student, int instrumentToRent, int numberOfMonth) throws RentalException, StudentException {
        Rental rental = null;
        if(student.getActiveRentals() < 2) {
            try {
                rental = schoolDAO.rentInstrument(student.getId(), instrumentToRent, numberOfMonth);
                student.addRental(rental);
            } catch (SchoolDBException e) {
                throw new RentalException("Unable to make rental.", e);
            }
        }
        else
            throw new StudentException("Student is not allowed to exceed rental limits.");

    }

    /**
     * Sets a specified rental as terminated and reduces number of active rentals with one.
     * @param student the student that has the rental
     * @param rental the rental to terminate
     * @throws RentalException if unable to terminate the rental
     * @throws StudentException if student does not own specified rental.
     */
    public void terminateRental(Student student, Rental rental) throws RentalException, StudentException {
        if(student.getRentals().contains(rental)){
            try {
                schoolDAO.terminateRental(student.getId(),rental.getId(), rental.getInstrumentId());
                rental.setIsTerminated(true);
                student.reduceActiveRentals();
            } catch (SchoolDBException e) {
               throw new RentalException("Could not terminate rental.", e);
            }
        }
        else
            throw new StudentException("Student does not own specified rental.");
    }


    public Student getStudent( String userName) throws StudentException {
        Student student;

        try{
            student = schoolDAO.findStudent(userName);
        } catch (SchoolDBException e) {
            throw new StudentException("Could not find student.", e);
        }

        return student;
    }


}
