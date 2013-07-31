import java.io.*;

/**
 * Display statistics about the directories in the tree.
 * Usage java TreeLines [fileEnding] [fileEnding] [fileEnding]
 * @author James Stauffer
 */
public class TreeLines {
    public static void main(String[] args) {
        FileExtensions = args;
        System.out.println("Files \tLines \tChars \tDirectory");
        System.out.println(ProcessDir(new File(".")) + ".");
    }

    private static String[] FileExtensions;
    private static FileFilter filter = new Filter();
    private static int LinesThreshold = 3000;

    private static Info ProcessDir(File dir) {
        Info info = new Info();
        File[] files = dir.listFiles(filter);
        for(int index = 0; index < files.length; index++) {
            if(files[index].isDirectory()) {
                info.add(ProcessDir(files[index]));
            } else {
                info.add(ProcessFile(files[index]));
            }
        }
        if(info.getLines() > LinesThreshold) {
            System.out.println(info + dir.getPath());
        }
        return info;
    }

    private static Info ProcessFile(File file) {
        int lines = 0;
        int characters = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                characters += line.length();
                lines++;
            }
            br.close();
        } catch(IOException e) {
            System.err.println(file);
            e.printStackTrace();
        }
        return new Info(lines, characters);
    }

    static class Filter implements FileFilter {
        public boolean accept(File pathname) {
            if(FileExtensions.length > 0) {
                for(int index = 0; index < FileExtensions.length; index++) {
                    if(pathname.isDirectory() || pathname.getName().endsWith(FileExtensions[index])) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        }
    }

    static class Info {
        public Info() {
            this.lines = 0;
            this.characters = 0;
            this.files = 0;
        }

        public Info(int lines, int characters) {
            this.lines = lines;
            this.characters = characters;
            this.files = 1;
        }

        public void add(Info info) {
            lines += info.lines;
            characters += info.characters;
            files += info.files;
        }

        public int getLines() {
            return lines;
        }

        public String toString() {
            return files + " \t" + lines + " \t" + characters + " \t";
        }

        private int lines;
        private int characters;
        private int files;
    }
}
