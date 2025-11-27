# US213: List users

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US213
* **Title:** List Users
* **As a:** Administrator
* **I want to:** be able to list the users of the backoffice, including their status
* **So that:** I can see the users that are registered in the system and their status

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

* List all users in the backoffice
* Include the status of the users (enabled/disabled)

**From the client clarifications:**

* No clarifications were provided.

### 1.3. Acceptance Criteria

* A.C.1: Only Admins can list users

### 1.4. Found out Dependencies

* US210 (Authentication/Authorization) it is required to have an admin logged in to list users
* US211 (Add new users) it is required to have an admin created to list users

### 1.5. Input and Output Data

**Input Data:**

* From User :
    * No input data is required from the user
        
* From System:
    * 
        * list of users

**Output Data:**

* List of users:
    * List of users to be disabled/enabled

* Stored Data:
    * No data is stored in the system
    
### 1.6. Other Relevant Remarks

* No relevant remarks.
