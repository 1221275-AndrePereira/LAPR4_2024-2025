/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.shodrone.infrastructure.persistence;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.shodrone.drone.repository.DroneInventoryRepository;
import eapli.shodrone.droneModel.repository.DroneModelRepository;
import eapli.shodrone.maintenanceType.repositories.MaintenanceTypeRepository;
import eapli.shodrone.proposalDocumentTemplate.repositories.ProposalDocumentTemplateRepository;
import eapli.shodrone.shodroneusermanagement.repository.ShodroneUserRepository;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventConsumptionRepository;
import eapli.shodrone.figurecategory.repositories.FigureCategoryCatalogue;
import eapli.shodrone.showProposal.repositories.ShowProposalRepository;
import eapli.shodrone.showrequest.repositories.ShowRequestRepository;

/**
 * The interface for the repository factory of eCafeteria.
 * <p>
 * This is the Abstract Factory in the Abstract Factory (GoF) pattern. Each of
 * the return types is an Abstract Product. For instance,
 * JpaDishRepository} is a Concrete Product.
 * </p>
 *
 * @author Paulo Gandra Sousa
 */
public interface RepositoryFactory {

	/**
	 * Factory method to create a transactional context to use in the repositories
	 *
	 * @return
	 */
	TransactionalContext newTransactionalContext();

	/**
	 * @param autoTx the transactional context to enroll
	 * @return
	 */
	UserRepository users(TransactionalContext autoTx);

	/**
	 * repository will be created in auto transaction mode
	 *
	 * @return
	 */
	UserRepository users();

	FigureRepository figures();

	FigureRepository figures( TransactionalContext autoTx );

	FigureCategoryCatalogue figureCategories();

	FigureCategoryCatalogue figureCategories(TransactionalContext txCtx);

	MaintenanceTypeRepository maintenanceTypes();

	MaintenanceTypeRepository maintenanceTypes(TransactionalContext autoTx);

	ShodroneUserRepository shodroneUsers(TransactionalContext txCtx);

	ShodroneUserRepository shodroneUsers();

	ShowRequestRepository showRequestCatalogue(TransactionalContext txCtx);

    ShowRequestRepository showRequestCatalogue();

	DroneModelRepository droneModels();
	DroneModelRepository droneModels(TransactionalContext autoTx);

	DroneInventoryRepository droneInventory();
	DroneInventoryRepository droneInventory(TransactionalContext autoTx);

	ShowProposalRepository showProposals(TransactionalContext autoTx);
	ShowProposalRepository showProposals();

	ProposalDocumentTemplateRepository proposalDocumentTemplates(TransactionalContext autoTx);

	ProposalDocumentTemplateRepository proposalDocumentTemplates();

}
