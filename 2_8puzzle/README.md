# Giải 8-Puzzle bằng A* với Manhattan Heuristic

## Mô tả
Bài toán 8-Puzzle là trò chơi xếp số từ 1-8 trên bảng 3x3, với một ô trống. Mục tiêu là sắp xếp các số theo thứ tự từ trạng thái ban đầu.

## Thuật toán A*
- Sử dụng heuristic Manhattan Distance
- f(n) = g(n) + h(n)
  - g(n): Chi phí từ trạng thái đầu
  - h(n): Ước lượng chi phí đến đích (Manhattan)

## Chạy chương trình
```bash
python 8puzzle_astar.py
```
