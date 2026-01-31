import java.util.List;

public interface SimilarityScoreIF {
    double calculateSimilarity(HouseCriteria criteria, House house);
    List<House> findTopSimilar(HouseCriteria criteria, List<House> allHouses, int topN);
}