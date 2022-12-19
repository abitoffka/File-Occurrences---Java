package word_occurrences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

final class FileWalker {
    private final Occurrences occ;
    private final File rootDir;

    FileWalker(String rootDirPath, Occurrences occ) throws FileNotFoundException {
        this.occ = occ;
        this.rootDir = new File(rootDirPath);
        if (!this.rootDir.isDirectory()) {
            throw new FileNotFoundException(rootDirPath + " does not exist, or is not a directory.");
        }
    }

    void populateOccurrenceMap() {
        try {
            this.populateOccurrenceMap(this.rootDir);
        } catch (IOException var2) {
            System.out.println(var2);
        }

    }

    private void populateOccurrenceMap(File fileOrDir) throws IOException {
        if (!fileOrDir.exists()) {
            System.out.println("" + fileOrDir + " doesn't exist!");
        }

        int line = 1;
        int column = 1;
        int currentColumn = 0;
        if (fileOrDir.isFile()) {
            BufferedReader reader = new BufferedReader(new FileReader(fileOrDir));
            String word = "";

            int ch;
            do {
                ch = reader.read();
                ++currentColumn;
                if (!Syntax.isInWord((char)ch)) {
                    if (word.length() > 0) {
                        FilePosition position = new FilePosition(line, column);
                        this.occ.put(word, fileOrDir.getPath(), position);
                        word = "";
                    }

                    if (Syntax.isNewLine((char)ch)) {
                        ++line;
                        currentColumn = 0;
                    }
                } else {
                    if (word == "") {
                        column = currentColumn;
                    }

                    word = word + (char)ch;
                }
            } while(ch != -1);
            reader.close();
        } else {
            File[] files = fileOrDir.listFiles();
            for(File file : files) {
                this.populateOccurrenceMap(file);
            }
        }

    }
}