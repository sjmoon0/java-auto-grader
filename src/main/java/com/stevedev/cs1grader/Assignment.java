/* 
 * Copyright (C) 2016 steve
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.stevedev.cs1grader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
* Grades requirements for all labs and projects (compilibility, class name, directory name)
* Specific requirments found in Classes that inherit from this class.
*
* @author Mr. Steven Moon
* @version 2016.08.15
*/
public class Assignment
{
	private static String type;
	private static int num;
	private static String un;
	private static FileWriter studentGradeFile;
	private static FileWriter masterGradeFile;
	private static HashSet<String> STUDENTS;
        private static boolean usingNetbeans;
	private static final int IC_POINTS=1;//Points for initial conditions. Decrease for subsequent labs

	/**
	* Assignment constructor
	* @param type "Lab" or "Project"
	* @param num lab or project number
	*/
	public Assignment(String type, int num, boolean useNetbeans){
		this.type=type;
		this.num=num;
                this.usingNetbeans=useNetbeans;
		this.un=getUsername();
		initializeStudents();
	}
	
	/**
	* Calls the 2 initial conditions:
	* Correct main class name?
	* Correct directory name?
	* @param mainClass The name of the class that contains the main method
	* @return A list of requirements to be graded
	*/
	public static ArrayList<Requirement> checkInitialConditions(String mainClass){
		ArrayList<Requirement> ral = new ArrayList<Requirement>();
		ral.add(checkClassExists(mainClass));
		//Commented this out because only works on dev machine
		//ral.add(checkCompilibility(mainClass));
		ral.add(checkDirectoryName());
		return ral;
	}

