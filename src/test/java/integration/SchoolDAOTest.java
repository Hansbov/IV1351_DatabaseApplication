package integration;

import model.Rental;
import model.StudentDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolDAOTest {
    SchoolDAO schoolDAO;
    StudentDTO student;
    Rental rental;

    @BeforeEach
    void setUp() {
        try {
            schoolDAO = new SchoolDAO();
        } catch (SchoolDBException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        schoolDAO = null;
        student = null;
        rental = null;
    }

    @Test
    void findInstrumentsToRentByType() {
        try {
            assertNotNull(schoolDAO.findInstrumentsToRentByType("guitar"));
        } catch (SchoolDBException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findStudent() {
        try {
            student = schoolDAO.findStudent("KAKA");
            assertNotNull(student);
            assertEquals("KAKA", student.getStudentTag());
        } catch (SchoolDBException e) {
            e.printStackTrace();
        }
    }

    @Test
    void rentInstrument() {
        try {
            rental = schoolDAO.rentInstrument(student.getId(),1,1);
            assertNotNull(rental);
            assertEquals(student.getId(), rental.getStudentId());
            assertEquals(1, rental.getInstrumentId());
            assertTrue(student.getRentals().contains(rental));
        } catch (SchoolDBException e) {
            e.printStackTrace();
        }
    }


    @Test
    void terminateRental() {
        try{
            schoolDAO.terminateRental(student.getId(), rental.getId(), rental.getInstrumentId());
            assertTrue(rental.getIsTerminated());

        } catch (SchoolDBException e) {
            e.printStackTrace();
        }

    }
}