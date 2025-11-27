# US235: List Show Requests of Client - Tests and Implementation

## 1. User Story Description

**US235: List show requests of client**
As CRM Manager or CRM Collaborator, I want to list all show requests of a client. The show request status information should be provided.

This functionality enables authorized CRM personnel (Managers or Collaborators) to select a client and view a comprehensive list of all show requests associated with that particular client. The display includes key details for each request. While the user story mentions "status information," the current UI implementation primarily lists the core attributes of the show requests.

## 2. Implementation Details

### 2.1. Presentation Layer (UI)

* **`ListShowRequestUI.java`**: This class orchestrates the user interaction for listing a client's show requests.
  * It first utilizes the `ListShodroneUsersController` to fetch and display a list of all `ShodroneUser`s.
  * It filters this list to include only users with the `CRM_COLLABORATOR` role (i.e., customers).
  * A `SelectWidget` is employed to allow the CRM staff to choose a specific customer from the filtered list. The `ShodroneUserPrinter` is used to format the display of each customer (Email and VAT).
  * Upon selection of a customer, the UI calls the `findShowRequestsByCustomer(selectedUser)` method of the `FindShowRequestController`.
  * The retrieved list of `ShowRequest` objects is then sorted by their ID (if available and comparable).
  * Finally, it iterates through the sorted list and prints the details of each `ShowRequest` (ID, Description, Place, Duration, Number of Drones, Show Date, Creation Date) to the console.

  **UI Flow Snippet in `doShow()`:**
    ```java
    // From: implementations from the project/presentation/showrequest/ListShowRequestUI.java
    final Iterable<ShodroneUser> usersIterable = this.listShodroneUsersController.listShodroneUsers();
    final List<ShodroneUser> shodroneUserList = StreamSupport.stream(usersIterable.spliterator(), false)
            .collect(Collectors.toList());

    //keep only customers
    shodroneUserList.removeIf(user -> !user.systemUser().hasAny(ShodroneRoles.CRM_COLLABORATOR));

    if (shodroneUserList.isEmpty()) {
        System.out.println("There are no Shodrone users registered in the system.");
        return false;
    }

    System.out.println("Select a Shodrone User to list their Show Requests:");
    final SelectWidget<ShodroneUser> userSelector = new SelectWidget<>("Available Users:", shodroneUserList, new ShodroneUserPrinter());
    userSelector.show();
    final ShodroneUser selectedUser = userSelector.selectedElement();

    if (selectedUser == null) {
        log.info("No user selected. Operation cancelled.");
        return false;
    }
    // ... (logging selected user)

    final List<ShowRequest> userShowRequests = findShowRequestController.findShowRequestsByCustomer(selectedUser);
    if (!userShowRequests.isEmpty() && userShowRequests.getFirst().getId() != null) {
        userShowRequests.sort(Comparator.comparing(ShowRequest::getId));
    } else if (!userShowRequests.isEmpty()) {
        log.warn("ShowRequest ID might not be comparable; List may not be sorted by ID.");
    }

    System.out.println("\n--- Show Requests for User: " + getUserDisplayString(selectedUser) + " ---");
    for (ShowRequest showRequest : userShowRequests) {
        System.out.println(
                "-------------------------------------\n" +
                        "Show Request ID: " + (
                        showRequest.getId() != null ? showRequest.getId().toString() : "N/A"
                ) + "\n" +
                        "Description: " + showRequest.getDescription() + "\n" +
                        "Place: " + showRequest.getPlace() + "\n" +
                        "Show Duration: " + showRequest.getDuration() + " minutes\n" +
                        "Number of Drones: " + showRequest.getNdDrones() + "\n" +
                        "Show Date: " + showRequest.getShowDate() + "\n" +
                        "Request Creation Date: " + showRequest.getCreationDate()
        );
    }
    System.out.println("-------------------------------------");
    return true;
    ```

* **`ShodroneUserPrinter.java`**: A `Visitor` implementation used by the `SelectWidget` to display `ShodroneUser` information in a consistent format (Email and VAT).
    ```java
    // From: implementations from the project/presentation/showrequest/ShodroneUserPrinter.java
    @Override
    public void visit(final ShodroneUser visitee) {
        String vatStr = "[No VAT]";
        if (visitee.identity() != null) {
            vatStr = visitee.identity().toString();
        }

        String emailStr = "[No Email]";
        if (visitee.systemUser() != null && visitee.systemUser().email() != null) {
            emailStr = visitee.systemUser().email().toString();
        }
        // Consistent display format: (Email) [VAT]
        System.out.printf("(%s) [%s]", emailStr, vatStr);
    }
    ```

