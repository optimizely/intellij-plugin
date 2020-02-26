# Optimizely IntelliJ Plugin

## 0.0.1
March 14, 2020

A plugin for all versions of IntelliJ.  The basic actions that are support are:
In the pulldown menu, there is a Optimizely option.  From there you can go to:
Set SDK Key -> set an sdk key
If the SDK key is set, it is displayed at the top level and the Jump To menu is available.
Jump To -> Experiments, Features -> open the Optimizely application edit page for the experiment or feature.
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
