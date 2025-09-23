package MLJavaBook.RecommenderSystem;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private Vector v1;
    private Vector v2;

    @BeforeEach
    void setUp() {
        v1 = new DenseVector(new double[]{1.0, 2.0, 3.0});
        v2 = new DenseVector(new double[]{4.0, 5.0, 6.0});
    }

    @Test
    void testDotProduct() {
        double dotProduct = v1.dot(v2);
        
        assertEquals(32.0, dotProduct, 0.001, "Скалярное произведение должно быть 32.0");
    }

    @Test
    void testVectorCreation() {
        assertNotNull(v1, "Вектор v1 не должен быть null");
        assertNotNull(v2, "Вектор v2 не должен быть null");
        
        assertEquals(3, v1.size(), "Размер вектора v1 должен быть 3");
        assertEquals(3, v2.size(), "Размер вектора v2 должен быть 3");
    }

    @Test
    void testVectorValues() {
        // Используем AssertJ для более читаемых assertions
        assertThat(v1.get(0)).isEqualTo(1.0);
        assertThat(v1.get(1)).isEqualTo(2.0);
        assertThat(v1.get(2)).isEqualTo(3.0);
        
        assertThat(v2.get(0)).isEqualTo(4.0);
        assertThat(v2.get(1)).isEqualTo(5.0);
        assertThat(v2.get(2)).isEqualTo(6.0);
    }

    @Test
    void testVectorNorm() {
        double norm1 = v1.norm(2); // Евклидова норма
        double expectedNorm1 = Math.sqrt(1*1 + 2*2 + 3*3);
        
        assertEquals(expectedNorm1, norm1, 0.001, "Норма вектора v1 вычислена неверно");
    }
}
