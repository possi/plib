plib
====

Possis Library is just another Bukkit Plugin Library
----------------------------------------------------

The main goal to achiev with this library is to generate the plugin.yml and config.yml automatically from source code.
So adding new permissions or increasing version number in pom.xml doesn't require you to update plugin.yml.
Make your config.yml auto-update able.

To generate the plugin.yml before jar-generation you have to use [Maven](http://maven.apache.org/) to build your project.
As long as there is no documentation take a look at the pom.xml from LimitedCreative.

Possis Code-Conventions
-----------------------
- 4 Spaces, no Tabs.
- Opening braces in same line
- Max. line length of 120 chars is explicit optional! (Don't come up with breaking at 80 chars, that doesn't help anyone!)

CheckStyle configuration: https://dl.dropbox.com/u/5023975/coding/checkstyle.xml

How to use CheckStyle in Eclipse: http://eclipse-cs.sourceforge.net/downloads.html then Right-click the Project and
Checkstyle -> Actiate Checkstyle