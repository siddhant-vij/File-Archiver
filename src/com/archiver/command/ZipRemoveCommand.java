package com.archiver.command;

import java.nio.file.Path;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import com.archiver.exception.PathNotFoundException;

public class ZipRemoveCommand extends ZipCommand {
  @Override
  public void execute() throws Exception {
    try {
      ConsoleHelper.writeMessage("\nRemoving files from the archive.");
      ZipFileManager zipFileManager = getZipFileManager();
      ConsoleHelper.askInput("Enter the full path to the file or directory to be removed: ");
      Path path = Path.of(ConsoleHelper.readString());
      zipFileManager.removeFile(path);
      ConsoleHelper.writeMessage("Files removed");
    } catch (PathNotFoundException e) {
      ConsoleHelper.writeMessage("Invalid path for removing files.");
    }
  }
}
