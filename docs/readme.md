# Shodrone - Planning and Technical Documentation
````
   _____  _                 _                          
  / ____|| |               | |                         
 | (___  | |__    ___    __| | _ __  ___   _ __    ___ 
  \___ \ | '_ \  / _ \  / _` || '__|/ _ \ | '_ \  / _ \
  ____) || | | || (_) || (_| || |  | (_) || | | ||  __/
 |_____/ |_| |_| \___/  \__,_||_|   \___/ |_| |_| \___|

````
## 1. Introduction

This document provides an overview of the planning and technical aspects of the Shodrone project. Shodrone is a system designed for managing and delivering customized drone multimedia shows. This documentation covers the project's context as an integrative academic project, its structure, key technical components, development methodology, system specifications, architectural considerations, and non-functional requirements.

The Shodrone project is developed as part of the 4th semester of the Licenciatura em Engenharia Informática at ISEP (2024/2025) and integrates five course units: Applications Engineering (EAPLI), Laboratory and Project IV (LAPR4), Languages and Programming (LPROG), Computer Networks (RCOMP), and Computer Systems (SCOMP). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 2]

## 2. Project Overview

Shodrone is a company that provides customized drone multimedia shows. The system aims to support Shodrone's back-office operations and client interactions. Key functionalities include:

* **Client Management:** Registering and managing client information. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7]
* **Show Request Management:** Handling client requests for shows, from initial submission to analysis, validation, and acceptance/rejection. This includes managing exclusivity for figures. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7, 10]
* **Figure Library Management:** Managing a library of drone figures, including creation, categorization, and versioning. Clients can select from this library or propose new figures. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7, 9-10]
* **DSL for Figures:** Utilization of a Domain Specific Language (DSL) to describe figures, aiming to avoid vendor lock-in and support scalability. The DSL code is imported into the system. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7, 10]
* **Drone Code Generation:** Generating actual drone code from the DSL descriptions via specific modules/plugins. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7, 10]
* **Simulation and Testing:**
  * **Figure Simulation:** Simulating the operation of all drones in a figure to detect collisions within a 3D matrix. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7, 11]
  * **Full Show Simulation:** Simulating entire shows, involving coordination with a central orchestrator/maestro server. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7]
* **Scalability:** The system is designed to support large shows with hundreds of drones, including capabilities for parallelizing simulations. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 7]

## 3. Project Structure (Maven Modules)

The Shodrone project is a multi-module Maven project. The primary modules identified are:

* **`shodrone` (Parent POM):** The root project that aggregates all other modules. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/pom.xml]
* **`shodrone.core`:** Contains the core domain logic, business rules, and entities (Users, Show Requests, Figures, etc.). [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.core/pom.xml]
* **`shodrone.app.common.console`:** Provides common console UI components and base application classes. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.app.common.console/pom.xml]
* **`shodrone.app.backoffice.console`:** The console application for backoffice users. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.app.backoffice.console/pom.xml]
* **`shodrone.app.customer.console`:** A console application for customer interactions (referred to as "Customer App" in requirements). [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.app.customer.console/pom.xml]
* **`shodrone.app.bootstrap`:** Handles initial data setup. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.app.bootstrap/pom.xml]
* **`shodrone.bootstrappers`:** Contains classes for bootstrapping application data. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.bootstrappers/pom.xml]
* **`shodrone.persistence.impl`:** Implements the persistence layer (JPA). [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.persistence.impl/pom.xml]
* **`shodrone.infrastructure.application`:** Contains infrastructure-level concerns. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.infrastructure.application/pom.xml]
* **`shodrone.util.ci`:** Utility module for Continuous Integration (CI). [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.util.ci/pom.xml]
* **`shodrone.integrations.plugins.figure.standard`:** Suggests a plugin architecture for figures. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/shodrone.integrations.plugins.figure.standard/pom.xml]
* **`plugins/ShowProposalPlugin`**: An external plugin for generating show proposals. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/plugins/ShowProposalPlugin/src/main/java/Main.java]

## 4. Architectural Overview

