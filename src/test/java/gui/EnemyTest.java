package gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    private Enemy enemy;
    private Dimension fieldSize;

    @BeforeEach
    void setUp() {
        enemy = new Enemy(100, 100, Color.BLUE);
        fieldSize = new Dimension(800, 600);
    }

    @Test
    void testEnemyMovesTowardsTarget() {
        enemy.positionX = 100;
        enemy.positionY = 100;
        double targetX = 200;
        double targetY = 200;

        for (int i = 0; i < 20; i++) {
            enemy.moveTowards(targetX, targetY, 10, fieldSize);
        }

        assertTrue(enemy.positionX > 100, "Enemy should move right towards target");
        assertTrue(enemy.positionY > 100, "Enemy should move down towards target");

        double finalDistance = Enemy.distance(enemy.positionX, enemy.positionY, targetX, targetY);
        double initialDistance = Enemy.distance(100, 100, 200, 200);
        assertTrue(finalDistance < initialDistance, "Distance to target should decrease");
    }

    @Test
    void testEnemyStopsWhenReachesTarget() {
        // Помещаем врага очень близко к цели
        enemy.positionX = 100;
        enemy.positionY = 100;

        enemy.moveTowards(100.1, 100.1, 10, fieldSize);

        assertEquals(100, enemy.positionX, 0.5);
        assertEquals(100, enemy.positionY, 0.5);
    }

    @Test
    void testEnemyWrapsAroundFieldEdges() {
        enemy.positionX = fieldSize.width * 2 - 5;
        enemy.positionY = 300;
        enemy.direction = 0; // Направление вправо

        double initialX = enemy.positionX;

        for (int i = 0; i < 10; i++) {
            enemy.updatePosition(0.2, 0, 10, fieldSize);
        }

        assertTrue(enemy.positionX < initialX,
                "Enemy should wrap around from right to left. Current X: " + enemy.positionX);

        assertEquals(300, enemy.positionY, 0.001);
    }

    @Test
    void testAngleToTargetCalculation() {
        // Враг в (0,0), цель в (1,1) - угол должен быть PI/4 (45 градусов)
        enemy.positionX = 0;
        enemy.positionY = 0;
        double angle = Enemy.angleTo(0, 0, 1, 1);
        assertEquals(Math.PI/4, angle, 0.01);
    }
}
