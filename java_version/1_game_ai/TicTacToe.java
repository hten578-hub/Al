/**
 * Trí tuệ nhân tạo trong trò chơi - Tic Tac Toe với Minimax
 * Java version
 */
import java.util.*;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private Scanner scanner;
    
    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
        scanner = new Scanner(System.in);
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }
    
    public void printBoard() {
        System.out.println("\n  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }
    
    public boolean makeMove(int row, int col, char player) {
        if (isValidMove(row, col)) {
            board[row][col] = player;
            return true;
        }
        return false;
    }
    
    public char checkWinner() {
        // Kiểm tra hàng
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && 
                board[row][1] == board[row][2] && 
                board[row][0] != ' ') {
                return board[row][0];
            }
        }
        
        // Kiểm tra cột
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && 
                board[1][col] == board[2][col] && 
                board[0][col] != ' ') {
                return board[0][col];
            }
        }
        
        // Kiểm tra đường chéo
        if (board[0][0] == board[1][1] && 
            board[1][1] == board[2][2] && 
            board[0][0] != ' ') {
            return board[0][0];
        }
        
        if (board[0][2] == board[1][1] && 
            board[1][1] == board[2][0] && 
            board[0][2] != ' ') {
            return board[0][2];
        }
        
        return ' '; // Không có người thắng
    }
    
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<int[]> getEmptyCells() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }
    
    /**
     * Thuật toán Minimax với Alpha-Beta Pruning
     */
    public int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        char winner = checkWinner();
        
        // Trạng thái kết thúc
        if (winner == 'O') { // AI thắng
            return 10 - depth;
        } else if (winner == 'X') { // Người chơi thắng
            return depth - 10;
        } else if (isBoardFull()) { // Hòa
            return 0;
        }
        
        if (isMaximizing) { // Lượt AI (O)
            int maxEval = Integer.MIN_VALUE;
            for (int[] cell : getEmptyCells()) {
                int row = cell[0], col = cell[1];
                board[row][col] = 'O';
                int eval = minimax(depth + 1, false, alpha, beta);
                board[row][col] = ' ';
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Alpha-Beta pruning
                }
            }
            return maxEval;
        } else { // Lượt người chơi (X)
            int minEval = Integer.MAX_VALUE;
            for (int[] cell : getEmptyCells()) {
                int row = cell[0], col = cell[1];
                board[row][col] = 'X';
                int eval = minimax(depth + 1, true, alpha, beta);
                board[row][col] = ' ';
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha-Beta pruning
                }
            }
            return minEval;
        }
    }
    
    public int[] getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        
        for (int[] cell : getEmptyCells()) {
            int row = cell[0], col = cell[1];
            board[row][col] = 'O';
            int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            board[row][col] = ' ';
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = new int[]{row, col};
            }
        }
        
        return bestMove;
    }
    
    public void playGame() {
        System.out.println("=== TIC TAC TOE - NGƯỜI vs AI ===");
        System.out.println("Bạn là X, AI là O");
        
        while (true) {
            printBoard();
            
            // Lượt người chơi
            if (currentPlayer == 'X') {
                System.out.println("Lượt của bạn (X)");
                try {
                    System.out.print("Nhập hàng (0-2): ");
                    int row = scanner.nextInt();
                    System.out.print("Nhập cột (0-2): ");
                    int col = scanner.nextInt();
                    
                    if (makeMove(row, col, 'X')) {
                        currentPlayer = 'O';
                    } else {
                        System.out.println("Nước đi không hợp lệ!");
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Vui lòng nhập số!");
                    scanner.next(); // Clear invalid input
                    continue;
                }
            }
            // Lượt AI
            else {
                System.out.println("Lượt của AI (O)...");
                int[] bestMove = getBestMove();
                if (bestMove != null) {
                    makeMove(bestMove[0], bestMove[1], 'O');
                    System.out.println("AI đi: (" + bestMove[0] + ", " + bestMove[1] + ")");
                }
                currentPlayer = 'X';
            }
            
            // Kiểm tra kết thúc
            char winner = checkWinner();
            if (winner != ' ') {
                printBoard();
                if (winner == 'X') {
                    System.out.println("🎉 Bạn thắng!");
                } else {
                    System.out.println("🤖 AI thắng!");
                }
                break;
            }
            
            if (isBoardFull()) {
                printBoard();
                System.out.println("🤝 Hòa!");
                break;
            }
        }
        
        scanner.close();
    }
    
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playGame();
    }
}