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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import static com.stevedev.cs1grader.Assignment.printDirInfo;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a ParsedClass object out of a java file. 
 * ParsedClass consists of a ParsedMethods ArrayList.
 * @author steve
 * @version 2016.09.17
 */
public class JavaFileParser {
    /**
     * Only public method, call this to generate a ParsedClass. Will look for the 
     * class in the same directory that the resultant jar file will be placed in.
     * @param f The classname to be parsed. 
     * @return A ParsedClass object that represents the static .java file associated with the classname
     */
    public static ParsedClass parse(String f){
        String fileName = f;
        try{
            FileInputStream in = new FileInputStream(fileName+".java");
            CompilationUnit cu;
            cu = JavaParser.parse(in);
            ParsedClass pc = buildClass(cu,buildFields(cu),buildMethods(cu));
            in.close();
            return pc;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Separates the compilation unit into a list of methods
     * @param cu CompilationUnit from JavaParser
     * @return ArrayList of ParsedMethods to be used for the ParsedClass
     */
    private static ArrayList<ParsedMethod> buildMethods(CompilationUnit cu){
        ArrayList<ParsedMethod> pm = new ArrayList();
        List<TypeDeclaration> types = cu.getTypes();
        for(TypeDeclaration type:types){
            List<BodyDeclaration> members = type.getMembers();
            for(BodyDeclaration member:members){
                if(member instanceof MethodDeclaration){
                    MethodDeclaration m = (MethodDeclaration)member;
                    String comments = (m.getJavaDoc()==null ? "":m.getJavaDoc().toString());
                    pm.add(new ParsedMethod(m.getName().trim(),comments.trim(),m.getDeclarationAsString().trim(),m.getBody().toStringWithoutComments().trim()));
                }
            }
        }
        return pm;
    }
    
    /**
     * Separates the compilation unit into a list of fields
     * @param cu CompilationUnit from JavaParser
     * @return ArrayList of Strings to be used for the ParsedClass
     */
    private static ArrayList<String> buildFields(CompilationUnit cu){
        ArrayList<String> fields = new ArrayList();
        List<TypeDeclaration> types = cu.getTypes();
        for(TypeDeclaration type:types){
            List<BodyDeclaration> members = type.getMembers();
            for(BodyDeclaration member:members){
                if(member instanceof FieldDeclaration){
                    FieldDeclaration f = (FieldDeclaration)member;
                    fields.add(f.toStringWithoutComments().trim());
                }
            }
        }
        return fields;
    }
    
    /**
     * Takes results from other methods and builds a ParsedClass object
     * @param cu JavaParser CompilationUnit
     * @param fields ArrayList of Strings that represent fields
     * @param methods ArrayList of ParsedMethod objects that represent methods
     * @return The final ParsedClass object
     */
    private static ParsedClass buildClass(CompilationUnit cu, ArrayList<String> fields,ArrayList<ParsedMethod> methods){
        List<TypeDeclaration> types = cu.getTypes();
        for(TypeDeclaration type:types){
            if(type instanceof ClassOrInterfaceDeclaration){
                ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration)type;
                String comments = (c.getJavaDoc()==null ? "":c.getJavaDoc().toString());
                return new ParsedClass(c.getName().trim(),comments.trim(),fields,methods);
            }
        }
        return null;
    }
}
