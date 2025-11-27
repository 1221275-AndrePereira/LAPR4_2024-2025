# Project Shodrone
````
   _____  _                 _                          
  / ____|| |               | |                         
 | (___  | |__    ___    __| | _ __  ___   _ __    ___ 
  \___ \ | '_ \  / _ \  / _` || '__|/ _ \ | '_ \  / _ \
  ____) || | | || (_) || (_| || |  | (_) || | | ||  __/
 |_____/ |_| |_| \___/  \__,_||_|   \___/ |_| |_| \___|

````
Engenharia de Aplicações (EAPLI)

Polythecnic of Porto, School of Engineering

---------------------------------------------
## 1. Description of the Project

"Shodrone" is a cutting-edge platform for creating and managing customized drone multimedia shows. Our mission is to empower clients to design their own unique aerial displays, from selecting from a rich library of figures and sequences to proposing entirely new drone formations, like company logos.

This project is a comprehensive academic endeavor for the 4th semester of the Licenciatura em Engenharia Informática at ISEP (2024/2025), integrating five core disciplines: Applications Engineering, Project Laboratory, Programming Languages, Computer Networks, and Computer Systems.


## 2. Features

Based on the available documentation, Shodrone includes the following features:
* **Customizable Shows**: Clients can create personalized shows by selecting from a catalog of drone figures and sequences.
* **Bespoke Figure Proposals**: Clients can submit their own figure designs for truly unique and exclusive shows.
* **Backoffice Management**: A powerful backoffice application for managing clients, drones, figures, and show proposals.
* **Customer-Facing Application**: A user-friendly console for clients to manage their show requests and proposals.
* **Extensible Architecture**: A modular, plugin-based architecture that allows for easy extension, such as adding support for new drone languages.
* **Simulation and Testing**: A dedicated testing application for simulating and validating drone shows.

## 3. Planning and Technical Documentation

* [Planning and Technical Documentation](docs/readme.md)
* [Glossary of Terms](./docs/Glossary/glossary.md)

## Architecture & Design

The Shodrone platform is built on a robust and scalable architecture, leveraging modern design principles and technologies.


### Architectural Style

The project follows a classic **Layered Architecture**, with a clear separation of concerns between:

* **Presentation Layer**: The user interface, implemented as console applications for different user roles.
* **Application Layer**: Contains the application logic and orchestrates the domain layer.
* **Domain Layer**: The heart of the application, modeled using **Domain-Driven Design (DDD)** principles. It includes entities, value objects, aggregates, and domain services.
* **Infrastructure Layer**: Provides implementations for persistence, and other technical services.

### Modular Structure

The project is highly modular, with each module representing a specific bounded context or technical concern. Key modules include:

* `shodrone.core`: The core domain model.
* `shodrone.app.backoffice.console`: The backoffice application.
* `shodrone.app.customer.console`: The customer-facing application.
* `persistence.impl.jpa`: The JPA-based persistence implementation.
* `shodrone.integrations.plugins.*`: A suite of plugins for drone languages and other integrations.


## 4. How to Build

Make sure `JAVA_HOME` is set to the JDK folder and Maven is on the system `PATH`.

**On Windows:**

* To build the project:
    ```bash
    ./build-all.bat
    ```
* To quick-build the project (copies dependencies and verifies, skips Javadoc):
    ```bash
    ./quickbuild.bat
    ```
* To rebuild the project:
    ```bash
    ./rebuild-all.bat
    ```

**On Linux/MacOS:**

* To build the project (copies dependencies and packages):
    ```bash
    ./build-all.sh
    ```
  *You can pass Maven command line options as an argument, e.g., `./build-all.sh -DskipTests`*
* To quick-build the project (copies dependencies and verifies, skips Javadoc):
    ```bash
    ./quickbuild.sh
    ```
  *You can pass Maven command line options as an argument, e.g., `./quickbuild.sh -DskipTests`*
* To rebuild the project:
    ```bash
    ./rebuild-all.sh
    ```
  *This script likely calls `./build-all.sh clean package` or a similar Maven command.*

## 5. How to Execute Tests

Make sure `JAVA_HOME` is set to the JDK folder and Maven is on the system `PATH`.

**On Windows:**

```bash
./run-tests.bat
```

**On Linux/MacOS:**

```bash
./run-tests.sh
```
*This script executes `mvn test`.*

## 6. How to Run

Ensure the project has been built with Maven, including the `copy-dependencies` goal.

**Backoffice Application:**

* **On Windows:**
    ```bash
    ./run-backoffice.bat
    ```
* **On Linux/MacOS:**
  The script sets up the classpath and runs the `ShodroneBackOffice` Java class.
    ```bash
    ./run-backoffice-app.sh
    ```

**Customer Server:**

* **On Windows:**
    ```bash
    ./run-deamon-server.bat
    ```
* **On Linux/MacOS:**

  ```bash
    ./run-deamon-server.sh
    ```

**Customer Application:**

* **On Windows:**
    ```bash
    ./run-customer-app.bat
    ```
* **On Linux/MacOS:**

  ```bash
    ./run-customer-app.sh
    ```

**Show Testing Server:**

* **On Windows:**
    ```bash
    ./run-testing-server.bat
    ```
* **On Linux/MacOS:**

  ```bash
    ./run-testing-server.sh
    ```

**Show Testing Application:**

* **On Windows:**
    ```bash
    ./run-testing-app.bat
    ```
* **On Linux/MacOS:**

  ```bash
    ./run-testing-app.sh
    ```

**Bootstrap Application:**
(Used to initialize data in the system)

* **On Windows:**
    ```bash
    ./run-bootstrap.bat
    ```
* **On Linux/MacOS:**
    ```bash
    ./run-bootstrap.sh
    ```

## 7. How to Generate PlantUML Diagrams

To generate PlantUML diagrams for documentation (currently for Linux/Unix/MacOS):
```bash
./generate-plantuml-diagrams.sh
```
*This script finds all `.puml` files in the `docs` directory and uses `libs/plantuml-1.2023.1.jar` to generate diagrams, by default in SVG format.*
