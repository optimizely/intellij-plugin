Optimizely IntelliJ Plugin
===================

This plugin provides quick contextual access to the configuration of your Optimizely Project, and is available for all IntelliJ Platform based IDEs.

Based around the [Optimizely Java SDK](https://github.com/optimizely/java-sdk), the plugin provides a set of shortcuts and contextual links back to the relevant entities within your Optimizely application.

After installation, the plugin will prompt you for your [SDK Key](https://docs.developers.optimizely.com/full-stack/docs/get-the-datafile#section-access-the-datafile-via-the-app), which corresponds to a particular Environment in your Optimizely Full Stack Project, and is available under the Settings tab.  

Once the SDK key is in place, the active Experiments, Features, Attributes and Events from your Environment will be accessible both via the Dropdown menu under Tools, and via keyboard shortcuts.

## Menu Options
#### Set SDK Key
set or update your sdk key.

If the SDK key is set, it is displayed at the top level and the Jump To menu is available.

#### Jump To

Provide direct links to your Experiments and Features within the Optimizely application, opening within your default OS browser.

#### List of Experiments
Displays a dropdown of the Experiment Keys of all Experiments that are currently Running within your selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List of Features
Displays a list of the Feature Keys of all non-archived Features within your selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List of Variations
Displays the Variation Key or Variable Key of the most recently selected Experiment or Feature. Selecting a key will insert that key string at the current editor cursor location.

#### List of Attributes
Displays all non-archived Attribute keys within the selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List of Events
Displays a list of all non-archived event keys within the selected environment.Selecting a key will insert that key string at the current editor cursor location.


### Keyboard Shortcuts
Direct keyboard access can be achieved for in-place editing.  All shortcuts can be initiated by entering `option` or `alt o`

`s` - set the sdk key.

`f` - list of feature keys.

`e` - list of experiment keys.

`v` - list of variable or variation keys depending on which feature or experiment was chosen last.

`t` - list of event keys

`a` - list of attribute keys.

Intellij and AndroidStudio can use default autocomplete for feature methods such as isFeatureEnabled and getFeatureVariable to return only a feature key list. Same holds true for experiment methods.

#### Gradle

There are different gradle configurations depending on where you are building and publishing to.  It also creates the appropriate jar and prepares for upload the the plugin repository.

#### Dependencies
The Optimizely Plugin uses the [Optimizely Java SDK](https://github.com/optimizely/java-sdk)
`core-api` requires [org.slf4j:slf4j-api:1.7.16](https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.16) and a supported JSON parser. 
We currently integrate with [Jackson](https://github.com/FasterXML/jackson), [GSON](https://github.com/google/gson), [json.org](http://www.json.org),
and [json-simple](https://code.google.com/archive/p/json-simple); if any of those packages are available at runtime, they will be used by `core-api`.

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
