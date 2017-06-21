
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matúš
 */
public class ReadFile {

    private String path;
    int mapSize;
    int totalTreasures;
    int[] startPosition = new int[2];
    int[] treasuresCoordX;
    int[] treasuresCoordY;
    int populationSize;
    int generationCount;
    String selectionMethod;
    InputStream fis;
    InputStreamReader isr;
    RandomAccessFile raf;
    File file;
    int step = 0;
    boolean isNext = true;

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public String getSelectionMethod() {
        return selectionMethod;
    }

    public boolean getIsNext() {
        return isNext;
    }

    public void loadData() {
        try {
//            for(int j = 0; j<step;j++){
//                System.out.println(raf.readLine());
//            }
//            if(raf.readLine() == null)
//            return false;

            mapSize = Integer.valueOf(raf.readLine());
            String[] tmp = raf.readLine().split(" ");
            startPosition[0] = Integer.valueOf(tmp[0]);
            startPosition[1] = Integer.valueOf(tmp[1]);
            totalTreasures = Integer.valueOf(raf.readLine());
            treasuresCoordX = new int[totalTreasures];
            treasuresCoordY = new int[totalTreasures];

            for (int i = 0; i < totalTreasures; i++) {
                String[] s = raf.readLine().split(" ");
                treasuresCoordX[i] = Integer.valueOf(s[0]);
                treasuresCoordY[i] = Integer.valueOf(s[1]);
            }
            populationSize = Integer.valueOf(raf.readLine());
            generationCount = Integer.valueOf(raf.readLine());
            selectionMethod = raf.readLine();
            if (raf.readLine() != null) {
                isNext = true;
            }
            else {
                isNext = false;
            }

        }
        catch (IOException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
            isNext = false;
        }
    }

    public ReadFile(String file_path) {
        try {
            this.path = file_path;
            file = new File(file_path);
            raf = new RandomAccessFile(file, "r");
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public String[] openFile() throws IOException {
//        FileReader fr = new FileReader(path);
//        BufferedReader br = new BufferedReader(fr);
//
//        int lines = getLines();
//        String[] data = new String[lines];
//        int i;
//
//        for (i = 0; i < lines; i++) {
//            data[i] = br.readLine();
//        }
//        br.close();
//
//        return data;
//    }
//
//    public int getLines() throws IOException {
//        FileReader fr = new FileReader(path);
//        BufferedReader br = new BufferedReader(fr);
//
//        int count = 0;
//
//        while (br.readLine() != null) {
//            count++;
//        }
//        br.close();
//        return count;
//    }
    public int getMapSize() {
        return mapSize;
    }

    public int geTotalTreasures() {
        return totalTreasures;
    }

    public int[] getStartPosition() {
        return startPosition;
    }

    public int[] getTreasuresCoordX() {
        return treasuresCoordX;
    }

    public int[] getTreasuresCoordY() {
        return treasuresCoordY;
    }
}
