#!/usr/bin/env bash

export BASE_CP="shodrone.daemon.proposal/target/daemon.proposal-1.0.0.jar:shodrone.daemon.proposal/target/dependency/*"

#REM call the java VM, e.g,
java -cp "$BASE_CP" eapli.shodrone.daemon.customer.ShodroneDaemon

