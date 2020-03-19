Optimizely IntelliJ Plugin
==========================

A plugin for all versions of IntelliJ. 
The plugin uses the [Optimizely Java SDK](https://github.com/optimizely/java-sdk).  It polls for datafile updates so that the latest datafile is available while you are editing.  The plugin is designed to be as light weight as possible so as not to slow down any other features.  You cut and paste your SDK key into the Set SDK key dialog and then use the keyboard shortcuts to insert the appropriate Optimizely key.

The following basic actions are supported:

In the pulldown menu, there is a Optimizely option.  From there you can go to:

* Set SDK Key -> set an sdk key.

    * If the SDK key is set, it is displayed at the top level and the Jump To menu is available.

    * Jump To -> Experiments, Features -> open the Optimizely application edit page for the experiment or feature using your default browser.

* List of Experiments-> valid experiment keys. If chosen, it will insert that key string at the current editor cursor location.

* List of Features-> valid feature keys. If chosen, it will insert that key string at the current editor cursor location.

* List of Events -> valid event keys. If chosen, it will insert that key string at the current editor cursor location.

* List of Attributes -> valid attribute keys. If chosen, it will insert that key string at the current editor cursor location.

* List of Variations -> variation key or variable key depending on the last experiment or feature chosen. If chosen, it will insert that key string at the current editor cursor location.


Direct keyboard access can be achieved for in place editing.  Below is a list of keyboard shortcuts:
option or alt o :

* s - set the SDK key.

* f - list of feature keys.

* e - list of experiment keys.

* t - list of event keys.

* a - list of attribute keys.

* v - list of variable or variation keys depending on which feature or experiment was chosen last.

IntelliJ and AndroidStudio can use default autocomplete for feature methods such as `isFeatureEnabled` and `getFeatureVariable` to return only a feature key list. Same holds true for experiment methods.

#### Gradle

There are different gradle configurations depending on where you are building and publishing to.  It also creates the appropriate jar and prepares for upload the the plugin repository.

#### Dependencies
The Optimizely Plugin uses the [Optimizely Java SDK](https://github.com/optimizely/java-sdk)
`core-api` requires [org.slf4j:slf4j-api:1.7.16](https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.16) and a supported JSON parser. 
We currently integrate with [Jackson](https://github.com/FasterXML/jackson), [GSON](https://github.com/google/gson), [json.org](http://www.json.org),
and [json-simple](https://code.google.com/archive/p/json-simple); if any of those packages are available at runtime, they will be used by `core-api`.

### Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md).
