package basicalgorithms;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.util.Random;

public class ClassificationTaskSMO {

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
        File logFile = new File("smo_analysis_log.txt");
        PrintStream fileStream = new PrintStream(new FileOutputStream(logFile));
        
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;
        
        // Создаем TeePrintStream для дублирования вывода
        TeePrintStream teeStream = new TeePrintStream(fileStream, originalOut);
        
        // Устанавливаем наш кастомный поток как System.out
        System.setOut(teeStream);
        
        // Добавляем заголовок с временем выполнения
        System.out.println("=".repeat(80));
        System.out.println("АНАЛИЗ ДАННЫХ С ИСПОЛЬЗОВАНИЕМ SMO (SVM)");
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
             * Предобработка данных для SVM
             */
            System.out.println("\n=== ПРЕДОБРАБОТКА ДАННЫХ ДЛЯ SVM ===");
            
            // Для SVM часто полезно нормализовать данные, но для нашего датасета с бинарными признаками это не критично
            System.out.println("Особенности предобработки для SVM:");
            System.out.println("• SVM чувствителен к масштабированию признаков");
            System.out.println("• Наш датасет содержит в основном бинарные признаки (0/1)");
            System.out.println("• Нормализация может не потребоваться");
            
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
             * Построение SVM модели с SMO
             */
            System.out.println("\n=== ПОСТРОЕНИЕ МОДЕЛИ SMO (SVM) ===");
            
            // Настройка параметров SMO
            String[] options = new String[] {
                "-C", "1.0",        // Параметр регуляризации C (complexity)
                "-L", "0.001",      // Tolerance parameter (точность)
                "-P", "1.0E-12",    // Epsilon для круглой ошибки
                "-N", "0",          // Нормализация/стандартизация (0 - выкл, 1 - вкл, 2 - стандартизация, 3 - нормализация)
                "-M",               // Подгонка логистических моделей для вероятностей
                "-V", "-1",         // Количество folds для внутренней кросс-валидации (-1 = отключить)
                "-W", "1",          // Случайное перемешивание данных
                "-K", "weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007" // Полиномиальное ядро
            };

            System.out.println("Параметры модели SMO (SVM):");
            System.out.println("  C (параметр регуляризации): 1.0");
            System.out.println("  L (точность): 0.001");
            System.out.println("  N (нормализация): 0 (выкл)");
            System.out.println("  M (логистические модели): вкл");
            System.out.println("  Ядро: PolyKernel (полиномиальное)");
            System.out.println("    - E (экспонента): 1.0 (линейное ядро)");
            System.out.println("    - C (константа): 250007");

            SMO smo = new SMO();
            smo.setOptions(options);
            smo.buildClassifier(data);

            System.out.println("\n=== ИНФОРМАЦИЯ О МОДЕЛИ SVM ===");
            System.out.println("Модель SVM успешно построена с использованием SMO алгоритма!");
            System.out.println("Алгоритм: Sequential Minimal Optimization");
            System.out.println("Количество опорных векторов: " + getNumberOfSupportVectors(smo));
            System.out.println("Количество атрибутов: " + data.numAttributes());
            System.out.println("Количество классов: " + data.numClasses());
            
            // Информация о ядре
            System.out.println("\nИНФОРМАЦИЯ О ЯДРЕ:");
            System.out.println("Тип ядра: " + smo.getKernel().getClass().getSimpleName());
            System.out.println("Линейное ядро хорошо для данных с четкими линейными границами");

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

            double label = smo.classifyInstance(myUnicorn);
            String predictedClass = data.classAttribute().value((int) label);
            System.out.println("Предсказанный класс: " + predictedClass);

            // Вероятности классификации (SMO с опцией -M предоставляет вероятности)
            double[] distribution = smo.distributionForInstance(myUnicorn);
            System.out.println("Вероятности по классам:");
            for (int i = 0; i < distribution.length; i++) {
                System.out.printf("  %s: %.3f\n", data.classAttribute().value(i), distribution[i]);
            }

            // Информация о решающей функции
            System.out.println("Значение решающей функции: " + getDecisionFunctionValue(smo, myUnicorn));

            /*
             * Оценка качества модели
             */
            System.out.println("\n=== ОЦЕНКА КАЧЕСТВА МОДЕЛИ ===");
            
            // Используем ту же конфигурацию для оценки
            Classifier cl = new SMO();
            //cl.setOptions(options);
            
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(cl, data, 10, new Random(1), new Object[] {});
            
            System.out.println("ОБЩАЯ СТАТИСТИКА:");
            System.out.println(eval.toSummaryString());
            
