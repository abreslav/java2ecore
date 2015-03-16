# NOTE: The project is fozen due to motivation issues. If you are interested in it (not supporting by any means, but simply using), please, let me know by [submitting a bug](http://code.google.com/p/java2ecore/issues/entry) or any way you like. Thank you for visiting this page #

![http://java2ecore.googlecode.com/svn/trunk/logo/java2ecore.png](http://java2ecore.googlecode.com/svn/trunk/logo/java2ecore.png)

Ever worked with [Emfatic](http://www.alphaworks.ibm.com/tech/emfatic)? Well, it's nice but very old. For example, it does not support generics. And the editor is not so good.
But there's a JDT Java editor which is very powerful, and Emfatic syntax is so much alike Java...
Well, I'm working on this project to make Java be a syntax for Ecore.

There's a set of annotations inside and a simple Eclipse plug-in which just adds a builder that can convert your "special Java" to Ecore. Checkout our wiki for more details:
  * [Overview](Java2Ecore.md)
  * Update Site URL: http://java2ecore.googlecode.com/svn/trunk/org.abreslav.java2ecore.updatesite/site.xml
  * [Eclipse integration](Integration.md)
  * [Syntax](Syntax.md) description
  * [JavaDoc](http://java2ecore.googlecode.com/svn/trunk/org.abreslav.java2ecore.docs/index.html) for Java2Ecore annotations and library classes

Have a look at our [blog](http://java2ecore.blogspot.com/).

And, please, [rate Java2Ecore at EPIC](http://www.eclipseplugincentral.com/Web_Links-index-req-ratelink-lid-1042.html). You comments will help make Java2Ecore better.

### Current version (0.3 beta) features ###
  * EClasses, EDataTypes, EEnums
    * EAttributes, EReferences, EOperations
    * Generics
  * Nested packages
  * Imports of exiting Ecore models
    * From registered packages
    * From GenModels with generated Java code
  * Brief documentation provided directly in the editor (through JavaDoc)
See [New and noteworthy](NewAndNoteworthy.md)