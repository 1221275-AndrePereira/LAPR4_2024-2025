# US311: Add drone models to a show proposal - Requirements Engineering

## 1\. Requirements Engineering

### 1.1. User Story Description

* **ID:** US311
* **Title:** Add drone models to a show proposal
* **As a:** CRM Collaborator
* **I want to:** add one or more of the available drone models to a show proposal.
* **So that:** I can specify which drone models will be used in the show.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

* "For a show proposal, it is necessary to select from the existing drone models in the drone fleet, which models will be used in the show."
* "It is also necessary to indicate the number of drones of each model to be used."

**From the client clarifications:**

* It's clarified that all drones of all selected models for a show are used in every figure of that show. No drones can be left out.

### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to select an existing show proposal to add drone models to.
* **AC2:** The system must display a list of available drone models from the company's fleet.
* **AC3:** The user must be able to select one or more drone models from the list.
* **AC4:** For each selected drone model, the user must specify the number of drones to be used.
* **AC5:** The total number of drones specified across all models must be consistent with the total number of drones defined in the show proposal.
* **AC6:** Upon successful addition, the show proposal must be updated to include the selected drone models and their respective quantities.

### 1.4. Found out Dependencies

* **US230: Register a drone model:** Drone models must exist in the system before they can be added to a proposal.
* **US310: Create a show proposal:** A show proposal must exist to which the drone models can be added.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
  * Selection of a show proposal.
  * Selection of one or more drone models.
  * The number of drones for each selected model.
* From System:
  * List of available show proposals.
  * List of available drone models.

**Output Data:**

* To User (CRM Collaborator):
  * Confirmation message of successful addition.
* To System:
  * The `ShowProposal` aggregate is updated in the database with the new `ProposalDroneModel` entities.