	/**
	* The first initial condition: does the class exist?
	* @param mainClass The name of the class that contains the main method
	* @return A single requirement to be graded
	*/
	private static Requirement checkClassExists(String mainClass){
		try{
                    URL location = Assignment.class.getProtectionDomain().getCodeSource().getLocation();
                    URL parentFolder;
                    if(usingNetbeans){
                        parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().substring(0,location.getFile().lastIndexOf("/")).lastIndexOf("/"))+"/build/classes/");
                    }
                    else{
                        parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().lastIndexOf("/"))+"/");
                    }
                    //System.out.println(parentFolder.toString());
                    Class<?> c = Class.forName(mainClass,true,new URLClassLoader(new URL[]{parentFolder}));
		}catch(Exception e){
			return new Requirement("Class:"+mainClass+" Exists?",IC_POINTS,0, mainClass+".java not found");
		}
		return new Requirement("Class:"+mainClass+" Exists?",IC_POINTS,IC_POINTS,"Correct!");
	}

	/**
	* The second initial condition: does the program compile?
	* @param mainClass The name of the class that contains the main method
	* @return A single requirement to be graded
	*/
	private static Requirement checkCompilibility(String mainClass){
		String compErrs="";
		try{
			System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_60");
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics,null,null);
			File file = new File(Assignment.class.getResource("/"+mainClass+".java").toURI());
			Iterable< ? extends JavaFileObject > sources = manager.getJavaFileObjectsFromFiles( Arrays.asList(file));
			CompilationTask task = compiler.getTask(null,manager,diagnostics,null,null,sources);
			boolean result = task.call();
			if(result){
				manager.close();
				return new Requirement("Compiles?",IC_POINTS,IC_POINTS,"Correct!");
			}
			for(final Diagnostic< ? extends JavaFileObject > diagnostic : diagnostics.getDiagnostics()){
				compErrs+=diagnostic.getMessage(null)+", line #"+diagnostic.getLineNumber()+
					" in "+diagnostic.getSource().getName()+System.getProperty("line.separator");
			}
			manager.close();
		}catch(Exception e){
			return new Requirement("Compiles?",IC_POINTS,0,compErrs);
		}
		return new Requirement("Compiles?",IC_POINTS,0,compErrs);
	}

	/**
	* The third initial condition: Is the directory named correctly?
	* @param mainClass The name of the class that contains the main method
	* @return A single requirement to be graded
	*/
	private static Requirement checkDirectoryName(){
		URL location = Assignment.class.getProtectionDomain().getCodeSource().getLocation();
		String[] dirnames = location.getFile().split("/");
		String currdir = dirnames[dirnames.length-(usingNetbeans?4:2)];
		if(STUDENTS.contains(currdir.split("_")[1].toLowerCase()) && currdir.split("_")[0].toLowerCase().equals(type.toLowerCase()+num)){
			return new Requirement("Directory Name?",IC_POINTS,IC_POINTS,"Correct!");
		}
		return new Requirement("Directory Name?",IC_POINTS,0,"Incorrect directory name. Check project specifications for naming convention.");
	}
        
        /**
         * Finds the class file given the name in the working directory and creates an instance of it.
         * @param className Class name
         * @param argTypes Constructor Argument types
         * @param argValues Constructor Argument values
         * @return 
         */
        public static Object getInstance(String className, Class<?>[] argTypes, Object[] argValues){
            //System.out.println("getInstance:");printDirInfo();
            try{
                URL location = Assignment.class.getProtectionDomain().getCodeSource().getLocation();
                URL parentFolder;
                if(usingNetbeans){
                    parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().substring(0,location.getFile().lastIndexOf("/")).lastIndexOf("/"))+"/build/classes/");
                }
                else{
                    parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().lastIndexOf("/"))+"/");
                }
                //System.out.println(parentFolder.toString());
                Class<?> c = Class.forName(className,true,new URLClassLoader(new URL[]{parentFolder}));
                Constructor<?> cstruct = c.getConstructor(argTypes);
                return cstruct.newInstance(argValues);
            }
            catch(Exception e){
                System.err.println("Failure in creating "+className+" object");
                //e.printStackTrace();
                return null;
            }
        }
        
        /**
         * Prints the current working directory and the files/directories inside
         */
        public static void printDirInfo(){
            System.out.println("Directory:"+System.getProperty("user.dir"));
            File folder = new File(System.getProperty("user.dir"));
            File[] listOfFiles = folder.listFiles();

            for(int i =0;i<listOfFiles.length;++i){
                if(listOfFiles[i].isFile()){
                    System.out.println("F-->"+listOfFiles[i].getName());
                }else{
                    System.out.println("D-->"+listOfFiles[i].getName());
                }
            }
        }
        
	/**
	* Gets the username from the directory name. Static so this can be called by the constructor
	* @return The username of the student being graded
	*
	*/
	public static String getUsername(){
            if(un !=null && !un.equals("")){
                    return un;
            }
            URL location = Assignment.class.getProtectionDomain().getCodeSource().getLocation();
            String[] dirnames = location.getFile().split("/");
            String currdir = dirnames[dirnames.length-(usingNetbeans?4:2)];
            return currdir.split("_")[1].trim();
	}

	/**
	* Tests what has been printed to System.out via a particular method
	* @param testObj The instance of the student implemented class. 
	* @param methodName The method being tested
	* @param params The list of parameters passed into the method being tested
	* @param targetOutput Expected output from the method being tested
	* @param pointsPossible Points this method is worth
	* @return A single requirement to be graded. 0% if No output or method doesn't exist. 50% if Output is incorrect but present
	*/
	public Requirement assertSystemOutput(Object testObj, String methodName,Class[] params,String targetOutput, int pointsPossible){
		try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
		    PrintStream old = System.out;
                    Class<?> getclass = testObj.getClass();
                    Method check = getclass.getMethod(methodName,params);
                    if(check!=null){
                        System.setOut(ps);
                        check.invoke(testObj,(Object[])params);
                        if(baos.toString().length()>0 ){
                                if(baos.toString().trim().equals(targetOutput.trim())){
                                    System.out.flush();
                                    System.setOut(old);
                                    System.out.print(baos.toString());
                                    return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible,"Correct!");
                                }
                                else{
                                    System.out.flush();
                                    System.setOut(old);
                                    System.out.print(baos.toString());
                                    return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible/2,"Incorrect Output. Expecting :\""+
                                                targetOutput+"\", Found:\""+baos.toString().trim()+"\". Check spelling and capitalization");
                                }
                        }
                        System.out.flush();
                        System.setOut(old);
                        System.out.print(baos.toString());
                        return new Requirement("Output?:"+methodName,pointsPossible,0,"No output printed to console");
                    }
                    else{
                        System.out.flush();
                        System.setOut(old);
                        System.out.print(baos.toString());
                        return new Requirement("Output?:"+methodName,pointsPossible,0,"No output printed to console");
                    }
            }catch(Exception e){
                return new Requirement("Output?:"+methodName,pointsPossible,0,"Could not find method: "+
                            methodName+". Check requirement document for method spelling, case, and parameter datatypes.");
            }
	}

	/**
	* Tests what has been printed to System.out via a particular method using regular expression matching
	* @param testObj The instance of the student implemented class. 
	* @param methodName The method being tested
	* @param params The list of parameters passed into the method being tested
	* @param targetRegex Reqular expression to match custom user output
	* @param pointsPossible Points this method is worth
	* @return A single requirement to be graded. 0% if No output or method doesn't exist. 50% if Output is incorrect but present
	*/
	public Requirement assertSystemOutputRegex(Object testObj, String methodName,Class[] paramTypes,Object[] paramValues, String targetRegex, int pointsPossible){
		try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    PrintStream old = System.out;
                    InputStream oldin = System.in;
                    Class<?> getclass = testObj.getClass();
                    Method check = getclass.getMethod(methodName,paramTypes);
                    if(check!=null){
                            System.setOut(ps);
                            System.setIn(getClass().getResourceAsStream("/input/"+type+num+"input.txt"));
                            check.invoke(testObj,(Object[])paramValues);
                            if(baos.toString().length()>0 ){
                                    Pattern p = Pattern.compile(targetRegex,Pattern.DOTALL);
                                    Matcher m = p.matcher(baos.toString());
                                    //System.out.println(baos.toString());
                                    if(m.find()){
                                            System.out.flush();
                                            System.setOut(old);
                                            System.out.print(baos.toString());
                                            return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible,"Correct!");
                                    }
                                    else{
                                            System.out.flush();
                                            System.setOut(old);
                                            System.setIn(oldin);
                                            System.out.print(baos.toString());
                                            return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible/2,
                                                    "Incorrect Output. Check requirements for spelling, spacing, character order, and capitalization");
                                    }
                            }
                            System.out.flush();
                            System.setOut(old);
                            System.setIn(oldin);
                            //System.out.print(baos.toString());
                    return new Requirement("Output?:"+methodName,pointsPossible,0,"No output printed to console");
                    }
                    else{
                            System.out.flush();
                            System.setOut(old);
                            System.setIn(oldin);
                            System.out.print(baos.toString());
                            return new Requirement("Output?:"+methodName,pointsPossible,0,"No output printed to console");
                    }
		}catch(Exception e){
			e.printStackTrace();
			return new Requirement("Output?:"+methodName,pointsPossible,0,"Could not find method: "+
				methodName+". Check requirement document for method spelling, case, and parameter datatypes.");
		}
	}
        
        /**
         * Takes a string and checks to see if the target regex can be found
         * in the corresponding code String. 
         * @param codeToCheck Section of code to check. Could be method, field, comments
         * @param targetRegex Requirement for the code to match
         * @return true if all code is found in the codeToCheck
         */
        public boolean assertCodeExistsRegex(String codeToCheck, String targetRegex){
            try{
                Pattern p = Pattern.compile(targetRegex,Pattern.DOTALL);
                Matcher m = p.matcher(codeToCheck);
                return m.find();
            }catch(Exception e){
                System.err.println("Failure in regex comparison");
                //e.printStackTrace();
                return false;
            }
        }
        
        /**
         * Checks the ParsedClass for the existence of a field, returns 
         * a gradable requirement depending on regex matching
         * @param parsedClass The ParsedClass object to be searched
         * @param fieldName The name of the field
         * @param fieldRegex An String that represents patterns to be tested
         * @param pointPerField The bumber of points for each field in the fieldRegexes
         * @return Gradable requirements
         */
        public Requirement checkFieldExists(ParsedClass parsedClass,String fieldName, String fieldRegex,int pointPerField){
            ArrayList<ParsedField> fields = parsedClass.getFields();
            ArrayList<Requirement> reqs = new ArrayList();
            for(int i=0;i<fields.size();++i){
                if(fieldName.equals(fields.get(i).getName())){
                    if(assertCodeExistsRegex(fields.get(i).getDeclaration(),fieldRegex)){
                        return new Requirement("Class Variable: "+fieldName+" exists?",pointPerField,pointPerField,"Correct!");
                    }
                }
            }
            return new Requirement("Class variable: "+fieldName+" exists?",pointPerField,0,"Not found. Check spelling and data type.");
        }
        
        /**
         * Checks to see if supplied regular expression can be found within a constructor and returns a 
         * Requirement object with a provided description.
         * @param parsedClass The static text of the class
         * @param constructorName The constructor to be checked
         * @param params The parameters of the constructor being checked
         * @param reqDesc A text description of the what is expected in the constructor
         * @param targetRegex The regular expression to be found
         * @param points The number of points that this requirement is worth
         * @return Gradable Requirement object
         */
        public Requirement checkConstructorBodyContains(ParsedClass parsedClass, String constructorName, String[] params, String reqDesc, String targetRegex, int points){
            ArrayList<ParsedConstructor> constructors = parsedClass.getConstructors();
            for(int i=0;i<constructors.size();++i){
                if(constructorName.equals(constructors.get(i).getName())&&hasCorrectParamTypes(constructors.get(i).getParams(),params)){
                    if(assertCodeExistsRegex(constructors.get(i).getBody(),targetRegex)){
                        return new Requirement(constructorName+" constructor contains "+reqDesc+"?",points,points,"Correct!");
                    }else{
                        return new Requirement(constructorName+" constructor contains "+reqDesc+"?",points,0,"Incorrect");
                    }
                }
            }
            return new Requirement(constructorName+" constructor contains "+reqDesc+"?",points,0,"Not found");
        }
        
        /**
         * Checks to see if supplied regular expression can be found within a constructor's comments
         * and returns a Requirement object with a provided description.
         * @param parsedClass The static text of the class
         * @param constructorName The constructor to be checked
         * @param params The parameters of the constructor being checked
         * @param targetRegex The regular expression to be found
         * @param points The number of points that this requirement is worth
         * @return Gradable Requirement object
         */
        public Requirement checkConstructorComments(ParsedClass parsedClass, String constructorName, String[] params, String targetRegex, int points){
            ArrayList<ParsedConstructor> constructors = parsedClass.getConstructors();
            for(int i=0;i<constructors.size();++i){
                if(constructorName.equals(constructors.get(i).getName())&&hasCorrectParamTypes(constructors.get(i).getParams(),params)){
                    if(assertCodeExistsRegex(constructors.get(i).getComments(),targetRegex)){
                        return new Requirement(constructorName+" constructor comments?",points,points,"Correct!");
                    }else{
                        return new Requirement(constructorName+" constructor comments?",points,0,"Incorrect");
                    }
                }
            }
            return new Requirement(constructorName+" constructor comments?",points,0,"Not found");
        }
        
        /**
         * Checks to see if supplied regular expression can be found within a method and returns a 
         * Requirement object with a provided description.
         * @param parsedClass The static text of the class
         * @param methodName The method to be checked
         * @param params The parameters of the method being checked
         * @param reqDesc A text description of the what is expected in the method
         * @param targetRegex The regular expression to be found
         * @param points The number of points that this requirement is worth
         * @return Gradable Requirement object
         */
        public Requirement checkMethodBodyContains(ParsedClass parsedClass, String methodName, String[] params, String reqDesc, String targetRegex, int points){
            ArrayList<ParsedMethod> methods = parsedClass.getMethods();
            for(int i=0;i<methods.size();++i){
                if(methodName.equals(methods.get(i).getName())&&hasCorrectParamTypes(methods.get(i).getParams(),params)){
                    if(assertCodeExistsRegex(methods.get(i).getBody(),targetRegex)){
                        return new Requirement(methodName+" contains "+reqDesc+"?",points,points,"Correct!");
                    }else{
                        return new Requirement(methodName+" contains "+reqDesc+"?",points,0,"Incorrect");
                    }
                }
            }
            return new Requirement(methodName+" contains "+reqDesc+"?",points,0,"Not found");
        }
        /**
         * Checks to see if supplied regular expression can be found within a method's comments
         * and returns a Requirement object with a provided description.
         * @param parsedClass The static text of the class
         * @param methodName The method to be checked
         * @param params The parameters of the method being checked
         * @param targetRegex The regular expression to be found
         * @param points The number of points that this requirement is worth
         * @return Gradable Requirement object
         */
        public Requirement checkMethodComments(ParsedClass parsedClass, String methodName, String[] params, String targetRegex, int points){
            ArrayList<ParsedMethod> methods = parsedClass.getMethods();
            for(int i=0;i<methods.size();++i){
                if(methodName.equals(methods.get(i).getName())&&hasCorrectParamTypes(methods.get(i).getParams(),params)){
                    if(assertCodeExistsRegex(methods.get(i).getComments(),targetRegex)){
                        return new Requirement(methodName+" comments?",points,points,"Correct!");
                    }else{
                        return new Requirement(methodName+" comments?",points,0,"Incorrect");
                    }
                }
            }
            return new Requirement(methodName+" comments?",points,0,"Not found");
        }
        
        
        
        /**
         * Checks to make sure the actual type returned matches the expected return type.
         * Doesn't yet compare return values.
         * @param testObj The instance of the student implemented class.
         * @param methodName The name of the method being returned
         * @param paramTypes Array of class types for the method parameters
         * @param paramValues Array of object types for the method parameters
         * @param targetReturnType Class data type expected to be returned
         * @param targetReturnValue Object data value expected to be returned
         * @param pointsPossible points for the requirement that tests proper return type
         * @return Whether or not the return type matches what's expected
         */
        public Requirement checkMethodReturnType(Object testObj, String methodName,Class[] paramTypes,Object[] paramValues, Class targetReturnType, Object targetReturnValue, int pointsPossible){
            try{
                Class<?> getclass = testObj.getClass();
                Method check = getclass.getMethod(methodName,paramTypes);
                if(check!=null){
                    Object actualReturnValue = check.invoke(testObj,(Object[])paramValues);
                    if(actualReturnValue.getClass().toString().equals(targetReturnValue.getClass().toString())){
                        return new Requirement("Return type?:"+methodName,pointsPossible,pointsPossible,"Correct!");
                    }
                    return new Requirement("Return type?:"+methodName,pointsPossible,0,"Incorrect return data type");
                }
                else{
                    return new Requirement("Return type?:"+methodName,pointsPossible,0,"Could not find method "+methodName);
                }
            }catch(Exception e){
                e.printStackTrace();
                return new Requirement("Return type?:"+methodName,pointsPossible,0,"Could not find method: "+
                            methodName+". Check requirement document for method spelling, case, and parameter datatypes.");
            }
        }
        
        /**
         * Checks to make sure the value returned matches the expected return value.
         * Doesn't yet compare return values.
         * @param testObj The instance of the student implemented class.
         * @param methodName The name of the method being returned
         * @param paramTypes Array of class types for the method parameters
         * @param paramValues Array of object types for the method parameters
         * @param targetReturnType Class data type expected to be returned
         * @param targetReturnValue Object data value expected to be returned
         * @param pointsPossible points for the requirement that tests proper return type
         * @return Whether or not the return type matches what's expected
         */
        public Requirement checkMethodReturnValue(Object testObj, String methodName,Class[] paramTypes,Object[] paramValues, Class targetReturnType, Object targetReturnValue, int pointsPossible){
            try{
                Class<?> getclass = testObj.getClass();
                Method check = getclass.getMethod(methodName,paramTypes);
                if(check!=null){
                    Object actualReturnValue = check.invoke(testObj,(Object[])paramValues);
                    //System.out.println(actualReturnValue.toString() +" : "+targetReturnValue.toString());
                    if(actualReturnValue.toString().trim().equals(targetReturnValue.toString().trim())){
                        return new Requirement("Return value?:"+methodName,pointsPossible,pointsPossible,"Correct!");
                    }
                    return new Requirement("Return value?:"+methodName,pointsPossible,0,"Incorrect return data value");
                }
                else{
                    return new Requirement("Return value?:"+methodName,pointsPossible,0,"Could not find method "+methodName);
                }
            }catch(Exception e){
                e.printStackTrace();
                return new Requirement("Return value?:"+methodName,pointsPossible,0,"Could not find method: "+
                            methodName+". Check requirement document for method spelling, case, and parameter datatypes.");
            }
        }
        /**
         * Checks the ParsedClass for the existence of a method, returns 
         * a gradable requirement depending on regex matching
         * @param parsedClass ParsedClass object being checked
         * @param methodName Name of the method to be checked
         * @param commentRegex Comment regex pattern
         * @param headerRegex Header regex pattern
         * @param bodyRegex Body regex pattern
         * @param pointsPerReq Points per requirement. Methods have requirements about method comment,header, and body
         * @return 
         */
        public ArrayList<Requirement> checkMethodCorrectness(ParsedClass parsedClass, String methodName, String[] params, String commentRegex, String headerRegex, String bodyRegex, int pointsPerReq){
            ArrayList<ParsedMethod> methods = parsedClass.getMethods();
            ArrayList<Requirement> reqs = new ArrayList();
            for(int i=0;i<methods.size();++i){
                if(methodName.equals(methods.get(i).getName())&&hasCorrectParamTypes(methods.get(i).getParams(),params)){
                    if(assertCodeExistsRegex(methods.get(i).getComments(),commentRegex)){
                        reqs.add(new Requirement("Method "+methodName+" comments?",pointsPerReq,pointsPerReq,"Correct!"));
                    }else{
                        reqs.add(new Requirement("Method "+methodName+" comments?",pointsPerReq,0,"Incorrect"));
                    }
                    if(assertCodeExistsRegex(methods.get(i).getHeader(),headerRegex)){
                        reqs.add(new Requirement("Method "+methodName+" header?",pointsPerReq,pointsPerReq,"Correct!"));
                    }else{
                        reqs.add(new Requirement("Method "+methodName+" header?",pointsPerReq,0,"Incorrect"));
                    }
                    if(assertCodeExistsRegex(methods.get(i).getBody(),bodyRegex)){
                        reqs.add(new Requirement("Method "+methodName+" body?",pointsPerReq*2,pointsPerReq*2,"Correct!"));
                    }else{
                        reqs.add(new Requirement("Method "+methodName+" body?",pointsPerReq*2,0,"Incorrect"));
                    }
                    break;
                }
                else{
                    if(i==methods.size()-1){
                        reqs.add(new Requirement("Method "+methodName+"?",pointsPerReq*4,0,"Not found"));
                        break;
                    }
                    continue;
                }
            }
            return reqs;
        }
        
        /**
         * checkMethodCorrectness helper method
         * @param foundParams Parameters found during the checkMethodCorrectness method search
         * @param targetParams Expected param types for the method
         * @return True if the found params all equal the target params in the correct order
         */
        private boolean hasCorrectParamTypes(ArrayList<String> foundParams, String[] targetParams){
            int j=0;
            for(String paramType:targetParams){
                if(j>foundParams.size() || foundParams.size() != targetParams.length){
                    return false;
                }
                if(!foundParams.get(j).equals(targetParams[j])){
                    return false;
                }
                j++;
            }
            return true;
        }
	/**
	* "Grades" a list of requirements by writing them to the student grade text file.
	* Writes to master grade csv depending on isMaster parameter
	* @param ral List of Requirements to be graded
	* @param isMaster True causes the program to write to a master grade file. Use only if doing batch grading
	*/
	public static void grade(ArrayList<Requirement> ral,boolean isMaster){
		writeToStudentGrade(ral);
		if(isMaster){
			writeToMasterGrade(ral);
		}
	}

	/**
	* "Grades" a list of requirments by writing them to the student grade text file.
	* @param ral List of Requirements to be graded
	*/
	private static void writeToStudentGrade(ArrayList<Requirement> ral){
		try{
			studentGradeFile = new FileWriter(type+num+"_"+un+"_grade.txt");
			studentGradeFile.append("COSC240"+System.getProperty("line.separator"));
			studentGradeFile.append(type+num+System.getProperty("line.separator"));
			studentGradeFile.append(un+System.getProperty("line.separator")+System.getProperty("line.separator"));
			studentGradeFile.append("#\tPoints\tName\t\t\tExplanation"+System.getProperty("line.separator"));

			int i=0;
			for(Requirement r : ral){
				studentGradeFile.append(""+ i++ +"\t"+r.getReceived()+"/"+r.getPossible()+"\t"+
					r.getName()+"\t\t"+r.getDescription()+System.getProperty("line.separator"));
			}
			studentGradeFile.close();
		}
		catch(Exception e){
                    System.err.println("Failure in writing to student grade");
			//e.printStackTrace();
		}
	}

	/**
	* "Grades" a list of requirements by writing them to the master grade csv file.
	* Master grade file located in ../../Grades/Master.csv
	* @param ral List of Requirements to be graded
	*/
	private static void writeToMasterGrade(ArrayList<Requirement> ral){
		String masterGradeFilePath = (usingNetbeans?"..\\..\\":"")+"..\\..\\..\\Grades\\"+type+num+"_Master.csv";
		try{
			File f = new File(masterGradeFilePath);
			if(!f.exists()&&!f.isDirectory()){
				masterGradeFile = new FileWriter(masterGradeFilePath,true);
				masterGradeFile.append(type+num+ " Grade Sheet"+System.getProperty("line.separator"));
				masterGradeFile.append("Username"+",");
				for(Requirement r: ral){
					masterGradeFile.append(""+r.getName()+",");
				}
				masterGradeFile.append(System.getProperty("line.separator"));
			}
			else{
				masterGradeFile = new FileWriter(masterGradeFilePath,true);
			}
			masterGradeFile.append(un+",");
			for(Requirement r: ral){
				masterGradeFile.append(""+r.getReceived()+",");
			}
			masterGradeFile.append(System.getProperty("line.separator"));
			masterGradeFile.close();
		}
		catch(Exception e){
                    System.err.println("Failure in writing to master grade");
                    //e.printStackTrace();
		}
	}

	/**
	* Initializes the HashSet used to store students names
	*/
	private static void initializeStudents(){
		STUDENTS = new HashSet<String>();
		STUDENTS.add("gkbarnett0");
		STUDENTS.add("jbhylet0");
		STUDENTS.add("bmbinkley0");
		STUDENTS.add("mabissonnette0");
		STUDENTS.add("rmbrosh0");
		STUDENTS.add("jdbrummell0");
		STUDENTS.add("jdavis0");
		STUDENTS.add("tgapieieva0");
		STUDENTS.add("jegiordano0");
		STUDENTS.add("dkglover0");
		STUDENTS.add("aggooding0");
		STUDENTS.add("shegde02");
		STUDENTS.add("hahensel0");
		STUDENTS.add("twjones01");
		STUDENTS.add("lsmaclean0");
		STUDENTS.add("hrmcfalls0");
		STUDENTS.add("bamiller0");
		STUDENTS.add("djpabst0");
		STUDENTS.add("jaritchie0");
		STUDENTS.add("tprock0");
		STUDENTS.add("erseibert0");
		STUDENTS.add("wasilva0");
		STUDENTS.add("drsmith02");
		STUDENTS.add("jhsutherland0");
		STUDENTS.add("favandivier0");
		STUDENTS.add("dwilliams05");
		STUDENTS.add("sjmoon");
	}
}
