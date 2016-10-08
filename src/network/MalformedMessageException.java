package network;

/**
 * Created by archer on 2016-10-07.
 */
public class MalformedMessageException extends Exception {
    public MalformedMessageException() {
    }

    public MalformedMessageException(String message) {
        super(message);
    }

    public MalformedMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedMessageException(Throwable cause) {
        super(cause);
    }

    public MalformedMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
