package org.luxoft.assignments;

import java.util.function.Supplier;
import lombok.Getter;


public class Solution2 {
  private static int ROOMS = 8;

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
    for (int i = 0; i < ROOMS; i++ ) {
      solveForPrize(i);
    }
  }

  private void solveForPrize(int prizeIdx) {

    bombs = 0;
    CellType[] buffer = new CellType[ROOMS];
    backtrace(prizeIdx, 0, buffer);
  }


  private void backtrace(int prizeIdx, int curIdx, CellType[] buffer) {
    buffer[curIdx] = CellType.E;
    backtrace(prizeIdx, curIdx + 1, buffer);

    buffer[curIdx] = CellType.B;
    backtrace(prizeIdx, curIdx + 1, buffer);

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
    Supplier<Boolean> cond3 = () -> cond2.get() && buffer[0] == CellType.P;

    // Here is a bomb and label 3 is false.
    Supplier<Boolean> cond1 = () -> buffer[0] == CellType.B && !cond3.get();

    // The prize is in an odd room if room 2 is empty.
    Supplier<Boolean> cond4 = () -> buffer[1] == CellType.E && prizeIdx % 2 == 0;

    // 3 rooms are empty.
    int emptyRooms = 8 - 1 - bombs;
    Supplier<Boolean> cond5 = () -> emptyRooms == 3;

    // There are 5 bombs in the 8 rooms.
    Supplier<Boolean> cond6 = () -> bombs == 5;

    // Room 6 is not empty.
    Supplier<Boolean> cond7 = () -> buffer[5] != CellType.E;

    // The prize is in room 7
    Supplier<Boolean> cond8 = () -> buffer[6] == CellType.P;

    if (level == 4) {
      return passedRule(cond4, buffer[3]);
    }

    if (level == 2) {
      boolean label1Passed = passedRule(() -> cond1.get(), buffer[0]);

      boolean label2Passed = passedRule(cond2, buffer[1]);

      boolean label3Passed = passedRule(() -> cond3.get(), buffer[2]);

      return label1Passed && label2Passed && label3Passed;
    }

    if (level == 6) {
      return passedRule(cond7, buffer[6]);
    }

    if (level == 7) {

      boolean label5Passed = passedRule(cond5, buffer[4]);

      boolean label6Passed = passedRule(cond6, buffer[5]);

      boolean label8Passed = passedRule(cond8, buffer[7]);

      boolean result = label5Passed && label6Passed && label8Passed;

      if (result) {
        print(buffer, level);
      }

      return result;
    }

    return true;

  }


  private void print(CellType[] buffer, int level) {

    StringBuilder r = new StringBuilder();
    for (int i = 0; i <= level; i++) {
      r.append(buffer[i]);
    }

    System.out.println(
        r + ", bombs: " + bombs + ", empty: " + (8 - 1 - bombs) + ", level: " + level);
  }
}
