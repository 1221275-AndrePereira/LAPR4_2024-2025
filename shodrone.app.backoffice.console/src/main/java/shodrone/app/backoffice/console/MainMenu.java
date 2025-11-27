package shodrone.app.backoffice.console;

import console.presentation.authz.MyUserMenu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;

import eapli.shodrone.usermanagement.domain.ShodroneRoles;

import shodrone.Application;
import shodrone.app.backoffice.console.presentation.Authz.*;
import shodrone.app.backoffice.console.presentation.drone.AddDroneUI;
import shodrone.app.backoffice.console.presentation.drone.ChangeDroneStatusUI;
import shodrone.app.backoffice.console.presentation.drone.ListDronesInventoryUI;
import shodrone.app.backoffice.console.presentation.drone.RemoveDroneUI;
import shodrone.app.backoffice.console.presentation.droneModel.AddDroneModelUI;
import shodrone.app.backoffice.console.presentation.droneModel.ListDroneModelUI;
import shodrone.app.backoffice.console.presentation.droneModel.RemoveDroneModelUI;
import shodrone.app.backoffice.console.presentation.figure.AddFigureUI;
import shodrone.app.backoffice.console.presentation.figure.ListActiveFiguresUI;
import shodrone.app.backoffice.console.presentation.figureCategory.AddFigureCategoryUI;
import shodrone.app.backoffice.console.presentation.figureCategory.AlterFigureCategoryStatusUI;
import shodrone.app.backoffice.console.presentation.figureCategory.EditFigureCategoryUI;
import shodrone.app.backoffice.console.presentation.figureCategory.ListFigureCategoriesUI;
import shodrone.app.backoffice.console.presentation.maintenanceType.AddMaintenanceTypeUI;
import shodrone.app.backoffice.console.presentation.maintenanceType.EditMaintenanceTypeUI;
import shodrone.app.backoffice.console.presentation.maintenanceType.ListMaintenanceTypesUI;
import shodrone.app.backoffice.console.presentation.proposalDocument.CreateProposalDocumentTemplateUI;
import shodrone.app.backoffice.console.presentation.proposalDocument.GenerateProposalDocumentUI;
import shodrone.app.backoffice.console.presentation.showProposal.*;
import shodrone.app.backoffice.console.presentation.showrequest.AddShowRequestUI;
import shodrone.app.backoffice.console.presentation.showrequest.EditShowRequestUI;
import shodrone.app.backoffice.console.presentation.showrequest.ListShowRequestUI;
import shodrone.app.backoffice.console.presentation.showrequest.RemoveShowRequestUI;