The project follows a layered architecture: Presentation, Application, Domain, and Infrastructure.
System component diagrams in the project requirements illustrate the evolving architecture across sprints:
* **Sprint 2 Architecture (Figure 4):** Depicts the Backoffice application interacting with a Database, DSL Plugin, Drone Language Plugin, and Show Proposal Plugin. A separate Show Simulator component interacts with a Figure Code Repository. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 12]
* **Sprint 3 Architecture (Figure 5):** Expands on Sprint 2 by introducing a Customer App communicating with a Customer App Server (via CAS_Socket) within the Backoffice. A Testing App is also introduced, interacting with the Customer App Server and the Show Simulator. The Show Simulator includes a Drone Runner component. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 17]

Key architectural considerations include modularity via Maven and a plugin-based approach for DSL processing, drone language support, and show proposal generation.

## 5. Key Technical Components & Concepts

### 5.1. Domain Model
The core domain entities include:
* **Users & Roles:** System users (Admin, CRM Manager, CRM Collaborator, Show Designer, Drone Tech) and Customer Representatives. Roles are defined in `ShodroneRoles`. Users are identified by a unique `showdrone.com` email. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 9]
* **Customers:** European corporate/public entities with VAT, address, and representatives. Statuses include Created, Regular, VIP, Infringement, Deleted. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 9]
* **Figures & Shows:** A show is a sequence of figures. Figures are drone compositions (static or dynamic) described by a versioned DSL. Figures have attributes like code, description, version, exclusivity, designer, and DSL code. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 9-10]
* **Show Requests:** Captures customer requests (customer, place, time, #drones, duration, description, figure details, exclusivity). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 10]
* **Drone Models & Inventory:** Drone models include behavior under wind. Inventory tracks drones by serial number. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 14]
* **PlantUML:** Used for domain modeling. Diagrams are generated from `.puml` files in the `docs` directory. [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/generate-plantuml-diagrams.sh] [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]

### 5.2. Persistence
* **JPA & Repository Pattern:** For ORM and data access abstraction.
* **Database Flexibility (NFR07):** Supports in-memory (H2 for dev/test) and remote persistent RDBMS for final deployment. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]
* **Data Initialization:** Bootstrap process for default data. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 13, 14, 21]

### 5.3. User Authentication and Authorization (NFR08)
* Role-based access control (`ShodroneRoles`).
* Authentication for all users and functionalities. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]
* Frameworks can be used for these aspects (US210). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 13]

### 5.4. Figure/Show Description (DSL) & Drone Languages (NFR11)
* **High-Level DSL:** Neutral language for figures (geometric figures or 3D bitmaps, rotation/translation movements). Versioned. To be specified (US251). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 10, 15]
* **ANTLR:** Used for DSL and drone language analysis/validation. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 22]
* **Drone Programming Languages:** Drone-specific. System allows configuration of programming language per drone model (US253). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 11, 15]
* **Plugins:** DSL Plugin (US340) for figure description analysis, Drone Language Plugin (US345) for drone program analysis. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 18]

### 5.5. Drone Simulation (SCOMP)
* **Purpose:** Validate figures/shows for collisions and safety.
* **Sprint 2 Simulation (US261-US266):**
  * Implemented in C. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 15]
  * Utilizes processes (one per drone), pipes for communication, and signals.
  * Main process tracks drone positions in a time-indexed 3D matrix.
  * Detects collisions (drones in same position at same time).
  * Synchronized time-step progression.
  * Generates a report (file) with drone count, status, collision details, pass/fail.
  * Incorporates environmental factors (e.g., wind) from an external file. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 15-16]
* **Sprint 3 Simulation (NFR12, US361-US365):**
  * Evolves to a hybrid simulation with a multithreaded parent process and child drone processes. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 18, 22]
  * Communication via shared memory.
  * Parent process has dedicated threads (e.g., collision detection, report generation).
  * Synchronization using semaphores, mutexes, and condition variables.
  * Report generation thread notified of collisions via condition variables.
  * Step-by-step synchronization enforced by semaphores.
  * Termination via signals if collision threshold exceeded. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 18-19]

### 5.6. Network Communication (NFR10)
* Network sockets APIs can use a new application protocol or a standard one like HTTP. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 22]
* Relevant for Customer App Server (CAS_Socket) and Show Simulator communication with Drone Runner (R_Socket) and Testing App (S_Socket). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 17]

