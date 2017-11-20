package be.howest.sooa.o10.ex;

/**
 *
 * @author Hayk
 */
public class DBException extends RuntimeException {

    private static final String MESSAGE
            = "Something went wrong with the database connection.";
    
    public DBException(String message) {
        super(message);
    }
    
    public DBException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
