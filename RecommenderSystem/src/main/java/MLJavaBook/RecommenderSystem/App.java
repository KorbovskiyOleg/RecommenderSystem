package MLJavaBook.RecommenderSystem;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            logger.info("Запуск приложения...");
            
            
            // Создаем простой вектор
            Vector v1 = new DenseVector(new double[]{1.0, 2.0, 3.0});
            Vector v2 = new DenseVector(new double[]{4.0, 5.0, 6.0});

            
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
            
            
            
            logger.info("Приложение завершено.");
            
        } catch (Exception e) {
            logger.error("Критическая ошибка: ", e);
        }
    }
}
