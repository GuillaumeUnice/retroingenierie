package org.lucci.lmu.input;

public class JarFileAnalyser extends ModelFactory
{
	@Override
	public AbstractJavaAnalyser createConcreteProduct(String path) throws ParseError
	{	
		return new JarJavaAnalyser();
	}

}
