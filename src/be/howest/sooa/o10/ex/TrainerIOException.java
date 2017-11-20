package be.howest.sooa.o10.ex;

/**
 *
 * @author hayk
 */
public class TrainerIOException extends RuntimeException {
    private static final String MESSAGE
            = "Could not find or save trainer.";
    
    public TrainerIOException(String message) {
        super(message);
    }
    
    public TrainerIOException(Throwable cause) {
        this(MESSAGE, cause);
    }
    
    public TrainerIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
