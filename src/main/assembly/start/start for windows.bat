echo off

set APP_NAME=${project.build.finalName}.jar

:jmx
java -server -jar lib/%APP_NAME%
goto end

:end
pause