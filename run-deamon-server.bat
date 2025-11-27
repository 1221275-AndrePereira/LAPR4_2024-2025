REM set the class path,
REM assumes the build was executed with maven copy-dependencies

BASE_CP="shodrone.daemon.proposal/target/daemon.proposal-1.0.0.jar:shodrone.daemon.proposal/target/dependency/*"

java -cp "$BASE_CP" eapli.shodrone.daemon.customer.ShodroneDaemon