### 2.2. Application Layer

* **`FindShowRequestController.java`**: This controller provides the application logic for finding show requests. For US235, the relevant method is `findShowRequestsByCustomer`.
  * It ensures that the authenticated user has one of the required roles (`ShodroneRoles.CRM_MANAGER`, `ShodroneRoles.ADMIN`, or `ShodroneRoles.CRM_COLLABORATOR`) before proceeding.
  * It interacts with the `ShowRequestRepository` to fetch all show requests associated with the provided `ShodroneUser` (customer).
  * It handles transactional context if one is active for the repository operations.

  **Key Method: `findShowRequestsByCustomer`**
    ```java
    // From: implementations from the project/showrequest/application/FindShowRequestController.java
    public List<ShowRequest> findShowRequestsByCustomer(ShodroneUser customer) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_COLLABORATOR);

        if (txCtx != null) { // Example of transactional context handling
            List<ShowRequest> showRequestList = new ArrayList<>();
            try {
                txCtx.beginTransaction();
                showRequestRepository.findByCustomer(customer).forEach(showRequestList::add);
                txCtx.commit();
            } catch (Exception e) {
                if (txCtx.isActive()) {
                    txCtx.rollback();
                }
                throw new RuntimeException("Error finding show requests by customer: " + e.getMessage(), e);
            }
            return showRequestList;
        }
        // Fallback or direct repository call if no txCtx
        List<ShowRequest> showRequestList = new ArrayList<>();
        showRequestRepository.findByCustomer(customer).forEach(showRequestList::add);
        return showRequestList;
    }
    ```

### 2.3. Domain Layer

* **`ShowRequest.java`**: The aggregate root representing a single show request. It holds all the details pertinent to a show, such as description, place, dates, duration, number of drones, and the associated customer.
* **`ShodroneUser.java`**: (from `eapli.shodrone.shodroneusermanagement.domain`) Represents a user in the Shodrone system, who in this context is the customer whose show requests are being listed.
* Value Objects (`RequestPlace`, `RequestCreationDate`, `RequestShowDate`, `RequestDuration`, `RequestNDrones`, `Description`): These objects encapsulate the specific attributes of a `ShowRequest`, ensuring their validity and consistency.

### 2.4. Repository Layer

* **`ShowRequestRepository.java`**: This interface defines the contract for data persistence operations related to `ShowRequest` aggregates.
  * The method crucial for US235 is `findByCustomer(ShodroneUser customer)`.

  **Repository Method Signature:**
    ```java
    // From: implementations from the project/showrequest/repositories/ShowRequestRepository.java
    package eapli.shodrone.showrequest.repositories;

    import eapli.framework.domain.repositories.DomainRepository;
    import eapli.framework.general.domain.model.Description;
    import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
    import eapli.shodrone.showrequest.domain.ShowRequest;
    import java.util.Optional;

    public interface ShowRequestRepository extends DomainRepository<Long, ShowRequest> {
        // ... other methods
        Iterable<ShowRequest> findByCustomer(ShodroneUser customer);
    }
    ```

## 3. Test Details

While specific test files for `ListShowRequestUI.java` or a dedicated `FindShowRequestControllerTest.java` focusing solely on the listing feature were not explicitly provided in the uploaded files, testing for this User Story would typically involve:

### 3.1. Application Layer Tests

Unit tests for the `findShowRequestsByCustomer` method in `FindShowRequestController.java` would be essential. These tests would typically:
* **Mock Dependencies**:
  * Mock the `AuthorizationService` (`authz`) to simulate different user roles and ensure authorization checks are correctly applied (e.g., allowing access for `CRM_MANAGER` and `CRM_COLLABORATOR`, denying for others if applicable).
  * Mock the `ShowRequestRepository` to control the data returned (e.g., an empty list, a list with one or more `ShowRequest` objects, or simulate repository errors).
  * Mock the `TransactionalContext` (`txCtx`) if its interaction needs to be verified (e.g., `beginTransaction`, `commit`, `rollback`).