public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";
    private static final String RETURN_LABEL = "Return ";
    private static final int EXIT_OPTION = 0;

    // Authorization service to check user permissions
    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    @Override
    protected boolean doShow() {
        final Menu menu = buildMainMenu();
        final MenuRenderer renderer;

        if (Application.settings().isMenuLayoutHorizontal()) {
            renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        } else {
            renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        }
        return renderer.render();
    }

    private Menu buildMainMenu() {

        final Menu mainMenu = new Menu();
        int currentOption = 1;

        final Menu myUserMenu = new MyUserMenu();
        mainMenu.addSubMenu(currentOption++, myUserMenu);

        // If the user is an admin, show all options
        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
            final Menu usersMenu = buildUsersMenu(true);
            mainMenu.addSubMenu(currentOption++, usersMenu);
        }

        // If the user is a CRM manager, show only the add user option
        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.CRM_MANAGER) &&
                !authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
            final Menu usersMenu = buildUsersMenu(false);
            mainMenu.addSubMenu(currentOption++, usersMenu);
        }

        // Figure section
        if(authz.isAuthenticatedUserAuthorizedTo(
                ShodroneRoles.POWER_USER,
                ShodroneRoles.ADMIN,
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.SHOW_DESIGNER)) {
            final Menu figureMenu = buildFigureMenu();
            mainMenu.addSubMenu(currentOption++, figureMenu);
        }

        if(authz.isAuthenticatedUserAuthorizedTo(
                ShodroneRoles.ADMIN,
                ShodroneRoles.POWER_USER,
                ShodroneRoles.SHOW_DESIGNER)) {
            final Menu categoryMenu = buildFigureCategoryMenu();
            mainMenu.addSubMenu(currentOption++, categoryMenu);
        }

        if (authz.isAuthenticatedUserAuthorizedTo(
                ShodroneRoles.POWER_USER,
                ShodroneRoles.ADMIN,
                ShodroneRoles.CRM_MANAGER)) {
            final Menu requestMenu = buildShowRequestMenu();
            mainMenu.addSubMenu(currentOption++, requestMenu);
        }

        if (authz.isAuthenticatedUserAuthorizedTo(
                ShodroneRoles.POWER_USER,
                ShodroneRoles.ADMIN,
                ShodroneRoles.CRM_MANAGER,
                ShodroneRoles.CRM_COLLABORATOR
        )) {
            final Menu proposalMenu = buildShowProposalMenu();
            mainMenu.addSubMenu(currentOption++, proposalMenu);
        }

        if(authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER,ShodroneRoles.DRONE_TECH)){
            final Menu maintenanceTypeMenu = buildMaintenanceTypeMenu();
            mainMenu.addSubMenu(currentOption++, maintenanceTypeMenu);
        }

        // Drones Section
        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.DRONE_TECH)) {
            final Menu DroneModelMenu = buildDroneModelMenu();
            mainMenu.addSubMenu(currentOption++, DroneModelMenu);
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.DRONE_TECH)) {
            final Menu droneMenu = buildDroneMenu();
            mainMenu.addSubMenu(currentOption++, droneMenu);
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER)) {
            final Menu proposalDocumentTemplateMenu = buildProposalDocumentTemplateMenu();
            mainMenu.addSubMenu(currentOption++, proposalDocumentTemplateMenu);
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("User session terminated"));

        return mainMenu;
    }

    /**
     * Build the users menu.
     *
     * @param isAdmin true if the user is an admin, false otherwise
     * @return the users menu
     */
    private Menu buildUsersMenu(boolean isAdmin) {
        final Menu menu = new Menu("Users ");
        int currentOption = 1;

        // If the user is an admin, show all options
        if (isAdmin) {
            menu.addItem(currentOption++, "Add User", new AddUserAdminAction());
            menu.addItem(currentOption++, "List all Users", new ListUsersAction());
            menu.addItem(currentOption++, "Activate User", new ActiveUserAction());
            menu.addItem(currentOption++, "Deactivate User", new DeactivateUserAction());
        } else {
            // If the user is not an admin, show only the add user option, so he can add Only Shodorne Users
            // and not Admins or Power Users
            menu.addItem(currentOption++, "Add User", new AddUserManagerAction());
        }
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildDroneMenu() {
        final Menu menu = new Menu("Drone ");
        int currentOption = 1;

        AddDroneUI addDroneModelUI = new AddDroneUI();
        RemoveDroneUI removeDroneUI = new RemoveDroneUI();
        ListDronesInventoryUI listDronesInventoryUI = new ListDronesInventoryUI();
        ChangeDroneStatusUI changeDroneStatusUI = new ChangeDroneStatusUI();

        menu.addItem(currentOption++, "Add Drone To Inventory", addDroneModelUI::show);
        menu.addItem(currentOption++, "Remove Drone of Inventory", removeDroneUI::show);
        menu.addItem(currentOption++, "Change Drone Status", changeDroneStatusUI::show);
        menu.addItem(currentOption++, "List Drone's Inventory", listDronesInventoryUI::show);

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildFigureCategoryMenu() {
        final Menu menu = new Menu("Figure Category ");
        int currentOption = 1;

        menu.addItem(currentOption++, "Add a Figure Category", new AddFigureCategoryUI()::show);
        menu.addItem(currentOption++, "List Figure Category", new ListFigureCategoriesUI()::show);
        menu.addItem(currentOption++, "Edit Figure Category", new EditFigureCategoryUI()::show);
        menu.addItem(currentOption++, "Change Figure Category Status", new AlterFigureCategoryStatusUI()::show);

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildFigureMenu() {
        final Menu menu = new Menu("Figure ");
        int currentOption = 1;

        menu.addItem(currentOption++, "Register a Figure",new AddFigureUI()::show);
        menu.addItem(currentOption++, "List all figures", new ListActiveFiguresUI()::show);


        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildShowRequestMenu() {
        final Menu menu = new Menu("Show Request ");
        int currentOption = 1;

        final AddShowRequestUI addShowRequestUI = new AddShowRequestUI();
        final ListShowRequestUI listShowRequestUI = new ListShowRequestUI();
        final EditShowRequestUI editShowRequestUI = new EditShowRequestUI();
        final RemoveShowRequestUI removeShowRequestUI = new RemoveShowRequestUI();

        menu.addItem(currentOption++, addShowRequestUI.headline(), addShowRequestUI::show);
        menu.addItem(currentOption++, listShowRequestUI.headline(), listShowRequestUI::show);
        menu.addItem(currentOption++, editShowRequestUI.headline(), editShowRequestUI::show);
        menu.addItem(currentOption++, removeShowRequestUI.headline(), removeShowRequestUI::show);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildShowProposalMenu() {
        final Menu menu = new Menu("Show Proposal ");
        int currentOption = 1;

        final GenerateProposalDocumentUI generateProposalDocumentUI = new GenerateProposalDocumentUI();
        final AddShowProposalUI addShowProposalUI = new AddShowProposalUI() ;
        final EditProposalUI editProposalUI = new EditProposalUI();
        final SendShowProposalUI sendShowProposalUI = new SendShowProposalUI();
        final ListShowProposalUI listShowProposalUI = new ListShowProposalUI();
        final EditVideoOfProposalUI editVideoOfProposalUI = new EditVideoOfProposalUI();
        final FinalizeProposalUI finalizeProposalUI = new FinalizeProposalUI();

        if(authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(currentOption++, addShowProposalUI.headline(), addShowProposalUI::show);
            menu.addItem(currentOption++, editProposalUI.headline(), editProposalUI::show);
            menu.addItem(currentOption++, editVideoOfProposalUI.headline(), editVideoOfProposalUI::show);
            menu.addItem(currentOption++, sendShowProposalUI.headline(), sendShowProposalUI::show);
            menu.addItem(currentOption++, finalizeProposalUI.headline(), finalizeProposalUI::show);
        }
        menu.addItem(currentOption++, listShowProposalUI.headline(), listShowProposalUI::show);
        if(authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.ADMIN,ShodroneRoles.POWER_USER,ShodroneRoles.CRM_MANAGER)) {
            menu.addItem(currentOption++, generateProposalDocumentUI.headline(), generateProposalDocumentUI::show);
        }
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildMaintenanceTypeMenu(){
        final Menu menu=new Menu("Maintenance Type ");
        int currentOption=1;

        final AddMaintenanceTypeUI addMaintenanceTypeUI = new AddMaintenanceTypeUI();
        final ListMaintenanceTypesUI listMaintenanceTypesUI = new ListMaintenanceTypesUI();
        final EditMaintenanceTypeUI editMaintenanceTypeUI = new EditMaintenanceTypeUI();

        menu.addItem(currentOption++, "Add Maintenance Type", addMaintenanceTypeUI::show);
        menu.addItem(currentOption++, "Edit Maintenance Type", editMaintenanceTypeUI::show);
        menu.addItem(currentOption++, "List Maintenance Types", listMaintenanceTypesUI::show);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildDroneModelMenu(){
        final Menu menu = new Menu("Drone Model");
        int currentOption=1;

        final AddDroneModelUI addDroneModelUI = new AddDroneModelUI();
        final RemoveDroneModelUI removeDroneModelUI = new RemoveDroneModelUI();
        final ListDroneModelUI listDroneModelUI = new ListDroneModelUI();

        menu.addItem(currentOption++, "Create Drone Model", addDroneModelUI::show);
        menu.addItem(currentOption++, "Remove Drone Model", removeDroneModelUI::show);
        menu.addItem(currentOption++, "List Drone Models", listDroneModelUI::show);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);


        return menu;
    }

    private Menu buildProposalDocumentTemplateMenu(){
        final Menu menu = new Menu("Proposal Document Template");
        int currentOption=1;

        final CreateProposalDocumentTemplateUI createProposalDocumentTemplateUI = new CreateProposalDocumentTemplateUI();

        menu.addItem(currentOption++,"Create Proposal Document Template", createProposalDocumentTemplateUI::show);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    @Override
    public String headline() {
        return authz.session().map(s -> "Shodrone [ @" + s.authenticatedUser().identity() + " ]")
                .orElse("Shodrone [ ==Anonymous== ]");
    }
}
