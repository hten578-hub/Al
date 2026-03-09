"""
Giải mê cung bằng BFS, DFS và A*
"""
from collections import deque
import heapq
from typing import List, Tuple, Optional, Set

class MazeSolver:
    def __init__(self, maze: List[List[int]]):
        self.maze = maze
        self.rows = len(maze)
        self.cols = len(maze[0])
        self.directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]  # Phải, Xuống, Trái, Lên
        
    def is_valid(self, x: int, y: int, visited: Set[Tuple[int, int]]) -> bool:
        """Kiểm tra ô hợp lệ"""
        return (0 <= x < self.rows and 0 <= y < self.cols and 
                self.maze[x][y] == 0 and (x, y) not in visited)
    
    def bfs(self, start: Tuple[int, int], end: Tuple[int, int]) -> Optional[List[Tuple[int, int]]]:
        """Breadth-First Search"""
        queue = deque([(start, [start])])
        visited = {start}
        
        while queue:
            (x, y), path = queue.popleft()
            
            if (x, y) == end:
                return path
            
            for dx, dy in self.directions:
                nx, ny = x + dx, y + dy
                if self.is_valid(nx, ny, visited):
                    visited.add((nx, ny))
                    queue.append(((nx, ny), path + [(nx, ny)]))
        
        return None
    
    def dfs(self, start: Tuple[int, int], end: Tuple[int, int]) -> Optional[List[Tuple[int, int]]]:
        """Depth-First Search"""
        stack = [(start, [start])]
        visited = {start}
        
        while stack:
            (x, y), path = stack.pop()
            
            if (x, y) == end:
                return path
            
            for dx, dy in self.directions:
                nx, ny = x + dx, y + dy
                if self.is_valid(nx, ny, visited):
                    visited.add((nx, ny))
                    stack.append(((nx, ny), path + [(nx, ny)]))
        
        return None
    
    def heuristic(self, pos: Tuple[int, int], goal: Tuple[int, int]) -> int:
        """Manhattan distance heuristic"""
        return abs(pos[0] - goal[0]) + abs(pos[1] - goal[1])
    
    def astar(self, start: Tuple[int, int], end: Tuple[int, int]) -> Optional[List[Tuple[int, int]]]:
        """A* Search"""
        heap = [(0, start, [start], 0)]
        visited = {start}
        
        while heap:
            f, (x, y), path, g = heapq.heappop(heap)
            
            if (x, y) == end:
                return path
            
            for dx, dy in self.directions:
                nx, ny = x + dx, y + dy
                if self.is_valid(nx, ny, visited):
                    visited.add((nx, ny))
                    new_g = g + 1
                    new_h = self.heuristic((nx, ny), end)
                    new_f = new_g + new_h
                    heapq.heappush(heap, (new_f, (nx, ny), path + [(nx, ny)], new_g))
        
        return None

def print_maze_with_path(maze: List[List[int]], path: List[Tuple[int, int]] = None):
    """In mê cung với đường đi"""
    path_set = set(path) if path else set()
    for i, row in enumerate(maze):
        for j, cell in enumerate(row):
            if (i, j) in path_set:
                if (i, j) == path[0]:
                    print("S", end=" ")  # Start
                elif (i, j) == path[-1]:
                    print("E", end=" ")  # End
                else:
                    print("*", end=" ")  # Path
            elif cell == 1:
                print("█", end=" ")  # Wall
            else:
                print(".", end=" ")  # Empty
        print()

if __name__ == "__main__":
    # Mê cung: 0 = đường đi, 1 = tường
    maze = [
        [0, 1, 0, 0, 0],
        [0, 1, 0, 1, 0],
        [0, 0, 0, 1, 0],
        [1, 1, 0, 0, 0],
        [0, 0, 0, 1, 0]
    ]
    
    start = (0, 0)
    end = (4, 4)
    
    solver = MazeSolver(maze)
    
    print("Mê cung ban đầu:")
    print_maze_with_path(maze)
    
    print("\n=== BFS ===")
    path_bfs = solver.bfs(start, end)
    if path_bfs:
        print(f"Tìm thấy đường đi ({len(path_bfs)} bước):")
        print_maze_with_path(maze, path_bfs)
    
    print("\n=== DFS ===")
    path_dfs = solver.dfs(start, end)
    if path_dfs:
        print(f"Tìm thấy đường đi ({len(path_dfs)} bước):")
        print_maze_with_path(maze, path_dfs)
    
    print("\n=== A* ===")
    path_astar = solver.astar(start, end)
    if path_astar:
        print(f"Tìm thấy đường đi ({len(path_astar)} bước):")
        print_maze_with_path(maze, path_astar)
