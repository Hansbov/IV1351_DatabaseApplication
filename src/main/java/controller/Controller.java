package controller;
import integration.SchoolDAO;
import integration.SchoolDBException;
import model.Instrument;
import model.InstrumentDTO;
import model.InstrumentException;

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
     */
    public List<? extends InstrumentDTO> getInstrumentsOfType(String typeOfInstrument) throws InstrumentException {
        try {
            return schoolDAO.findInstrumentsToRentByType(typeOfInstrument);
        } catch (SchoolDBException e) {
            throw new InstrumentException("Unable to search for instruments", e);
        }
    }




}
