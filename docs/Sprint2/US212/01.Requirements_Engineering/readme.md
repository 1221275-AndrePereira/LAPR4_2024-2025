# US212: Disable/enable users

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US212
* **Title:** Disable/enable users
* **As a:** Administrator
* **I want to:** be able to disable/enable users of the backoffice
* **So that:** I can manage user access to the system

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

* No clarifications were provided.

**From the client clarifications:**

* No clarifications were provided.

### 1.3. Acceptance Criteria

* A.C.1: Only Admins can Disable/Enable users

### 1.4. Found out Dependencies

* US210 (Authentication/Authorization) it is required to have an admin logged in to disable/enable users
* US211 (Add new users) it is required to have an admin created to disable/enable

### 1.5. Input and Output Data

**Input Data:**

* From User :
    *  
    * Selected user to disable/enable
        
* From System:
    * 
        * Username

**Output Data:**

* List of users:
    * List of users to be disabled/enabled
* System Confirmation:
    *
        * Success/Error message

* Stored Data:
    * 
        * User status (enabled/disabled)
    
### 1.6. Other Relevant Remarks

* No relevant remarks.
