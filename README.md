Optimizely IntelliJ Plugin
==========================

This plugin provides quick contextual access to the configuration of your Optimizely Project, and is available for IntelliJ Platform based IDEs (AndroidStudio, GoLand, IntelliJ, and PyCharm).

Based around the [Optimizely Java SDK](https://github.com/optimizely/java-sdk), the plugin provides a set of shortcuts and contextual links back to the relevant entities within your Optimizely application.

After installation, use the menu or keyboard shortcut to set your [SDK Key](https://docs.developers.optimizely.com/full-stack/docs/get-the-datafile#section-access-the-datafile-via-the-app), which corresponds to a particular Environment in your Optimizely Full Stack Project, and is available under the Settings tab.  

Once the SDK key is in place, the active Experiments, Features, Attributes and Events from your Environment will be accessible both via the Dropdown menu under Tools, and via keyboard shortcuts.

## Menu Options
#### Set SDK Key
Set or update your SDK Key.
If the SDK key is set, it is displayed at the top level and the Jump To menu is available.

#### Jump To
Provide direct links to your Experiments and Features within the Optimizely application, opening within your default OS browser.

#### List Experiment Keys
Displays a dropdown of the Experiment Keys of all Experiments that are currently Running within your selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List Feature Keys
Displays a list of the Feature Keys of all non-archived Features within your selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List Variation Keys
Displays the Variation Key or Variable Key of the most recently selected Experiment or Feature. Selecting a key will insert that key string at the current editor cursor location.

#### List Attribute Keys
Displays all non-archived Attribute keys within the selected environment. Selecting a key will insert that key string at the current editor cursor location.

#### List Event Keys
Displays a list of all non-archived event keys within the selected environment.Selecting a key will insert that key string at the current editor cursor location.


### Keyboard Shortcuts
Direct keyboard access can be achieved for in-place editing.  All shortcuts can be initiated by entering `option` or `alt o`

`s` - set the sdk key.

`f` - list of feature keys.

`e` - list of experiment keys.

`v` - list of variable or variation keys depending on which feature or experiment was chosen last.

`t` - list of event keys

`a` - list of attribute keys.

Default autocomplete for feature methods such as `isFeatureEnabled` and `getFeatureVariable` to return only a feature key list. Same holds true for experiment methods.

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

**SLF4J** [https://www.slf4j.org ](https://www.slf4j.org)  
Copyright &copy; 2004-2017 QOS.ch  
License (MIT): [https://www.slf4j.org/license.html](https://www.slf4j.org/license.html)

**Jackson Annotations** [https://github.com/FasterXML/jackson-annotations](https://github.com/FasterXML/jackson-annotations)  
License (Apache 2.0): [https://github.com/FasterXML/jackson-annotations/blob/master/src/main/resources/META-INF/LICENSE](https://github.com/FasterXML/jackson-annotations/blob/master/src/main/resources/META-INF/LICENSE)

**Jackson Databind** [https://github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind)   
License (Apache 2.0): [https://github.com/FasterXML/jackson-databind/blob/master/src/main/resources/META-INF/LICENSE](https://github.com/FasterXML/jackson-databind/blob/master/src/main/resources/META-INF/LICENSE)

**Apache HttpClient** [https://hc.apache.org/httpcomponents-client-ga/index.html ](https://hc.apache.org/httpcomponents-client-ga/index.html)  
Copyright &copy; January 2004
License (Apache 2.0): [https://github.com/apache/httpcomponents-client/blob/master/LICENSE.txt](https://github.com/apache/httpcomponents-client/blob/master/LICENSE.txt)

**Logback Classic Module** 
Eclipse Public License - v 1.0:	[http://www.eclipse.org/legal/epl-v10.html](http://www.eclipse.org/legal/epl-v10.html)
GNU Lesser General Public License: [http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html](http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html)
