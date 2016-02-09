package org.lucci.lmu.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;

import toools.ClassContainer;
import toools.ClassPath;
import toools.io.file.RegularFile;

public class JarJavaAnalyser extends AbstractJavaAnalyser{
	
	@Override
	public Model createConcreteModel(String path) throws ParseError
	{
		
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

		try
		{
			// convert to byte
			RegularFile inputFile = new RegularFile(path);
			byte[] data = inputFile.getContent();	
			 
			
			// create a jar file on the disk from the binary data
			/*RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(data);
			
			// load a jar file
			ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });

			ClassPath classContainers = new ClassPath();
			classContainers.add(new ClassContainer(jarFile, classLoader));

			for (RegularFile thisJarFile : this.knownJarFiles)
			{
				classContainers.add(new ClassContainer(thisJarFile, classLoader));
			}
			
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
		}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}

		return model;
	}
	
}
