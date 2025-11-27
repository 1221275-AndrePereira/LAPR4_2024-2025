# US230: Register Show Request - Tests and Implementation

## 1. User Story Description

**US230: Show request**
As a CRM Collaborator, I want to register (create) a show request.

This functionality allows a CRM Collaborator to input the details of a new show request into the system. This includes information such as the show's description, location (place), creation and show dates, duration, and the number of drones required. The system also associates the request with the customer making it.

## 2. Implementation Details

### 2.1. Application Layer

* **`RegisterShowRequestController.java`**: This is the primary controller responsible for handling the logic of registering a new show request.
  * It receives the necessary data (description, place, creation and show calendar dates, duration in minutes, number of drones, and the `ShodroneUser` representing the customer) from the UI layer.
  * It performs data validation, creates domain value objects, instantiates a new `ShowRequest` aggregate, and uses the `ShowRequestRepository` to persist it.
  * It includes a check (`isCustomer`) to ensure the `ShodroneUser` has the `CRM_COLLABORATOR` role.

  **Key Method: `registerShowRequest`**
    ```java
    // From: implementations from the project/showrequest/application/RegisterShowRequestController.java
    public ShowRequest registerShowRequest(
            final String descriptionText,
            final String placeText,
            final Calendar requestCreateDateTime,
            final Calendar requestShowDateTime,
            final int durationMinutes,
            final int numberOfDrones,
            final ShodroneUser shodroneUser
        )
    {
        //data validation:
        if (descriptionText == null || descriptionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (placeText == null || placeText.trim().isEmpty()) {
            throw new IllegalArgumentException("Place cannot be null or empty.");
        }
        if (requestShowDateTime.before(requestCreateDateTime)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }
        if (numberOfDrones <= 0) {
            throw new IllegalArgumentException("Number of drones must be greater than zero.");
        }
        if (shodroneUser == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        final Description description = Description.valueOf(descriptionText);
        final RequestPlace requestPlace = RequestPlace.valueOf(placeText);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formattedCreateDate = dateFormat.format(requestCreateDateTime.getTime());
        final RequestCreationDate requestCreateDate = RequestCreationDate.valueOf(formattedCreateDate);

        String formattedShowDate = dateFormat.format(requestShowDateTime.getTime());
        final RequestShowDate requestShowDate = RequestShowDate.valueOf(formattedShowDate);

        final RequestDuration requestDuration = RequestDuration.valueOf(durationMinutes);
        final RequestNDrones requestNdDrones = RequestNDrones.valueOf(numberOfDrones);


        final ShowRequest newShowRequest = new ShowRequest(
                description,
                requestPlace,
                requestCreateDate,
                requestShowDate,
                requestDuration,
                requestNdDrones,
                shodroneUser
        );

        return isCustomer(shodroneUser) ? this.showRequestRepository.save(newShowRequest) : null;
    }
    ```

  **Helper Method: `isCustomer`**
    ```java
    // From: implementations from the project/showrequest/application/RegisterShowRequestController.java
    public boolean isCustomer(ShodroneUser shodroneUser) {
        try {
            return shodroneUser.systemUser().hasAny(ShodroneRoles.CRM_COLLABORATOR);
        } catch (Exception e) {
            log.error("Error checking if user is a customer: {}", e.getMessage());
            return false;
        }
    }
    ```

### 2.2. Domain Layer

The core domain entity for this user story is `ShowRequest.java`, which aggregates several value objects:

