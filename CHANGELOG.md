# Optimizely IntelliJ Plugin

## 0.2.6
Sept 3, 2020
- Fix kotlin autocomplete order for 2020 builds.
- Update to Optimizely SDK 3.5 and add featureJSON support.
- Add support for featureJSON and getAllFeatureVariables in debug dialog.

## 0.2.5
May 15, 2020
Fix kotlin autocomplete for experiment and variation.

## 0.2.4
May 14, 2020
Fix kotlin autocomplete.

## 0.2.3
May 8, 2020
Fix debug dialog attribute table size and add/delete

## 0.2.2
May 7, 2020
Set debug dialog preferred size.

## 0.2.1
April 25, 2020
Downgrade the IntelliJ build number to cover all SDKs.  Fix debug dialog so that appropriate variables show up when
first picking a feature api.

## 0.2.0
April 23, 2020
Add a Debug dialog that allows you to run experiments, and features without sending backend data.  This way you can confirm your setup before you event complete your project.

## 0.1.1
April 13, 2020
Add support for auto-complete for the variable name in feature variable getters.  
So, now if you use auto-complete for the second parameter of getFeatureVariable, you will get a list of feature variables 
of that type.

## 0.1.0
March 14, 2020
Created one plugin for all versions via one gradle file.

## 0.0.1
March 14, 2020

A plugin for all versions of IntelliJ.  The basic actions that are support are:<br/>
In the pulldown menu, there is a Optimizely option.  From there you can go to:<br/>
Set SDK Key -> set an sdk key<br/>
If the SDK key is set, it is displayed at the top level and the Jump To menu is available.<br/>
Jump To -> Experiments or Features -> open the Optimizely application edit page for the experiment or feature.<br/>
List of Experiments-> experiment key. If chosen, it will insert that key string at the current editor cursor location.  
List of Features-> feature key. If chosen, it will insert that key string at the current editor cursor location.  
List of Variations -> variation key or variable key dending on the last experiment or feature chosen. If chosen, it will insert that key string at the current editor cursor location.<br/> 
List of Attritutes -> valid attribute keys. If chosen, it will insert that key string at the current editor cursor location.<br/>
List of Events -> valid event keys. If chosen, it will insert that key string at the current editor cursor location.<br/>
Direct keyboard access can be achieved for inplace editing.  Below is a list of keyboard shortcuts:<br/>
option or alt o :<br/>
s - set the sdk key.<br/>
f - list of feature keys.<br/>
e - list of experiment keys.<br/>
v - list of variable or variation keys depending on which feature or experiment was chosen last.<br/>
t - list of event keys<br/>
a - list of attribute keys.<br/>

Golang, Java, Kotlin and Python can use default autocomplete for feature methods such as isFeatureEnabled and getFeatureVariable to return only a feature key list. Same holds true for experiment methods. 
