package eapli.shodrone.daemon.customer.requests;

/**
 * Represents a request with an unrecognized command.
 */
public class UnknownRequest extends BaseErrorRequest {

    public UnknownRequest(final String inputLine) {
        super(inputLine, "Unknown request type");
    }

    @Override
    protected String messageType() {
        return "UNKNOWN_REQUEST";
    }
}
