package basicalgorithms;

import javax.swing.JFrame;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class ClassificationTask {

	public static void main(String[] args) throws Exception {

		
		/*
		 * Загружаем данные
		 */

		DataSource source = new DataSource("data/zoo.arff");
		Instances data = source.getDataSet();

		System.out.println(data.numInstances() + " instance loaded");
		System.out.println(data.numAttributes() + " attributes");
		//System.out.println(data.toString());

		// Удаления шумных, избыточных или ненужных атрибутов перед обучением модели!
		
		// Удаляем первый атрибут - animal
		String[] opts = new String[] { "-R", "1" };// Настройка параметров: удалить атрибуты 1 и 3 (индексация с 1)
		Remove remove = new Remove();// Создание фильтра удаления атрибутов
		remove.setOptions(opts);
		remove.setInputFormat(data);// Указание формата входных данных
		data = Filter.useFilter(data, remove);// применяем фильтр к набору данных
		
		
		/*
		 * Выбор признаков(наиболее релевантных для обучения)
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
		
		
		/*
		 * Построение дерева решений J48 без pruning (отсечения)
		 */
		
		String[] options = new String[1];//Создаем массив строк длиной 1 для хранения параметров настройки
		options[0] = "-U";// Задаем параметр -U (Unpruned - без отсечения)
		
		J48 tree = new J48();//Создаем объект дерева решений (аналог C4.5) - пустое дерево
		tree.setOptions(options);//Передаем параметры настройки алгоритму J48(алгоритм теперь знает, что нужно строить дерево без pruning)
		tree.buildClassifier(data);//Обучаем дерево решений на данных (data). Алгоритм анализирует данные,Строит дерево решений по алгоритму C4.5
		System.out.println(tree);//выводим дерево 
		
		/*
		 * Визуализация
		 */
		
		TreeVisualizer tv = new TreeVisualizer(null, tree.graph(),
				new PlaceNode2());
		JFrame frame = new javax.swing.JFrame("Tree Visualizer");
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tv);
		frame.setVisible(true);
		tv.fitToScreen();

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
