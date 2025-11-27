US230: Register Show Request - Requirements Engineering
=====================================================================================

1\. Requirements Engineering
----------------------------

### 1.1. User Story Description

* **ID:**US230

* **Title:**Register Show Request

* **As a:**CRM Collaborator

* **I want to:**Register (create) a show request for a customer

* **So that:**The customer’s show can be planned and executed.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

* A customer contacts a Shodrone's CRM Collaborator to submit a request for a show. (Sec 3.1.4)

* The CRM Collaborator creates the show request into the system. (Sec 3.1.4)

* If it is a new customer, it must be created in the system first. (Sec 3.1.4)

* A show request includes the customer, place, time, number of drones requested (tentative) and duration. (Sec 3.1.4)

* _Implementation Alignment:_The RegisterShowRequestController accepts parameters for description, place, creation date,
  show date, duration, number of drones, and the selected ShodroneUser(customer).

* It also should include the show's description, i.e. a document with the sequence of figures from the Shodrone's
  catalogue and/or request of new figures, as well as customer's exclusivity requirements. (Sec 3.1.4)

* _Implementation Alignment:_A description string is captured. The detailed structure (sequence of figures, exclusivity)
  is currently part of this general text description.

* Basic workflow information should also be kept (author of the request, history, etc.). (Sec 3.1.4)

* _Implementation Alignment:_

* The ShowRequest entity stores the ShodroneUser customer \[cite: implementations from the
  project/showrequest/domain/ShowRequest.java\], representing the customer for whom the request is made. The CRM
  Collaborator performing the data entry is not stored as a separate "author" field on the ShowRequest itself.

* A RequestCreationDate is stored.

* Fields for detailed workflow history (e.g., createdBy, lastModifiedBy, history list) are commented out in the
  ShowRequest.java domain class, indicating they are not currently implemented.

* A CRM collaborator can only submit show request of active customers. (Sec 3.1.2)

* _Implementation Alignment:_The RegisterShowRequestController's isCustomer()method checks if the ShodroneUser has the
  ShodroneRoles.CRM\_COLLABORATOR role \[cite: implementations from the
  project/showrequest/application/RegisterShowRequestController.java\]. This confirms they are a recognized customer
  type but does not explicitly check a separate "active" status attribute (e.g., 'active' vs. 'inactive', or 'REGULAR'
  vs. 'VIP' for edit permissions) before creation.

* "VIP" customers have always priority over "Regular" customers (Note: Priority likely affects later stages, not the
  creation itself). (Sec 3.1.2)

**From the client clarifications:**

* _(No specific client clarifications were provided for this US. Requirements are derived from the specification
  document and actual implementation)._

### 1.3. Acceptance Criteria

* **AC1:**A show request must be associated with an existing ShodroneUser(Customer).

* _Implementation Alignment:_AddShowRequestUI uses a SelectWidget to choose an existing ShodroneUser. This user is then
  associated with the ShowRequest.

* **AC2:**The associated Customer must be identifiable as a valid customer (e.g., possesses the CRM\_COLLABORATORrole).

* _Implementation Alignment:_The RegisterShowRequestController.isCustomer()method verifies the selected ShodroneUser has
  the ShodroneRoles.CRM\_COLLABORATOR role before saving the request. A more granular "active" status check (beyond
  role) is not present in this controller's logic.

* **AC3:**The request must include mandatory fields: Selected Customer, Show Description, Place, Show Date, Duration (
  minutes), and Number of Drones.

* _Implementation Alignment:_The AddShowRequestUI collects these fields, and the RegisterShowRequestController validates
  against null/empty for description and place, and positive values for duration and number of drones. Date validation (
  show date not in the past) is also performed in the UI.

* **AC4:**The system must automatically record the Creation Timestamp for the request.

* _Implementation Alignment:_RequestCreationDate.valueOf()is used in RegisterShowRequestController, derived from
  Calendar.getInstance()in AddShowRequestUI, effectively capturing the creation timestamp.

* **AC5:**The system should define how the author of the request (the CRM Collaborator performing the action) is
  tracked, if distinct from the customer field.

* _Implementation Note:_Currently, the ShowRequest entity stores the customer. If the logged-in CRM Collaborator needs
  to be stored as a distinct "author" on the ShowRequest, this requires a model change.

* **AC6:**The initial status of the created request (e.g., 'PENDING') should be defined and set if applicable.

* _Implementation Note:_The ShowRequest.java domain class does not currently include an explicit status field. If a
  status is required, the domain model and persistence need an update.

### 1.4. Found out Dependencies

* **Customer Existence & Role:**The ShodroneUser(Customer) for whom the request is being made must exist in the system
  and possess a role that identifies them as a customer (e.g., CRM\_COLLABORATOR). (Potentially created/managed via
  US220).

* **User Authentication:**The CRM Collaborator performing the action must be logged in to access the:AddShowRequestUI. (
  Dependency on US210).

* **Figure Catalogue (Indirect):**The show description might reference figures from a catalogue (managed by other US
  like US231, US233). The current implementation treats the description as a text field.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator via AddShowRequestUI):

* Selected ShodroneUser(Customer) from a list.

* Show Description (String).

* Show Location (Place description - String).

* Tentative Show Date (java.util.Calendar object from UI, converted to RequestShowDate).

* Tentative Duration (integer, in minutes).

* Tentative Number of Drones (integer).

* From System:

* Creation Timestamp (java.util.Calendar.getInstance()in UI, converted to RequestCreationDate).

* Authenticated CRM Collaborator's identity (available via application security context, though not explicitly stored
  as "author" on the ShowRequest entity itself in the current code).

**Output Data:**

* System Confirmation (via AddShowRequestUI):

* Indication of success ("Show request created successfully.") or failure ("Error creating show request: ...") of the
  operation.

* Stored Data:

* A new ShowRequest record persisted with:

* Auto-generated ID.

* Associated ShodroneUser(customer).

* Description, Place, Creation Date, Show Date, Duration, Number of Drones.

* Version number for optimistic locking.

* _(Currently Not Stored on ShowRequest entity based on ShowRequest.java)_: Explicit "Author" (if different from
  customer), "Initial Status", detailed "Workflow History".

### 1.6. Other Relevant Remarks

* This user story initiates the "Request and Proposal" workflow.

* The ShowRequest entity in ShowRequest.java has commented-out fields for author, createdBy, createdAt, lastModifiedBy,
  lastModifiedAt, and history. This suggests an intent for more detailed workflow tracking that is not yet active in the
  provided code.

* The handling of requests for_new_figures within the description is part of subsequent workflow stages.

* The priority of VIP customers is noted but likely impacts downstream processes.

* The RegisterShowRequestController includes validation logic for input parameters (e.g., non-empty strings, positive
  integers for duration/drones, show date not before creation date).