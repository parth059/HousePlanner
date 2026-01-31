public class HouseCriteria implements SearchCriteria {

    private final String city;
    private final double maxPrice;
    private final int bedRooms;
    private final String houseType;
    private final double houseSize;

    private HouseCriteria(Builder builder) {
        this.city = builder.city;
        this.maxPrice = builder.maxPrice;
        this.bedRooms = builder.bedRooms;
        this.houseType = builder.houseType;
        this.houseSize = builder.houseSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String city;
        private double maxPrice;
        private int bedRooms;
        private String houseType;
        private double houseSize;

        private Builder() {}

        public Builder city(String city) {
            String trimmed = city.trim();
            for (char c : trimmed.toCharArray()) {
                if (!Character.isAlphabetic(c) && c != ' ' && c != '-') {
                    throw new IllegalArgumentException("City can only contain letters, spaces, and hyphens.");
                }
            }
            this.city = trimmed;
            return this;
        }

        public Builder maxPrice(double maxPrice) {
            if (maxPrice <= 0) {
                throw new IllegalArgumentException("The price can't be negative or zero");
            }
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder bedRooms(int bedRooms) {
            if (bedRooms < 0) {
                throw new IllegalArgumentException("Bedroom number cannot be negative");
            }
            this.bedRooms = bedRooms;
            return this;
        }

        public Builder houseType(String houseType) {
            if (houseType == null) {
                throw new IllegalArgumentException("House type cannot be null.");
            }
            String trimmed = houseType.trim();
            if (trimmed.isBlank()) {
                throw new IllegalArgumentException("House type cannot be blank or empty.");
            }
            for (char c : trimmed.toCharArray()) {
                if (!Character.isAlphabetic(c) && c != ' ' && c != '-') {
                    throw new IllegalArgumentException("House type can only contain letters, spaces, and hyphens.");
                }
            }
            this.houseType = trimmed;
            return this;
        }

        public Builder houseSize(double houseSize) {
            if (houseSize <= 0) {
                throw new IllegalArgumentException("House size must be greater than 0");
            }
            this.houseSize = houseSize;
            return this;
        }

        public HouseCriteria build() {
            if (city == null || houseType == null) {
                throw new IllegalStateException("Required fields (city, houseType) are missing.");
            }
            return new HouseCriteria(this);
        }
    }

    @Override
    public String getCityLocation() {
        return city;
    }

    @Override
    public double getMaxPrice() {
        return maxPrice;
    }

    @Override
    public int getRoomAmount() {
        return bedRooms;
    }

    @Override
    public String getHouseType() {
        return houseType;
    }

    @Override
    public double getHouseSize() {
        return houseSize;
    }
}