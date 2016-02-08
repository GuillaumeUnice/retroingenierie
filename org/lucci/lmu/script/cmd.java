package org.lucci.lmu.script;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java4unix.CommandLine;


import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;
import org.lucci.lmu.input.AbstractAnalyser;
import org.lucci.lmu.input.AbstractJavaAnalyser;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ModelFactory;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import toools.io.FileUtilities;
import toools.io.file.RegularFile;

/*
 * Created on Oct 2, 2004
 */

/**
 * @author luc.hogie
 */
public class cmd extends LmuScript
{

	public static void main(String[] args) throws Throwable
	{
		cmd test = new cmd();
		//test.run("/home/21307458/workspace/ProgDist/bin");

		//test.run("/home/21307458/workspace/RetroIngenierie/bin", "lol.png");
		//test.run("/home/21307458/workspace/RetroIngenierie/bin/org/lucci/lmu/Filters.class");
		
		//test.run("/home/21307458/Téléchargements/all.lmu");
		
		test.run("/home/21307458/Téléchargements/exo16.jar");
		//test.run("/home/21307458/workspace/ProgDist/bin/client/MonInterfaceDistante.class");
	}

	@Override
	public int runScript(CommandLine cmdLine)
	{
		// Assertions.ensure(Posix.commandIsAvailable("dot"),
		// "Graphiz is not installed on this computer (the 'dot' command was not found)! Please check http://www.graphviz.org/");
		List<String> args = cmdLine.findArguments();
		RegularFile inputFile = new RegularFile(args.get(0));
		String inputType = FileUtilities.getFileNameExtension(inputFile.getName());
		

		try
		{
			//System.out.println(inputType);
			//ModelFactory modelFactory = ModelFactory.getModelFactory();
			ModelFactory modelFactory = ModelFactory.getModelFactory(inputType);
			

			if (modelFactory == null)
			{
				printNonFatalError("No parser defined for input type '" + inputType + "\n");
			}
			else
			{
				RegularFile outputFile;
				try {
					outputFile = new RegularFile(args.size() == 1 ? FileUtilities.replaceExtensionBy(inputFile.getName(), "pdf") : args.get(1));	
				} catch (Exception e) {

					outputFile = new RegularFile("lol.pdf");	

				}
				String outputType = FileUtilities.getFileNameExtension(outputFile.getName());
				AbstractWriter factory = AbstractWriter.getTextFactory(outputType);
					
				if (factory == null)
				{
					printFatalError("Do not know how to generate '" + outputType + "' code\n");
				}
				else
				{
					
					AbstractAnalyser test = modelFactory.createConcreteProduct(args.get(0));
					Model model = test.createConcreteModel(args.get(0));
					
					printMessage(model.getEntities().size() + " entities and " + model.getRelations().size() + " relations\n");

					try
					{
						printMessage("Writing file " + outputFile.getPath() + "\n");
						
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
			}
		}
		catch (ParseError ex)
		{
			System.err.println("Parse error: " + ex.getMessage() + "\n");
		}
		catch (ModelException ex)
		{
			System.err.println("Model error: " + ex.getMessage() + "\n");
		}

		return 0;
	}

}
