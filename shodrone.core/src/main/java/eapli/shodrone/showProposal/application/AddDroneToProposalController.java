package eapli.shodrone.showProposal.application;

import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddDroneToProposalController {

    private final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();
    DroneInventoryRepository droneRepository = PersistenceContext.repositories().droneInventory();


    /**
     * Adds a DroneModel to a ShowProposal with its respective amount
     *
     * @param showProposalId
     * @param newSelection the selected DroneModels with their respective amount
     *
     */
    public void updateDroneSelection(final Long showProposalId, Map<DroneModel, Integer> newSelection) {

        ShowProposal proposal = showProposalRepository.findByIdentifier(showProposalId)
                .orElseThrow(() -> new IllegalArgumentException("ShowProposal with ID " + showProposalId + " not found."));

        Set<ProposalDroneModel> set = new HashSet<>();

        for(Map.Entry<DroneModel, Integer> entry : newSelection.entrySet()){
            ProposalDroneModel droneModel = new ProposalDroneModel(entry.getKey(), ProposalNDrones.valueOf(entry.getValue()));
            set.add(droneModel);
        }

        proposal.setDroneModels(set);
        proposal.updateStatus();
        showProposalRepository.save(proposal);

    }

}
