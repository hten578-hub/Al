"""
Giải bài toán Người du lịch (TSP) bằng Simulated Annealing
"""
import random
import math
from typing import List, Tuple

def calculate_distance(city1: Tuple[float, float], city2: Tuple[float, float]) -> float:
    """Tính khoảng cách Euclidean giữa 2 thành phố"""
    return math.sqrt((city1[0] - city2[0])**2 + (city1[1] - city2[1])**2)

def total_distance(tour: List[int], cities: List[Tuple[float, float]]) -> float:
    """Tính tổng khoảng cách của tour"""
    distance = 0
    for i in range(len(tour)):
        distance += calculate_distance(cities[tour[i]], cities[tour[(i + 1) % len(tour)]])
    return distance

def get_neighbor(tour: List[int]) -> List[int]:
    """Tạo tour láng giềng bằng cách hoán đổi 2 thành phố"""
    new_tour = tour[:]
    i, j = random.sample(range(len(tour)), 2)
    new_tour[i], new_tour[j] = new_tour[j], new_tour[i]
    return new_tour

def simulated_annealing(cities: List[Tuple[float, float]], 
                       initial_temp: float = 1000,
                       cooling_rate: float = 0.995,
                       min_temp: float = 1) -> Tuple[List[int], float]:
    """
    Giải TSP bằng Simulated Annealing
    """
    n = len(cities)
    current_tour = list(range(n))
    random.shuffle(current_tour)
    current_distance = total_distance(current_tour, cities)
    
    best_tour = current_tour[:]
    best_distance = current_distance
    
    temperature = initial_temp
    
    while temperature > min_temp:
        new_tour = get_neighbor(current_tour)
        new_distance = total_distance(new_tour, cities)
        
        delta = new_distance - current_distance
        
        # Chấp nhận nếu tốt hơn hoặc theo xác suất
        if delta < 0 or random.random() < math.exp(-delta / temperature):
            current_tour = new_tour
            current_distance = new_distance
            
            if current_distance < best_distance:
                best_tour = current_tour[:]
                best_distance = current_distance
        
        temperature *= cooling_rate
    
    return best_tour, best_distance

if __name__ == "__main__":
    # Danh sách thành phố (tọa độ x, y)
    cities = [
        (0, 0), (1, 5), (5, 2), (6, 6), (8, 3),
        (3, 7), (2, 4), (7, 1), (4, 8), (9, 4)
    ]
    
    print(f"Số thành phố: {len(cities)}")
    print("\nĐang tìm kiếm lời giải tối ưu...")
    
    best_tour, best_distance = simulated_annealing(cities)
    
    print(f"\nTour tốt nhất: {best_tour}")
    print(f"Tổng khoảng cách: {best_distance:.2f}")
    print(f"\nThứ tự thăm: {' -> '.join(map(str, best_tour))} -> {best_tour[0]}")
