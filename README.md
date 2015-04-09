Sonar-grammar
=============

Sonar Grammar Plugin to provide spell checking of source code. 

As a spell checking library Jazzy is used http://jazzy.sourceforge.net/

Requirements environment:
==========================
1. Java 1.7
2. Maven
3. SonarQube(current version 5.1)

SonarQube install steps:
========================
1. Download SonarQube (current version 5.1) from  [Sonarqube homepage](http://www.sonarqube.org/downloads/)
2. Unpack and go to \sonarqube-5.1\bin\ folder
3. Select you current OS and run sonar(can be ".bat" or ".sh" file)
4. In browser go to -  [http://localhost:9000/](http://localhost:9000/)
5. Login in Sonar as Administrator (login - admin, password - admin)
6. On Sonar page top menu go to the "Quality Profiles". You need to make sure that "Sonar way " stands as the default
7. Open terminal(comand line), go to the project that you want to test and write "mvn sonar:sonar" command. For debug use "mvnDebug sonar:sonar" command(port 8000)
8. After that you can see results on [http://localhost:9000/dashboard/](http://localhost:9000/dashboard/) or follow the link that will be found in bottom command line

Sonar-Grammar-Plugin use:
=========================
1. Use maven for build project
2. In "sonar-grammar\target\" folder will be "sonar-grammar-plugin-XXX.jar" file. Copy this file to local SonarQubefolder - "sonarqube-5.1\extensions\plugins\"
3. Restart local SonarQube
4. Open SonarQube console - [http://localhost:9000/](http://localhost:9000/) and login as Administrator(login - admin, password - admin)
5. Go to the tab "Quality Profiles" and set Sonar Grammar Profile as Default
6. (Optional) for check not only grammar plugin but "Sonnar way" and "Grammar" together. Set inheritance for Grammar profile:
"Quality Profiles" - select "Sonar Grammar Profile" - click "Profile Inheritance" and select  inheritance from "Sonnar way" 
in drop-down list
8. Plugin ready for work. You can use step 7-8 from "SonarQube install steps"


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
To do this - append new word to **dict/english.0**, run **reprocess_dict.sh** after that copy **dict/english.1** with new word and in correct format to **src/main/resources/dict/english.0**.



