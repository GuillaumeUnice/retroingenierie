package org.lucci.lmu.input;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;

import toools.io.file.RegularFile;

public class FileJavaAnalyser extends AbstractJavaAnalyser {

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
		

		
		/*try
		{*/

			//RegularFile jarFile = RegularFile.createTempFile("lmu-", ".class");
			//jarFile.setContent(data);
			
			//RegularFile jarFile = new RegularFile("lmu-blabla.class");
			//jarFile.setContent(data);

			ClassLoader classLoader;
			
			
  

            // Load the target class using its binary name
            try {
        		// recuperation sous forme de byte
        		RegularFile inputFile = new RegularFile(path);
        		byte[] data = inputFile.getContent();
            	
            	/*classLoader = new URLClassLoader(new URL[] { new File("/tmp/exo16.jar").toURI().toURL() });
				Class loadedMyClass = classLoader.loadClass("client.MonObjetDistant");
			    System.out.println("Loaded class name: " + loadedMyClass.getName());
			     */
            	ByteClassLoader test = new ByteClassLoader();
            	Class loadedMyClass = test.findClass(null, data);
			     
			    Entity entity = new Entity();
				entity.setName(computeEntityName(loadedMyClass));
				entity.setNamespace(computeEntityNamespace(loadedMyClass));
				entity_class.put(entity, loadedMyClass);
				model.addEntity(entity);
			     
			     
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*JavaClassLoader javaClassLoader = new JavaClassLoader();
			javaClassLoader.invokeClassMethod("toools.io.JavaResource", "getPath");
			*/
			

			
			
			
			
			/*
			ClassLoader classLoader = URLClassLoader.newInstance(new URL[] {new URL("file:/tmp/lmu-0.jar")});
			//System.out.println(new URL[] { new File("/tmp/exo16.jar").toURI().toURL()});
			Class<?> thisClass;
			System.out.println("liiiiol");
			try {
				thisClass = classLoader.loadClass("MonObjetDistant");
				if (!thisClass.getName().matches(".+\\$[0-9]+"))
				{
					System.out.println("looool");
					Entity entity = new Entity();
					entity.setName(computeEntityName(thisClass));
					entity.setNamespace(computeEntityNamespace(thisClass));
					entity_class.put(entity, thisClass);
					model.addEntity(entity);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
/*
			ClassPath classContainers = new ClassPath();
			classContainers.add(new ClassContainer(jarFile, classLoader));

			// take all the classes in the jar files and convert them to LMU
			// Entities
			for (Class<?> thisClass : classContainers.listAllClasses())
			{
				// if this is not an anonymous inner class (a.b$1)
				// we take it into account
				if (!thisClass.getName().matches(".+\\$[0-9]+"))
				{
					Entity entity = new Entity();
					entity.setName(computeEntityName(thisClass));
					entity.setNamespace(computeEntityNamespace(thisClass));
					entity_class.put(entity, thisClass);
					model.addEntity(entity);
				}
			}*/

			// at this only the name of entities is known
			// neither members nor relation are known
			// let's find them
			fillModel(model);
			//jarFile.delete();
		/*}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}*/

		return model;
	}
	

}