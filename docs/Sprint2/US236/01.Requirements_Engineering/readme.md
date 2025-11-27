# US236: Edit Show Requests — Requirements Engineering

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US236
* **Title:** Edit show requests
* **As a:** CRM Collaborator
* **I want to:** Edit a show request of a client.
* **So that:** I can update the details of a request before a proposal is generated.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

> US 236 Edit show requests
> As CRM Collaborator, I want to edit a show requests of a client.
> Only show requests without a proposal can be edited.

**From the client clarifications:**

* *(No specific client clarifications were provided for this US. Requirements are derived from the specification document).*

### 1.3. Acceptance Criteria

* **AC1:** The system must allow the user (CRM Collaborator) to select a specific, existing show request to edit (likely identified via its ID, potentially after listing them with US235).
* **AC2:** The system must verify the status of the selected show request before allowing edits.
* **AC3:** Editing must be prohibited if the show request's status indicates a proposal has already been generated (e.g., status is not 'PENDING' or a similar pre-proposal state). An appropriate message should be displayed if editing is disallowed.
* **AC4:** If editing is allowed, the system must allow the CRM Collaborator to modify editable fields of the show request. *(Assumption: Editable fields include Place, Tentative Date/Time, Tentative Duration, Tentative Drone Count, Description, and potentially the associated Figure IDs. Customer ID and Author should not be editable).*
* **AC5:** The system must persist the changes made to the editable fields of the show request upon confirmation by the user.
* **AC6:** The system should provide feedback indicating whether the update was successful or failed.

### 1.4. Found out Dependencies

* **Show Request Existence:** The Show Request to be edited must already exist in the system (created via US230).
* **User Authentication:** The CRM Collaborator performing the action must be logged in (Dependency on US210).
* **Show Request Status:** The status of the Show Request must be in a state that permits editing (e.g., 'PENDING', before proposal generation).
* **Listing/Finding Requests:** The user likely needs a way to find/select the specific request to edit (potentially using functionality related to US235).

### 1.5. Input and Output Data

**Input Data:**

* From User:
  * Show Request Identifier (to select the request).
  * New values for the fields being edited (e.g., updated place, date, duration, drone count, description, figure IDs).

**Output Data:**

* System Confirmation:
  * Indication of success or failure of the update operation.
  * (If editing is disallowed due to status) A message explaining why editing is not permitted.
* Stored Data:
  * (If successful) The specified `ShowRequest` record is updated in the persistent store with the new values.

### 1.6. Other Relevant Remarks

* **Clarification Needed:** The specific fields that are editable within a Show Request need explicit confirmation. The assumption listed in AC4 (Place, Date, Duration, Drones, Description, Figures) should be verified.
* **Concurrency:** Consideration should be given to potential concurrency issues if multiple users could attempt to edit the same request simultaneously, although this is less likely in a typical CRM workflow. A simple "last-write-wins" might suffice, or optimistic locking could be considered if needed.

