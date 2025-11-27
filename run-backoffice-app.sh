#!/usr/bin/env bash

#REM set the class path,
#REM assumes the build was executed with maven copy-dependencies
BASE_CP="shodrone.app.backoffice.console/target/backoffice-1.0.0.jar:shodrone.app.backoffice.console/target/dependency/*:shodrone.app.backoffice.console/libs/*"

#REM call the java VM, e.g,
java -cp "$BASE_CP" ShodroneBackOffice

#java --add-opens java.base/java.lang=ALL-UNNAMED \
#     --add-opens java.base/java.util=ALL-UNNAMED \
#     --add-opens java.base/java.nio=ALL-UNNAMED \
#     --add-opens java.base/java.lang.invoke=ALL-UNNAMED \
#     --add-opens java.base/java.net=ALL-UNNAMED \
#     --add-opens java.base/sun.net.www.protocol.http=ALL-UNNAMED \
#     --add-opens java.base/sun.net.www.protocol.https=ALL-UNNAMED \
#     --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
#     -cp "$BASE_CP" ShodroneBackOffice