package org.lucci.lmu.input;


public class LmuParser extends ModelFactory
{
	
	@Override
	public AbstractLmuAnalyser createConcreteProduct(String path) throws ParseError
	{	
		return FileLmuAnalyser.getParser();
	}
	
}
