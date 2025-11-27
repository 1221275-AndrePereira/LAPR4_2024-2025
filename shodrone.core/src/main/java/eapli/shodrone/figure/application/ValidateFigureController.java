package eapli.shodrone.figure.application;

import eapli.shodrone.integrations.plugins.dsl.FigureValidator;

import eapli.framework.application.UseCaseController;

@UseCaseController
public class ValidateFigureController {

    /**
     * Validates a figure's DSL code.
     *
     * @param dslFilePath the path to the DSL file to be validated
     * @return true if the validation is successful, false otherwise
     */
    public boolean validateFigure(String dslFilePath) {
        try {
            return FigureValidator.validator(dslFilePath);
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        }
    }
}
