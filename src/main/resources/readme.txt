============
jinFORM v0.2
============

1. Installation

		Orbeon Presentation Server (OPS) must be installed and correctly running before deploying jinFORM.
		
		The minimum version of OPS to use is any official release or nightly build after November 15th 2006.

				Releases      : http://download.forge.objectweb.org/ops/
				Nightly builds: http://forge.objectweb.org/nightlybuilds/ops/ops/

		Add in OPS' global page-flow.xml:
        
        <page id="jinform" path-info="/jinform*" model="/jinform/page-flow.xpl"/>
    
    Create a jinform folder under /ops.war/WEB-INF/resources and copy the content of the ops folder into it.
     
   	Copy jinform.properties file to you application server's classpath (or simply put it in jinform.war)
   	

2. Configuration

		Edit jinform.properties and adapt the different entries to your environment.
		
		
3. Starting
   
   Point your browser to http://localhost:8080/jinform (or the equivalent adapted to your environment)
   and you can start filling Infopath forms on the web!
   
    
4. XSN Format Support  
   
   	If you want to have native support of Infopath XSN files, you must have ms-cab.jar (i.e. the
   	com.ms.util.cab package from Microsoft JDK) in your application server classpath. This allows
   	handling XSN files even on non-windows systems (tested on Solaris and Linus).
   	
   	It is not possible to distribute this jar with the project as it is copyrighted by Microsoft. Should
   	you have troubles finding it, contact the author for help.
   	
   	Without it jinFORM will only work with XSN files (CAB archives) converted to Zip archives. For Windows
   	users, IZarc (http://www.izarc.org) performs CAB to ZIP conversion neatly and for free.
   	
   