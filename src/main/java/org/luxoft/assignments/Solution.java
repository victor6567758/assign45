package org.luxoft.assignments;

import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.Getter;

public class Solution {

  private int bombs;

  static class Node {

    Node left;
    Node right;

    Node(int val) {
      this.val = val;
    }

    int val;

  }


  @Getter
  private enum CellType {
    P(-1),
    E(0),
    B(1);

    private final int type;

    CellType(int type) {
      this.type = type;
    }

  }

  public void solve() {
    int priceIdx = 4;
    Node root = generateTree(priceIdx, 0, 8);

    bombs = 0;
    CellType[] buffer = new CellType[8];
    traversal(priceIdx, root, buffer);

    int t = 0;
  }

  private Node generateTree(int prizeIdx, int level, int depth) {

    Node newNode = new Node(level);
    if (depth == 1) {
      return newNode;
    }

    if (prizeIdx != level) {
      newNode.left = generateTree(prizeIdx, level + 1, depth - 1);
      newNode.right = generateTree(prizeIdx, level + 1, depth - 1);
    } else {
      newNode.left = generateTree(prizeIdx, level + 1, depth - 1);
    }
    return newNode;
  }

  private void traversal(int prizeIdx, Node node, CellType[] buffer) {
    if (node == null) {
      return;
    }

    if (prizeIdx != node.val) {
      buffer[node.val] = CellType.B;
      bombs++;
      if (!checkConditionsPassed(prizeIdx, buffer, node.val)) {
        node.left = null;
      }

      print(buffer, node.val);
      traversal(prizeIdx, node.left, buffer);
      bombs--;

      buffer[node.val] = CellType.E;
      if (!checkConditionsPassed(prizeIdx, buffer, node.val)) {
        node.right = null;
      }
      print(buffer, node.val);
      traversal(prizeIdx, node.right, buffer);

    } else {
      buffer[node.val] = CellType.P;

      if (!checkConditionsPassed(prizeIdx, buffer, node.val)) {
        node.left = null;
      }

      print(buffer, node.val);
      traversal(prizeIdx, node.left, buffer);
    }

  }


  private boolean RuleIsFalse(Supplier<Boolean> ruleSupplier, CellType cellType) {
    return cellType == CellType.B && !ruleSupplier.get();
  }

  private boolean RuleIsTrue(Supplier<Boolean> ruleSupplier, CellType cellType) {
    return cellType == CellType.P ? ruleSupplier.get() : false;
  }

  private boolean passedRule(Supplier<Boolean> ruleSupplier, CellType cellType) {
    return switch (cellType) {
      case B -> !ruleSupplier.get();
      case P -> ruleSupplier.get();
      case E -> true;
    };
  }

  private boolean checkConditionsPassed(int prizeIdx, CellType[] buffer, int level) {

    Supplier<Boolean> cond2 = () -> buffer[1] != CellType.P;

    if (level == 0) {
      // Here is a bomb and label 3 is false  - ignore
      return true;
    }

    if (level == 1) {
      // The prize is not here.
      return passedRule(cond2, buffer[1]);
    }

    if (level == 2) {

      // Label 2 is true and the prize is in room 1
      // check label 2 from
      Supplier<Boolean> cond3 = () -> {
        return RuleIsTrue(cond2, buffer[1]) && buffer[0] == CellType.P;
      };

      boolean label3Passed = passedRule(() -> cond3.get(), buffer[2]);


      // Here is a bomb and label 3 is false.
      // check the label 3 from the room 1
      Supplier<Boolean> cond1 = () -> {
        return RuleIsFalse(cond3, buffer[2]) && buffer[0] == CellType.B;
      };

      boolean label1Passed = passedRule(() -> cond1.get(), buffer[0]);

      return label1Passed && label3Passed;
    }

    if (level == 3) {
      //The prize is in an odd room if room 2 is empty.
      return passedRule(() -> buffer[1] == CellType.E && prizeIdx % 2 == 0, buffer[2]);
    }
//
//    if (level == 4) {
//      return true;
//    }
//
//    if (level == 5) {
//      return true;
//    }
//
//    if (level == 6) {
//      // Room 6 is not empty
//      return passedRule(() -> buffer[5] != CellType.E, curCellType);
//    }
//
//    if (level == 7) {
//      // The prize is in room 7
//      // There are 5 bombs in the 8 rooms.
//      // 3 rooms are empty.
//      return passedRule(() -> buffer[6] == CellType.P && bombs == 5, curCellType);
//    }

    if (level == 7) {

    }

    return true;

  }

  private void print(CellType[] buffer, int level) {
    if (level != 7) {
      return;
    }

    StringBuilder r = new StringBuilder();
    for (int i = 0; i <= level; i++) {
      r.append(buffer[i]);
    }

    System.out.println(
        r + ", bombs: " + bombs + ", empty: " + (8 - 1 - bombs) + ", level: " + level);
  }

  private long countEmptyRooms(CellType[] buffer, int level) {
    if (level < 7) {
      throw new IllegalArgumentException();
    }

    return Stream.of(buffer).filter(n -> n == CellType.E).count();
  }

  private long countBombs(CellType[] buffer, int level) {
    if (level < 7) {
      throw new IllegalArgumentException();
    }

    return Stream.of(buffer).filter(n -> n == CellType.B).count();
  }

  private boolean rule1(CellType[] buffer, int level) {

    return buffer[0] == CellType.B && !rule3(buffer, level);
  }

  private boolean rule2(CellType[] buffer, int level) {
    return buffer[1] != CellType.P;
  }

  private boolean rule3(CellType[] buffer, int level) {
    return rule2(buffer, level) && buffer[0] == CellType.P;
  }

  private boolean rule4(CellType[] buffer, int level) {
    if (level < 7) {
      throw new IllegalArgumentException();
    }

    return buffer[1] == CellType.E && (buffer[0] == CellType.P && buffer[2] == CellType.P
        && buffer[4] == CellType.P
        && buffer[6] == CellType.P);
  }

  private boolean rule5(CellType[] buffer, int level) {
    return countEmptyRooms(buffer, level) == 3;
  }

  private boolean rule6(CellType[] buffer, int level) {
    return countBombs(buffer, level) == 5;
  }

  private boolean rule7(CellType[] buffer, int level) {
    if (level < 5) {
      throw new IllegalArgumentException();
    }

    return buffer[5] != CellType.E;
  }

  private boolean rule8(CellType[] buffer, int level) {

    return buffer[6] == CellType.P;
  }

}
