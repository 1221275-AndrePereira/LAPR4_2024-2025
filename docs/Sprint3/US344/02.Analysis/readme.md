# US345: Generate Drone Programs for a Show Proposal - Analysis

## 2. Analysis

### 2.1 System Sequence Diagram

This diagram illustrates the primary interaction between the CRM Collaborator and the Shodrone system for generating drone programs. The user selects a proposal, and the system handles the generation process internally.

![System Sequence Diagram](US345_SSD.svg)

### 2.2. Relevant Domain Model Snippet

The domain model shows how a `ShowProposal` is composed of `ProposalFigure`(s) and `ProposalDroneModel`(s). The generation process creates `DroneInstructions` for each combination and links them via the `DroneTypeSetting` entity. The status of the `ShowProposal` is crucial for controlling the workflow.

![Domain Model](US345_DM.png)

### 2.3. Other Remarks

The preconditions checked in the `GenerateProgramsController` are critical for ensuring the domain remains in a consistent state. An attempt to generate programs for a proposal that is not in the `TESTING` state or lacks figures/models would violate the business logic.