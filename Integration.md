# Installation #

Developed and tested under Eclipse 3.3.1.1.
Requires JDT, EMF 2.3 and Java 5 or higher.

Update site URL: http://java2ecore.googlecode.com/svn/trunk/org.abreslav.java2ecore.updatesite/site.xml

# How does Java2Ecore integrate with Eclipse #

It just uses standard Java editor for entering text.

In addition it defines a project builder and a nature.

To start using Java2Ecore on some files in your project you have to perform the following steps:
  * Your project must be a Java project (either plug-in project or not)
![http://content.screencast.com/media/0dc63ae8-799c-4f68-9420-a2baeefdb29a_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png](http://content.screencast.com/media/0dc63ae8-799c-4f68-9420-a2baeefdb29a_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png)
  * Right-click your project and choose "Toggle Java2Ecore nature"
![http://content.screencast.com/media/3da30d7b-a016-4163-948d-f1a2c7d5528d_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png](http://content.screencast.com/media/3da30d7b-a016-4163-948d-f1a2c7d5528d_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png)

  * Java2Ecore builder will be added to your project
  * `java2ecore.jar` will be added on the project's classpath
  * `ecores` source folder will be created
    * It would have been much better if we would use some property page but for now there's just a hard-coded name
    * Now for all the `*`.java files placed under `ecores` source folder our builder will work and produce models
![http://content.screencast.com/media/0853a114-e54b-4eff-bd94-f06a0c906e8c_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png](http://content.screencast.com/media/0853a114-e54b-4eff-bd94-f06a0c906e8c_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png)
    * Models are put to `<project>/models` folder (it's hard-coded too :( )
    * File names are `<package_name>.ecore`
![http://content.screencast.com/media/0541fdd0-b130-4584-9e98-483e16c434ad_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png](http://content.screencast.com/media/0541fdd0-b130-4584-9e98-483e16c434ad_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png)