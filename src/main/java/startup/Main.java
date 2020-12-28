package startup;

import controller.Controller;
import integration.SchoolDBException;
import view.BlockingInterpreter;

/**
 * Starts the school client.
 */
public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
            new BlockingInterpreter(new Controller()).handleCmds();
        } catch(SchoolDBException bdbe) {
            System.out.println("Could not connect to School db.");
            bdbe.printStackTrace();
        }
    }
}