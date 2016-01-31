package org.lucci.lmu.input;


public class JavaFileAnalyser extends ModelFactory {

	
	public AbstractJavaAnalyser createConcreteProduct(String path) throws ParseError {
		return new FileJavaAnalyser();
	}

}