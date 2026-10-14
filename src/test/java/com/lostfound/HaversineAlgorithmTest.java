package com.lostfound;

import com.lostfound.member3_matching.algo.HaversineAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HaversineAlgorithmTest {

    @Test
    public void testSameCoordinatesZeroDistance() {
        double lat = 28.6139;
        double lon = 77.2090;
        double dist = HaversineAlgorithm.calculateDistanceKm(lat, lon, lat, lon);

        assertEquals(0.0, dist, 0.001);
        assertEquals(100.0, HaversineAlgorithm.convertDistanceToScore(dist));
    }

    @Test
    public void testDistanceToScoreThresholds() {
        assertEquals(100.0, HaversineAlgorithm.convertDistanceToScore(0.05));
        assertEquals(90.0, HaversineAlgorithm.convertDistanceToScore(0.30));
        assertEquals(75.0, HaversineAlgorithm.convertDistanceToScore(0.80));
        assertEquals(60.0, HaversineAlgorithm.convertDistanceToScore(1.50));
        assertEquals(40.0, HaversineAlgorithm.convertDistanceToScore(3.50));
        assertEquals(20.0, HaversineAlgorithm.convertDistanceToScore(10.0));
    }

    @Test
    public void testKnownDistanceBetweenPoints() {
        // Distance between New Delhi (28.6139, 77.2090) and Mumbai (19.0760, 72.8777) is ~1148 km
        double dist = HaversineAlgorithm.calculateDistanceKm(28.6139, 77.2090, 19.0760, 72.8777);
        assertTrue(dist > 1100 && dist < 1200);
    }
}
