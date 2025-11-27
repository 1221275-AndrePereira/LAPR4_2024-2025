US312: Add figures to a proposal - Requirements Engineering
=====================================================================================

1\. Requirements Engineering
----------------------------

### 1.1. User Story Description

* **ID:** US312 [page: 17; line: 17]
* **Title:** Add figures to a proposal
* **As a:** CRM Collaborator
* **I want to:** add one of the available figures to a show proposal.
* **So that:** I can include the desired figures in the show proposal for the customer.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (Sem4PI\_Project\_Requirements\_v03c.pdf):**

* "As a CRM Collaborator, I want to add one of the available figures to a show proposal."
    * _Implementation Note:_
        * "Any active figure can be added to the show proposal."
        * "A figure may have more than one occurrence in a show, but never in two consecutive positions."
        * "For each figure, it must be established the relation between each drone type in the figure and the drone
          models in the show."

**From the client clarifications:**

> **Question 1:** Boa tarde,
>
> Um dos requisitos desta US é "For each figure, it must be established the relation between each drone type in the
> figure
> and the drone models in the show."
>
> Inicialmente interpretei como se só pudesse adicionar uma figura a um show, se os tipos de drones da figura forem
> iguais
> aos tipos dos drones do show. Mas como cada show proposal tem mais de uma figura, também terá diferentes modelos de
> drones.
>
> Ao que certo se refere esta relação pedida ?

> **Answer to Question 1:** Boa tarde,
>
>Olhando para os ficheiros de exemplo das figuras, verifica-se que estão lá definidos "Drone Types". E cada Show
> Proposal terá um conjunto de modelos. É preciso fazer essa associação para cada figura.
>
>Cumprimentos,
> Angelo Martins

> **Question 2:** Olá professor,
>
>Temos uma dúvida em relação ao requisito sobre a adição de figuras ao show proposal (US312).
>Gostaríamos de confirmar se alguma das opções seguintes se enquadra nos objetivos deste requisito:
>
>Cada figura precisa obrigatoriamente utilizar todos os drone models definidos no show proposal (ou seja, cada drone type da figura poderia estar associado a mais de um drone model)?
>O conjunto completo das figuras no show proposal é que, no final, devem cobrir todos os drone models?
>Ou então isto nem sequer se aplica no contexto do show...
>Agradecemos desde já pela atenção!
>
>Cumprimentos,
>Ricardo Meireles | FOURCORP

> **Answer to Question 2:** Bom dia,
>
>Os requisitos são claros: qualquer figura do show usa TODOS os drones desse mesmo show. Não podem ficar drones de fora.
>
>Quanto à relação entre tipo de drone numa figura e o modelo de drone, não parece sensato que um tipo de drone numa figura possa ser implementado por mais do que um modelo de drone. Era uma confusão...
>
>Cumprimentos,
>Angelo Martins 

### 1.3. Acceptance Criteria

* **AC1:** The system must allow a CRM Collaborator to add one or more existing figures to a show proposal.(created via
  US310)

* **AC2:** The figures must be selectable from a list of available figures in the system.

* **AC3:** For each figure added, the system must establish the relationship between each drone type in the figure and
  the drone models in the show proposal. This means that the system should allow the CRM Collaborator to specify which
  drone types in the figure correspond to which drone models in the show.

* **AC4:** Upon Successful addition of a figure, the system must update the show proposal to reflect the newly added
  figures.

### 1.4. Found out Dependencies

* **User Authentication (US210):** The CRM Collaborator must be authenticated and authorized to access this
  functionality.
* **Show Proposal Existence (US310):** A Show Proposal must already exist before figures can be added to it, as this
  user story
  builds upon the proposal created in US310.
* **Figure Creation (US233):** Figures must already exist in the system, created via US233, before they can be added to
  a proposal.

### 1.5. Input and Output Data

**Input Data:**

* From User (CRM Collaborator):
    * Selection of one or more figures from a list of available figures in the system.
* From System:
    * List of available figures, including their attributes such as:
        * Unique Figure ID.
        * Name/Description of the figure.
        * Associated Drone Types (e.g., "Type A", "Type B").
        * Any other relevant metadata.

**Output Data:**

* To User (CRM Collaborator):
    * Confirmation of successful addition of figures to the show proposal.
    * Updated show proposal details, including the newly added figures and their associated drone types.
* To System:
    * Updated show proposal record in the database, reflecting the added figures and their relationships with drone
      models.

### 1.6. Other Relevant Remarks

* The system should ensure that the figures added to the show proposal are compatible with the drone models in the
  proposal.