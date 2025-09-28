package basicalgorithms;

import weka.classifiers.trees.M5P;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.Evaluation;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class M5PRegressionTask {

    // Кастомный PrintStream для дублирования вывода в файл и консоль
    private static class TeePrintStream extends PrintStream {
        private final PrintStream second;
        
        public TeePrintStream(PrintStream first, PrintStream second) {
            super(first);
            this.second = second;
        }
        
        @Override
        public void write(byte[] buf, int off, int len) {
            super.write(buf, off, len);
            second.write(buf, off, len);
        }
        
        @Override
        public void print(boolean b) {
            super.print(b);
            second.print(b);
        }
        
        @Override
        public void print(char c) {
            super.print(c);
            second.print(c);
        }
        
        @Override
        public void print(int i) {
            super.print(i);
            second.print(i);
        }
        
        @Override
        public void print(long l) {
            super.print(l);
            second.print(l);
        }
        
        @Override
        public void print(float f) {
            super.print(f);
            second.print(f);
        }
        
        @Override
        public void print(double d) {
            super.print(d);
            second.print(d);
        }
        
        @Override
        public void print(char[] s) {
            super.print(s);
            second.print(s);
        }
        
        @Override
        public void print(String s) {
            super.print(s);
            second.print(s);
        }
        
        @Override
        public void print(Object obj) {
            super.print(obj);
            second.print(obj);
        }
        
        @Override
        public void println() {
            super.println();
            second.println();
        }
        
        @Override
        public void println(boolean x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(char x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(int x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(long x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(float x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(double x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(char[] x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(String x) {
            super.println(x);
            second.println(x);
        }
        
        @Override
        public void println(Object x) {
            super.println(x);
            second.println(x);
        }
    }

    private static PrintStream originalOut;
    private static PrintStream fileStream;

    public static void main(String[] args) throws Exception {
        
        // Настройка логирования в файл
        setupLogging();
        
        try {
            // Загрузка данных
            DataSource source = new DataSource("data/ENB2012_data.csv");
            Instances data = source.getDataSet();
            
            // Подготовка данных - установка Y1 как целевой переменной
            data.setClassIndex(data.numAttributes() - 2);
            Remove remove = new Remove();
            remove.setOptions(new String[] { "-R", data.numAttributes() + "" });
            remove.setInputFormat(data);
            data = Filter.useFilter(data, remove);
            
            // Создание и обучение модели M5P
            M5P model = createM5PModel();
            model.buildClassifier(data);
            
            // Оценка модели с улучшенным выводом
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(model, data, 10, new Random(1));
            
            // ПРОФЕССИОНАЛЬНЫЙ ВЫВОД РЕЗУЛЬТАТОВ
            printDetailedEvaluation(eval, model, data);
            
            // Дополнительный анализ специфичный для M5P
            printM5PModelInterpretation(eval, model, data);
            
            // Сохранение модели в файл
            saveModel(model, "m5p_regression_model.model");
            
        } finally {
            // Восстановление стандартного вывода и закрытие файла
            cleanupLogging();
        }
    }
    
    private static M5P createM5PModel() throws Exception {
        M5P m5p = new M5P();
        // Настройка параметров M5P
        m5p.setOptions(weka.core.Utils.splitOptions(
            "-M 4.0 -R"  // Минимальное количество instances в листьях = 4, не строить регрессионные модели в листьях
        ));
        return m5p;
    }
    
    private static void setupLogging() throws FileNotFoundException {
        // Создаем уникальное имя файла с временной меткой
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String logFileName = "m5p_regression_analysis_" + timestamp + ".txt";
        File logFile = new File(logFileName);
        
        // Создаем поток для записи в файл
        fileStream = new PrintStream(new FileOutputStream(logFile));
        
        // Сохраняем оригинальный System.out
        originalOut = System.out;
        
        // Создаем TeePrintStream для дублирования вывода
        TeePrintStream teeStream = new TeePrintStream(fileStream, originalOut);
        
        // Устанавливаем наш кастомный поток как System.out
        System.setOut(teeStream);
        
        // Выводим информацию о начале анализа
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         РЕГРЕССИОННЫЙ АНАЛИЗ - M5P ДЕРЕВО РЕГРЕССИИ        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Файл лога: " + logFile.getAbsolutePath() + " ║");
        System.out.println("║ Время начала: " + new Date() + " ║");
        System.out.println("║ Алгоритм: M5P - Дерево регрессии с линейными моделями       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void cleanupLogging() {
        if (fileStream != null) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    АНАЛИЗ ЗАВЕРШЕН                          ║");
            System.out.println("║          Результаты сохранены в лог-файл                   ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            fileStream.close();
        }
        // Восстанавливаем оригинальный System.out
        System.setOut(originalOut);
        
        // Сообщение в консоль о завершении
        System.out.println("✅ Анализ M5P завершен. Результаты сохранены в файл.");
    }
    
    private static void printDetailedEvaluation(Evaluation eval, M5P model, Instances data) throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║               РЕЗУЛЬТАТЫ РЕГРЕССИОННОГО АНАЛИЗА (M5P)       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n📊 ОСНОВНЫЕ МЕТРИКИ КАЧЕСТВА:");
        System.out.println("┌──────────────────────────────────────┬─────────────┐");
        System.out.printf("│ Коэффициент корреляции              │ %11.4f │\n", eval.correlationCoefficient());
        System.out.printf("│ Средняя абсолютная ошибка (MAE)     │ %11.4f │\n", eval.meanAbsoluteError());
        System.out.printf("│ Корень из MSE (RMSE)                │ %11.4f │\n", eval.rootMeanSquaredError());
        System.out.printf("│ Относительная абсолютная ошибка     │ %10.2f%% │\n", eval.relativeAbsoluteError());
        System.out.printf("│ Относительная квадратичная ошибка   │ %10.2f%% │\n", eval.rootRelativeSquaredError());
        System.out.printf("│ Количество наблюдений               │ %11.0f │\n", eval.numInstances());
        System.out.println("└──────────────────────────────────────┴─────────────┘");
        
        
        
        // Интерпретация корреляции
        System.out.println("\n🔍 ИНТЕРПРЕТАЦИЯ КОРРЕЛЯЦИИ:");
        double correlation = eval.correlationCoefficient();
        if (correlation >= 0.9) {
            System.out.println("   ✅ ОТЛИЧНО: Сильная линейная зависимость (≥ 0.9)");
        } else if (correlation >= 0.7) {
            System.out.println("   👍 ХОРОШО: Умеренная линейная зависимость (0.7-0.9)");
        } else if (correlation >= 0.5) {
            System.out.println("   📊 УДОВЛЕТВОРИТЕЛЬНО: Слабая зависимость (0.5-0.7)");
        } else {
            System.out.println("   ⚠️  СЛАБО: Незначительная линейная зависимость (< 0.5)");
        }
        
        // Анализ ошибок
        System.out.println("\n📈 АНАЛИЗ ТОЧНОСТИ:");
        double mae = eval.meanAbsoluteError();
        double meanTarget = calculateMeanTargetValue(data);
        double errorPercentage = (mae / meanTarget) * 100;
        
        System.out.printf("   Средняя тепловая нагрузка: %.2f единиц\n", meanTarget);
        System.out.printf("   Ошибка составляет: %.1f%% от среднего значения\n", errorPercentage);
        
        if (errorPercentage < 10) {
            System.out.println("   ✅ ОТЛИЧНАЯ точность (< 10% ошибки)");
        } else if (errorPercentage < 20) {
            System.out.println("   👍 ХОРОШАЯ точность (10-20% ошибки)");
        } else if (errorPercentage < 30) {
            System.out.println("   📊 УДОВЛЕТВОРИТЕЛЬНАЯ точность (20-30% ошибки)");
        } else {
            System.out.println("   ⚠️  НИЗКАЯ точность (> 30% ошибки)");
        }
    }
    
    private static void printM5PModelInterpretation(Evaluation eval, M5P model, Instances data) {
        System.out.println("\n🔬 ИНТЕРПРЕТАЦИЯ МОДЕЛИ M5P:");
        
        // Вывод структуры дерева
        System.out.println("\n🌿 СТРУКТУРА ДЕРЕВА:");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println(model.toString());
        System.out.println("══════════════════════════════════════════════════════");
        
        // Анализ правил дерева
        System.out.println("\n📋 ПРАВИЛА ПРИНЯТИЯ РЕШЕНИЙ:");
        String modelString = model.toString();
        String[] lines = modelString.split("\n");
        
        int ruleCount = 0;
        for (String line : lines) {
            if (line.contains("LM") && line.contains("Number")) {
                System.out.println("   📍 " + line.trim());
                ruleCount++;
            } else if (line.trim().startsWith("X") && line.contains("<") || line.contains(">")) {
                System.out.println("   🌳 " + line.trim());
            }
        }
        
        System.out.println("   Всего правил/листьев: " + ruleCount);
        
        // Практические выводы
        System.out.println("\n💡 ПРАКТИЧЕСКИЕ ВЫВОДЫ ДЛЯ АРХИТЕКТОРОВ:");
        System.out.println("   • M5P создает интерпретируемые правила 'если-то'");
        System.out.println("   • Модель автоматически определяет важные взаимодействия признаков");
        System.out.println("   • Разные правила применяются для разных типов зданий");
        System.out.println("   • Можно точно определить при каких условиях тепловая нагрузка высокая/низкая");
        
        // Сравнение с линейной регрессией
        System.out.println("\n🆚 СРАВНЕНИЕ С ЛИНЕЙНОЙ РЕГРЕССИЕЙ:");
        System.out.println("   ✅ M5P преимущества:");
        System.out.println("      • Учитывает нелинейные зависимости");
        System.out.println("      • Автоматически определяет взаимодействия признаков");
        System.out.println("      • Более интерпретируемые правила");
        System.out.println("      • Устойчивее к выбросам");
        
        System.out.println("   ⚠️  M5P ограничения:");
        System.out.println("      • Может быть сложнее для понимания");
        System.out.println("      • Требует больше вычислительных ресурсов");
        System.out.println("      • Может переобучаться на маленьких датасетах");
        
        // Рекомендации по использованию
        System.out.println("\n🎯 РЕКОМЕНДАЦИИ ПО ИСПОЛЬЗОВАНИЮ M5P:");
        System.out.println("   • Используйте когда важна интерпретируемость");
        System.out.println("   • Хорошо для данных со сложными взаимодействиями");
        System.out.println("   • Настройте minNumInstances для контроля переобучения");
        System.out.println("   • Рассмотрите ансамбли с M5P для большей точности");
    }
    
    private static double calculateMeanTargetValue(Instances data) {
        double sum = 0;
        int classIndex = data.classIndex();
        for (int i = 0; i < data.numInstances(); i++) {
            sum += data.instance(i).value(classIndex);
        }
        return sum / data.numInstances();
    }
    
    private static void saveModel(M5P model, String filename) {
        try {
            weka.core.SerializationHelper.write(filename, model);
            System.out.println("\n💾 МОДЕЛЬ M5P СОХРАНЕНА:");
            System.out.println("   Файл: " + filename);
            System.out.println("   Модель готова для использования в production");
            System.out.println("   Модель включает дерево решений с линейными моделями в листьях");
        } catch (Exception e) {
            System.out.println("\n⚠️  Не удалось сохранить модель M5P: " + e.getMessage());
        }
    }
}
