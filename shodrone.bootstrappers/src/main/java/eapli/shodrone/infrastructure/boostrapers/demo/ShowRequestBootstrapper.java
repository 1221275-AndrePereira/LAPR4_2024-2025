package eapli.shodrone.infrastructure.boostrapers.demo;

import eapli.framework.actions.Action;

import eapli.shodrone.figure.domain.Description;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.VATNumber;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.showrequest.application.RegisterShowRequestController;
import eapli.shodrone.showrequest.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShowRequestBootstrapper implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowRequestBootstrapper.class);

    private final RegisterShowRequestController controller = new RegisterShowRequestController();

    private final FigureRepository figureRepository = PersistenceContext.repositories().figures();
    private final ShodroneUserRepository userRepository = PersistenceContext.repositories().shodroneUsers();

    @Override
    public boolean execute() {
        List<Optional<Figure>> figures = List.of(
                figureRepository.findByDescription(Description.valueOf("Circle")),
                figureRepository.findByDescription(Description.valueOf("Square")),
                figureRepository.findByDescription(Description.valueOf("Triangle")),
                figureRepository.findByDescription(Description.valueOf("Rectangle"))
        );

        // turn in to a list<Figure>
        List<Figure> figureList = figures.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();


        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Drone Show at the Beach with no Figures"),
                RequestPlace.valueOf(12.2F, 12.3F),
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(100)),
                RequestDuration.valueOf(30),
                RequestNDrones.valueOf(50),
                userRepository.findByVatNumber(VATNumber.valueOf("256789012")).orElse(null),
                List.of() // No figures for this request
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Park Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(25)),
                RequestDuration.valueOf(10),
                RequestNDrones.valueOf(5),
                userRepository.findByVatNumber(VATNumber.valueOf("256789012")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(0)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Stadium Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(50)),
                RequestDuration.valueOf(20),
                RequestNDrones.valueOf(10),
                userRepository.findByVatNumber(VATNumber.valueOf("256789012")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("City Center Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(10)),
                RequestDuration.valueOf(15),
                RequestNDrones.valueOf(20),
                userRepository.findByVatNumber(VATNumber.valueOf("256789012")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2),
                        figureList.get(3)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Airport Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(5)),
                RequestDuration.valueOf(25),
                RequestNDrones.valueOf(15),
                userRepository.findByVatNumber(VATNumber.valueOf("267890123")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2),
                        figureList.get(3)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Festival Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(15)),
                RequestDuration.valueOf(30),
                RequestNDrones.valueOf(25),
                userRepository.findByVatNumber(VATNumber.valueOf("267890123")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2),
                        figureList.get(3)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Concert Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(30)),
                RequestDuration.valueOf(35),
                RequestNDrones.valueOf(30),
                userRepository.findByVatNumber(VATNumber.valueOf("267890123")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2),
                        figureList.get(3)
                )
        );

        controller.registerShowRequest(
                eapli.framework.general.domain.model.Description.valueOf("Corporate Event Show with Figures"),
                RequestPlace.valueOf(38.7167F, -9.4672F), // Near Sintra
                RequestCreationDate.now(),
                RequestShowDate.valueOf(LocalDateTime.now().plusDays(40)),
                RequestDuration.valueOf(40),
                RequestNDrones.valueOf(35),
                userRepository.findByVatNumber(VATNumber.valueOf("267890123")).orElse(null),
                List.of(
                        figureList.get(0),
                        figureList.get(1),
                        figureList.get(2),
                        figureList.get(1),
                        figureList.get(0)
                )
        );
        return true;
    }
}
