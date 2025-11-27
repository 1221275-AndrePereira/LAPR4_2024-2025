package eapli.shodrone.drone.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class SerialNumber implements ValueObject, Comparable<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long serialNumber;

    public SerialNumber(final Long serialNumber) {
        Preconditions.ensure(serialNumber != null && serialNumber.toString().matches("[a-zA-Z0-9]{1,20}"),
            "Serial number must be a valid alphanumeric string with a maximum of 20 characters and minimum of 5.");
        this.serialNumber = serialNumber;
    }

    /**
     * Default constructor for ORM.
     */
    protected SerialNumber() {
        this.serialNumber = null; // For ORM purposes
    }


    public Long serialNumber (){
        return this.serialNumber;
    }

    public static SerialNumber valueOf(final Long serialNumber) {
        return new SerialNumber(serialNumber);
    }

    @Override
    public int compareTo(final Long other) {
        return 0;
    }

    @Override
    public String toString() {
        return serialNumber != null ? serialNumber.toString() : "N/A";
    }
}
