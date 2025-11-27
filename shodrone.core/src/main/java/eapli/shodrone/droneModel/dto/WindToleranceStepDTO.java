package eapli.shodrone.droneModel.dto;

import eapli.framework.representations.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for Wind Tolerance Step information.
 * This class is used to transfer raw data for a single wind tolerance step
 * from the presentation layer (e.g., UI) to the application controller.
 */
@DTO
@Data
public class WindToleranceStepDTO {
    private final double minWindSpeed;
    private final double maxWindSpeed;
    private final double deviation;

    /**
     * Constructs a WindToleranceStepDTO.
     *
     * @param minWindSpeed The minimum wind speed for this interval (m/s).
     * @param maxWindSpeed The maximum wind speed for this interval (m/s).
     * @param deviation    The position deviation for this interval (meters).
     */
    public WindToleranceStepDTO(double minWindSpeed, double maxWindSpeed, double deviation) {
        this.minWindSpeed = minWindSpeed;
        this.maxWindSpeed = maxWindSpeed;
        this.deviation = deviation;
    }

    public double getMinWindSpeed() {
        return minWindSpeed;
    }

    public double getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public double getDeviation() {
        return deviation;
    }

    @Override
    public String toString() {
        return "WindToleranceStepDTO{" +
                "minWindSpeed=" + minWindSpeed +
                ", maxWindSpeed=" + maxWindSpeed +
                ", deviation=" + deviation +
                '}';
    }
}
