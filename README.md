<p align="center">
  <img src="https://raw.githubusercontent.com/nunomaduro/phpinsights/master/art/logo.gif" width="334" alt="PHP Insights">
  <img src="https://raw.githubusercontent.com/olivernybroe/phpinsights-intellij/master/art/preview.png" width="882" alt="PHP Insights Preview">
</p>

[![JetBrains plugins](https://img.shields.io/jetbrains/plugin/d/13004-php-insights.svg)](https://plugins.jetbrains.com/plugin/13004-php-insights)
![GitHub stars](https://img.shields.io/github/stars/olivernybroe/phpinsights-intellij.svg?label=Stars)
![Jetbrains rating](https://img.shields.io/badge/dynamic/json.svg?label=JetBrains%20rating&url=https%3A%2F%2Fplugins.jetbrains.com%2Fplugin%2FgetPluginInfo%3FpluginId%3D13004&query=%24.totalRating&suffix=/5)
[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/13004-php-insights.svg)](https://plugins.jetbrains.com/plugin/13004-php-insights)

### PHP Insights support built into PHPStorm
With this plugin you will now be able to use PHP Insights directly in PHPStorm. The files will be automatically analysed
and errors are shown directly on the line.

## Installation
To install the plugin you can simply open up the marketplace in your IDE and search for `PHP Insights`.

### Manual installation
If you for some reason prefer not to use the marketplace, then you can download the latest version of the plugin here 
and use the install from disk feature in PHPStorm.


## Configuration
After you have installed the plugin you will have to setup the path to your php insight installation.  
This can be both a global installation or a per project based installation.

For setting the path go to `Languages & Frameworks > PHP > Quality Tools > PHP Insights` and set the path to your path.  
Normally you can find it under `vendor/bin/phpinsights` in your project folder.

![Tool Settings](https://raw.githubusercontent.com/olivernybroe/phpinsights-intellij/master/art/tool_settings.png)

You also have the option to configure the severity of the messages, just like any other inspection.  
You can find the inspection settings under `Editor > Inspections > PHP > Quality Tools > PHP Insights validation`.  
This is also where you define your configuration file which you want php insight to use.

![Inspection Settings](https://raw.githubusercontent.com/olivernybroe/phpinsights-intellij/master/art/inspections_settings.png)


## FAQ

### Does the plugin work for other IDE?
The plugin only works for IntelliJ based IDE's which have the PHP plugin installed.  
You can for example use it in IntelliJ IDEA Ultimate if you install the PHP plugin. Afterwards this plugin should
be available in the marketplace as usual.

### How do I use a custom config file?
You can use a custom config file by going to `Editor > Inspections` and search for `PHP Insights`.  
From here you should have the option to set the path to a config file.

### I have PHP Insights installed globally, can I still use it?
Absolutely, you just go to your Quality tools settings and define the path to your global composer installation folder.
