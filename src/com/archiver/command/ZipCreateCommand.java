package com.archiver.command;

import java.nio.file.Path;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import com.archiver.exception.PathNotFoundException;

public class ZipCreateCommand extends ZipCommand {
  @Override
  public void execute() throws Exception {
    try {
      ConsoleHelper.writeMessage("\nCreating an archive.");
      ZipFileManager zipFileManager = getZipFileManager();
      ConsoleHelper.askInput("Enter the full name of the file or directory to be archived: ");
      Path sourcePath = Path.of(ConsoleHelper.readString());
      zipFileManager.createZip(sourcePath);
      ConsoleHelper.writeMessage("Archive created.");
    } catch (PathNotFoundException e) {
      ConsoleHelper.writeMessage("You didn't correctly enter a file name or directory.");
    }
  }
}
