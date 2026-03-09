# Bài tập Trí tuệ Nhân tạo - Java Version

Dự án này bao gồm các bài toán AI cơ bản được viết bằng Java, được tổ chức theo thư mục từ 1-7:

## Cấu trúc thư mục

```
java_version/
├── 1_game_ai/
│   └── TicTacToe.java
├── 2_8puzzle/
│   └── EightPuzzle.java
├── 3_tsp/
│   └── TSPSimulatedAnnealing.java
├── 4_maze/
│   └── MazeSolver.java
├── 5_expert_system/
│   └── ExpertSystem.java
├── 6_family_tree/
│   └── FamilyTree.java
└── 7_spam_detection/
    └── SpamDetection.java
```

## Danh sách bài tập

1. **1_game_ai/TicTacToe.java** - Trí tuệ nhân tạo trong trò chơi (Minimax + Alpha-Beta)
2. **2_8puzzle/EightPuzzle.java** - Giải 8-Puzzle bằng A* (Manhattan Heuristic)
3. **3_tsp/TSPSimulatedAnnealing.java** - Bài toán Người du lịch với Simulated Annealing
4. **4_maze/MazeSolver.java** - Giải mê cung với BFS, DFS và A*
5. **5_expert_system/ExpertSystem.java** - Hệ chuyên gia chẩn đoán bệnh dựa trên luật logic
6. **6_family_tree/FamilyTree.java** - Hệ suy luận cây gia phả bằng First-Order Logic
7. **7_spam_detection/SpamDetection.java** - Phát hiện thư rác bằng Naive Bayes

## Yêu cầu hệ thống

- Java 8 hoặc cao hơn
- IDE: IntelliJ IDEA, Eclipse, hoặc VS Code với Java Extension

## Cách chạy

### 1. Biên dịch và chạy từng file:

```bash
# Bài 1: Tic Tac Toe
cd java_version/1_game_ai
javac TicTacToe.java
java TicTacToe

# Bài 2: 8-Puzzle
cd java_version/2_8puzzle
javac EightPuzzle.java
java EightPuzzle

# Bài 3: TSP
cd java_version/3_tsp
javac TSPSimulatedAnnealing.java
java TSPSimulatedAnnealing

# Bài 4: Maze Solver
cd java_version/4_maze
javac MazeSolver.java
java MazeSolver

# Bài 5: Expert System
cd java_version/5_expert_system
javac ExpertSystem.java
java ExpertSystem

# Bài 6: Family Tree
cd java_version/6_family_tree
javac FamilyTree.java
java FamilyTree

# Bài 7: Spam Detection
cd java_version/7_spam_detection
javac SpamDetection.java
java SpamDetection
```
```

### 2. Hoặc sử dụng IDE:
- Mở thư mục `java_version` trong IDE
- Navigate đến thư mục con tương ứng (1_game_ai, 2_8puzzle, etc.)
- Chạy từng file Java bằng cách nhấn Run

## Tính năng chính

### 1. 1_game_ai/TicTacToe.java
- AI sử dụng thuật toán Minimax với Alpha-Beta Pruning
- Không bao giờ thua
- Giao diện console tương tác

### 2. 2_8puzzle/EightPuzzle.java
- Thuật toán A* với heuristic Manhattan Distance
- Hỗ trợ nhập trạng thái tùy chỉnh
- Tìm lời giải tối ưu

### 3. 3_tsp/TSPSimulatedAnnealing.java
- Giải bài toán TSP bằng Simulated Annealing
- Hiển thị tiến trình tối ưu hóa
- Tham số có thể điều chỉnh

### 4. 4_maze/MazeSolver.java
- So sánh 3 thuật toán: BFS, DFS, A*
- Hiển thị đường đi trên mê cung
- Đo thời gian thực hiện

### 5. 5_expert_system/ExpertSystem.java
- Forward Chaining
- Chẩn đoán bệnh từ triệu chứng
- Hiển thị độ tin cậy

### 6. 6_family_tree/FamilyTree.java
- First-Order Logic
- Suy luận các quan hệ gia đình
- Hiển thị cây gia phả trực quan

### 7. 7_spam_detection/SpamDetection.java
- Naive Bayes Classifier
- Laplace Smoothing
- Đánh giá độ chính xác
- Test tương tác

## Cấu trúc code

Mỗi file Java được tổ chức theo cấu trúc:
- **Class chính**: Chứa logic thuật toán
- **Class hỗ trợ**: Các cấu trúc dữ liệu cần thiết
- **Main method**: Demo và test chương trình

## Ví dụ chạy

```bash
$ cd java_version/1_game_ai
$ javac TicTacToe.java
$ java TicTacToe
=== TIC TAC TOE - NGƯỜI vs AI ===
Bạn là X, AI là O

  0 1 2
0       
1       
2       

Lượt của bạn (X)
Nhập hàng (0-2): 1
Nhập cột (0-2): 1
```

## Lưu ý

- Tất cả chương trình đều có giao diện console
- Code được comment bằng tiếng Việt để dễ hiểu
- Có xử lý lỗi input cơ bản
- Hiển thị kết quả chi tiết

## Tác giả

[Tên sinh viên] - [Mã số sinh viên]
# Expert System
cd java_version/5_expert_system
javac ExpertSystem.java
java ExpertSystem

# Family Tree
cd java_version/6_family_tree
javac FamilyTree.java
java FamilyTree

# Spam Detection
cd java_version/7_spam_detection
javac SpamDetection.java
java SpamDetection