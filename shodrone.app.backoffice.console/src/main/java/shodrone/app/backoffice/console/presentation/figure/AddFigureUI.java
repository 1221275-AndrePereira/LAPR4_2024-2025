package shodrone.app.backoffice.console.presentation.figure;


import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.io.util.Console;

import eapli.shodrone.shodroneusermanagement.application.ListShodroneUsersController;
import eapli.shodrone.figurecategory.application.ListFigureCategoriesController;
import eapli.shodrone.figure.application.ValidateFigureController;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.figure.application.AddFigureController;
import eapli.shodrone.figurecategory.domain.FigureCategory;

import shodrone.Application;

import utils.FileSelector;
import utils.InputValidator;
import utils.ListPrompt;


import java.util.stream.StreamSupport;
import java.io.IOException;
import java.io.File;
import java.util.*;

import static utils.YNQuestionPrompt.continueQuestion;

public class AddFigureUI extends AbstractUI {

    AddFigureController addFigureController = new AddFigureController();
    ListFigureCategoriesController listFigureCategoriesController = new ListFigureCategoriesController();
    ListShodroneUsersController listShodroneUsersController = new ListShodroneUsersController();
    ValidateFigureController validateFigureController = new ValidateFigureController();

    @Override
    protected boolean doShow() {
        Set<String> keywords = new HashSet<>();

        File dslDirectory = new File(Application.settings().dslScriptsPath());

        if (!dslDirectory.exists() || !dslDirectory.isDirectory()) {
            System.err.println("Error: DSL directory does not exist or is not a directory: " + dslDirectory.getAbsolutePath());
        }

        final String description = Console.readNonEmptyLine("Figure Description",
                "Enter Figure Description: ");



        int rep = 1;
        boolean a = true;
        while (a && (rep <= 5)){
            final String keyword = InputValidator.readValidInput("Enter a Figure Keyword: ", InputValidator::isValidKeyword, "Invalid Keyword");

            keywords.add(keyword);

            a = Console.readBoolean("Do you wish to add any more keywords?(Current amount: "+ rep +" ) (y/n)");

            rep++;
        }

        final String figureVersion = InputValidator.readValidInput("Figure Version: ", InputValidator::isValidVersion, "Invalid Version format. Please try again.");

        if(addFigureController.alreadyExist(description,figureVersion)){
            System.out.println(("A Figure with the same description ('" + description +
                    "') and version ('" + figureVersion + "') already exists."));
            return true;
        }

        FileSelector fileSelector = new FileSelector();
        String dslPath = fileSelector.collectDslFilePathsByFilename();

        boolean isValid = validateFigureController.validateFigure(dslPath);

        if(isValid) {

            List<FigureCategory> categories = listFigureCategoriesController.listFigureCategories();

            ListPrompt<FigureCategory> listPrompt = new ListPrompt<>("Categories", categories,
                    FigureCategory::toString);

            if (categories.isEmpty()) {
                System.out.println("There are no Categories registered.");
                return true;
            }

            FigureCategory selectedCategory = listPrompt.selectItemRequired();

            ShodroneUser exclusiveUser = null;
            if (Console.readBoolean("Is this figure exclusive to someone? (y/n)")) {

                while (true) {
                    String exclusive = InputValidator.readValidInput("Write the customers username", InputValidator::isValidUsernameRegex, "Invalid Username");
                    final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();

                    final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
                            .toList();
                    for (ShodroneUser shodroneUser : shodroneUserList) {
                        if (shodroneUser.systemUser().username().toString().equals(exclusive)) {
                            exclusiveUser = shodroneUser;
                            break;
                        }

                    }
                    if (exclusiveUser == null) {
                        System.out.println("Error: Username " + exclusive + " does not exist");
                        String leave = Console.readLine("Type 'exit' to leave");

                        if (leave.equalsIgnoreCase("exit")) {
                            break;
                        }
                    } else {
                        break;
                    }

                }
            }

            try {
                addFigureController.addFigure(description, keywords, figureVersion, selectedCategory, dslPath, exclusiveUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Console.readLine("Enter to continue...");
            return false;
        }

        return true;
    }



    @Override
    public String headline() {
        return "Add Figure";
    }
}
