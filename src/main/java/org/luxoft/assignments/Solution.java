package org.luxoft.assignments;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class Solution {

  private static final int ROOMS = 8;

  private int bombs;

  private int prizeIdx;

  // The prize is not here.
  private final Function<CellType[], Boolean> cond2 = (CellType[] buffer) -> buffer[1]
      != CellType.P;

  // Label 2 is true and the prize is in room 1
  private final Function<CellType[], Boolean> cond3 = (CellType[] buffer) -> cond2.apply(buffer)
      && buffer[0] == CellType.P;

  // Here is a bomb and label 3 is false.
  private final Function<CellType[], Boolean> cond1 = (CellType[] buffer) -> buffer[0] == CellType.B
      && !cond3.apply(buffer);

  // The prize is in an odd room if room 2 is empty.
  private final Function<CellType[], Boolean> cond4 = (CellType[] buffer) -> buffer[1] == CellType.E
      && prizeIdx % 2 == 0;

  // 3 rooms are empty.
  private final Function<CellType[], Boolean> cond5 = (CellType[] buffer) -> ROOMS - 1 - bombs == 3;

  // There are 5 bombs in the 8 rooms.
  private final Function<CellType[], Boolean> cond6 = (CellType[] buffer) -> bombs == 5;

  // Room 6 is not empty.
  private final Function<CellType[], Boolean> cond7 = (CellType[] buffer) -> buffer[5]
      != CellType.E;

  // The prize is in room 7
  private final Function<CellType[], Boolean> cond8 = (CellType[] buffer) -> buffer[6]
      == CellType.P;


  @Getter
  @RequiredArgsConstructor
  private enum CellType {
    P(-1),
    E(0),
    B(1);

    private final int type;

  }

  public void solve() {
    for (prizeIdx = 0; prizeIdx < ROOMS; prizeIdx++) {
      solveForPrize();
    }
  }

  private void solveForPrize() {
    bombs = 0;
    CellType[] buffer = new CellType[ROOMS];
    buffer[prizeIdx] = CellType.P;
    backtrace(0, buffer);
  }


  private void backtrace(int level, CellType[] buffer) {

    if (level > 0 && !checkConditionsPassed(buffer, level)) {
      return;
    }

    if (level == buffer.length) {
      print(buffer);
      return;
    }

    if (prizeIdx != level) {
      buffer[level] = CellType.E;
      backtrace(level + 1, buffer);

      buffer[level] = CellType.B;
      bombs++;
      backtrace(level + 1, buffer);
      bombs--;
    } else {
      backtrace(level + 1, buffer);
    }

  }

  private boolean passedRule(Function<CellType[], Boolean> ruleSupplier, CellType[] buffer,
      int roomIdx) {
    if (buffer[roomIdx] == null) {
      throw new IllegalArgumentException();
    }

    return switch (buffer[roomIdx]) {
      case B -> !ruleSupplier.apply(buffer);
      case P -> ruleSupplier.apply(buffer);
      case E -> true;
    };
  }

  private boolean checkConditionsPassed(CellType[] buffer, int level) {

    boolean result = switch(level) {
      case 1, 2, 5, 6 -> true;
      case 3 -> passedRule(cond1, buffer, 0) && passedRule(cond2, buffer, 1) && passedRule(cond3,
          buffer, 2);
      case 4 -> passedRule(cond4, buffer, 3);
      case 7 -> passedRule(cond7, buffer, 6);
      case 8 -> passedRule(cond5, buffer, 4) && passedRule(cond6, buffer, 5) && passedRule(cond8,
          buffer, 7);
      default -> throw new IllegalArgumentException();
    };

    return result;

  }


  private void print(CellType[] buffer) {
    System.out.println(Stream.of(buffer).map(Enum::toString).collect(Collectors.joining("")));
  }
}
