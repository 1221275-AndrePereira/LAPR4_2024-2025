# US312: Add figures to a proposal - Tests and Implementation

# US345: Generate Drone Programs for a Show Proposal - Tests and Implementation

## 1. Implementation Details

The implementation is divided across three main layers: Presentation (UI), Application (Controller), and Domain.

### 1.1. Presentation Layer

The `GenerateProgramsUI` class is responsible for initiating the process. It takes a `ShowProposal` object and calls the controller to perform the main logic.

```java
// shodrone.app.backoffice.console/src/main/java/shodrone/app/backoffice/console/presentation/showProposal/GenerateProgramsUI.java

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
        return "Generate Drone Programs";
    }
}

```
