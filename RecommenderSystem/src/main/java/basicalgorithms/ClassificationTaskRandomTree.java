/*package basicalgorithms;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomTree;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.swing.*;
import java.util.Random;

public class ClassificationTaskRandomTree {

    public static void main(String[] args) throws Exception{



        /*
         * Загружаем данные
         

        ConverterUtils.DataSource source = new ConverterUtils.DataSource("data/zoo.arff");
        Instances data = source.getDataSet();

        System.out.println(data.numInstances() + " instance loaded");
        System.out.println(data.numAttributes() + " attributes");

        // Устанавливаем индекс класса (последний атрибут) ДО фильтрации
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }
        System.out.println("Class attribute: " + data.classAttribute().name());


        // Удаления шумных, избыточных или ненужных атрибутов перед обучением модели!

        // Удаляем первый атрибут - animal
        String[] opts = new String[] { "-R", "1" };// Настройка параметров: удалить атрибуты 1 и 3 (индексация с 1)
        Remove remove = new Remove();// Создание фильтра удаления атрибутов
        remove.setOptions(opts);
        remove.setInputFormat(data);// Указание формата входных данных
        data = Filter.useFilter(data, remove);// применяем фильтр к набору данных

        // После фильтрации нужно ПЕРЕУСТАНОВИТЬ индекс класса!
        // Теперь атрибутов стало на 1 меньше, поэтому классовый атрибут будет последним
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("After filtering - Class attribute: " + data.classAttribute().name());
        System.out.println("Remaining attributes: " + data.numAttributes());


        /*
         * Построение случайного дерева
        

        // настройка параметров
        String[] options = new String[] {
                "-K", "0",     // Количество случайных признаков для рассмотрения (0 = log2(число_признаков) + 1)
                "-M", "1",     // Минимальное количество объектов в листе
                "-S", "1",     // Seed для генератора случайных чисел
                "-depth", "0"  // Максимальная глубина дерева (0 = без ограничений)
        };

        RandomTree randomTree = new RandomTree();
        randomTree.setOptions(options);
        randomTree.buildClassifier(data);

        System.out.println("\n=== Random Tree Model ===");
        System.out.println(randomTree.toString());

        /*
         * Визуализация
        
        try {
        TreeVisualizer tv = new TreeVisualizer(null, randomTree.graph(),
                new PlaceNode2());
        JFrame frame = new javax.swing.JFrame("RandomTree Visualizer");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(tv);
        frame.setVisible(true);
        tv.fitToScreen();} catch (Exception e) {
            throw new RuntimeException(e);
        }

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

        double label = randomTree.classifyInstance(myUnicorn);// Дерево решений классифицирует животное - числовой код класса (например, 0 для "mammal", 1 для "bird")
        System.out.println(data.classAttribute().value((int) label));// Преобразуем числовой код в текстовое название класса

        /*
         * Оценка качества модели
         

        Classifier cl = new RandomTree();// Создаем объект дерева решений 
        Evaluation eval_roc = new Evaluation(data);// Создаем объект для оценки качества модели
        eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});//Перекрестная проверка (Cross-Validation)
        System.out.println(eval_roc.toSummaryString());// Вывод общей статистики

        // Матрица ошибок
       // double[][] confusionMatrix = eval_roc.confusionMatrix();//Возвращаем двумерный массив с матрицей ошибок
        System.out.println(eval_roc.toMatrixString());//Выводим матрицу ошибок в читаемом формате

    }
}*/

package basicalgorithms;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomTree;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.swing.*;
import java.io.*;
import java.util.Random;

public class ClassificationTaskRandomTree {

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
        File logFile = new File("randomtree_analysis_log.txt");
        PrintStream fileStream = new PrintStream(new FileOutputStream(logFile));
        
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;
        
        // Создаем TeePrintStream для дублирования вывода
        TeePrintStream teeStream = new TeePrintStream(fileStream, originalOut);
        
        // Устанавливаем наш кастомный поток как System.out
        System.setOut(teeStream);
        
        // Добавляем заголовок с временем выполнения
        System.out.println("=".repeat(80));
        System.out.println("АНАЛИЗ ДАННЫХ С ИСПОЛЬЗОВАНИЕМ RANDOM TREE");
        System.out.println("Время начала: " + new java.util.Date());
        System.out.println("Файл лога: " + logFile.getAbsolutePath());
        System.out.println("=".repeat(80));

