package eapli.shodrone.showProposal.domain;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.shodrone.figure.domain.Figure;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalDroneModel;
import eapli.shodrone.showProposal.domain.proposalDrone.ProposalNDrones;
import eapli.shodrone.showProposal.domain.proposalFields.*;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationReport;
import eapli.shodrone.showProposal.domain.simulationReport.SimulationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowProposalTest {
//
//    @Mock
//    private SystemUser mockManager;
//    @Mock
//    private SimulationReport mockSimulationReport;
//    @Mock
//    private ProposalFigure mockProposalFigure1;
//    @Mock
//    private ProposalFigure mockProposalFigure2;
//    @Mock
//    private Figure mockFigure1;
//    @Mock
//    private Figure mockFigure2;
//    @Mock
//    private ProposalDroneModel mockProposalDroneModel;
//
//    // Concrete Value Objects
//    private ProposalNDrones validNDrones;
//    private ProposedDuration validDuration;
//    private Insurance validInsurance;
//    private ProposedPlace validPlace;
//    private ProposedShowDate validShowDate;
//    private ProposalCreationDate fixedCreationDate;
//
//    private ShowProposal showProposal; // Instance for general tests, typically initialized in @BeforeEach of nested classes
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        validNDrones = ProposalNDrones.valueOf(10);
//        validDuration = ProposedDuration.valueOf(30);
//        validInsurance = Insurance.valueOf(1000L);
//        validPlace = ProposedPlace.valueOf(45.0f, 45.0f);
//        validShowDate = ProposedShowDate.valueOf(LocalDateTime.now().plusDays(1));
//        fixedCreationDate = ProposalCreationDate.valueOf(LocalDate.from(LocalDateTime.of(2023, 1, 1, 12, 00)));
//
//        when(mockProposalFigure1.figure()).thenReturn(mockFigure1);
//        when(mockProposalFigure2.figure()).thenReturn(mockFigure2);
//        when(mockFigure1.isActive()).thenReturn(true);
//        when(mockFigure2.isActive()).thenReturn(true);
//        // Ensure figures are treated as distinct by equals for relevant tests
//        // (though direct `equals` on mocks might not behave as expected without specific stubbing if needed beyond reference equality)
//        // For the `addFigure` consecutive check, it relies on `proposalFigure.figure().equals(...)`
//        // So, if mockFigure1 and mockFigure2 are different mock instances, they won't be equal by default.
//    }
//
//    // Helper to create a ShowProposal instance for testing, using the default constructor
//    // to avoid the main constructor's NPE issue and then setting fields.
//    private ShowProposal createTestShowProposalInstance() {
//        ShowProposal proposal = new ShowProposal(); // Default constructor is safer
//        proposal.setDescription("Valid Test Proposal");
//        proposal.setProposalNDrones(validNDrones);
//        proposal.setDuration(validDuration);
//        proposal.setInsuranceAmount(validInsurance);
//        proposal.setProposedPlace(validPlace);
//        proposal.setProposedShowDate(validShowDate);
//        proposal.setManager(mockManager);
//        // Default constructor initializes: creationDate, status (INCOMPLETE), videoLink,
//        // simulationReport (default), figures (empty list), droneModels (empty set),
//        // customerFeedback (""), downLoadCode (default).
//        proposal.updateStatus(); // Recalculate status based on any newly set values if necessary
//        return proposal;
//    }
//
//
//    @Nested
//    @DisplayName("Constructor Tests")
//    class ConstructorTests {
//
//        @Test
//        @DisplayName("Main constructor throws IllegalArgumentException for null critical arguments")
//        void mainConstructor_nullCriticalArgs_throwsIllegalArgumentException() {
//            // Preconditions.noneNull(duration, proposedPlace, numberOfDrones, proposedShowDate);
//            assertThrows(IllegalArgumentException.class, () -> new ShowProposal("Desc", null, validDuration, validInsurance, validPlace, validShowDate, mockManager));
//            assertThrows(IllegalArgumentException.class, () -> new ShowProposal("Desc", validNDrones, null, validInsurance, validPlace, validShowDate, mockManager));
//            assertThrows(IllegalArgumentException.class, () -> new ShowProposal("Desc", validNDrones, validDuration, validInsurance, null, validShowDate, mockManager));
//            assertThrows(IllegalArgumentException.class, () -> new ShowProposal("Desc", validNDrones, validDuration, validInsurance, validPlace, null, mockManager));
//            // Description, insurance, manager can be null as per Preconditions check in constructor.
//        }
//
//        @Test
//        @DisplayName("Default constructor initializes fields to default/empty values and INCOMPLETE status")
//        void defaultConstructor_initializesDefaults() {
//            ShowProposal proposal = new ShowProposal();
//            assertEquals(ProposalStatus.INCOMPLETE, proposal.getStatus());
//            assertNotNull(proposal.getProposalNDrones());
//            assertEquals(0, proposal.getProposalNDrones().number());
//            assertNotNull(proposal.getDuration());
//            assertEquals(0, proposal.getDuration().minutes());
//            assertNotNull(proposal.getInsuranceAmount());
//            assertEquals(0L, proposal.getInsuranceAmount().getInsuranceAmount());
//            assertNotNull(proposal.getVideoLink());
//            assertSame("", proposal.getVideoLink().video());
//            assertNotNull(proposal.getCreationDate()); // Sets to LocalDate.now()
//            assertNotNull(proposal.getProposedPlace());
//            assertNull(proposal.getProposedPlace().getLatitude());
//            assertNotNull(proposal.getProposedShowDate());
//            assertNull(proposal.getProposedShowDate().date());
//            assertNotNull(proposal.getSimulationReport()); // Default SimulationReport (result=FAILURE)
//            assertEquals(SimulationResult.FAILURE, proposal.getSimulationReport().getSimulationResult());
//            assertTrue(proposal.getFigures().isEmpty());
//            assertNotNull(proposal.getDroneModels());
//            assertTrue(proposal.getDroneModels().isEmpty());
//            assertEquals("", proposal.getCustomerFeedback());
//            assertNotNull(proposal.getDownLoadCode());
//            assertNull(proposal.getDescription());
//            assertNull(proposal.getManager());
//            assertNull(proposal.getProposalDocument());
//        }
//    }
//
//    @Nested
//    @DisplayName("Identity Test")
//    class IdentityTest {
//        @Test
//        @DisplayName("identity() returns null for new proposal (ID not set)")
//        void identity_returnsId() {
//            ShowProposal proposal = new ShowProposal();
//            assertNull(proposal.identity(), "ID should be null for a new, unpersisted entity.");
//            // To test with an ID, one would typically persist the entity and retrieve it,
//            // or use reflection if a setter was available for testing (which is not the case here).
//        }
//    }
//
//    @Nested
//    @DisplayName("State Modification Methods Tests")
//    class StateModificationTests {
//
//        @BeforeEach
//        void setupStateTests() {
//            showProposal = createTestShowProposalInstance(); // Uses default constructor and sets common fields
//        }
//
//        @Test
//        @DisplayName("accept() sets status to ACCEPTED and updates feedback")
//        void accept_setsStatusAndFeedback() {
//            String feedback = "Great proposal!";
//            showProposal.accept(feedback);
//            assertEquals(ProposalStatus.ACCEPTED, showProposal.getStatus());
//            // customerFeedback is private without a getter. Verification of its content
//            // would require reflection or a change in ShowProposal.
//            // This test primarily verifies the status change.
//        }
//
//        @Test
//        @DisplayName("reject() sets status to REFUSED and updates feedback")
//        void reject_setsStatusAndFeedback() {
//            String feedback = "Needs improvement.";
//            showProposal.reject(feedback);
//            assertEquals(ProposalStatus.REFUSED, showProposal.getStatus());
//            // Same comment as accept() regarding customerFeedback verification.
//        }
//
//        @Test
//        @DisplayName("addFigure() successfully adds an active, non-consecutive figure")
//        void addFigure_validFigure_addsSuccessfully() {
//            showProposal.addFigure(mockProposalFigure1);
//            assertEquals(1, showProposal.getFigures().size());
//            assertTrue(showProposal.getFigures().contains(mockProposalFigure1));
//        }
//
//        @Test
//        @DisplayName("addFigure() throws IllegalArgumentException for null figure")
//        void addFigure_nullFigure_throwsException() {
//            assertThrows(IllegalArgumentException.class, () -> showProposal.addFigure(null));
//        }
//
//        @Test
//        @DisplayName("addFigure() throws IllegalStateException for inactive figure")
//        void addFigure_inactiveFigure_throwsException() {
//            when(mockFigure1.isActive()).thenReturn(false);
//            assertThrows(IllegalArgumentException.class, () -> showProposal.addFigure(mockProposalFigure1));
//        }
//
//        @Test
//        @DisplayName("addFigure() throws IllegalStateException for consecutive identical figure")
//        void addFigure_consecutiveIdenticalFigure_throwsException() {
//            // Ensure mockFigure1.equals(mockFigure1) is true, which it is by default for the same instance.
//            // If mockProposalFigure1.figure() returns the same mockFigure1 instance, this will work.
//            showProposal.addFigure(mockProposalFigure1);
//            assertThrows(IllegalArgumentException.class, () -> showProposal.addFigure(mockProposalFigure1));
//        }
//
//        @Test
//        @DisplayName("addFigure() allows different figure after another")
//        void addFigure_differentFigure_addsSuccessfully() {
//
//            showProposal.addFigure(mockProposalFigure1);
//            showProposal.addFigure(mockProposalFigure2);
//            assertEquals(2, showProposal.getFigures().size());
//        }
//
//        @Test
//        @DisplayName("removeAllDroneModels() clears drone models set")
//        void removeAllDroneModels_clearsSet() {
//            showProposal.getDroneModels().add(mockProposalDroneModel); // Add one to make it non-empty
//            assertFalse(showProposal.getDroneModels().isEmpty());
//
//            showProposal.removeAllDroneModels();
//            assertTrue(showProposal.getDroneModels().isEmpty());
//        }
//
//        @Test
//        @DisplayName("sendToCustomer() changes status to PENDING and generates code if status is SAFE")
//        void sendToCustomer_whenSafe_changesStatusAndGeneratesCode() {
//            // Setup to make status SAFE
//            showProposal.getFigures().add(mockProposalFigure1);
//            showProposal.getDroneModels().add(mockProposalDroneModel);
//            showProposal.setVideoLink(ProposalVideoLink.valueOf("http://example.com/video.mp4"));
//            when(mockSimulationReport.getSimulationResult()).thenReturn(SimulationResult.SUCCESS);
//            showProposal.assignReport(mockSimulationReport); // This will call updateStatus
//
//            assertEquals(ProposalStatus.SAFE, showProposal.getStatus());
//            DownLoadCode initialDownLoadCode = showProposal.getDownLoadCode();
//
//
//
//            showProposal.sendToCustomer();
//
//            assertEquals(ProposalStatus.PENDING, showProposal.getStatus());
//            assertNotNull(showProposal.getDownLoadCode());
//            // Assuming DownLoadCode.generateNewCode returns a new instance or an instance with a different code.
//            assertNotEquals(initialDownLoadCode, showProposal.getDownLoadCode(), "Download code should be regenerated.");
//            // Note: this.id will be null if not persisted. DownLoadCode.generateNewCode must handle this.
//        }
//
//        @Test
//        @DisplayName("sendToCustomer() does nothing if status is not SAFE")
//        void sendToCustomer_whenNotSafe_doesNothing() {
//            showProposal.setStatus(ProposalStatus.TESTING); // Any status other than SAFE
//            DownLoadCode initialCode = showProposal.getDownLoadCode();
//
//            showProposal.sendToCustomer();
//
//            assertEquals(ProposalStatus.TESTING, showProposal.getStatus());
//            assertEquals(initialCode, showProposal.getDownLoadCode());
//        }
//
//        @Test
//        @DisplayName("assignReport() assigns report and updates status (e.g., to SAFE if conditions met)")
//        void assignReport_assignsReportAndUpdateStatus() {
//            // Setup for status to become SAFE after report assignment
//            showProposal.getFigures().add(mockProposalFigure1);
//            showProposal.getDroneModels().add(mockProposalDroneModel);
//            showProposal.setVideoLink(ProposalVideoLink.valueOf("http://video.com"));
//
//            when(mockSimulationReport.getSimulationResult()).thenReturn(SimulationResult.SUCCESS);
//            showProposal.assignReport(mockSimulationReport);
//
//            assertEquals(mockSimulationReport, showProposal.getSimulationReport());
//            assertEquals(ProposalStatus.SAFE, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateNdrones() updates the number of drones")
//        void updateNdrones_updatesValue() {
//            int newNumberOfDrones = 25;
//            showProposal.updateNdrones(newNumberOfDrones);
//            assertEquals(newNumberOfDrones, showProposal.getProposalNDrones().number());
//        }
//    }
//
//    @Nested
//    @DisplayName("updateStatus() Logic Tests")
//    class UpdateStatusTests {
//
//        @BeforeEach
//        void setupUpdateStatusTests() {
//            showProposal = createTestShowProposalInstance(); // Base setup
//        }
//
//        // Helper to make a proposal ready for SAFE/FAILED state if report is set
//        private void makeProposalReadyForReportCheck(ShowProposal proposal) {
//            proposal.getFigures().add(mockProposalFigure1);
//            proposal.getDroneModels().add(mockProposalDroneModel);
//            proposal.setVideoLink(ProposalVideoLink.valueOf("http://example.com/video.mp4"));
//        }
//
//        @Test
//        @DisplayName("updateStatus: Stays ACCEPTED if already ACCEPTED")
//        void updateStatus_alreadyAccepted_staysAccepted() {
//            showProposal.setStatus(ProposalStatus.ACCEPTED);
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.ACCEPTED, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Stays REFUSED if already REFUSED")
//        void updateStatus_alreadyRefused_staysRefused() {
//            showProposal.setStatus(ProposalStatus.REFUSED);
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.REFUSED, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Stays PENDING if already PENDING")
//        void updateStatus_alreadyPending_staysPending() {
//            showProposal.setStatus(ProposalStatus.PENDING);
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.PENDING, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes INCOMPLETE if figures are empty (even if others set)")
//        void updateStatus_figuresEmpty_becomesIncomplete() {
//            // Drones, video, report might be set, but figures are empty
//            showProposal.getDroneModels().add(mockProposalDroneModel);
//            showProposal.setVideoLink(ProposalVideoLink.valueOf("http://example.com/video.mp4"));
//            showProposal.setSimulationReport(mockSimulationReport);
//            showProposal.getFigures().clear(); // Ensure figures are empty
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.INCOMPLETE, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes INCOMPLETE if drone models are empty (even if others set)")
//        void updateStatus_droneModelsEmpty_becomesIncomplete() {
//            // Figures, video, report might be set, but droneModels are empty
//            showProposal.getFigures().add(mockProposalFigure1);
//            showProposal.setVideoLink(ProposalVideoLink.valueOf("http://example.com/video.mp4"));
//            showProposal.setSimulationReport(mockSimulationReport);
//            showProposal.getDroneModels().clear(); // Ensure drone models are empty
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.INCOMPLETE, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes TESTING if video link's video is null (figures/drones present)")
//        void updateStatus_videoLinkVideoIsNull_becomesTesting() {
//            showProposal.getFigures().add(mockProposalFigure1);
//            showProposal.getDroneModels().add(mockProposalDroneModel);
//            // videoLink by default from createTestShowProposalInstance might be non-null object but video() is null
//            // Let's ensure it's explicitly a link with null video content
//            ProposalVideoLink linkWithNullVideo = mock(ProposalVideoLink.class);
//            when(linkWithNullVideo.video()).thenReturn(null);
//            showProposal.setVideoLink(linkWithNullVideo);
//            showProposal.setSimulationReport(mockSimulationReport); // Report is present
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.TESTING, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes TESTING if simulation report is null (figures/drones/video present)")
//        void updateStatus_simulationReportNull_becomesTesting() {
//            makeProposalReadyForReportCheck(showProposal); // Figures, drones, video are present
//            showProposal.setSimulationReport(null); // Report is null
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.TESTING, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes FAILED if using default SimulationReport (which has FAILURE result and other conditions met)")
//        void updateStatus_defaultSimulationReport_becomesFailed() {
//            // showProposal from createTestShowProposalInstance already has a default SimulationReport (result=FAILURE)
//            // We just need to ensure other conditions are met
//            makeProposalReadyForReportCheck(showProposal);
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.FAILED, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes SAFE if all conditions met and simulation is SUCCESS")
//        void updateStatus_allConditionsMet_simulationSuccess_becomesSafe() {
//            makeProposalReadyForReportCheck(showProposal);
//            when(mockSimulationReport.getSimulationResult()).thenReturn(SimulationResult.SUCCESS);
//            showProposal.setSimulationReport(mockSimulationReport);
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.SAFE, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Becomes FAILED if all conditions met and simulation is FAILURE")
//        void updateStatus_allConditionsMet_simulationFailure_becomesFailed() {
//            makeProposalReadyForReportCheck(showProposal);
//            when(mockSimulationReport.getSimulationResult()).thenReturn(SimulationResult.FAILURE);
//            showProposal.setSimulationReport(mockSimulationReport);
//
//            showProposal.updateStatus();
//            assertEquals(ProposalStatus.FAILED, showProposal.getStatus());
//        }
//
//        @Test
//        @DisplayName("updateStatus: Stays TESTING if report is present but its result is null (figures/drones/video present)")
//        void updateStatus_simulationReportResultIsNull_staysTesting() {
//            makeProposalReadyForReportCheck(showProposal);
//            SimulationReport reportWithNullResult = mock(SimulationReport.class);
//            when(reportWithNullResult.getSimulationResult()).thenReturn(null); // Simulate this case
//            showProposal.setSimulationReport(reportWithNullResult);
//
//            showProposal.updateStatus();
//            // If figures/drones/video are present, and report is present but result is null,
//            // the `else if (this.simulationReport.getSimulationResult() != null)` block is skipped.
//            // It should remain in TESTING (or whatever prior state was if INCOMPLETE conditions were met earlier).
//            // Given `makeProposalReadyForReportCheck`, it should be TESTING.
//            assertEquals(ProposalStatus.TESTING, showProposal.getStatus());
//        }
//    }
//
//    @Nested
//    @DisplayName("sameAs() Method Tests")
//    class SameAsTests {
//        private ShowProposal proposal1;
//        private ShowProposal proposal2;
//
//        @BeforeEach
//        void setUpSameAs() {
//            // sameAs checks: status, duration, videoLink, creationDate, proposedPlace, proposedShowDate.
//            // It does NOT check: id, simulationReport, figures, proposalNDrones, droneModels,
//            // customerFeedback, manager, insuranceAmount, description, proposalDocument, downLoadCode.
//
//            proposal1 = new ShowProposal(); // Uses default constructor
//            proposal1.setStatus(ProposalStatus.TESTING);
//            proposal1.setDuration(ProposedDuration.valueOf(60));
//            proposal1.setVideoLink(ProposalVideoLink.valueOf("http://video.com"));
//            proposal1.setCreationDate(fixedCreationDate); // Use a fixed creation date
//            proposal1.setProposedPlace(ProposedPlace.valueOf(10f, 20f));
//            proposal1.setProposedShowDate(ProposedShowDate.valueOf(LocalDateTime.of(2024,12,25, 0,0)));
//
//            proposal2 = new ShowProposal();
//            proposal2.setStatus(ProposalStatus.TESTING);
//            proposal2.setDuration(ProposedDuration.valueOf(60));
//            proposal2.setVideoLink(ProposalVideoLink.valueOf("http://video.com"));
//            proposal2.setCreationDate(fixedCreationDate);
//            proposal2.setProposedPlace(ProposedPlace.valueOf(10f, 20f));
//            proposal2.setProposedShowDate(ProposedShowDate.valueOf(LocalDateTime.of(2024,12,25, 0, 0)));
//        }
//
//        @Test
//        @DisplayName("sameAs returns true for the same object")
//        void sameAs_sameObject_returnsTrue() {
//            assertTrue(proposal1.sameAs(proposal1));
//        }
//
//        @Test
//        @DisplayName("sameAs returns true for different objects with same relevant fields")
//        void sameAs_identicalRelevantFields_returnsTrue() {
//            assertTrue(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if status differs")
//        void sameAs_differentStatus_returnsFalse() {
//            proposal2.setStatus(ProposalStatus.SAFE);
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if duration differs")
//        void sameAs_differentDuration_returnsFalse() {
//            proposal2.setDuration(ProposedDuration.valueOf(30));
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if videoLink differs")
//        void sameAs_differentVideoLink_returnsFalse() {
//            proposal2.setVideoLink(ProposalVideoLink.valueOf("http://another.com"));
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if creationDate differs")
//        void sameAs_differentCreationDate_returnsFalse() {
//            proposal2.setCreationDate(ProposalCreationDate.valueOf(LocalDate.now().plusDays(1)));
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if proposedPlace differs")
//        void sameAs_differentProposedPlace_returnsFalse() {
//            proposal2.setProposedPlace(ProposedPlace.valueOf(30f, 40f));
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false if proposedShowDate differs")
//        void sameAs_differentProposedShowDate_returnsFalse() {
//            proposal2.setProposedShowDate(ProposedShowDate.valueOf(LocalDateTime.now().plusMonths(1)));
//            assertFalse(proposal1.sameAs(proposal2));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false for null object")
//        void sameAs_nullObject_returnsFalse() {
//            assertFalse(proposal1.sameAs(null));
//        }
//
//        @Test
//        @DisplayName("sameAs returns false for object of different type")
//        void sameAs_differentType_returnsFalse() {
//            assertFalse(proposal1.sameAs(new Object()));
//        }
//
//        @Test
//        @DisplayName("sameAs ignores differences in fields not checked by it (e.g., description, proposalNDrones)")
//        void sameAs_ignoresUncheckedFields() {
//            proposal1.setDescription("Desc 1");
//            proposal2.setDescription("Desc 2"); // Different description
//
//            proposal1.setProposalNDrones(ProposalNDrones.valueOf(100));
//            proposal2.setProposalNDrones(ProposalNDrones.valueOf(50)); // Different number of drones
//
//            // All 'sameAs' relevant fields are still identical from setUpSameAs()
//            assertTrue(proposal1.sameAs(proposal2), "sameAs should ignore description and proposalNDrones fields");
//        }
//    }
//
//    @Nested
//    @DisplayName("toString() Method Tests")
//    class ToStringTests {
//
//        @Test
//        @DisplayName("toString() on a fully populated object returns a non-empty detailed string")
//        void toString_fullyPopulated_returnsDetailedString() {
//            ShowProposal proposal = createTestShowProposalInstance(); // Base setup
//            // Simulate ID being set (e.g., by JPA after persistence)
//            // For unit test, we can't set private 'id'. toString() handles null id.
//            // Let's assume id is null for this unit test, so it will show "N/A".
//            // If we could set id: proposal.setId(1L);
//
//            proposal.setDescription("Detailed Test Proposal");
//            proposal.setStatus(ProposalStatus.SAFE);
//            proposal.setVideoLink(ProposalVideoLink.valueOf("http://example.com/video.mp4"));
//
//            // Mock manager username
//            Username testUsername = Username.valueOf("testmanager");
//            when(mockManager.username()).thenReturn(testUsername);
//            proposal.setManager(mockManager);
//
//            // Add a figure with a specific toString
//            when(mockProposalFigure1.toString()).thenReturn("FigureDetails[MockedFigure1]");
//            proposal.getFigures().add(mockProposalFigure1);
//
//            String str = proposal.toString();
//            assertNotNull(str);
//            assertFalse(str.isEmpty());
//
//            assertTrue(str.contains("Show Proposal ID: N/A")); // ID is null
//            assertTrue(str.contains("Description: Detailed Test Proposal"));
//            assertTrue(str.contains("Status: SAFE"));
//            assertTrue(str.contains("Proposed Show Date: " + validShowDate.date().toString()));
//            assertTrue(str.contains("Proposed Place: Lat: 45.0, Lon: 45.0"));
//            assertTrue(str.contains("Proposed Duration: 30 minutes"));
//            assertTrue(str.contains("Number of Drones: 10"));
//            assertTrue(str.contains("Insurance Amount: 1000"));
//            assertTrue(str.contains("Manager: testmanager"));
//            assertTrue(str.contains("Video Link: http://example.com/video.mp4"));
//            assertTrue(str.contains("FigureDetails[MockedFigure1]"));
//        }
//
//        @Test
//        @DisplayName("toString() on a default (JPA) constructed object handles nulls/defaults gracefully")
//        void toString_defaultConstructed_handlesNullsAndDefaults() {
//            ShowProposal proposal = new ShowProposal(); // Uses default constructor
//            String str = proposal.toString();
//
//            assertNotNull(str);
//            assertFalse(str.isEmpty());
//            assertTrue(str.contains("Show Proposal ID: N/A"));
//            assertTrue(str.contains("Description: N/A")); // Default is null, toString shows N/A
//            assertTrue(str.contains("Status: INCOMPLETE"));
//            assertTrue(str.contains("Creation Date: " + LocalDate.now().toString())); // Default is LocalDate.now()
//            assertTrue(str.contains("Proposed Show Date: N/A")); // Default ProposedShowDate.date() is null
//            assertTrue(str.contains("Proposed Place: N/A")); // Default ProposedPlace lat/lon are null
//            assertTrue(str.contains("Proposed Duration: N/A")); // Default ProposedDuration.minutes() is 0
//            assertTrue(str.contains("Number of Drones: N/A")); // Default ProposalNDrones.number() is 0
//            // Default Insurance.getInsuranceAmount() is 0L. toString() logic for insurance:
//            // `if (this.insuranceAmount != null && this.insuranceAmount.getInsuranceAmount() != null)`
//            // Default constructor sets `this.insuranceAmount = new Insurance();` which sets amount to 0L.
//            // So it should print "0" not "N/A".
//            assertTrue(str.contains("Insurance Amount: 0"));
//            assertTrue(str.contains("Manager: N/A"));
//            assertTrue(str.contains("Video Link: No Video Link Provided")); // Default videoLink.video() is null
//            assertTrue(str.contains("Figures: \n")); // Empty list, followed by ----
//            assertTrue(str.endsWith("-------------------------------------"));
//        }
//    }

}