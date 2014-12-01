package com.messners.ajf.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import com.messners.ajf.logging.Logging;
import com.messners.ajf.ui.GuiApplication;


/**
 * This class is a generic object loader.
 *
 * @author Greg Messner <greg@messners.com>
 */
public class ObjectLoader {


	protected static Logger logger = Logging.getLogger(ObjectLoader.class);
	
	protected String packageName = null;
	protected String classSuffix = null;
	protected ClassLoader classLoader  = null;

	
	/**
	 * Construct an object loader with no package name or class suffix.
	 */
	public ObjectLoader () {
	}


	/**
	 * Construct an object loader with no package name or class suffix.
	 *
	 * @param  loader_object
	 */
	public ObjectLoader (Object loader_object) {
		classLoader = loader_object.getClass().getClassLoader();
	}


	/**
	 * Construct an object loader with the specified package name and
	 * class suffix.
	 *
	 * @param  loader_object
	 * @param  package_name
	 * @param  class_suffix
	 */
	public ObjectLoader (Object loader_object,
				String package_name, String class_suffix) {
		classLoader = loader_object.getClass().getClassLoader();
		packageName = package_name;
		classSuffix = class_suffix;
	}


	/**
	 * Get the default package name for an action.
	 */
	public String getPackageName () {
		return (packageName);
	}


	/**
	 * Set the default package name to load actions from
	 */
	public void setPackageName (String pkg) {
		packageName = pkg;
	}


	/**
	 * 
	 */
	public String nameToClassName (String name) {

		if (name == null) {
        		return (null);
		}

		if (name.indexOf('.') == -1) {
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		int i = 0;
		while (true) {
		        i = name.indexOf("-", i);
        		if (i > 0) {
                		String tmpname = name.substring(0, i);
                		name = tmpname + name.substring(i + 1, i + 2).
                        		toUpperCase() + name.substring(i + 2);
        		} else {
                		break;
        		}
		}

		/*
		 * Do we need to prepend the package name or class suffix?
		 */
		if (name.indexOf('.') == -1) {

			if (packageName != null && !name.startsWith(packageName)) {
				name = packageName + "." + name;
			}

			/*
			 * Do we need to append the class suffix?
			 */
			if (classSuffix != null && !name.endsWith(classSuffix)) {
				name = name + classSuffix;
			}
		}
		
		return (name);
	}


	/**
	 *
	 * @exception ClassNotFoundException when the class for the
	 *            specified name cannot be found
	 */
	public Object loadObject (String name)
		throws IllegalArgumentException, ClassNotFoundException {

		return (loadObject(name, (Class [])null, (Object [])null));
	}


	public Object loadObject (
		String name, Class<? extends GuiApplication> type, Object arg) 
		throws IllegalArgumentException, ClassNotFoundException {

		Class<?> argtypes[] = new Class[1];
		argtypes[0] = type;

		Object args[] = new Object[1];
		args[0] = arg;

		return (loadObject(name, argtypes, args));
	}


	public Object loadObject (
		String name, Class<?> type0, Object arg0, 
		Class<?> type1, Object arg1)
		throws IllegalArgumentException, ClassNotFoundException {

		Class<?> argtypes[] = new Class[2];
		argtypes[0] = type0;
		argtypes[1] = type1;

		Object args[] = new Object[2];
		args[0] = arg0;
		args[1] = arg1;

		return (loadObject(name, argtypes, args));
	}


	public Object loadObject (
		String name, Class<?> type0, Object arg0, 
		Class<?> type1, Object arg1, Class<?> type2, Object arg2) 
		throws IllegalArgumentException, ClassNotFoundException {

		Class<?> argtypes[] = new Class[3];
		argtypes[0] = type0;
		argtypes[1] = type1;
		argtypes[2] = type2;

		Object args[] = new Object[3];
		args[0] = arg0;
		args[1] = arg1;
		args[2] = arg2;

		return (loadObject(name, argtypes, args));
	}


	public Object loadObject (
		String name, Class<?> argtypes[], Object args[]) 
		throws IllegalArgumentException, ClassNotFoundException {

		String class_name = nameToClassName(name);

		/*
		 * Validate the parameter type Classes for the constructor
		 */
		if (argtypes != null && args != null &&
			argtypes.length != args.length) {

			throw new IllegalArgumentException(
				"invalid argument parameters");

		} else if ((argtypes != null && args == null) ||
			(argtypes == null && args != null)) {

				throw new IllegalArgumentException(
					"invalid argument parameters");
		}
		
	
		Class<?> clazz;
		if (classLoader != null) {
			clazz = classLoader.loadClass(class_name);
		} else {
			clazz = Class.forName(class_name);
		}

		Object obj = null;
		try {

			if (argtypes == null) {
				obj = clazz.newInstance();
			} else {
				Constructor<?> constructor = 
					clazz.getConstructor(argtypes);
				obj = constructor.newInstance(args);
			}

		} catch (InvocationTargetException ite) {

			if (ite.getCause() != null) {
				ite.getCause().printStackTrace();
			} else {
				ite.printStackTrace();
			}

			System.err.println("error loading " +  name);
			System.err.println(ite.getTargetException().getMessage());

		} catch (InstantiationException ie) {

			System.err.println("error loading " +  name);

			if (ie.getCause() != null) {
				ie.getCause().printStackTrace();
				System.err.println(ie.getCause().getMessage());
			} else {
				ie.printStackTrace();
				System.err.println(ie.getMessage());
			}

		} catch (Exception ignore) {

			ignore.printStackTrace();
			System.err.println("error loading " +  name);
			System.err.println(ignore.getMessage());
		}

		return (obj);
	}
}
