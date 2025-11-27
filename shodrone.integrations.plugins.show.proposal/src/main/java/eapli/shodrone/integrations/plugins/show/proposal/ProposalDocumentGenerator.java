package eapli.shodrone.integrations.plugins.show.proposal;

import eapli.shodrone.customerRepresentative.domain.CustomerRepresentative;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.proposalDocumentTemplate.domain.ProposalDocumentTemplate;

import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.domain.ShowProposal;

import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalFigure;
import eapli.shodrone.showProposalDocument.application.DocumentGenerator;
import eapli.shodrone.showrequest.domain.*;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class ProposalDocumentGenerator implements DocumentGenerator {

    private final ShowRequestRepository showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();

    @Override
    public StringBuilder generate(StringBuilder content, ShowProposal proposal, ProposalDocumentTemplate proposalDocumentTemplate, ShodroneUser user) {


        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(proposalDocumentTemplate.getProposalDocumentTemplateFilePath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line;
        boolean inDroneList = false;
        boolean inFigureList = false;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line.trim().toLowerCase().contains("list") && line.trim().toLowerCase().contains("drones")) {
                inDroneList = true; //Enters the list of drone types
                content.append(line).append("\r\n");
                continue;
            } else if (line.trim().toLowerCase().contains("list") && line.trim().toLowerCase().contains("figures")) {
                inDroneList = false; //Gets out of drone types list(if there)
                inFigureList = true; //Enters the list of figures
                content.append(line).append("\r\n");
                continue;
            }

            if (inDroneList && line.contains("[model] – [quantity] units")) {
                for (ProposalDroneModel droneModel : proposal.getDroneModels()) {
                    String droneLine = line.replace("[model]", droneModel.droneModel().getModelName())
                            .replace("[quantity]", String.valueOf(droneModel.nDrones()));
                    content.append(droneLine).append("\r\n");
                }
                continue;
            } else if (inFigureList && line.contains("[position in show] – [figure name]")) {
                int position = 1;
                for (ProposalFigure proposalFigure : proposal.getFigures()) {
                    String figureLine = line.replace("[position in show]", String.valueOf(position))
                            .replace("[figure name]", "Figure " + proposalFigure.getFigure().getId());
                    content.append(figureLine).append("\r\n");
                    position++;
                }
                continue;
            }

            Iterable<ShowRequest> allShowRequests = showRequestRepository.obtainAllShowRequests();



            ShowRequest showRequest = StreamSupport.stream(allShowRequests.spliterator(), false)
                    .filter(request -> request.getProposals() != null)
                    .filter(request -> request.getProposals().stream()
                            .anyMatch(showProposal -> Objects.equals(showProposal.identity(), proposal.identity())))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No show proposal with this id is associated with a show request"));

            ShodroneUser customer = showRequest.getCustomer();


            // Process other lines with placeholders
            content.append(replacePlaceholders(line, proposal, customer)).append("\r\n");
        }


        return content;
    }


    /**
     * Replace the placeholders(in brackets) by values from the show proposal
     *
     * @param content  the content of the current line
     * @param proposal the show proposal
     * @return the result(placeholders replaced with values from show proposal)
     */
    private String replacePlaceholders(String content, ShowProposal proposal, ShodroneUser user) {
        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
        List<CustomerRepresentative> customerRepresentatives = user.customerRepresentatives();
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer();

        //Using the switch we will replace everything within brackets to their corresponding data regarding show proposals
        while (matcher.find()) {
            String placeholder = matcher.group(1); // Get text inside brackets
            placeholder = placeholderReplacement(placeholder);
            String documentReplacement;
            switch (placeholder) {

                //Obtain the address of the recipient of the document
                case "ADDRESS":
                    documentReplacement = String.format("%s\r\n%s %s", user.address().streetAddress(),
                            user.address().postalCode(),
                            user.address().city());
                    break;

                //Obtain the company of the recipient of the document
                case "COMPANY":
                    String companyEmail = customerRepresentatives.getFirst().companyEmail().toString();
                    documentReplacement = companyEmail.substring(
                            companyEmail.indexOf("@") + 1,
                            companyEmail.lastIndexOf(".")
                    );
                    break;

                //Obtain the customer name
                case "CUSTOMER":
                    documentReplacement = user.systemUser().name().toString();
                    break;

                //Obtain the customer representative name
                case "CUSTOMER REPRESENTATIVE":
                    documentReplacement = customerRepresentatives.getFirst().systemUser().name().toString();
                    break;

                //Obtain the duration of the show proposed(in minutes)
                case "DURATION":
                    documentReplacement = String.valueOf(proposal.getDuration());
                    break;

                //Obtain the gps location of where the proposed show would take place
                case "GPS LOCATION":
                    documentReplacement = proposal.getProposedPlace().getLatitude() + ", " + proposal.getProposedPlace().getLongitude();
                    break;

                //Obtain the insurance amount of this show proposal
                case "INSURANCE":
                    documentReplacement = proposal.getInsuranceAmount().getInsuranceAmount() + " euros";
                    break;

                //Page break
                case "PAGE BREAK":
                    documentReplacement = "----------------------------------------------------------------------------------------------";
                    break;

                //Obtain the date of the proposal creation
                case "PROPOSAL DATE":
                    documentReplacement = proposal.getCreationDate().date().getDayOfMonth() + "/" + proposal.getCreationDate().date().getMonthValue() + "/" + proposal.getCreationDate().date().getYear();
                    break;

                //Obtain the number(id) of the show proposal
                case "PROPOSAL NUMBER":
                    documentReplacement = proposal.identity().toString();
                    break;

                //Obtain the sender of the document(CRM Manager or admin)
                case "SENDER":
                    documentReplacement = proposal.getManager().name().toString();
                    break;

                //Obtain the date in which the show would take place(DD/MM/YYYY format)
                case "SHOW DATE":
                    documentReplacement = proposal.getProposedShowDate().date().getDayOfMonth() + "/" + proposal.getProposedShowDate().date().getMonthValue() + "/" + proposal.getProposedShowDate().date().getYear();
                    break;

                //Obtain the hour and minute in which the show would take place(HH:MM format)
                case "SHOW TIME":
                    documentReplacement = proposal.getProposedShowDate().date().getHour() + ":" + proposal.getProposedShowDate().date().getMinute();
                    break;

                //Obtain the VAT Number of the recipient of the document
                case "VAT NUMBER":
                    documentReplacement = user.vatNumber().toString(); //obtains the vat number of the recipient of the document
                    break;

                //Obtain the link to the video of the show
                case "VIDEO LINK":
                    documentReplacement = proposal.getVideoLink().toString();
                    break;


                //Obtains UNKNOWN_VALUE in case the placeholder is out of the scope of the proposal data
                default:
                    documentReplacement = "UNKNOWN_VALUE";
                    break;
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(documentReplacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Replaces the text inside brackets(placeholder) for a keyword related to show proposal data
     *
     * @param placeholder the key
     * @return the updated key
     */
    private String placeholderReplacement(String placeholder) {
        if (placeholder.toLowerCase().contains("address")) {
            placeholder = "ADDRESS";
        } else if (placeholder.toLowerCase().contains("company")) {
            placeholder = "COMPANY";
        } else if (placeholder.toLowerCase().equals("customer")) {
            placeholder = "CUSTOMER";
        } else if (placeholder.toLowerCase().contains("customer representative")) {
            placeholder = "CUSTOMER REPRESENTATIVE";
        } else if (placeholder.toLowerCase().contains("duration")) {
            placeholder = "DURATION";
        } else if (placeholder.toLowerCase().contains("location") || placeholder.toLowerCase().contains("gps coordinates")) {
            placeholder = "GPS LOCATION";
        } else if (placeholder.toLowerCase().contains("insurance")) {
            placeholder = "INSURANCE";
        } else if (placeholder.toLowerCase().contains("page break")) {
            placeholder = "PAGE BREAK";
        } else if (placeholder.toLowerCase().equals("date")) {
            placeholder = "PROPOSAL DATE";
        } else if (placeholder.toLowerCase().contains("proposal") && placeholder.toLowerCase().contains("number")) {
            placeholder = "PROPOSAL NUMBER";
        } else if (placeholder.toLowerCase().contains("crm manager") || placeholder.toLowerCase().contains("admin")) {
            placeholder = "SENDER";
        } else if (placeholder.toLowerCase().contains("date") && (placeholder.toLowerCase().contains("event") || placeholder.toLowerCase().contains("show"))) {
            placeholder = "SHOW DATE";
        } else if (placeholder.toLowerCase().contains("time") && (placeholder.toLowerCase().contains("event") || placeholder.toLowerCase().contains("show"))) {
            placeholder = "SHOW TIME";
        } else if (placeholder.toLowerCase().contains("vat number")) {
            placeholder = "VAT NUMBER";
        } else if (placeholder.toLowerCase().contains("link") && placeholder.toLowerCase().contains("video")) {
            placeholder = "VIDEO LINK";
        }


        return placeholder;
    }
}
