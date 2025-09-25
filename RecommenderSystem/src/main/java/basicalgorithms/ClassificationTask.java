package basicalgorithms;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.Utils;
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
		
		/*
		 * Выбор признаков
		 */
		
		// выбор атрибутов на основе информационного выигрыша (Information Gain). 
		
		AttributeSelection attSelect = new AttributeSelection(); // Создаем основной объект, который будет управлять процессом выбора атрибутов
		InfoGainAttributeEval eval = new InfoGainAttributeEval();// Создаем оценщика, который вычисляет информационный выигрыш для каждого атрибута
		Ranker search = new Ranker();// Создаем объект, который будет ранжировать атрибуты по их полезности
		attSelect.setEvaluator(eval);// Говорит оценщику, какого эксперта использовать для оценки
		attSelect.setSearch(search);// Указывает метод ранжирования атрибутов
		attSelect.SelectAttributes(data); // Запускает процесс оценки и ранжирования атрибутов на ваших данных
		
		// получение результатов
		int[] indices = attSelect.selectedAttributes(); // Возвращаем массив индексов отобранных атрибутов(Индексы начинаются с 0 (в отличие от фильтра Remove!))
		System.out.println("Selected attributes: "+Utils.arrayToString(indices));
	}
}
