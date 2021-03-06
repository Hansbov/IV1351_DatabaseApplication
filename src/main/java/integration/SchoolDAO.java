package integration;

import model.Instrument;
import model.Rental;
import model.Student;
import model.StudentException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SchoolDAO {
    private static final String INSTRUMENT_TABLE_NAME = "instrument_to_rent";
    private static final String RENTAL_TABLE_NAME = "rental";
    private static final String STUDENT_TABLE_NAME = "student";
    private static final String INSTRUMENT_NAME_COLUMN_NAME = "instrument_tag";
    private static final String PK_ID_COLUMN_NAME = "id";
    private static final String INSTRUMENT_BRAND_COLUMN_NAME = "brand";
    private static final String INSTRUMENT_AVAILABLE_COLUMN_NAME = "is_available";
    private static final String INSTRUMENT_PRICE_COLUMN_NAME = "fee";
    private static final String INSTRUMENT_TYPE_COLUMN_NAME = "type_of_instrument";
    private static final String STUDENT_ID_COLUMN_NAME = "person_id";
    private static final String STUDENT_TAG_COLUMN_NAME = "student_tag";
    private static final String RENTAL_STUDENT_ID_COLUMN = "student_id";
    private static final String RENTAL_INSTRUMENT_ID_COLUMN = "instrument_id";
    private static final String RENTAL_START_DATE_COLUMN = "start_date";
    private static final String RENTAL_END_DATE_COLUMN ="end_date";
    private static final String RENTAL_TERMINATED_COLUMN = "is_terminated";


    private Connection connection;
    private PreparedStatement listInstrumentsStmt;
    private PreparedStatement rentInstrumentStmt;
    private PreparedStatement terminateRentalStmt;
    private PreparedStatement currentRentalsStmt;
    private PreparedStatement updateRentalStatusStmt;
    private PreparedStatement getStudentUserStmt;
    private PreparedStatement checkRentalStatusStmt;
    private PreparedStatement getStudentRentalsStmt;
    private PreparedStatement getRentalID;

    private int currentRentalID;


    /**
     * Constructs a new DAO object connected to the bank database.
     */
    public SchoolDAO() throws SchoolDBException{
        try {
            connectToSchoolDB();
            prepareStatements();
           ResultSet result = getRentalID.executeQuery();
           result.next();
           currentRentalID = result.getInt(PK_ID_COLUMN_NAME)+1;
        } catch (ClassNotFoundException | SQLException exception) {
            throw new SchoolDBException("Could not connect to database.", exception);
        }
    }


    private void connectToSchoolDB() throws SQLException, ClassNotFoundException {
       // Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project",
                "postgres", "mhansbo");
        connection.setAutoCommit(false);
    }

    /**
     * Find instruments available to rent and returns a list with these instruments.
     * @param typeOfInstrument the type of instrument (i.e. trumpet, guitar...)
     * @return A list of all instruments of a certain type that are available to rent.
     * @throws SchoolDBException if failed to search for instrument of specified type.
     */
    public List<Instrument> findInstrumentsToRentByType(String typeOfInstrument) throws SchoolDBException {
        String failureMSG = "Could not search for specified instrument type.";
        ResultSet result= null;
        List<Instrument> instruments = new ArrayList<>();
        try{
            listInstrumentsStmt.setString(1, typeOfInstrument);
            result = listInstrumentsStmt.executeQuery();
            while(result.next()){
                instruments.add(new Instrument(result.getInt(PK_ID_COLUMN_NAME),
                        result.getString(INSTRUMENT_NAME_COLUMN_NAME),
                        result.getString(INSTRUMENT_TYPE_COLUMN_NAME),
                        result.getString(INSTRUMENT_BRAND_COLUMN_NAME),
                        result.getDouble(INSTRUMENT_PRICE_COLUMN_NAME),
                        result.getBoolean(INSTRUMENT_AVAILABLE_COLUMN_NAME)));
            }
            connection.commit();
        }
        catch (SQLException sqlException) {
            handleException(failureMSG, sqlException);
        }
        return instruments;
    }

    /**
     * Rents an instrument for a specific student.
     * @param student the student to who wants to rent the instrument
     * @param instrumentID the ID of the instrument to be rented
     * @param monthsToRent How many months the instrument will be rented for
     * @throws SchoolDBException if failed to rent specified instrument by specified student.
     */
    public void rentInstrument(Student student, int instrumentID, int monthsToRent ) throws SchoolDBException {
        String failureMSG = "Could not rent specified instrument for specified student.";
        ResultSet available = null;
        int studentID = student.getId();
        LocalDate date = LocalDate.now();
        LocalDate plusMonth = date.plusMonths(monthsToRent);
        try {
            checkRentalStatusStmt.setInt(1,instrumentID);
            available = checkRentalStatusStmt.executeQuery();
            available.next();
            Boolean result = available.getBoolean(INSTRUMENT_AVAILABLE_COLUMN_NAME);

            if(result){

                int updatedRows = 0;
                rentInstrumentStmt.setInt(1, currentRentalID);
                rentInstrumentStmt.setDate(2, Date.valueOf(date));
                rentInstrumentStmt.setDate(3, Date.valueOf(plusMonth));
                rentInstrumentStmt.setInt(4, studentID);
                rentInstrumentStmt.setInt(5,instrumentID);
                updatedRows = rentInstrumentStmt.executeUpdate();
                if (updatedRows != 1) {
                    handleException(failureMSG, null);
                }
                else{
                    updateRentalStatusStmt.setBoolean(1, false);
                    updateRentalStatusStmt.setInt(2, instrumentID);
                    updatedRows = updateRentalStatusStmt.executeUpdate();
                    if (updatedRows != 1) {
                        handleException(failureMSG, null);
                    }
                }
                Rental rental = new Rental(currentRentalID,
                        Date.valueOf(date),
                        Date.valueOf(plusMonth),
                        studentID,
                        instrumentID,
                        false);
                currentRentalID+=1;
                student.addRental(rental);

                connection.commit();
            }
        } catch (SQLException | StudentException sqlException) {
            handleException(failureMSG, sqlException);
        }
    }

    /**
     * Returns the a Student with the specified user name.
     * @param UserName the user name of the student
     * @return a Student
     * @throws SchoolDBException if failed to search for student
     */
    public Student findStudent (String UserName) throws SchoolDBException {
        String failureMSG = "Could not find specified user.";
        ResultSet result = null;
        Student student = null;

        try {
            getStudentUserStmt.setString(1,UserName);
            result = getStudentUserStmt.executeQuery();
            if (result.next()){
                student = new Student(result.getInt(STUDENT_ID_COLUMN_NAME),
                        result.getString(STUDENT_TAG_COLUMN_NAME));

                if(student!=null) {
                    ArrayList<Rental> rentals = new ArrayList<>();
                    getStudentRentalsStmt.setInt(1, student.getId());
                    result = getStudentRentalsStmt.executeQuery();
                    while (result.next()) {
                        rentals.add(new Rental(result.getInt(PK_ID_COLUMN_NAME),
                                result.getDate(RENTAL_START_DATE_COLUMN),
                                result.getDate(RENTAL_END_DATE_COLUMN),
                                result.getInt(RENTAL_STUDENT_ID_COLUMN),
                                result.getInt(RENTAL_INSTRUMENT_ID_COLUMN),
                                result.getBoolean(RENTAL_TERMINATED_COLUMN)));
                    }
                    student.setRentals(rentals);
                }
                else{
                    handleException(failureMSG, null);
                }
            }
            else {
                handleException(failureMSG,null);
            }
            connection.commit();
        } catch (SQLException | StudentException sqlException) {
            handleException(failureMSG,sqlException);
        }
        return student;
    }



    public void terminateRental(int studentID, int rentalID, int instrumentID) throws SchoolDBException {
        String failureMSG = "Could not terminate specified rental.";
        try {
            terminateRentalStmt.setInt(1,studentID);
            terminateRentalStmt.setInt(2,rentalID);
            int updatedRows = 0;
            updatedRows = terminateRentalStmt.executeUpdate();
            if(updatedRows!=1)
                handleException(failureMSG, null);

            updateRentalStatusStmt.setBoolean(1,true);
            updateRentalStatusStmt.setInt(2, instrumentID);
            updatedRows = updateRentalStatusStmt.executeUpdate();
            if(updatedRows!=1){
                handleException(failureMSG, null);
            }

            connection.commit();
        } catch (SQLException sqlException) {
            handleException(failureMSG,sqlException);
        }

    }


    private void prepareStatements() throws SQLException {
        listInstrumentsStmt = connection.prepareStatement("SELECT " +
                PK_ID_COLUMN_NAME+ ","+ INSTRUMENT_NAME_COLUMN_NAME+ "," +INSTRUMENT_TYPE_COLUMN_NAME+","+
                INSTRUMENT_BRAND_COLUMN_NAME+ ","+INSTRUMENT_PRICE_COLUMN_NAME+","+
                INSTRUMENT_AVAILABLE_COLUMN_NAME +
                " FROM "+INSTRUMENT_TABLE_NAME +
                " WHERE "+INSTRUMENT_AVAILABLE_COLUMN_NAME +"= True AND "+ INSTRUMENT_TYPE_COLUMN_NAME +"= ?");

        rentInstrumentStmt = connection.prepareStatement("INSERT INTO "+ RENTAL_TABLE_NAME +
                " (" +PK_ID_COLUMN_NAME+","+RENTAL_START_DATE_COLUMN+","
                +RENTAL_END_DATE_COLUMN+", "+RENTAL_STUDENT_ID_COLUMN+"," +RENTAL_INSTRUMENT_ID_COLUMN+") " +
                "VALUES (?, ?, ?, ?, ?)");

        checkRentalStatusStmt = connection.prepareStatement( "SELECT "+INSTRUMENT_AVAILABLE_COLUMN_NAME+
                " FROM "+INSTRUMENT_TABLE_NAME+
                " WHERE " + PK_ID_COLUMN_NAME +" = ?");

        updateRentalStatusStmt = connection.prepareStatement("UPDATE " +INSTRUMENT_TABLE_NAME+
                " SET "+INSTRUMENT_AVAILABLE_COLUMN_NAME+" = ? " +
                "WHERE "+PK_ID_COLUMN_NAME+" = ?");

        terminateRentalStmt = connection.prepareStatement("UPDATE "+RENTAL_TABLE_NAME+
                " SET "+RENTAL_TERMINATED_COLUMN+" = true" +
                " WHERE "+RENTAL_STUDENT_ID_COLUMN+" = ? AND "+PK_ID_COLUMN_NAME+" = ?");

        currentRentalsStmt = connection.prepareStatement("SELECT " +
                RENTAL_START_DATE_COLUMN +"," +RENTAL_END_DATE_COLUMN+"," +
                INSTRUMENT_NAME_COLUMN_NAME+", "+INSTRUMENT_PRICE_COLUMN_NAME +
                " FROM "+RENTAL_TABLE_NAME+" AS r" +
                " INNER JOIN "+INSTRUMENT_TABLE_NAME+" AS i" +
                " ON i."+PK_ID_COLUMN_NAME+" = r."+RENTAL_INSTRUMENT_ID_COLUMN+
                " WHERE r."+RENTAL_STUDENT_ID_COLUMN+" = ? AND r."+RENTAL_TERMINATED_COLUMN+" = false");

        getStudentUserStmt = connection.prepareStatement("SELECT " +
                STUDENT_ID_COLUMN_NAME +", "+ STUDENT_TAG_COLUMN_NAME+
                " FROM " +STUDENT_TABLE_NAME+
                " WHERE " + STUDENT_TAG_COLUMN_NAME +"= ?");

        getStudentRentalsStmt = connection.prepareStatement("SELECT *" +
                " FROM " + RENTAL_TABLE_NAME +
                " WHERE " + RENTAL_STUDENT_ID_COLUMN +" = ?");

        getRentalID = connection.prepareStatement( "SELECT " +
                PK_ID_COLUMN_NAME +" FROM "+RENTAL_TABLE_NAME+
                " ORDER BY " +PK_ID_COLUMN_NAME+ " DESC LIMIT 1");
    }

    private void handleException(String failureMsg, Exception cause) throws SchoolDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new SchoolDBException(failureMsg, cause);
        } else {
            throw new SchoolDBException(failureMsg);
        }
    }
}
