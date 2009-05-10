package wekaizing;

import weka.clusterers.AbstractClusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;

/**
 * Class that handles the Weka clusterer calls 
 * 
 * For now, the clusterer type is chosen using a String, name of classifier. 
 * If possible, a better and situation smart way will be implemented that takes the type
 * of data into account.
 * 
 * @author Barkin Aygun 
 *  
 *   
 * @see <a href="WEKA LIBRARIES CLUSTERERS">weka.clusterers</a>
 * 
 * @usage Library
 */
public class WekaClusterer {
	public static final int FARTHEST_FIT	= 0;
	public static final int EM				= 1;
	public static final int COBWEB			= 2;
	public static final int KMEANS			= 3;
	
	public int numClusters;
	
	public AbstractClusterer myClusterer;
	
	/**
	 * The constructor needed for initialization
	 * 
	 */
	public WekaClusterer() {
		myClusterer = new EM();
	}
	
	/**
	 * The constructor with a specific type
	 * @param type Clusterer type
	 */
	public WekaClusterer(int type) {
		switch (type) {
		case FARTHEST_FIT:
			myClusterer = new FarthestFirst();
			break;
		case EM:
			myClusterer = new EM();
			break;
		case COBWEB:
			myClusterer = new Cobweb();
			break;
		case KMEANS:
			myClusterer = new SimpleKMeans();
			break;
		default:
			myClusterer = new EM();
			break;
		}
	}
	
	/**
	 * Trains the clusterer on the given data, this is the heart function of 
	 * every clusterer, and should be called after data is input, or new data is in
	 * 
	 * @param inData WekaData to train on
	 * 
	 * @see WekaData
	 */
	/*
	public void Build(WekaData inData) {
		try {
			inData.myInstances.setClassIndex(-1);
			myClusterer.buildClusterer(inData.GetData());
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * Given a set of data, clusters them all and returns the cluster number they belong to
	 * 
	 * @param data WekaData to classify
	 * @return
	 */
	public int[] clusterData(WekaData data) {
		int dataLength = data.myInstances.numInstances();
		int[] clusters = new int[dataLength];
		Instance tempInst;
		try {
			data.myInstances.setClassIndex(-1);
			myClusterer.buildClusterer(data.GetData());
			numClusters = myClusterer.numberOfClusters();
		} catch (Exception e1) {
			System.out.println("Cluster build unsuccessful");
			System.out.println(e1.getMessage());
		}
		for (int i = 0 ; i < dataLength; i++) {
			try {
				tempInst = data.getInstance(i);
				clusters[i] = myClusterer.clusterInstance(tempInst);
			} catch (Exception e) {
				System.out.println("Clustering an instance unsuccessful");
				System.out.println(e.getLocalizedMessage());
			}
		}
		
		return clusters;
		
	}
	
	/**
	 * Given a set of data, clusters them all and returns the cluster number they belong to
	 * 
	 * @param data WekaData to classify
	 * @return
	 */
	public int[] clusterData(WekaData data, int clusterCount) {
		int dataLength = data.myInstances.numInstances();
		int[] clusters = new int[dataLength];
		Instance tempInst;
		try {
			data.myInstances.setClassIndex(-1);
			((EM)myClusterer).setNumClusters(clusterCount);
			myClusterer.buildClusterer(data.GetData());
			numClusters = myClusterer.numberOfClusters();
		} catch (Exception e1) {
			System.out.println("Cluster build unsuccessful");
			System.out.println(e1.getMessage());
		}
		for (int i = 0 ; i < dataLength; i++) {
			try {
				tempInst = data.getInstance(i);
				clusters[i] = myClusterer.clusterInstance(tempInst);
			} catch (Exception e) {
				System.out.println("Clustering an instance unsuccessful");
				System.out.println(e.getMessage());
			}
		}
		
		return clusters;
		
	}
	
	
	/**
	 * For advanced users, allows custom options for the clusterer in Weka
	 * Refer to weka doc and specific clusterer doc for details
	 * 
	 * @param options String array of options
	 */
	public void setOptions(String[] options) {
		try {
			((EM)myClusterer).setOptions(options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
