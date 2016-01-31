package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class ModelFactory
{

	static private Map<String, ModelFactory> factoryMap = new HashMap<String, ModelFactory>();

	static
	{
		factoryMap.put(null, new PackageAnalyser());
		factoryMap.put("lmu", new LmuParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("class", new JavaFileAnalyser());

	}

	public static ModelFactory getModelFactory(String type)
	{
		return factoryMap.get(type);
	}

	public abstract AbstractAnalyser createConcreteProduct(String path) throws ParseError, ModelException;
	
}