* **Test Scenarios**:
  * **Successful Retrieval**: Verify that when an authorized user requests show requests for a valid customer, the controller correctly calls `showRequestRepository.findByCustomer(expectedCustomer)` and returns the list of `ShowRequest` objects provided by the mock repository.
  * **No Requests Found**: Test the scenario where a customer has no show requests, ensuring an empty list is returned.
  * **Authorization Denied**: Test that if a user without the required roles attempts to access the functionality, an appropriate authorization exception is thrown by the `authz.ensureAuthenticatedUserHasAnyOf(...)` call.
  * **Repository Errors**: Simulate exceptions thrown by the repository to ensure the controller handles them gracefully (e.g., by rolling back a transaction if active and re-throwing a runtime exception).

  **Conceptual Test Snippet for `FindShowRequestController.findShowRequestsByCustomer`:**
    ```java
    // This is a conceptual test structure as the actual test file was not provided for this specific controller method.
    // @ExtendWith(MockitoExtension.class) // Using JUnit 5 with Mockito
    // class FindShowRequestControllerTest {

    //     @Mock
    //     private AuthorizationService authz;
    //     @Mock
    //     private ShowRequestRepository showRequestRepository;
    //     @Mock
    //     private TransactionalContext txCtx; // Assuming it can be mocked or a test version provided

    //     @InjectMocks // Or manually instantiate: controller = new FindShowRequestController(authz, showRequestRepository, txCtx);
    //     private FindShowRequestController controller;

    //     private ShodroneUser testCustomer;
    //     private List<ShowRequest> expectedShowRequests;

    //     @BeforeEach
    //     void setUp() {
    //         testCustomer = mock(ShodroneUser.class); // new ShodroneUser(...);
    //         ShowRequest sr1 = mock(ShowRequest.class); // new ShowRequest(...);
    //         ShowRequest sr2 = mock(ShowRequest.class); // new ShowRequest(...);
    //         expectedShowRequests = Arrays.asList(sr1, sr2);

    //         // Optional: If TransactionalContext is always used and active
    //         // when(showRequestRepository.newTransactionalContext()).thenReturn(txCtx);
    //     }

    //     @Test
    //     void findShowRequestsByCustomer_whenAuthorizedAndRequestsExist_returnsRequests() {
    //         // Arrange
    //         doNothing().when(authz).ensureAuthenticatedUserHasAnyOf(any()); // Assume user is authorized
    //         when(showRequestRepository.findByCustomer(testCustomer)).thenReturn(expectedShowRequests);
    //         // if txCtx is used directly
    //         // when(txCtx.beginTransaction()).thenReturn(null); // or similar for begin
    //         // doNothing().when(txCtx).commit();

    //         // Act
    //         List<ShowRequest> actualShowRequests = controller.findShowRequestsByCustomer(testCustomer);

    //         // Assert
    //         assertNotNull(actualShowRequests);
    //         assertEquals(expectedShowRequests.size(), actualShowRequests.size());
    //         assertTrue(actualShowRequests.containsAll(expectedShowRequests));
    //         verify(authz).ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_MANAGER, ShodroneRoles.ADMIN, ShodroneRoles.CRM_COLLABORATOR);
    //         verify(showRequestRepository).findByCustomer(testCustomer);
    //         // verify(txCtx, times(1)).commit(); // if transactional context is mocked and commit is expected
    //     }

    //     @Test
    //     void findShowRequestsByCustomer_whenNotAuthorized_throwsException() {
    //         // Arrange
    //         doThrow(new UnauthorizedException()).when(authz).ensureAuthenticatedUserHasAnyOf(any());

    //         // Act & Assert
    //         assertThrows(UnauthorizedException.class, () -> controller.findShowRequestsByCustomer(testCustomer));
    //         verify(showRequestRepository, never()).findByCustomer(any());
    //     }
    // }
    ```
  *Unfortunately, it was not possible to write tests for this controller.*

### 3.2. Domain Layer Tests

* **`ShowRequestTest.java`**: Contains comprehensive unit tests for the `ShowRequest` aggregate itself, including its constructor, `sameAs` logic, and property update methods. These tests ensure the integrity of the `ShowRequest` objects that are listed.
* Tests for value objects like `RequestPlaceTest.java`, `RequestDurationTest.java`, etc., ensure that the individual attributes of a `ShowRequest` are valid and behave as expected. These are crucial as their `toString()` methods are often used in the UI display.

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
