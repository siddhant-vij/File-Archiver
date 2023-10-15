package com.archiver.command;

import java.nio.file.Path;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;

public abstract class ZipCommand implements Command {
  public ZipFileManager getZipFileManager() throws Exception {
    ConsoleHelper.askInput("\nEnter the full path to the archive file: ");
    Path zipPath = Path.of(ConsoleHelper.readString());
    return new ZipFileManager(zipPath);
  }
}
