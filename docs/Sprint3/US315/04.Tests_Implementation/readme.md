# US315: Add Video of Simulation to the Proposal - Tests and Implementation

## 1. User Story Description

**US315: Add video of simulation to the proposal**

As a CRM Collaborator, I want to add a video of the simulated show so the customer can have a preview of the show.

*Note: In the scope of LAPR4, the team does not need to actually generate the video and can use any suitable video file.*

## 2. Implementation Details

The implementation for this use case follows a classic layered architecture pattern, separating concerns into presentation, application, domain, and persistence layers.

### 2.1. Application Layer

The application layer orchestrates the use case, handling user input and coordinating domain objects and repositories.

* **UI (`EditVideoOfProposalUI.java`)**: This class is responsible for the user interaction. It first presents a list of proposals that are in a `SAFE` state, which is the prerequisite for adding a video. After the user selects a proposal, it offers the choice to add or remove a video. Upon selection, it captures the video link from the user and invokes the application controller.

* **Controller (`AddVideoToProposalController.java`)**: This controller exposes the core logic for this use case.

    * `addVideoToProposal(ShowProposal proposal, String videoUrl)`: This method takes the selected proposal and the video URL string. It creates a `ProposalVideoLink` value object from the URL, sets this link on the `ShowProposal` aggregate, updates the proposal's status, and finally saves the changes via the repository.

    * `removeVideoFromProposal(ShowProposal proposal)`: This method sets the `videoLink` on the `ShowProposal` aggregate to `null`, updates the status accordingly, and persists the change.

    * Authorization is checked using `authz.ensureAuthenticatedUserHasAnyOf` to ensure only authorized roles (CRM Collaborator, Admin, Power User) can perform this action.

### 2.2. Domain Layer

The domain layer contains the business logic and rules related to the entities involved.

* **`ShowProposal.java` (Aggregate Root)**: This is the central entity.

    * It contains a field `videoLink` of type `ProposalVideoLink`.

    * The method `setVideoLink(ProposalVideoLink videoLink)` is used to update the video link.

    * Crucially, after the video link is updated (either added or removed), the `updateStatus()` method is called. This method recalculates the proposal's status. For instance, a proposal might transition from `TESTING` to `SAFE` if adding a video link is the final step, or from `SAFE` back to `TESTING` if the video link is removed.

* **`ProposalVideoLink.java` (Value Object)**: This class encapsulates the video link.

    * It ensures that the provided link is not null via a `Preconditions.nonNull()` check in its constructor.

    * Its constructor and factory method `valueOf()` handle the creation and validation of the link string.

    * Being a `ValueObject`, it's immutable and its equality is based on its content (the link string).

### 2.3. Repository Layer

The persistence layer is responsible for saving and retrieving the `ShowProposal` aggregate.

* **`ShowProposalRepository.java` (Interface)**: Defines the contract for persistence operations on `ShowProposal` aggregates, extending the generic `DomainRepository`.

* **`JpaShowProposalRepository.java` (Implementation)**: Implements the repository interface using JPA. The `save()` method is used by the `AddVideoToProposalController` to persist the updated `ShowProposal` object, including the new or removed `ProposalVideoLink`, to the database.

## 3. Test Details

### 3.1. Application Layer Tests

No specific unit tests for the `AddVideoToProposalController` were found in the provided files. Testing at this layer would typically involve mocking the repository and authorization service to verify that the controller correctly interacts with the domain objects (`ShowProposal`) and persists the result.

### 3.2. Domain Layer Tests

The domain layer is well-tested, focusing on the business rules and state transitions of the domain objects.

* **`ProposalVideoLinkTest.java`**: This test class thoroughly validates the `ProposalVideoLink` value object.

    * **Constructor & Factory Tests**: Ensures that the object can be created with valid links (including empty strings) and that it correctly throws an `IllegalArgumentException` for null input.

      ```java
      @Test
      @DisplayName("Main constructor with valid video link succeeds")
      void mainConstructor_withValidLink_succeeds() {
          ProposalVideoLink link = new ProposalVideoLink(VALID_VIDEO_LINK_1);
          assertNotNull(link);
          assertEquals(VALID_VIDEO_LINK_1, link.video());
      }
  
      @ParameterizedTest
      @DisplayName("Main constructor with null video link throws IllegalArgumentException")
      @NullSource
      void mainConstructor_withNullLink_throwsIllegalArgumentException(String nullVideoLink) {
          assertThrows(IllegalArgumentException.class,
                  () -> new ProposalVideoLink(nullVideoLink));
      }
      ```

    * **Equals and HashCode Tests**: Verifies that the Lombok-generated `equals()` and `hashCode()` methods work correctly, ensuring that two objects with the same link are considered equal.

      ```java
      @Test
      @DisplayName("equals() returns true for equal video links")
      void equals_returnsTrueForEqualLinks() {
          assertEquals(linkA1, linkA2);
          assertEquals(linkEmpty1, linkEmpty2);
          assertEquals(ormLink1, ormLink2);
          assertEquals(linkEmpty1, ormLink1); // Empty string vs ORM default
      }
      ```

    * **`compareTo()` Tests**: Confirms that the comparison logic, based on the lexicographical order of the video link string, is correct.

      ```java
      @Test
      @DisplayName("compareTo: this less than other")
      void compareTo_thisLessThanOther() {
          assertTrue(link_C_lex_smaller.compareTo(link_B_lex_greater) < 0, "'[http://example.com/aa](http://example.com/aa)' should be less than '[http://example.com/b](http://example.com/b)'");
          assertTrue(link_A_lex.compareTo(link_B_lex_greater) < 0, "'[http://example.com/a](http://example.com/a)' should be less than '[http://example.com/b](http://example.com/b)'");
          assertTrue(link_Empty.compareTo(link_A_lex) < 0, "Empty string should be less than non-empty string");
      }
      ```

* **`ShowProposalTest.java`**: Although no specific test named `testAddVideoLink` was found, the test suite for `ShowProposal` implicitly covers the behavior of setting a video link through its impact on the proposal's status.

    * **`updateStatus()` Logic Tests**: This is the most relevant test group. These tests verify that the proposal's status changes correctly based on its state. Adding a video link is a key condition for the status to transition from `INCOMPLETE` or `TESTING` to `SAFE`. For example, the test `updateStatus_allConditionsMet_simulationSuccess_becomesSafe()` ensures that if a video link is present (along with other necessary data), the status correctly becomes `SAFE`. Conversely, tests for the `TESTING` status confirm that if the video link is missing, the status is set appropriately.

## 4. Dependencies (Maven Project)

The project relies on the following key dependencies as defined in the `pom.xml` files:

* **`eapli.framework`**: The core framework providing DDD building blocks, persistence support, and application controller patterns.

* **`junit`**: For writing and running unit tests.

* **`org.mockito`**: For creating mock objects in unit tests to isolate classes from their dependencies.

* **`org.hibernate.orm`**: The JPA provider used for database persistence.

* **`lombok`**: Used in domain classes to reduce boilerplate code for getters, setters, equals, hashCode, etc.