        try {
            /*
             * Загружаем данные
             */
            System.out.println("\n=== ЗАГРУЗКА ДАННЫХ ===");
            ConverterUtils.DataSource source = new ConverterUtils.DataSource("data/zoo.arff");
            Instances data = source.getDataSet();

            System.out.println(data.numInstances() + " instances loaded");
            System.out.println(data.numAttributes() + " attributes");

            // Устанавливаем индекс класса (последний атрибут) ДО фильтрации
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            System.out.println("Class attribute: " + data.classAttribute().name());

            /*
             * Предобработка данных
             */
            System.out.println("\n=== ПРЕДОБРАБОТКА ДАННЫХ ===");
            // Удаляем первый атрибут - animal
            String[] opts = new String[] { "-R", "1" };
            Remove remove = new Remove();
            remove.setOptions(opts);
            remove.setInputFormat(data);
            data = Filter.useFilter(data, remove);

            // После фильтрации нужно ПЕРЕУСТАНОВИТЬ индекс класса!
            data.setClassIndex(data.numAttributes() - 1);
            System.out.println("After filtering - Class attribute: " + data.classAttribute().name());
            System.out.println("Remaining attributes: " + data.numAttributes());

            // Информация о классе
            System.out.println("Class distribution:");
            for (int i = 0; i < data.numClasses(); i++) {
                int count = data.attributeStats(data.classIndex()).nominalCounts[i];
                System.out.printf("  %s: %d instances (%.1f%%)\n", 
                    data.classAttribute().value(i), count, 
                    (double)count / data.numInstances() * 100);
            }

            /*
             * Построение случайного дерева
             */
            System.out.println("\n=== ПОСТРОЕНИЕ МОДЕЛИ RANDOM TREE ===");
            
            // Настройка параметров
            String[] options = new String[] {
                "-K", "0",     // Количество случайных признаков (0 = log2(число_признаков) + 1)
                "-M", "1",     // Минимальное количество объектов в листе
                "-S", "1",     // Seed для генератора случайных чисел
                "-depth", "0"  // Максимальная глубина дерева (0 = без ограничений)
            };

            System.out.println("Параметры модели:");
            System.out.println("  K (количество признаков): 0 (auto)");
            System.out.println("  M (мин. объектов в листе): 1");
            System.out.println("  S (seed): 1");
            System.out.println("  Depth (макс. глубина): 0 (неограничено)");

            RandomTree randomTree = new RandomTree();
            randomTree.setOptions(options);
            randomTree.buildClassifier(data);

            System.out.println("\n=== СТРУКТУРА СЛУЧАЙНОГО ДЕРЕВА ===");
            System.out.println(randomTree.toString());

            // Альтернативная статистика дерева (так как measureNumLeaves() не доступен)
            System.out.println("\nИНФОРМАЦИЯ О МОДЕЛИ:");
            System.out.println("Модель успешно построена");
            System.out.println("Количество атрибутов: " + data.numAttributes());
            System.out.println("Количество классов: " + data.numClasses());

            /*
             * Визуализация
             */
            System.out.println("\n=== ВИЗУАЛИЗАЦИЯ ===");
            System.out.println("Открытие окна визуализации дерева...");
            try {
                TreeVisualizer tv = new TreeVisualizer(null, randomTree.graph(), new PlaceNode2());
                JFrame frame = new JFrame("RandomTree Visualizer");
                frame.setSize(1200, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(tv);
                frame.setVisible(true);
                tv.fitToScreen();
                System.out.println("Окно визуализации успешно открыто");
            } catch (Exception e) {
                System.out.println("Ошибка при визуализации: " + e.getMessage());
                System.out.println("Визуализация может быть недоступна для больших деревьев");
            }

            /*
             * Классификация новых данных
             */
            System.out.println("\n=== КЛАССИФИКАЦИЯ НОВЫХ ДАННЫХ ===");
            double[] vals = new double[data.numAttributes()];
            vals[0] = 0.0;  // hair {false, true}
            vals[1] = 1.0;  // feathers {false, true}
            vals[2] = 1.0;  // eggs {false, true}
            vals[3] = 0.0;  // milk {false, true}
            vals[4] = 1.0;  // airborne {false, true}
            vals[5] = 0.0;  // aquatic {false, true}
            vals[6] = 0.0;  // predator {false, true}
            vals[7] = 0.0;  // toothed {false, true}
            vals[8] = 1.0;  // backbone {false, true}
            vals[9] = 1.0;  // breathes {false, true}
            vals[10] = 0.0; // venomous {false, true}
            vals[11] = 0.0; // fins {false, true}
            vals[12] = 2.0; // legs INTEGER [0,9]
            vals[13] = 1.0; // tail {false, true}
            vals[14] = 0.0; // domestic {false, true}
            vals[15] = 0.0; // catsize {false, true}

            Instance myUnicorn = new DenseInstance(1.0, vals);
            myUnicorn.setDataset(data);

            double label = randomTree.classifyInstance(myUnicorn);
            String predictedClass = data.classAttribute().value((int) label);
            System.out.println("Предсказанный класс: " + predictedClass);

            // Вероятности классификации
            double[] distribution = randomTree.distributionForInstance(myUnicorn);
            System.out.println("Вероятности по классам:");
            for (int i = 0; i < distribution.length; i++) {
                System.out.printf("  %s: %.3f\n", data.classAttribute().value(i), distribution[i]);
            }

            /*
             * Оценка качества модели
             */
            System.out.println("\n=== ОЦЕНКА КАЧЕСТВА МОДЕЛИ ===");
            
            // Используем ту же конфигурацию для оценки
            Classifier cl = new RandomTree();
            //cl.setOptions(options);
            
            Evaluation eval_roc = new Evaluation(data);
            eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});
            
