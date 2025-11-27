# US211: Register Users

## 1. Requirements Engineering

### 1.1. User Story Description

* **ID:** US211
* **Title:** Register Users
* **As a:** Administrator
* **I want to:**   be able to register users of the backoffice(including bootstrap initialization)
* **So that:** I can manage user accounts and their roles in the system.

### 1.2. Customer Specifications and Clarifications

**From the specifications document (v02b):**

* Users must have a unique @showdrone.com email (Section 3.1.1)
* System Users Requires username, email, password, and role
* Shodrone Users requires all previous fields plus vat number, address, and phone
* Bootstrap process needed for initial system setup
* Six predefined roles exist (Admin, CRM Manager, etc.)

**From the client clarifications:**

* *(No specific client clarifications were provided for this US. Requirements are derived from the specification document).*

### 1.3. Acceptance Criteria

* A.C.1: Only Admins can register System Users
* A.C.2: Managers can register Shodrone Users
* A.C.3: Username and Email must be unique. Email must end with @showdrone.com domain
* A.C.4: Mandatory fields for System User: username, email, password, role
* A.C.4: Mandatory fields for Shodrone User: vat number, address, phone
* A.C.5: Bootstrap must create at least one Admin user
* A.C.6: Password meets complexity requirements( 7 characters, 1 uppercase, 1 lowercase, 1 number)
* A.C.7: Success/error messages returned

### 1.4. Found out Dependencies

* US211 (Authentication/Authorization) for role validation
* Database persistence (NFR07)

### 1.5. Input and Output Data

**Input Data:**

* From User :
    *  
    * System User:
        * Email (Shodrone domain)
        * Name (first and last name)
        * Role
        * Password
      * Shodrone User:
        * System User
        * Phone Number
        * VAT Number
        * Address( Street, City, Postal Code)
        
* From System:
    * 
        * Hash of the password
        * Salt for password hashing

**Output Data:**

* System Confirmation:
    *
        * Success/Error message

* Stored Data:
    * 
        * Email
        * Name
        * Phone Number
        * Role
        * VAT Number
        * Address
        * Username
        * Hashed Password
        * Salt
    
### 1.6. Other Relevant Remarks

* Password complexity requirements:
    * At least 7 characters
    * At least one uppercase letter
    * At least one lowercase letter
    * At least one number
* Bootstrap process should be automated
