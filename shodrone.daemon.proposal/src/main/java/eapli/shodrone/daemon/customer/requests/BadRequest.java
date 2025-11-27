package eapli.shodrone.daemon.customer.requests;

/**
 * Represents a request that is syntactically incorrect.
 */
public class BadRequest extends BaseErrorRequest {

    public BadRequest(final String request, final String errorDescription) {
        super(request, errorDescription);
    }

    @Override
    protected String messageType() {
        return "ERROR_IN_REQUEST";
    }
}
