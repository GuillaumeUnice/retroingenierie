package org.lucci.lmu.input;

import org.lucci.lmu.Model;

public abstract class AbstractAnalyser {
	
	public abstract Model createConcreteModel(String path) throws ParseError;

}
