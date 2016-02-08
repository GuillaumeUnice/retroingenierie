package org.lucci.lmu;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lucci.lmu.input.ModelException;

import toools.collections.Collections;
import toools.math.relation.HashRelation;

public class DeploymentUnits {
	public static Set<ModelElement> findAllModelElementsInUnitDeploy(UnitDeploy unitDeploy)
	{
		Set<ModelElement> res = new HashSet<ModelElement>();

		return res;
	}

	public static UnitDeploy findUnitDeployByName(Model model, String name)
	{
		if (model == null) throw new NullPointerException();
		if (name == null) throw new NullPointerException();

		for (UnitDeploy unitDeploy : model.getUnitDeploy())
		{
			if (unitDeploy.getName().equals(name))
			{
				return unitDeploy;
			}
		}

		return null;
	}

	public static toools.math.relation.Relation<String, UnitDeploy> findNameSpaces(Set<UnitDeploy> deployementUnits)
	{
		if (deployementUnits == null) throw new NullPointerException();
		toools.math.relation.Relation<String, UnitDeploy> res = new HashRelation<String, UnitDeploy>();
		
		for (UnitDeploy unit : deployementUnits)
		{
			res.add(unit.getNamespace(), unit);
		}

		return res;
	}

	public static Set<UnitDeploy> findUnitDeployByNameSpace(Set<UnitDeploy> deployementUnits, String name)
	{
		if (name == null) throw new NullPointerException();
		toools.math.relation.Relation<String, UnitDeploy> r =  findNameSpaces(deployementUnits);
		Collection<UnitDeploy> deployementUnitsInNamespace = r.getValues(name);
		return deployementUnitsInNamespace == null ? new HashSet() : new HashSet(deployementUnitsInNamespace);
	}

	// TODO
	public static Set<Set<UnitDeploy>> findConnectedComponents(Set<UnitDeploy> deploymentUnits, final Model model)
	{
		deploymentUnits = new HashSet<UnitDeploy>(deploymentUnits);
		Set<Set<UnitDeploy>> connectedComponents = new HashSet<Set<UnitDeploy>>();

		while (!deploymentUnits.isEmpty())
		{
			Set<UnitDeploy> someUnitDeploy = new HashSet(Collections.singleton(deploymentUnits.iterator().next()));
			Set<UnitDeploy> connectedComponent = findDeploymentUnitsConnectedTo(someUnitDeploy, Integer.MAX_VALUE, model);
			connectedComponents.add(connectedComponent);
			deploymentUnits.removeAll(connectedComponent);
		}

		return connectedComponents;
	}

	public static Set<UnitDeploy> findLargestConnectedComponent(Set<UnitDeploy> deploymentUnits, final Model model)
	{
		return (Set<UnitDeploy>) Collections.getLargestCollections(findConnectedComponents(deploymentUnits, model)).iterator().next();
	}

	public static Set<UnitDeploy> findIsolatedDeploymentUnits(Set<UnitDeploy> deploymentUnits, final Model model)
	{
		Set<UnitDeploy> isolatedDeploymentUnits = new HashSet<UnitDeploy>();

		for (UnitDeploy UnitDeploy : deploymentUnits)
		{
			if (Relations.findRelationsInvolvingUnitDeploy(UnitDeploy, model.getRelations()).isEmpty())
			{
				isolatedDeploymentUnits.add(UnitDeploy);
			}
		}

		return isolatedDeploymentUnits;
	}

	public static Set<UnitDeploy> getNonPublicDeploymentUnits(Set<UnitDeploy> deploymentUnits)
	{
		return new HashSet<UnitDeploy>(Collections.filter(deploymentUnits, new Filters.VisiblityFilter(Visibility.PUBLIC)));
	}

	public static Collection<org.lucci.lmu.Relation> removeDeploymentUnits(Collection<UnitDeploy> deploymentUnitsToRemove, Model model)
	{
		Collection<org.lucci.lmu.Relation> removed = new HashSet<org.lucci.lmu.Relation>();

		for (UnitDeploy UnitDeploy : deploymentUnitsToRemove)
		{
			removed.addAll(model.removeUnitDeploy(UnitDeploy));
		}

		return removed;
	}

	public static Collection<UnitDeploy> getNeighborDeploymentUnits(UnitDeploy UnitDeploy, Model model)
	{
		Collection<UnitDeploy> neighbors = new HashSet<UnitDeploy>();
		Collection<org.lucci.lmu.Relation> relations = Relations.findRelationsInvolvingUnitDeploy(UnitDeploy, model.getRelations());

		for (org.lucci.lmu.Relation relation : relations)
		{
			neighbors.add(relation.getTailUnitDeploy() == UnitDeploy ? relation.getHeadUnitDeploy() : relation.getTailUnitDeploy());
		}

		return neighbors;
	}

