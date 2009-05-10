package wekaizing;

import java.io.*;

import weka.core.*;

/**
 * Class that handles datasets, created to make it as easy as possible to
 * create own data from scratch. Since Weka Instances class requires the
 * attributes to be set before anything, this class acts as a wrapper
 * both for Instances, and for use by classifiers to make things easier.
 * 
 * @author Barkin Aygun 
 *  
 *   
 * @see <a href="WEKA INSTANCES">weka.core.instances</a>
 * @see <a href="WEKA ATTRIBUTE">weka.core.attribute</a>
 * 
 * @usage Library
 */
public class WekaData {
	FastVector myAttributes;
	public Instances myInstances;
	int myDatasize;
	
	/**
	 * Ctor with nothing
	 */
	public WekaData() {
		
	}
	
	/**
	 * Ctor to create an empty data set with given capacities,
	 * while these are not hard limits, setting them accurate will speed up things
	 * 
	 * @param attrCapacity Number of attributes
	 * @param dataCapacity Number of data instances
	 */
	public WekaData(int attrCapacity, int dataCapacity) {
		myAttributes = new FastVector(attrCapacity);
		myDatasize = dataCapacity;
	}
	
	/**
	 * Ctor to read from an arff file
	 * @param filename
	 */
	public WekaData(String filename) {
		//TODO Add myAttributes parsing from ARFF file
		Reader reader;
		try {
			reader = new FileReader(filename);
			myInstances = new Instances(reader);
			myInstances.setClassIndex(myInstances.numAttributes()-1);
		} catch (FileNotFoundException e) {
			//TODO Beautify these messages
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
	
	/*
	 * METHODS
	 */
	/**
	 * Private sanity check to make sure instances are created well,
	 * raises specific errors
	 * 
	 * @param len Dataset size
	 */
	private void checkInstances(int len) {
		if (myInstances == null)
		{
			myInstances = new Instances("myData", myAttributes, len);
		}
	}
	/**
	 * Initializes the attribute vector if necessary and adds a numeric attribute
	 * 
	 * @param attrName Name of the attribute
	 * @return Attribute created
	 */
	public Attribute AddAttribute(String attrName) {
		if (myAttributes == null)
		{
			myAttributes = new FastVector();
		}
		Attribute tempAttr = new Attribute(attrName);
		myAttributes.addElement(tempAttr);
		return tempAttr;
	}
	
	/**
	 * Initalizes the attribute vector if necessary and adds a string attribute
	 * 
	 * @param attrName Name of the attribute
	 * @return Attribute created
	 */
	public Attribute AddStringAttribute(String attrName) {
		if (myAttributes == null)
		{
			myAttributes = new FastVector();
		}
		Attribute tempAttr = new Attribute(attrName, (FastVector) null);
		myAttributes.addElement(tempAttr);
		return tempAttr;
	}
	
	/**
	 * Adds a nominal attribute to the attributes set, nominal attributes are 
	 * ones with defined set of countable values
	 * 
	 * @param attrName Name of the attribute
	 * @param nominal_values Set of values for the attribute
	 * @return Attribute created
	 */
	public Attribute AddAttribute(String attrName, Object[] nominal_values) {
		if (myAttributes == null)
		{
			myAttributes = new FastVector();
		}
		FastVector temp_nominal_values = new FastVector(nominal_values.length);
		for (int i = 0; i < nominal_values.length; i++) {
			temp_nominal_values.addElement(nominal_values[i].toString());
		}
		Attribute tempAttr = new Attribute(attrName,  temp_nominal_values);
		myAttributes.addElement(tempAttr);
		return tempAttr;
	}
	
	/**
	 * Use this function to enter big amount of data, takes in a 2D array of objects
	 * where each row is a single instance and parses the values to their classes.
	 * 
	 * @param data 2D array of Objects for instances x values
	 */
	public void InsertData(Object[][] data) {
		checkInstances(data.length);
		for (int i = 0; i < data.length; i++) {
			Instance tempInst = new Instance(data[0].length);
			tempInst.setDataset(myInstances);
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j].getClass().equals(Float.class))
				{
					tempInst.setValue(j,Float.parseFloat(data[i][j].toString()));	
				}
				if (data[i][j].getClass().equals(Double.class))
				{
					tempInst.setValue(j,Double.parseDouble(data[i][j].toString()));	
				}
				else if (data[i][j].getClass().equals(Integer.class))
				{
					tempInst.setValue(j,Integer.parseInt(data[i][j].toString()));
				}
				else if (data[i][j].getClass().equals(String.class))
				{
					tempInst.setValue(j, data[i][j].toString());
				}
			}
			myInstances.add(tempInst);
		}
	}
	
	/**
	 * Use this function to enter a single data, takes in an array of objects
	 * and parses the values to their classes.
	 * 
	 * @param data array of Objects for values
	 */
	public void InsertData(Object[] data) {
		Object[][] dataset = {data};
		InsertData(dataset);
	}
	
	/**
	 * Data insertion for integer arrays
	 * 
	 * @param data array of integers for values
	 */
	public void InsertData(int[] data) {
		checkInstances(1);
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
			}
		myInstances.add(tempInst);
	}
	
	
	/**
	 * Data insertion for float arrays
	 * 
	 * @param data array of floats for values
	 */
	public void InsertData(float[] data) {
		checkInstances(1);
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
			}
		myInstances.add(tempInst);
	}
	
	/**
	 * Returns the Instances (for internal use)
	 * 
	 * @return weka.core.instances of the WekaData
	 */
	public Instances GetData()
	{
		return myInstances;	
	}
	
	/**
	 * Prints the data into a double array, the behavior is undefined if
	 * there are string data. 
	 * 
	 * @return 2D array of instances x values with doubles
	 */
	public double[][] GetDataIntoArray()
	{
		double[][] outArray = new double[myInstances.numInstances()][myInstances.numAttributes()];
		for (int i = 0; i < myInstances.numInstances(); i++)
		{
			for (int j = 0; j < myInstances.numAttributes(); j++)
			{
				outArray[i][j] = myInstances.instance(i).value(j);
			}
		}
		return outArray;
	}
	
	/**
	 * Prints the dataset into an arff file
	 * 
	 * @param filename Address of the file
	 */
	public void PrintData(String filename) {
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			fw.write(myInstances.toString());
			fw.close();
			System.out.println("Printed to " + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/**
	 * Returns the indexed instance
	 * @param index Index of the instance
	 * @return Instance with the given index
	 */
	public Instance getInstance(int index) {
		return myInstances.instance(index);
	}
	
	/**
	 * Sets the indexed attribute to the class attribute
	 * @param index Index of the attribute
	 */
	public void setClassIndex(int index) {
		myInstances.setClassIndex(index);
	}
	
	/**
	 * A function that takes an int array of cluster indices and adds them as an
	 * attribute, RemoveClustersFromData() should be called after handling this method
	 * 
	 * @param clusters Integer array of cluster indices in the same order with the dataset
	 */
	public void AddClustersAsClass(int[] clusters) {
		Attribute tempCluster = new Attribute("WekaTempCluster");
		myInstances.insertAttributeAt(tempCluster, myAttributes.size());
		for (int i = 0; i < clusters.length; i++) {
			myInstances.instance(i).setValue(myAttributes.size(), clusters[i]);
		}
	}
	
	/**
	 * A helper function that removes the last attribute, so make sure it is
	 * called after AddClustersAsClass(int[] clusters) 
	 */
	public void RemoveClustersFromData() {
		System.out.println("Removing the last attribute, make sure it is the temp cluster class");
		myInstances.deleteAttributeAt(myInstances.numAttributes()-1);
	}
	

}
