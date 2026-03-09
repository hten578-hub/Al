"""
Hệ suy luận cây gia phả bằng Logic vị từ bậc nhất (First-Order Logic)
"""
from typing import Set, Tuple, List

class FamilyTree:
    def __init__(self):
        self.parent_of = set()  # (cha/mẹ, con)
        self.male = set()
        self.female = set()
        
    def add_parent(self, parent: str, child: str):
        """Thêm quan hệ cha/mẹ - con"""
        self.parent_of.add((parent, child))
    
    def add_male(self, person: str):
        """Đánh dấu là nam"""
        self.male.add(person)
    
    def add_female(self, person: str):
        """Đánh dấu là nữ"""
        self.female.add(person)
    
    def is_parent(self, parent: str, child: str) -> bool:
        """Kiểm tra quan hệ cha/mẹ"""
        return (parent, child) in self.parent_of
    
    def is_child(self, child: str, parent: str) -> bool:
        """Kiểm tra quan hệ con"""
        return (parent, child) in self.parent_of
    
    def is_father(self, father: str, child: str) -> bool:
        """Kiểm tra quan hệ cha"""
        return self.is_parent(father, child) and father in self.male
    
    def is_mother(self, mother: str, child: str) -> bool:
        """Kiểm tra quan hệ mẹ"""
        return self.is_parent(mother, child) and mother in self.female
    
    def get_children(self, parent: str) -> Set[str]:
        """Lấy danh sách con"""
        return {child for p, child in self.parent_of if p == parent}
    
    def get_parents(self, child: str) -> Set[str]:
        """Lấy danh sách cha/mẹ"""
        return {parent for parent, c in self.parent_of if c == child}
    
    def is_sibling(self, person1: str, person2: str) -> bool:
        """Kiểm tra anh chị em (có chung cha hoặc mẹ)"""
        if person1 == person2:
            return False
        parents1 = self.get_parents(person1)
        parents2 = self.get_parents(person2)
        return len(parents1 & parents2) > 0
    
    def is_grandparent(self, grandparent: str, grandchild: str) -> bool:
        """Kiểm tra quan hệ ông/bà - cháu"""
        children = self.get_children(grandparent)
        for child in children:
            if self.is_parent(child, grandchild):
                return True
        return False
    
    def is_grandfather(self, grandfather: str, grandchild: str) -> bool:
        """Kiểm tra quan hệ ông"""
        return self.is_grandparent(grandfather, grandchild) and grandfather in self.male
    
    def is_grandmother(self, grandmother: str, grandchild: str) -> bool:
        """Kiểm tra quan hệ bà"""
        return self.is_grandparent(grandmother, grandchild) and grandmother in self.female
    
    def is_uncle_or_aunt(self, person: str, nephew_niece: str) -> bool:
        """Kiểm tra quan hệ chú/bác/cô/dì"""
        parents = self.get_parents(nephew_niece)
        for parent in parents:
            if self.is_sibling(person, parent):
                return True
        return False
    
    def is_cousin(self, person1: str, person2: str) -> bool:
        """Kiểm tra quan hệ anh chị em họ"""
        parents1 = self.get_parents(person1)
        parents2 = self.get_parents(person2)
        for p1 in parents1:
            for p2 in parents2:
                if self.is_sibling(p1, p2):
                    return True
        return False
    
    def get_ancestors(self, person: str, depth: int = 10) -> Set[str]:
        """Lấy tổ tiên"""
        ancestors = set()
        current_gen = {person}
        
        for _ in range(depth):
            next_gen = set()
            for p in current_gen:
                parents = self.get_parents(p)
                ancestors.update(parents)
                next_gen.update(parents)
            if not next_gen:
                break
            current_gen = next_gen
        
        return ancestors
    
    def get_descendants(self, person: str, depth: int = 10) -> Set[str]:
        """Lấy hậu duệ"""
        descendants = set()
        current_gen = {person}
        
        for _ in range(depth):
            next_gen = set()
            for p in current_gen:
                children = self.get_children(p)
                descendants.update(children)
                next_gen.update(children)
            if not next_gen:
                break
            current_gen = next_gen
        
        return descendants

if __name__ == "__main__":
    print("=== HỆ SUY LUẬN CÂY GIA PHẢ ===\n")
    
    # Tạo cây gia phả mẫu
    family = FamilyTree()
    
    # Thế hệ 1 (ông bà)
    family.add_male("Ông_Năm")
    family.add_female("Bà_Năm")
    
    # Thế hệ 2 (cha mẹ và chú bác)
    family.add_male("Bố_An")
    family.add_female("Mẹ_An")
    family.add_male("Chú_Hai")
    family.add_female("Cô_Ba")
    
    # Quan hệ thế hệ 1-2
    family.add_parent("Ông_Năm", "Bố_An")
    family.add_parent("Bà_Năm", "Bố_An")
    family.add_parent("Ông_Năm", "Chú_Hai")
    family.add_parent("Bà_Năm", "Chú_Hai")
    family.add_parent("Ông_Năm", "Cô_Ba")
    family.add_parent("Bà_Năm", "Cô_Ba")
    
    # Thế hệ 3 (con cháu)
    family.add_male("An")
    family.add_female("Bình")
    family.add_male("Cường")
    
    family.add_parent("Bố_An", "An")
    family.add_parent("Mẹ_An", "An")
    family.add_parent("Bố_An", "Bình")
    family.add_parent("Mẹ_An", "Bình")
    family.add_parent("Chú_Hai", "Cường")
    
    # Truy vấn
    print("Kiểm tra các quan hệ:\n")
    
    print(f"Bố_An là cha của An? {family.is_father('Bố_An', 'An')}")
    print(f"Mẹ_An là mẹ của An? {family.is_mother('Mẹ_An', 'An')}")
    print(f"An và Bình là anh chị em? {family.is_sibling('An', 'Bình')}")
    print(f"An và Cường là anh chị em họ? {family.is_cousin('An', 'Cường')}")
    print(f"Ông_Năm là ông của An? {family.is_grandfather('Ông_Năm', 'An')}")
    print(f"Chú_Hai là chú của An? {family.is_uncle_or_aunt('Chú_Hai', 'An')}")
    
    print(f"\nCon của Bố_An: {family.get_children('Bố_An')}")
    print(f"Cha mẹ của An: {family.get_parents('An')}")
    print(f"Tổ tiên của An: {family.get_ancestors('An')}")
    print(f"Hậu duệ của Ông_Năm: {family.get_descendants('Ông_Năm')}")
