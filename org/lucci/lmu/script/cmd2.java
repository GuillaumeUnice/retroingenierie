package org.lucci.lmu.script;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.lucci.lmu.Entity;
import org.lucci.lmu.InheritanceRelation;
import org.lucci.lmu.Model;
import org.lucci.lmu.Relation;
import org.lucci.lmu.UnitDeploy;
import org.lucci.lmu.input.AbstractAnalyser;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ModelFactory;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import java4unix.CommandLine;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

public class cmd2 {

	public static void main(String[] args) throws Throwable
	{
		cmd2 abstractAnalyser = new cmd2();
		HashMap<String,String> dependencies = new HashMap<>();
		dependencies.put("John", "Doe Bla Pour");
		dependencies.put("Doe", "Bot Ro");
		dependencies.put("Pour", "U");
		abstractAnalyser.runScript("John",dependencies, "res.png");
	}
	
	public int runScript(String entryPoint, HashMap<String,String> dependencies,String file) {

		
		Model model = new Model();
		//Entity unitDeploy = new Entity();
		UnitDeploy unitDeploy = new UnitDeploy();
		unitDeploy.setName(entryPoint);
		//e.setNamespace(""); A utiliser
		model.addUnitDeploy(unitDeploy);
		//model.addEntity(unitDeploy);
		createModelRecursive(unitDeploy,dependencies,model);
		
		ModelFactory modelFactory = null;
			
				RegularFile outputFile;
				try {
					
					outputFile = new RegularFile("res.pdf");	
				} catch (Exception e) {

					outputFile = new RegularFile("lol.pdf");	

				}
				String outputType = FileUtilities.getFileNameExtension(outputFile.getName());
				AbstractWriter factory = AbstractWriter.getTextFactory(outputType);
					
				if (factory == null)
				{
					System.out.println("Do not know how to generate '" + outputType + "' code\n");
				}
				else
				{
					
					
					System.out.println(model.getEntities().size() + " entities and " + model.getRelations().size() + " relations\n");

					try
					{
						System.out.println("Writing file " + outputFile.getPath() + "\n");
						
						byte[] outputBytes = factory.writeModel(model);
						
						outputFile.setContent(outputBytes);
					}
					catch (WriterException ex)
					{
						System.err.println(ex.getMessage() + "'\n");
					}
					catch (IOException ex)
					{
						System.err.println("I/O error while writing file " + outputFile.getPath() + "\n");
					}
				}
		
		return 0;
	}

	public static void createModelRecursive(Entity entryPoint,HashMap<String,String> dependencies,Model model){

		if(dependencies.get(entryPoint.getName())!=null){
			String[] dep = dependencies.get(entryPoint.getName()).split(" ");
			for(int i=0; i<dep.length; i++){
				Entity unitDeploy = new Entity();
				unitDeploy.setName(dep[i]);
				//e.setNamespace(""); A utiliser
				model.addEntity(unitDeploy);
				model.addRelation(new InheritanceRelation(entryPoint,unitDeploy));
				createModelRecursive(unitDeploy,dependencies,model);
			}
		}
	}
	
	public static void createModelRecursive(UnitDeploy entryPoint,HashMap<String,String> dependencies,Model model){

		if(dependencies.get(entryPoint.getName())!=null){
			String[] dep = dependencies.get(entryPoint.getName()).split(" ");
			for(int i=0; i<dep.length; i++){
				UnitDeploy unitDeploy = new UnitDeploy();
				unitDeploy.setName(dep[i]);
				//e.setNamespace(""); A utiliser
				model.addUnitDeploy(unitDeploy);
				model.addRelationUnitDeploy(new InheritanceRelation(entryPoint,unitDeploy));
				createModelRecursive(unitDeploy,dependencies,model);
			}
		}
	}

}
