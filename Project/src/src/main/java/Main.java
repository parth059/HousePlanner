import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to the House Search Engine ===\n");
        System.out.println("First, define the importance of each factor (weights must sum to 100):\n");

        double cityW, typeW, priceW, bedW, sizeW;
        while (true) {
            System.out.print("Weight for City (%): ");
            cityW = Double.parseDouble(scanner.nextLine());
            System.out.print("Weight for House Type (%): ");
            typeW = Double.parseDouble(scanner.nextLine());
            System.out.print("Weight for Price closeness (%): ");
            priceW = Double.parseDouble(scanner.nextLine());
            System.out.print("Weight for Bedrooms (%): ");
            bedW = Double.parseDouble(scanner.nextLine());
            System.out.print("Weight for Size (%): ");
            sizeW = Double.parseDouble(scanner.nextLine());

            double sum = cityW + typeW + priceW + bedW + sizeW;
            if (Math.abs(sum - 100.0) < 0.01) {
                break;
            }
            System.out.println("Error: Weights must add up to 100 (current: " + String.format("%.1f", sum) + "). Try again.\n");
        }

        SimilarityScoreIF scorer = new SimilarityScore(cityW, typeW, priceW, bedW, sizeW);
        HouseRepository repository = new HouseRepository(scorer);

        System.out.println("\nEnter your search preferences:\n");

        System.out.print("City (required): ");
        String city = scanner.nextLine();
        System.out.print("House type (required, e.g., Detached): ");
        String houseType = scanner.nextLine();
        System.out.print("Maximum price: ");
        double maxPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Minimum bedrooms: ");
        int bedRooms = Integer.parseInt(scanner.nextLine());
        System.out.print("Minimum size (sqm): ");
        double houseSize = Double.parseDouble(scanner.nextLine());

        try {
            HouseCriteria criteria = HouseCriteria.builder()
                    .city(city)
                    .houseType(houseType)
                    .maxPrice(maxPrice)
                    .bedRooms(bedRooms)
                    .houseSize(houseSize)
                    .build();

            System.out.println("\nSearching...\n");

            List<House> matches = repository.findBestMatches(criteria, 5);

            if (matches.isEmpty()) {
                System.out.println("No houses found.");
            } else {
                boolean hasExact = !repository.findMatching(criteria).isEmpty();
                if (hasExact) {
                    System.out.println("Exact matches found:\n");
                } else {
                    System.out.println("No exact matches. Top similar houses:\n");
                }

                for (int i = 0; i < matches.size(); i++) {
                    House house = matches.get(i);
                    double similarity = repository.calculateSimilarity(criteria, house);
                    System.out.printf("Rank #%d - Similarity: %.1f%%\n", i + 1, similarity);
                    System.out.println("City: " + house.city());
                    System.out.println("Type: " + house.type());
                    System.out.println("Price: $" + String.format("%,.0f", house.price()));
                    System.out.println("Bedrooms: " + house.bedrooms());
                    System.out.println("Size: " + house.size() + " sqm");
                    System.out.println("Image: " + house.imageUrl());
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}