package eapli.shodrone.daemon.customer.requests;

import eapli.framework.csv.CsvRecord;

/**
 * An abstract base class for all protocol requests.
 * It provides common functionality for building responses and handling errors.
 */
public abstract class ProtocolRequest {

    protected final String request;

    /**
     * Constructs a protocol request.
     * @param request The original request string from the client.
     */
    protected ProtocolRequest(final String request) {
        this.request = request;
    }

    /**
     * Executes the logic of the request and returns a response.
     * @return A string containing the response to be sent to the client.
     */
    public abstract String execute();

    /**
     * Checks if the request is a "goodbye" message, indicating the client wants to disconnect.
     * @return true if the client is disconnecting, false otherwise.
     */
    public boolean isGoodbye() {
        return false;
    }

    /**
     * Builds a server error response.
     * @param errorDescription A description of the error.
     * @return A formatted error message.
     */
    protected String buildServerError(final String errorDescription) {
        final BaseErrorRequest r = new BaseErrorRequest(request, errorDescription) {
            @Override
            protected String messageType() {
                return "SERVER_ERROR";
            }
        };
        return r.buildResponse();
    }

    /**
     * Builds a bad request error response.
     * @param errorDescription A description of why the request was bad.
     * @return A formatted error message.
     */
    protected String buildBadRequest(final String errorDescription) {
        final BaseErrorRequest r = new BaseErrorRequest(request, errorDescription) {
            @Override
            protected String messageType() {
                return "BAD_REQUEST";
            }
        };
        return r.buildResponse();
    }
}
