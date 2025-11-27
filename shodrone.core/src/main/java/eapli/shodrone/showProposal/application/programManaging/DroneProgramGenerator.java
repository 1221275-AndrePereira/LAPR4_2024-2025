package eapli.shodrone.showProposal.application.programManaging;

import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.integrations.plugins.dsl.translators.DSLTranslator;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalFigure;

import java.util.*;

public class DroneProgramGenerator {

    private final static String droneProgramsDirectory = "genOutputs/dronePrograms/";

    public Map<String, String> makePaths(ProposalFigure proposalFigure, Set<ProposalDroneModel> models) {

        Figure figure = proposalFigure.getFigure();

        //MAP OF LANGUAGE ID - AND RESPECTIVE PATH TO TRANSLATED PROGRAM
        Map<String, String> langFile = new HashMap<>();

        for (ProposalDroneModel model : models) {
            String langName = model.getModel().getLanguage().language();
            String path = droneProgramsDirectory + figure.getDescription() + "_" + figure.getFigureVersion() + "_" + langName + ".txt";
            langFile.put(langName, path);
        }

        return langFile;

    }

    public boolean generateAndValidate(String inputPath, String outputPath, String language) {
        DSLTranslator.translate(inputPath, outputPath, language);
        return new DroneProgramValidator().validateProgram(outputPath, language);
    }


}
