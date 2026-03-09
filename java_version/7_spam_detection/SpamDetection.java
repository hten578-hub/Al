/**
 * Phát hiện thư rác bằng Naive Bayes
 * Java version
 */
import java.util.*;
import java.util.regex.Pattern;

class EmailMessage {
    private String text;
    private String label; // "spam" hoặc "ham"
    
    public EmailMessage(String text, String label) {
        this.text = text;
        this.label = label;
    }
    
    public String getText() { return text; }
    public String getLabel() { return label; }
}

class ClassificationResult {
    private String prediction;
    private double confidence;
    
    public ClassificationResult(String prediction, double confidence) {
        this.prediction = prediction;
        this.confidence = confidence;
    }
    
    public String getPrediction() { return prediction; }
    public double getConfidence() { return confidence; }
}

public class SpamDetection {
    private Map<String, Integer> wordCountSpam;
    private Map<String, Integer> wordCountHam;
    private int spamCount;
    private int hamCount;
    private Set<String> vocabulary;
    private Pattern wordPattern;
    
    public SpamDetection() {
        this.wordCountSpam = new HashMap<>();
        this.wordCountHam = new HashMap<>();
        this.spamCount = 0;
        this.hamCount = 0;
        this.vocabulary = new HashSet<>();
        this.wordPattern = Pattern.compile("\\b\\w+\\b");
    }
    
    /**
     * Tiền xử lý văn bản
     */
    public List<String> preprocess(String text) {
        List<String> words = new ArrayList<>();
        String lowerText = text.toLowerCase();
        
        // Tách từ bằng regex
        String[] tokens = lowerText.split("\\W+");
        for (String token : tokens) {
            if (!token.isEmpty() && token.length() > 1) { // Bỏ từ có 1 ký tự
                words.add(token);
            }
        }
        
        return words;
    }
    
    /**
     * Huấn luyện mô hình
     */
    public void train(List<EmailMessage> messages) {
        System.out.println("Đang huấn luyện mô hình...");
        
        for (EmailMessage message : messages) {
            List<String> words = preprocess(message.getText());
            vocabulary.addAll(words);
            
            if ("spam".equals(message.getLabel())) {
                spamCount++;
                for (String word : words) {
                    wordCountSpam.put(word, wordCountSpam.getOrDefault(word, 0) + 1);
                }
            } else {
                hamCount++;
                for (String word : words) {
                    wordCountHam.put(word, wordCountHam.getOrDefault(word, 0) + 1);
                }
            }
        }
        
        System.out.println("Hoàn thành huấn luyện:");
        System.out.println("- Số email spam: " + spamCount);
        System.out.println("- Số email ham: " + hamCount);
        System.out.println("- Kích thước từ vựng: " + vocabulary.size());
    }
    
    /**
     * Tính xác suất log để tránh underflow
     */
    public double[] calculateLogProbability(String text) {
        List<String> words = preprocess(text);
        
        int totalMessages = spamCount + hamCount;
        double pSpam = (double) spamCount / totalMessages;
        double pHam = (double) hamCount / totalMessages;
        
        // Laplace smoothing
        int totalWordsSpam = wordCountSpam.values().stream().mapToInt(Integer::intValue).sum();
        int totalWordsHam = wordCountHam.values().stream().mapToInt(Integer::intValue).sum();
        int vocabSize = vocabulary.size();
        
        double logProbSpam = Math.log(pSpam);
        double logProbHam = Math.log(pHam);
        
        for (String word : words) {
            // P(word|spam) với Laplace smoothing
            double probWordSpam = (double) (wordCountSpam.getOrDefault(word, 0) + 1) / 
                                 (totalWordsSpam + vocabSize);
            double probWordHam = (double) (wordCountHam.getOrDefault(word, 0) + 1) / 
                                (totalWordsHam + vocabSize);
            
            logProbSpam += Math.log(probWordSpam);
            logProbHam += Math.log(probWordHam);
        }
        
        return new double[]{logProbSpam, logProbHam};
    }
    
    /**
     * Dự đoán đơn giản
     */
    public String predict(String text) {
        double[] logProbs = calculateLogProbability(text);
        return logProbs[0] > logProbs[1] ? "spam" : "ham";
    }
    
    /**
     * Dự đoán với độ tin cậy
     */
    public ClassificationResult predictWithConfidence(String text) {
        double[] logProbs = calculateLogProbability(text);
        double logProbSpam = logProbs[0];
        double logProbHam = logProbs[1];
        
        // Chuyển về xác suất thực bằng cách normalize
        double maxLog = Math.max(logProbSpam, logProbHam);
        double probSpam = Math.exp(logProbSpam - maxLog);
        double probHam = Math.exp(logProbHam - maxLog);
        
        double total = probSpam + probHam;
        probSpam /= total;
        probHam /= total;
        
        if (probSpam > probHam) {
            return new ClassificationResult("spam", probSpam);
        } else {
            return new ClassificationResult("ham", probHam);
        }
    }
    
