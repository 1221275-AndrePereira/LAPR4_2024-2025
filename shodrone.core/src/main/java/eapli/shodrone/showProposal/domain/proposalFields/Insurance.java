package eapli.shodrone.showProposal.domain.proposalFields;

import eapli.framework.domain.model.ValueObject;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public final class Insurance implements ValueObject, Comparable<Insurance> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long insuranceAmount;

    /**
     * Constructor to create an Insurance instance.
     *
     * @param insuranceAmount The amount of insurance.
     */
    public Insurance(Long insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Insurance() {
        // for ORM
        this.insuranceAmount = 0L; // Or handle appropriately
    }

    /**
     * Factory method to create an Insurance instance.
     *
     * @param insuranceAmount The amount of insurance.
     * @return A new Insurance instance.
     */
    public static Insurance valueOf(Long insuranceAmount){
        return new Insurance(insuranceAmount);
    }


    @Override
    public int compareTo(Insurance o) {
        return this.insuranceAmount.compareTo(o.insuranceAmount);
    }
}
