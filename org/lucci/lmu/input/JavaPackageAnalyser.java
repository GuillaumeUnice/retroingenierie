package org.lucci.lmu.input;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.URL;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;

import toools.io.FileUtilities;

public class JavaPackageAnalyser extends AbstractJavaAnalyser{

	private ArrayList<String> strTest = new ArrayList<String>();
	
	private void listFilesForFolder(final File folder, String path) throws IOException {
		String lol;
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	
	        	//path += ("/" + fileEntry.getName());

	        	if(path.charAt(path.length() - 1) != '/') {
	        		lol = path + "/" + fileEntry.getName();
	        	} else {
	        		lol = path + fileEntry.getName();
	        	}
	        	
	        	System.out.println("path : " + path);
	            listFilesForFolder(fileEntry, lol);
	        } else {
	        	if(path.charAt(path.length() - 1) != '/') {
	        		path += "/";
	        	}
	        	
	        	if(fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length()).equals("class")) {
	        		
	        		System.out.println(path + fileEntry.getName());
	            	this.strTest.add(path + fileEntry.getName());
	        	
	        	}
	        }
	    }
	}
	
	/*private void listFilesForFolder(final File folder, String path) throws IOException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	
	        	path += ("/" + fileEntry.getName());
	        	System.out.println("path : " + path);
	            listFilesForFolder(fileEntry, path);
	        } else {
	        	if(path.charAt(path.length() - 1) != '/') {
	        		path += "/";
	        	}
	        	System.out.println(fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length()));
	        	if(fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length()).equals("class")) {
	        		
	        		System.out.println(path + fileEntry.getName());
	            	this.strTest.add(path + fileEntry.getName());
	        	
	        	}
	        }
	    }
	}*/	
	
	
	@Override
	public Model createConcreteModel(String path) throws ParseError {
	
		Model model = new Model();
		primitiveMap.put(void.class, Entities.findEntityByName(model, "void"));
		primitiveMap.put(int.class, Entities.findEntityByName(model, "int"));
		primitiveMap.put(long.class, Entities.findEntityByName(model, "long"));
		primitiveMap.put(char.class, Entities.findEntityByName(model, "char"));
		primitiveMap.put(float.class, Entities.findEntityByName(model, "float"));
		primitiveMap.put(double.class, Entities.findEntityByName(model, "double"));
		primitiveMap.put(String.class, Entities.findEntityByName(model, "string"));
		primitiveMap.put(Class.class, Entities.findEntityByName(model, "class"));
		primitiveMap.put(boolean.class, Entities.findEntityByName(model, "boolean"));
		primitiveMap.put(Collection.class, Entities.findEntityByName(model, "set"));
		primitiveMap.put(List.class, Entities.findEntityByName(model, "sequence"));
		primitiveMap.put(Map.class, Entities.findEntityByName(model, "map"));
		primitiveMap.put(Object.class, Entities.findEntityByName(model, "object"));
		primitiveMap.put(java.util.Date.class, Entities.findEntityByName(model, "date"));
		primitiveMap.put(java.sql.Date.class, Entities.findEntityByName(model, "date"));
			
		ClassLoader classLoader;
    
        try {
            	
        	this.listFilesForFolder(new File(path), path);
    
           	for(String object: this.strTest){
           		byte[] byteTest = FileUtilities.getFileContent(new File(object));
           	
           		
               	ByteClassLoader test = new ByteClassLoader();
               	Class loadedMyClass = test.findClass(null, byteTest);
                if(loadedMyClass != null) {

               		Entity entity = new Entity();
            		entity.setName(computeEntityName(loadedMyClass));
            		entity.setNamespace(computeEntityNamespace(loadedMyClass));
            		entity_class.put(entity, loadedMyClass);
            		model.addEntity(entity);	
                }	
  
            }
    		     
		} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
		}
            
		fillModel(model);
		
		return model;
	}
}