package com.archiver;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.archiver.exception.NoSuchZipFileException;
import com.archiver.exception.PathNotFoundException;

public class ZipFileManager {
  private Path zipFile;

  public ZipFileManager(Path zipFile) {
    this.zipFile = zipFile;
  }

  private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
    Path fullPath = filePath.resolve(fileName);
    try (InputStream is = Files.newInputStream(fullPath)) {
      ZipEntry entry = new ZipEntry(fileName.toString());
      zipOutputStream.putNextEntry(entry);
      copyData(is, zipOutputStream);
      zipOutputStream.closeEntry();
    }
  }

  private void copyData(InputStream in, OutputStream out) throws Exception {
    byte[] buffer = new byte[1024];
    int len;
    while ((len = in.read(buffer)) > 0) {
      out.write(buffer, 0, len);
    }
  }

  public void createZip(Path source) throws Exception {
    Path zipDirectory = zipFile.getParent();
    if (Files.notExists(zipDirectory))
      Files.createDirectories(zipDirectory);

    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
      if (Files.isDirectory(source)) {
        FileManager fileManager = new FileManager(source);
        List<Path> fileNames = fileManager.getFileList();
        for (Path fileName : fileNames)
          addNewZipEntry(zos, source, fileName);
      } else if (Files.isRegularFile(source)) {
        addNewZipEntry(zos, source.getParent(), source.getFileName());
      } else {
        throw new PathNotFoundException();
      }
    }
  }

  public List<FileProperties> getFileList() throws Exception {
    if (!Files.isRegularFile(zipFile)) {
      throw new NoSuchZipFileException();
    }
    List<FileProperties> files = new ArrayList<>();
    try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copyData(zis, baos);
        files.add(new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(),
            zipEntry.getMethod()));
        zipEntry = zis.getNextEntry();
      }
    }
    return files;
  }

  public void extractAll(Path outputFolder) throws Exception {
    if (!Files.isRegularFile(zipFile)) {
      throw new NoSuchZipFileException();
    }
    try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
      if (Files.notExists(outputFolder))
        Files.createDirectories(outputFolder);
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        Path filePath = outputFolder.resolve(zipEntry.getName());
        Path parent = filePath.getParent();
        if (Files.notExists(parent))
          Files.createDirectories(parent);
        try (OutputStream os = Files.newOutputStream(filePath)) {
          copyData(zis, os);
        }
        zipEntry = zis.getNextEntry();
      }
    }
  }

  public void removeFile(Path path) throws Exception {
    removeFiles(Collections.singletonList(path));
  }

  public void removeFiles(List<Path> pathList) throws Exception {
    if (!Files.isRegularFile(zipFile)) {
      throw new NoSuchZipFileException();
    }
    Path tempZipFile = Files.createTempFile(null, null);
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
      try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
          Path archivedFile = Path.of(zipEntry.getName());
          if (!pathList.contains(archivedFile)) {
            zos.putNextEntry(new ZipEntry(zipEntry.getName()));
            copyData(zis, zos);
            zos.closeEntry();
            zis.closeEntry();
          } else {
            ConsoleHelper
                .writeMessage(String.format("File '%s' was removed from the archive.", archivedFile.toString()));
          }
          zipEntry = zis.getNextEntry();
        }
      }
    }
    Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
  }

  public void addFile(Path path) throws Exception {
    addFiles(Collections.singletonList(path));
  }

  public void addFiles(List<Path> absolutePathList) throws Exception {
    if (!Files.isRegularFile(zipFile)) {
      throw new NoSuchZipFileException();
    }
    Path tempZipFile = Files.createTempFile(null, null);
    List<Path> archiveFiles = new ArrayList<>();
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
      try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
          archiveFiles.add(Path.of(zipEntry.getName()));
          zos.putNextEntry(new ZipEntry(zipEntry.getName()));
          copyData(zis, zos);
          zis.closeEntry();
          zos.closeEntry();
          zipEntry = zis.getNextEntry();
        }
      }
      for (Path file : absolutePathList) {
        if (Files.isRegularFile(file)) {
          if (archiveFiles.contains(file.getFileName()))
            ConsoleHelper.writeMessage(String.format("File '%s' already exists in the archive.", file.toString()));
          else {
            addNewZipEntry(zos, file.getParent(), file.getFileName());
            ConsoleHelper.writeMessage(String.format("File '%s' was added to the archive.", file.toString()));
          }
        } else
          throw new PathNotFoundException();
      }
    }
    Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
  }
}
