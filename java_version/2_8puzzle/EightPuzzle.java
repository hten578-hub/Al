/**
 * Giải bài toán 8-Puzzle bằng thuật toán A* với heuristic Manhattan Distance
 * Java version
 */
import java.util.*;

class PuzzleState implements Comparable<PuzzleState> {
    private int[][] board;
    private PuzzleState parent;
    private String move;
    private int g; // Chi phí từ trạng thái đầu
    private int h; // Heuristic
    private int f; // Tổng chi phí
    
    public PuzzleState(int[][] board, PuzzleState parent, String move, int g) {
        this.board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 3);
        }
        this.parent = parent;
        this.move = move;
        this.g = g;
        this.h = manhattanDistance();
        this.f = this.g + this.h;
    }
    
    public int manhattanDistance() {
        int distance = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != 0) {
                    int targetX = (board[i][j] - 1) / 3;
                    int targetY = (board[i][j] - 1) % 3;
                    distance += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }
        }
        return distance;
    }
    
    public int[] findBlank() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }
    
    public List<PuzzleState> getNeighbors() {
        List<PuzzleState> neighbors = new ArrayList<>();
        int[] blank = findBlank();
        int x = blank[0], y = blank[1];
        
        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        // Hướng ngược lại vì ô trống di chuyển nhưng ta mô tả ô số di chuyển
        String[] directions = {"Xuống", "Lên", "Phải", "Trái"};
        
        for (int i = 0; i < moves.length; i++) {
            int nx = x + moves[i][0];
            int ny = y + moves[i][1];
            
            if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                int[][] newBoard = new int[3][3];
                for (int row = 0; row < 3; row++) {
                    System.arraycopy(board[row], 0, newBoard[row], 0, 3);
                }
                
                // Lưu số ô sẽ di chuyển
                int movedTile = board[nx][ny];
                
                // Hoán đổi
                newBoard[x][y] = newBoard[nx][ny];
                newBoard[nx][ny] = 0;
                
                String moveDescription = "Di chuyển ô " + movedTile + " " + directions[i];
                neighbors.add(new PuzzleState(newBoard, this, moveDescription, g + 1));
            }
        }
        
        return neighbors;
    }
    
    public boolean isGoal() {
        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        return Arrays.deepEquals(board, goal);
    }
    
    @Override
    public int compareTo(PuzzleState other) {
        return Integer.compare(this.f, other.f);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PuzzleState that = (PuzzleState) obj;
        return Arrays.deepEquals(board, that.board);
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
    
    // Getters
    public int[][] getBoard() { return board; }
    public PuzzleState getParent() { return parent; }
    public String getMove() { return move; }
    public int getF() { return f; }
}

public class EightPuzzle {
    
    public static List<String> solve8Puzzle(int[][] initialBoard) {
        PuzzleState start = new PuzzleState(initialBoard, null, "", 0);
        
        if (start.isGoal()) {
            return new ArrayList<>();
        }
        
        PriorityQueue<PuzzleState> openSet = new PriorityQueue<>();
        Set<PuzzleState> closedSet = new HashSet<>();
        
        openSet.add(start);
        
        while (!openSet.isEmpty()) {
            PuzzleState current = openSet.poll();
            
            if (current.isGoal()) {
                List<String> path = new ArrayList<>();
                while (current.getParent() != null) {
                    path.add(0, current.getMove());
                    current = current.getParent();
                }
                return path;
            }
            
            closedSet.add(current);
            
            for (PuzzleState neighbor : current.getNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                openSet.add(neighbor);
            }
        }
        
        return null; // Không tìm thấy lời giải
    }
    
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print((cell == 0 ? "_" : cell) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static int[][] inputPuzzle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhập trạng thái 8-Puzzle (dùng 0 cho ô trống)");
        System.out.println("Nhập 9 số cách nhau bởi dấu cách (ví dụ: 1 2 3 4 0 6 7 5 8)");
        System.out.println("Hoặc nhập từng hàng:");
        
        try {
            System.out.print("\nNhập 1 dòng (1) hay 3 dòng (3)? [1/3]: ");
            String choice = scanner.nextLine().trim();
            
            int[][] board = new int[3][3];
            
            if ("1".equals(choice)) {
                System.out.print("Nhập 9 số: ");
                String[] numbers = scanner.nextLine().trim().split("\\s+");
                if (numbers.length != 9) {
                    System.out.println("Phải nhập đúng 9 số!");
                    return null;
                }
                
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        board[i][j] = Integer.parseInt(numbers[i * 3 + j]);
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    System.out.print("Nhập hàng " + (i + 1) + " (3 số cách nhau bởi dấu cách): ");
                    String[] row = scanner.nextLine().trim().split("\\s+");
                    if (row.length != 3) {
                        System.out.println("Mỗi hàng phải có 3 số!");
                        return null;
                    }
                    for (int j = 0; j < 3; j++) {
                        board[i][j] = Integer.parseInt(row[j]);
                    }
                }
            }
            
            // Kiểm tra hợp lệ
            boolean[] used = new boolean[9];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int num = board[i][j];
                    if (num < 0 || num > 8 || used[num]) {
                        System.out.println("Phải có đủ các số từ 0-8, mỗi số xuất hiện đúng 1 lần!");
                        return null;
                    }
                    used[num] = true;
                }
            }
            
            return board;
            
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Vui lòng nhập số hợp lệ!");
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== GIẢI 8-PUZZLE BẰNG A* ===\n");
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chọn chế độ:");
        System.out.println("1. Dùng ví dụ có sẵn");
        System.out.println("2. Nhập trạng thái tùy chỉnh");
        
        System.out.print("\nChọn [1/2]: ");
        String mode = scanner.nextLine().trim();
        
        int[][] initial;
        
        if ("2".equals(mode)) {
            initial = inputPuzzle();
            if (initial == null) {
                System.out.println("Sử dụng ví dụ mặc định...");
                initial = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 5, 8}};
            }
        } else {
            // Trạng thái ban đầu mặc định
            initial = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 5, 8}};
        }
        
        System.out.println("\nTrạng thái ban đầu:");
        printBoard(initial);
        
        System.out.println("Đang tìm lời giải...");
        List<String> solution = solve8Puzzle(initial);
        
        if (solution != null) {
            System.out.println("\n✓ Tìm thấy lời giải với " + solution.size() + " bước:");
            System.out.println(String.join(" -> ", solution));
        } else {
            System.out.println("\n✗ Không tìm thấy lời giải!");
        }
        
        scanner.close();
    }
}