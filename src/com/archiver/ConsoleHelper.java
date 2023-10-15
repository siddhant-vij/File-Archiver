package com.archiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
  private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  public static void writeMessage(String message) {
    System.out.println(message);
  }

  public static void askInput(String message) {
    System.out.print(message);
  }

  public static String readString() throws IOException {
    return br.readLine();
  }

  public static int readInt() throws NumberFormatException, IOException {
    return Integer.parseInt(readString());
  }
}
