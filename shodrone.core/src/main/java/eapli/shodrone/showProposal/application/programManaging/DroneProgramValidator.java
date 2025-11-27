package eapli.shodrone.showProposal.application.programManaging;

import eapli.shodrone.integrations.plugins.drone.language.droneone.ValidateDroneOneFileSyntax;
import eapli.shodrone.integrations.plugins.drone.language.dronetwo.ValidateDroneTwoFileSyntax;

public class DroneProgramValidator {

    public boolean validateProgram(String path, String language){

        if (language.equals("DroneOne")){
            return ValidateDroneOneFileSyntax.validate(path);
        }

        else if (language.equals("DroneTwo")) {
            return ValidateDroneTwoFileSyntax.validate(path);
        }

        return false;
    }

}
