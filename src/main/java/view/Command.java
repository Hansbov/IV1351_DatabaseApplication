package view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Sets a current user to specified student.
     */
    USER,
    /**
     * Lists all instrument by specified type that are available to rent.
     */
    SEARCH,
    /**
     * Terminates a specified rental.
     */
    TERMINATE,
    /**
     * Rents specified instrument on current user.
     */
    RENT,
    /**
     * Lists all rentals on current user.
     */
    RENTALS,
    /**
     * Lists all commands.
     */
    HELP,
    /**
     * Leave the chat application.
     */
    QUIT,
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND
}

