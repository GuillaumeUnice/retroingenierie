package org.lucci.lmu.input;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.lucci.lmu.Entities;
import org.lucci.lmu.Entity;
import org.lucci.lmu.Model;

import toools.ClassContainer;
import toools.ClassPath;
import toools.io.file.RegularFile;

public class FileLmuAnalyser extends AbstractLmuAnalyser{
	//singleton
    private final static FileLmuAnalyser parser = new FileLmuAnalyser();

    public static FileLmuAnalyser getParser()
    {
	return parser;
    }

    private FileLmuAnalyser()
    {
    }
	 @Override
	    public Model createConcreteModel(String str) throws ParseError
	    {
			RegularFile inputFile = new RegularFile(str);
	    	byte[] data;
			try {
				data = inputFile.getContent();
		    	try {
					return createModel(new String(data));
				} catch (ModelException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return null;

	    }
	
}
