package com.archiver;

import java.io.IOException;

import com.archiver.exception.NoSuchZipFileException;

public class FileArchiver {
  public static Operation askOperation() throws IOException {
    ConsoleHelper.writeMessage("\n\n\nFile Archiver Terminal Utility\n\n");
    ConsoleHelper.writeMessage("Select an operation:");
    ConsoleHelper.writeMessage(String.format("%d - Zip files into an archive", Operation.CREATE.ordinal()));
    ConsoleHelper.writeMessage(String.format("%d - Add a file to an archive", Operation.ADD.ordinal()));
    ConsoleHelper.writeMessage(String.format("%d - Remove a file from an archive", Operation.REMOVE.ordinal()));
    ConsoleHelper.writeMessage(String.format("%d - Extract an archive", Operation.EXTRACT.ordinal()));
    ConsoleHelper.writeMessage(String.format("%d - View the contents of an archive", Operation.CONTENT.ordinal()));
    ConsoleHelper.writeMessage(String.format("%d - Exit", Operation.EXIT.ordinal()));
    ConsoleHelper.askInput("\nYour choice: ");

    return Operation.values()[ConsoleHelper.readInt()];
  }

  public static void main(String[] args) throws Exception {
    Operation operation = null;
    do {
      try {
        operation = askOperation();
        CommandExecutor.execute(operation);
      } catch (NoSuchZipFileException e) {
        ConsoleHelper.writeMessage("You didn't select an archive or you selected an invalid file.");
      } catch (Exception e) {
        ConsoleHelper.writeMessage("An error occurred. Please check the entered data.");
      }

    } while (operation != Operation.EXIT);
  }
}