    /**
     * Đánh giá mô hình
     */
    public void evaluate(List<EmailMessage> testMessages) {
        int correct = 0;
        int totalSpam = 0, totalHam = 0;
        int correctSpam = 0, correctHam = 0;
        
        System.out.println("\n=== ĐÁNH GIÁ MÔ HÌNH ===");
        
        for (EmailMessage message : testMessages) {
            String actual = message.getLabel();
            String predicted = predict(message.getText());
            
            if (actual.equals(predicted)) {
                correct++;
                if ("spam".equals(actual)) correctSpam++;
                else correctHam++;
            }
            
            if ("spam".equals(actual)) totalSpam++;
            else totalHam++;
        }
        
        double accuracy = (double) correct / testMessages.size();
        double spamPrecision = totalSpam > 0 ? (double) correctSpam / totalSpam : 0;
        double hamPrecision = totalHam > 0 ? (double) correctHam / totalHam : 0;
        
        System.out.printf("Độ chính xác tổng thể: %.1f%%\n", accuracy * 100);
        System.out.printf("Độ chính xác spam: %.1f%% (%d/%d)\n", 
                         spamPrecision * 100, correctSpam, totalSpam);
        System.out.printf("Độ chính xác ham: %.1f%% (%d/%d)\n", 
                         hamPrecision * 100, correctHam, totalHam);
    }
    
    /**
     * Hiển thị từ quan trọng nhất
     */
    public void showTopWords(int topN) {
        System.out.println("\n=== TỪ QUAN TRỌNG NHẤT ===");
        
        // Tính TF-IDF đơn giản cho spam
        Map<String, Double> spamImportance = new HashMap<>();
        int totalWordsSpam = wordCountSpam.values().stream().mapToInt(Integer::intValue).sum();
        int totalWordsHam = wordCountHam.values().stream().mapToInt(Integer::intValue).sum();
        
        for (String word : vocabulary) {
            int spamFreq = wordCountSpam.getOrDefault(word, 0);
            int hamFreq = wordCountHam.getOrDefault(word, 0);
            
            if (spamFreq > 0) {
                double spamProb = (double) spamFreq / totalWordsSpam;
                double hamProb = (double) Math.max(hamFreq, 1) / totalWordsHam; // Tránh chia 0
                double importance = spamProb / hamProb;
                spamImportance.put(word, importance);
            }
        }
        
        System.out.println("\nTừ đặc trưng cho SPAM:");
        spamImportance.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(topN)
            .forEach(entry -> System.out.printf("• %s (%.2f)\n", 
                                               entry.getKey(), entry.getValue()));
    }
    
    public static void main(String[] args) {
        System.out.println("=== BỘ LỌC THƯ RÁC NAIVE BAYES ===\n");
        
        // Dữ liệu huấn luyện
        List<EmailMessage> trainingData = Arrays.asList(
            new EmailMessage("Congratulations! You won $1000000. Click here now!", "spam"),
            new EmailMessage("Free money! Act now! Limited time offer!", "spam"),
            new EmailMessage("Get rich quick! Buy now! Special discount!", "spam"),
            new EmailMessage("Viagra cheap! Best price guaranteed! Order now!", "spam"),
            new EmailMessage("You are a winner! Claim your prize immediately!", "spam"),
            new EmailMessage("Urgent! Your account will be closed! Click here!", "spam"),
            new EmailMessage("Make money fast! Work from home! Easy money!", "spam"),
            new EmailMessage("Meeting tomorrow at 3pm in conference room", "ham"),
            new EmailMessage("Can you review this document please?", "ham"),
            new EmailMessage("Lunch at 12? Let me know if you can make it", "ham"),
            new EmailMessage("Project deadline is next Friday, please prepare", "ham"),
            new EmailMessage("Thanks for your help yesterday with the presentation", "ham"),
            new EmailMessage("Please find attached the quarterly report", "ham"),
            new EmailMessage("The system will be down for maintenance tonight", "ham"),
            new EmailMessage("Happy birthday! Hope you have a great day", "ham")
        );
        
        // Huấn luyện
        SpamDetection classifier = new SpamDetection();
        classifier.train(trainingData);
        
        // Dữ liệu test
        List<EmailMessage> testMessages = Arrays.asList(
            new EmailMessage("Free offer! Click now to win big!", "spam"),
            new EmailMessage("Meeting rescheduled to 4pm tomorrow", "ham"),
            new EmailMessage("Congratulations! You won a fantastic prize!", "spam"),
            new EmailMessage("Can we discuss the project status tomorrow?", "ham"),
            new EmailMessage("Buy now! Limited stock available!", "spam"),
            new EmailMessage("Thanks for the update on the client meeting", "ham")
        );
        
        // Đánh giá
        classifier.evaluate(testMessages);
        
        // Test chi tiết
        System.out.println("\n=== KẾT QUẢ PHÂN LOẠI CHI TIẾT ===\n");
        for (EmailMessage msg : testMessages) {
            ClassificationResult result = classifier.predictWithConfidence(msg.getText());
            String actual = msg.getLabel();
            String status = actual.equals(result.getPrediction()) ? "✓" : "✗";
            
            System.out.printf("%s Tin nhắn: \"%s\"\n", status, msg.getText());
            System.out.printf("   Thực tế: %s | Dự đoán: %s (%.1f%%)\n\n", 
                             actual.toUpperCase(), 
                             result.getPrediction().toUpperCase(), 
                             result.getConfidence() * 100);
        }
        
        // Hiển thị từ quan trọng
        classifier.showTopWords(10);
        
        // Test tương tác
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== TEST TƯƠNG TÁC ===");
        System.out.println("Nhập email để kiểm tra (hoặc 'quit' để thoát):");
        
        while (true) {
            System.out.print("\nEmail: ");
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input)) {
                break;
            }
            
            if (!input.isEmpty()) {
                ClassificationResult result = classifier.predictWithConfidence(input);
                System.out.printf("Kết quả: %s (%.1f%% tin cậy)\n", 
                                 result.getPrediction().toUpperCase(), 
                                 result.getConfidence() * 100);
            }
        }
        
        scanner.close();
        System.out.println("Cảm ơn bạn đã sử dụng!");
    }
}