package com.archiver;

import java.util.HashMap;
import java.util.Map;

import com.archiver.command.Command;
import com.archiver.command.ExitCommand;
import com.archiver.command.ZipAddCommand;
import com.archiver.command.ZipContentCommand;
import com.archiver.command.ZipCreateCommand;
import com.archiver.command.ZipExtractCommand;
import com.archiver.command.ZipRemoveCommand;

public class CommandExecutor {
  private static final Map<Operation, Command> allKnownCommandsMap = new HashMap<>();
  
  private CommandExecutor() {
  }
  
  static {
    allKnownCommandsMap.put(Operation.CREATE, new ZipCreateCommand());
    allKnownCommandsMap.put(Operation.ADD, new ZipAddCommand());
    allKnownCommandsMap.put(Operation.REMOVE, new ZipRemoveCommand());
    allKnownCommandsMap.put(Operation.EXTRACT, new ZipExtractCommand());
    allKnownCommandsMap.put(Operation.CONTENT, new ZipContentCommand());
    allKnownCommandsMap.put(Operation.EXIT, new ExitCommand());
  }

  public static void execute(Operation operation) throws Exception {
    allKnownCommandsMap.get(operation).execute();
  }
}
