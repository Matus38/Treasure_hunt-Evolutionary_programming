
import java.awt.SystemTray;
import java.util.Random;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Jedinec {

    private double fitnes = 2;
    private int[] cells = new int[64];
    String path = "";
    private int lastIndexWithTreasure;
    int treasuresCount = 0;

    public Jedinec() {

    }

    public Jedinec(int[] cells, int last) {
        System.arraycopy(cells, 0, this.cells, 0, cells.length);
        lastIndexWithTreasure = last;
    }

    public void generateCells() {
        Random rand = new Random();

        for (int i = 0; i < 64; i++) {
            cells[i] = rand.nextInt(256);
        }
    }
    
    public void generateZero() {

        for (int i = 0; i < 64; i++) {
            cells[i] = 0;//new Random().nextInt(63) +192;
        }
    }
    
    public void setTreasuresCount(){
        treasuresCount++;
    }
    
     public int getTreasuresCount(){
        return treasuresCount;
    }

    public void setLastIndexWithTreasure(int i){
        lastIndexWithTreasure = i;
    }
    
    public int getLastIndexWithTreasure(){
        return lastIndexWithTreasure;
    }
    
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 64; i++) {
            s += cells[i] + " ";
        }
        return s;
    }

    public void setPath(String s) {
        path += s + " ";
    }

    public String getPath() {
        return path;
    }

    public int[] getCells() {
        return cells;
    }

    public void setFitnes(double f) {
        if (fitnes + f < 0) {
            fitnes = 0;
        }
        else {
            fitnes = fitnes + f;
        }
    }

    public double getFitnes() {
        return fitnes;
    }
}
