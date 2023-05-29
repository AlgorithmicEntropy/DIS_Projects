package de.dis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PageFile {
    private int pageId;
    private File file;

    public PageFile(int pageId) {
        this.pageId = pageId;
        this.file = new File("files" + File.separator + "Page_" + pageId + ".txt");
        // create if not exists
        try {
            this.file.getParentFile().mkdirs();
            this.file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save data to page file
     */
    public void save(int lsn, String data) {
        try {
            FileWriter fw = new FileWriter(this.file);
            fw.write(lsn + PersistenceManager.SEPARATOR + data);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getContent() {
        try {
            return Files.readAllLines(this.file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
