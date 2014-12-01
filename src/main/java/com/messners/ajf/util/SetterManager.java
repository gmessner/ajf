package com.messners.ajf.util;

import java.util.HashMap;

/**
 * This class provides a manager for managing Setter class implementations.
 * A Setter class is used for mapping a name to functionality contained
 * in a Setter class.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class SetterManager {

   /**
    * Holds the name to Setter instance map
    */
   protected HashMap<Object, Setter> setterMap = null;


   public SetterManager () {
        super();
   }


   /**
    * This interface provides a way to map a set method that
    * takes a String, int, float, or Enum parameter to a name.
    */
   public interface Setter {
      public void set (Object container, float value);
      public void set (Object container, int value);
      public void set (Object container, String value);
      public void set (Object container, Enum<?> value);
   }


   /**
    * This abstract class provides a way to map a set method that
	* takes a String parameter to a name.
    */
   public abstract static class StringSetter implements Setter {

      public void set (Object container, float value) {
         set(container, Float.toString(value));
      }

      public void set (Object container, int value) {
         set(container, Integer.toString(value));
      }

      public void set (Object container, Enum<?> value) {
		 throw new RuntimeException("Enum setter not supportted");
      }
   }


   /**
    * This abstract class provides a way to map a set method that
	* takes an int parameter to a name.
    */
   public abstract static class IntSetter implements Setter {

      public void set (Object container, float value) {
         set(container, (int)value);
      }

      public void set (Object container, String value) {
         if (StringUtils.isNumeric(value)) {
            set(container, Integer.parseInt(value));
         }
      }

      public void set (Object container, Enum<?> value) {
		throw new RuntimeException("Enum setter not supportted");
      }
   }


   /**
    * This abstract class provides a way to map a set method that
    * takes an float parameter to a name.
    */
   public abstract static class FloatSetter implements Setter {

      public void set (Object container, int value) {
         set(container, (float)value);
      }

      public void set (Object container, String value) {
         if (StringUtils.isNumeric(value)) {
            set(container, Float.parseFloat(value));
         }
      }

      public void set (Object container, Enum<?> value) {
		 throw new RuntimeException("Enum setter not supportted");
	  }
   }


   /**
    * This abstract class provides a way to map a set method that
	* takes an Enum parameter to a name.
    */
   public abstract static class EnumSetter implements Setter {

      public void set (Object container, int value) {
		 throw new RuntimeException("int setter not supportted");
      }

      public void set (Object container, float value) {
		 throw new RuntimeException("float setter not supportted");
	  }

      public void set (Object container, String value) {
		 throw new RuntimeException("String setter not supportted");
	  }
   }


   /**
    * Register a Setter instance.
    *
    * @param key    the key to map the setter to
    * @param setter the Setter instance to register
    */
   public void registerSetter (Object key, Setter setter) {

      if (setterMap == null) {
         setterMap = new HashMap<Object, Setter>();
      }

      setterMap.put(key, setter);
   }


   /**
    * Lookup a registered Setter by key.
    */
   public Setter lookupSetter (Object key) {
       return setterMap.get(key);
   }
}
