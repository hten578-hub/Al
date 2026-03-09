"""
Giải bài toán 8-Puzzle bằng thuật toán A* với heuristic Manhattan Distance
"""
import heapq
from typing import List, Tuple, Optional

class PuzzleState:
    def __init__(self, board: List[List[int]], parent=None, move="", g=0):
        self.board = board
        self.parent = parent
        self.move = move
        self.g = g  # Chi phí từ trạng thái đầu
        self.h = self.manhattan_distance()  # Heuristic
        self.f = self.g + self.h  # Tổng chi phí
        
    def manhattan_distance(self) -> int:
        """Tính khoảng cách Manhattan"""
        distance = 0
        for i in range(3):
            for j in range(3):
                if self.board[i][j] != 0:
                    target_x = (self.board[i][j] - 1) // 3
                    target_y = (self.board[i][j] - 1) % 3
                    distance += abs(i - target_x) + abs(j - target_y)
        return distance
    
    def find_blank(self) -> Tuple[int, int]:
        """Tìm vị trí ô trống (0)"""
        for i in range(3):
            for j in range(3):
                if self.board[i][j] == 0:
                    return i, j
        return -1, -1
    
    def get_neighbors(self) -> List['PuzzleState']:
        """Lấy các trạng thái kế tiếp"""
        neighbors = []
        x, y = self.find_blank()
        moves = [(-1, 0, "Lên"), (1, 0, "Xuống"), (0, -1, "Trái"), (0, 1, "Phải")]
        
        for dx, dy, direction in moves:
            nx, ny = x + dx, y + dy
            if 0 <= nx < 3 and 0 <= ny < 3:
                new_board = [row[:] for row in self.board]
                new_board[x][y], new_board[nx][ny] = new_board[nx][ny], new_board[x][y]
                neighbors.append(PuzzleState(new_board, self, direction, self.g + 1))
        
        return neighbors
    
    def is_goal(self) -> bool:
        """Kiểm tra trạng thái đích"""
        goal = [[1, 2, 3], [4, 5, 6], [7, 8, 0]]
        return self.board == goal
    
    def __lt__(self, other):
        return self.f < other.f
    
    def __eq__(self, other):
        return self.board == other.board
    
    def __hash__(self):
        return hash(str(self.board))

def solve_8puzzle(initial_board: List[List[int]]) -> Optional[List[str]]:
    """Giải 8-puzzle bằng A*"""
    start = PuzzleState(initial_board)
    
    if start.is_goal():
        return []
    
    open_set = [start]
    closed_set = set()
    
    while open_set:
        current = heapq.heappop(open_set)
        
        if current.is_goal():
            path = []
            while current.parent:
                path.append(current.move)
                current = current.parent
            return path[::-1]
        
        closed_set.add(current)
        
        for neighbor in current.get_neighbors():
            if neighbor in closed_set:
                continue
            
            heapq.heappush(open_set, neighbor)
    
    return None

def print_board(board: List[List[int]]):
    """In bảng puzzle"""
    for row in board:
        print(" ".join(str(x) if x != 0 else "_" for x in row))
    print()

def input_puzzle() -> List[List[int]]:
    """Nhập trạng thái puzzle từ người dùng"""
    print("Nhập trạng thái 8-Puzzle (dùng 0 cho ô trống)")
    print("Nhập 9 số cách nhau bởi dấu cách (ví dụ: 1 2 3 4 0 6 7 5 8)")
    print("Hoặc nhập từng hàng:")
    
    try:
        choice = input("\nNhập 1 dòng (1) hay 3 dòng (3)? [1/3]: ").strip()
        
        if choice == "1":
            numbers = input("Nhập 9 số: ").strip().split()
            if len(numbers) != 9:
                print("Phải nhập đúng 9 số!")
                return None
            
            board = []
            for i in range(3):
                row = [int(numbers[i*3 + j]) for j in range(3)]
                board.append(row)
        else:
            board = []
            for i in range(3):
                row = input(f"Nhập hàng {i+1} (3 số cách nhau bởi dấu cách): ").strip().split()
                if len(row) != 3:
                    print("Mỗi hàng phải có 3 số!")
                    return None
                board.append([int(x) for x in row])
        
        # Kiểm tra hợp lệ
        nums = [num for row in board for num in row]
        if sorted(nums) != list(range(9)):
            print("Phải có đủ các số từ 0-8, mỗi số xuất hiện đúng 1 lần!")
            return None
        
        return board
    
    except ValueError:
        print("Lỗi: Vui lòng nhập số hợp lệ!")
        return None

if __name__ == "__main__":
    print("=== GIẢI 8-PUZZLE BẰNG A* ===\n")
    
    # Cho phép chọn chế độ
    print("Chọn chế độ:")
    print("1. Dùng ví dụ có sẵn")
    print("2. Nhập trạng thái tùy chỉnh")
    
    mode = input("\nChọn [1/2]: ").strip()
    
    if mode == "2":
        initial = input_puzzle()
        if initial is None:
            print("Sử dụng ví dụ mặc định...")
            initial = [
                [1, 2, 3],
                [4, 0, 6],
                [7, 5, 8]
            ]
    else:
        # Trạng thái ban đầu mặc định
        initial = [
            [1, 2, 3],
            [4, 0, 6],
            [7, 5, 8]
        ]
    
    print("\nTrạng thái ban đầu:")
    print_board(initial)
    
    print("Đang tìm lời giải...")
    solution = solve_8puzzle(initial)
    
    if solution:
        print(f"\n✓ Tìm thấy lời giải với {len(solution)} bước:")
        print(" -> ".join(solution))
    else:
        print("\n✗ Không tìm thấy lời giải!")