* **`ShowRequest.java`**: Represents the show request itself.
  * Contains attributes like `id`, `version`, `description` (Framework's `Description`), `requestPlace`, `requestCreateDate`, `requestShowDate`, `requestDuration`, `requestNdDrones`, and the `customer` (`ShodroneUser`).
  * The constructor performs precondition checks.

  **Constructor Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/ShowRequest.java
    public ShowRequest(
            Description description,
            RequestPlace requestPlace,
            RequestCreationDate requestCreateDate,
            RequestShowDate requestShowDate,
            RequestDuration requestDuration,
            RequestNDrones requestNdDrones,
            ShodroneUser customer
    ) {

        Preconditions.noneNull(
                description,
                requestPlace,
                requestCreateDate,
                requestShowDate,
                requestDuration,
                requestNdDrones
                // customer can be null if not explicitly required by preconditions here
                // but controller logic might enforce it.
        );

        this.description = description;
        this.requestPlace = requestPlace;
        this.requestCreateDate = requestCreateDate;
        this.requestShowDate = requestShowDate;
        this.requestDuration = requestDuration;
        this.requestNdDrones = requestNdDrones;
        this.customer = customer;
    }
    ```

* **`RequestPlace.java`**: Value object for the location.
  **Factory Method `valueOf` Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/RequestPlace.java
    public static RequestPlace valueOf(final String description) {
        return new RequestPlace(description);
    }

    private RequestPlace(final String description) {
        Preconditions.ensure(StringPredicates.isPhrase(description),
                "RequestPlace description should not be empty or have leading/trailing spaces.");
        // Add more specific validations if needed (e.g., length, specific format)
        this.placeDescription = description;
    }
    ```

* **`RequestCreationDate.java`**: Value object for the creation date.
  **Factory Method `valueOf` (String) Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/RequestCreationDate.java
    public static RequestCreationDate valueOf(final String dateString) {
        Preconditions.nonEmpty(dateString, "Date string cannot be empty");
        try {
            return new RequestCreationDate(LocalDate.parse(dateString));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for RequestCreationDate: " + dateString, e);
        }
    }
    ```

* **`RequestShowDate.java`**: Value object for the show date.
  **Factory Method `valueOf` (Calendar) Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/RequestShowDate.java
    public static RequestShowDate valueOf(final Calendar date) {
        Preconditions.nonNull(date, "Date cannot be null");
        return new RequestShowDate(date.toInstant().atZone(date.getTimeZone().toZoneId()).toLocalDate());
    }
    ```

* **`RequestDuration.java`**: Value object for the duration in minutes.
  **Factory Method `valueOf` Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/RequestDuration.java
    public static RequestDuration valueOf(int durationMinutes) {
        return new RequestDuration(durationMinutes);
    }

    private RequestDuration(final long minutes) {
        Preconditions.ensure(minutes > 0, "RequestDuration must be positive");
        this.durationInMinutes = minutes;
    }
    ```

* **`RequestNDrones.java`**: Value object for the number of drones.
  **Factory Method `valueOf` Snippet:**
    ```java
    // From: implementations from the project/showrequest/domain/RequestNDrones.java
    public static RequestNDrones valueOf(final int number) {
        return new RequestNDrones(number);
    }

    private RequestNDrones(final int number) {
        Preconditions.ensure(number > 0, "Number of drones must be positive");
        this.numberOfDrones = number;
    }
    ```

* **`ShodroneUser.java`**: Represents the customer. (Details in user management domain).

### 2.3. Repository Layer

* **`ShowRequestRepository.java`**: Interface defining the contract for persistence operations.
    ```java
    // From: implementations from the project/showrequest/repositories/ShowRequestRepository.java
    package eapli.shodrone.showrequest.repositories;

    import eapli.framework.domain.repositories.DomainRepository;
    import eapli.framework.general.domain.model.Description;
    import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
    import eapli.shodrone.showrequest.domain.ShowRequest;
    // ... other imports
    import java.util.Optional; // Added based on interface content

    public interface ShowRequestRepository extends DomainRepository<Long, ShowRequest> {

        Optional<ShowRequest> findByDescription(Description description);

        Iterable<ShowRequest> obtainAllShowRequests();

        Iterable<ShowRequest> findByCustomer(String customer); // This might be an email or username string

        Iterable<ShowRequest> findByCustomer(ShodroneUser customer);
    }
    ```
  The `save(ShowRequest showRequest)` method is inherited from `DomainRepository`.

## 3. Test Details

### 3.1. Application Layer Tests

* **`RegisterShowRequestControllerTest.java`**: Contains unit tests for the `RegisterShowRequestController`.
  **`registerShowRequest()` Test Method Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/application/RegisterShowRequestControllerTest.java
    @Test
    void registerShowRequest() {
        // Arrange
        String description = "Original Description";
        String place = "Original Place";
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance(); // Should be after startDate in a real scenario
        endDate.add(Calendar.DAY_OF_MONTH, 1); // Ensure endDate is after startDate
        int duration = 120;
        int numberOfDrones = 300;

        // Mocking SystemUser for ShodroneUser
        SystemUser mockSystemUser = mock(SystemUser.class);
        when(testUser.systemUser()).thenReturn(mockSystemUser);
        when(mockSystemUser.hasAny(any(Role[].class))).thenReturn(true); // Assume user is a customer

        // Act
        ShowRequest req = controller.registerShowRequest(
                description,
                place,
                startDate,
                endDate,
                duration,
                numberOfDrones,
                testUser
        );

        // Assert
        assertNotNull(req); // Ensure the returned request is not null
        assertEquals(description, req.getDescription().toString());
        assertEquals(place, req.getPlace().toString());
        assertEquals(duration, req.getDuration().minutes());
        assertEquals(numberOfDrones, req.getNdDrones().number());
        assertEquals(testUser, req.getCustomer());

        verify(showRequestRepository, times(1)).save(req);
    }
    ```
    *Unfortunately, it was not possible to write well made unit tests for this controller.*

### 3.2. Domain Layer Tests

* **`ShowRequestTest.java`**: Tests the `ShowRequest` aggregate.
  **Valid Construction Test Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/domain/ShowRequestTest.java
    @Test
    void testShowRequest_validConstruction() {
        ShowRequest showRequest = createValidShowRequest(); // Helper method to create a valid instance

        assertNotNull(showRequest);
        assertEquals(description, showRequest.getDescription());
        assertEquals(requestPlace, showRequest.getPlace());
        // ... more assertions for other fields
    }
    ```
  **Null Precondition Test Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/domain/ShowRequestTest.java
    @Test
    void testShowRequest_nullDescription_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
                null, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockShodroneUser
        ));
    }
    ```

* **`RequestPlaceTest.java`**:
  **Invalid Input Test (Empty String) Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/domain/RequestPlaceTest.java
    @Test
    void testValueOfWithEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> RequestPlace.valueOf(""));
    }
    ```

* **`RequestDurationTest.java`**:
  **Negative Duration Test Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/domain/RequestDurationTest.java
    @Test
    void testValueOf_negativeMinutes_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestDuration.valueOf(-30));
    }
    ```

* **`RequestNDronesTest.java`**:
  **Zero Drones Test Snippet:**
    ```java
    // From: implementations from the project/test/showrequest/domain/RequestNDronesTest.java
    @Test
    void testValueOfWithZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RequestNDrones.valueOf(0));
    }
    ```
  (Similar tests exist for `RequestCreationDateTest.java` and `RequestShowDateTest.java` covering various instantiation scenarios and precondition checks.)

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
