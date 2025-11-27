package eapli.shodrone.daemon.customer.presentation;

import eapli.framework.csv.util.CsvLineMarshaler;
import eapli.shodrone.daemon.customer.requests.*;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;

/**
 * The message parser for the Customer Service Protocol.
 * This class is responsible for parsing incoming messages from clients,
 * validating their format, and creating the appropriate request handler objects.
 */
public class MessageParser {

    private static final Logger LOGGER = LogManager.getLogger(MessageParser.class);
    private final ShodroneUserRepository userRepo = PersistenceContext.repositories().shodroneUsers();
    private final ShowProposalRepository proposalRepo = PersistenceContext.repositories().showProposals();


    /**
     * Parses a line of text from the client and returns a corresponding ProtocolRequest object.
     *
     * @param inputLine The line of text received from the client.
     * @return A ProtocolRequest object that can be executed.
     */
    public ProtocolRequest parse(final String inputLine) {
        String[] tokens;
        try {
            tokens = CsvLineMarshaler.tokenize(inputLine).toArray(new String[0]);
            if (tokens.length == 0) {
                return new BadRequest(inputLine, "Empty request");
            }
            final String command = tokens[0];

            switch (command) {
                case "LOGIN":
                    return parseLoginRequest(inputLine, tokens);
                case "LIST_SHOWS":
                    return parseListShowsRequest(inputLine, tokens);
                case "GET_SHOW_DETAILS":
                    return parseGetShowDetails(inputLine,tokens);
                case "RETRIEVE_FILE":
                    return parseRetrieveProposalFileRequest(inputLine, tokens);
                case "ACCEPT_PROPOSAL":
                    return parseUpdateProposalStatusRequest(inputLine, tokens, true);
                case "REJECT_PROPOSAL":
                    return parseUpdateProposalStatusRequest(inputLine, tokens, false);
                default:
                    return new UnknownRequest(inputLine);
            }
        } catch (final ParseException e) {
            LOGGER.info("Unable to parse request: {}", inputLine);
            return new BadRequest(inputLine, "Unable to parse request");
        } catch (final Exception e) {
            LOGGER.error("An unexpected error occurred during parsing: {}", inputLine, e);
            return new BadRequest(inputLine, "Server-side parsing error.");
        }
    }

    private ProtocolRequest parseLoginRequest(final String inputLine, final String[] tokens) throws ParseException {
        if (tokens.length != 3) {
            return new BadRequest(inputLine, "Wrong number of parameters for LOGIN. Expected: LOGIN,\"<username>\",\"<password>\"");
        }
        final String username = CsvLineMarshaler.unquote(tokens[1]);
        final String password = CsvLineMarshaler.unquote(tokens[2]);
        return new LoginRequest(inputLine, username, password);
    }

    private ProtocolRequest parseListShowsRequest(final String inputLine, final String[] tokens) throws ParseException {
        if (tokens.length != 2) {
            return new BadRequest(inputLine, "Wrong number of parameters for LIST_SHOWS. Expected: LIST_SHOWS,\"<vat>\", \"<all | past |future>\"");
        }
        final String vat = CsvLineMarshaler.unquote(tokens[1]);
        final String agr = CsvLineMarshaler.unquote(tokens[2]);
        return new ListShowsRequest(inputLine, vat, agr, userRepo);
    }

    private ProtocolRequest parseGetShowDetails(final String inputLine, final String[] tokens) throws ParseException {
        if(tokens.length != 1) {
            return new BadRequest(inputLine,"Wrong number of parameters for GET_SHOW_DETAILS. Expected: GET_SHOW_DETAILS,\"<id>\"");
        }

        return new ShowDetailRequest(inputLine,tokens);
    }

    private ProtocolRequest parseUpdateProposalStatusRequest(final String inputLine, final String[] tokens, boolean isAccepted) throws ParseException {
        if (tokens.length != 4) {
            return new BadRequest(inputLine, "Wrong number of parameters for this request. Expected: CMD,\"<vat>\",<proposal_id>,\"<feedback>\"");
        }
        try {
            final long proposalId = Long.parseLong(tokens[2]); // proposalId is not quoted
            final String feedback = CsvLineMarshaler.unquote(tokens[3]);
            return new UpdateProposalStatusRequest(inputLine, proposalId, feedback, isAccepted, proposalRepo);
        } catch (NumberFormatException e) {
            return new BadRequest(inputLine, "Invalid proposal_id. It must be a number.");
        }
    }


    private ProtocolRequest parseRetrieveProposalFileRequest(String inputLine, final String[] tokens){
            if(tokens.length != 1) {
                return new BadRequest(inputLine,"Wrong number of parameters for USE_CODE. Exepected: USE_CODE,\"<code>\"");
            }
            return new FileRequest(inputLine,tokens);
        }

}


