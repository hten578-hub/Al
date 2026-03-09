"""
Phát hiện thư rác bằng Naive Bayes
"""
import re
from collections import defaultdict
from typing import List, Dict, Tuple
import math

class NaiveBayesSpamFilter:
    def __init__(self):
        self.word_count_spam = defaultdict(int)
        self.word_count_ham = defaultdict(int)
        self.spam_count = 0
        self.ham_count = 0
        self.vocab = set()
        
    def preprocess(self, text: str) -> List[str]:
        """Tiền xử lý văn bản"""
        text = text.lower()
        words = re.findall(r'\w+', text)
        return words
    
    def train(self, messages: List[Tuple[str, str]]):
        """
        Huấn luyện mô hình
        messages: List of (text, label) where label is 'spam' or 'ham'
        """
        for text, label in messages:
            words = self.preprocess(text)
            self.vocab.update(words)
            
            if label == 'spam':
                self.spam_count += 1
                for word in words:
                    self.word_count_spam[word] += 1
            else:
                self.ham_count += 1
                for word in words:
                    self.word_count_ham[word] += 1
    
    def calculate_probability(self, text: str) -> Tuple[float, float]:
        """Tính xác suất spam và ham"""
        words = self.preprocess(text)
        
        total_messages = self.spam_count + self.ham_count
        p_spam = self.spam_count / total_messages
        p_ham = self.ham_count / total_messages
        
        # Laplace smoothing
        total_words_spam = sum(self.word_count_spam.values())
        total_words_ham = sum(self.word_count_ham.values())
        vocab_size = len(self.vocab)
        
        log_prob_spam = math.log(p_spam)
        log_prob_ham = math.log(p_ham)
        
        for word in words:
            # P(word|spam) với Laplace smoothing
            prob_word_spam = (self.word_count_spam[word] + 1) / (total_words_spam + vocab_size)
            prob_word_ham = (self.word_count_ham[word] + 1) / (total_words_ham + vocab_size)
            
            log_prob_spam += math.log(prob_word_spam)
            log_prob_ham += math.log(prob_word_ham)
        
        return log_prob_spam, log_prob_ham
    
    def predict(self, text: str) -> str:
        """Dự đoán spam hay ham"""
        log_prob_spam, log_prob_ham = self.calculate_probability(text)
        return 'spam' if log_prob_spam > log_prob_ham else 'ham'
    
    def predict_with_confidence(self, text: str) -> Tuple[str, float]:
        """Dự đoán với độ tin cậy"""
        log_prob_spam, log_prob_ham = self.calculate_probability(text)
        
        # Chuyển về xác suất thực
        max_log = max(log_prob_spam, log_prob_ham)
        prob_spam = math.exp(log_prob_spam - max_log)
        prob_ham = math.exp(log_prob_ham - max_log)
        
        total = prob_spam + prob_ham
        prob_spam /= total
        prob_ham /= total
        
        if prob_spam > prob_ham:
            return 'spam', prob_spam
        else:
            return 'ham', prob_ham

if __name__ == "__main__":
    print("=== BỘ LỌC THƯ RÁC NAIVE BAYES ===\n")
    
    # Dữ liệu huấn luyện
    training_data = [
        ("Congratulations! You won $1000000. Click here now!", "spam"),
        ("Free money! Act now! Limited time offer!", "spam"),
        ("Get rich quick! Buy now!", "spam"),
        ("Viagra cheap! Best price guaranteed!", "spam"),
        ("You are a winner! Claim your prize!", "spam"),
        ("Meeting tomorrow at 3pm in conference room", "ham"),
        ("Can you review this document?", "ham"),
        ("Lunch at 12? Let me know", "ham"),
        ("Project deadline is next Friday", "ham"),
        ("Thanks for your help yesterday", "ham"),
        ("Please find attached the report", "ham"),
    ]
    
    # Huấn luyện
    classifier = NaiveBayesSpamFilter()
    classifier.train(training_data)
    
    print("Đã huấn luyện với {} tin nhắn\n".format(len(training_data)))
    
    # Kiểm tra
    test_messages = [
        "Free offer! Click now to win!",
        "Meeting rescheduled to 4pm",
        "Congratulations! You won a prize!",
        "Can we discuss the project tomorrow?",
        "Buy now! Limited stock!",
        "Thanks for the update"
    ]
    
    print("=== KẾT QUẢ PHÂN LOẠI ===\n")
    for msg in test_messages:
        prediction, confidence = classifier.predict_with_confidence(msg)
        print(f"Tin nhắn: \"{msg}\"")
        print(f"Phân loại: {prediction.upper()} (Độ tin cậy: {confidence*100:.1f}%)\n")
