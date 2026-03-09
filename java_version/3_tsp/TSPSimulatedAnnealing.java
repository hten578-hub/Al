/**
 * Giải bài toán Người du lịch (TSP) bằng Simulated Annealing
 * Java version
 */
import java.util.*;

class City {
    private double x, y;
    
    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    
    public double distanceTo(City other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

public class TSPSimulatedAnnealing {
    private List<City> cities;
    private Random random;
    
    public TSPSimulatedAnnealing(List<City> cities) {
        this.cities = cities;
        this.random = new Random();
    }
    
    public double calculateTotalDistance(List<Integer> tour) {
        double distance = 0;
        for (int i = 0; i < tour.size(); i++) {
            City current = cities.get(tour.get(i));
            City next = cities.get(tour.get((i + 1) % tour.size()));
            distance += current.distanceTo(next);
        }
        return distance;
    }
    
    public List<Integer> getNeighbor(List<Integer> tour) {
        List<Integer> newTour = new ArrayList<>(tour);
        
        // Sử dụng 2-opt swap thay vì random swap để tối ưu hơn
        int i = random.nextInt(tour.size());
        int j = random.nextInt(tour.size());
        
        // Đảm bảo i < j
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        
        if (i == j) return newTour;
        
        // Reverse đoạn từ i đến j (2-opt)
        while (i < j) {
            Collections.swap(newTour, i, j);
            i++;
            j--;
        }
        
        return newTour;
    }
    
    public TSPResult simulatedAnnealing(double initialTemp, double coolingRate, double minTemp) {
        int n = cities.size();
        
        // Chạy nhiều lần và lấy kết quả tốt nhất
        List<Integer> globalBestTour = null;
        double globalBestDistance = Double.POSITIVE_INFINITY;
        int numRuns = 3; // Chạy 3 lần
        
        System.out.println("Bắt đầu tối ưu hóa với " + numRuns + " lần chạy...");
        
        for (int run = 0; run < numRuns; run++) {
            System.out.println("\n--- Lần chạy " + (run + 1) + "/" + numRuns + " ---");
            
            // Tạo tour ban đầu ngẫu nhiên
            List<Integer> currentTour = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                currentTour.add(i);
            }
            Collections.shuffle(currentTour);
            
            double currentDistance = calculateTotalDistance(currentTour);
            
            List<Integer> bestTour = new ArrayList<>(currentTour);
            double bestDistance = currentDistance;
            
            double temperature = initialTemp;
            int iterations = 0;
            
            while (temperature > minTemp) {
                List<Integer> newTour = getNeighbor(currentTour);
                double newDistance = calculateTotalDistance(newTour);
                
                double delta = newDistance - currentDistance;
                
                // Chấp nhận nếu tốt hơn hoặc theo xác suất
                if (delta < 0 || random.nextDouble() < Math.exp(-delta / temperature)) {
                    currentTour = newTour;
                    currentDistance = newDistance;
                    
                    if (currentDistance < bestDistance) {
                        bestTour = new ArrayList<>(currentTour);
                        bestDistance = currentDistance;
                    }
                }
                
                temperature *= coolingRate;
                iterations++;
                
                // In tiến trình mỗi 10000 lần lặp
                if (iterations % 10000 == 0) {
                    System.out.printf("  Lần lặp %d: Nhiệt độ = %.2f, Khoảng cách tốt nhất = %.2f%n", 
                                    iterations, temperature, bestDistance);
                }
            }
            
            System.out.printf("Hoàn thành lần chạy %d: %d lần lặp, Khoảng cách = %.2f%n", 
                            run + 1, iterations, bestDistance);
            
            if (bestDistance < globalBestDistance) {
                globalBestDistance = bestDistance;
                globalBestTour = new ArrayList<>(bestTour);
            }
        }
        
        System.out.println("\n=== KẾT QUẢ TỐT NHẤT ===");
        System.out.printf("Khoảng cách tốt nhất sau %d lần chạy: %.2f%n", numRuns, globalBestDistance);
        
        return new TSPResult(globalBestTour, globalBestDistance);
    }
    
    public static class TSPResult {
        private List<Integer> tour;
        private double distance;
        
        public TSPResult(List<Integer> tour, double distance) {
            this.tour = tour;
            this.distance = distance;
        }
        
        public List<Integer> getTour() { return tour; }
        public double getDistance() { return distance; }
    }
    
    public static void main(String[] args) {
        // Danh sách thành phố (tọa độ x, y)
        List<City> cities = Arrays.asList(
            new City(0, 0), new City(1, 5), new City(5, 2), new City(6, 6), new City(8, 3),
            new City(3, 7), new City(2, 4), new City(7, 1), new City(4, 8), new City(9, 4)
        );
        
        System.out.println("=== BÀI TOÁN NGƯỜI DU LỊCH (TSP) ===");
        System.out.println("Số thành phố: " + cities.size());
        
        System.out.println("\nDanh sách thành phố:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.println("Thành phố " + i + ": " + cities.get(i));
        }
        
        TSPSimulatedAnnealing tsp = new TSPSimulatedAnnealing(cities);
        
        // Tham số Simulated Annealing
        double initialTemp = 1000.0;
        double coolingRate = 0.995;
        double minTemp = 1.0;
        
        System.out.println("\nTham số:");
        System.out.println("Nhiệt độ ban đầu: " + initialTemp);
        System.out.println("Tỷ lệ làm lạnh: " + coolingRate);
        System.out.println("Nhiệt độ tối thiểu: " + minTemp);
        
        long startTime = System.currentTimeMillis();
        TSPResult result = tsp.simulatedAnnealing(initialTemp, coolingRate, minTemp);
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n=== KẾT QUẢ ===");
        System.out.println("Tour tốt nhất: " + result.getTour());
        System.out.printf("Tổng khoảng cách: %.2f%n", result.getDistance());
        System.out.println("Thời gian thực hiện: " + (endTime - startTime) + " ms");
        
        System.out.println("\nThứ tự thăm:");
        List<Integer> tour = result.getTour();
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < tour.size(); i++) {
            path.append(tour.get(i));
            if (i < tour.size() - 1) {
                path.append(" -> ");
            }
        }
        path.append(" -> ").append(tour.get(0)); // Quay về điểm xuất phát
        System.out.println(path.toString());
    }
}