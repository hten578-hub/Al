/**
 * Hệ chuyên gia chẩn đoán bệnh dựa trên luật logic
 * Java version
 */
import java.util.*;

class Rule {
    private List<String> conditions;
    private String conclusion;
    private double confidence;
    
    public Rule(List<String> conditions, String conclusion, double confidence) {
        this.conditions = new ArrayList<>(conditions);
        this.conclusion = conclusion;
        this.confidence = confidence;
    }
    
    public List<String> getConditions() { return conditions; }
    public String getConclusion() { return conclusion; }
    public double getConfidence() { return confidence; }
}

public class ExpertSystem {
    private List<Rule> rules;
    private Set<String> facts;
    
    public ExpertSystem() {
        this.rules = new ArrayList<>();
        this.facts = new HashSet<>();
    }
    
    public void addRule(List<String> conditions, String conclusion, double confidence) {
        rules.add(new Rule(conditions, conclusion, confidence));
    }
    
    public void addFact(String fact) {
        facts.add(fact);
    }
    
    /**
     * Suy diễn tiến (Forward Chaining)
     */
    public Map<String, Double> forwardChaining() {
        Map<String, Double> conclusions = new HashMap<>();
        boolean changed = true;
        
        while (changed) {
            changed = false;
            for (Rule rule : rules) {
                // Kiểm tra tất cả điều kiện có thỏa mãn không
                boolean allConditionsMet = true;
                for (String condition : rule.getConditions()) {
                    if (!facts.contains(condition)) {
                        allConditionsMet = false;
                        break;
                    }
                }
                
                if (allConditionsMet && !facts.contains(rule.getConclusion())) {
                    facts.add(rule.getConclusion());
                    conclusions.put(rule.getConclusion(), rule.getConfidence());
                    changed = true;
                }
            }
        }
        
        return conclusions;
    }
    
    public static ExpertSystem createMedicalExpertSystem() {
        ExpertSystem es = new ExpertSystem();
        
        // Luật chẩn đoán
        es.addRule(Arrays.asList("sốt", "ho", "đau_họng"), "cảm_cúm", 0.8);
        es.addRule(Arrays.asList("sốt", "đau_đầu", "buồn_nôn"), "cảm_cúm", 0.7);
        es.addRule(Arrays.asList("sốt_cao", "đau_bụng", "tiêu_chảy"), "ngộ_độc_thực_phẩm", 0.85);
        es.addRule(Arrays.asList("ho_kéo_dài", "khó_thở", "đau_ngực"), "viêm_phổi", 0.9);
        es.addRule(Arrays.asList("sốt", "phát_ban", "ngứa"), "dị_ứng", 0.75);
        es.addRule(Arrays.asList("đau_đầu", "chóng_mặt", "mệt_mỏi"), "huyết_áp_thấp", 0.7);
        
        // Luật khuyến nghị
        es.addRule(Arrays.asList("cảm_cúm"), "nghỉ_ngơi_và_uống_nhiều_nước", 1.0);
        es.addRule(Arrays.asList("ngộ_độc_thực_phẩm"), "đến_bệnh_viện_ngay", 1.0);
        es.addRule(Arrays.asList("viêm_phổi"), "cần_khám_bác_sĩ_gấp", 1.0);
        es.addRule(Arrays.asList("dị_ứng"), "dùng_thuốc_kháng_histamine", 0.9);
        es.addRule(Arrays.asList("huyết_áp_thấp"), "tăng_cường_dinh_dưỡng", 0.8);
        
        return es;
    }
    
