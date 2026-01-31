import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityScoreTest {

    private final SimilarityScoreIF scorer = new SimilarityScore(40.0, 10.0, 40.0, 5.0, 5.0);

    private final HouseCriteria torontoDetachedCriteria = HouseCriteria.builder()
            .city("Toronto")
            .houseType("Detached")
            .maxPrice(2_000_000)
            .bedRooms(4)
            .houseSize(200.0)
            .build();


    @Test
    void calculateSimilarity_slightlyOverBudgetWithin30Percent() {
        House overBudget = new House("Toronto", 2_400_000, 5, "Detached", 260.0, "url");
        // 20% over (400k / 2.4M ≈ 16.67%) → price score = (0.4 - 0.1667) × 40 ≈ 9.33

        double similarity = scorer.calculateSimilarity(torontoDetachedCriteria, overBudget);

        // City 40 + Type 10 + Price ≈9.33 + Bedrooms 5.5 + Size 6.5 ≈ 71.33%
        assertEquals(71.3, similarity, 0.5);
    }

    @Test
    void calculateSimilarity_overBudgetMoreThan30PercentGetsZeroPriceScore() {
        House wayOver = new House("Toronto", 3_000_000, 5, "Detached", 260.0, "url");
        // >30% over → price score = 0

        double similarity = scorer.calculateSimilarity(torontoDetachedCriteria, wayOver);

        assertEquals(62.0, similarity, 0.1);
    }



    @Test
    void findTopSimilar_returnsCorrectlyOrderedTopN() {
        List<House> houses = List.of(
                new House("Toronto", 1_800_000, 5, "Detached", 260.0, "perfect"),      // ~100%
                new House("Toronto", 1_280_000, 4, "Semi-detached", 182.4, "semi"),    // high (city+price)
                new House("Toronto", 980_000, 3, "Townhouse", 138.2, "town"),          // lower size/beds
                new House("Paris", 2_100_000, 6, "Villa", 320.1, "villa")              // wrong city
        );

        List<House> top = scorer.findTopSimilar(torontoDetachedCriteria, houses, 3);

        assertEquals(3, top.size());
        assertEquals("perfect", top.get(0).imageUrl());
        assertEquals("semi", top.get(1).imageUrl());
        assertEquals("town", top.get(2).imageUrl());
    }

    @Test
    void findTopSimilar_whenFewerThanN_returnsAllInOrder() {
        List<House> houses = List.of(
                new House("Paris", 2_100_000, 6, "Villa", 320.1, "low"),
                new House("Toronto", 1_800_000, 5, "Detached", 260.0, "high")
        );

        List<House> top = scorer.findTopSimilar(torontoDetachedCriteria, houses, 5);

        assertEquals(2, top.size());
        assertEquals("high", top.get(0).imageUrl());
        assertEquals("low", top.get(1).imageUrl());
    }
}