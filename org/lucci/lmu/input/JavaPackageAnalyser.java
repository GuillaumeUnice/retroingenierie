package org.lucci.lmu.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;

import toools.io.FileUtilities;

public class JavaPackageAnalyser extends AbstractJavaAnalyser{

	private ArrayList<String> strClasspath = new ArrayList<String>();
	
	private void listFilesForFolder(final File folder, String path) throws IOException {
		String newPath;
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {

	        	if(path.charAt(path.length() - 1) != '/') {
	        		newPath = path + "/" + fileEntry.getName();
	        	} else {
	        		newPath = path + fileEntry.getName();
	        	}
	        	
	            listFilesForFolder(fileEntry, newPath);
	        } else {
	        	if(path.charAt(path.length() - 1) != '/') {
	        		path += "/";
	        	}
	        	if(fileEntry.getName().length() > 5) {
	        		if(fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length()).equals("class")) {
		            	this.strClasspath.add(path + fileEntry.getName());
		        	}	
	        	}
	        	
	        }
	    }
	}
	
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
    
           	for(String object: this.strClasspath){
           		byte[] data = FileUtilities.getFileContent(new File(object));
           	
           		
               	ByteClassLoader byteClassLoader = new ByteClassLoader();
               	Class loadedMyClass = byteClassLoader.findClass(null, data);
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
