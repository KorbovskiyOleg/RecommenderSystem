/*package basicalgorithms;


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
		 
		
		String[] options = new String[1];//Создаем массив строк длиной 1 для хранения параметров настройки
		options[0] = "-U";// Задаем параметр -U (Unpruned - без отсечения)
		
		J48 tree = new J48();//Создаем объект дерева решений (аналог C4.5) - пустое дерево
		tree.setOptions(options);//Передаем параметры настройки алгоритму J48(алгоритм теперь знает, что нужно строить дерево без pruning)
		tree.buildClassifier(data);//Обучаем дерево решений на данных (data). Алгоритм анализирует данные,Строит дерево решений по алгоритму C4.5
		System.out.println(tree.toString());//выводим дерево 
		
		
		/*
		 * Визуализация
		 
		
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
		
		myUnicorn.setDataset(data); // Связываем экземпляр со структурой dataset чтобы алгоритм понимал типы атрибутов (номинальные, числовые)

		double label = tree.classifyInstance(myUnicorn);// Дерево решений классифицирует животное - числовой код класса (например, 0 для "mammal", 1 для "bird")
		System.out.println(data.classAttribute().value((int) label));// Преобразуем числовой код в текстовое название класса

		
		/*
		 * Оценка качества модели
		 
		
		Classifier cl = new J48();// Создаем объект дерева решений J48
		Evaluation eval_roc = new Evaluation(data);// Создаем объект для оценки качества модели
		eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});//Перекрестная проверка (Cross-Validation)
		System.out.println(eval_roc.toSummaryString());// Вывод общей статистики
		
		// Матрица ошибок
		//double[][] confusionMatrix = eval_roc.confusionMatrix();//Возвращаем двумерный массив с матрицей ошибок
		System.out.println(eval_roc.toMatrixString());//Выводим матрицу ошибок в читаемом формате
		
		
		
		/*
		 * Построение ROC кривой
		 
		

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

}*/

package basicalgorithms;

