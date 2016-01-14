package org.lucci.lmu.input;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
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

import toools.ClassContainer;
import toools.ClassPath;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

public class PackageAnalyser extends ModelFactory {

	
	@Override
	public AbstractJavaAnalyser createConcreteProduct(String path) throws ParseError, ModelException {
		return new JavaPackageAnalyser();
	}
}
