package eapli.shodrone.showProposal.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.application.UseCaseController;

import eapli.shodrone.showProposal.domain.simulationReport.SimulationReport;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.usermanagement.domain.ShodroneRoles;
import eapli.shodrone.showProposal.domain.ShowProposal;

import utils.FileLoader;

@UseCaseController
public class TestShowProposalController {

    private final FileLoader fl;
    private final AuthorizationService authz;
    private final ShowProposalRepository showProposalRepository;

    public TestShowProposalController(){
        fl = new FileLoader();
        authz = AuthzRegistry.authorizationService();
        showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public TestShowProposalController(FileLoader fl, final AuthorizationService authz, final ShowProposalRepository showProposalRepository) {
        this.fl = fl;
        this.authz = authz;
        this.showProposalRepository = showProposalRepository;
    }

    public String message (ShowProposal proposal) {

        int numberOfDrones = proposal.getProposalNDrones().number();

        return fl.loadFiles(numberOfDrones);
    }

    public boolean assignReport(final long proposalId, SimulationReport report) {

        ShowProposal proposal = showProposalRepository.findByIdentifier(proposalId).orElse(null);

        if (proposal == null) {
            throw new IllegalArgumentException("ShowProposal not found with this id");
        }

        // Debug - Check if the ShowProposal or Report has invalid data
        if (report == null) {
            throw new IllegalArgumentException("SimulationReport cannot be null");
        }

        // Ensure report is assigned
        proposal.assignReport(report);

        // Persist and return success or failure
        showProposalRepository.save(proposal);

        return true;
    }


}
