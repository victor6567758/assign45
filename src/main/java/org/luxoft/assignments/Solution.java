package org.luxoft.assignments;

import java.util.stream.Stream;

public class Solution {

  static class Node {

    Node left;
    Node right;

    Node(int val) {
      this.val = val;
    }

    int val;

  }


  private enum CellType {
    P(-1),
    E(0),
    B(1);

    private final int type;

    CellType(int type) {
      this.type = type;
    }

    public int getType() {
      return type;
    }

  }

  public void solve() {
    int priceIdx = 4;
    Node root = generateTree(priceIdx, 0, 8);

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
      if (!checkConditionsPassed(buffer, node.val)) {
        return;
      }


      if (node.val == 7) {
        print(buffer, node.val);
      }
      traversal(prizeIdx, node.left, buffer);

      buffer[node.val] = CellType.E;
      if (!checkConditionsPassed(buffer, node.val)) {
        return;
      }

      if (node.val == 7) {
        print(buffer, node.val);
      }
      traversal(prizeIdx, node.right, buffer);

    } else {
      buffer[node.val] = CellType.P;

      if (!checkConditionsPassed(buffer, node.val)) {
        return;
      }

      if (node.val == 7) {
        print(buffer, node.val);
      }

      traversal(prizeIdx, node.left, buffer);

    }

  }

  private boolean checkConditionsPassed(CellType[] buffer, int level) {

    if (level == 1) {
      // The prize is not here.

      boolean passed = switch (buffer[0]) {
        case B -> !rule2(buffer, level);
        case P -> rule2(buffer, level);
        case E -> true;
      };

      return passed;

    }

    if (level == 3) {
      boolean passed = switch (buffer[0]) {
        case B -> !(rule1(buffer, level) && rule3(buffer, level));
        case P -> rule1(buffer, level) && rule3(buffer, level);
        case E -> true;
      };

      return passed;
    }

//    boolean passed = switch (buffer[0]) {
//      case B -> !rule1(buffer, level);
//      case P -> rule1(buffer, level);
//      case E -> true;
//    };
//
//    if (passed) {
//      passed = switch (buffer[1]) {
//        case B -> !rule2(buffer, level);
//        case P -> rule2(buffer, level);
//        case E -> true;
//      };
//    } else {
//      return false;
//    }
//
//    if (passed) {
//      passed = switch (buffer[2]) {
//        case B -> !rule3(buffer, level);
//        case P -> rule3(buffer, level);
//        case E -> true;
//      };
//    } else {
//      return false;
//    }

    return true;

  }

  private void print(CellType[] buffer, int level) {
    StringBuilder r = new StringBuilder();
    for (int i = 0; i <= level; i++) {
      r.append(buffer[i]);
    }

    System.out.println(r);
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
