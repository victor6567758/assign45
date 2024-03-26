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
    for (int i = 0; i < 8; i++ ) {
      solveForPrize(i);
    }

  }

  private void solveForPrize(int prizeIdx) {
    Node root = generateTree(prizeIdx, 0, 8);

    bombs = 0;
    CellType[] buffer = new CellType[8];
    traversal(prizeIdx, root, buffer);
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

    // The prize is not here.
    Supplier<Boolean> cond2 = () -> buffer[1] != CellType.P;

    // Label 2 is true and the prize is in room 1
    Supplier<Boolean> cond3 = () -> {
      return RuleIsTrue(cond2, buffer[1]) && buffer[0] == CellType.P;
    };

    // Here is a bomb and label 3 is false.
    Supplier<Boolean> cond1 = () -> {
      return RuleIsFalse(cond3, buffer[2]) && buffer[0] == CellType.B;
    };



    int emptyRooms = 8 - 1 - bombs;
    // 3 rooms are empty.
    Supplier<Boolean> cond5 = () -> emptyRooms == 3;

    // The prize is in an odd room if room 2 is empty.
    Supplier<Boolean> cond4 = () -> buffer[1] == CellType.E && prizeIdx % 2 == 0;

    // There are 5 bombs in the 8 rooms.
    Supplier<Boolean> cond6 = () -> bombs == 5;

    // Room 6 is not empty.
    Supplier<Boolean> cond7 = () -> buffer[5] != CellType.E;

    // The prize is in room 7
    Supplier<Boolean> cond8 = () -> buffer[6] == CellType.P;

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


      boolean label3Passed = passedRule(() -> cond3.get(), buffer[2]);

      boolean label1Passed = passedRule(() -> cond1.get(), buffer[0]);

      return label1Passed && label3Passed;
    }

    if (level == 3) {
      return passedRule(cond4, buffer[2]);
    }

    if (level == 4) {
      return true;
    }

    if (level == 5) {
      return true;
    }

    if (level == 6) {
      return passedRule(cond7, buffer[5]);
    }


    if (level == 7) {
      boolean label5Passed = passedRule(cond5, buffer[4]);

      boolean label6Passed = passedRule(cond6, buffer[5]);

      boolean label8Passed = passedRule(cond8, buffer[7]);

      return label5Passed && label6Passed && label8Passed;
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

}
