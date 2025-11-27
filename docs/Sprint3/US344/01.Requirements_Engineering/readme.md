# US345: Generate Drone Programs for a Show Proposal - Requirements Engineering

## 1\. Requirements Engineering

### 1.1. User Story Description

* **ID:** US345
* **Title:** Generate Drone Programs for a Show Proposal
* **As a:** CRM Collaborator
* **I want to:** generate the drone-specific program files for all figures within a show proposal.
* **So that:** I can prepare the proposal for simulation and testing, ensuring all figures can be executed by the specified drone models.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (`Sem4PI_Project_Requirements_v03c.pdf`):**

* "The support for this functionality must follow specific technical requirements provided in LPROG. The ANTLR tool should be used."
* "Shodrone wants the simulation system to use a multithreaded parent process and child drone processes..." This implies that programs must be generated in a format that the simulation engine can understand and execute.

**From the application code (`GenerateProgramsController.java`):**

* A show proposal must have at least one drone model associated with it before program generation can occur.
* A show proposal must have at least one figure associated with it.
* The show proposal must be in the **'TESTING'** status before generation can be initiated.
* After successful generation, the proposal's status is automatically updated.

### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to select a show proposal and trigger the generation of its drone programs.
* **AC2:** The generation process can only be started if the proposal's status is 'TESTING'.
* **AC3:** The system must verify that the proposal contains at least one figure and at least one drone model.
* **AC4:** For every figure in the proposal, the system must generate a corresponding, translated program for each drone model assigned to the proposal.
* **AC5:** After all programs are generated successfully, the system must update the show proposal's status to the next stage (e.g., 'READY_FOR_SIMULATION').
* **AC6:** The system must store or link the generated drone instruction files within the proposal's data structure for future use.

### 1.4. Found out Dependencies

* **US310: Create a show proposal:** A show proposal must exist.
* **US311: Add drone models to a proposal:** Drone models must have been defined and added to the proposal.
* **US312/US344: Add figures to a proposal:** Figures, which contain the high-level DSL scripts, must be added to the proposal.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
    * Selection of an existing show proposal.
* From System:
    * The identity of the selected `ShowProposal`.

**Output Data:**

* To User (CRM Collaborator):
    * A message indicating that the drone programs are being generated.
    * Confirmation of successful generation or an error message if preconditions are not met.
* To System:
    * The `ShowProposal` entity is updated in the database with the new instruction paths and a new status.

### 1.6. Other Relevant Remarks

* The core of this functionality lies in the `DroneProgramGenerator` which is responsible for translating the DSL of a figure into the specific programming language of a drone model. This process leverages ANTLR for parsing and translation as specified in the project requirements.