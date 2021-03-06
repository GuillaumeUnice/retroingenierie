package org.lucci.lmu.input;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

import toools.ClassName;
import toools.Clazz;
import toools.io.file.RegularFile;

public abstract class AbstractJavaAnalyser extends AbstractAnalyser{

	protected Collection<RegularFile> knownJarFiles = new HashSet<RegularFile>();
	protected Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
	protected Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();

	public Collection<RegularFile> getJarFiles()
	{
		return this.knownJarFiles;
	}

	protected static Class<?> createClassNamed(String fullName)
	{
		ClassName cn = Clazz.getClassName(fullName);
		String src = "";

		if (cn.pkg != null)
		{
			src += "package " + cn.pkg + ";";
		}

		src += "public class " + cn.name + " {}";

		// System.out.println(src);
		return DynamicCompiler.compile(fullName, src);
	}

	public String computeEntityName(Class<?> c)
	{
		return c.getName().substring(c.getName().lastIndexOf('.') + 1);
	}

	public String computeEntityNamespace(Class<?> c)
	{
		return c.getPackage() == null ? Entity.DEFAULT_NAMESPACE : c.getPackage().getName();
	}

	protected void fillModel(Model model)
	{
		for (Entity entity : new HashSet<Entity>(model.getEntities()))
		{
			if (!entity.isPrimitive())
			{
				Class<?> clazz = entity_class.get(entity);
				//initInheritance(clazz, entity, model);
				//initAttributes(clazz, entity, model);
				//initOperations(clazz, entity, model);
			}
		}
	}

	private void initInheritance(Class<?> clazz, Entity entity, Model model)
	{
		// this collection will store the super class and super interfaces for
		// the given class
		Set<Class<?>> supers = new HashSet<Class<?>>();

		// first get the superclass, if any
		if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class && clazz.getSuperclass() != Enum.class)
		{
			supers.add(clazz.getSuperclass());
		}

		// then find all super interfaces
		supers.addAll(Arrays.asList(clazz.getInterfaces()));

		for (Class<?> c : supers)
		{
			Entity superentity = getEntity(model, c);

			// if the superentity exists in the model
			if (superentity != null)
			{
				// define the corresponding relation
				model.addRelation(new InheritanceRelation(entity, superentity));
			}
		}
	}

	private void initAttributes(Class<?> clazz, Entity entity, Model model)
	{

		try{
			for (Field field : clazz.getDeclaredFields())
			{
				// if the field is not static
				if ((field.getModifiers() & Modifier.STATIC) == 0)
				{
					// System.err.println(clazz.getName() + " " + field.getName());
					Type fieldType = field.getGenericType();
		
					if (fieldType instanceof ParameterizedType)
					{
						for (Type parameterType : ((ParameterizedType) fieldType).getActualTypeArguments())
						{
							if (parameterType instanceof Class<?>)
							{
								Class<?> parameterClass = (Class<?>) parameterType;
								Entity parameterEntity = getEntity(model, parameterClass);
		
								if (!parameterEntity.isPrimitive())
								{
									AssociationRelation rel = new AssociationRelation(parameterEntity, entity);
									rel.setType(AssociationRelation.TYPE.AGGREGATION);
									//
									// if
									// (!field.getName().equalsIgnoreCase(parameterEntity.getName()
									// + 's'))
									// {
									// rel.setLabel(field.getName());
									// }
									//
									rel.setLabel(field.getName());
									rel.setCardinality("0..n");
									model.addRelation(rel);
								}
							}
						}
					}
					else
					{
						Entity fieldTypeEntity = getEntity(model, field.getType());
		
						if (fieldTypeEntity != null)
						{
							if (fieldTypeEntity.isPrimitive())
							{
								Attribute att = new Attribute();
								att.setName(field.getName());
								att.setVisibility(getVisibility(field));
								att.setType(fieldTypeEntity);
								entity.getAttributes().add(att);
							}
							else
							{
								AssociationRelation rel = new AssociationRelation(fieldTypeEntity, entity);
								rel.setType(AssociationRelation.TYPE.AGGREGATION);
		
								// if (fieldTypeEntity.getName().contains("$"))
								// System.out.println("inner class: " +
								// fieldTypeEntity.getName());
		
								if (fieldTypeEntity.getName().toUpperCase().indexOf(field.getName().toUpperCase()) < 0)
								{
									rel.setLabel(field.getName());
								}
		
								rel.setCardinality("1");
								model.addRelation(rel);
							}
						}
					}
				}
			}
		
		} catch (VerifyError e){
			return;
		}
		}

	@SuppressWarnings("finally")
	private void initOperations(Class<?> clazz, Entity entity, Model model)
	{
		try
		{
			for (Method method : clazz.getDeclaredMethods())
			{
				Entity typeEntity = getEntity(model, method.getReturnType());

				if (typeEntity != null)
				{
					Operation op = new Operation();
					op.setClassStatic((method.getModifiers() & Modifier.STATIC) != 0);
					op.setName(method.getName());
					op.setVisibility(getVisibility(method));
					op.setType(typeEntity);

					Class<?>[] parms = method.getParameterTypes();

					for (int j = 0; j < parms.length; ++j)
					{
						Entity parmEntity = getEntity(model, parms[j]);

						if (parmEntity == null)
						{
							return;
						}
						else
						{
							op.getParameterList().add(parmEntity);
						}

					}

					entity.getOperations().add(op);

				}
			}
		}
		catch (NoClassDefFoundError ex)
		{
			ex.printStackTrace();

		} 
		finally{
		    return;
		}
	}

	private Entity getEntity(Model model, Class<?> c)
	{
		Entity e = (Entity) primitiveMap.get(c);

		if (e == null)
		{
			e = Entities.findEntityByName(model, computeEntityName(c));

			if (e == null && c != Object.class && Entities.isValidEntityName(computeEntityName(c)))
			{
				e = new Entity();
				e.setPrimitive(true);
				e.setName(computeEntityName(c));
				model.addEntity(e);
			}
		}

		return e;
	}

	private Visibility getVisibility(Member m)
	{
		if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		{
			return Visibility.PUBLIC;
		}
		else if ((m.getModifiers() & Modifier.PROTECTED) != 0)
		{
			return Visibility.PROTECTED;
		}
		else if ((m.getModifiers() & Modifier.PRIVATE) != 0)
		{
			return Visibility.PRIVATE;
		}
		else
		{
			return Visibility.PRIVATE;
		}
	}

}
