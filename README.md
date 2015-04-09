sonar-grammar 
=============

Sonar Grammar Plugin to provide spell checking of source code. 

As a spell checking library Jazzy is used http://jazzy.sourceforge.net/

Requirements environment:
==========================
-Java 1.7
-Maven
-Idea
-SonarQube(current version 5.1)

SonarQube install steps:
========================
1. Download SonarQube (current version 5.1) from site - http://www.sonarqube.org/downloads/
2. Unpack and go to \sonarqube-5.1\bin\ folder
3. Select you current OS and run sonar(can be ".bat" or ".sh" file)
4. In browser write -  http://localhost:9000/
5. Login in sonar as Administrator (login - admin, password - admin)
6. In Sonar console go to the tab "Quality Profiles". You need to make sure that "Sonar way " stands as the default
7. Open terminal(comand line), go to the project that you want to test and write "mvn sonar:sonar" command. For debug use "mvnDebug sonar:sonar" command(port 8000)
8. After that you can see results on "http://localhost:9000/dashboard/" or follow the link that will give you the command line

Sonar-Grammar-Plugin use:
=========================
1. In idea use maven for build project
2. In "sonar-grammar\target\" folder will be "sonar-grammar-plugin-0.1-SNAPSHOT.jar" file. Copy this file to local SonarQubefolder - "sonarqube-5.1\extensions\plugins\"
3. Restart local SonarQube
4. Open SonarQube console - http://localhost:9000/ and login as Administrator(login - admin, password - admin)
5. Go to the tab "Quality Profiles" and set Sonar Grammar Profile as Default
6. Plugin ready for work. You can use step 7-8 from "SonarQube install steps"


Status
------

[![Build Status](https://travis-ci.org/webdizz/sonar-grammar.png?branch=master)](https://travis-ci.org/webdizz/sonar-grammar)
[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/webdizz/sonar-grammar/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

Development
-----------

* **To start Sonar**

```bash
mvn install org.codehaus.sonar:sonar-dev-maven-plugin:start-war -Dsonar.runtimeVersion=3.7.3 -Djava.io.tmpdir=/tmp
```
* **To debug** in another console

```bash
export SONAR_RUNNER_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"
mvnDebug sonar:sonar
```

Ammend dictionary
-----------

There is built in dictionary, however to add additional words here is a bash script to rearrange dictionary.
To do this - apend new word to **dict/english.0**, run **reprocess_dict.sh** after that copy **dict/english.1** with new word and in correct format to **src/main/resoources/dict/enslish.0**.



