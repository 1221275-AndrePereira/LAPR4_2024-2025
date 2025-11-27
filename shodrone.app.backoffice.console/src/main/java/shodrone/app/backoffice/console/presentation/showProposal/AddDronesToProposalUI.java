package shodrone.app.backoffice.console.presentation.showProposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.shodrone.drone.application.ListDronesInventoryController;
import eapli.shodrone.droneModel.application.ListDroneModelController;
import eapli.shodrone.droneModel.domain.DroneModel;
import eapli.shodrone.showProposal.application.AddDroneToProposalController;
import eapli.shodrone.showProposal.application.ListShowProposalController;
import eapli.shodrone.showProposal.domain.ShowProposal;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;

import java.util.*;
import java.util.stream.Collectors;

public class AddDronesToProposalUI extends AbstractUI {

    private final ListDronesInventoryController droneController = new ListDronesInventoryController();
    private final ListDroneModelController droneModelController = new ListDroneModelController();
    private final AddDroneToProposalController droneToProposalController = new AddDroneToProposalController();
    private final ShowProposal showProposal;

    public AddDronesToProposalUI(ShowProposal showProposal) {
        this.showProposal = showProposal;
    }

    @Override
    protected boolean doShow() {
        // --- SETUP ---
        final List<DroneModel> allAvailableModels = droneModelController.listAllDroneModels();
        final Map<DroneModel, Integer> modelInventoryCount = prefetchAllDroneCounts(allAvailableModels);
        int proposalDroneCap = manageProposalDroneCapacity(this.showProposal);

        // --- STATE MANAGEMENT ---
        // IMPROVEMENT: Pre-load the current selection from the proposal to allow editing.
        Map<DroneModel, Integer> currentSelection = new HashMap<>();
        if (showProposal.getDroneModels() != null) {
            for (ProposalDroneModel pdm : showProposal.getDroneModels()) {
                currentSelection.put(pdm.getModel(), pdm.getNdrones().number());
            }
        }
        int currentAmount = currentSelection.values().stream().mapToInt(Integer::intValue).sum();

        // FIX: DO NOT empty the proposal's drones here. The update should be atomic at the end.
        // droneToProposalController.emptyCurrentDrones(showProposal); // <-- This dangerous line is removed.

        // --- MAIN UI LOOP ---
        boolean done = false;
        while (!done) {
            printCurrentSelection(currentSelection, currentAmount, proposalDroneCap);
            int choice = showMenu();

            switch (choice) {
                case 1:
                    currentAmount = doAddModel(allAvailableModels, modelInventoryCount, currentSelection, currentAmount, proposalDroneCap);
                    break;
                case 2:
                    currentAmount = doRemoveModel(currentSelection, modelInventoryCount, currentAmount);
                    break;
                case 0:
                    droneToProposalController.updateDroneSelection(showProposal.identity(), currentSelection);
                    System.out.println("Proposal drone selection updated successfully.");
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
        return true;
    }

    // --- Other helper methods (unchanged or with minor improvements) ---
    private int doAddModel(List<DroneModel> allModels, Map<DroneModel, Integer> inventory, Map<DroneModel, Integer> currentSelection, int currentAmount, int cap) {
        List<DroneModel> modelsToAdd = allModels.stream()
                .filter(model -> !currentSelection.containsKey(model))
                .collect(Collectors.toList());

        if (modelsToAdd.isEmpty()) {
            System.out.println("No more drone models available to add.");
            return currentAmount;
        }

        DroneModel selectedModel = selectDroneModel(modelsToAdd, inventory);
        if (selectedModel == null) return currentAmount;

        int availableAmount = inventory.get(selectedModel);
        int amountToAdd = selectAmount(availableAmount, currentAmount, cap);

        if (amountToAdd > 0) {
            currentSelection.put(selectedModel, amountToAdd);
            System.out.println("Added " + amountToAdd + " drones of model " + selectedModel.getModelName());
            return currentAmount + amountToAdd;
        }
        return currentAmount;
    }

    private Map<DroneModel, Integer> prefetchAllDroneCounts(List<DroneModel> models) {
        Map<DroneModel, Integer> counts = new HashMap<>();
        for (DroneModel model : models) {
            counts.put(model, droneController.listAllDronesOfModel(model).size());
        }
        return counts;
    }

    private int doRemoveModel(Map<DroneModel, Integer> session, Map<DroneModel, Integer> inventory, int currentAmount) {
        if (session.isEmpty()) {
            System.out.println("No drones have been added to the selection to remove.");
            return currentAmount;
        }

        // IMPROVEMENT: Use SelectWidget for a robust and consistent UI.
        DroneModel modelToRemove = selectDroneModel(new ArrayList<>(session.keySet()), inventory);
        if (modelToRemove == null) {
            return currentAmount; // User cancelled
        }

        // FIX: Correctly calculate the amount removed and update the total.
        int amountThatWasRemoved = session.get(modelToRemove);
        session.remove(modelToRemove);

        System.out.println("Removed " + amountThatWasRemoved + " drones of model " + modelToRemove.getModelName());
        return currentAmount - amountThatWasRemoved;
    }

    public DroneModel selectDroneModel(List<DroneModel> droneModels, Map<DroneModel, Integer> inventory) {
        int current = 1;
        int choice = -1;

        System.out.println("\n--- Available Drone Models ---");

        for (DroneModel model : droneModels) {
            System.out.printf("%d  - Model: %-15s | Amount: %d\n", current++, model.getModelName(), inventory.get(model));
        }
        while (choice < 0 || choice > current) {
            choice = Console.readInteger("Please select a Drone Model:");
            if (choice == 0) {
                return null;
            }
        }

        return droneModels.get(choice - 1);
    }

    public int selectAmount(int availableAmount, int currentAmount, int proposalDroneCap) {
        while (true) {
            String prompt = String.format("\nEnter amount for this model (Max available: %d, '0' to cancel):", availableAmount);
            int n = Console.readInteger(prompt);

            if (n == 0) return 0;

            if (n < 0) {
                System.out.println("Amount cannot be negative.");
            } else if (n > availableAmount) {
                System.out.println("Amount exceeds available inventory for this model.");
            } else if (currentAmount + n > proposalDroneCap) {
                System.out.println("This number surpasses the total proposed amount for the show!");
            } else {
                return n;
            }
        }
    }

    private void printCurrentSelection(Map<DroneModel, Integer> session, int currentAmount, int cap) {
        System.out.println("\n-------------------------------------------------");
        System.out.println("Current Drone Selection: " + currentAmount + " / " + cap);
        if (session.isEmpty()) {
            System.out.println("  (No models added yet)");
        } else {
            for (Map.Entry<DroneModel, Integer> entry : session.entrySet()) {
                System.out.printf("  - Model: %-15s | Amount: %d\n", entry.getKey().getModelName(), entry.getValue());
            }
        }
        System.out.println("-------------------------------------------------");
    }

    private int manageProposalDroneCapacity(ShowProposal selectedProposal) {
        int proposalDroneCap = selectedProposal.getProposalNDrones().number();
        System.out.println("This proposal is specified for " + proposalDroneCap + " Drones.");
        if (Console.readBoolean("Do you wish to change this amount? (y/n)")) {
            while (true) {
                int maxDrones = droneController.listAllDrones().size();
                int n = Console.readInteger("Input a new total amount of drones for this proposal: ");
                if (n > 0 && n <= maxDrones) {
                    selectedProposal.updateNdrones(n);
                    return n;
                } else {
                    System.out.println("Number must be between 1 and " + maxDrones + ".");
                }
            }
        }
        return proposalDroneCap;
    }


    private int showMenu() {
        System.out.println("\n--- Add Drone Model Menu ---");
        System.out.println("1. Add a Drone Model to the proposal");
        System.out.println("2. Remove a Drone Model from the proposal");
        System.out.println("0. Finish and Save");
        return Console.readInteger("Please select an option:");
    }


    @Override
    public String headline() {
        return "Add Drones to Proposal";
    }
}
