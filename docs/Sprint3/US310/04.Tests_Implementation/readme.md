# `ShowRequestTest` Unit Test Documentation

This test class verifies the correctness and integrity of the `ShowRequest` domain model in the `eapli.shodrone.showrequest.domain` package.

## Purpose

The `ShowRequest` class encapsulates data related to a drone show request. These unit tests ensure:
- Constructor enforces required non-null fields.
- Domain logic (e.g., equality via `sameAs`) works correctly.
- Update methods modify internal state as expected.
- Figures and proposals are correctly handled.
- Edge cases (e.g. uninitialized sets) are safely guarded or highlighted.

---

## Test Categories

### Constructor Tests

Validate construction with valid and invalid data:
```java
@Test
void testShowRequest_validConstruction() {
    ShowRequest showRequest = createValidShowRequest();
    assertNotNull(showRequest);
    assertEquals(description, showRequest.getDescription());
    // More assertions...
}
```

Constructor guards against null inputs:
```java
@Test
void testShowRequest_nullDescription_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new ShowRequest(
        null, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures
    ));
}
```

### `sameAs()` Logic

Tests equality comparison between two requests based on business identity:
```java
@Test
void testSameAs_equalRequests() {
    assertTrue(showRequest1.sameAs(showRequest2));
}
```

Ensures differences are detected properly:
```java
@Test
void testSameAs_differentDescription() {
    ShowRequest different = new ShowRequest(
        Description.valueOf("Another show"), requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, mockCustomer, figures);
    assertFalse(showRequest.sameAs(different));
}
```

Note: Customer is **not** part of the comparison logic:
```java
@Test
void testSameAs_differentCustomer_shouldBeTrue_asCustomerNotInSameAsLogic() {
    ShodroneUser otherCustomer = mock(ShodroneUser.class);
    ShowRequest other = new ShowRequest(description, requestPlace, requestCreationDate, requestShowDate, requestDuration, requestNDrones, otherCustomer, figures);
    assertTrue(showRequest.sameAs(other));
}
```

---

## Update Method Tests

Update individual fields:
```java
@Test
void testUpdateShowDescription() {
    showRequest.updateShowDescription(Description.valueOf("Updated Show"));
    assertEquals("Updated Show", showRequest.getDescription().toString());
}
```

Other updates follow the same pattern for:
- Date (`updateShowDate`)
- Duration (`updateShowDuration`)
- Place (`updateShowPlace`)
- Drones (`updateShowNDrones`)
- Customer (`updateShowCustomer`)
- Figures (`setFigure`)

---

## Proposal Handling

By default, `proposals` is not initialized (potential design caveat). These tests verify the consequences:

```java
@Test
void testAddProposal_addsToSetWhenProposalsInitialized() {
    showRequest.setProposals(new HashSet<>());
    ShowProposal proposal = mock(ShowProposal.class);
    showRequest.addProposal(proposal);
    assertTrue(showRequest.getProposals().contains(proposal));
}
```

```java
@Test
void testAddProposal_throwsNullPointerException_ifProposalsSetNotInitialized() {
    ShowProposal proposal = mock(ShowProposal.class);
    assertThrows(NullPointerException.class, () -> showRequest.addProposal(proposal));
}
```

---

## Identity and Version

These methods return `null` until the object is persisted:
```java
@Test
void testIdentity_beforePersistence() {
    assertNull(showRequest.identity());
}
```

---

## `toString()` Representation

Ensures a complete, human-readable summary of the request:
```java
@Test
void testToString_withAllFieldsSetAndId() {
    showRequest.setId(1L);
    when(mockCustomer.systemUser().username()).thenReturn(Username.valueOf("testUser123"));
    when(mockFigure1.toString()).thenReturn("Figure[Circle, Color:Red]");
    
    String toString = showRequest.toString();
    assertTrue(toString.contains("Show Request ID: 1"));
    assertTrue(toString.contains("testUser123"));
}
```

---

## Observations

- The test suite is comprehensive and covers both happy and edge paths.
- The `proposals` field might benefit from internal initialization (e.g., to `new HashSet<>()`) to avoid NPEs.
- `sameAs` logic intentionally ignores the customer field, focusing on business-relevant identity.

---

## Related Domain Classes

These tests rely on and mock:
- `ShowProposal`
- `ShodroneUser`
- `Figure`
- Value Objects like `RequestPlace`, `RequestShowDate`, `RequestNDrones`, etc.

---

## Sample Helper Method

```java
private ShowRequest createValidShowRequest() {
    return new ShowRequest(
        description, requestPlace, requestCreationDate, requestShowDate,
        requestDuration, requestNDrones, mockCustomer, figures
    );
}
```

---

## Conclusion

The `ShowRequestTest` class ensures domain logic is safe, predictable, and robust. It follows best practices in naming, mocking, and verifying behaviors.

