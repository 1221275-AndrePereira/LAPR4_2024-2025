# US316 - Send show proposal to customer


## 1. Requirements Engineering

### 1.1. User Story Description

As CRM Collaborator, I want to send the show proposal to the customer.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>	

**From the client clarifications:**

> **Question:** Quando se refere a enviar a proposta ao cliente, significa: Ficar disponível na aplicação do cliente ou enviar um email?

> **Answer:** Tem de ficar disponível na App, como é possível ver na figura 3. Se quiserem também enviar um email ao cliente há um problema: para quem o enviam? Para todos os Customer Representatives?
> 
### 1.3. Acceptance Criteria

* **AC1:** The proposal to be sent is a properly formated document with the show details and a link to the video preview.
* **AC2:** The format of the proposal must be one supported by the system and has to be generated using the correct plugin.
* **AC3:** No proposal can be sent to the customer without prior successful testing of its show.


### 1.4. Found out Dependencies

* There are dependencies to most US's related to show proposal as the show proposal must be on the state "SAFE".

### 1.5 Input and Output Data

**Input Data:**

* The id of the show proposal to be sent to the customer (assuming it already has a document ready to be sent)

**Output Data:**

* (In)Success of the operation.

### 1.7 Other Relevant Remarks

This US will change the show proposal status from "SAFE"(meaning it was succesfully tested and it's ready to sent) to "PENDING"(meaning it was sent and it's available for the customer to accept/reject)