	public static Set<UnitDeploy> getNeighborDeploymentUnits(Collection<UnitDeploy> deploymentUnits, Model model)
	{
		Set<UnitDeploy> neighbors = new HashSet<UnitDeploy>();

		for (UnitDeploy e : deploymentUnits)
		{
			neighbors.addAll(getNeighborDeploymentUnits(e, model));
		}

		return neighbors;
	}

	public static Set<UnitDeploy> findDeploymentUnitsWhoseNameMatch(Collection<UnitDeploy> deploymentUnits, String regexp)
	{
		Set<UnitDeploy> matchingDeploymentUnits = new HashSet<UnitDeploy>();

		for (UnitDeploy UnitDeploy : deploymentUnits)
		{
			if (UnitDeploy.getName().matches(regexp))
			{
				matchingDeploymentUnits.add(UnitDeploy);
			}
		}

		return matchingDeploymentUnits;
	}

	public static Set<UnitDeploy> findDeploymentUnitsConnectedTo(Set<UnitDeploy> deploymentUnits, int distance, Model model)
	{
		if (distance < 0) throw new IllegalArgumentException();
		Set<UnitDeploy> res = new HashSet<UnitDeploy>(deploymentUnits);

		int previousSize = -1;
		int size = res.size();

		while (distance-- > 0 && size != previousSize)
		{
			res.addAll(getNeighborDeploymentUnits(res, model));
			previousSize = size;
			size = res.size();
		}

		return res;
	}

	public static void removeJavaPackageNames(Set<UnitDeploy> deploymentUnits, Model model) throws ModelException
	{
		for (UnitDeploy UnitDeploy : deploymentUnits)
		{
			int pos = UnitDeploy.getName().lastIndexOf('.');

			if (pos > 0)
			{
				String newName = UnitDeploy.getName().substring(pos + 1);
				UnitDeploy clashingUnitDeploy = findUnitDeployByName(model, newName);

				if (clashingUnitDeploy != null)
				{
					throw new ModelException("Cannot do that because then two classes would confict!", null, new NamedModelElement[]
					{ UnitDeploy, clashingUnitDeploy });
				}

				UnitDeploy.setName(newName);
			}
		}
	}

	public static boolean isValidUnitDeployName(String s)
	{
		if (s.length() == 0)
		{
			return false;
		}
		else
		{
			if (!Character.isLetter(s.charAt(0)) && s.charAt(0) != '_')
			{
				return false;
			}
			else
			{
				if (s.indexOf("..") >= 0 || s.startsWith(".") || s.endsWith("."))
				{
					return false;
				}
				else
				{
					for (int i = 1; i < s.length(); ++i)
					{
						char c = s.charAt(i);

						if (!Character.isLetterOrDigit(c) && c != '_' && c != '.')
						{
							return false;
						}
					}

					return true;
				}
			}
		}
	}

	public static void retainDeploymentUnits(Set<UnitDeploy> s, Model model)
	{
		removeDeploymentUnits((Set<UnitDeploy>) Collections.difference(model.getUnitDeploy(), s), model);
	}

	public static Set<UnitDeploy> findNonPrimitiveDeploymentUnits(Model model)
	{
		Set<UnitDeploy> matchingDeploymentUnits = new HashSet<UnitDeploy>();

		/*for (UnitDeploy UnitDeploy : model.getUnitDeploy())
		{
			if (!UnitDeploy.isPrimitive())
			{
				matchingDeploymentUnits.add(UnitDeploy);
			}
		}*/

		return matchingDeploymentUnits;
	}

	public static Set<UnitDeploy> findUnitDeployWhoseNameSpaceMatches(Set<UnitDeploy> model, String regexp)
	{
		toools.math.relation.Relation<String, UnitDeploy> namespaces = findNameSpaces(model);
		Set<UnitDeploy> res = new HashSet<UnitDeploy>();
		
		for (String namespace : namespaces.getKeys())
		{
			if (namespace.matches(regexp))
			{
				res.addAll(namespaces.getValues(namespace));
			}
		}
		
		return res;
	}

	public static Collection<? extends UnitDeploy> findSuperDeploymentUnits(UnitDeploy subUnitDeploy, Model model)
	{
		Set<UnitDeploy> res = new HashSet<UnitDeploy>();
		
		for (UnitDeploy e : model.getUnitDeploy())
		{
			if (DeploymentUnits.isSuperUnitDeploy(e, subUnitDeploy, model.getRelations()))
			{
				res.add(e);
			}
		}
		
		return res;
	}

	public static boolean isSuperUnitDeploy(UnitDeploy mother, UnitDeploy child, Set<Relation> among)
	{
		for (Relation r : Relations.findRelationsInvolvingUnitDeploy(among, mother, child))
		{
			if (r instanceof InheritanceRelation)
			{
				InheritanceRelation ir = (InheritanceRelation) r;
				
				if (ir.getSubUnitDeploy() == child && ir.getSuperUnitDeploy() == mother)
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
