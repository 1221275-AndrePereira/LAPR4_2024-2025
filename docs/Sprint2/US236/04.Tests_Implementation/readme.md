# US236: Edit Show Request - Tests and Implementation

## 1. User Story Description

**US236: Edit show requests**
As CRM Collaborator, I want to edit a show request of a client. Only show requests without a proposal can be edited.

This functionality allows a CRM Collaborator to modify the details of an existing show request. The collaborator first identifies the show request by its ID. They can then update attributes such as the description, place, show date, duration, number of drones, and optionally, change the associated customer. The system enforces that only show requests that do not yet have a proposal can be edited (this specific business rule regarding "without a proposal" is noted in the requirement, though its direct enforcement isn't explicitly detailed in the provided `EditShowRequestController` or `EditShowRequestUI` snippets, which would typically be handled by business logic or state checks before allowing the edit operation).

## 2. Implementation Details

### 2.1. Presentation Layer (UI)

* **`EditShowRequestUI.java`**: This class manages the user interface for editing a show request.
  * The user is first prompted to enter the ID of the show request they wish to edit.
  * The `FindShowRequestController.findShowRequestById()` method is used to retrieve the specified `ShowRequest`. If not found, an error is displayed.
  * If found, the user is prompted to enter new values for:
    * Show Description
    * Show Place
    * Show Date (validated to not be in the past)
    * Show Duration (validated to be a positive integer)
    * Number of Drones (validated to be a positive integer)
  * The UI then asks if the user wants to change the customer associated with the show request.
    * If yes, it uses `ListShodroneUsersController` to list available users (filtered for `CRM_COLLABORATOR` role) via a `SelectWidget` for the CRM Collaborator to choose a new customer.
    * If no, or if no new customer is selected, the original customer remains associated.
  * Finally, the `EditShowRequestController.editShowRequest()` method is called with the original `ShowRequest` object and all the new (or unchanged) values.
  * Success or error messages are displayed to the user.

  **UI Flow Snippet in `doShow()`:**
    ```java
    // From: implementations from the project/presentation/showrequest/EditShowRequestUI.java
    Long showRequestId;
    // ... (read showRequestId)

    ShowRequest showRequest = findController.findShowRequestById(showRequestId);
    if (showRequest == null) {
        // ... (handle not found) return false;
    }

    final String newDescription = Console.readNonEmptyLine("Show Description", "...");
    final String requestPlace = Console.readNonEmptyLine("Show Place", "...");
    // ... (read newRequestShowDate with past date validation)
    // ... (read newRequestDuration with positive integer validation)
    // ... (read newRequestNdDrones with positive integer validation)

    final boolean changeCustomer = Console.readBoolean("Do you want to change the customer? (y/n)");
    ShodroneUser targetCustomer = showRequest.getCustomer(); // Default to original

    if (changeCustomer) {
        final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
        // ... (filter for CRM_COLLABORATOR and select user logic using SelectWidget)
        ShodroneUser selectedUser = userSelector.selectedElement();
        if (selectedUser != null) {
            targetCustomer = selectedUser;
        }
    }

    try {
        ShowRequest editedRequest = controller.editShowRequest(
                showRequest,
                newDescription,
                newRequestShowDate, // Calendar object
                (long) newRequestDuration, // Cast to long for controller
                requestPlace,
                newRequestNdDrones,
                targetCustomer
        );
        System.out.println("Show request edited successfully.");
        System.out.println(editedRequest);
    } catch (final IllegalArgumentException | IllegalStateException e) {
        log.error("Error editing show request: {}", e.getMessage());
    }
    return false;
    ```

### 2.2. Application Layer

* **`FindShowRequestController.java`**: Used by the UI to fetch the `ShowRequest` to be edited.
  * **`findShowRequestById(Long id)`**: Retrieves a `ShowRequest` based on its unique identifier. It performs authorization checks (user must have `CRM_MANAGER` or `ADMIN` roles as per this controller's implementation).

* **`EditShowRequestController.java`**: This controller handles the core logic of modifying an existing show request.
  * **`editShowRequest(...)`**:
    * Takes the existing `ShowRequest` object, and new values for its attributes (description, show date, duration, place, number of drones, and customer) as input.
    * Performs authorization, ensuring the authenticated user has `ShodroneRoles.POWER_USER`, `ShodroneRoles.ADMIN`, or `ShodroneRoles.CRM_MANAGER` roles.
    * Validates that input parameters (like the show request object itself, new description, duration, place, number of drones, and customer) are not null or invalid (e.g., empty strings, non-positive numbers).
    * Converts the input strings/primitives into the appropriate domain Value Objects (`Description`, `RequestPlace`, `RequestShowDate`, `RequestDuration`, `RequestNDrones`).
    * Calls the update methods on the passed `ShowRequest` aggregate (e.g., `showRequest.updateShowDescription(...)`, `showRequest.updateShowDate(...)`, etc.).
    * Persists the modified `ShowRequest` aggregate using the `ShowRequestRepository.save()` method.

  **Key Method: `editShowRequest`**
    ```java
    // From: implementations from the project/showrequest/application/EditShowRequestController.java
    public ShowRequest editShowRequest(
            final ShowRequest showRequest,
            final String newShowDescription,
            final Calendar newShowDate, // Calendar
            final long newShowDuration,
            final String newShowPlace,
            final int newShowNDrones,
            final ShodroneUser newCustomer // Renamed for clarity from newCusomer
    ) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);

        // parse the new values
        final Description showDescription = Description.valueOf(newShowDescription);
        final RequestPlace showPlace = RequestPlace.valueOf(newShowPlace);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedShowDate = dateFormat.format(newShowDate.getTime());
        final RequestShowDate showDate = RequestShowDate.valueOf(formattedShowDate);

        final RequestDuration showDuration = RequestDuration.valueOf(Math.toIntExact(newShowDuration));
        final RequestNDrones showNdDrones = RequestNDrones.valueOf(newShowNDrones);

        // check if the show request values are valid (includes null checks on parameters)
        if (
            showRequest == null ||
            showDescription == null ||
            showDescription.toString().trim().isEmpty() ||
            showDuration.minutes() <= 0 || // Validation done during VO creation as well
            showPlace.toString().trim().isEmpty() || // Validation done during VO creation as well
            showNdDrones.number() <= 0 || // Validation done during VO creation as well
            newCustomer == null
        ) {
            throw new IllegalArgumentException("Invalid input parameters for editing show request.");
        }

        // update the show request with the new values
        showRequest.updateShowDescription(showDescription);
        showRequest.updateShowDate(showDate);
        showRequest.updateShowDuration(showDuration);
        showRequest.updateShowPlace(showPlace);
        showRequest.updateShowNDrones(showNdDrones);
        showRequest.updateShowCustomer(newCustomer);

        return showRequestRepository.save(showRequest);
    }
    ```

* **`ListShodroneUsersController.java`**: Used by the UI to list potential customers if the CRM Collaborator chooses to change the customer associated with the show request.

### 2.3. Domain Layer

* **`ShowRequest.java`**: The aggregate root for a show request. It contains the actual data and business logic for updating its state.
  * **Update Methods**: `updateShowDescription(Description)`, `updateShowDate(RequestShowDate)`, `updateShowDuration(RequestDuration)`, `updateShowPlace(RequestPlace)`, `updateShowNDrones(RequestNDrones)`, `updateShowCustomer(ShodroneUser)`. These methods are called by the `EditShowRequestController` to modify the entity's attributes.
* Value Objects (`Description`, `RequestPlace`, `RequestShowDate`, `RequestDuration`, `RequestNDrones`): Used to encapsulate and validate the new attribute values before they are applied to the `ShowRequest` entity.
* **`ShodroneUser.java`**: Represents the customer. The reference to the customer on the `ShowRequest` can be updated.

  **Example Update Method in `ShowRequest.java`:**
    ```java
    // From: implementations from the project/showrequest/domain/ShowRequest.java
    public void updateShowDescription(Description newDescription) {
        // Preconditions.nonNull(newDescription); // Could be added for robustness
        this.description = newDescription;
    }

    public void updateShowDate(RequestShowDate newDate) {
        this.requestShowDate = newDate;
    }
    // ... other update methods
    ```

### 2.4. Repository Layer

* **`ShowRequestRepository.java`**: This interface defines the contract for data persistence operations.
  * **`ofIdentity(Long id)`**: Used by `FindShowRequestController` to fetch the `ShowRequest` to be edited.
  * **`save(ShowRequest showRequest)`**: Inherited from `DomainRepository`. This method is used by `EditShowRequestController` to persist the changes made to the `ShowRequest` aggregate. JPA or other ORM mechanisms typically handle whether this is an insert or an update based on the entity's persistence state.

## 3. Test Details

### 3.1. Application Layer Tests

* **`EditShowRequestControllerTest.java`**: This class contains unit tests for the `EditShowRequestController`.
  * **Setup (`setUp()` method)**:
    * Mocks `ShowRequestRepository` and `AuthorizationService`.
    * Creates an instance of `EditShowRequestController` with the mocked dependencies.
    * Prepares an original `ShowRequest` object and a `ShodroneUser` (mocked).
    * Configures the mock `showRequestRepository.save()` to return the argument it receives (simulating a successful save and update).
  * **`editShowRequestTest()` method**:
    * **Arrange**: Defines new values for description, date, duration, place, number of drones.
    * **Act**: Calls the `editController.editShowRequest()` method with the original `ShowRequest` and the new values.
    * **Assert**:
      * Verifies that the returned `ShowRequest` (the edited one) is not null.
      * Verifies that `showRequestRepository.save()` was called once with the `ShowRequest` object.
      * Asserts that the properties of the edited `ShowRequest` have been updated to the new values and are different from the original values where changes were made.
      * Asserts that properties not intended to change (like the customer, if not explicitly changed in a test variant) remain the same or are updated as expected.

  **`editShowRequestTest()` Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/application/EditShowRequestControllerTest.java
    @Test
    void editShowRequestTest() {
        // Store original values for comparison (example)
        String originalDescriptionStr = originalRequest.getDescription().toString();

        // New values
        String newDesc = "New Description";
        Calendar newDate = Calendar.getInstance(); // Today, for simplicity in test
        newDate.add(Calendar.YEAR, 1); // Future date
        long newDuration = 120;
        String newPlace = "New Place";
        int newNDrones = 128;
        // ShodroneUser newCustomer = mock(ShodroneUser.class); // If testing customer change

        // Perform edit
        ShowRequest editedRequest = editController.editShowRequest(
                originalRequest,
                newDesc,
                newDate,
                newDuration,
                newPlace,
                newNDrones,
                testUser // Using original testUser for this snippet, can be newCustomer
        );

        assertNotNull(editedRequest);
        verify(showRequestRepository).save(any(ShowRequest.class)); // or save(originalRequest)

        assertEquals(newDesc, editedRequest.getDescription().toString());
        assertEquals(newPlace, editedRequest.getPlace().toString());
        assertEquals(newDuration, editedRequest.getDuration().minutes());
        assertEquals(newNDrones, editedRequest.getNdDrones().number());
        // Add assertion for date and customer if necessary
        assertNotEquals(originalDescriptionStr, editedRequest.getDescription().toString());
    }
    ```

### 3.2. Domain Layer Tests

* **`ShowRequestTest.java`**: Provides crucial unit tests for the `ShowRequest` aggregate's update methods:
  * `testUpdateShowDescription()`: Verifies `updateShowDescription` correctly changes the description.
  * `testUpdateShowDate()`: Verifies `updateShowDate` correctly changes the show date.
  * `testUpdateShowDuration()`: Verifies `updateShowDuration` correctly changes the duration.
  * `testUpdateShowPlace()`: Verifies `updateShowPlace` correctly changes the place.
  * `testUpdateShowNDrones()`: Verifies `updateShowNDrones` correctly changes the number of drones.
  * A test for `updateShowCustomer()` would also be relevant here if customer change is a core part of the domain update logic tested directly on the entity.
    These tests ensure that the internal state of the `ShowRequest` object is correctly modified by its own methods.

* **Value Object Tests** (e.g., `RequestDurationTest.java`, `RequestPlaceTest.java`, etc.): Ensure that new values provided during the edit process are correctly encapsulated and validated by their respective value objects before being set on the `ShowRequest` entity. For example, `RequestDuration.valueOf(newDuration)` will throw an exception if `newDuration` is not positive.

### 3.3. Presentation Layer (UI) Tests (Conceptual)

* Direct unit testing of `EditShowRequestUI.java` is complex due to its reliance on `eapli.framework.io.util.Console` and interaction with multiple controllers.
* Testing could focus on:
  * **Input Validation Logic**: If any complex validation logic (beyond what `Console` provides) exists within the UI class itself (though most seems delegated to `Console` or controller/VOs), it could be refactored into testable methods.
  * **Controller Interaction**: Ensuring the correct controller methods are called with the right parameters, possibly by using a test double for the controllers if the UI class's structure allows for dependency injection or a way to substitute controllers during tests.
  * **Integration/End-to-End Tests**: More comprehensive tests would cover the flow from UI input to database update, but these are typically outside the scope of "unit tests" for the UI class itself.

## 4. Dependencies (Maven Project)

The following are key dependencies typically found in the `pom.xml` for a project with this structure:

* **Java Development Kit (JDK)** (Specified in Maven compiler plugin)
* **JUnit 5 (Jupiter)**: For writing and running unit tests.
    ```xml
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.0</version> <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.0</version> <scope>test</scope>
    </dependency>
    ```
* **Mockito**: For creating mock objects.
    ```xml
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version> <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.5.0</version> <scope>test</scope>
    </dependency>
    ```
* **eapli.framework**: The core application framework. (Assuming it's available in a Maven repository or as a local dependency).
    ```xml
    <dependency>
        <groupId>eapli</groupId> <artifactId>eapli.framework.core</artifactId> <version>YOUR_EAPLI_FRAMEWORK_VERSION</version>
    </dependency>
    ```
* **Project-specific domain classes**: These are part of the same project or multi-module Maven setup.
* **Lombok**: (Optional, if used for boilerplate code reduction).
    ```xml
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version> <scope>provided</scope>
    </dependency>
    ```
* **Jakarta Persistence API (JPA)**: If using JPA for persistence.
    ```xml
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>3.1.0</version> </dependency>
    ```

(Note: Version numbers shown are examples and should be replaced with the versions actually used in the project.)# US236: Edit Show Request - Tests and Implementation

## 1. User Story Description

**US236: Edit show requests**
As CRM Collaborator, I want to edit a show request of a client. Only show requests without a proposal can be edited.

This functionality allows a CRM Collaborator to modify the details of an existing show request. The collaborator first identifies the show request by its ID. They can then update attributes such as the description, place, show date, duration, number of drones, and optionally, change the associated customer. The system enforces that only show requests that do not yet have a proposal can be edited (this specific business rule regarding "without a proposal" is noted in the requirement, though its direct enforcement isn't explicitly detailed in the provided `EditShowRequestController` or `EditShowRequestUI` snippets, which would typically be handled by business logic or state checks before allowing the edit operation).

## 2. Implementation Details

### 2.1. Presentation Layer (UI)

* **`EditShowRequestUI.java`**: This class manages the user interface for editing a show request.
  * The user is first prompted to enter the ID of the show request they wish to edit.
  * The `FindShowRequestController.findShowRequestById()` method is used to retrieve the specified `ShowRequest`. If not found, an error is displayed.
  * If found, the user is prompted to enter new values for:
    * Show Description
    * Show Place
    * Show Date (validated to not be in the past)
    * Show Duration (validated to be a positive integer)
    * Number of Drones (validated to be a positive integer)
  * The UI then asks if the user wants to change the customer associated with the show request.
    * If yes, it uses `ListShodroneUsersController` to list available users (filtered for `CRM_COLLABORATOR` role) via a `SelectWidget` for the CRM Collaborator to choose a new customer.
    * If no, or if no new customer is selected, the original customer remains associated.
  * Finally, the `EditShowRequestController.editShowRequest()` method is called with the original `ShowRequest` object and all the new (or unchanged) values.
  * Success or error messages are displayed to the user.

  **UI Flow Snippet in `doShow()`:**
    ```java
    // From: implementations from the project/presentation/showrequest/EditShowRequestUI.java
    Long showRequestId;
    // ... (read showRequestId)

    ShowRequest showRequest = findController.findShowRequestById(showRequestId);
    if (showRequest == null) {
        // ... (handle not found) return false;
    }

    final String newDescription = Console.readNonEmptyLine("Show Description", "...");
    final String requestPlace = Console.readNonEmptyLine("Show Place", "...");
    // ... (read newRequestShowDate with past date validation)
    // ... (read newRequestDuration with positive integer validation)
    // ... (read newRequestNdDrones with positive integer validation)

    final boolean changeCustomer = Console.readBoolean("Do you want to change the customer? (y/n)");
    ShodroneUser targetCustomer = showRequest.getCustomer(); // Default to original

    if (changeCustomer) {
        final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
        // ... (filter for CRM_COLLABORATOR and select user logic using SelectWidget)
        ShodroneUser selectedUser = userSelector.selectedElement();
        if (selectedUser != null) {
            targetCustomer = selectedUser;
        }
    }

    try {
        ShowRequest editedRequest = controller.editShowRequest(
                showRequest,
                newDescription,
                newRequestShowDate, // Calendar object
                (long) newRequestDuration, // Cast to long for controller
                requestPlace,
                newRequestNdDrones,
                targetCustomer
        );
        System.out.println("Show request edited successfully.");
        System.out.println(editedRequest);
    } catch (final IllegalArgumentException | IllegalStateException e) {
        log.error("Error editing show request: {}", e.getMessage());
    }
    return false;
    ```

### 2.2. Application Layer

* **`FindShowRequestController.java`**: Used by the UI to fetch the `ShowRequest` to be edited.
  * **`findShowRequestById(Long id)`**: Retrieves a `ShowRequest` based on its unique identifier. It performs authorization checks (user must have `CRM_MANAGER` or `ADMIN` roles as per this controller's implementation).

* **`EditShowRequestController.java`**: This controller handles the core logic of modifying an existing show request.
  * **`editShowRequest(...)`**:
    * Takes the existing `ShowRequest` object, and new values for its attributes (description, show date, duration, place, number of drones, and customer) as input.
    * Performs authorization, ensuring the authenticated user has `ShodroneRoles.POWER_USER`, `ShodroneRoles.ADMIN`, or `ShodroneRoles.CRM_MANAGER` roles.
    * Validates that input parameters (like the show request object itself, new description, duration, place, number of drones, and customer) are not null or invalid (e.g., empty strings, non-positive numbers).
    * Converts the input strings/primitives into the appropriate domain Value Objects (`Description`, `RequestPlace`, `RequestShowDate`, `RequestDuration`, `RequestNDrones`).
    * Calls the update methods on the passed `ShowRequest` aggregate (e.g., `showRequest.updateShowDescription(...)`, `showRequest.updateShowDate(...)`, etc.).
    * Persists the modified `ShowRequest` aggregate using the `ShowRequestRepository.save()` method.

  **Key Method: `editShowRequest`**
    ```java
    // From: implementations from the project/showrequest/application/EditShowRequestController.java
    public ShowRequest editShowRequest(
            final ShowRequest showRequest,
            final String newShowDescription,
            final Calendar newShowDate, // Calendar
            final long newShowDuration,
            final String newShowPlace,
            final int newShowNDrones,
            final ShodroneUser newCustomer // Renamed for clarity from newCusomer
    ) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_MANAGER);

        // parse the new values
        final Description showDescription = Description.valueOf(newShowDescription);
        final RequestPlace showPlace = RequestPlace.valueOf(newShowPlace);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedShowDate = dateFormat.format(newShowDate.getTime());
        final RequestShowDate showDate = RequestShowDate.valueOf(formattedShowDate);

        final RequestDuration showDuration = RequestDuration.valueOf(Math.toIntExact(newShowDuration));
        final RequestNDrones showNdDrones = RequestNDrones.valueOf(newShowNDrones);

        // check if the show request values are valid (includes null checks on parameters)
        if (
            showRequest == null ||
            showDescription == null ||
            showDescription.toString().trim().isEmpty() ||
            showDuration.minutes() <= 0 || // Validation done during VO creation as well
            showPlace.toString().trim().isEmpty() || // Validation done during VO creation as well
            showNdDrones.number() <= 0 || // Validation done during VO creation as well
            newCustomer == null
        ) {
            throw new IllegalArgumentException("Invalid input parameters for editing show request.");
        }

        // update the show request with the new values
        showRequest.updateShowDescription(showDescription);
        showRequest.updateShowDate(showDate);
        showRequest.updateShowDuration(showDuration);
        showRequest.updateShowPlace(showPlace);
        showRequest.updateShowNDrones(showNdDrones);
        showRequest.updateShowCustomer(newCustomer);

        return showRequestRepository.save(showRequest);
    }
    ```

* **`ListShodroneUsersController.java`**: Used by the UI to list potential customers if the CRM Collaborator chooses to change the customer associated with the show request.

### 2.3. Domain Layer

* **`ShowRequest.java`**: The aggregate root for a show request. It contains the actual data and business logic for updating its state.
  * **Update Methods**: `updateShowDescription(Description)`, `updateShowDate(RequestShowDate)`, `updateShowDuration(RequestDuration)`, `updateShowPlace(RequestPlace)`, `updateShowNDrones(RequestNDrones)`, `updateShowCustomer(ShodroneUser)`. These methods are called by the `EditShowRequestController` to modify the entity's attributes.
* Value Objects (`Description`, `RequestPlace`, `RequestShowDate`, `RequestDuration`, `RequestNDrones`): Used to encapsulate and validate the new attribute values before they are applied to the `ShowRequest` entity.
* **`ShodroneUser.java`**: Represents the customer. The reference to the customer on the `ShowRequest` can be updated.

  **Example Update Method in `ShowRequest.java`:**
    ```java
    // From: implementations from the project/showrequest/domain/ShowRequest.java
    public void updateShowDescription(Description newDescription) {
        // Preconditions.nonNull(newDescription); // Could be added for robustness
        this.description = newDescription;
    }

    public void updateShowDate(RequestShowDate newDate) {
        this.requestShowDate = newDate;
    }
    // ... other update methods
    ```

### 2.4. Repository Layer

* **`ShowRequestRepository.java`**: This interface defines the contract for data persistence operations.
  * **`ofIdentity(Long id)`**: Used by `FindShowRequestController` to fetch the `ShowRequest` to be edited.
  * **`save(ShowRequest showRequest)`**: Inherited from `DomainRepository`. This method is used by `EditShowRequestController` to persist the changes made to the `ShowRequest` aggregate. JPA or other ORM mechanisms typically handle whether this is an insert or an update based on the entity's persistence state.

## 3. Test Details

### 3.1. Application Layer Tests

* **`EditShowRequestControllerTest.java`**: This class contains unit tests for the `EditShowRequestController`.
  * **Setup (`setUp()` method)**:
    * Mocks `ShowRequestRepository` and `AuthorizationService`.
    * Creates an instance of `EditShowRequestController` with the mocked dependencies.
    * Prepares an original `ShowRequest` object and a `ShodroneUser` (mocked).
    * Configures the mock `showRequestRepository.save()` to return the argument it receives (simulating a successful save and update).
  * **`editShowRequestTest()` method**:
    * **Arrange**: Defines new values for description, date, duration, place, number of drones.
    * **Act**: Calls the `editController.editShowRequest()` method with the original `ShowRequest` and the new values.
    * **Assert**:
      * Verifies that the returned `ShowRequest` (the edited one) is not null.
      * Verifies that `showRequestRepository.save()` was called once with the `ShowRequest` object.
      * Asserts that the properties of the edited `ShowRequest` have been updated to the new values and are different from the original values where changes were made.
      * Asserts that properties not intended to change (like the customer, if not explicitly changed in a test variant) remain the same or are updated as expected.

  **`editShowRequestTest()` Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/application/EditShowRequestControllerTest.java
    @Test
    void editShowRequestTest() {
        // Store original values for comparison (example)
        String originalDescriptionStr = originalRequest.getDescription().toString();

        // New values
        String newDesc = "New Description";
        Calendar newDate = Calendar.getInstance(); // Today, for simplicity in test
        newDate.add(Calendar.YEAR, 1); // Future date
        long newDuration = 120;
        String newPlace = "New Place";
        int newNDrones = 128;
        // ShodroneUser newCustomer = mock(ShodroneUser.class); // If testing customer change

        // Perform edit
        ShowRequest editedRequest = editController.editShowRequest(
                originalRequest,
                newDesc,
                newDate,
                newDuration,
                newPlace,
                newNDrones,
                testUser // Using original testUser for this snippet, can be newCustomer
        );

        assertNotNull(editedRequest);
        verify(showRequestRepository).save(any(ShowRequest.class)); // or save(originalRequest)

        assertEquals(newDesc, editedRequest.getDescription().toString());
        assertEquals(newPlace, editedRequest.getPlace().toString());
        assertEquals(newDuration, editedRequest.getDuration().minutes());
        assertEquals(newNDrones, editedRequest.getNdDrones().number());
        // Add assertion for date and customer if necessary
        assertNotEquals(originalDescriptionStr, editedRequest.getDescription().toString());
    }
    ```

