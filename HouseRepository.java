import java.util.List;

public class HouseRepository {

    private static final List<House> HOUSES = List.of(
            new House("Toronto", 1800000, 5, "Detached", 260.0, "https://torontolife.mblycdn.com/tl/resized/2024/05/w1280/Derwyn1.jpg"),
            new House("Toronto", 1280000, 4, "Semi-detached", 182.4, "https://archello.s3.eu-central-1.amazonaws.com/images/2014/03/03/Binchang-02edittedcropped.1506072824.5898.jpg"),
            new House("Toronto", 980000, 3, "Townhouse", 138.2, "https://newhomes.livingrealtykw.com/wp-content/uploads/2021/06/dvistas-fi.jpg"),
            new House("Toronto", 1450000, 2, "Apartment", 112.6, "https://robbreport.com/wp-content/uploads/2024/04/FifthAve_Condo5.jpg"),
            new House("New York", 3200000, 3, "Penthouse", 195.0, "https://photos.zillowstatic.com/fp/9e72976698e5d7a8bccf1c39192e48fd-cc_ft_960.jpg"),
            new House("London", 1650000, 4, "Bungalow", 210.3, "https://cdn.houseplansservices.com/product/tahbfmakhok6k787jtmjm3ecgt/w560x373.png?v=9"),
            new House("Paris", 2100000, 6, "Villa", 320.1, "https://media.vrbo.com/lodging/93000000/92750000/92749300/92749255/78a6905c.jpg?impolicy=resizecrop&rw=575&rh=575&ra=fill"),
            new House("Tokyo", 2500000, 4, "Detached", 240.0, "https://cdn.mos.cms.futurecdn.net/6e85cbFxbbxZsxLkApn2zf.jpg"),
            new House("Sydney", 1950000, 3, "Townhouse", 150.5, "https://assets.savills.com/properties/AUS2RCR000181/AUS2RCR000181_I01_l_lis.jpg"),
            new House("Dubai", 4800000, 5, "Villa", 400.0, "https://rqitects.com/wp-content/uploads/2024/08/01_VILLA_ISMAIL_FRONT1.webp"),
            new House("Berlin", 1400000, 3, "Apartment", 130.2, "https://cf.bstatic.com/xdata/images/hotel/max1024x768/143407681.jpg?k=1b31194500187b168af60058a36fe5482defae5fcd105a2b13356a83b694d28a&o="),
            new House("Barcelona", 1750000, 4, "Semi-detached", 180.0, "https://images.adsttc.com/media/images/5005/01a4/28ba/0d4e/8d00/14e5/large_jpg/stringio.jpg?1375403590"),
            new House("Rome", 2200000, 5, "Villa", 310.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Villa_of_the_Mysteries_in_Pompeii.jpg/330px-Villa_of_the_Mysteries_in_Pompeii.jpg"),
            new House("Los Angeles", 3300000, 6, "Mansion", 450.0, "https://photos.zillowstatic.com/fp/6156ec1b0682986522be5230fb575a51-p_e.jpg"),
            new House("Singapore", 2700000, 4, "Condominium", 210.0, "https://stacked-editorial.sgp1.digitaloceanspaces.com/editorial/wp-content/uploads/2021/07/15170231/the-interlace-review.jpg")
    );

    private final HouseMatcher matcher = new HouseMatcher();
    private final SimilarityScoreIF similarityScorer;

    public HouseRepository(SimilarityScoreIF similarityScorer) {
        this.similarityScorer = similarityScorer;
    }

    public List<House> findMatching(HouseCriteria criteria) {
        return HOUSES.stream()
                .filter(house -> matcher.matches(criteria, house))
                .toList();
    }

    public List<House> findBestMatches(HouseCriteria criteria, int maxResults) {
        List<House> exact = findMatching(criteria);
        if (!exact.isEmpty()) {
            return exact.size() <= maxResults ? exact : exact.subList(0, maxResults);
        }
        return similarityScorer.findTopSimilar(criteria, HOUSES, maxResults);
    }

    public double calculateSimilarity(HouseCriteria criteria, House house) {
        return similarityScorer.calculateSimilarity(criteria, house);
    }
}