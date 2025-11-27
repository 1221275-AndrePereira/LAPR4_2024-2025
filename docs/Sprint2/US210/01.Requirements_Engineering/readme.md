# US210: Authentication and Authorization

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US210
* **Title:** Authentication and Authorization
* **As a:** Project Manager
* **I want to:**  support and apply authentication and authorization for all its users and funcionalities.
* **So that:** the system can ensure that only authorized users can access specific functionalities and data.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

* Users must authenticate into the system to do anything (section 3.1.1)
* Users are identified by a unique valid email from Shodrone's domain (@showdrone.com)
* The system has six main actor roles: Admin, CRM Manager, CRM Collaborator,
Show Designer, Drone Tech, and Customer Representative
* Currently, users don't have multiple roles but this may change in the future

**From the client clarifications:**

* Each group can define the minimum requirements for the password
* One Important aspect is to ensure that the mecanism used to generate the hash is strong and 
  secure

### 1.3. Acceptance Criteria

* A.C.1: The system must require authentication for all functionality access
* A.C.2: Users must be authenticated through a valid username and password
* A.C.3: Authorization must be role-based according to the six defined roles
* A.C.4: The system must prevent unauthorized access to functionalities
* A.C.5: The solution should be implemented using existing frameworks where possible
* A.C.6: Password policies must be enforced (minimum length, complexity)
* A.C.7: Passwords must be securely hashed and stored

### 1.4. Found out Dependencies

* US211 (Register users) - Required to create user accounts before authentication
* Database implementation (NFR07) - Needed for user credential storage
* Role definitions from system specifications (section 3.1.1)

### 1.5. Input and Output Data

**Input Data:**

* From User :
    *  
        * Username
        * Password
* From System:
    * 
        * User role
        * User Account Status (Active/Inactive)

**Output Data:**

* System Confirmation:
    * 
        * Authentication success/failure message
        * Authorization success/failure message
        * User role and permissions
* Stored Data:
    * 
    
### 1.6. Other Relevant Remarks

* Password storage must be hashed using strong algorithms (e.g., bcrypt)
