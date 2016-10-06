package Network;

/**
 * Created by archer on 2016-10-06.
 */
public class NoClientSpecifiedException extends Exception {
    public NoClientSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoClientSpecifiedException(Throwable cause) {
        super(cause);
    }

    public NoClientSpecifiedException() {
    }

    public NoClientSpecifiedException(String message) {
        super(message);
    }
}
