Task 1 is solved in file `calculator.groovy`  
To run this file use `groovy calculator.groovy 'exp'`, where expression is mathematical expression to solve  
Calculator supports +,-,*,/ operations considering order (multiplication first)
* Important - expression containing only addition and subtraction could be stated without quotes, but in case multiplication and division quotes are mandatory
Also supports braces which should be evaluated at first
  
Examples of input: 
* '3+5*2' == 13
* '2*(6-1)' == 10
* '8/(4-2)+6' == 10
* '4*(8/(2+2))-7' == 1
* '((3+2)*(4-1))/7' == 2
* '5+((8-3)*2)-1' == 14
* '9+(7-3*(4-2))/(6-1)' == 9
* '((2+4)*(8-3))/((7-1)/(2+1))' == 15

  
Used features from Groovy language:
* Safe navigation (null-safe) operator ?.
* Closures
* Closure Composition Operator <<
* GString
* Groovy operator overloading

Task 2  
Gradle part  
build.gradle contains instruction to assemble .jar and .war files  
To do so use `gradle jar` and `gradle war` correspondingly  
To run .jar use `java -jar app.jar 'args'`  
To run .war servlet container required, Tomcat for instance. Start Tomcat running startup.bat or startup.sh then place .war to tomcat/webapps and go to http://localhost:8080/webapp/
  
Maven part
Each component marked as project, each contains pom.xml with instructions. To create all .jar files invoke `mvn package` from root or each project separately  
To run .jar use `java -jar app.jar 'args'` where app is jar name
Same as with gradle, .war should be placed in tomcat/webapps 