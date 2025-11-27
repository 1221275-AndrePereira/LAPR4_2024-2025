# US235: List Show Requests of Client - Requirements Engineering

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US235
* **Title:** List show requests of client
* **As a:** CRM Manager or CRM Collaborator
* **I want to:** List all show requests of a client.
* **So that:** I can review the history and status of requests for a specific customer.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

> US 235 List show requests of client
> As CRM Manager or CRM Collaborator, I want to list all show requests of a client.
> The show request status information should be provided.

**From the client clarifications:**

* *(No specific client clarifications were provided for this US. Requirements are derived from the specification document).*

### 1.3. Acceptance Criteria

* **AC1:** The system must allow the user (CRM Manager or Collaborator) to specify a client.
* **AC2:** The system must retrieve all show requests associated with the specified client.
* **AC3:** For each show request listed, the system must display its current status.
* **AC4:** If a client has no show requests, the system should indicate this clearly (e.g., display an empty list or a "No requests found" message).
* **AC5:** The list should ideally include enough information to identify each request (e.g., Request ID, Creation Date, Description Snippet, Status).

### 1.4. Found out Dependencies

* **Customer Existence:** The Customer whose requests are being listed must exist in the system.
* **Show Request Existence:** Show requests must have been previously created (via US230) for the specified customer.
* **User Authentication:** The CRM Manager or Collaborator performing the action must be logged in (Dependency on US210).

### 1.5. Input and Output Data

**Input Data:**

* From User:
  * Customer Identifier (e.g., Phone number or Email to select the customer).

**Output Data:**

* System Display:
  * A list of show requests associated with the specified customer.
  * Each list item should display key information, including at minimum:
    * Show Request Identifier
    * Show Request Status
    * (Recommended) Creation Date
    * (Recommended) Brief Description or Place
  * Or, a message indicating that no show requests were found for the customer.

### 1.6. Other Relevant Remarks

* The user story does not specify sorting or filtering criteria for the list, but sorting by creation date (newest first) might be a sensible default implementation detail.
* Pagination might be necessary if a customer can have a very large number of requests, although not explicitly required by the user story.

