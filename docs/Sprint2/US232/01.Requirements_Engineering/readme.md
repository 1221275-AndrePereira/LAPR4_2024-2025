# US232: Search Figures - Requirements Engineering

## 1. Requirements Engineering

### 1.1. User Story Description

 US 232 Search Figure catalogue
 As CRM Collaborator, I want to search the figure catalogue by category and/or keyword.
 The search should ignore accents and shouldn’t be case sensiƟve

### 1.2. Customer Specifications and Clarifications

**From the client clarifications:**

>Question:Podemos considerar que o conceito "Category" existem em um "catalogo", podendo assim ser selecionadas?
>Awsner: Sim
> 
>Question: No que toca a "Keyword", estas podem ser livres, ou seja, é um valor introduzido pelo utilizador?
>Awsner: Sim. A comparação não deve ser case sensitive e deve ignorar acentos.


### 1.3. Acceptance Criteria

* **AC1:** The search should ignore accents and shouldn’t be case sensetive.

### 1.4. Found out Dependencies

* There must be Figures to search for and with Categories assigned. US233
* There must be Categories to search for. US245

### 1.5. Input and Output Data

**Input Data:**

* A string to base the Figures search of.

**Output Data:**

* System Display:
  * The list of figures that matched the string.

### 1.6. Other Relevant Remarks

* This user story was implemented together with US231 and US234 as a single feature
