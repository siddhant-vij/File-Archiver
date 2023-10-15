package com.archiver.command;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;

public class ZipContentCommand extends ZipCommand {
  @Override
  public void execute() throws Exception {
    ConsoleHelper.writeMessage("\nViewing contents of the archive.");
    ZipFileManager zipFileManager = getZipFileManager();
    ConsoleHelper.writeMessage("Archive contents:");
    zipFileManager.getFileList().forEach(System.out::println);
    ConsoleHelper.writeMessage("Archive contents viewed.");
  }
}
