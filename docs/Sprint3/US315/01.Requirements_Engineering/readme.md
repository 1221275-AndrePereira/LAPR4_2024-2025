US315: Add Video of Simulation to the Proposal - Requirements Engineering
=====================================================================================

1\. Requirements Engineering
----------------------------

### 1.1. User Story Description

* **ID:** US315
* **Title:** Add video of simulation to the proposal
* **As a:** CRM Collaborator
* **I want to:** add a video of the simulated show
* **So that:** the customer can have a preview of the show. [cite: 1]

### 1.2. Customer Specifications and Clarifications

**From the specifications document (Sem4PI\_Project\_Requirements\_v03a.pdf):**

* "As CRM Collaborator, I want to add a video of the simulated show so the customer can have a preview of the show." (Sec 4.3, US315) [cite: 1]
* A related user story, US316 ("Send show proposal to the customer"), mentions: "The proposal to be sent is a properly formatted document with the show details and a link to the video preview." (Sec 4.3) [cite: 1] This implies the video added in US315 will be referenced as a link.
* It is also noted for US316, which is relevant here: "In the scope of LAPR4, the team does not need to actually generate the video and can use any suitable video file." (Sec 4.3) [cite: 1] This suggests that for the purpose of this user story, the focus is on associating an existing video (file path or link) rather than on the system generating the simulation video itself.
* This user story assumes that a show proposal (created via US310) already exists and is in a state where details can be added or modified.
* The actual simulation process that would generate such a video is covered by other user stories (e.g., US261-US266 for figure simulation, US361-US366 for hybrid simulation, US376 for show testing). This US deals with attaching the outcome of such a simulation (the video) to the proposal.

**From the client clarifications:**

* The CRM Collaborator should be able to select an existing show proposal that is in an editable state (e.g., 'Draft', 'Under Review').
* Video in the Show Proposal is considered as link to the video file or a URL to an externally hosted video.

### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to select an existing show proposal that is in an editable state (e.g., 'Draft', 'Under Review').
* **AC2:** The system must provide a mechanism for the CRM Collaborator to specify the video to be associated with the proposal (e.g., by uploading a video file or providing a URL to an externally hosted video).
* **AC3:** The system must store the reference to the video (e.g., file path or URL) and associate it with the selected show proposal.
* **AC4:** If a video reference is already associated with the show proposal, the system should allow the CRM Collaborator to replace it with a new one.
* **AC5:** The system must confirm to the CRM Collaborator that the video has been successfully added/updated for the show proposal.
* **AC6:** The system should handle cases where an invalid file path or URL is provided (e.g., display an error message).

### 1.4. Found out Dependencies

* **Show Proposal Existence and State (US310):** A show proposal must exist in the system, and it should be in a state that allows for modifications like adding a video link.
* **User Authentication (US210):** The CRM Collaborator performing the action must be authenticated and have the necessary permissions.
* **Video Availability:** A "suitable video file" or a link to a video must be available to the CRM Collaborator to add to the proposal. The creation of this video is outside the scope of this US. [cite: 1]
* **(Logical) Simulation Completion:** While not a direct system dependency for *this* US, logically, a simulation (covered by USs like US261-US266, US361-US366, US376) would precede this step to generate the video content. This US is concerned only with the act of attaching the video to the proposal.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
  * Identifier of the target Show Proposal (e.g., selected from a list).
  * Video file to be uploaded OR a URL (string) pointing to the video.
* From System:
  * Identity of the authenticated CRM Collaborator (for logging/auditing purposes, if applicable).

**Output Data:**

* System Confirmation (e.g., UI message):
  * Message indicating successful addition/update of the video link to the show proposal.
  * Error message if the operation fails (e.g., proposal not found, invalid video link/file).
* Stored Data:
  * The selected Show Proposal entity in the database updated to include/store the path or URL of the associated video.

### 1.6. Other Relevant Remarks

* This user story focuses on enhancing a show proposal by attaching a visual preview, specifically a video of the simulated show.
* The primary goal is to improve the customer's understanding and experience of the proposed show before they make a decision.
* For the LAPR4 course context, the system does not need to generate the simulation video itself; using any "suitable video file" is acceptable. [cite: 1] This simplifies the scope to managing a link or file path.
* This functionality typically precedes sending the proposal to the customer (US316).