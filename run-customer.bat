REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET BASE_CP=shodrone.app.customer.console\target\shodrone.app.customer.console-0.1.0.jar;shodrone.app.customer.console\target\dependency\*;

REM call the java VM, e.g,
java -cp %BASE_CP% shodrone.app.customer.console.hello.HelloWorld