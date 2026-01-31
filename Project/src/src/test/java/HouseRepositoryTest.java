import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HouseRepositoryTest {

    private final SimilarityScoreIF scorer = new SimilarityScore(40, 10, 40, 5, 5);
    private final HouseRepository repository = new HouseRepository(scorer);

    private final HouseCriteria torontoDetachedCriteria = HouseCriteria.builder()
            .city("Toronto")
            .houseType("Detached")
            .maxPrice(2_000_000)
            .bedRooms(4)
            .houseSize(200.0)
            .build();

    @Test
    void findMatching_returnsOnlyExactMatches() {
        List<House> matches = repository.findMatching(torontoDetachedCriteria);

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());

        House house = matches.get(0);
        assertEquals("Toronto", house.city());
        assertEquals("Detached", house.type());
        assertEquals(1_800_000, house.price(), 0.001);
        assertEquals(5, house.bedrooms());
        assertEquals(260.0, house.size(), 0.001);
    }

    @Test
    void findMatching_returnsEmptyWhenNoExactMatch() {
        HouseCriteria strictCriteria = HouseCriteria.builder()
                .city("Toronto")
                .houseType("Villa")
                .maxPrice(3_000_000)
                .bedRooms(5)
                .houseSize(300.0)
                .build();

        List<House> matches = repository.findMatching(strictCriteria);

        assertTrue(matches.isEmpty());
    }

    @Test
    void findBestMatches_returnsExactMatchesWhenAvailable() {
        List<House> best = repository.findBestMatches(torontoDetachedCriteria, 5);

        assertEquals(1, best.size());
        assertEquals("Toronto", best.get(0).city());
        assertEquals("Detached", best.get(0).type());
    }

    @Test
    void findBestMatches_returnsTopSimilarWhenNoExactMatch() {
        HouseCriteria noExactCriteria = HouseCriteria.builder()
                .city("Toronto")
                .houseType("Villa")
                .maxPrice(3_000_000)
                .bedRooms(4)
                .houseSize(200.0)
                .build();

        List<House> best = repository.findBestMatches(noExactCriteria, 5);

        assertEquals(5, best.size());
        assertFalse(best.isEmpty());

        // The top one should be the Toronto Detached (city match + high other scores)
        assertEquals("Toronto", best.get(0).city());
        assertTrue(best.get(0).type().contains("Detached") || best.get(0).type().contains("Semi-detached"));
    }

    @Test
    void findBestMatches_respectsMaxResultsLimit() {
        HouseCriteria broadCriteria = HouseCriteria.builder()
                .city("Toronto")
                .houseType("Any")
                .maxPrice(10_000_000)
                .bedRooms(0)
                .houseSize(1.0)
                .build();

        List<House> best = repository.findBestMatches(broadCriteria, 2);

        assertEquals(2, best.size());
    }





    @Test
    void calculateSimilarity_handlesPriceOverBudgetWithin30Percent() {
        House slightlyOver = new House("Toronto", 2_400_000, 5, "Detached", 260.0, "url");
        // 20% over budget → price score reduced

        double similarity = repository.calculateSimilarity(torontoDetachedCriteria, slightlyOver);

        assertTrue(similarity > 70 && similarity < 85);
    }
}