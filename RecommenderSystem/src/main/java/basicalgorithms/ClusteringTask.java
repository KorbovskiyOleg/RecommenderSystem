package basicalgorithms;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import java.util.Random;

public class ClusteringTask {

    public static void main(String args[]) throws Exception {
        
       
        DataSource source = new DataSource("data/bank-data.arff");
        Instances data = source.getDataSet();
        
        System.out.println("✅ Данные загружены через DataSource:");
        System.out.println("   Экземпляров: " + data.numInstances());
        System.out.println("   Атрибутов: " + data.numAttributes());
        
        performClustering(data);
    }
    
    private static void performClustering(Instances data) throws Exception {
        EM model = new EM();
        model.buildClusterer(data);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ КЛАСТЕРИЗАЦИИ ===");
        System.out.println(model);
        
        double logLikelihood = ClusterEvaluation.crossValidateModel(model, data, 10, new Random(1));
        System.out.println("Log Likelihood: " + logLikelihood);
    }
}