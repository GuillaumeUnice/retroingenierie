package org.lucci.lmu.input;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lucci.lmu.AssociationRelation;
import org.lucci.lmu.Attribute;
import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.InheritanceRelation;
import org.lucci.lmu.Model;
import org.lucci.lmu.Operation;
import org.lucci.lmu.Visibility;
import org.lucci.lmu.test.DynamicCompiler;

import toools.ClassContainer;
import toools.ClassName;
import toools.ClassPath;
import toools.Clazz;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public class JarFileAnalyser extends ModelFactory
{
	@Override
	public AbstractJavaAnalyser createConcreteProduct(String path) throws ParseError
	{
		
		return new JarJavaAnalyser();
		//return test.createModel(path);
		/*
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

			// create a jar file on the disk from the binary data
			RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(data);
			
			// permet de charge un fichier jar peut etre java pour pouvoir y faire des operations dessus
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
			}

			// at this only the name of entities is known
			// neither members nor relation are known
			// let's find them
			fillModel(model);
			jarFile.delete();
		}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}

		return model;*/
	}


}
