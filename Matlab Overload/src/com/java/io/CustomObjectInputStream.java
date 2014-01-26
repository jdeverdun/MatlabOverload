package com.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.*;

import com.java.modeles.Worker;


/**
 * Custom Object InputStream
 * to bypass some errors
 * @author Jérémy DEVERDUN
 *
 */
public class CustomObjectInputStream extends ObjectInputStream {
		private ClassLoader classLoader;
 
		/**
		 * 
		 * @param in : InputStream
		 * @throws IOException
		 */
		public CustomObjectInputStream(InputStream in) throws IOException {
			super(in);
		}
 
		
		/**
		 * 
		 */
		protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
				ClassNotFoundException {			
			return Class.forName(desc.getName(), false, Worker.class.getClassLoader());
		}
}
