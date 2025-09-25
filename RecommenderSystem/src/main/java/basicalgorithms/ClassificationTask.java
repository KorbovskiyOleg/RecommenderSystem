package basicalgorithms;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class ClassificationTask {
    
    public static void main(String[] args) throws Exception { 
        
    	//Загружаем данные
        DataSource source = new DataSource("data/zoo.arff");
        Instances data = source.getDataSet();
        
        System.out.println(data.numInstances() + " instance loaded");
        
     // Удаляем первsq атрибут - animal
     		String[] opts = new String[] { "-R", "1" };
     		Remove remove = new Remove();
     		remove.setOptions(opts);
     		remove.setInputFormat(data);
     		data = Filter.useFilter(data, remove);
     		
     		
    }
}
