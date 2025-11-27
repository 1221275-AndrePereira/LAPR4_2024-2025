#!/usr/bin/env bash

#REM set the class path,
#REM assumes the build was executed with maven copy-dependencies
BASE_CP="shodrone.app.bootstrap/target/app.bootstrap-1.0.0.jar:shodrone.app.bootstrap/target/dependency/*:shodrone.app.boostrap/libs/*"

#REM call the java VM, e.g,
java -cp "$BASE_CP" bootstrap.ShodroneBootstrap

#java --add-opens java.base/java.lang=ALL-UNNAMED \
#     --add-opens java.base/java.util=ALL-UNNAMED \
#     --add-opens java.base/java.nio=ALL-UNNAMED \
#     --add-opens java.base/java.lang.invoke=ALL-UNNAMED \
#     --add-opens java.base/java.net=ALL-UNNAMED \
#     --add-opens java.base/sun.net.www.protocol.http=ALL-UNNAMED \
#     --add-opens java.base/sun.net.www.protocol.https=ALL-UNNAMED \
#     --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
#     -cp "$BASE_CP" bootstrap.ShodroneBootstrap