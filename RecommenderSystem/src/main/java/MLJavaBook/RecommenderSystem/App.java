package MLJavaBook.RecommenderSystem;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            logger.info("Запуск приложения...");
            
            
            // Создаем простой вектор
            Vector v1 = new DenseVector(new double[]{1.0, 2.0, 3.0});
            Vector v2 = new DenseVector(new double[]{4.0, 5.0, 6.0});
            
            //Проверка Git
            
            // Вычисляем скалярное произведение
            double dotProduct = v1.dot(v2);
            
            // Логируем с разными уровнями
            logger.debug("Отладочная информация: векторы созданы");
            logger.info("Dot Product: {}", dotProduct);
            logger.info("Вектор v1: {}", v1);
            logger.info("Вектор v2: {}", v2);
            
            if (dotProduct != 32.0) {
                logger.warn("Возможна ошибка в расчетах. Ожидалось 32.0, получено {}", dotProduct);
            } else {
                logger.info("Расчет выполнен успешно!");
            }
            
         // Код Weka для работы с данными
            logger.info("Загрузка данных Weka...");
            
            // Загрузка данных
            DataSource source = new DataSource("src/main/java/MLJavaBook/RecommenderSystem/data.arff");
            Instances data = source.getDataSet();
            
            // Установка класса (последний атрибут)
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            
            // Создание и обучение классификатора
            J48 tree = new J48();
            tree.buildClassifier(data);
            
            logger.info("Weka успешно подключена!");
            logger.info("Количество экземпляров: {}", data.numInstances());
            logger.info("Количество атрибутов: {}", data.numAttributes());
            logger.info("Дерево решений:\n{}", tree.toString());
            
            logger.info("Приложение завершено.");
            
           
            
        } catch (Exception e) {
            logger.error("Критическая ошибка: ", e);
        }
    }
}
