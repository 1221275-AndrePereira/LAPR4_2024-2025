package eapli.shodrone.showProposal.domain.simulationReport;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Getter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
public class SimulationReport implements ValueObject,Comparable<SimulationReport> {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private final SimulationResult simulationResult;

    private final LocalDateTime creationDate;


    /**
     * Constructor to create a SimulationReport instance.
     *
     * @param simulationResult The result of the simulation.
     */
    public SimulationReport(final SimulationResult simulationResult) {
//        Preconditions.nonEmpty(path, "Path cannot be null or blank.");
//        Preconditions.ensure(!path.contains(".."), "Path cannot contain '..'");

        this.creationDate = LocalDateTime.now();
//        this.path = ReportPath.pathOf(path);
        this.simulationResult = simulationResult;
    }

    public SimulationReport() {
        // for ORM
        this.creationDate = LocalDateTime.now();
        this.simulationResult = SimulationResult.FAILURE;
//        this.path = null;
    }

    /**
     * Performs an alphabetical comparison of the path strings.
     */
    @Override
    public int compareTo(final SimulationReport other) {
        return this.id.compareTo(other.id);
    }

    /**
     * Checks for value equality.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SimulationReport that = (SimulationReport) o;
        return Objects.equals(this.id, that.id);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     *
     * @return the whole string value of the Code Path
     */
    @Override
    public String toString() {
        return this.getSimulationResult().toString();
    }

}