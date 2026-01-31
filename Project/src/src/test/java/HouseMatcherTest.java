import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HouseMatcherTest {

    private final HouseMatcher matcher = new HouseMatcher();

    @Test
    void matches_allCriteriaMet_returnsTrue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Toronto")
                .houseType("Detached")
                .maxPrice(2_000_000)
                .bedRooms(4)
                .houseSize(200.0)
                .build();

        House house = new House("Toronto", 1_800_000, 5, "Detached", 260.0,
                "https://example.com/toronto-detached.jpg");

        assertTrue(matcher.matches(criteria, house));
    }

    @Test
    void matches_exactPriceAndSizeOnBoundary_returnsTrue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Paris")
                .houseType("Villa")
                .maxPrice(2_100_000)
                .bedRooms(6)
                .houseSize(320.1)
                .build();

        House house = new House("Paris", 2_100_000, 6, "Villa", 320.1,
                "https://example.com/paris-villa.jpg");

        assertTrue(matcher.matches(criteria, house));
    }

    @Test
    void matches_caseInsensitiveCityAndType_returnsTrue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("tokyo")
                .houseType("detached")
                .maxPrice(3_000_000)
                .bedRooms(3)
                .houseSize(200.0)
                .build();

        House house = new House("Tokyo", 2_500_000, 4, "Detached", 240.0,
                "https://example.com/tokyo.jpg");

        assertTrue(matcher.matches(criteria, house));
    }

    @Test
    void matches_moreBedroomsThanRequired_returnsTrue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Dubai")
                .houseType("Villa")
                .maxPrice(5_000_000)
                .bedRooms(4)
                .houseSize(350.0)
                .build();

        House house = new House("Dubai", 4_800_000, 5, "Villa", 400.0,
                "https://example.com/dubai-villa.jpg");

        assertTrue(matcher.matches(criteria, house));
    }

    @Test
    void matches_priceOverMax_returnsFalse() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("New York")
                .houseType("Penthouse")
                .maxPrice(3_000_000)
                .bedRooms(3)
                .houseSize(190.0)
                .build();

        House house = new House("New York", 3_200_000, 3, "Penthouse", 195.0,
                "https://example.com/ny-penthouse.jpg");

        assertFalse(matcher.matches(criteria, house));
    }

    @Test
    void matches_fewerBedroomsThanRequired_returnsFalse() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("London")
                .houseType("Bungalow")
                .maxPrice(2_000_000)
                .bedRooms(5)
                .houseSize(200.0)
                .build();

        House house = new House("London", 1_650_000, 4, "Bungalow", 210.3,
                "https://example.com/london-bungalow.jpg");

        assertFalse(matcher.matches(criteria, house));
    }

    @Test
    void matches_smallerSizeThanRequired_returnsFalse() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Sydney")
                .houseType("Townhouse")
                .maxPrice(2_000_000)
                .bedRooms(3)
                .houseSize(160.0)
                .build();

        House house = new House("Sydney", 1_950_000, 3, "Townhouse", 150.5,
                "https://example.com/sydney.jpg");

        assertFalse(matcher.matches(criteria, house));
    }

    @Test
    void matches_differentCity_returnsFalse() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Los Angeles")
                .houseType("Mansion")
                .maxPrice(4_000_000)
                .bedRooms(5)
                .houseSize(400.0)
                .build();

        House house = new House("San Francisco", 3_300_000, 6, "Mansion", 450.0,
                "https://example.com/la-mansion.jpg");

        assertFalse(matcher.matches(criteria, house));
    }

    @Test
    void matches_differentHouseType_returnsFalse() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Barcelona")
                .houseType("Apartment")
                .maxPrice(2_000_000)
                .bedRooms(3)
                .houseSize(150.0)
                .build();

        House house = new House("Barcelona", 1_750_000, 4, "Semi-detached", 180.0,
                "https://example.com/barcelona.jpg");

        assertFalse(matcher.matches(criteria, house));
    }

    @Test
    void matches_nullCriteria_throwsIllegalArgumentException() {
        House house = new House("Toronto", 1_800_000, 5, "Detached", 260.0, "img.jpg");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                matcher.matches(null, house));

        assertEquals("Search criteria cannot be null", ex.getMessage());
    }

    @Test
    void matches_nullHouse_throwsIllegalArgumentException() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Toronto")
                .houseType("Detached")
                .maxPrice(2_000_000)
                .bedRooms(3)
                .houseSize(150.0)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                matcher.matches(criteria, null));

        assertEquals("House cannot be null", ex.getMessage());
    }
}