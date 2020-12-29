package view;

import controller.Controller;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Reads and interprets user commands. This command interpreter is blocking, the user
 * interface does not react to user input while a command is being executed.
 */
public class BlockingInterpreter {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private Controller ctrl;
    private boolean keepReceivingCmds = false;
    private Student currentUser = null;

    /**
     * Creates a new instance that will use the specified controller for all operations.
     *
     * @param ctrl The controller used by this instance.
     */
    public BlockingInterpreter(Controller ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Stops the commend interpreter.
     */
    public void stop() {
        keepReceivingCmds = false;
    }

    /**
     * Interprets and performs user commands. This method will not return until the
     * UI has been stopped. The UI is stopped either when the user gives the
     * "quit" command, or when the method <code>stop()</code> is called.
     */
    public void handleCmds() {
        keepReceivingCmds = true;
        while (keepReceivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    case QUIT:
                        keepReceivingCmds = false;
                        break;
                    case USER:
                        Student student = ctrl.getStudent(cmdLine.getParameter(0));
                        if(student!= null){
                            currentUser = student;
                        }
                        else
                            System.out.println("No such user.");

                        break;
                    case TERMINATE:
                        ArrayList<Rental> userRentals= currentUser.getRentals();
                        Rental rental = null;
                        for(Rental r: userRentals){
                            if(r.getId() == parseInt(cmdLine.getParameter(0))&& !r.getIsTerminated()){
                                rental = r;
                                break;
                            }
                        }
                        if(rental != null)
                            ctrl.terminateRental(currentUser, rental);
                        else
                            System.out.println("Specified rental could not be found on current user.");
                        break;
                    case SEARCH:
                        List<? extends InstrumentDTO> instruments = null;

                        instruments = ctrl.getInstrumentsOfType(cmdLine.getParameter(0));

                        for (InstrumentDTO instrument : instruments) {
                            System.out.println("id: " + instrument.getId() + ", "
                                   + "name: " + instrument.getName() + ", "
                                    + "brand: " + instrument.getBrand() + ", "
                                    + "fee: " + instrument.getFee());
                        }
                        break;
                    case RENT:
                        if(currentUser!=null) {
                            ctrl.rentInstrument(currentUser, parseInt(cmdLine.getParameter(0)), parseInt(cmdLine.getParameter(1)));
                        }
                        else
                            System.out.println("Please use USER to choose who to rent the instrument for.");
                        break;
                    case RENTALS:
                        List<? extends RentalDTO> rentals = null;
                        if(currentUser!=null) {
                            rentals = currentUser.getRentals();
                            for (RentalDTO r : rentals) {
                                if(!r.getIsTerminated()) {
                                    System.out.println("rental id: " + r.getId() + ", "
                                            + "rental date: " + r.getRentalDate() + ", "
                                            + "return date: " + r.getReturnDate() + ", "
                                            + "instrument id: " + r.getInstrumentId());
                                }
                            }
                        }
                        else System.out.println("Please use USER to choose which users rentals to view.");

                        break;
                    default:
                        System.out.println("illegal command");
                }
            } catch (Exception | StudentException e) {
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}
