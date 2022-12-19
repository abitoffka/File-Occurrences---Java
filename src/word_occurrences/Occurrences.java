
package word_occurrences;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Occurrences {
    private final TreeMap<String, TreeMap<String, TreeSet<FilePosition>>> occMap = new TreeMap();

    public Occurrences(String rootDirPath) throws FileNotFoundException {
        FileWalker walker = new FileWalker(rootDirPath, this);
        walker.populateOccurrenceMap();
    }

    void put(String word, String filePath, FilePosition pos) {
        word = word.toLowerCase();
        TreeMap<String, TreeSet<FilePosition>> trmp = new TreeMap();
        TreeSet trst1;
        if (this.occMap.containsKey(word)) {
            trmp = this.occMap.get(word);
            trst1 = trmp.get(filePath);
            if (trst1 == null) {
                TreeSet<FilePosition> trst2 = new TreeSet();
                trst2.add(pos);
                trmp.put(filePath, trst2);
            } else {
                trst1.add(pos);
                trmp.put(filePath, trst1);
                this.occMap.put(word, trmp);
            }
        } else {
            trst1 = new TreeSet();
            trst1.add(pos);
            trmp.put(filePath, trst1);
            this.occMap.put(word, trmp);
        }

    }

    public int distinctWords() {
        return this.occMap.size();
    }

    public int totalOccurrences() {
        int count = 0;
        Collection<TreeMap<String, TreeSet<FilePosition>>> trmpCollection = this.occMap.values();

        for (TreeMap<String, TreeSet<FilePosition>> trmp : trmpCollection) {
            Collection<TreeSet<FilePosition>> trstCollection = trmp.values();
            for (TreeSet<FilePosition> trst : trstCollection) {
                count += trst.size();
            }
        }
        return count;
    }

    public int totalOccurrencesOfWord(String word) {
        if (this.occMap.get(word) == null) {
            return 0;
        } else {
            int count = 0;
            TreeMap<String, TreeSet<FilePosition>> trmp = this.occMap.get(word);
            Collection<TreeSet<FilePosition>> trstCollection = trmp.values();

            for(TreeSet<FilePosition> trst : trstCollection) {
                count += trst.size();
            }

            return count;
        }
    }

    public int totalOccurrencesOfWordInFile(String word, String filepath) {
        if (this.occMap.get(word) == null) {
            return 0;
        } else {
            TreeMap<String, TreeSet<FilePosition>> trmp = this.occMap.get(word);
            if (trmp.get(filepath) == null) {
                return 0;
            } else {
                TreeSet<FilePosition> trst = trmp.get(filepath);
                return trst.size();
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> setOfWords = this.occMap.keySet();

        for (String word : setOfWords) {
            sb.append("\"" + word + "\" has " + this.totalOccurrencesOfWord(word) + " occurrence(s):\n");
            TreeMap<String, TreeSet<FilePosition>> trmp = this.occMap.get(word);
            Set<String> setFileNames = trmp.keySet();

            for (String fileName : setFileNames) {
                File file = new File(fileName);
                String filepath = file.getPath();
                TreeSet<FilePosition> trstPos = trmp.get(fileName);

                for (FilePosition pos : trstPos) {
                    sb.append("   ( file: \"" + filepath + "\"; " + pos + " )\n");
                }
            }
        }

        return sb.toString();
    }
}


