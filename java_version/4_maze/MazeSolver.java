/**
 * Giải mê cung bằng BFS, DFS và A*
 * Java version
 */
import java.util.*;

class Position {
    private int x, y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class AStarNode implements Comparable<AStarNode> {
    private Position position;
    private List<Position> path;
    private int g; // Chi phí từ điểm bắt đầu
    private int h; // Heuristic
    private int f; // Tổng chi phí
    
    public AStarNode(Position position, List<Position> path, int g, int h) {
        this.position = position;
        this.path = new ArrayList<>(path);
        this.g = g;
        this.h = h;
        this.f = g + h;
    }
    
    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.f, other.f);
    }
    
    // Getters
    public Position getPosition() { return position; }
    public List<Position> getPath() { return path; }
    public int getG() { return g; }
    public int getF() { return f; }
}

public class MazeSolver {
    private int[][] maze;
    private int rows, cols;
    private int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Phải, Xuống, Trái, Lên
    
    public MazeSolver(int[][] maze) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
    }
    
    private boolean isValid(int x, int y, Set<Position> visited) {
        return x >= 0 && x < rows && y >= 0 && y < cols && 
               maze[x][y] == 0 && !visited.contains(new Position(x, y));
    }
    
    /**
     * Breadth-First Search
     */
    public List<Position> bfs(Position start, Position end) {
        Queue<List<Position>> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();
        
        List<Position> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.offer(initialPath);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            List<Position> path = queue.poll();
            Position current = path.get(path.size() - 1);
            
            if (current.equals(end)) {
                return path;
            }
            
            for (int[] dir : directions) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];
                Position next = new Position(nx, ny);
                
                if (isValid(nx, ny, visited)) {
                    visited.add(next);
                    List<Position> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    queue.offer(newPath);
                }
            }
        }
        
        return null; // Không tìm thấy đường đi
    }
    
    /**
     * Depth-First Search
     */
    public List<Position> dfs(Position start, Position end) {
        Stack<List<Position>> stack = new Stack<>();
        Set<Position> visited = new HashSet<>();
        
        List<Position> initialPath = new ArrayList<>();
        initialPath.add(start);
        stack.push(initialPath);
        visited.add(start);
        
        while (!stack.isEmpty()) {
            List<Position> path = stack.pop();
            Position current = path.get(path.size() - 1);
            
            if (current.equals(end)) {
                return path;
            }
            
            for (int[] dir : directions) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];
                Position next = new Position(nx, ny);
                
                if (isValid(nx, ny, visited)) {
                    visited.add(next);
                    List<Position> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    stack.push(newPath);
                }
            }
        }
        
        return null; // Không tìm thấy đường đi
    }
    
    private int heuristic(Position pos, Position goal) {
        return Math.abs(pos.getX() - goal.getX()) + Math.abs(pos.getY() - goal.getY());
    }
    
    /**
     * A* Search
     */
    public List<Position> aStar(Position start, Position end) {
        PriorityQueue<AStarNode> heap = new PriorityQueue<>();
        Set<Position> visited = new HashSet<>();
        
        List<Position> initialPath = new ArrayList<>();
        initialPath.add(start);
        heap.offer(new AStarNode(start, initialPath, 0, heuristic(start, end)));
        visited.add(start);
        
        while (!heap.isEmpty()) {
            AStarNode current = heap.poll();
            Position pos = current.getPosition();
            
            if (pos.equals(end)) {
                return current.getPath();
            }
            
            for (int[] dir : directions) {
                int nx = pos.getX() + dir[0];
                int ny = pos.getY() + dir[1];
                Position next = new Position(nx, ny);
                
                if (isValid(nx, ny, visited)) {
                    visited.add(next);
                    List<Position> newPath = new ArrayList<>(current.getPath());
                    newPath.add(next);
                    int newG = current.getG() + 1;
                    int newH = heuristic(next, end);
                    heap.offer(new AStarNode(next, newPath, newG, newH));
                }
            }
        }
        
        return null; // Không tìm thấy đường đi
    }
    
    public void printMazeWithPath(List<Position> path) {
        Set<Position> pathSet = new HashSet<>();
        if (path != null) {
            pathSet.addAll(path);
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position current = new Position(i, j);
                if (path != null && pathSet.contains(current)) {
                    if (current.equals(path.get(0))) {
                        System.out.print("S "); // Start
                    } else if (current.equals(path.get(path.size() - 1))) {
                        System.out.print("E "); // End
                    } else {
                        System.out.print("* "); // Path
                    }
                } else if (maze[i][j] == 1) {
                    System.out.print("█ "); // Wall
                } else {
                    System.out.print(". "); // Empty
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static int[][] generateRandomMaze(int rows, int cols, Position start, Position end) {
        Random random = new Random();
        int[][] maze = new int[rows][cols];
        
        // Bước 1: Tạo tường ngẫu nhiên nhưng ít hơn
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i != start.getX() || j != start.getY()) && 
                    (i != end.getX() || j != end.getY())) {
                    maze[i][j] = random.nextDouble() < 0.25 ? 1 : 0; // 25% tường
                }
            }
        }
        
        // Bước 2: Đảm bảo có đường đi
        MazeSolver tempSolver = new MazeSolver(maze);
        if (tempSolver.bfs(start, end) == null) {
            createSimplePath(maze, start, end);
        }
        
        return maze;
    }
    
    private static void createSimplePath(int[][] maze, Position start, Position end) {
        // Tạo đường đi đơn giản từ start đến end
        int x = start.getX();
        int y = start.getY();
        
        // Di chuyển theo hàng trước
        while (x != end.getX()) {
            maze[x][y] = 0;
            x += (end.getX() > x) ? 1 : -1;
        }
        
        // Sau đó di chuyển theo cột
        while (y != end.getY()) {
            maze[x][y] = 0;
            y += (end.getY() > y) ? 1 : -1;
        }
        
        maze[end.getX()][end.getY()] = 0;
    }
    
    public static void main(String[] args) {
        Position start = new Position(0, 0);
        Position end = new Position(4, 4);
        
        System.out.println("=== GIẢI MÊ CUNG ===");
        System.out.println("\nChọn chế độ:");
        System.out.println("1. Dùng mê cung có sẵn");
        System.out.println("2. Tạo mê cung ngẫu nhiên");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nChọn [1/2]: ");
        String choice = scanner.nextLine().trim();
        
        int[][] maze;
        
        if ("2".equals(choice)) {
            System.out.print("Nhập kích thước (ví dụ: 10 cho mê cung 10x10): ");
            try {
                int size = Integer.parseInt(scanner.nextLine().trim());
                if (size < 5 || size > 50) {
                    System.out.println("Kích thước phải từ 5 đến 50. Sử dụng 10.");
                    size = 10;
                }
                end = new Position(size - 1, size - 1);
                maze = generateRandomMaze(size, size, start, end);
                System.out.println("\nĐã tạo mê cung ngẫu nhiên " + size + "x" + size);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi! Sử dụng mê cung mặc định.");
                maze = new int[][]{
                    {0, 1, 0, 0, 0},
                    {0, 1, 0, 1, 0},
                    {0, 0, 0, 1, 0},
                    {1, 1, 0, 0, 0},
                    {0, 0, 0, 1, 0}
                };
                end = new Position(4, 4);
            }
        } else {
            // Mê cung mặc định: 0 = đường đi, 1 = tường
            maze = new int[][]{
                {0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 1, 0}
            };
        }
        
        MazeSolver solver = new MazeSolver(maze);
        
        System.out.println("\nMê cung:");
        solver.printMazeWithPath(null);
        
        System.out.println("Điểm bắt đầu: " + start);
        System.out.println("Điểm kết thúc: " + end);
        
        // BFS
        System.out.println("\n=== BFS (Breadth-First Search) ===");
        long startTime = System.currentTimeMillis();
        List<Position> pathBFS = solver.bfs(start, end);
        long endTime = System.currentTimeMillis();
        
        if (pathBFS != null) {
            System.out.println("Tìm thấy đường đi (" + pathBFS.size() + " bước):");
            solver.printMazeWithPath(pathBFS);
            System.out.println("Thời gian: " + (endTime - startTime) + " ms");
        } else {
            System.out.println("Không tìm thấy đường đi!");
        }
        
        // DFS
        System.out.println("\n=== DFS (Depth-First Search) ===");
        startTime = System.currentTimeMillis();
        List<Position> pathDFS = solver.dfs(start, end);
        endTime = System.currentTimeMillis();
        
        if (pathDFS != null) {
            System.out.println("Tìm thấy đường đi (" + pathDFS.size() + " bước):");
            solver.printMazeWithPath(pathDFS);
            System.out.println("Thời gian: " + (endTime - startTime) + " ms");
        } else {
            System.out.println("Không tìm thấy đường đi!");
        }
        
        // A*
        System.out.println("\n=== A* (A-Star Search) ===");
        startTime = System.currentTimeMillis();
        List<Position> pathAStar = solver.aStar(start, end);
        endTime = System.currentTimeMillis();
        
        if (pathAStar != null) {
            System.out.println("Tìm thấy đường đi (" + pathAStar.size() + " bước):");
            solver.printMazeWithPath(pathAStar);
            System.out.println("Thời gian: " + (endTime - startTime) + " ms");
        } else {
            System.out.println("Không tìm thấy đường đi!");
        }
        
        // So sánh kết quả
        System.out.println("\n=== SO SÁNH KẾT QUẢ ===");
        if (pathBFS != null) System.out.println("BFS: " + pathBFS.size() + " bước");
        if (pathDFS != null) System.out.println("DFS: " + pathDFS.size() + " bước");
        if (pathAStar != null) System.out.println("A*: " + pathAStar.size() + " bước");
        
        scanner.close();
    }
}