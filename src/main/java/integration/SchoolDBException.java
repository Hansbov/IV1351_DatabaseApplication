package integration;

/**
 * Thrown when a call to the bank database fails.
 */
public class SchoolDBException extends Exception {

    /**
     * Create a new instance thrown because of the specified reason.
     *
     * @param reason Why the exception was thrown.
     */
    public SchoolDBException(String reason) {
        super(reason);
    }

    /**
     * Create a new instance thrown because of the specified reason and exception.
     *
     * @param reason    Why the exception was thrown.
     * @param rootCause The exception that caused this exception to be thrown.
     */
    public SchoolDBException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}