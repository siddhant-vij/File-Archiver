package com.archiver.command;

import com.archiver.ConsoleHelper;

public class ExitCommand implements Command {
  @Override
  public void execute() throws Exception {
    ConsoleHelper.writeMessage("Bye!");
  }  
}
