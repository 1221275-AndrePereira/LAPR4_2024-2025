US317: Mark show proposal as accepted - Requirements Engineering
=====================================================================================

1\. Requirements Engineering
----------------------------

### 1.1. User Story Description

* **ID:** US317 [page: 17; line: 17]
* **Title:** Mark show proposal as accepted
* **As a:** CRM Collaborator
* **I want to:** I want to mark the proposal as accepted by the customer by attaching the customer’s acceptance email.
* **So that:** I can formally acknowledge the acceptance of the show proposal by the customer.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (Sem4PI\_Project\_Requirements\_v03c.pdf):**

* "As a CRM Collaborator, I want to mark the proposal as accepted by the customer by 
attaching the customer’s acceptance email

**From the client clarifications:**

> **Question 1:**
>Boa tarde,
>
>Relativamente à user story US317, gostaríamos de confirmar se a seguinte abordagem está correta:
>
>A funcionalidade será implementada de forma que o CRM Collaborator possa consultar, para um determinado cliente, se existe algum show proposal que já tenha sido aceite por um dos seus representativos (via Customer App). Caso exista, o Collaborator poderá então decidir marcar (ou não) essa proposta como formalmente aceite no sistema.
>
>Esta abordagem está de acordo com o pretendido para esta user story?
>
>Cumprimentos,
>Grupo 41


> **Answer to Question 1:**
>Boa tarde,
>
>É "representante", não "representativo".
>O cenário que descreve não me parece mal.
>
>Cumprimentos,
>Angelo Martins

> **Question 2:** Boa tarde, professor;
>
>Antes de mais, obrigado pela disponibilidade e o tempo dedicado a responder a esta pergunta.
>
>Durante a análise e levantamento de requisitos, o nosso grupo identificou que existe menção de um estado intermédio entre a proposta ser concretizada pelo CRM Colaborator e o parecer positivo do Representante do Cliente.
>
>Assim, em primeiro lugar, esse estado identificado existe? Na eventualidade da sua existência, qual seria o nome mais apropriado para esse estado (e.x Enviado, Parcialmente Aprovado, Implementado, etc.) ?
>
>Cumprimentos;
>Grupo 64

> **Answer to Question 2:** Boa tarde,
>
>Lamento, mas não percebi. Talvez pudesse enumerar os estados.
>
>Cumprimentos,
>Angelo Martins


### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to mark a show proposal as accepted by attaching the
  customer’s acceptance email.

* **AC2:** To a Show Proposal be marked as accepted, the system must require the CRM Collaborator to select a
  specific show proposal from a list of proposals associated with the customer.

* **AC3:** * A Show Proposal can't be incomplete or in a state that prevents it from being marked as accepted.

* **AC4:** Upon Successful marking of a show proposal as accepted, the system must update the status of the
  proposal to "Accepted" and store the attached acceptance email.

### 1.4. Found out Dependencies

* **User Authentication (US210):** The CRM Collaborator must be authenticated and authorized to access this
  functionality.
* **Show Proposal Existence (US310):** A Show Proposal must already exist before it can be marked as accepted.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
    * Selection of one specific show proposal from a list of proposals associated with the customer.
* From System:
    * List of show proposals associated with the customer.
    * email attachment containing the customer’s acceptance of the proposal.
    * Current status of the selected show proposal.

**Output Data:**

* To User (CRM Collaborator):
    * Confirmation of successful marking of the show proposal as accepted.
    * Updated status of the show proposal to "Accepted".
* To Customer:
    * new show proposal record in the database, reflecting the acceptance status and the attached email.
* To System:
    * Updated show proposal record in the database, reflecting the acceptance status.

### 1.6. Other Relevant Remarks

* The system should ensure that the CRM Collaborator has the necessary permissions to mark a show proposal as accepted.
* The system should validate that the selected show proposal is in a state that allows it to be marked as accepted.