### 3.2. Domain Layer Tests

* **`ShowRequestTest.java`**: Provides crucial unit tests for the `ShowRequest` aggregate's update methods:
  * `testUpdateShowDescription()`: Verifies `updateShowDescription` correctly changes the description.
  * `testUpdateShowDate()`: Verifies `updateShowDate` correctly changes the show date.
  * `testUpdateShowDuration()`: Verifies `updateShowDuration` correctly changes the duration.
  * `testUpdateShowPlace()`: Verifies `updateShowPlace` correctly changes the place.
  * `testUpdateShowNDrones()`: Verifies `updateShowNDrones` correctly changes the number of drones.
  * A test for `updateShowCustomer()` would also be relevant here if customer change is a core part of the domain update logic tested directly on the entity.
    These tests ensure that the internal state of the `ShowRequest` object is correctly modified by its own methods.

* **Value Object Tests** (e.g., `RequestDurationTest.java`, `RequestPlaceTest.java`, etc.): Ensure that new values provided during the edit process are correctly encapsulated and validated by their respective value objects before being set on the `ShowRequest` entity. For example, `RequestDuration.valueOf(newDuration)` will throw an exception if `newDuration` is not positive.

## 4. Dependencies (Maven Project)

The following are key dependencies typically found in the root or package `pom.xml` with this structure:

