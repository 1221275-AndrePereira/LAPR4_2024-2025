package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.validations.Preconditions;
import eapli.shodrone.figure.application.ListFigureController;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import eapli.shodrone.showProposal.application.AddFigureToProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalFields.ProposalFigure;
import eapli.shodrone.showrequest.application.FindShowRequestController;
import eapli.shodrone.showrequest.domain.ShowRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * UI for adding figures to a proposal.
 * Allows the user to add, remove, and order figures in a proposal.
 */
public class AddFiguresToProposalUI extends AbstractUI {

    // Controllers for managing figures and proposals
    ListFigureController listFigureController = new ListFigureController();
    AddFigureToProposalController addFigureToProposalController = new AddFigureToProposalController();
    FindShowRequestController findShowRequestController = new FindShowRequestController();

    private final ShowProposal showProposal;

    private final String pageBreak = ("------------------------------------------------------------------------------------------------------");

    /**
     * Constructor for AddFiguresToProposalUI.
     *
     * @param showProposal The proposal to which figures will be added.
     */
    public AddFiguresToProposalUI(ShowProposal showProposal) {
        this.showProposal = showProposal;
    }

    /**
     * Displays the UI for adding figures to a proposal.
     * Allows the user to add, remove, and order figures in the proposal.
     *
     * @return true if the operation was successful, false otherwise.
     */
    @Override
    protected boolean doShow() {
        // Retrieve the list of public and exclusive figures available for the user
        final List<Figure> publicFigures = listFigureController.listActivePublicFigures();

        ShodroneUser user = retrieveUser();

        // Get the initial list of figures in the proposal and convert them to a list of Figure objects
        List<ProposalFigure> initialProposalFigures = showProposal.getFigures();
        List<Figure> initialFigures = initialProposalFigures.stream().map(ProposalFigure::figure).toList();

        // get the list of exclusive figures for the user
        final List<Figure> exclusiveFigures = listFigureController.listExclusiveFigureOfUser(user);

        // Print the initial figures in the proposal
        printOrder(initialFigures, "| Current Figures Order :");
        System.out.println();

        List<Figure> currentSelection = new ArrayList<>();

        // Add the initial figures from the proposal to the current selection
        for(ProposalFigure proposalFigure : initialProposalFigures){
            // Ensure that the figure is not already in the current selection
            if(!currentSelection.contains(proposalFigure.figure())) {
                currentSelection.add(proposalFigure.figure());
            }
        }

        // Combine public and exclusive figures, excluding those already in the current selection
        List<Figure> availableFigures = new ArrayList<>();
        availableFigures.addAll(publicFigures);
        availableFigures.addAll(exclusiveFigures);
        // Remove figures that are already in the current selection from the available figures
        availableFigures.removeAll(currentSelection);

        boolean done = false;
        // Main loop for the UI, allowing the user to add, remove, and order figures
        while (!done) {
            printCurrentSelection(currentSelection,"| Present Figures in Proposal :");

            int choice = showMenu();

            switch (choice) {
                case 1:
                    doAddFigure(availableFigures, currentSelection);
                    break;
                case 2:
                    doRemoveFigure(availableFigures, currentSelection);
                    break;
                case 3:
                    if(!currentSelection.isEmpty()) {
                        orderFigures(currentSelection);
                    }else{
                        done = true;
                        break;
                    }
                case 0:
                    if(!currentSelection.isEmpty()) {
                        orderFigures(currentSelection);
                        addFigureToProposalController.setAllFiguresForProposal(showProposal.identity(), currentSelection);

                        printOrder(currentSelection, "| New Order Saved:");
                    }
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
        return true;
    }

    /**
     * Orders the figures in the current selection based on user input.
     * The user can specify the order of figures in a specific format.
     *
     * @param currentSelection The list of currently selected figures to be ordered.
     */
    private void orderFigures(List<Figure> currentSelection) {
        String order;
        String[] figures;
        List<Integer> orderedIndexes;

        printOrder(currentSelection,"| Figures to order :");
        System.out.println("0. Skip");
        // Prompt the user to enter the order of figures in a specific format
        while(true) {
            order = Console.readLine("Order figures in the desired positions (Ex. (F1,F2,F1,F3,F2,F1...) beware figures can't be in 2 consecutive positions)");

            // Check if the user wants to skip ordering
            if (order.equals("0")) {
                return;
            }

            // Validate the input format using a regex pattern
            try {
                Preconditions.matches(Pattern.compile("\\(F[0-9]+(,F[0-9]+)*\\)"), order, "");

                // Extract the figure indexes from the input string
                figures = order.replace("(","").replace(")","").trim().split(",");
                orderedIndexes = new ArrayList<>();
                // Convert the figure indexes to integers and add them to the orderedIndexes list
                for (String figure : figures) {
                    figure = figure.replace("F","").trim();
                    orderedIndexes.add(Integer.parseInt(figure));
                }

                // Check if 2 consecutive figures are the same
                for (int i = 1; i < orderedIndexes.size(); i++) {
                    if (orderedIndexes.get(i).equals(orderedIndexes.get(i - 1))) {
                        throw new IllegalArgumentException("Figures can't appear in two consecutive positions.");
                    }
                }

                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid order format: " + e.getMessage());
            }
        }

        List<Figure> orderedFigures = new ArrayList<>();

        // Reorder the current selection based on the specified order
        for(Integer index : orderedIndexes){
            orderedFigures.add(currentSelection.get(index-1));
        }

        // Clear the current selection and add the ordered figures
        currentSelection.clear();
        currentSelection.addAll(orderedFigures);

    }

    /**
     * Adds a figure to the current selection from the available figures.
     * The user selects a figure to add, which is then removed from the available figures.
     *
     * @param figures          The list of available figures to choose from.
     * @param currentSelection The list of currently selected figures to which the new figure will be added.
     */
    private void doAddFigure(List<Figure> figures, List<Figure> currentSelection) {
        Figure selected = selectFigure(figures);
        figures.remove(selected);
        currentSelection.add(selected);
    }

    /**
     * Removes a figure from the current selection and adds it back to the available figures.
     * The user selects a figure to remove, which is then added back to the available figures.
     *
     * @param figures          The list of available figures to which the removed figure will be added.
     * @param currentSelection The list of currently selected figures from which the figure will be removed.
     */
    private void doRemoveFigure(List<Figure> figures, List<Figure> currentSelection) {
        Figure selected = selectFigure(currentSelection);
        currentSelection.remove(selected);
        figures.add(selected);
    }

    /**
     * Prompts the user to select a figure from a list of figures.
     * Displays the available figures and allows the user to choose one.
     *
     * @param figures The list of figures to choose from.
     * @return The selected Figure object, or null if the user chooses to cancel.
     */
    private Figure selectFigure(List<Figure> figures) {
        int current = 1;
        int choice = -1;

        System.out.println(pageBreak);
        for(Figure figure : figures) {

            System.out.printf("%d - |Description: %-15s | Version: %-10s | Keywords: %s\n",
                    current++,
                    figure.getDescription(),
                    figure.getFigureVersion(),
                    figure.getKeywords());
        }
        System.out.println(pageBreak);

        while(choice < 0 || choice > current) {
            choice = Console.readInteger("Please select a Drone Model:");
            if(choice == 0){
                return null;
            }
        }

        return figures.get(choice - 1);

    }

    /**
     * Retrieves the user associated with the show proposal.
     * This method finds the show request related to the proposal and retrieves the customer (user) from it.
     *
     * @return The ShodroneUser associated with the show proposal, or null if not found.
     */
    private ShodroneUser retrieveUser() {
        ShowRequest showRequest = null;
        Optional<ShowRequest> request = findShowRequestController.findShowRequestByProposal(showProposal);
        if (request.isPresent()) {
            showRequest = request.get();
        }

        ShodroneUser user = null;
        if(showRequest != null) {
            user = showRequest.getCustomer();
        }else{
            System.out.println("Failed to retrieve belonging customer");
        }

        return user;
    }

    /**
     * Displays the menu options for adding figures to a proposal.
     * The user can choose to add a figure, remove a figure, or save the current order of figures.
     *
     * @return The user's choice as an integer.
     */
    private int showMenu() {
        System.out.println("\n--- Add Figure Menu ---");
        System.out.println("1. Add a Figure to the proposal");
        System.out.println("2. Remove a Figure from the proposal");

        System.out.println("0. Define Figures Order and Save");
        return Console.readInteger("Please select an option:");
    }

    /**
     * Prints the current selection of figures in the proposal.
     * Displays the description, version, and keywords of each figure.
     *
     * @param figures The list of figures currently selected in the proposal.
     * @param headline The headline to display above the list of figures.
     */
    private void printCurrentSelection(List<Figure> figures, String headline) {
        System.out.println(pageBreak);
        System.out.println(headline);
        System.out.println(pageBreak);
        if (figures.isEmpty()) {
            System.out.println("  (No Figures added yet)");
        } else {
            for (Figure figure : figures) {
                System.out.printf("|Description: %-15s | Version: %-10s | Keywords: %s\n", figure.getDescription(), figure.getFigureVersion(), figure.getKeywords());
            }
        }
        System.out.println(pageBreak);
    }

    /**
     * Returns the headline for the Add Figures to Proposal UI.
     * This method is used to display the title of the UI when it is shown.
     *
     * @return The headline string for the UI.
     */
    @Override
    public String headline() {
        return "Add Figures to Proposal";
    }

    /**
     * Prints the order of figures in the proposal.
     * Displays the description, version, and keywords of each figure in the current selection.
     *
     * @param currentSelection The list of currently selected figures to be printed.
     * @param headline The headline to display above the list of figures.
     */
    private void printOrder(List<Figure> currentSelection, String headline){
        int current = 1;

        System.out.println(pageBreak);
        System.out.println(headline);
        System.out.println(pageBreak);

        for(Figure figure : currentSelection) {
            String currentString = "F" + current++;

            System.out.printf("%s - |Description: %-15s | Version: %-10s | Keywords: %s\n",
                    currentString,
                    figure.getDescription(),
                    figure.getFigureVersion(),
                    figure.getKeywords());
        }
        System.out.println(pageBreak);

    }
}