package cachesim;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 *
 * @author jacobsheets
 */
public class CacheSim {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main (String[] args) throws FileNotFoundException, IOException{
        

        Scanner myScanner = new Scanner (System.in);
        
// Uncomment for file change.
//        System.out.println("Please enter file name");
//        String fileName = myScanner.nextLine(); 

        String fileName = "data.txt";
        LinkedHashMap <Integer, String> memory = new LinkedHashMap();
        int addresscount = 0;
        
        try (FileReader reader = new FileReader(fileName);
              BufferedReader br = new BufferedReader(reader)) {

               // read line by line
               String line;
               addresscount = 0;
               while ((line = br.readLine()) != null) {
                   memory.put(addresscount, "0x" + line);
                   addresscount++;
               }
        }

        boolean   quit       = false;
        int       loop       = 0;
        int       hits       = 0;
        int []    FIFO       = new int [4];
        int [] [] assCache   = new int [8][2];
        double    hitRatio   = 0.0;
        
        System.out.println("***** CACHE SIM *****");
        System.out.println("press \'q\' for quit\n");
        
        while (!quit) {
       

            System.out.println("Please Enter Address:");
            String inputString = myScanner.nextLine();
            int     address     = 0;
            boolean isNumber    = false;
            
            // QUIT FUNCTION
            if("q".equals(inputString)){
                break;
            }
            
            // CHECK INPUT FOR NUMBER            
            try {
                address     = Integer.valueOf(inputString);
                isNumber    = true;
                    
                
            } catch (NumberFormatException e) {
                System.out.println("**ERROR** Not Numerical");
                System.out.println();
                isNumber    = false;
            }

            // OUT OF INDEX CHECK
            if (address > addresscount){
                System.out.println("**ERROR** Out of Index of Memory");
                System.out.println();

            } else if (isNumber) {


                String binaryAddress    = String.format("%6s", Integer.toBinaryString(address)).replace(' ', '0');
                binaryAddress           = binaryAddress.substring(binaryAddress.length() - 6);

                String indexField       = binaryAddress.substring(4);
                String tagField         = binaryAddress.substring(0,4);
                
                int tagFieldB           = Integer.parseInt(tagField, 2);
                boolean found           = false;
                
                // CHECK FOR HIT
                for (int i = 0; i < 8; i++) {

                        String temp = String.format("%6s", Integer.toBinaryString(assCache[i][0])).replace(' ', '0');
                        temp        = temp + indexField;
                        temp        = temp.substring(temp.length() - 6);

                        if (binaryAddress.equals(temp)) {

                            String binaryData = memory.get(address);

                            if (binaryData.equals("0x" + Integer.toHexString(assCache[i][1]).toUpperCase())){
                                found = true;
                                System.out.println("\n**Cache Hit**\n");
                                hits++;
                            }
                        }
                }

                // IF NOT HIT
                if (!found){
                    
                    System.out.println("\n**Cache Miss**\n");
                    String data = "";
                    // SET 0
                    if ("00".equals(indexField)){

                        if (assCache[0][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[0][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[0][0] = tagFieldB;

                            FIFO[0] = 2;

                        } else if (assCache[1][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[1][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[1][0] = tagFieldB;

                            FIFO[0]        = 1;

                        } else {

                            switch (FIFO[0]) {
                                case 1:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[0][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[0][0] = tagFieldB;

                                    FIFO[0]        = 2;
                                    break;

                                case 2:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[1][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[1][0] = tagFieldB;

                                    FIFO[0]        = 1;
                                    break;
                            }
                        }
                    }

                    // SET 1
                    if ("01".equals(indexField)){

                        if (assCache[2][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[2][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[2][0] = tagFieldB;

                            FIFO[1]        = 2;

                        } else if (assCache[3][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[3][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[3][0] = tagFieldB;

                            FIFO[1]        = 1;

                        } else {

                            switch (FIFO[1]) {
                                case 1:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[2][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[2][0] = tagFieldB;

                                    FIFO[1]        = 2;
                                    break;

                                case 2:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[3][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[3][0] = tagFieldB;

                                    FIFO[1]        = 1;
                                    break;
                            }
                        }
                    }

                    // SET 2
                    if ("10".equals(indexField)){


                        if (assCache[4][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[4][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[4][0] = tagFieldB;

                            FIFO[2]        = 2;

                        } else if (assCache[5][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[5][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[5][0] = tagFieldB;

                            FIFO[2]        = 1;

                        } else {

                            switch (FIFO[1]) {
                                case 1:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[4][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[4][0] = tagFieldB;

                                    FIFO[2]        = 2;
                                    break;

                                case 2:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[5][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[5][0] = tagFieldB;

                                    FIFO[2]        = 1;
                                    break;
                            }
                        }
                    }

                    // SET 3
                    if ("11".equals(indexField)){


                        if (assCache[6][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[6][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[6][0] = tagFieldB;

                            FIFO[3]        = 2;

                        } else if (assCache[7][1] == 0){
                            data           = memory.get(address);
                            data           = data.trim();
                            assCache[7][1] = Integer.parseInt(data.substring(2), 16);
                            assCache[7][0] = tagFieldB;

                            FIFO[3]        = 1;

                        } else {

                            switch (FIFO[1]) {
                                case 1:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[6][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[6][0] = tagFieldB;

                                    FIFO[3]        = 2;
                                    break;

                                case 2:
                                    data           = memory.get(address);
                                    data           = data.trim();
                                    assCache[7][1] = Integer.parseInt(data.substring(2), 16);
                                    assCache[7][0] = tagFieldB;

                                    FIFO[3]        = 1;
                                    break;
                            }
                        }
                    }

                }

                // RESULTS
                
                if (hits > 0) {
                    hitRatio = (hits * 1.0)/loop; 
                }
                loop++;

                System.out.println  ("-----------CACHE RESULTS------------");
                System.out.format   ("SET 0: %s %s   %s %s \n", fixTag(assCache[0][0]), fixdata(assCache[0][1]), fixTag(assCache[1][0]), fixdata(assCache[1][1]));
                System.out.format   ("SET 1: %s %s   %s %s \n", fixTag(assCache[2][0]), fixdata(assCache[2][1]), fixTag(assCache[3][0]), fixdata(assCache[3][1]));
                System.out.format   ("SET 2: %s %s   %s %s \n", fixTag(assCache[4][0]), fixdata(assCache[4][1]), fixTag(assCache[5][0]), fixdata(assCache[5][1]));
                System.out.format   ("SET 3: %s %s   %s %s \n", fixTag(assCache[6][0]), fixdata(assCache[6][1]), fixTag(assCache[7][0]), fixdata(assCache[7][1]));
                System.out.format   ("HITS: %d \n", hits);
                System.out.format   ("HIT RATIO: %f \n", hitRatio);
                System.out.println  ("------------------------------------");
                System.out.println  ();
                System.out.println  ();
            }
        }
    }
    
    public static String fixTag(int x) {
       
        String temp = String.format("%6s", Integer.toBinaryString(x)).replace(' ', '0');
        temp        = temp.substring(temp.length() - 4);
        return temp;
    }
    
    public static String fixdata(int x) {

        String temp = Integer.toHexString(x).toUpperCase();
        return temp;
    }
}