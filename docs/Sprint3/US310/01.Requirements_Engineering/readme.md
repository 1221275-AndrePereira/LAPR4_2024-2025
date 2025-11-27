US310: Create Show Proposal - Requirements Engineering
=====================================================================================

1\. Requirements Engineering
----------------------------

### 1.1. User Story Description

* **ID:** US310 [cite: 231]
* **Title:** Create Show Proposal [cite: 231]
* **As a:** CRM Collaborator [cite: 231]
* **I want to:** start the process for creating a show proposal [cite: 231]
* **So that:** we can reply to the customer [cite: 231]

### 1.2. Customer Specifications and Clarifications

**From the specifications document (Sem4PI\_Project\_Requirements\_v03a.pdf):**

* "As CRM Collaborator I want to start the process for creating a show proposal so that we can reply to the customer." (Sec 4.3, US310) [cite: 231]
* "The show proposal includes the total number of drones to be used in the show." (Sec 4.3, US310) [cite: 231]
  * _Implementation Note:_ This implies that when initiating the proposal, the total number of drones is a key piece of information to be captured or planned for.
* "Currently, all figures in a show must use all drones." (Sec 4.3, US310) [cite: 231]
  * _Implementation Note:_ This is a constraint affecting the composition of the show proposal, likely detailed further in US311 and US312, but important context for US310.
* "The show proposal must follow a predefined template." (Sec 4.3, US310) [cite: 231]
  * _Implementation Note:_ The system should allow association with a template when the proposal is initiated, even if template management is US318/US255. [cite: 246, 201]
* Workflow context from "Request and Proposal workflow" (Figure 3)[cite: 139]:
  * The "Create Proposal" activity follows either the completion of "Create Figure" (if new figures were requested) or directly after "Register Request". [cite: 139] US310 represents this "Create Proposal" step. [cite: 139, 231]
  * Subsequent steps include "Fill proposal data," "Add figures to proposal," "Generate proposal," and "Send to costumer." [cite: 139]
* Related User Story: "The CRM Collaborator is then able to generate a show proposal with the figures the customer desires." (Sec 3.1.4) [cite: 135] US310 initiates this. [cite: 231]
* Related User Story: "If the customer accepts the proposal, the CRM Collaborator updates the status of the request and the proposal, and it goes into production." (Sec 3.1.4) [cite: 137]
* Related User Story for template validation and document generation: US347 "As CRM Manager, I want the system to validate the proposal template and to generate the proposal document for the show proposal." (Sec 4.3) [cite: 266]

**From the client clarifications:**

* _(No specific client clarifications were provided directly for US310 beyond the main document. Requirements are derived from the specification document and related user stories.)_

### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to initiate the creation of a new show proposal for an existing and valid Show Request (created via US230). [cite: 170, 231]
* **AC2:** When initiating the proposal, the system must capture or allow for the specification of the total number of drones to be used in the show, as this is a fundamental part of the proposal. [cite: 231]
* **AC3:** The system must associate the newly created show proposal with a predefined proposal template. [cite: 231] The selection mechanism or default assignment of the template should be clear.
* **AC4:** Upon successful initiation of a show proposal, the system should ideally update the status of the associated Show Request (e.g., from 'Awaiting Proposal' to 'Proposal in Progress').
* **AC5:** The system must automatically record the creation date and time for the show proposal.
* **AC6:** The system must record the CRM Collaborator who initiated the show proposal as its author or creator.

### 1.4. Found out Dependencies

* **Show Request Existence (US230):** A Show Request must exist in the system, for which this proposal is being created. [cite: 170] The request should likely be in a state that permits proposal creation (e.g., 'Approved', 'Figures Finalized' if applicable).
* **User Authentication (US210):** The CRM Collaborator must be authenticated and authorized to access this functionality. [cite: 156]
* **Proposal Template Definition (US318, US255):** At least one proposal template must be defined in the system to be associated with the proposal, as per the requirement that "The show proposal must follow a predefined template." [cite: 231, 246, 201]
* **(Potentially) Figure Creation (US233):** If the related show request involved the creation of new figures, these should ideally be completed and available before a comprehensive proposal is made, as suggested by the workflow in Figure 3. [cite: 139, 174] However, US310 focuses on *starting* the proposal object. [cite: 231]

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
  * Identifier/Reference to the existing Show Request.
  * The total number of drones planned for the show. [cite: 231]
  * Selection or identifier of the proposal template to be used. [cite: 231]
* From System:
  * Identity of the currently logged-in CRM Collaborator (to be set as author).
  * Current system date and time (for creation timestamp).

**Output Data:**

* System Confirmation (e.g., UI message):
  * Success message indicating the show proposal has been successfully initiated, possibly with a proposal ID.
  * Error message if the initiation fails (e.g., invalid Show Request ID, no templates available).
* Stored Data (New or Updated Entities):
  * A new **Show Proposal** entity with attributes such as:
    * Unique Proposal ID (system-generated).
    * Link/Reference to the parent Show Request.
    * Author/Creator (the CRM Collaborator).
    * Creation Timestamp.
    * Initial Status (e.g., 'Draft', 'New', 'Being Prepared').
    * Specified total number of drones. [cite: 231]
    * Associated Proposal Template ID. [cite: 231]
  * (Potentially) Update to the **Show Request** entity's status.

### 1.6. Other Relevant Remarks

* This user story is a foundational step for assembling the full show proposal. It creates the main proposal object/record. [cite: 231]
* Subsequent user stories like US311 (Add drones to a proposal)[cite: 231], US312 (Add figures to a proposal)[cite: 231], and US315 (Add video of simulation to the proposal) [cite: 231] will populate this proposal with details.
* The actual generation of a formatted document for the customer is handled by other user stories (e.g., US316, US347). [cite: 231, 266]
* The constraint "Currently, all figures in a show must use all drones" will heavily influence the data added in US311 and US312. [cite: 231]
* The workflow depicted in Figure 3 ("Request and Proposal workflow") should guide the states and transitions related to the proposal. [cite: 139]