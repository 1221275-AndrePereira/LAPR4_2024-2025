package eapli.shodrone.infrastructure.boostrapers.demo;

import eapli.framework.actions.Action;

import eapli.shodrone.drone.application.ListDronesInventoryController;
import eapli.shodrone.droneModel.application.ListDroneModelController;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.showProposal.application.*;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;

import eapli.shodrone.showProposal.application.programManaging.GenerateProgramsController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.domain.proposalFields.*;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationReport;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationResult;
import eapli.shodrone.showProposalDocument.application.GenerateProposalDocumentController;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.showrequest.domain.ShowRequest;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

public class ShowProposalBootstrapper implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowProposalBootstrapper.class);

    private final CreateShowProposalController controller = new CreateShowProposalController();

    private final ShowRequestRepository showRequestRepository = PersistenceContext.repositories().showRequestCatalogue();
    private final AddFigureToProposalController addFigureToProposalController = new AddFigureToProposalController();
    private final AddDroneToProposalController addDroneToProposalController = new AddDroneToProposalController();
    private final ListDronesInventoryController listDronesInventoryController = new ListDronesInventoryController();
    private final ListDroneModelController listDroneModelController = new ListDroneModelController();

    //private final

    public ShowProposalBootstrapper() {
    }


    @Override
    public boolean execute() {
        Iterable<ShowRequest> showRequests = showRequestRepository.obtainAllShowRequests();

        int count = 0;
        for (ShowRequest showRequest : showRequests) {
            if (count == 4) {
                break;
            }
            try {
                // Randomize Insurance number
                Long insuranceNumber = (long) (Math.random() * 1000000000L);
                createShowProposal(
                        showRequest,
                        showRequest.getDescription().toString(),
                        ProposalNDrones.valueOf(showRequest.getNdDrones().number()),
                        ProposedDuration.valueOf(showRequest.getDuration().minutes()),
                        new Insurance(insuranceNumber),
                        ProposedPlace.valueOf(showRequest.getPlace().getLatitude(), showRequest.getPlace().getLongitude()),
                        ProposedShowDate.valueOf(showRequest.getShowDate().date()));

                count++;
            } catch (Exception e) {
                LOGGER.error("Error creating show proposal for request '{}': {}", showRequest.getDescription(), e.getMessage());
            }
        }



        Iterator<ShowRequest> iterator = showRequests.iterator();
        List<ShowRequest> showRequestList = new ArrayList<>();

        while (iterator.hasNext()) {
            showRequestList.add(iterator.next());
        }


        //COMPLETE SENT AND ACCEPTED PROPOSAL
        Long insuranceNumber = (long) (Math.random() * 1000000000L);

        ListShowProposalController listShowProposalController = new ListShowProposalController();

        Iterable<ShowProposal> showProposal = listShowProposalController.listAllShowProposals();

        List<ShowProposal> proposalList = new ArrayList<>();

        for(ShowProposal proposal : showProposal){
            proposalList.add(proposal);
        }

        ShowProposal proposal = proposalList.get(2);

        FindShowRequestController showRequestController = new FindShowRequestController();
        Optional<ShowRequest> showRequest1 = showRequestController.findShowRequestByProposal(proposal);
        ShowRequest showRequest;
        if(showRequest1.isPresent()){
            showRequest = showRequest1.get();
        }else{
            System.out.println("Show request is null");
            return false;
        }

        List<DroneModel> droneModels = listDroneModelController.listAllDroneModels();

        Map<DroneModel, Integer> droneModelMap = new HashMap<>();

        droneModelMap.put(droneModels.getFirst(),listDronesInventoryController.listAllDronesOfModel(droneModels.getFirst()).size());
        droneModelMap.put(droneModels.get(1),listDronesInventoryController.listAllDronesOfModel(droneModels.get(1)).size());

        addDroneToProposalController.updateDroneSelection(proposal.identity(), droneModelMap);

        addFigureToProposalController.setAllFiguresForProposal(proposal.identity(), showRequest.getFigure());

        proposal.updateStatus();
        System.out.println(proposal.getStatus());

        GenerateProgramsController generateProgramsController = new GenerateProgramsController();

        generateProgramsController.generatePrograms(proposal.identity());

        SimulationReport simulationReport = new SimulationReport(SimulationResult.SUCCESS);

        TestShowProposalController testShowProposalController = new TestShowProposalController();

        AddVideoToProposalController addVideoToProposalController = new AddVideoToProposalController();

        testShowProposalController.assignReport(proposal.identity(),simulationReport);

        addVideoToProposalController.addVideoToProposal(proposal,"https://www.youtube.com/watch?v=dQw4w9WgXcQ");

        proposal.updateStatus();

        System.out.println(proposal.getStatus());

        SendShowProposalController sendShowProposalController = new SendShowProposalController();

        sendShowProposalController.sendProposalToCustomer(proposal.identity());

        System.out.println(proposal.getDownLoadCode().value());

        GenerateProposalDocumentController generateProposalDocumentController = new GenerateProposalDocumentController();

        ProposalDocumentTemplateRepository proposalDocumentTemplateRepository = PersistenceContext.repositories().proposalDocumentTemplates();

        try {
            generateProposalDocumentController.generateProposalDocument(proposal.identity(),proposalDocumentTemplateRepository.obtainAllProposalDocumentTemplates().iterator().next().identity());
        } catch (IOException e) {
            System.out.println("Error generating proposal document");
        }






        //PROPOSAL TO SEND


        //PENDING PROPOSAL


        return true;
    }

    private ShowProposal createShowProposal(
            ShowRequest showRequest,
            String description,
            ProposalNDrones numberOfDrones,
            ProposedDuration duration,
            Insurance insurance,
            ProposedPlace proposedPlace,
            ProposedShowDate proposedShowDate
    ) {

        try {
            return controller.createShowProposal(
                    showRequest,
                    description,
                    numberOfDrones,
                    duration,
                    proposedPlace,
                    proposedShowDate,
                    insurance
            );
        } catch (Exception e) {
            LOGGER.error("Error creating show proposal: {}", e.getMessage());
        }


        return null;
    }
}

