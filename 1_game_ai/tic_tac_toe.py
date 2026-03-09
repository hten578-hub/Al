"""
Trí tuệ nhân tạo trong trò chơi - Tic Tac Toe với Minimax
"""
import math
from typing import List, Tuple, Optional

class TicTacToe:
    def __init__(self):
        self.board = [[' ' for _ in range(3)] for _ in range(3)]
        self.current_player = 'X'
        
    def print_board(self):
        """In bảng game"""
        print("\n  0 1 2")
        for i, row in enumerate(self.board):
            print(f"{i} {' '.join(row)}")
        print()
    
    def is_valid_move(self, row: int, col: int) -> bool:
        """Kiểm tra nước đi hợp lệ"""
        return 0 <= row < 3 and 0 <= col < 3 and self.board[row][col] == ' '
    
    def make_move(self, row: int, col: int, player: str) -> bool:
        """Thực hiện nước đi"""
        if self.is_valid_move(row, col):
            self.board[row][col] = player
            return True
        return False
    
    def check_winner(self) -> Optional[str]:
        """Kiểm tra người thắng"""
        # Kiểm tra hàng
        for row in self.board:
            if row[0] == row[1] == row[2] != ' ':
                return row[0]
        
        # Kiểm tra cột
        for col in range(3):
            if self.board[0][col] == self.board[1][col] == self.board[2][col] != ' ':
                return self.board[0][col]
        
        # Kiểm tra đường chéo
        if self.board[0][0] == self.board[1][1] == self.board[2][2] != ' ':
            return self.board[0][0]
        if self.board[0][2] == self.board[1][1] == self.board[2][0] != ' ':
            return self.board[0][2]
        
        return None
    
    def is_board_full(self) -> bool:
        """Kiểm tra bảng đầy"""
        return all(cell != ' ' for row in self.board for cell in row)
    
    def get_empty_cells(self) -> List[Tuple[int, int]]:
        """Lấy các ô trống"""
        return [(i, j) for i in range(3) for j in range(3) if self.board[i][j] == ' ']
    
    def minimax(self, depth: int, is_maximizing: bool, alpha: float, beta: float) -> int:
        """
        Thuật toán Minimax với Alpha-Beta Pruning
        """
        winner = self.check_winner()
        
        # Trạng thái kết thúc
        if winner == 'O':  # AI thắng
            return 10 - depth
        elif winner == 'X':  # Người chơi thắng
            return depth - 10
        elif self.is_board_full():  # Hòa
            return 0
        
        if is_maximizing:  # Lượt AI (O)
            max_eval = -math.inf
            for row, col in self.get_empty_cells():
                self.board[row][col] = 'O'
                eval_score = self.minimax(depth + 1, False, alpha, beta)
                self.board[row][col] = ' '
                max_eval = max(max_eval, eval_score)
                alpha = max(alpha, eval_score)
                if beta <= alpha:
                    break
            return max_eval
        else:  # Lượt người chơi (X)
            min_eval = math.inf
            for row, col in self.get_empty_cells():
                self.board[row][col] = 'X'
                eval_score = self.minimax(depth + 1, True, alpha, beta)
                self.board[row][col] = ' '
                min_eval = min(min_eval, eval_score)
                beta = min(beta, eval_score)
                if beta <= alpha:
                    break
            return min_eval
    
    def get_best_move(self) -> Tuple[int, int]:
        """Tìm nước đi tốt nhất cho AI"""
        best_score = -math.inf
        best_move = None
        
        for row, col in self.get_empty_cells():
            self.board[row][col] = 'O'
            score = self.minimax(0, False, -math.inf, math.inf)
            self.board[row][col] = ' '
            
            if score > best_score:
                best_score = score
                best_move = (row, col)
        
        return best_move

def play_game():
    """Chơi game"""
    game = TicTacToe()
    print("=== TIC TAC TOE - NGƯỜI vs AI ===")
    print("Bạn là X, AI là O")
    
    while True:
        game.print_board()
        
        # Lượt người chơi
        if game.current_player == 'X':
            print("Lượt của bạn (X)")
            try:
                row = int(input("Nhập hàng (0-2): "))
                col = int(input("Nhập cột (0-2): "))
                
                if game.make_move(row, col, 'X'):
                    game.current_player = 'O'
                else:
                    print("Nước đi không hợp lệ!")
                    continue
            except ValueError:
                print("Vui lòng nhập số!")
                continue
        
        # Lượt AI
        else:
            print("Lượt của AI (O)...")
            row, col = game.get_best_move()
            game.make_move(row, col, 'O')
            print(f"AI đi: ({row}, {col})")
            game.current_player = 'X'
        
        # Kiểm tra kết thúc
        winner = game.check_winner()
        if winner:
            game.print_board()
            if winner == 'X':
                print("🎉 Bạn thắng!")
            else:
                print("🤖 AI thắng!")
            break
        
        if game.is_board_full():
            game.print_board()
            print("🤝 Hòa!")
            break

if __name__ == "__main__":
    play_game()
