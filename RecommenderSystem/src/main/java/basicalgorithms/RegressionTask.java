package basicalgorithms;

import weka.core.Instances;
//import weka.core.Instances;
import weka.core.converters.CSVLoader;
import java.io.File;

public class RegressionTask {
	
	public static void main(String[] args) throws Exception {
		
		
		/*
		 * Load data
		 */
		CSVLoader loader = new CSVLoader();
		loader.setFieldSeparator(",");
		loader.setSource(new File("data/ENB2012_data.csv"));
		Instances data = loader.getDataSet();

		 System.out.println(data);
	}

}
