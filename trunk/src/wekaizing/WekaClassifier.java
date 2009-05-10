package wekaizing;

import weka.classifiers.*;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.lazy.KStar;
import weka.classifiers.trees.J48;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;

/**
 * Class that handles the Weka classifier calls 
 * 
 * 
 * @author Barkin Aygun 
 *  
 *   
 * @see <a href="http://weka.sourceforge.net/doc/weka/classifiers/package-frame.html">weka.classifiers</a>
 * 
 */
public class WekaClassifier {
	public static final int J48					= 0;
	public static final int KSTAR				= 1;
	public static final int BAYESNET			= 2;
	public static final int LOGISTIC			= 3;
	public static final int VOTEDPERCEPTRON 	= 4;
	
	//Number of classes
	private int classNum;
	
	public Classifier myClassifier;
	
	/**
	 * Constructor for an empty classifier, classifier is not built right away since
	 * WekaData needs to be prepared and this way seems more suitable for a better 
	 * organized code
	 */
	public WekaClassifier() {
		myClassifier = new Logistic();
	}
	
	/**
	 * Constructor for an empty classifier with the specific type
	 * 
	 * @param classifier Type of classifier
	 */
	public WekaClassifier(int classifier) {
		switch (classifier) {
		case J48:
			myClassifier = new J48();
			break;
		case KSTAR:
			myClassifier = new KStar();
			break;
		case BAYESNET:
			myClassifier = new BayesNet();
			break;
		case LOGISTIC:
			myClassifier = new Logistic();
			break;
		case VOTEDPERCEPTRON:
			myClassifier = new VotedPerceptron();
			break;
		default:
			myClassifier = new KStar();
			break;
		}
	}
	
	/**
	 * Creates a classifier from a given Weka classifier class
	 * 
	 * @param classifier A class of type weka.classifiers.Classifier
	 */
	public WekaClassifier(weka.classifiers.Classifier classifier) {
		myClassifier = classifier;
	}
	
	/**
	 * Trains the classifier on the given data, this is the heart function of 
	 * every classifier, and should be called after data is input, or new data is in
	 * 
	 * @param inData WekaData to train on
	 * 
	 * @see WekaData
	 */
	public void Build(WekaData inData)
	{
		try {
			inData.setClassIndex(inData.myInstances.numAttributes() - 1);
			myClassifier.buildClassifier(inData.GetData());
			classNum = inData.myInstances.numClasses();
		} catch (Exception e) {
			System.out.println("Classifier build failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Given the test data, runs the trained classifier on it, assigns the result of
	 * classification to an integer array and returns it
	 * 
	 * @param testData WekaData that contains the test dataset
	 * @return Integer array of results 1-to-1 on the test dataset
	 */
	public int[] Classify(WekaData testData)
	{
		int[] results = new int[testData.myInstances.numInstances()];
		try {
		for (int i = 0; i < testData.myInstances.numInstances(); i++)
		{
				results[i] = (int) myClassifier.classifyInstance(testData.getInstance(i));
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Takes the given object array of an instance and classifies it,
	 * this function should be used for classifiers that return class number <br>
	 * <br>
	 * If the function is used on a multi class classifier that implements 
	 * distributionForInstance method instead of classifyInstance, it will be 
	 * invalid. 
	 * <br><br>
	 * To make sure there are no mistakes, best thing to do is check the classifier
	 * doc's. Later on a smarter way of handling classify might be implemented.
	 * 
	 * 
	 * @param data Object Array of the instance
	 * @return index of the class the instance is classified into
	 * 
	 * @see ClassifyProbs
	 */
	public int Classify(Object[] data)
	{
		double[] retClass;
		double maxClassProb = 0.0;
		int maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			if (data[j].getClass().equals(Double.class))
			{
				tempInst.setValue(j,Double.parseDouble(data[j].toString()));	
			}
			else if (data[j].getClass().equals(Integer.class))
			{
				tempInst.setValue(j,Integer.parseInt(data[j].toString()));
			}
			else if (data[j].getClass().equals(String.class))
			{
				tempInst.setValue(j, data[j].toString());
			}
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				return (int)(myClassifier.classifyInstance(tempInst));
			} catch (Exception e1){
				System.out.println("Classification failed");
				e1.printStackTrace();
				return 0;
				}
			}
		for (int i = 0; i < retClass.length; i++) {
			if (retClass[i] > maxClassProb) {
				maxClass = i;
				maxClassProb = retClass[i];
			}
		}
		return maxClass;
	}
	
	/**
	 * Override for Classify using integer data
	 * 
	 * @param data integer array of a data instance
	 * @return
	 */
	public int Classify(int[] data)
	{
		double[] retClass;
		double maxClassProb = 0.0;
		int maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				return (int)(myClassifier.classifyInstance(tempInst));
			} catch (Exception e1){
				System.out.println("Classification failed");
				e1.printStackTrace();
				return 0;
				}
			}
		for (int i = 0; i < retClass.length; i++) {
			if (retClass[i] > maxClassProb) {
				maxClass = i;
				maxClassProb = retClass[i];
			}
		}
		return maxClass;
	}
	
	/**
	 * Override of classify using double data
	 * 
	 * @param data double array of a data instance
	 * @return
	 */
	public int Classify(double[] data)
	{
		double[] retClass;
		double maxClassProb = 0.0;
		int maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				return (int)(myClassifier.classifyInstance(tempInst));
			} catch (Exception e1){
				System.out.println("Classification failed");
				e1.printStackTrace();
				return 0;
				}
			}
		for (int i = 0; i < retClass.length; i++) {
			if (retClass[i] > maxClassProb) {
				maxClass = i;
				maxClassProb = retClass[i];
			}
		}
		return maxClass;
	}
	
