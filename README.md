sonar-grammar [![Build Status](https://travis-ci.org/webdizz/sonar-grammar.png?branch=master)](https://travis-ci.org/boxenwebdizz/sonar-grammar) 
=============

Sonar Grammar Plugin to provide spell checking of source code. 

As a spell checking library Jazzy is used http://jazzy.sourceforge.net/



# Development
* **To start Sonar**

```bash
mvn install org.codehaus.sonar:sonar-dev-maven-plugin:start-war -Dsonar.runtimeVersion=3.7.3 -Djava.io.tmpdir=/tmp
```
* **To debug** in another console

```bash
export SONAR_RUNNER_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"
mvnDebug sonar:sonar
```