* **Java Development Kit (JDK)** (Specified in Maven compiler plugin)
* **JUnit 5 (Jupiter)**: For writing and running unit tests.
    ```xml
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    ```
* **Mockito**: For creating mock objects.
    ```xml
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>4.11.0</version>
        <scope>test</scope>
    </dependency>
    ```
* **eapli.framework**: The core application framework.
    ```xml
    <dependency>
        <groupId>org.bitbucket.pag_isep.eapliframework</groupId>
        <artifactId>eapli.framework.core</artifactId>
        <version>${eapli.framework.core.version}</version>
    </dependency>
    <dependency>
        <groupId>org.bitbucket.pag_isep.eapliframework</groupId>
        <artifactId>eapli.framework.infrastructure.authz</artifactId>
        <version>${eapli.framework.authz.version}</version>
    </dependency>
    <dependency>
        <groupId>org.bitbucket.pag_isep.eapliframework</groupId>
        <artifactId>eapli.framework.infrastructure.pubsub</artifactId>
        <version>${eapli.framework.pubsub.version}</version>
    </dependency>
    ```
* **Project-specific domain classes**: These are part of the same project or multi-module Maven setup.
  * **Lombok**: (Optional, if used for boilerplate code reduction).
      ```xml
      <dependency>
          <groupId>org.projectlombok</groupId>
          <artifactId>lombok</artifactId>
          <version>1.18.38</version>
          <scope>provided</scope>
      </dependency>
      ```
  * **Jakarta Persistence API (JPA)**: If using JPA for persistence.
      ```xml
      <dependency>
          <groupId>jakarta.xml.bind</groupId>
          <artifactId>jakarta.xml.bind-api</artifactId>
          <version>4.0.1</version>
      </dependency>
      ```
