package com.messners.ajf.ui;

import com.messners.ajf.data.MetaConverter;
import com.messners.ajf.reflect.ClassUtils;
import com.messners.ajf.reflect.MethodUtils;

import java.awt.Component;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class NameValueTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;


    /**
	 * Default name for column 0.
	 */
	public static final String DEFAULT_COLUMN_0_NAME = "Field";


	/**
	 * Default name for column 1.
	 */
	public static final String DEFAULT_COLUMN_1_NAME = "Value";


	/**
	 * Holds the object instance that holds the values for the table.
	 */
	protected Object valueObject;


	/**
	 * Holds the name of the model, will be null if not created from
	 * a resource file..
	 */
	protected String modelName;


    /**
	 * Names of the columns, used for the TableModel getColumnName method.
	 * Usually set to "Field" and "Value".
	 */
    protected String columnNames[];


	/**
	 * The name for each row.
	 */
	protected String rowNames[];
	protected String rowNameMethods[];
	protected HashMap<String, HashMap<Class<?>, Method>> rowNameMap;


    /**
	 * Method names used to determine a particular value. Used for the
     * TableModel method getValueAt.
	 */
    private String getMethodNames[];
	private HashMap<Class<?>, Method> getMethodMap[];
    private Class<?> getParamTypes[];
    private Object getParamValues[];

	private String units[];
	private StringBuffer valueBuffer = new StringBuffer();


    /**
	 * Has method names, used to see if a particular value is applicable.
     * A null entry, or array, indicates that the particular value
	 * is always applicable.
     */
    private String hasMethodNames[];
	private HashMap<Class<?>, Method> hasMethodMap[];


    /**
	 * Setter method names, used to set a particular value. Used for the
     * TableModel method setValueAt. A null entry, or array, indicates the
     * column is not editable.
     */
    private String setMethodNames[];
	private HashMap<Class<?>, Method> setMethodMap[];


    /**
	 * This used for get method paramaters.
     */


	/**
	 * Creates a NameValueTableModel instance loading the configuration
	 * from the ResourceBundle owned by resourceOwner.
	 *
	 * @param  resourceOwner  the owing class of the ResourceBundle
	 * @param  model          the name of the model in the ResourceBundle
	 */
    public NameValueTableModel (Class<?> resourceOwner, String model) {

		super();
		ResourceLoader loader = new ResourceLoader(resourceOwner);
		init(loader, model);
	}


	/**
	 * Creates a NameValueTableModel instance loading the configuration
	 * from the specified ResourceLoader.
	 *
	 * @param  loader  the ResourceLoader to load the model from
	 * @param  model   the name of the model in the ResourceBundle
	 */
    public NameValueTableModel (ResourceLoader loader, String model) {

		super();
		init(loader, model);
	}



	/**
	 */
    protected NameValueTableModel () {
		super();
	}


	protected void init (ResourceLoader loader, String model) {

		modelName = model;

		String names[] = loader.splitResource(model);
		if (names == null || names.length == 0) {
			throw new RuntimeException("\"" + model + "\" model not found");
		}

		int numRows = names.length;
		units = new String[numRows];
		String rowNames[] = new String[numRows];
		String getMethods[] = new String[numRows];
		Class<?> paramTypes[] = new Class<?>[numRows];
		Object paramValues[] = new Object[numRows];
		String hasMethods[] = new String[numRows];
		String setMethods[] = new String[numRows];
		for (int i = 0; i < numRows; i++) {

			String name = names[i];

			String s = loader.getResource(name + ".name");
			rowNames[i] = s;

			s = loader.getResource(name + ".units");
			units[i] = s;
		
			s = loader.getResource(name + ".get-method");
			getMethods[i] = s;

			s = loader.getResource(name + ".has-method");
			hasMethods[i] = s;

			s = loader.getResource(name + ".set-method");
			setMethods[i] = s;

			s = loader.getResource(name + ".get-parameter.value");
			if (s != null) {

				Class<?> type = ClassUtils.getWrapperClass(loader.getResource(name + ".get-parameter.type"));
				if (type != null) {

					try {

						Object value = MetaConverter.convert(s, type);
						paramValues[i] = new Object[1];
						((Object[])paramValues[i])[0] = value;
						paramTypes[i] = type;

					} catch (Exception ignore) {
					}
				}
			}
		}

		String columnNames[] = loader.splitResource(model + ".column-names");
		if (columnNames == null || columnNames.length == 0) {
			columnNames = loader.splitResource("column-names");
		}

		init(columnNames, rowNames, getMethods, 
					paramValues, paramTypes, hasMethods, setMethods);
	}


    /**
     * Constructor for creating a NameValueTableModel.
     */
    public NameValueTableModel(
		String rowNames[], String getMethodNames[], String setMethodNames[]) {

		super();
		init(null, rowNames, getMethodNames, null, null, null, setMethodNames);
	}

    public NameValueTableModel(
		String rowNames[], String getMethodNames[],
		String hasMethodNames[], String setMethodNames[]) {

		super();
		init(null, rowNames, getMethodNames, null, null,
					hasMethodNames, setMethodNames);
	}


    /**
     * Constructor for creating a NameValueTableModel.
     */
    public NameValueTableModel(String columnNames[], String rowNames[],
				 String getMethodNames[], String hasMethodNames[],
				 String setMethodNames[]) {

		super();
    	init(columnNames, rowNames, getMethodNames, null, null,
						hasMethodNames, setMethodNames);
	}


    @SuppressWarnings("unchecked")
    protected void init (String columnNames[], String rowNames[],
				 String getMethodNames[], Object getParamValues[],
				 Class<?> getParamTypes[], String hasMethodNames[],
				 String setMethodNames[]) {

		if (columnNames == null || columnNames.length == 0) {

			columnNames = new String[2];
			columnNames[0] = DEFAULT_COLUMN_0_NAME;
			columnNames[1] = DEFAULT_COLUMN_1_NAME;
		}

		
		int numRows = rowNames.length;
		rowNameMethods = new String[numRows];
		rowNameMap = new HashMap<String, HashMap<Class<?>, Method>>();
		for (int i = 0; i < numRows; i++) {

			String s = rowNames[i];
			if (s != null && s.length() > 11 && s.startsWith("get-method:")) {

				rowNameMethods[i] = s.substring(11);
				HashMap<Class<?>, Method> map = new HashMap<Class<?>, Method>();
				rowNameMap.put(rowNameMethods[i], map);
				rowNames[i] = null;

			} else {

				rowNames[i] = s;
				rowNameMethods[i] = null;
			}
		}

		this.columnNames = columnNames;
		this.rowNames = rowNames;
		this.getMethodNames = getMethodNames;
		this.getParamValues = getParamValues;
		this.getParamTypes  = getParamTypes;
		this.hasMethodNames = hasMethodNames;
		this.setMethodNames = setMethodNames;

		getMethodMap = new HashMap[getMethodNames.length];
		hasMethodMap = new HashMap[getMethodNames.length];
		setMethodMap = new HashMap[getMethodNames.length];
    }


	/**
	 * Gets the value object for the table.
	 */
	public Object getValue () {
		return (valueObject);
	}


	/**
	 * Sets the value object for the table.
	 */
	public void setValue (Object value) {
		valueObject = value;

		int numRows = getRowCount();
		for (int i = 0; i < numRows; i++) {
		}
	}


    /**
     * Returns the number of column names passed into the constructor.
     */
    public int getColumnCount () {
		return (2);
    }


    /**
     * Returns the column name passed into the constructor.
     */
    public String getColumnName (int column) {

		if (column < 0 || column > 2) {
	    	return (null);
		}

		return (columnNames[column]);
    }


	/**
	 * Override the getRowCount() method to return the number of fields
	 * setup by this model.
	 */
	public int getRowCount () {
		return (getMethodNames.length);
	}


    /**
     * Returns the name for the row.
     */
    protected Object getRowName (int row) {

		String name = rowNames[row];
		if (name != null) {
			return (name);
		}

		name = rowNameMethods[row];
		HashMap<Class<?>, Method> map = rowNameMap.get(name);
		if (map == null) {

			if (name == null) {
				throw new RuntimeException(
					(modelName == null ? "" : (modelName + ": ")) +
					"name for row " + row + " is null");
			}

			return (getRowName(name));
		}

		if (valueObject == null) {
			return (getRowName(name));
		}

		Class<?> cls = valueObject.getClass();
		Method method = map.get(cls);
		if (method == null) {

			try {
				method = MethodUtils.getMethod(cls, name, (Class<?>)null);
			} catch (Exception e) {
				return (getRowName(name));
			}

			map.put(cls, method);
		}

		try {
			return (method.invoke(valueObject, (Object [])null));
		} catch  (Exception e) {
			return (getRowName(name));
		}
	}


	protected static String getRowName (String methodName) {

		if (methodName.startsWith("get")) {

			if (methodName.endsWith("Label")) {

				int len = methodName.length();
				return (methodName.substring(3, len - 5));
			}


			return (methodName.substring(3));
		}

		return (methodName);
	}


    /**
     * Returns the value for the column <code>column</code> and object
     * <code>node</code>. The return value is determined by invoking
     * the method specified in constructor for the passed in column.
     */
    public Object getValueAt (int row, int column) {

		if (column == 0) {
			return (getRowName(row));
		}
		
		if (valueObject == null) {
			return (null);
		}


		try {

		    Method method = getHasMethod(row);
		    if (method != null) {
				Boolean hasData = (Boolean)method.invoke(valueObject);
				if (Boolean.FALSE.equals(hasData)) {
					return (null);
				}
		    }

		} catch  (Exception e) {
		}

   
		try {

		    Method method = getGetMethod(row);
		    if (method != null) {

				if (units == null || units[row] == null) {
					return (method.invoke(valueObject, 
							(Object[])getParamValues[row]));
				}

				Object value = method.invoke(valueObject, 
							(Object[])getParamValues[row]);
				if (value instanceof String) {

					valueBuffer.setLength(0);
					valueBuffer.append(value).append(' ').append(units[row]);
					return (valueBuffer.toString());
				}

				return (value);
		    }

		} catch  (Exception e) {
		}
   
		return (null); 
    }


	protected Method getGetMethod (int row) throws Exception {

		HashMap<Class<?>, Method> map = getMethodMap[row];
		if (map == null) {
			map = new HashMap<Class<?>, Method>();
			getMethodMap[row] = map;
		}

		Class<?> cls = valueObject.getClass();
		Method m = map.get(cls);
		if (m != null) {
			return (m);
		}

		Class<?> paramType;
		if (getParamTypes != null) {
			paramType = getParamTypes[row];
		} else {
			paramType = null;
		}

		m = MethodUtils.getMethod(cls, getMethodNames[row], paramType);
		if (m != null) {
			map.put(cls, m);
		}

		return (m);
	}


	protected Method getHasMethod (int row) throws Exception {

		if (hasMethodNames == null) {
			return (null);
		}

		String methodName = hasMethodNames[row];
		if (methodName == null) {
			return (null);
		}

		HashMap<Class<?>, Method> map = hasMethodMap[row];
		if (map == null) {
			map = new HashMap<Class<?>, Method>();
			hasMethodMap[row] = map;
		}

		Class<?> cls = valueObject.getClass();
		Method m = map.get(cls);
		if (m != null) {
			return (m);
		}

		m = MethodUtils.getMethod(cls, methodName, (Class<?>)null);
		if (m != null) {
			map.put(cls, m);
		}

		return (m);
	}



    /**
     * Returns true if there is a set method name forrow  
     * <code>row</code>. This is set in the constructor.
     */
    public boolean isCellEditable(int row, int column) { 
         return (setMethodNames != null && setMethodNames[row] != null);
    }


    /**
     * Sets the value to <code>value</code> for the object value
     * <code>value</code> in row <code>row</code>. This is done
     * by using the setter method name, and coercing the passed in
     * value to the specified type.
     */
    public void setValueAt (Object value, int row, int column) {

		if (valueObject == null) {
			return;
		}


		Method m = getSetMethod(row);
		if (m == null) {
			return;
		}

		try {
		    
			Class<?> param = m.getParameterTypes()[0];
		    if (!param.isInstance(value)) {

				/*
				 * Yes, we can use the value passed in directly,
				 * no coercision is necessary!
				 */
				if (value instanceof String && ((String)value).length() == 0) {

					/*
					 * Assume an empty string is null, this is
					 * probably bogus for here.
					 */
		    		value = null;

				} else {

					/*
					 * Have to attempt some sort of coercision.
					 * See if the expected parameter type has
					 * a constructor that takes a String.
					 */
		   			Constructor<?> cs = param.getConstructor(new Class[] { String.class });
				    if (cs != null) {

						value = cs.newInstance(new Object[] { value });

		    		} else {

						value = null;
				    }
				}
	    	}

			/*
			 * null either means it was an empty string, or there
			 * was no translation. Could potentially deal with these
			 * differently
			 */
	    	m.invoke(valueObject, new Object[] { value });

		} catch (Exception e) {
			System.err.println("exception: " + e);
		}
   	}


	protected final Method getSetMethod (int row) {

		String methodName = setMethodNames[row];
		if (methodName == null) {
			return (null);
		}

		HashMap<Class<?>, Method> map = setMethodMap[row];
		if (map == null) {
			map = new HashMap<Class<?>, Method>();
			setMethodMap[row] = map;
		}

		Class<?> cls = valueObject.getClass();
		Method m = map.get(cls);
		if (m != null) {
			return (m);
		}

		try {

			/*
			 * We have to search through all the methods since the
			 * types may not match up
			 */
		    Method[] methods = cls.getMethods();

		    for (int i = methods.length - 1; i >= 0; i--) {

				m = methods[i];
				Class<?> types[] = m.getParameterTypes();
				if (m.getName().equals(methodName) &&
					types != null && types.length == 1) {

					map.put(cls, m);
					return (m);
				}
			}

		} catch (Exception e) {
    		System.out.println("exception: " + e);
		}

		return (null);
	} 


	/**
	 * Sets the width of the given JTable column to the width of its
	 * widest contained value.
	 *
	 * @param table  the JTable that contains the column
	 */
	public void setSettingColumnWidth (JTable table) {
		setSettingColumnWidth(table, 4);
	}


	/**
	 * Sets the width of the given JTable column to the width of its
	 * widest contained value.
	 *
	 * @param table  the JTable that contains the column
	 */
	public void setSettingColumnWidth (JTable table, int pad) {

		int resizeMode = table.getAutoResizeMode();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableCellRenderer renderer = table.getCellRenderer(0, 0);
		int max = 0;
		int rows = getRowCount();
		for (int i = 0; i < rows; i++) {

			Object obj = getValueAt(i, 0);
			Component c = renderer.getTableCellRendererComponent(
				table, obj, true, true, i, 0);
			max = Math.max(max, c.getPreferredSize().width);
		}

		TableColumnModel tcm = table.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setPreferredWidth(max + pad);
		tc.setMaxWidth(max + pad);
		tc.setMinWidth(max + pad);
		tc.setWidth(max + pad);

		table.setAutoResizeMode(resizeMode);
	}
}
