Optimizely IntelliJ Plugin
===================

A plugin for all versions of IntelliJ. 
The plugin uses the Optimizely Java open source SDK.  It polls for datafile updates so that the latest datafile is available while you are editing.  The plugin is designed to be as light weight as possible so as not to slow down any other features.  You cut and paste your sdk key into the set sdk key dialog and then use the keyboard shortcuts to insert the appropriate Optimizely key.

The basic actions that are supported:
In the pulldown menu, there is a Optimizely option.  From there you can go to:
Set SDK Key -> set an sdk key
If the SDK key is set, it is displayed at the top level and the Jump To menu is available.
Jump To -> Experiments, Features -> open the Optimizely application edit page for the experiment or feature using your default browser.
List of Experiments-> experiment key. If chosen, it will insert that key string at the current editor cursor location.
List of Features-> feature key. If chosen, it will insert that key string at the current editor cursor location.
List of Variations -> variation key or variable key dending on the last experiment or feature chosen. If chosen, it will insert that key string at the current editor cursor location.
List of Attritutes -> valid attribute keys. If chosen, it will insert that key string at the current editor cursor location.
List of Events -> valid event keys. If chosen, it will insert that key string at the current editor cursor location.
Direct keyboard access can be achieved for inplace editing.  Below is a list of keyboard shortcuts:
option or alt o :
s - set the sdk key.
f - list of feature keys.
e - list of experiment keys.
v - list of variable or variation keys depending on which feature or experiment was chosen last.
t - list of event keys
a - list of attribute keys.

Intellij and AndroidStudio can use default autocomplete for feature methods such as isFeatureEnabled and getFeatureVariable to return only a feature key list. Same holds true for experiment methods.

#### Gradle

There are different gradle configurations depending on where you are building and publishing to.  It also creates the appropriate jar and prepares for upload the the plugin repository.

#### Dependencies
The Optimizely Plugin uses the [Optimizely Java SDK](https://github.com/optimizely/java-sdk)
`core-api` requires [org.slf4j:slf4j-api:1.7.16](https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.16) and a supported JSON parser. 
We currently integrate with [Jackson](https://github.com/FasterXML/jackson), [GSON](https://github.com/google/gson), [json.org](http://www.json.org),
and [json-simple](https://code.google.com/archive/p/json-simple); if any of those packages are available at runtime, they will be used by `core-api`.
If none of those packages are already provided in your project's classpath, one will need to be added. `core-httpclient-impl` is an optional 
dependency that implements the event dispatcher and requires [org.apache.httpcomponents:httpclient:4.5.2](https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient/4.5.2).
The supplied `pom` files on Bintray define module dependencies.

### Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md).

### Credits

First-party code (under core-api/ and core-httpclient-impl) is copyright Optimizely, Inc. and contributors, licensed under Apache 2.0.

### Additional Code

This software incorporates code from the following open source projects:

#### core-api module

**SLF4J** [https://www.slf4j.org ](https://www.slf4j.org)  
Copyright &copy; 2004-2017 QOS.ch  
License (MIT): [https://www.slf4j.org/license.html](https://www.slf4j.org/license.html)

**Jackson Annotations** [https://github.com/FasterXML/jackson-annotations](https://github.com/FasterXML/jackson-annotations)  
License (Apache 2.0): [https://github.com/FasterXML/jackson-annotations/blob/master/src/main/resources/META-INF/LICENSE](https://github.com/FasterXML/jackson-annotations/blob/master/src/main/resources/META-INF/LICENSE)

**Gson** [https://github.com/google/gson ](https://github.com/google/gson)  
Copyright &copy; 2008 Google Inc.
License (Apache 2.0): [https://github.com/google/gson/blob/master/LICENSE](https://github.com/google/gson/blob/master/LICENSE)

**JSON-java** [https://github.com/stleary/JSON-java](https://github.com/stleary/JSON-java)  
Copyright &copy; 2002 JSON.org 
License (The JSON License): [https://github.com/stleary/JSON-java/blob/master/LICENSE](https://github.com/stleary/JSON-java/blob/master/LICENSE)

**JSON.simple** [https://code.google.com/archive/p/json-simple/](https://code.google.com/archive/p/json-simple/)  
Copyright &copy; January 2004  
License (Apache 2.0): [https://github.com/fangyidong/json-simple/blob/master/LICENSE.txt](https://github.com/fangyidong/json-simple/blob/master/LICENSE.txt)

**Jackson Databind** [https://github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind)   
License (Apache 2.0): [https://github.com/FasterXML/jackson-databind/blob/master/src/main/resources/META-INF/LICENSE](https://github.com/FasterXML/jackson-databind/blob/master/src/main/resources/META-INF/LICENSE)

#### core-httpclient-impl module

**Gson** [https://github.com/google/gson ](https://github.com/google/gson)  
Copyright &copy; 2008 Google Inc.
License (Apache 2.0): [https://github.com/google/gson/blob/master/LICENSE](https://github.com/google/gson/blob/master/LICENSE)

**Apache HttpClient** [https://hc.apache.org/httpcomponents-client-ga/index.html ](https://hc.apache.org/httpcomponents-client-ga/index.html)  
Copyright &copy; January 2004
License (Apache 2.0): [https://github.com/apache/httpcomponents-client/blob/master/LICENSE.txt](https://github.com/apache/httpcomponents-client/blob/master/LICENSE.txt)