	/**
	 * Override of classify using float data
	 * 
	 * @param data float array of a data instance
	 * @return
	 */
	public int Classify(float[] data)
	{
		double[] retClass;
		double maxClassProb = 0.0;
		int maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				return (int)(myClassifier.classifyInstance(tempInst));
			} catch (Exception e1){
				System.out.println("Classification failed");
				e1.printStackTrace();
				return 0;
				}
			}
		for (int i = 0; i < retClass.length; i++) {
			if (retClass[i] > maxClassProb) {
				maxClass = i;
				maxClassProb = retClass[i];
			}
		}
		return maxClass;
	}
	
	/**
	 * Given the object array of an instance values, will return the 
	 * probability of every class. If the classifier does not support this method
	 * will return an array of doubles with only the classified class' value 1.0 
	 * and rest 0.0
	 * 
	 * @param data Object Array of the instance
	 * @return index of the class the instance is classified into
	 * 
	 * @see Classify
	 */
	public double[] ClassifyProbs(Object[] data)
	{
		double[] retClass;
		double maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			if (data[j].getClass().equals(Double.class))
			{
				tempInst.setValue(j,Double.parseDouble(data[j].toString()));	
			}
			else if (data[j].getClass().equals(Integer.class))
			{
				tempInst.setValue(j,Integer.parseInt(data[j].toString()));
			}
			else if (data[j].getClass().equals(String.class))
			{
				tempInst.setValue(j, data[j].toString());
			}
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				maxClass = myClassifier.classifyInstance(tempInst);
				retClass = new double[this.classNum];
				for (int i = 0; i < this.classNum; i++)
				{
					retClass[i] = 0;
				}
				retClass[(int)maxClass] = 1.0;
			} catch (Exception e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new double[] {1,0};
				}
			}
		return retClass;
	}
	/**
	 * Override of ClassifyProbs using integer data
	 * 
	 * @param data integer array of a data instance
	 * @return
	 */
	public double[] ClassifyProbs(int[] data)
	{
		double[] retClass;
		double maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				maxClass = myClassifier.classifyInstance(tempInst);
				retClass = new double[this.classNum];
				for (int i = 0; i < this.classNum; i++)
				{
					retClass[i] = 0;
				}
				retClass[(int)maxClass] = 1.0;
			} catch (Exception e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new double[] {1,0};
				}
			}
		return retClass;
	}

	/**
	 * Override of ClassifyProbs using double data
	 * 
	 * @param data double array of a data instance
	 * @return
	 */
	public double[] ClassifyProbs(double[] data)
	{
		double[] retClass;
		double maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				maxClass = myClassifier.classifyInstance(tempInst);
				retClass = new double[this.classNum];
				for (int i = 0; i < this.classNum; i++)
				{
					retClass[i] = 0;
				}
				retClass[(int)maxClass] = 1.0;
			} catch (Exception e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new double[] {1,0};
				}
			}
		return retClass;
	}

	/**
	 * Override of ClassifyProbs using float data
	 * 
	 * @param data float array of a data instance
	 * @return
	 */
	public double[] ClassifyProbs(float[] data)
	{
		double[] retClass;
		double maxClass = 0;
		Instance tempInst = new Instance(data.length);
		for (int j = 0; j < data.length; j++) {
			tempInst.setValue(j,data[j]);
		}
		try {
			retClass = myClassifier.distributionForInstance(tempInst);
		} catch (Exception e) {
			try {
				maxClass = myClassifier.classifyInstance(tempInst);
				retClass = new double[this.classNum];
				for (int i = 0; i < this.classNum; i++)
				{
					retClass[i] = 0;
				}
				retClass[(int)maxClass] = 1.0;
			} catch (Exception e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new double[] {1,0};
				}
			}
		return retClass;
	}

	/**
	 * Calls the toString function of the classifier, returning an explanation
	 * 
	 * @return String representation of the classifier
	 */
	public String toString()
	{
		return myClassifier.toString();
	}
	
	
}
