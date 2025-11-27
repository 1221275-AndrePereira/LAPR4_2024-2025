package eapli.shodrone.showProposal.application.programManaging;

import eapli.framework.validations.Preconditions;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.*;
import eapli.shodrone.showProposal.domain.proposalDrone.DroneInstructions;
import eapli.shodrone.showProposal.domain.proposalDrone.DroneTypeSetting;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalFigure;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalStatus;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;

import java.util.*;

public class GenerateProgramsController {

    ShowProposalRepository repository = PersistenceContext.repositories().showProposals();


    public void generatePrograms(final long proposalId) {

        Optional<ShowProposal> proposal = repository.findByIdentifier(proposalId);
        ShowProposal showProposal;
        if(proposal.isPresent()){
            showProposal = proposal.get();
        }else{
            System.out.println("No porposal found with the given ID");
            return;
        }

        Preconditions.ensure(!showProposal.getDroneModels().isEmpty(),"Attempted generation of proposal with no Drone Models associated");
        Preconditions.ensure(!showProposal.getFigures().isEmpty(),"Attempted generation of proposal with no Figures associated");
        Preconditions.ensure(showProposal.getStatus().equals(ProposalStatus.TESTING),"Without proper initialization (Expecting initial status of 'TESTING')");

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Generating required drone programs");
        System.out.println("-------------------------------------------------------------------------------------");

        DroneProgramGenerator generator = new DroneProgramGenerator();

        //MAP OF LANGUAGE ID - AND RESPECTIVE PATH TO TRANSLATED PROGRAM
        Map<String, String> langFilePath;

        List<ProposalFigure> proposalFigures = showProposal.getFigures();

        Set<ProposalDroneModel> models = showProposal.getDroneModels();

        for (ProposalFigure proposalFigure : proposalFigures) {
            langFilePath = generator.makePaths(proposalFigure, models);

            for (ProposalDroneModel droneModel : models) {
                String languageName = droneModel.getModel().getLanguage().language();
                String instructionsPath = langFilePath.get(languageName);
                String dslPath = proposalFigure.getFigure().getDslCode().toString();

                if (generator.generateAndValidate(dslPath, instructionsPath, languageName)) {
                    DroneTypeSetting typeSetting = new DroneTypeSetting(droneModel, DroneInstructions.valueOf(instructionsPath));
                    proposalFigure.addTypeSetting(typeSetting);

                }
            }
        }


        showProposal.updateStatus();
        repository.save(showProposal);
    }
}
