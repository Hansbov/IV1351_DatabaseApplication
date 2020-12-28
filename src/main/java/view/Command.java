package view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Creates a new account.
     */
    NEW,
    /**
     * Lists all existing accounts.
     */
    LIST,
    /**
     * Deletes the specified account.
     */
    DELETE,
    /**
     * Deposits the specified amount to the specified account
     */
    DEPOSIT,
    /**
     * Withdraws the specified amount from the specified account
     */
    WITHDRAW,
    /**
     * Lists the balance of the specified account.
     */
    BALANCE,
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

