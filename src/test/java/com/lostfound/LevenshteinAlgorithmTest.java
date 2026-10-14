package com.lostfound;

import com.lostfound.member3_matching.algo.LevenshteinAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LevenshteinAlgorithmTest {

    @Test
    public void testIdenticalStrings() {
        int distance = LevenshteinAlgorithm.computeDistance("wallet", "wallet");
        assertEquals(0, distance);

        double similarity = LevenshteinAlgorithm.computeLevenshteinSimilarity("Tommy Hilfiger Wallet", "Tommy Hilfiger Wallet");
        assertEquals(100.0, similarity, 0.01);
    }

    @Test
    public void testDistanceCalculation() {
        // "kitten" to "sitting" requires 3 edits (substitution 'k'->'s', 'e'->'i', addition 'g')
        int distance = LevenshteinAlgorithm.computeDistance("kitten", "sitting");
        assertEquals(3, distance);
    }

    @Test
    public void testCaseInsensitiveNormalization() {
        double similarity = LevenshteinAlgorithm.computeLevenshteinSimilarity("BLACK WALLET", "black   wallet");
        assertEquals(100.0, similarity, 0.01);
    }

    @Test
    public void testTokenOverlapAndTextScore() {
        double score = LevenshteinAlgorithm.calculateTextScore("Black Leather Tommy Hilfiger Wallet", "Black Leather Men Wallet");
        assertTrue(score > 60.0, "Expected score > 60.0, got: " + score);
    }

    @Test
    public void testEmptyStrings() {
        assertEquals(0, LevenshteinAlgorithm.computeDistance("", ""));
        assertEquals(5, LevenshteinAlgorithm.computeDistance("apple", ""));
        assertEquals(5, LevenshteinAlgorithm.computeDistance("", "apple"));
    }
}