import java.io.*;
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

    // Кастомный PrintStream для дублирования вывода в файл и консоль
    private static class TeePrintStream extends PrintStream {
        private final PrintStream fileStream;
        private final PrintStream consoleStream;

        public TeePrintStream(PrintStream fileStream, PrintStream consoleStream) {
            super(fileStream);
            this.fileStream = fileStream;
            this.consoleStream = consoleStream;
        }

        @Override
        public void print(boolean b) {
            fileStream.print(b);
            consoleStream.print(b);
        }

        @Override
        public void print(char c) {
            fileStream.print(c);
            consoleStream.print(c);
        }

        @Override
        public void print(int i) {
            fileStream.print(i);
            consoleStream.print(i);
        }

        @Override
        public void print(long l) {
            fileStream.print(l);
            consoleStream.print(l);
        }

        @Override
        public void print(float f) {
            fileStream.print(f);
            consoleStream.print(f);
        }

        @Override
        public void print(double d) {
            fileStream.print(d);
            consoleStream.print(d);
        }

        @Override
        public void print(char[] s) {
            fileStream.print(s);
            consoleStream.print(s);
        }

        @Override
        public void print(String s) {
            fileStream.print(s);
            consoleStream.print(s);
        }

        @Override
        public void print(Object obj) {
            fileStream.print(obj);
            consoleStream.print(obj);
        }

        @Override
        public void println() {
            fileStream.println();
            consoleStream.println();
        }

        @Override
        public void println(boolean x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(char x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(int x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(long x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(float x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(double x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(char[] x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(String x) {
            fileStream.println(x);
            consoleStream.println(x);
        }

        @Override
        public void println(Object x) {
            fileStream.println(x);
            consoleStream.println(x);
        }
    }

    public static void main(String[] args) throws Exception {
        
        // Создаем файл для сохранения логов
        File logFile = new File("j48_analysis_log(clean).txt");
        PrintStream fileStream = new PrintStream(new FileOutputStream(logFile));
        
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;
        
        // Создаем TeePrintStream для дублирования вывода
        TeePrintStream teeStream = new TeePrintStream(fileStream, originalOut);
        
        // Устанавливаем наш кастомный поток как System.out
        System.setOut(teeStream);
        
        // Добавляем заголовок с временем выполнения
        System.out.println("=".repeat(80));
        System.out.println("АНАЛИЗ ДАННЫХ С ИСПОЛЬЗОВАНИЕМ J48");
        System.out.println("Время начала: " + new java.util.Date());
        System.out.println("Файл лога: " + logFile.getAbsolutePath());
        System.out.println("=".repeat(80));

        try {
            /*
             * Загружаем данные
             */
            System.out.println("\n=== ЗАГРУЗКА ДАННЫХ ===");
            DataSource source = new DataSource("data/zoo_clean.arff");
            Instances data = source.getDataSet();

            System.out.println(data.numInstances() + " instances loaded");
            System.out.println(data.numAttributes() + " attributes");
            
            // Устанавливаем индекс класса
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            System.out.println("Class attribute: " + data.classAttribute().name());

            // Удаление шумных, избыточных или ненужных атрибутов перед обучением модели!
            System.out.println("\n=== ПРЕДОБРАБОТКА ДАННЫХ ===");
            // Удаляем первый атрибут - animal
            String[] opts = new String[] { "-R", "1" };// Настройка параметров: удалить атрибут 1
            Remove remove = new Remove();// Создание фильтра удаления атрибутов
            remove.setOptions(opts);
            remove.setInputFormat(data);// Указание формата входных данных
            data = Filter.useFilter(data, remove);// применяем фильтр к набору данных
            System.out.println("После фильтрации - атрибутов: " + data.numAttributes());
            System.out.println(data);
            
            // Переустанавливаем индекс класса после фильтрации
            data.setClassIndex(data.numAttributes() - 1);
            System.out.println("Class attribute after filtering: " + data.classAttribute().name());

            /*
             * Выбор признаков (наиболее релевантных для обучения)
             */
            System.out.println("\n=== ВЫБОР ПРИЗНАКОВ ===");
            // выбор атрибутов на основе информационного выигрыша (Information Gain). 
            AttributeSelection attSelect = new AttributeSelection(); 
            InfoGainAttributeEval eval = new InfoGainAttributeEval();
            Ranker search = new Ranker();
            attSelect.setEvaluator(eval);
            attSelect.setSearch(search);
            attSelect.SelectAttributes(data);
            
            // получение результатов
            int[] indices = attSelect.selectedAttributes();
            System.out.println("Selected attributes: " + Utils.arrayToString(indices));

            /*
             * Построение дерева решений J48 без pruning (отсечения)
             */
            System.out.println("\n=== ПОСТРОЕНИЕ МОДЕЛИ J48 ===");
            String[] options = new String[1];
            options[0] = "-U";// Задаем параметр -U (Unpruned - без отсечения)
            
            J48 tree = new J48();
            tree.setOptions(options);
            tree.buildClassifier(data);
            System.out.println("Дерево решений построено успешно!");
            System.out.println("\n=== СТРУКТУРА ДЕРЕВА ===");
            System.out.println(tree.toString());

            /*
             * Визуализация
             */
            System.out.println("\n=== ВИЗУАЛИЗАЦИЯ ===");
            System.out.println("Открытие окна визуализации дерева...");
            TreeVisualizer tv = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
            JFrame frame = new javax.swing.JFrame("Tree Visualizer");
            frame.setSize(800, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(tv);
            frame.setVisible(true);
            tv.fitToScreen();

            /*
             * Классификация новых данных
             */
            System.out.println("\n=== КЛАССИФИКАЦИЯ НОВЫХ ДАННЫХ ===");
            double[] vals = new double[data.numAttributes()];
            vals[0] = 0.0; // hair {false, true}
            vals[1] = 1.0; // feathers {false, true}
            vals[2] = 1.0; // eggs {false, true}
            //vals[3] = 0.0; // milk {false, true}
            vals[2] = 1.0; // airborne {false, true}
            vals[3] = 0.0; // aquatic {false, true}
            vals[4] = 0.0; // predator {false, true}
            vals[5] = 0.0; // toothed {false, true}
            vals[6] = 1.0; // backbone {false, true}
            vals[7] = 1.0; // breathes {false, true}
            vals[8] = 0.0; // venomous {false, true}
            vals[9] = 0.0; // fins {false, true}
            vals[10] = 2.0; // legs INTEGER [0,9]
            vals[11] = 1.0; // tail {false, true}
            vals[12] = 0.0; // domestic {false, true}
            vals[13] = 0.0; // catsize {false, true}
            
            Instance myUnicorn = new DenseInstance(1.0, vals);
            myUnicorn.setDataset(data);

            double label = tree.classifyInstance(myUnicorn);
            String predictedClass = data.classAttribute().value((int) label);
            System.out.println("Предсказанный класс: " + predictedClass);
            
            // Дополнительная информация о вероятностях
            double[] distribution = tree.distributionForInstance(myUnicorn);
            System.out.println("Вероятности по классам:");
            for (int i = 0; i < distribution.length; i++) {
                System.out.printf("  %s: %.3f\n", data.classAttribute().value(i), distribution[i]);
            }

            /*
             * Оценка качества модели
             */
            System.out.println("\n=== ОЦЕНКА КАЧЕСТВА МОДЕЛИ ===");
            Classifier cl = new J48();
            Evaluation eval_roc = new Evaluation(data);
            eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});
            
            System.out.println("ОБЩАЯ СТАТИСТИКА:");
            System.out.println(eval_roc.toSummaryString());
            
            System.out.println("МАТРИЦА ОШИБОК:");
            System.out.println(eval_roc.toMatrixString());
            
         // Анализ матрицы ошибок
            analyzeConfusionMatrix(eval_roc,data);
         
            
            System.out.println("ДЕТАЛЬНАЯ СТАТИСТИКА ПО КЛАССАМ:");
            System.out.println(eval_roc.toClassDetailsString());
            
            // Дополнительные метрики
            System.out.println("ДОПОЛНИТЕЛЬНЫЕ МЕТРИКИ:");
            System.out.printf("Точность (Accuracy): %.2f%%\n", eval_roc.pctCorrect());
            System.out.printf("Каппа статистика: %.3f\n", eval_roc.kappa());
            System.out.printf("Средняя абсолютная ошибка: %.3f\n", eval_roc.meanAbsoluteError());

            /*
             * Построение ROC кривой
             */
            System.out.println("\n=== ROC-КРИВАЯ ===");
            System.out.println("Открытие окна ROC-кривой...");
            ThresholdCurve tc = new ThresholdCurve();
            int classIndex = 1;
            Instances result = tc.getCurve(eval_roc.predictions(), classIndex);
            
            ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
            vmc.setROCString("(Area under ROC = " + ThresholdCurve.getROCArea(result) + ")");
            vmc.setName(result.relationName());
            PlotData2D tempd = new PlotData2D(result);
            tempd.setPlotName(result.relationName());
            tempd.addInstanceNumberAttribute();
            
            boolean[] cp = new boolean[result.numInstances()];
            for (int n = 1; n < cp.length; n++)
                cp[n] = true;
            tempd.setConnectPoints(cp);

            vmc.addPlot(tempd);
            JFrame frameRoc = new javax.swing.JFrame("ROC Curve");
            frameRoc.setSize(800, 500);
            frameRoc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameRoc.getContentPane().add(vmc);
            frameRoc.setVisible(true);
            
            System.out.println("Площадь под ROC-кривой: " + ThresholdCurve.getROCArea(result));

            /*
             * ЗАВЕРШЕНИЕ
             */
            System.out.println("\n" + "=".repeat(80));
            System.out.println("АНАЛИЗ ЗАВЕРШЕН УСПЕШНО!");
            System.out.println("Все результаты сохранены в файл: " + logFile.getAbsolutePath());
            System.out.println("Время завершения: " + new java.util.Date());
            System.out.println("=".repeat(80));

        } catch (Exception e) {
            System.out.println("ПРОИЗОШЛА ОШИБКА:");
            e.printStackTrace();
        } finally {
            // Восстанавливаем оригинальный System.out
            System.setOut(originalOut);
            fileStream.close();
            
            System.out.println("\nЛоги сохранены в файл: " + logFile.getAbsolutePath());
            System.out.println("Размер файла: " + logFile.length() + " байт");
        }
        
    }
    
    private static void analyzeConfusionMatrix(Evaluation eval, Instances data) {
        System.out.println("\nАНАЛИЗ МАТРИЦЫ ОШИБОК:");
        double[][] matrix = eval.confusionMatrix();
        
        int totalCorrect = 0;
        int totalInstances = data.numInstances();
        
        for (int i = 0; i < matrix.length; i++) {
            totalCorrect += matrix[i][i];
        }
        
        System.out.printf("Правильно классифицировано: %d/%d (%.1f%%)\n", 
            totalCorrect, totalInstances, (double)totalCorrect / totalInstances * 100);
        
        // Анализ по классам
        for (int i = 0; i < matrix.length; i++) {
            int classCorrect = (int) matrix[i][i];
            int classTotal = 0;
            for (int j = 0; j < matrix.length; j++) {
                classTotal += matrix[i][j];
            }
            if (classTotal > 0) {
                double recall = (double) classCorrect / classTotal * 100;
                System.out.printf("  %s: %d/%d (%.1f%% recall)\n", 
                    data.classAttribute().value(i), classCorrect, classTotal, recall);
            }
        }
        
        // Дополнительная статистика для RandomForest
        System.out.println("\nОЖИДАЕМЫЕ ПРЕИМУЩЕСТВА RANDOM FOREST:");
        System.out.println("• Более сбалансированные ошибки между классами");
        System.out.println("• Лучшая обобщающая способность");
        System.out.println("• Устойчивость к переобучению");
    }
}
