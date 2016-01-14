package org.lucci.lmu.input;

import java.util.Map;

public class ByteClassLoader extends ClassLoader {
 
    protected Class<?> findClass(final String name, byte[] data) throws ClassNotFoundException {
       System.out.println(name);
       try {
    	   return defineClass(name, data, 0, data.length);
	} catch (NoClassDefFoundError e) {
		System.out.println("looool");
		return null;
	}
  	  // return defineClass(name, data, 0, data.length);   
    	 
    }

  }