public class HouseMatcher {

    public boolean matches(SearchCriteria criteria, House house) {
        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }
        if (house == null) {
            throw new IllegalArgumentException("House cannot be null");
        }

        return house.city().equalsIgnoreCase(criteria.getCityLocation())
                && house.type().equalsIgnoreCase(criteria.getHouseType())
                && house.price() <= criteria.getMaxPrice()
                && house.bedrooms() >= criteria.getRoomAmount()
                && house.size() >= criteria.getHouseSize();
    }
}