    public static void main(String[] args) {
        System.out.println("=== HỆ CHUYÊN GIA CHẨN ĐOÁN BỆNH ===\n");
        
        // Tạo hệ thống
        ExpertSystem expertSystem = createMedicalExpertSystem();
        
        // Danh sách triệu chứng có sẵn
        List<String> availableSymptoms = Arrays.asList(
            "sốt", "sốt_cao", "ho", "ho_kéo_dài", "đau_họng", 
            "đau_đầu", "đau_bụng", "đau_ngực", "buồn_nôn", 
            "tiêu_chảy", "khó_thở", "phát_ban", "ngứa", 
            "chóng_mặt", "mệt_mỏi"
        );
        
        System.out.println("Các triệu chứng có thể nhập:");
        for (int i = 0; i < availableSymptoms.size(); i++) {
            System.out.println((i + 1) + ". " + availableSymptoms.get(i));
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nNhập các triệu chứng (cách nhau bởi dấu phẩy):");
        System.out.println("Ví dụ: sốt, ho, đau_họng");
        System.out.print("Triệu chứng: ");
        
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            // Sử dụng ví dụ mặc định
            input = "sốt, ho, đau_họng";
            System.out.println("Sử dụng ví dụ: " + input);
        }
        
        String[] symptoms = input.split(",");
        
        // Thêm triệu chứng vào hệ thống
        System.out.println("\nTriệu chứng được nhận dạng:");
        for (String symptom : symptoms) {
            String cleanSymptom = symptom.trim();
            if (availableSymptoms.contains(cleanSymptom)) {
                expertSystem.addFact(cleanSymptom);
                System.out.println("✓ " + cleanSymptom);
            } else {
                System.out.println("✗ " + cleanSymptom + " (không nhận dạng)");
            }
        }
        
        // Chẩn đoán
        System.out.println("\n=== ĐANG PHÂN TÍCH... ===");
        Map<String, Double> diagnoses = expertSystem.forwardChaining();
        
        System.out.println("\n=== KẾT QUẢ CHẨN ĐOÁN ===");
        
        if (!diagnoses.isEmpty()) {
            // Phân loại kết quả
            Map<String, Double> diseases = new HashMap<>();
            Map<String, Double> recommendations = new HashMap<>();
            
            for (Map.Entry<String, Double> entry : diagnoses.entrySet()) {
                String diagnosis = entry.getKey();
                Double confidence = entry.getValue();
                
                if (diagnosis.contains("nghỉ") || diagnosis.contains("đến") || 
                    diagnosis.contains("cần") || diagnosis.contains("dùng") || 
                    diagnosis.contains("tăng")) {
                    recommendations.put(diagnosis, confidence);
                } else {
                    diseases.put(diagnosis, confidence);
                }
            }
            
            // Hiển thị chẩn đoán bệnh
            if (!diseases.isEmpty()) {
                System.out.println("\n🏥 CÁC CHẨN ĐOÁN CÓ THỂ:");
                diseases.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String disease = entry.getKey().replace("_", " ");
                        disease = disease.substring(0, 1).toUpperCase() + disease.substring(1);
                        System.out.printf("• %s: %.0f%% độ tin cậy%n", 
                                        disease, entry.getValue() * 100);
                    });
            }
            
            // Hiển thị khuyến nghị
            if (!recommendations.isEmpty()) {
                System.out.println("\n💊 KHUYẾN NGHỊ:");
                recommendations.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String recommendation = entry.getKey().replace("_", " ");
                        recommendation = recommendation.substring(0, 1).toUpperCase() + 
                                       recommendation.substring(1);
                        System.out.println("• " + recommendation);
                    });
            }
            
            System.out.println("\n⚠️  LƯU Ý: Đây chỉ là hệ thống tư vấn sơ bộ.");
            System.out.println("   Vui lòng tham khảo ý kiến bác sĩ để có chẩn đoán chính xác!");
            
        } else {
            System.out.println("❌ Không đủ thông tin để chẩn đoán.");
            System.out.println("   Vui lòng cung cấp thêm triệu chứng hoặc gặp bác sĩ.");
        }
        
        // Hiển thị thống kê
        System.out.println("\n=== THỐNG KÊ ===");
        System.out.println("Số triệu chứng nhập: " + symptoms.length);
        System.out.println("Số triệu chứng hợp lệ: " + expertSystem.facts.size());
        System.out.println("Số luật được kích hoạt: " + diagnoses.size());
        System.out.println("Tổng số luật trong hệ thống: " + expertSystem.rules.size());
        
        scanner.close();
    }
}