REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET BASE_CP=shodrone.app.backoffice.console\target\app.backoffice.console-1.0.0.jar;shodrone.app.backoffice.console\target\dependency\*

REM call the java VM, e.g,
java -cp %BASE_CP% .ShodroneBackOffice