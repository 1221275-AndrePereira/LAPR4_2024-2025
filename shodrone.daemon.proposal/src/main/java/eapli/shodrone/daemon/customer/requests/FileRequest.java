package eapli.shodrone.daemon.customer.requests;

import eapli.framework.csv.util.CsvLineMarshaler;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

public class FileRequest extends ProtocolRequest{

    private String code;
    ListShowProposalController controller =  new ListShowProposalController();

    /**
     * Constructs a protocol request.
     *
     * @param request The original request string from the client.
     */
    public FileRequest(String request, String[] tokens) {
        super(request);
        this.code = CsvLineMarshaler.unquote(tokens[0]);
    }

    @Override
    public String execute() {
        ShowProposal proposal = null;
        String extractedID = code.substring(code.length() - 3);

        Optional<ShowProposal> opProposal = controller.findByID(Long.parseLong(extractedID));

        if(opProposal.isPresent()){
            proposal = opProposal.get();
        }else{
            return ("Code not found");
        }

        String relativePath = proposal.getProposalDocument().getFilePath();
        Path absolutePath = Paths.get(System.getProperty("user.dir")).resolve(relativePath);
        File file = absolutePath.toFile();

        if (!file.exists() || !file.isFile()) {
            return buildBadRequest("Associated document file not found on server.");
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64Content = Base64.getEncoder().encodeToString(fileContent);

            String fileName = file.getName();
            long fileSize = file.length();

            return String.format("%s;%d;%s", fileName, fileSize, base64Content);

        } catch (IOException e) {

            return buildBadRequest("Error reading file on server: " + e.getMessage());
        }
    }

}

