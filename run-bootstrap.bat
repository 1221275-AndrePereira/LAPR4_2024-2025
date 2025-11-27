REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET BASE_CP=shodrone.app.bootstrap\target\app.bootstrap-1.0.0.jar;shodrone.app.bootstrap\target\dependency\*;

REM call the java VM, e.g,
java -cp %BASE_CP% bootstrap.ShodroneBootstrap