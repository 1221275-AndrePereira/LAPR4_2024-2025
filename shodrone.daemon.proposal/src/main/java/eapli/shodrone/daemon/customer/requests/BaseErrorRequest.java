package eapli.shodrone.daemon.customer.requests;

import eapli.framework.csv.CsvRecord;

/**
 * An abstract base class for all error-related requests.
 * Provides a standardized way to build error responses.
 */
public abstract class BaseErrorRequest extends ProtocolRequest {

    private final String errorDescription;

    protected BaseErrorRequest(final String request, final String errorDescription) {
        super(request);
        this.errorDescription = errorDescription;
    }

    protected BaseErrorRequest(final String request) {
        super(request);
        this.errorDescription = null;
    }

    @Override
    public String execute() {
        return buildResponse();
    }

    /**
     * Builds a standardized error response string.
     * @return A formatted error message.
     */
    protected String buildResponse() {
        final Object[] fields = {
                messageType(),
                request,
                errorDescription
        };
        final boolean[] mask = { false, true, true };
        return CsvRecord.valueOf(fields, mask).toString() + "\n";
    }

    /**
     * Returns the specific type of error message.
     * @return A string representing the error type.
     */
    protected abstract String messageType();
}