            System.out.println("ОБЩАЯ СТАТИСТИКА:");
            System.out.println(eval_roc.toSummaryString());
            
            System.out.println("МАТРИЦА ОШИБОК:");
            System.out.println(eval_roc.toMatrixString());
            
            System.out.println("ДЕТАЛЬНАЯ СТАТИСТИКА ПО КЛАССАМ:");
            System.out.println(eval_roc.toClassDetailsString());
            
            // Дополнительные метрики
            System.out.println("ДОПОЛНИТЕЛЬНЫЕ МЕТРИКИ:");
            System.out.printf("Точность (Accuracy): %.2f%%\n", eval_roc.pctCorrect());
            System.out.printf("Каппа статистика: %.3f\n", eval_roc.kappa());
            System.out.printf("Средняя абсолютная ошибка: %.3f\n", eval_roc.meanAbsoluteError());
            System.out.printf("Корень из среднеквадратичной ошибки: %.3f\n", eval_roc.rootMeanSquaredError());
            
            // Информация о перекрестной проверке
            System.out.println("\nПЕРЕКРЕСТНАЯ ПРОВЕРКА (10-fold):");
            System.out.println("Количество folds: 10");
            System.out.println("Seed для воспроизводимости: 1");

            // Сравнение с базовыми метриками
            System.out.println("\nСРАВНЕНИЕ С БАЗОВЫМИ МЕТРИКАМИ:");
            double baselineAccuracy = getBaselineAccuracy(data);
            System.out.printf("Точность базового классификатора (всегда majority class): %.2f%%\n", baselineAccuracy);
            System.out.printf("Улучшение над базовым классификатором: %.2f%%\n", 
                eval_roc.pctCorrect() - baselineAccuracy);

            // Анализ матрицы ошибок
            analyzeConfusionMatrix(eval_roc, data);

            /*
             * ЗАВЕРШЕНИЕ
             */
            System.out.println("\n" + "=".repeat(80));
            System.out.println("АНАЛИЗ С RANDOM TREE ЗАВЕРШЕН УСПЕШНО!");
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
            
            System.out.println("\nЛоги RandomTree сохранены в файл: " + logFile.getAbsolutePath());
            System.out.println("Размер файла: " + logFile.length() + " байт");
        }
    }
    
    /**
     * Вычисляет точность базового классификатора (всегда предсказывает majority class)
     */
    private static double getBaselineAccuracy(Instances data) {
        int[] classCounts = data.attributeStats(data.classIndex()).nominalCounts;
        int maxCount = 0;
        for (int count : classCounts) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return (double) maxCount / data.numInstances() * 100;
    }
    
    /**
     * Анализирует матрицу ошибок и выводит дополнительную информацию
     */
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
    }
}
