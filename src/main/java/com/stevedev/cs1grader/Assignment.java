/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
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
	private static final int IC_POINTS=8;//Points for initial conditions. Decrease for subsequent labs

	/**
	* Assignment constructor
	* @param type "Lab" or "Project"
	* @param num lab or project number
	*/
	public Assignment(String type, int num){
		this.type=type;
		this.num=num;
		this.un=getUsername();
		initializeStudents();
                //System.out.println("COnstructor"+this.getClass().getCanonicalName());
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
                    URL parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().lastIndexOf("/"))+"/");
                    System.out.println(parentFolder.toString());
                    Class<?> c = Class.forName(mainClass,true,new URLClassLoader(new URL[]{parentFolder}));
		}catch(Exception e){
			return new Requirement("Class Exists?",IC_POINTS,0, mainClass+".java not found");
		}
		return new Requirement("Class Exists?",IC_POINTS,IC_POINTS,"Correct!");
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
		String currdir = dirnames[dirnames.length-2];
		if(STUDENTS.contains(currdir.split("_")[1].toLowerCase()) && currdir.split("_")[0].toLowerCase().equals(type.toLowerCase()+num)){
			return new Requirement("Directory Name?",IC_POINTS,IC_POINTS,"Correct!");
		}
		return new Requirement("Directory Name?",IC_POINTS,0,"Incorrect directory name. Check project specifications for naming convention.");
	}
        
        public static Object getInstance(String className, Class<?>[] argTypes, Object[] argValues){
            //System.out.println("getInstance:");printDirInfo();
            try{
                URL location = Assignment.class.getProtectionDomain().getCodeSource().getLocation();
                URL parentFolder = new URL("file://"+location.getFile().substring(0,location.getFile().lastIndexOf("/"))+"/");
                //System.out.println(parentFolder.toString());
                Class<?> c = Class.forName(className,true,new URLClassLoader(new URL[]{parentFolder}));
                Constructor<?> cstruct = c.getConstructor(argTypes);
                return cstruct.newInstance(argValues);
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

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
		String currdir = dirnames[dirnames.length-2];
		//System.out.println(currdir);
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
	public Requirement assertSystemOutputRegex(Object testObj, String methodName,Class[] params,String targetRegex, int pointsPossible){
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
					Pattern p = Pattern.compile(targetRegex);
					Matcher m = p.matcher(baos.toString());
					if(m.find()){
						System.out.flush();
			    		System.setOut(old);
						System.out.print(baos.toString());
						return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible,"Correct!");
					}
					else{
						System.out.flush();
                                                System.setOut(old);
						System.out.print(baos.toString());
						return new Requirement("Output?:"+methodName,pointsPossible,pointsPossible/2,
							"Incorrect Output. Check requirements for spelling, spacing, character order, and capitalization");
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
			e.printStackTrace();
			return new Requirement("Output?:"+methodName,pointsPossible,0,"Could not find method: "+
				methodName+". Check requirement document for method spelling, case, and parameter datatypes.");
		}
	}
        
        /*
        public Requirement assertCodeInMethodBodyRegex(String methodName, String targetRegex, int pointsPossible){
            //Use JavaParser to check for presence of:
                //Local Variable names, types
                //loop used
                //conditional used
                //print vs println
        }
        
        public Requirement assertCodeInMethodHeaderRegex(String methodName, String targetRegex, int pointsPossible){
            //Use JavaParser to check a method's:
                //parameter list, names
                //return type
                //access modifier
        }
        
        public Requirement assertMethodIO(Object testObj, String methodName,Class[] params, int pointsPossible){
            //Use testObj to 
                //verify method output datatype is correct type
                //verify method output is correct
        }
        */

	/**
	* "Grades" a list of requirments by writing them to the student grade text file.
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
			e.printStackTrace();
		}
	}

	/**
	* "Grades" a list of requirments by writing them to the master grade csv file.
	* Master grade file located in ../../Grades/Master.csv
	* @param ral List of Requirements to be graded
	*/
	private static void writeToMasterGrade(ArrayList<Requirement> ral){
		String masterGradeFilePath = "..\\..\\..\\Grades\\"+type+num+"_Master.csv";
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
			e.printStackTrace();
		}
	}

	/**
	* Initializes the HashSet used to store students names
	*/
	private static void initializeStudents(){
		STUDENTS = new HashSet<String>();
		STUDENTS.add("ralem0");
		STUDENTS.add("jrbuntine0");
		STUDENTS.add("dachinnery0");
		STUDENTS.add("qmconroy0");
		STUDENTS.add("kvedwards0");
		STUDENTS.add("dahungerford0");
		STUDENTS.add("chymiller0");
		STUDENTS.add("aplehman0");
		STUDENTS.add("aellinas0");
		STUDENTS.add("jtmason0");
		STUDENTS.add("cdparker01");
		STUDENTS.add("jcpiper0");
		STUDENTS.add("mqian0");
		STUDENTS.add("nmrevell0");
		STUDENTS.add("jrrost0");
		STUDENTS.add("amshelton0");
		STUDENTS.add("cbshore0");
		STUDENTS.add("wasilva0");
		STUDENTS.add("btichnell0");
		STUDENTS.add("nrwade0");
		STUDENTS.add("sjmoon");
	}
}
