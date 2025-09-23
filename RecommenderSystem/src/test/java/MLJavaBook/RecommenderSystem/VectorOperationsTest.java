package MLJavaBook.RecommenderSystem;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorOperationsTest {

    @Test
    void testVectorAddition() {
        Vector v1 = new DenseVector(new double[]{1.0, 2.0, 3.0});
        Vector v2 = new DenseVector(new double[]{4.0, 5.0, 6.0});
        
        Vector result = v1.plus(v2);
        
        // Правильная проверка элементов вектора
        assertEquals(5.0, result.get(0), 0.001);
        assertEquals(7.0, result.get(1), 0.001);
        assertEquals(9.0, result.get(2), 0.001);
    }

    @Test
    void testVectorScalarMultiplication() {
        Vector v1 = new DenseVector(new double[]{1.0, 2.0, 3.0});
        Vector result = v1.times(2.0);
        
        assertEquals(2.0, result.get(0), 0.001);
        assertEquals(4.0, result.get(1), 0.001);
        assertEquals(6.0, result.get(2), 0.001);
    }

    @Test
    void testVectorDistance() {
        Vector v1 = new DenseVector(new double[]{0.0, 0.0});
        Vector v2 = new DenseVector(new double[]{3.0, 4.0});
        
        double distance = Math.sqrt(v1.getDistanceSquared(v2));
        assertEquals(5.0, distance, 0.001);
    }

    @Test
    void testVectorDotProduct() {
        Vector v1 = new DenseVector(new double[]{1.0, 2.0});
        Vector v2 = new DenseVector(new double[]{3.0, 4.0});
        
        double dotProduct = v1.dot(v2);
        assertEquals(11.0, dotProduct, 0.001); // 1*3 + 2*4 = 3 + 8 = 11
    }

    @Test
    void testVectorSize() {
        Vector v1 = new DenseVector(new double[]{1.0, 2.0, 3.0, 4.0});
        assertEquals(4, v1.size());
    }
}