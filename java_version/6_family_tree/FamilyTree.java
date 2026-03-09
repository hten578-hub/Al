/**
 * Hệ suy luận cây gia phả bằng Logic vị từ bậc nhất (First-Order Logic)
 * Java version
 */
import java.util.*;

class Relationship {
    private String parent;
    private String child;
    
    public Relationship(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }
    
    public String getParent() { return parent; }
    public String getChild() { return child; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Relationship that = (Relationship) obj;
        return Objects.equals(parent, that.parent) && Objects.equals(child, that.child);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
    
    @Override
    public String toString() {
        return parent + " -> " + child;
    }
}

public class FamilyTree {
    private Set<Relationship> parentOf;
    private Set<String> male;
    private Set<String> female;
    
    public FamilyTree() {
        this.parentOf = new HashSet<>();
        this.male = new HashSet<>();
        this.female = new HashSet<>();
    }
    
    public void addParent(String parent, String child) {
        parentOf.add(new Relationship(parent, child));
    }
    
    public void addMale(String person) {
        male.add(person);
    }
    
    public void addFemale(String person) {
        female.add(person);
    }
    
    public boolean isParent(String parent, String child) {
        return parentOf.contains(new Relationship(parent, child));
    }
    
    public boolean isChild(String child, String parent) {
        return parentOf.contains(new Relationship(parent, child));
    }
    
    public boolean isFather(String father, String child) {
        return isParent(father, child) && male.contains(father);
    }
    
    public boolean isMother(String mother, String child) {
        return isParent(mother, child) && female.contains(mother);
    }
    
    public Set<String> getChildren(String parent) {
        Set<String> children = new HashSet<>();
        for (Relationship rel : parentOf) {
            if (rel.getParent().equals(parent)) {
                children.add(rel.getChild());
            }
        }
        return children;
    }
    
    public Set<String> getParents(String child) {
        Set<String> parents = new HashSet<>();
        for (Relationship rel : parentOf) {
            if (rel.getChild().equals(child)) {
                parents.add(rel.getParent());
            }
        }
        return parents;
    }
    
