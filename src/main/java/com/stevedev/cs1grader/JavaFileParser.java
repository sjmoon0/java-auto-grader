/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author steve
 */
public class JavaFileParser {
    
    public static ParsedClass parse(String f){
        String fileName = f;
        try{
            printDirInfo();
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
    
    public static ArrayList<ParsedMethod> buildMethods(CompilationUnit cu){
        ArrayList<ParsedMethod> pm = new ArrayList();
        List<TypeDeclaration> types = cu.getTypes();
        for(TypeDeclaration type:types){
            List<BodyDeclaration> members = type.getMembers();
            for(BodyDeclaration member:members){
                //System.out.println("~>~>~>~>~>~>~>~>~>~"+member.getClass().getName()+"~>~>~>~>~>~>~>~>~>~"+"\n"+member.toString());
                if(member instanceof MethodDeclaration){
                    MethodDeclaration m = (MethodDeclaration)member;
                    String comments = (m.getJavaDoc()==null ? "":m.getJavaDoc().toString());
                    pm.add(new ParsedMethod(m.getName().trim(),comments.trim(),m.getDeclarationAsString().trim(),m.getBody().toStringWithoutComments().trim()));
                }
            }
        }
        return pm;
    }
    
    public static ArrayList<String> buildFields(CompilationUnit cu){
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
    
    public static ParsedClass buildClass(CompilationUnit cu, ArrayList<String> fields,ArrayList<ParsedMethod> methods){
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
