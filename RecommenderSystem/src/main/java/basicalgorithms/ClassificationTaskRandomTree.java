package basicalgorithms;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
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
         */

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
        */

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
        */
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

        myUnicorn.setDataset(data); // Связываем экземпляр со структурой dataset чтобы алгоритм понимал типы атрибутов (номинальные, числовые)

        double label = randomTree.classifyInstance(myUnicorn);// Дерево решений классифицирует животное - числовой код класса (например, 0 для "mammal", 1 для "bird")
        System.out.println(data.classAttribute().value((int) label));// Преобразуем числовой код в текстовое название класса

        /*
         * Оценка качества модели
         */

        Classifier cl = new RandomTree();// Создаем объект дерева решений J48
        Evaluation eval_roc = new Evaluation(data);// Создаем объект для оценки качества модели
        eval_roc.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});//Перекрестная проверка (Cross-Validation)
        System.out.println(eval_roc.toSummaryString());// Вывод общей статистики

        // Матрица ошибок
       // double[][] confusionMatrix = eval_roc.confusionMatrix();//Возвращаем двумерный массив с матрицей ошибок
        System.out.println(eval_roc.toMatrixString());//Выводим матрицу ошибок в читаемом формате

    }
}
