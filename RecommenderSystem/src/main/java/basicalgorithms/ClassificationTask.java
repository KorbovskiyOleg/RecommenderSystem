package basicalgorithms;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ClassificationTask {

	public static void main(String[] args) throws Exception {

		/*
		 * Загружаем данные
		 */

		DataSource source = new DataSource("data/zoo.arff");
		Instances data = source.getDataSet();

		System.out.println(data.numInstances() + " instance loaded");
		System.out.println(data.numAttributes() + " attributes");
		System.out.println(data.toString());

		// Удаления шумных, избыточных или ненужных атрибутов перед обучением модели!
		
		// Удаляем первый атрибут - animal
		String[] opts = new String[] { "-R", "1" };// Настройка параметров: удалить атрибуты 1 и 3 (индексация с 1)
		Remove remove = new Remove();// Создание фильтра удаления атрибутов
		remove.setOptions(opts);
		remove.setInputFormat(data);// Указание формата входных данных
		data = Filter.useFilter(data, remove);// применяем фильтр к набору данных
		
		

	}
}
