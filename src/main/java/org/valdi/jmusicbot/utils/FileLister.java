package org.valdi.jmusicbot.utils;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class FileLister {

    /**
     * Returns a list of yml files in the folder given. If the folder does not exist in the file system
     * it can check the plugin jar instead.
     * @param folderPath - folder path
     * @param checkJar - if true, the jar will be checked
     * @return List of file names

     */
    public static List<String> list(StoreLoader storeLoader, String folderPath, String extension, boolean checkJar) throws IOException {
        List<String> result = new ArrayList<>();
        // Check if the folder exists
        File localeDir = new File(storeLoader.getDataFolder(), folderPath);
        if (localeDir.exists()) {
            FilenameFilter ymlFilter = (File dir, String name) -> name.toLowerCase().endsWith(extension);
            return Arrays.asList(Objects.requireNonNull(localeDir.list(ymlFilter)));
        } else if (checkJar) {
            // Else look in the JAR
            return listJar(storeLoader, folderPath, extension);
        }
        return result;
    }

    public static List<String> listJar(StoreLoader storeLoader, String folderPath, String extension) throws IOException {
        List<String> result = new ArrayList<>();
        // Look in the JAR
        File jarfile = storeLoader.getJarFile();
        if(jarfile.isDirectory()) {
            File folder = new File(jarfile, folderPath);
            FilenameFilter ymlFilter = (File dir, String name) -> name.toLowerCase().endsWith(extension);
            String[] files = folder.list(ymlFilter);
            if(files == null) {
                return new ArrayList<>();
            }
            return Arrays.asList(files);
        }

        JarFile jar = new JarFile(jarfile);

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String path = entry.getName();

            if (!path.startsWith(folderPath)) {
                continue;
            }

            if (entry.getName().endsWith(extension)) {
                result.add(entry.getName());
            }

        }
        jar.close();

        return result;
    }
}
