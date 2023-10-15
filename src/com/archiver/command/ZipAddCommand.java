package com.archiver.command;

import java.nio.file.Path;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import com.archiver.exception.PathNotFoundException;

public class ZipAddCommand extends ZipCommand {
  @Override
  public void execute() throws Exception {
    try {
      ConsoleHelper.writeMessage("\nAdding files to the archive.");
      ZipFileManager zipFileManager = getZipFileManager();
      ConsoleHelper.askInput("Enter the full path to the file or directory to be added: ");
      Path path = Path.of(ConsoleHelper.readString());
      zipFileManager.addFile(path);
      ConsoleHelper.writeMessage("Files added");
    } catch (PathNotFoundException e) {
      ConsoleHelper.writeMessage("Invalid path for adding files.");
    }
  }  
}
