#!/usr/bin/env bash

#REM set the class path,
#REM assumes the build was executed with maven copy-dependencies
BASE_CP="shodrone.app.customer.console/target/customer-1.0.0.jar:shodrone.app.customer.console/target/dependency/*:shodrone.app.customer.console/libs/*"

#REM call the java VM, e.g,
java -cp "$BASE_CP" ShodroneCustomerApp
#echo "Info: Customer console is not implemented yet."