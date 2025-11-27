package eapli.shodrone.showProposal.domain.proposalFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsuranceTest {

    @Test
    void constructor_withPositiveAmount_createsInstance() {
        Long amount = 1000L;
        Insurance insurance = new Insurance(amount);
        assertEquals(amount, insurance.getInsuranceAmount(), "Insurance amount should be " + amount);
    }

    @Test
    void constructor_withZeroAmount_createsInstance() {
        Long amount = 0L;
        Insurance insurance = new Insurance(amount);
        assertEquals(amount, insurance.getInsuranceAmount(), "Insurance amount should be 0L");
    }

    @Test
    void constructor_withNegativeAmount_createsInstance() {
        // Assuming negative amounts are currently allowed as per implementation
        Long amount = -500L;
        Insurance insurance = new Insurance(amount);
        assertEquals(amount, insurance.getInsuranceAmount(), "Insurance amount should be " + amount);
    }

    @Test
    void noArgConstructor_initializesAmountToDefaultZero() {
        Insurance insurance = new Insurance();
        assertEquals(0L, insurance.getInsuranceAmount(), "Default insurance amount for no-arg constructor should be 0L.");
    }

    @Test
    void valueOf_createsEquivalentObject() {
        Long amount = 2000L;
        Insurance insuranceByValueOf = Insurance.valueOf(amount);
        Insurance insuranceByConstructor = new Insurance(amount);

        assertEquals(amount, insuranceByValueOf.getInsuranceAmount(), "Insurance amount from valueOf should match.");
        assertEquals(insuranceByConstructor, insuranceByValueOf, "Insurance object from valueOf should be equal to one from constructor with same amount.");
    }

    @Test
    void getInsuranceAmount_returnsCorrectValue() {
        Long amount = 5000L;
        Insurance insurance = new Insurance(amount);
        assertEquals(amount, insurance.getInsuranceAmount(), "getInsuranceAmount should return the constructor-set value.");
    }

    @Test
    void setInsuranceAmount_updatesValueCorrectlyIncludingNull() {
        Insurance insurance = new Insurance(100L); // Initial value

        Long newAmount = 200L;
        insurance.setInsuranceAmount(newAmount);
        assertEquals(newAmount, insurance.getInsuranceAmount(), "Amount should be updated to 200L.");

        insurance.setInsuranceAmount(null);
        assertNull(insurance.getInsuranceAmount(), "Amount should be updatable to null.");

        insurance.setInsuranceAmount(0L);
        assertEquals(0L, insurance.getInsuranceAmount(), "Amount should be updatable to 0L.");
    }

    @Test
    void compareTo_correctlyComparesInsurancesWithNonNullAmounts() {
        Insurance insurance100 = new Insurance(100L);
        Insurance insurance200 = new Insurance(200L);
        Insurance insurance100_clone = new Insurance(100L);

        assertTrue(insurance100.compareTo(insurance200) < 0, "100L should be less than 200L.");
        assertTrue(insurance200.compareTo(insurance100) > 0, "200L should be greater than 100L.");
        assertEquals(0, insurance100.compareTo(insurance100_clone), "100L should be equal to 100L in comparison.");
    }

    @Test
    void compareTo_withNullAmount_throwsNullPointerException() {
        Insurance insuranceWithAmount = new Insurance(100L);
        Insurance insuranceWithNullAmount = new Insurance(); // Amount is 0L initially
        insuranceWithNullAmount.setInsuranceAmount(null);    // Set to null

        // Case 1: this.insuranceAmount is null
        assertThrows(NullPointerException.class, () -> {
            insuranceWithNullAmount.compareTo(insuranceWithAmount);
        }, "compareTo should throw NPE if this.insuranceAmount is null.");

        // Case 2: o.insuranceAmount is null (and this.insuranceAmount is not)
        assertThrows(NullPointerException.class, () -> {
            insuranceWithAmount.compareTo(insuranceWithNullAmount);
        }, "compareTo should throw NPE if o.insuranceAmount is null and this.insuranceAmount is not.");

        // Case 3: both insuranceAmounts are null (this.insuranceAmount is null)
        Insurance anotherInsuranceWithNullAmount = new Insurance();
        anotherInsuranceWithNullAmount.setInsuranceAmount(null);
        assertThrows(NullPointerException.class, () -> {
            insuranceWithNullAmount.compareTo(anotherInsuranceWithNullAmount);
        }, "compareTo should throw NPE if both insuranceAmounts are null.");
    }

    @Test
    void equalsAndHashCode_verifyContract() {
        Insurance insurance1_amount100 = new Insurance(100L);
        Insurance insurance2_amount100 = new Insurance(100L); // Same amount as 1
        Insurance insurance3_amount200 = new Insurance(200L); // Different amount
        Insurance insurance4_nullAmount = new Insurance();    // Will be 0L
        insurance4_nullAmount.setInsuranceAmount(null);       // Set to null
        Insurance insurance5_nullAmount = new Insurance();    // Will be 0L
        insurance5_nullAmount.setInsuranceAmount(null);       // Set to null, same as 4

        // Reflexivity: an object must equal itself
        assertEquals(insurance1_amount100, insurance1_amount100);
        assertEquals(insurance1_amount100.hashCode(), insurance1_amount100.hashCode());

        // Symmetry: if a.equals(b) is true, then b.equals(a) must be true
        assertEquals(insurance1_amount100, insurance2_amount100);
        assertEquals(insurance2_amount100, insurance1_amount100);
        assertEquals(insurance1_amount100.hashCode(), insurance2_amount100.hashCode());

        // Transitivity: if a.equals(b) and b.equals(c) are true, then a.equals(c) must be true
        // (Implicitly tested by having multiple equal objects)

        // Consistency: multiple invocations of a.equals(b) consistently return true or false
        // (Implicitly tested)

        // Non-nullity: for any non-null reference value x, x.equals(null) must return false
        assertNotEquals(insurance1_amount100, null);

        // Inequality with different amounts
        assertNotEquals(insurance1_amount100, insurance3_amount200);
        // Note: Hashcodes *can* collide for different objects, but for Longs and Lombok's typical impl, they won't for these values.
        // If they did, the equals contract is still the primary concern.

        // Inequality with different types
        assertNotEquals(insurance1_amount100, new Object());

        // Test with null amounts
        assertEquals(insurance4_nullAmount, insurance5_nullAmount, "Two insurances with null amounts should be equal.");
        assertEquals(insurance4_nullAmount.hashCode(), insurance5_nullAmount.hashCode(), "Hashcodes for two insurances with null amounts should be equal.");
        assertNotEquals(insurance1_amount100, insurance4_nullAmount, "Insurance with non-null amount should not be equal to insurance with null amount.");
        assertNotEquals(insurance4_nullAmount, insurance1_amount100, "Insurance with null amount should not be equal to insurance with non-null amount.");
    }
}