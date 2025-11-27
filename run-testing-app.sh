#!/usr/bin/env bash

#REM set the class path,
#REM assumes the build was executed with maven copy-dependencies
BASE_CP="shodrone.app.testing.console/target/testing-1.0.0.jar:shodrone.app.testing.console/target/dependency/*:shodrone.app.testing.console/libs/*"

#REM call the java VM, e.g,
java -cp "$BASE_CP" ShodroneTestingApp