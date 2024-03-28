package org.luxoft.assignments;

import java.util.Arrays;

public class Main2 {

  static Boolean[] rooms = new Boolean[8]; // Represents the state of each room: true for prize, false for bomb, null for empty

  public static void generateCombinations() {
    backtrack(0);
  }

  public static boolean isValidCombination() {
    // Condition 1: Here is a bomb and label 3 is false
    if (rooms[0] != null && rooms[0] && (rooms[2] == null || rooms[2])) return false;

    // Condition 2: The prize is not here
    if (rooms[1] != null && rooms[1]) return false;

    // Condition 3: Label 2 is true and the prize is in room 1
    if (rooms[1] == null && (rooms[0] == null || rooms[0]) && (rooms[2] == null || !rooms[2])) return false;

    // Condition 4: The prize is in an odd room if room 2 is empty
    if (rooms[1] == null && (rooms[0] == null || !rooms[0])) {
      if (!anyRoomOccupied(0, 2, 4, 6)) return false;
    }

    // Condition 5: 3 rooms are empty
    if (countEmptyRooms() != 3) return false;

    // Condition 6: There are 5 bombs in the 8 rooms
    if (countBombs() != 5) return false;

    // Condition 7: Room 6 is not empty
    if (rooms[5] == null || rooms[5]) return false;

    // Condition 8: The prize is in room 7
    if (rooms[6] != null && !rooms[6]) return false;

    return true;
  }

  public static void backtrack(int roomIndex) {
    if (roomIndex == 8) {
      if (isValidCombination()) {
        System.out.println("Valid combination: " + Arrays.toString(rooms));
      }
      return;
    }

    // Try room with bomb
    rooms[roomIndex] = false;
    backtrack(roomIndex + 1);

    // Try room with prize
    rooms[roomIndex] = true;
    backtrack(roomIndex + 1);

    // Try empty room
    rooms[roomIndex] = null;
    backtrack(roomIndex + 1);
  }

  public static boolean anyRoomOccupied(int... indices) {
    for (int index : indices) {
      if (rooms[index] != null && rooms[index]) return true;
    }
    return false;
  }

  public static int countEmptyRooms() {
    int count = 0;
    for (Boolean room : rooms) {
      if (room == null) count++;
    }
    return count;
  }

  public static int countBombs() {
    int count = 0;
    for (Boolean room : rooms) {
      if (room != null && !room) count++;
    }
    return count;
  }

  public static void main(String[] args) {
    generateCombinations();
  }
}
