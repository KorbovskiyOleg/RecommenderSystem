package basicalgorithms;


import java.util.Random;

import javax.swing.JFrame;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

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
		System.out.println(tree.toString());//выводим дерево 
		
		
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
		
		
		/*
		 * Классификация новых данных
		 */
		double[] vals = new double[data.numAttributes()];//Создаеv массив для хранения значений всех атрибутов
		vals[0] = 0.0; // hair {false, true} - заполнение характеристик животного
		vals[1] = 1.0; // feathers {false, true}
		vals[2] = 1.0; // eggs {false, true}
		vals[3] = 0.0; // milk {false, true}
		vals[4] = 1.0; // airborne {false, true}
		vals[5] = 0.0; // aquatic {false, true}
		vals[6] = 0.0; // predator {false, true}
		vals[7] = 0.0; // toothed {false, true}
		vals[8] = 1.0; // backbone {false, true}
		vals[9] = 1.0; // breathes {false, true}
		vals[10] = 0.0; // venomous {false, true}
		vals[11] = 0.0; // fins {false, true}
		vals[12] = 2.0; // legs INTEGER [0,9]
		vals[13] = 1.0; // tail {false, true}
		vals[14] = 0.0; // domestic {false, true}
		vals[15] = 0.0; // catsize {false, true}
		
		Instance myUnicorn = new DenseInstance(1.0, vals);//Создаем новый экземпляр животного с заданными характеристиками(вес экземпляра (обычно 1.0))
		
		myUnicorn.setDataset(data); // Связываем экземпляр с структурой dataset чтобы алгоритм понимал типы атрибутов (номинальные, числовые)

		double label = tree.classifyInstance(myUnicorn);// Дерево решений классифицирует животное - числовой код класса (например, 0 для "mammal", 1 для "bird")
		System.out.println(data.classAttribute().value((int) label));// Преобразуем числовой код в текстовое название класса

		
		/*
		 * Оценка качества модели
		 */
		
		Classifier cl = new J48();// Создаем объект дерева решений J48
		Evaluation eval_roc = new Evaluation(data);// Создаем объект для оценки качества модели
		eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});//Перекрестная проверка (Cross-Validation)
		System.out.println(eval_roc.toSummaryString());// Вывод общей статистики
		
		// Матрица ошибок
		double[][] confusionMatrix = eval_roc.confusionMatrix();//Возвращаем двумерный массив с матрицей ошибок
		System.out.println(eval_roc.toMatrixString());//Выводим матрицу ошибок в читаемом формате
		
		
		
		/*
		 * Построение ROC кривой
		 */
		

		ThresholdCurve tc = new ThresholdCurve();
		int classIndex = 1;
		Instances result = tc.getCurve(eval_roc.predictions(), classIndex);
		// plot curve
		ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
		vmc.setROCString("(Area under ROC = " + ThresholdCurve.getROCArea(result) + ")");
		vmc.setName(result.relationName());
		PlotData2D tempd = new PlotData2D(result);
		tempd.setPlotName(result.relationName());
		tempd.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[result.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		tempd.setConnectPoints(cp);

		// add plot
		vmc.addPlot(tempd);
		// display curve
		JFrame frameRoc = new javax.swing.JFrame("ROC Curve");
		frameRoc.setSize(800, 500);
		frameRoc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameRoc.getContentPane().add(vmc);
		frameRoc.setVisible(true);
		
		
		
		
		
	}

}
