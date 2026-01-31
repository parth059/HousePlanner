import java.util.List;

public class SimilarityScore implements SimilarityScoreIF {

    private final double cityWeight;
    private final double typeWeight;
    private final double priceWeight;
    private final double bedroomsWeight;
    private final double sizeWeight;
    private final double totalWeight;

    public SimilarityScore(double cityWeight, double typeWeight,
                           double priceWeight, double bedroomsWeight, double sizeWeight) {
        this.cityWeight = cityWeight;
        this.typeWeight = typeWeight;
        this.priceWeight = priceWeight;
        this.bedroomsWeight = bedroomsWeight;
        this.sizeWeight = sizeWeight;
        this.totalWeight = cityWeight + typeWeight + priceWeight + bedroomsWeight + sizeWeight;

        if (Math.abs(totalWeight - 100.0) > 0.01) {
            throw new IllegalArgumentException("Weights must sum to 100.0");
        }
    }

    @Override
    public double calculateSimilarity(HouseCriteria criteria, House house) {
        double score = 0.0;

        if (house.city().equalsIgnoreCase(criteria.getCityLocation())) {
            score += cityWeight;
        }

        if (house.type().equalsIgnoreCase(criteria.getHouseType())) {
            score += typeWeight;
        }

        double priceDiff = criteria.getMaxPrice() - house.price();
        if (priceDiff >= 0) {
            double ratio = priceDiff / criteria.getMaxPrice();
            score += priceWeight * (0.5 + 0.5 * ratio);
        } else {
            double overRatio = -priceDiff / house.price();
            if (overRatio < 0.3) {
                score += priceWeight * (0.4 - overRatio);
            }
        }

        int extraBedrooms = house.bedrooms() - criteria.getRoomAmount();
        if (extraBedrooms >= 0) {
            score += bedroomsWeight * Math.min(1.0 + extraBedrooms * 0.1, 1.5);
        }

        double sizeRatio = house.size() / criteria.getHouseSize();
        if (sizeRatio >= 1.0) {
            score += sizeWeight * Math.min(sizeRatio, 2.0);
        } else {
            score += sizeWeight * sizeRatio;
        }

        return (score / totalWeight) * 100.0;
    }

    @Override
    public List<House> findTopSimilar(HouseCriteria criteria, List<House> allHouses, int topN) {
        return allHouses.stream()
                .map(house -> new ScoredHouse(house, calculateSimilarity(criteria, house)))
                .sorted((s1, s2) -> Double.compare(s2.score, s1.score))
                .limit(topN)
                .map(scored -> scored.house)
                .toList();
    }

    private record ScoredHouse(House house, double score) {}
}