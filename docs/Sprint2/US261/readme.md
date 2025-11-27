# US261 - Initiate simulation for a figure


## 1. Requirements Engineering

### 1.1. User Story Description

As a system user, I want to start a simulation process for a figure so that I can check for collisions before
approving it.


### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>

**From the client clarifications:**

> **Question:** Para a simulação dos drones, qual deve ser a resolução temporal entre atualizações de posição de cada drone? Minutos, segundos ou milissegundos?

> **Answer:** Provavelmente faz sentido que possa ser configurável pelo utilizador. Minutos é um disparate completo. Não faz qualquer sentido. Mas poucos milissegundos pode ser um exagero e ser muito pesado do ponto de vista computacional. A velocidade dos drones no show vai definir o intervalo de amostragem. Por exemplo, se a velocidade máxima for 10 m/s, o intervalo de amostragem deve ser de 50 milissegundos, ou 20 Hz. Em processamento do sinal define-se que é possível recuperar um sinal fielmente com uma amostragem que seja o dobro da frequência máxima do sinal. Obter a velocidade máxima no show/figura pode ser muito trabalhoso, pelo que deixe o utilizador decidir.

### 1.3. Acceptance Criteria

* **AC1:** This component must be implemented in C and must utilize processes, pipes, and signals.
* **AC2:** The system should fork a new process for each drone in the figure.
* **AC3:** Each drone process should execute its designated movement script.
* **AC4:** Pipes should facilitate communication between the main process and each drone process.
* **AC5:** The main process should track drone positions over time using an appropriate data structure.

### 1.4. Found out Dependencies

* This story is dependent of US233 - "Add figure to the catalogue" as this US will require the use of figures.

### 1.5 Input and Output Data

* Selected data:
    

**Output Data:**

* 

### 1.7 Other Relevant Remarks

