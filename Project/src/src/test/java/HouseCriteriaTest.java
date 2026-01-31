import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HouseCriteriaTest {

    @Test
    void builder_successfulBuildAndGetters() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Vancouver")
                .houseType("Detached")
                .maxPrice(2_500_000.0)
                .bedRooms(3)
                .houseSize(200.5)
                .build();

        assertEquals("Vancouver", criteria.getCityLocation());
        assertEquals(2_500_000.0, criteria.getMaxPrice(), 0.001);
        assertEquals(3, criteria.getRoomAmount());
        assertEquals("Detached", criteria.getHouseType());
        assertEquals(200.5, criteria.getHouseSize(), 0.001);
    }

    @Test
    void getCityLocation_returnsCorrectValue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("New York")
                .houseType("Apartment")
                .maxPrice(1_800_000)
                .bedRooms(2)
                .houseSize(120.0)
                .build();

        assertEquals("New York", criteria.getCityLocation());
    }

    @Test
    void getMaxPrice_returnsCorrectValue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("London")
                .houseType("Terraced")
                .maxPrice(1_250_000.75)
                .bedRooms(4)
                .houseSize(165.3)
                .build();

        assertEquals(1_250_000.75, criteria.getMaxPrice(), 0.001);
    }

    @Test
    void getRoomAmount_returnsCorrectValueIncludingZero() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Miami")
                .houseType("Condominium")
                .maxPrice(900_000)
                .bedRooms(0)  // Studio apartment scenario
                .houseSize(65.0)
                .build();

        assertEquals(0, criteria.getRoomAmount());
    }

    @Test
    void getHouseType_returnsCorrectValueWithHyphenAndSpaces() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("San Francisco")
                .houseType("Semi-detached")
                .maxPrice(3_200_000)
                .bedRooms(4)
                .houseSize(220.0)
                .build();

        assertEquals("Semi-detached", criteria.getHouseType());
    }

    @Test
    void getHouseSize_returnsCorrectValue() {
        HouseCriteria criteria = HouseCriteria.builder()
                .city("Sydney")
                .houseType("Bungalow")
                .maxPrice(2_100_000)
                .bedRooms(3)
                .houseSize(180.75)
                .build();

        assertEquals(180.75, criteria.getHouseSize(), 0.001);
    }

    // Validation tests

    @Test
    void builder_cityWithInvalidCharacters_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Toronto!@#")
                        .houseType("Detached")
                        .maxPrice(1_000_000)
                        .bedRooms(3)
                        .houseSize(150)
                        .build());

        assertTrue(ex.getMessage().contains("letters, spaces, and hyphens"));
    }

    @Test
    void builder_houseTypeWithInvalidCharacters_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Berlin")
                        .houseType("Loft-Apartment$")
                        .maxPrice(1_400_000)
                        .bedRooms(2)
                        .houseSize(110)
                        .build());

        assertTrue(ex.getMessage().contains("letters, spaces, and hyphens"));
    }

    @Test
    void builder_maxPriceZeroOrNegative_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Paris")
                        .houseType("Villa")
                        .maxPrice(0)
                        .bedRooms(5)
                        .houseSize(300)
                        .build());

        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Paris")
                        .houseType("Villa")
                        .maxPrice(-500_000)
                        .bedRooms(5)
                        .houseSize(300)
                        .build());
    }

    @Test
    void builder_negativeBedRooms_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Tokyo")
                        .houseType("Detached")
                        .maxPrice(2_800_000)
                        .bedRooms(-1)
                        .houseSize(240)
                        .build());
    }

    @Test
    void builder_houseSizeZeroOrNegative_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Dubai")
                        .houseType("Villa")
                        .maxPrice(5_000_000)
                        .bedRooms(5)
                        .houseSize(0)
                        .build());

        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Dubai")
                        .houseType("Villa")
                        .maxPrice(5_000_000)
                        .bedRooms(5)
                        .houseSize(-50)
                        .build());
    }

    @Test
    void builder_missingCity_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                HouseCriteria.builder()
                        .houseType("Townhouse")
                        .maxPrice(1_100_000)
                        .bedRooms(3)
                        .houseSize(140)
                        .build());

        assertTrue(ex.getMessage().contains("Required fields"));
    }

    @Test
    void builder_missingHouseType_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                HouseCriteria.builder()
                        .city("Barcelona")
                        .maxPrice(1_750_000)
                        .bedRooms(4)
                        .houseSize(180)
                        .build());

        assertTrue(ex.getMessage().contains("Required fields"));
    }

    @Test
    void builder_blankHouseType_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                HouseCriteria.builder()
                        .city("Rome")
                        .houseType("   ")
                        .maxPrice(2_200_000)
                        .bedRooms(5)
                        .houseSize(310)
                        .build());
    }
}