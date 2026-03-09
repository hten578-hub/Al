# Bài tập Trí tuệ Nhân tạo - Web Version

Giao diện web tương tác cho các bài toán AI

## 🌐 Danh sách Web Apps

1. **1_game_ai/index.html** - Tic Tac Toe với AI Minimax
2. **2_8puzzle/index.html** - 8-Puzzle Solver với A*
3. **3_tsp/index.html** - TSP Visualization (đang phát triển)
4. **4_maze/index.html** - Maze Solver (đang phát triển)
5. **5_expert_system/index.html** - Expert System (đang phát triển)
6. **6_family_tree/index.html** - Family Tree (đang phát triển)
7. **7_spam_detection/index.html** - Spam Detector (đang phát triển)

## 🚀 Cách chạy

### Cách 1: Mở trực tiếp
```bash
# Mở file HTML bằng trình duyệt
# Windows
start web_version/1_game_ai/index.html

# Mac
open web_version/1_game_ai/index.html

# Linux
xdg-open web_version/1_game_ai/index.html
```

### Cách 2: Dùng Live Server (Khuyến nghị)
```bash
# Cài đặt Live Server (nếu chưa có)
npm install -g live-server

# Chạy server
cd web_version
live-server
```

### Cách 3: Dùng Python HTTP Server
```bash
cd web_version
python -m http.server 8000
# Mở http://localhost:8000
```

## ✨ Tính năng

### 1. Tic Tac Toe (1_game_ai)
- ✅ Chơi với AI không thể thắng
- ✅ Minimax với Alpha-Beta Pruning
- ✅ Giao diện đẹp, responsive
- ✅ Hiệu ứng animation

### 2. 8-Puzzle (2_8puzzle)
- ✅ Chơi thủ công hoặc tự động giải
- ✅ Thuật toán A* với Manhattan Heuristic
- ✅ Hiển thị số bước và heuristic
- ✅ Xáo trộn và reset
- ✅ Hiển thị lời giải từng bước

### 3-7. Các bài còn lại
- ⏳ Đang phát triển

## 🎨 Đặc điểm

### Giao diện
- **Responsive**: Hoạt động tốt trên mọi thiết bị
- **Modern UI**: Gradient, shadow, animation
- **User-friendly**: Dễ sử dụng, trực quan

### Công nghệ
- **HTML5**: Cấu trúc semantic
- **CSS3**: Flexbox, Grid, Animation
- **Vanilla JavaScript**: Không cần framework
- **No dependencies**: Chạy offline được

## 📱 Tương thích

- ✅ Chrome/Edge (Khuyến nghị)
- ✅ Firefox
- ✅ Safari
- ✅ Mobile browsers

## 🎯 Demo cho giảng viên

### Ưu điểm của Web Version:
1. **Dễ demo**: Chỉ cần mở trình duyệt
2. **Trực quan**: Giao diện đẹp, dễ hiểu
3. **Tương tác**: Người xem có thể thử ngay
4. **Không cần cài đặt**: Chạy mọi nơi có browser
5. **Chuyên nghiệp**: Ấn tượng với giảng viên

### Cách demo:
1. Mở file HTML trước khi trình bày
2. Giải thích thuật toán
3. Demo trực tiếp trên web
4. Cho giảng viên/bạn bè thử chơi

## 📝 Lưu ý

- File HTML độc lập, không cần server
- Code JavaScript nhúng trong HTML
- Có thể chạy offline
- Dễ dàng customize màu sắc, layout

## 🚀 Phát triển tiếp

Để tạo web app cho các bài còn lại:
1. Copy template từ bài 1 hoặc 2
2. Thay đổi logic JavaScript
3. Customize giao diện
4. Test trên nhiều trình duyệt

## 📧 Hỗ trợ

Nếu có lỗi hoặc cần thêm tính năng, liên hệ qua email hoặc GitHub.

---

**Chúc bạn demo thành công! 🎉**