package basicalgorithms;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClassificationTask {
	
	public static void main(String[] args) throws Exception{ 
		
		/*
		 * Load the data
		 */
		DataSource source = new DataSource("data/zoo.arff");
		Instances data = source.getDataSet();
		System.out.println(data.numInstances() + " instances loaded.");
	}
}