    public boolean isSibling(String person1, String person2) {
        if (person1.equals(person2)) {
            return false;
        }
        Set<String> parents1 = getParents(person1);
        Set<String> parents2 = getParents(person2);
        
        // Có chung ít nhất một cha hoặc mẹ
        for (String parent : parents1) {
            if (parents2.contains(parent)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isGrandparent(String grandparent, String grandchild) {
        Set<String> children = getChildren(grandparent);
        for (String child : children) {
            if (isParent(child, grandchild)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isGrandfather(String grandfather, String grandchild) {
        return isGrandparent(grandfather, grandchild) && male.contains(grandfather);
    }
    
    public boolean isGrandmother(String grandmother, String grandchild) {
        return isGrandparent(grandmother, grandchild) && female.contains(grandmother);
    }
    
    public boolean isUncleOrAunt(String person, String nephewNiece) {
        Set<String> parents = getParents(nephewNiece);
        for (String parent : parents) {
            if (isSibling(person, parent)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUncle(String uncle, String nephewNiece) {
        return isUncleOrAunt(uncle, nephewNiece) && male.contains(uncle);
    }
    
    public boolean isAunt(String aunt, String nephewNiece) {
        return isUncleOrAunt(aunt, nephewNiece) && female.contains(aunt);
    }
    
    public boolean isCousin(String person1, String person2) {
        Set<String> parents1 = getParents(person1);
        Set<String> parents2 = getParents(person2);
        
        for (String p1 : parents1) {
            for (String p2 : parents2) {
                if (isSibling(p1, p2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Set<String> getAncestors(String person, int depth) {
        Set<String> ancestors = new HashSet<>();
        Set<String> currentGen = new HashSet<>();
        currentGen.add(person);
        
        for (int i = 0; i < depth; i++) {
            Set<String> nextGen = new HashSet<>();
            for (String p : currentGen) {
                Set<String> parents = getParents(p);
                ancestors.addAll(parents);
                nextGen.addAll(parents);
            }
            if (nextGen.isEmpty()) {
                break;
            }
            currentGen = nextGen;
        }
        
        return ancestors;
    }
    
    public Set<String> getDescendants(String person, int depth) {
        Set<String> descendants = new HashSet<>();
        Set<String> currentGen = new HashSet<>();
        currentGen.add(person);
        
        for (int i = 0; i < depth; i++) {
            Set<String> nextGen = new HashSet<>();
            for (String p : currentGen) {
                Set<String> children = getChildren(p);
                descendants.addAll(children);
                nextGen.addAll(children);
            }
            if (nextGen.isEmpty()) {
                break;
            }
            currentGen = nextGen;
        }
        
        return descendants;
    }
    
    public void printFamilyTree() {
        System.out.println("=== CÂY GIA PHẢ ===");
        
        // Tìm thế hệ gốc (những người không có cha mẹ)
        Set<String> allPeople = new HashSet<>();
        for (Relationship rel : parentOf) {
            allPeople.add(rel.getParent());
            allPeople.add(rel.getChild());
        }
        
        Set<String> roots = new HashSet<>();
        for (String person : allPeople) {
            if (getParents(person).isEmpty()) {
                roots.add(person);
            }
        }
        
        // In cây từ gốc
        for (String root : roots) {
            printPersonAndDescendants(root, 0);
        }
    }
    
    private void printPersonAndDescendants(String person, int level) {
        // In thụt lề theo cấp độ
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        
        // In tên và giới tính
        String gender = male.contains(person) ? " ♂" : (female.contains(person) ? " ♀" : "");
        System.out.println(person + gender);
        
        // In con cái
        Set<String> children = getChildren(person);
        for (String child : children) {
            printPersonAndDescendants(child, level + 1);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== HỆ SUY LUẬN CÂY GIA PHẢ ===\n");
        
        // Tạo cây gia phả mẫu
        FamilyTree family = new FamilyTree();
        
        // Thế hệ 1 (ông bà)
        family.addMale("Ông_Năm");
        family.addFemale("Bà_Năm");
        
        // Thế hệ 2 (cha mẹ và chú bác)
        family.addMale("Bố_An");
        family.addFemale("Mẹ_An");
        family.addMale("Chú_Hai");
        family.addFemale("Cô_Ba");
        
        // Quan hệ thế hệ 1-2
        family.addParent("Ông_Năm", "Bố_An");
        family.addParent("Bà_Năm", "Bố_An");
        family.addParent("Ông_Năm", "Chú_Hai");
        family.addParent("Bà_Năm", "Chú_Hai");
        family.addParent("Ông_Năm", "Cô_Ba");
        family.addParent("Bà_Năm", "Cô_Ba");
        
        // Thế hệ 3 (con cháu)
        family.addMale("An");
        family.addFemale("Bình");
        family.addMale("Cường");
        family.addFemale("Dung");
        
        family.addParent("Bố_An", "An");
        family.addParent("Mẹ_An", "An");
        family.addParent("Bố_An", "Bình");
        family.addParent("Mẹ_An", "Bình");
        family.addParent("Chú_Hai", "Cường");
        family.addParent("Cô_Ba", "Dung");
        
        // In cây gia phả
        family.printFamilyTree();
        
        // Truy vấn các quan hệ
        System.out.println("\n=== KIỂM TRA CÁC QUAN HỆ ===\n");
        
        System.out.println("👨‍👩‍👧‍👦 Quan hệ cha mẹ - con:");
        System.out.println("Bố_An là cha của An? " + (family.isFather("Bố_An", "An") ? "✓" : "✗"));
        System.out.println("Mẹ_An là mẹ của An? " + (family.isMother("Mẹ_An", "An") ? "✓" : "✗"));
        System.out.println("Ông_Năm là cha của Bố_An? " + (family.isFather("Ông_Năm", "Bố_An") ? "✓" : "✗"));
        
        System.out.println("\n👫 Quan hệ anh chị em:");
        System.out.println("An và Bình là anh chị em? " + (family.isSibling("An", "Bình") ? "✓" : "✗"));
        System.out.println("Bố_An và Chú_Hai là anh em? " + (family.isSibling("Bố_An", "Chú_Hai") ? "✓" : "✗"));
        System.out.println("An và Cường là anh em? " + (family.isSibling("An", "Cường") ? "✓" : "✗"));
        
        System.out.println("\n👴👵 Quan hệ ông bà - cháu:");
        System.out.println("Ông_Năm là ông của An? " + (family.isGrandfather("Ông_Năm", "An") ? "✓" : "✗"));
        System.out.println("Bà_Năm là bà của Bình? " + (family.isGrandmother("Bà_Năm", "Bình") ? "✓" : "✗"));
        System.out.println("Ông_Năm là ông của Cường? " + (family.isGrandfather("Ông_Năm", "Cường") ? "✓" : "✗"));
        
        System.out.println("\n👨‍👧 Quan hệ chú bác - cháu:");
        System.out.println("Chú_Hai là chú của An? " + (family.isUncle("Chú_Hai", "An") ? "✓" : "✗"));
        System.out.println("Cô_Ba là cô của Bình? " + (family.isAunt("Cô_Ba", "Bình") ? "✓" : "✗"));
        System.out.println("Bố_An là chú của Cường? " + (family.isUncle("Bố_An", "Cường") ? "✓" : "✗"));
        
        System.out.println("\n👬 Quan hệ anh chị em họ:");
        System.out.println("An và Cường là anh em họ? " + (family.isCousin("An", "Cường") ? "✓" : "✗"));
        System.out.println("Bình và Dung là chị em họ? " + (family.isCousin("Bình", "Dung") ? "✓" : "✗"));
        System.out.println("An và Dung là anh em họ? " + (family.isCousin("An", "Dung") ? "✓" : "✗"));
        
        // Hiển thị thông tin chi tiết
        System.out.println("\n=== THÔNG TIN CHI TIẾT ===\n");
        
        String person = "An";
        System.out.println("📋 Thông tin về " + person + ":");
        System.out.println("Con của: " + family.getParents(person));
        System.out.println("Anh chị em: " + getsiblings(family, person));
        System.out.println("Tổ tiên: " + family.getAncestors(person, 10));
        
        person = "Ông_Năm";
        System.out.println("\n📋 Thông tin về " + person + ":");
        System.out.println("Con: " + family.getChildren(person));
        System.out.println("Hậu duệ: " + family.getDescendants(person, 10));
        
        System.out.println("\n=== THỐNG KÊ GIA PHẢ ===");
        System.out.println("Tổng số người: " + getAllPeople(family).size());
        System.out.println("Số nam: " + family.male.size());
        System.out.println("Số nữ: " + family.female.size());
        System.out.println("Số quan hệ cha mẹ-con: " + family.parentOf.size());
    }
    
    private static Set<String> getsiblings(FamilyTree family, String person) {
        Set<String> siblings = new HashSet<>();
        Set<String> parents = family.getParents(person);
        
        for (String parent : parents) {
            Set<String> children = family.getChildren(parent);
            for (String child : children) {
                if (!child.equals(person)) {
                    siblings.add(child);
                }
            }
        }
        
        return siblings;
    }
    
    private static Set<String> getAllPeople(FamilyTree family) {
        Set<String> allPeople = new HashSet<>();
        for (Relationship rel : family.parentOf) {
            allPeople.add(rel.getParent());
            allPeople.add(rel.getChild());
        }
        return allPeople;
    }
}