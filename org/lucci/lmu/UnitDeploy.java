package org.lucci.lmu;

import java.util.Arrays;
import java.util.Random;

import toools.collections.Collections;
import toools.util.assertion.Assertions;

public class UnitDeploy extends NamedModelElement{
	private String color = null;
	private Model model;
	private String namespace = DEFAULT_NAMESPACE;
	//private final List<String> stereoTypeList = new ArrayList();


	public static final String DEFAULT_NAMESPACE = "default namespace";

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace(String namespace)
	{
		if (namespace == null)
			throw new NullPointerException();

		this.namespace = namespace;
	}

	public Model getModel()
	{
		return model;
	}

	@Override
	public void setName(String name)
	{
		if (getModel() != null && Entities.findEntityByName(getModel(), name) != null)
		{
			throw new IllegalArgumentException("an unitDeploy with the same name already exist");
		}

		super.setName(name);
	}

	public void setModel(Model model)
	{
		this.model = model;
	}

	public String getColorName()
	{
		return color;
	}

	@Override
	public String toString()
	{
		return getName() + "(" + getNamespace() + ")";
	}


	public void setColorName(String color)
	{
		if (color.equals("random"))
		{
			color = Collections.pickRandomObject(Arrays.asList(new String[] { "red", "blue", "green", "yellow" }),
					new Random());
		}

		this.color = color;
	}

	public void merge(Entity e)
	{
		Assertions.ensure(e.getName().equals(getName()), "unit deploy must have the same name");

		
	}

	@Override
	public int hashCode()
	{
		return (getNamespace() + '.' + getName()).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass() == getClass() && obj.hashCode() == hashCode();
	}

	public Object clone(String newName) throws CloneNotSupportedException
	{
		UnitDeploy clone = new UnitDeploy();

		clone.color = this.color;
		clone.namespace = this.namespace;
		clone.model = this.model;
		clone.model = this.model;
		clone.setName(newName);
		return clone;
	}
}
