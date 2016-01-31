package org.lucci.lmu.input;


public class ByteClassLoader extends ClassLoader {
 
    protected Class<?> findClass(final String name, byte[] data) throws ClassNotFoundException {
       try {
    	   return defineClass(name, data, 0, data.length);
		} catch (NoClassDefFoundError e) {
			return null;
		}
    }
}