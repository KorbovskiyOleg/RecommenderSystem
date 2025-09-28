package basicalgorithms;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
//import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.util.Random;

public class RegressionTask {
	
	public static void main(String[] args) throws Exception {
		
		
		/*
		 * Загрузка данных
		 */
		CSVLoader loader = new CSVLoader();
		loader.setFieldSeparator(",");
		loader.setSource(new File("data/ENB2012_data.csv"));
		Instances data = loader.getDataSet();
		 //System.out.println(data);
		 
		 /*
			 * Построение регрессионой моедли где пердсказываем только один целевой атрибут Y1(тепловая нагрузка)
			 */
			
			data.setClassIndex(data.numAttributes() - 2);// установка индекса класса на Y1 (нагрузка при нагреве)
			// удаление Y2
			Remove remove = new Remove();
			remove.setOptions(new String[] { "-R", data.numAttributes() + "" });
			remove.setInputFormat(data);
			data = Filter.useFilter(data, remove);
			
			// создаем модель регрессии
			LinearRegression model = new LinearRegression();
			model.buildClassifier(data);
			System.out.println(model);
			
			
			// 10-кратная cross-validation
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(model, data, 10, new Random(1), new String[] {});
			System.out.println(eval.toSummaryString());
			double coef[] = model.coefficients();
			System.out.println();
			
			
	}

}
