package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.presentation.console.AbstractUI;
import eapli.shodrone.showProposal.application.programManaging.GenerateProgramsController;
import eapli.shodrone.showProposal.domain.ShowProposal;

public class GenerateProgramsUI extends AbstractUI {

    GenerateProgramsController controller = new GenerateProgramsController();
    ShowProposal showProposal;

    public  GenerateProgramsUI(ShowProposal showProposal) {
        this.showProposal = showProposal;
    }

    @Override
    protected boolean doShow() {
        controller.generatePrograms(showProposal.identity());

        return false;
    }

    @Override
    public String headline() {
        return "";
    }
}
