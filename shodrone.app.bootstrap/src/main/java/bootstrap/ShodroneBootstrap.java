/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package bootstrap;

import console.BaseApplication;

import eapli.framework.collections.util.ArrayPredicates;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import eapli.framework.io.util.Console;

import eapli.shodrone.infrastructure.boostrapers.ShodroneBootstrapper;
import eapli.shodrone.infrastructure.boostrapers.demo.ShodroneDemoBootstrapper;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.infrastructure.smoketesters.ShodroneDemoSmokeTester;
import eapli.shodrone.usermanagement.domain.ShodronePasswordPolicy;

/**
 * The ShodroneBootstrap class is the entry point for bootstrapping the Shodrone application.
 * It extends the BaseApplication class and provides the implementation to initialize
 * the application, handle command-line arguments, bootstrap data, and execute specific
 * scenarios for demonstration or testing purposes.
 * <p>
 * This final class cannot be instantiated directly. The main method serves as the execution
 * starting point. The application supports several optional command-line arguments to
 * dictate the execution behavior, such as initializing demo data, running end-to-end
 * testing scenarios, or waiting for user input at the end.
 */
@SuppressWarnings("squid:S106")
public final class ShodroneBootstrap extends BaseApplication {

    private boolean isToBootstrapDemoData;
    private boolean isToRunSampleE2E;
    private boolean isToWaitInTheEnd;

    /**
     * avoid instantiation of this class.
     */
    private ShodroneBootstrap() {
    }

    public static void main(final String[] args) {

        new ShodroneBootstrap().run(args);
    }

    /**
     * Executes the main application logic for bootstrapping and optional operations.
     *
     * This method handles the initialization tasks such as bootstrapping master data,
     * optionally bootstrapping demo data, executing a sample end-to-end test scenario,
     * and waiting for user input at the end based on provided command-line arguments.
     *
     * @param args an array of command-line arguments specifying the execution options.
     *             Supported arguments include:
     *             - "-bootstrap:demo": Enables bootstrapping of demo data.
     *             - "-smoke:e2e": Executes a basic end-to-end scenario (implies demo data bootstrapping).
     *             - "-wait": Prompts the user to press Enter to finish the program.
     */
    @Override
    protected void doMain(final String[] args) {
        handleArgs(args);

        System.out.println("\n\n------- MASTER DATA -------");
        new ShodroneBootstrapper().execute();

        if (isToBootstrapDemoData) {
            System.out.println("\n\n------- DEMO DATA -------");
            new ShodroneDemoBootstrapper().execute();
        }
        if (isToRunSampleE2E) {
            System.out.println("\n\n------- BASIC SCENARIO -------");
            new ShodroneDemoSmokeTester().execute();
        }

        if (isToWaitInTheEnd) {
            Console.readLine("\n\n>>>>>> Enter to finish the program.");
        }
    }

    private void handleArgs(final String[] args) {
        isToRunSampleE2E = ArrayPredicates.contains(args, "-smoke:e2e");
        if (isToRunSampleE2E) {
            isToBootstrapDemoData = true;
        } else {
            isToBootstrapDemoData = ArrayPredicates.contains(args, "-bootstrap:demo");
        }

        isToWaitInTheEnd = ArrayPredicates.contains(args, "-wait");
    }

    @Override
    protected String appTitle() {
        return "Bootstrapping Shodrone data ";
    }

    @Override
    protected String appGoodbye() {
        return "Bootstrap data done.";
    }

    @Override
    protected void configureAuthz() {
        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
                new PlainTextEncoder());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doSetupEventHandlers(final EventDispatcher dispatcher) {
        //dispatcher.subscribe(new NewUserRegisteredFromSignupWatchDog(), NewUserRegisteredFromSignupEvent.class);
        //dispatcher.subscribe(new SignupShodroneUser(), SignupEvent.class);
    }
}
