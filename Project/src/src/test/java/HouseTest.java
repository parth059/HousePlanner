import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {

    private static final House SAMPLE_HOUSE = new House(
            "Toronto",
            1_800_000.0,
            5,
            "Detached",
            260.0,
            "https://example.com/toronto-detached.jpg"
    );

    @Test
    void city_returnsCorrectValue() {
        assertEquals("Toronto", SAMPLE_HOUSE.city());
    }

    @Test
    void price_returnsCorrectValue() {
        assertEquals(1_800_000.0, SAMPLE_HOUSE.price(), 0.001);
    }

    @Test
    void bedrooms_returnsCorrectValue() {
        assertEquals(5, SAMPLE_HOUSE.bedrooms());
    }

    @Test
    void type_returnsCorrectValue() {
        assertEquals("Detached", SAMPLE_HOUSE.type());
    }

    @Test
    void size_returnsCorrectValue() {
        assertEquals(260.0, SAMPLE_HOUSE.size(), 0.001);
    }

    @Test
    void imageUrl_returnsCorrectValue() {
        assertEquals("https://example.com/toronto-detached.jpg", SAMPLE_HOUSE.imageUrl());
    }

    @Test
    void equals_sameValues_returnsTrue() {
        House sameHouse = new House(
                "Toronto",
                1_800_000.0,
                5,
                "Detached",
                260.0,
                "https://example.com/toronto-detached.jpg"
        );

        assertEquals(SAMPLE_HOUSE, sameHouse);
        assertEquals(sameHouse, SAMPLE_HOUSE); // symmetric
        assertEquals(SAMPLE_HOUSE, SAMPLE_HOUSE); // reflexive
    }

    @Test
    void equals_differentValues_returnsFalse() {
        House differentPrice = new House("Toronto", 2_000_000.0, 5, "Detached", 260.0, SAMPLE_HOUSE.imageUrl());
        House differentCity = new House("Vancouver", 1_800_000.0, 5, "Detached", 260.0, SAMPLE_HOUSE.imageUrl());
        House differentBedrooms = new House("Toronto", 1_800_000.0, 4, "Detached", 260.0, SAMPLE_HOUSE.imageUrl());

        assertNotEquals(SAMPLE_HOUSE, differentPrice);
        assertNotEquals(SAMPLE_HOUSE, differentCity);
        assertNotEquals(SAMPLE_HOUSE, differentBedrooms);
        assertNotEquals(SAMPLE_HOUSE, null);
        assertNotEquals(SAMPLE_HOUSE, "Not a House"); // different type
    }

    @Test
    void hashCode_sameValues_sameHashCode() {
        House duplicate = new House(
                "Toronto",
                1_800_000.0,
                5,
                "Detached",
                260.0,
                "https://example.com/toronto-detached.jpg"
        );

        assertEquals(SAMPLE_HOUSE.hashCode(), duplicate.hashCode());
    }

    @Test
    void toString_containsAllFields() {
        String toString = SAMPLE_HOUSE.toString();

        assertTrue(toString.contains("Toronto"));
        assertTrue(toString.contains("1800000.0"));
        assertTrue(toString.contains("5"));
        assertTrue(toString.contains("Detached"));
        assertTrue(toString.contains("260.0"));
        assertTrue(toString.contains("https://example.com/toronto-detached.jpg"));
        // Typical format: House[city=Toronto, price=1800000.0, bedrooms=5, type=Detached, size=260.0, imageUrl=...]
        assertTrue(toString.startsWith("House["));
    }

    @Test
    void record_isImmutable_cannotModifyFields() {
        // Compile-time check: records don't have setters
        // We can only verify indirectly that fields can't be changed after construction

        House house = new House("Paris", 2_100_000, 6, "Villa", 320.1, "img.jpg");

        // No setter methods exist → this ensures immutability
        assertEquals("Paris", house.city()); // value remains as constructed
    }
}