### 5.7. Configuration
* **`application.properties`:** For persistence unit names, repository factories, etc.
* **`logback.xml`:** For logging.

## 6. Development Process & Planning (NFR01)

The project follows the **Scrum** methodology. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]

* **Team Structure:** Teams of 4-5 students. Regular (daily) updates to GitHub are expected. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 3-4]
* **Sprint Structure:** The project is divided into three sprints, with specific course unit involvement:
  * **Sprint 1 (Deadline: April 6th, 2025):** EAPLI, LAPR4. Focus on technical setup and initial domain modeling (US101-US105, US110). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 3, 12]
  * **Sprint 2 (Deadline: May 18th, 2025):** EAPLI, LPROG, SCOMP, LAPR4. Focus on core backend functionalities, user management, catalogue management, initial simulation (C-based with processes/pipes). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 3, 12-16]
  * **Sprint 3 (Deadline: June 15th, 2025):** EAPLI, LPROG, RCOMP, SCOMP, LAPR4. Focus on proposal generation, plugin integration, advanced simulation (C-based with shared memory/threads), and customer-facing app features. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 3, 17-20]
* **User Stories (US):** Provided at the beginning of each sprint. Teams are responsible for analysis, planning, and balanced task division. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 4]
* **Course Unit Integration:** Students apply knowledge from all enrolled UCs. LAPR4 students implement requirements from all semester UCs. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 4]
* **LAPR4 Role:** The LAPR4 PL teacher acts as Scrum Master. Assessment focuses on Scrum application and iterative/incremental development. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 6, 21]
* **Documentation (NFR02):** Technical documentation (Markdown, UML, PlantUML) in the `docs` folder. Development process of each US must be reported. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]

## 7. Build, Test, and CI

* **Build System (Maven):** Manages dependencies, builds, and tests. Scripts (`build-all.sh`, etc.) simplify operations.
* **Testing (NFR03):** Aim for Test-Driven Development (TDD). JUnit for unit tests (`mvn test`). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]
* **Source Control (NFR04):** GitHub repository. Main branch for releases. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]
* **Continuous Integration (NFR05):** GitHub Actions/Workflows for nightly builds, publishing results/metrics (e.g., JaCoCo). [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]

## 8. Deployment (NFR06)

* Scripts for building and deploying on Linux and Windows.
* `readme.md` in the root folder explains build, deploy, and execution. [cite: Sem4PI_Project_Requirements_v02b.pdf, p. 21]
  (Refer to the main project `readme.md` for detailed deployment steps).

## 9. Non-Functional Requirements (NFRs)

The project adheres to several non-functional requirements outlined in `Sem4PI_Project_Requirements_v02b.pdf` (pages 21-22):

* **NFR01 - Project management using Scrum:** Scrum methodology with weekly meetings.
* **NFR02 - Technical Documentation:** Markdown in `/docs`, UML, PlantUML (source and PNG). US development process documented.
* **NFR03 - Test-driven development:** Aim to adopt TDD.
* **NFR04 - Source Control:** GitHub, main branch for releases. Regular commits expected.
* **NFR05 - Continuous Integration:** GitHub Actions for nightly builds and metrics.
* **NFR06 - Deployment and Scripts:** Scripts for build/deploy on Linux & Windows. Root `readme.md` with instructions.
* **NFR07 - Database by configuration:** Support in-memory and RDBMS (remote persistent RDBMS for final deployment). Default data initialization.
* **NFR08 - Authentication and Authorization:** For all users and functionalities.
* **NFR09 - Programming language:** Java as the main language. Other languages (like C for simulation) as per specific requirements.
* **NFR10 - Network sockets APIs:** May implement new or use standard protocols (e.g., HTTP).
* **NFR11 - High-level language (DSL) and drones' language analysis/validation:** Follow LPROG technical requirements; use ANTLR.
* **NFR12 - Simulation system in sprint 3:** Multithreaded parent process, child drone processes, shared memory, dedicated threads (collision detection, report generation), semaphores for synchronization, signal-based termination on collision threshold.

## 10. Glossary

A glossary of terms is maintained in [`docs/Glossary/glossary.md`](./Glossary/glossary.md). [cite: sem4pi-2024-2025-sem4pi_2024_2025_g50/docs/Glossary/glossary.md]
