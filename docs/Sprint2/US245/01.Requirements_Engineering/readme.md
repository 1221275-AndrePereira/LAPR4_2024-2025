# US245 - Add figure category


## 1. Requirements Engineering

### 1.1. User Story Description

As a Show Designer, I want to add a figure category to the figure category catalogue.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>	

**From the client clarifications:**

> **Question:** Existe algum atributo da figure category que a identifique unicamente no negócio?

> **Answer:** Seria muito estranho haver duas categorias com o mesmo nome, não?

> **Question:** Em relação às categorias no sistema, gostava de confirmar que tipo de informação considera importante guardar no momento da sua criação. Um nome, uma descrição e um status são suficientes? Se não, então que dados considera importantes guardar

> **Answer:** "Um nome, uma descrição e um status são suficientes?" - São necessários. Quando se activa/desactiva qualquer coisa convém guardar a data da alteração. Isso permite fazer pesquisas no passado com os estados válidos à data. Claro que andar a ativar e desativar várias vezes obrigaria a manter uma lista de estados e complica um bocado. Mas guardar pelo menos a data de criação e a última alteração de estado parece-me essencial.

### 1.3. Acceptance Criteria

* **AC1:** The category name must be unique (not case sensitive).

### 1.4. Found out Dependencies

* There aren't any dependencies to this user story.

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * name of the figure category
    * description of the figure category

**Output Data:**

* (In)Success of the operation.

### 1.7 Other Relevant Remarks

n/a