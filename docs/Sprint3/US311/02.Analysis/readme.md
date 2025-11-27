# US311: Add drone models to a show proposal - Analysis

## 2. Analysis

### 2.1 System Sequence Diagram

This diagram shows the interaction between the CRM Collaborator and the system to add drone models to a proposal.

![System Sequence Diagram](US311_SSD.png)

### 2.2. Relevant Domain Model Snippet

The domain model snippet below, extracted from the complete project domain model, illustrates the core entities for this use case. A `ShowProposal` aggregate contains a collection of `ProposalDronesModel` objects. Each `ProposalDronesModel` links to a specific `DroneModel` and holds the number of drones of that model (`ProposedNDrones`).

![Domain Model](US311_DM.png)