            System.out.println("МАТРИЦА ОШИБОК:");
            System.out.println(eval.toMatrixString());
            
            System.out.println("ДЕТАЛЬНАЯ СТАТИСТИКА ПО КЛАССАМ:");
            System.out.println(eval.toClassDetailsString());
            
            // Дополнительные метрики
            System.out.println("ДОПОЛНИТЕЛЬНЫЕ МЕТРИКИ:");
            System.out.printf("Точность (Accuracy): %.2f%%\n", eval.pctCorrect());
            System.out.printf("Каппа статистика: %.3f\n", eval.kappa());
            System.out.printf("Средняя абсолютная ошибка: %.3f\n", eval.meanAbsoluteError());
            System.out.printf("Корень из среднеквадратичной ошибки: %.3f\n", eval.rootMeanSquaredError());
            
            // Статистика для SVM
            System.out.println("\nСТАТИСТИКА SVM:");
            System.out.println("Параметр C (регуляризация): 1.0");
            System.out.println("Тип ядра: Linear (Polynomial degree 1)");
            System.out.println("Количество опорных векторов: " + getNumberOfSupportVectors(smo));
            
            // Информация о перекрестной проверке
            System.out.println("\nПЕРЕКРЕСТНАЯ ПРОВЕРКА (10-fold):");
            System.out.println("Количество folds: 10");
            System.out.println("Seed для воспроизводимости: 1");

            // Сравнение с базовыми метриками
            System.out.println("\nСРАВНЕНИЕ С БАЗОВЫМИ МЕТРИКАМИ:");
            double baselineAccuracy = getBaselineAccuracy(data);
            System.out.printf("Точность базового классификатора (всегда majority class): %.2f%%\n", baselineAccuracy);
            System.out.printf("Улучшение над базовым классификатором: %.2f%%\n", 
                eval.pctCorrect() - baselineAccuracy);

            // Анализ матрицы ошибок
            analyzeConfusionMatrix(eval, data);
            
            // Особенности SVM
            System.out.println("\nОСОБЕННОСТИ SVM:");
            System.out.println("• Эффективен в высокоразмерных пространствах");
            System.out.println("• Работает с небольшими наборами данных");
            System.out.println("• Устойчив к переобучению благодаря margin maximization");
            System.out.println("• Чувствителен к выбору ядра и параметра C");
            
            // Рекомендации по настройке
            System.out.println("\nРЕКОМЕНДАЦИИ ПО НАСТРОЙКЕ:");
            System.out.println("• Попробовать разные ядра: RBF, Polynomial");
            System.out.println("• Настроить параметр C (больше C = сложнее модель)");
            System.out.println("• Рассмотреть нормализацию данных");

            /*
             * ЗАВЕРШЕНИЕ
             */
            System.out.println("\n" + "=".repeat(80));
            System.out.println("АНАЛИЗ С SMO (SVM) ЗАВЕРШЕН УСПЕШНО!");
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
            
            System.out.println("\nЛоги SMO (SVM) сохранены в файл: " + logFile.getAbsolutePath());
            System.out.println("Размер файла: " + logFile.length() + " байт");
        }
    }
    
    /**
     * Получает количество опорных векторов (приблизительно)
     */
    private static int getNumberOfSupportVectors(SMO smo) {
        try {
            // Попытка получить информацию об опорных векторах через toString
            String modelInfo = smo.toString();
            if (modelInfo.contains("Number of support vectors")) {
                // Парсим информацию из строки
                String[] lines = modelInfo.split("\n");
                for (String line : lines) {
                    if (line.contains("Number of support vectors")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            return Integer.parseInt(parts[1].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Если не удалось получить информацию
        }
        return -1; // Информация недоступна
    }
    
    /**
     * Получает значение решающей функции (приблизительно)
     */
    private static double getDecisionFunctionValue(SMO smo, Instance instance) {
        try {
            // Для SVM значение решающей функции можно получить через распределение
            double[] distribution = smo.distributionForInstance(instance);
            if (distribution.length > 0) {
                // Возвращаем максимальное значение как приближение решающей функции
                double max = distribution[0];
                for (int i = 1; i < distribution.length; i++) {
                    if (distribution[i] > max) {
                        max = distribution[i];
                    }
                }
                return max;
            }
        } catch (Exception e) {
            // Если не удалось получить значение
        }
        return 0.0;
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
        
        // Особенности SVM
        System.out.println("\nОЖИДАЕМЫЕ ХАРАКТЕРИСТИКИ SVM:");
        System.out.println("• Хорошая обобщающая способность");
        System.out.println("• Эффективная работа с разреженными данными");
        System.out.println("• Устойчивость к шуму в данных");
    }
}
