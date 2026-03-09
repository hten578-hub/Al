"""
Hệ chuyên gia chẩn đoán bệnh dựa trên luật logic
"""
from typing import List, Dict, Set

class ExpertSystem:
    def __init__(self):
        self.rules = []
        self.facts = set()
        
    def add_rule(self, conditions: List[str], conclusion: str, confidence: float = 1.0):
        """Thêm luật: IF conditions THEN conclusion"""
        self.rules.append({
            'conditions': conditions,
            'conclusion': conclusion,
            'confidence': confidence
        })
    
    def add_fact(self, fact: str):
        """Thêm sự kiện"""
        self.facts.add(fact)
    
    def forward_chaining(self) -> Dict[str, float]:
        """Suy diễn tiến (Forward Chaining)"""
        conclusions = {}
        changed = True
        
        while changed:
            changed = False
            for rule in self.rules:
                # Kiểm tra tất cả điều kiện có thỏa mãn không
                if all(cond in self.facts for cond in rule['conditions']):
                    conclusion = rule['conclusion']
                    if conclusion not in self.facts:
                        self.facts.add(conclusion)
                        conclusions[conclusion] = rule['confidence']
                        changed = True
        
        return conclusions

def create_medical_expert_system():
    """Tạo hệ chuyên gia y tế"""
    es = ExpertSystem()
    
    # Luật chẩn đoán
    es.add_rule(['sốt', 'ho', 'đau_họng'], 'cảm_cúm', 0.8)
    es.add_rule(['sốt', 'đau_đầu', 'buồn_nôn'], 'cảm_cúm', 0.7)
    es.add_rule(['sốt_cao', 'đau_bụng', 'tiêu_chảy'], 'ngộ_độc_thực_phẩm', 0.85)
    es.add_rule(['ho_kéo_dài', 'khó_thở', 'đau_ngực'], 'viêm_phổi', 0.9)
    es.add_rule(['sốt', 'phát_ban', 'ngứa'], 'dị_ứng', 0.75)
    es.add_rule(['đau_đầu', 'chóng_mặt', 'mệt_mỏi'], 'huyết_áp_thấp', 0.7)
    
    # Luật khuyến nghị
    es.add_rule(['cảm_cúm'], 'nghỉ_ngơi_và_uống_nhiều_nước', 1.0)
    es.add_rule(['ngộ_độc_thực_phẩm'], 'đến_bệnh_viện_ngay', 1.0)
    es.add_rule(['viêm_phổi'], 'cần_khám_bác_sĩ_gấp', 1.0)
    es.add_rule(['dị_ứng'], 'dùng_thuốc_kháng_histamine', 0.9)
    
    return es

if __name__ == "__main__":
    print("=== HỆ CHUYÊN GIA CHẨN ĐOÁN BỆNH ===\n")
    
    # Tạo hệ thống
    expert_system = create_medical_expert_system()
    
    # Danh sách triệu chứng có sẵn
    available_symptoms = [
        'sốt', 'sốt_cao', 'ho', 'ho_kéo_dài', 'đau_họng', 
        'đau_đầu', 'đau_bụng', 'đau_ngực', 'buồn_nôn', 
        'tiêu_chảy', 'khó_thở', 'phát_ban', 'ngứa', 
        'chóng_mặt', 'mệt_mỏi'
    ]
    
    print("Các triệu chứng có thể nhập:")
    for i, symptom in enumerate(available_symptoms, 1):
        print(f"{i}. {symptom}")
    
    print("\nNhập các triệu chứng (cách nhau bởi dấu phẩy):")
    print("Ví dụ: sốt, ho, đau_họng")
    
    # Ví dụ triệu chứng
    symptoms_input = "sốt, ho, đau_họng"
    print(f"\nTriệu chứng nhập vào: {symptoms_input}")
    
    symptoms = [s.strip() for s in symptoms_input.split(',')]
    
    # Thêm triệu chứng vào hệ thống
    for symptom in symptoms:
        if symptom in available_symptoms:
            expert_system.add_fact(symptom)
    
    # Chẩn đoán
    print("\n=== KẾT QUẢ CHẨN ĐOÁN ===")
    diagnoses = expert_system.forward_chaining()
    
    if diagnoses:
        print("\nCác chẩn đoán có thể:")
        for diagnosis, confidence in diagnoses.items():
            if not diagnosis.startswith('nghỉ') and not diagnosis.startswith('đến') and not diagnosis.startswith('cần') and not diagnosis.startswith('dùng'):
                print(f"- {diagnosis.replace('_', ' ').title()}: {confidence*100:.0f}% độ tin cậy")
        
        print("\nKhuyến nghị:")
        for recommendation, confidence in diagnoses.items():
            if diagnosis.startswith('nghỉ') or diagnosis.startswith('đến') or diagnosis.startswith('cần') or diagnosis.startswith('dùng'):
                print(f"- {recommendation.replace('_', ' ').title()}")
    else:
        print("Không đủ thông tin để chẩn đoán. Vui lòng gặp bác sĩ.")
