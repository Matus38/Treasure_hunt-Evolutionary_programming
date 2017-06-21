
import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AlgorithmParameterGeneratorSpi;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import sun.tools.jar.resources.jar_pt_BR;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Program {

    int populationSize = 40;
    static final int INCREMENT = 0;
    static final int DECREMENT = 1;
    static final int JUMP = 2;
    static final int PRINT = 3;
    static final int MACHINE_SIZE = 64;
    static final int STEPS_LIMIT = 500;
    static final int OUT_OF_MAP = -100;

    static final int TREASURE = 1;

    Hashtable foundTreasures;
    int[][] map;

    int[] machine = new int[MACHINE_SIZE];
    Jedinec[] population = new Jedinec[populationSize];
    Jedinec[] parents = new Jedinec[populationSize];
    Jedinec actJedinec;
    int machineSteps = 0;
    int actX, actY, startX, startY;
    int mapSize = 7;
    int treasuresCount;
    int foundTreasuresCount = 0;
    boolean allTreasuresFound = false;
    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
    int last = 0;
    int generationCount;
    int actGenerationCount = 1;
    String selectionMethod;
    DecimalFormat decF = new DecimalFormat("##.0000");
    ReadFile rf;
    int bestJedinecIndex = 0;

    public Program() {
        rf = new ReadFile("vstup.txt");
        //rf.loadData();
    }

    //funkcia na ziskanie instrukcie
    int getInstruction(int num) {
        return ((byte) ((num >> 6)));
    }

    //funkcia na urcenie smeru ktorym ma jedinec ist na zaklade dvoch poslednych bitov
    int getWay(int num) {
        int mask = 0b11;
        //System.out.println("WW = " +  (num&mask));
        return (num & mask);
    }

    //funkcia na ziskanie adresy
    int getAddress(int num) {
        return (num & ((1 << 6) - 1));
    }

    //funkcia ktora vykonava instrukcie na zaklade hodnot zo stroja
    int checkInstruction(int cell, int actualPosition) {
        //inkrementacia
        if (getInstruction(cell) == INCREMENT) {
            if (machine[getAddress(cell)] == 255) {
                machine[getAddress(cell)] = 0;
            }
            else {
                machine[getAddress(cell)]++;
            }
            return actualPosition;
        }
        //dekrementacia
        else if (getInstruction(cell) == DECREMENT) {
            if (machine[getAddress(cell)] == 0) {
                machine[getAddress(cell)] = 255;
            }
            else {
                machine[getAddress(cell)]--;
            }
            return actualPosition;
        }
        //skok
        else if (getInstruction(cell) == JUMP) {
            //System.out.println("SKOK NA " + getAddress(cell));
            // - 1 pretoze sa tato hodnota priradi do j vo fori a to sa potom inkrementuje na pozadovanu hodnotu
            return getAddress(cell) - 1;
        }
        //vypis
        else if (getInstruction(cell) == PRINT) {
            //UP
            if (getWay(cell) == 0) {
                if (actY + 1 < mapSize) {
                    //  System.out.print("U ");
                    checkTreasure(actX, actY + 1, actualPosition);
                    actJedinec.setPath("U");
                }
                else {
                    // System.out.print("U ");
                    return OUT_OF_MAP;
                }
            }
            //DOWN
            else if (getWay(cell) == 1) {
                if (actY - 1 > - 1) {
                    // System.out.print("D ");
                    checkTreasure(actX, actY - 1, actualPosition);
                    actJedinec.setPath("D");
                }
                else {
                    // System.out.print("D ");
                    return OUT_OF_MAP;
                }
            }
            //RIGHT
            else if (getWay(cell) == 2) {
                if (actX + 1 < mapSize) {
                    // System.out.print("R ");
                    checkTreasure(actX + 1, actY, actualPosition);
                    actJedinec.setPath("R");
                }
                else {
                    //  System.out.print("R ");
                    return OUT_OF_MAP;
                }
            }
            //LEFT
            else if (getWay(cell) == 3) {
                if (actX - 1 > -1) {
                    // System.out.print("L ");
                    checkTreasure(actX - 1, actY, actualPosition);
                    actJedinec.setPath("L");

                }
                else {
                    //System.out.print("L ");
                    return OUT_OF_MAP;
                }
            }
            return actualPosition;
        }
        return -1;
    }

    //funkcia ktora kontroluje ci jedinec nasiel poklad
    //a zaroven zapisuje do hash tabulky najdene poklady aby sa nehladali rovnake viac krat
    public void checkTreasure(int x, int y, int i) {
        String tempKey;
        tempKey = String.valueOf(x) + String.valueOf(y);
        if (map[x][y] == TREASURE && !foundTreasures.containsKey(tempKey)) {
            //System.out.print("poklad najdeny " + "XY = " + x + "," + y + " ");
            foundTreasures.put(tempKey, tempKey);
            foundTreasuresCount++;
            actJedinec.setLastIndexWithTreasure(i);
            actJedinec.getTreasuresCount();
        }
        actX = x;
        actY = y;
    }

    //inicializacia potrebnych premennych
    public boolean init() throws IOException {

        if (!rf.getIsNext()) {
            System.out.println("Vstupny subor neobsahuje dalsie udaje");
            return false;
        }
        rf.loadData();

        mapSize = rf.getMapSize();
        map = new int[mapSize][mapSize];
        treasuresCount = rf.geTotalTreasures();
        startX = rf.getStartPosition()[0];
        startY = rf.getStartPosition()[1];
        actX = startX;
        actY = startY;
        foundTreasures = new Hashtable(treasuresCount);

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                map[i][j] = 0;
            }
        }
        for (int i = 0; i < treasuresCount; i++) {
            map[rf.getTreasuresCoordX()[i]][rf.getTreasuresCoordY()[i]] = TREASURE;
        }
        populationSize = rf.getPopulationSize();
        generationCount = rf.getGenerationCount();
        selectionMethod = rf.getSelectionMethod();
        actGenerationCount = 1;

        //vygenerovanie prvotnej populacie
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Jedinec();
            population[i].generateCells();
        }
        allTreasuresFound = false;
        return true;
    }

    public void runProgram() throws IOException {
        while (true) {
            if (!init()) {
                break;
            }
            while (!allTreasuresFound || generationCount != actGenerationCount) {
                //nwm++;
                for (int i = 0; i < populationSize; i++) {
                    //skopirovanie buniek do virtualneho stroja
                    System.arraycopy(population[i].getCells(), 0, machine, 0, MACHINE_SIZE);
                    actJedinec = population[i];

                    for (int j = 0; j < MACHINE_SIZE; j++) {
                        //ak sa vykona viac ako je limit krokov skonci sa aktualny program v stroji
                        if (machineSteps++ > STEPS_LIMIT) {
                            break;
                        }
                        //System.out.println("J PRED " + j);
                        j = checkInstruction(machine[j], j);
                        if (j + 1 > MACHINE_SIZE) {
                            //System.out.print("REPEAT J ");
                            j = 0;
                        }
                        if (j < 0) {
                            break;
                        }
                        //System.out.println("J PO " + j);
                        if (treasuresCount == foundTreasuresCount) {
                            System.out.println("Jedinec " + i + " nasiel vsetky poklady\nJeho cesta bola " + actJedinec.getPath());
                            allTreasuresFound = true;
                            break;
                        }
                    }
                    if (allTreasuresFound) {
                        break;
                    }

                    //nastav fitnes
                    population[i].setFitnes((foundTreasuresCount) - (machineSteps * 0.001));
                    if (population[i].getFitnes() > population[bestJedinecIndex].getFitnes()) {
                        bestJedinecIndex = i;
                    }

                    //population[i].setFitnes((foundTreasuresCount*2)  - (machineSteps * ((1.0/(Math.pow(2,foundTreasuresCount)*100)))));
                    // System.out.println("Index = " + i + " Fitnes = " + decF.format(population[i].getFitnes()) + " Machine steps " + machineSteps + " Celkovo " + foundTreasuresCount);
                    //reset hodnot
                    machineSteps = 0;
                    foundTreasuresCount = 0;
                    foundTreasures.clear();
                    actX = startX;
                    actY = startY;
                }

                if (allTreasuresFound) {
                    break;
                }

                if (!allTreasuresFound) {
                    String st;
                    if (actGenerationCount == generationCount) {
                        System.out.println("Neboli najdene vsetky poklady, spustit dalsie kolo? A = ano, N = nie");
                        System.out.println("Najlepsi fitnes ma jedinec s indexom " + bestJedinecIndex + " jeho cesta je " + population[bestJedinecIndex].getPath());
                        st = bufferRead.readLine();
                        generationCount += generationCount;
                    }
                    else {
                        st = "a";
                    }
                    if (st.toUpperCase().equals("A")) {

                        actGenerationCount++;
                        for (int i = 0; i < populationSize; i++) {
                            //vybranie rodicov spomedzi aktualnej populacie
                            if (selectionMethod.equals("ruleta")) {
                                parents[i] = new Jedinec(rouletteSelect(population).getCells(), last);
                            }

                            if (selectionMethod.equals("turnaj")) {
                                parents[i] = new Jedinec(turnamentSelect(population).getCells(), last);
                            }

                            //System.out.println(parents[i].toString());
                        }
                        //60% sanca ze sa rodicia krizia
                        for (int j = 0; j < populationSize; j += 2) {
                            if (new Random().nextInt(100) < 60) {
                                for (int i = 0; i < new Random().nextInt(64); i++) {

                                    int xxx = parents[j + 1].getCells()[i];
                                    parents[j + 1].getCells()[i] = parents[j].getCells()[i];
                                    parents[j].getCells()[i] = xxx;

                                }
                            }
                        }

                        //prekopirovanie do aktualnej populacie
                        System.arraycopy(parents, 0, population, 0, parents.length);

                        for (int i = 0; i < populationSize; i++) {
                            //15% sanca na zmutovanie nahodnej bunky
                            if (new Random().nextInt(100) < 15) {
                                // for (int p = 0; p < new Random().nextInt(64); p++) {

                                //invertovanie nahodneho bitu v nahodnej bunke
                                int pp = new Random().nextInt(64);
                                population[i].getCells()[pp] = population[i].getCells()[pp] ^ (1 << new Random().nextInt(8));
                                //}
//                            if (new Random().nextInt(100) < 8) {
//                               int p= new Random().nextInt(64);
//                                population[i].getCells()[p] = ~population[i].getCells()[p];
//                            }
//                            if (new Random().nextInt(100) < 7) {
//                                int p= new Random().nextInt(64);
//                                population[i].getCells()[p] = new Random().nextInt(255);
//                            }
                            }
                        }

                        //5% sanca na novu krv
                        if (new Random().nextInt(100) < 5) {
                            for (int i = 0; i < 5; i++) {
                                int in = new Random().nextInt(populationSize);
                                population[in].generateCells();
                            }
                        }

                    }
                    else {
                        break;
                    }
                }
            }
            machineSteps = 0;
            foundTreasuresCount = 0;
            foundTreasures.clear();
            actX = startX;
            actY = startY;
            String st;
            System.out.println("Nacitat dalsiu mapu? A = ano, N = nie");
            st = bufferRead.readLine();
            if (!st.toUpperCase().equals("A")) {
                break;
            }
        }
    }

    public Jedinec turnamentSelect(Jedinec[] populacia) {
        int index = 0;
        for (int i = 0; i < 10; i++) {
            int tmp = new Random().nextInt(populationSize);
            if (populacia[index].getFitnes() < populacia[tmp].getFitnes()) {
                index = tmp;
            }
        }

        return populacia[index];
    }

    public Jedinec rouletteSelect(Jedinec[] populacia) {
        int index = -1;
        double sum = 0;
        int random;

        for (int i = 0; i < populacia.length; i++) {
            sum += populacia[i].getFitnes();

        }
        //System.out.println("SUM = " + sum);

        random = new Random().nextInt((int) sum);
        //System.out.println("RANDOM = " + random);

        while (random >= 0) {
            index++;
            if (index > 39) {
                last = populacia[39].getLastIndexWithTreasure();
                return populacia[39];
            }
            random -= populacia[index].getFitnes();
        }

        last = populacia[index].getLastIndexWithTreasure();
        return populacia[index];
    }

    public int[] uniqueRandom(int min, int max, int amount) {
        int[] result = new int[amount];
        int[] tmp = new int[max - min];
        int tmpMax = max - 1;

        for (int i = min; i < max; i++) {
            tmp[i - min] = i;
        }

        for (int j = 0; j < amount; j++) {
            // System.out.print("TMPMAX = " + tmpMax);
            int x = new Random().nextInt(tmpMax + 1);
            result[j] = tmp[x];
            tmp[x] = tmp[tmpMax];
            tmpMax--;
        }
        return result;
    }

}
