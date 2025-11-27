# US233 - Add figure


## 1. Requirements Engineering

### 1.1. User Story Description

As Show designer I want to add a figure to the public catalogue.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>Figures are classified with a category and a set of keywords. If a figure is custom-made to a customer’s
request it is not public and can only be used in shows for that customer.	

**From the client clarifications:**

> **Question:** Que conceito/atributo identifica uma figura? Ou seja, o que deverá diferenciar uma figura de todas as outras?
>
> **Answer:** Acho que a ideia de dar o devido crédito ao autor da figura é boa. É como no Ikea.
 

> **Question:**  For the "add a figure to the public catalogue" feature, can it be possible to add a new version of an existing figure to the public catalogue too?
>
> **Answer:** "Of course. All figures can be added to the catalogue.
> 
### 1.3. Acceptance Criteria

* **AC1:** A figure is distinguished from other figures with the combination of its Description and Version
* **AC2** If a Figure was made for a customer, only that customer is allowed to see and make use of it.

### 1.4. Found out Dependencies

* Dependency on US 213 needed to search a possible Customer to assign
* Dependency on US 245 needed to search Categories to assign

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * Description of the figure
    * Keywords for the figure
    * The version of the figure
    * The names of DSL files to assign
    * Categories names
    * Customers username

**Output Data:**

* (In)Success of the operation.

### 1.7 Other Relevant Remarks

n/a