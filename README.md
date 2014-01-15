sonar-grammar 
=============

Sonar Grammar Plugin to provide spell checking of source code. 

As a spell checking library Jazzy is used http://jazzy.sourceforge.net/

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



