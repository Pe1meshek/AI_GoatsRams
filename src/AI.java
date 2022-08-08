import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AI {
    private static final int KOL_DIFF_ELEM = 10;
    private static final int KOL_ELEM_IN_WORD = 4;
    private int counter = 0;

    private List<Kit> tryKit = new ArrayList<>();
    private List<Kit> oldKit = new ArrayList<>();
    private List<Kit> newKit = new ArrayList<>();

    public int[] genAnswer() {
        int[] res = new int[2];
        Scanner scan = new Scanner(System.in);
        while(true){
            //System.out.println("<ram> <goat>");
            res[0] = scan.nextInt();
            res[1] = scan.nextInt();
            if(res[0]>=0 && res[0]<=KOL_ELEM_IN_WORD &&
                    res[1]>=0 && res[1]<=KOL_ELEM_IN_WORD &&
                    res[0]+res[1]<=KOL_ELEM_IN_WORD){
                break;
            }
            else{
                System.out.println("error");
            }
        }
        return res;
    }

    public Kit genRandomNoRepeatKit(){
        Random rand = new Random();
        int[] arrK;

        if(tryKit.size() == KOL_DIFF_ELEM / KOL_ELEM_IN_WORD)
            arrK = new int[KOL_DIFF_ELEM % KOL_ELEM_IN_WORD];
        else arrK = new int[KOL_ELEM_IN_WORD];

        for( int i=0; i<arrK.length; i++){
            arrK[i] = rand.nextInt(KOL_DIFF_ELEM);

            testOnRepeat:
            for(int j=0; j<tryKit.size(); j++){
                int[] arrCpy = tryKit.get(j).getCopyArr();
                for(int g=0; g<arrCpy.length; g++)
                    if(arrK[i] == arrCpy[g]){
                        i--;
                        break testOnRepeat;
                    }
            }
            for(int g=0; g<i; g++)
                if(arrK[i] == arrK[g]){
                    i--;
                    break;
                }
        }
        return new Kit(arrK);
    }

    // SET-GET
    public void addTryKit(Kit k){
        tryKit.add(k);
    }

    public void addOldKit(Kit k){
        oldKit.add(k);
    }

    public void addNewKit(Kit k){
        newKit.add(k);
    }

    public Kit getTryKit(int ind){
        if (ind < 0 || ind > tryKit.size()) {
            return null;
        }
        return tryKit.get(ind);
    }

    public Kit getOldKit(int ind){
        if (ind < 0 || ind > oldKit.size()) {
            return null;
        }
        return oldKit.get(ind);
    }

    public Kit getNewKit(int ind){
        if (ind < 0 || ind > newKit.size()) {
            return null;
        }
        return newKit.get(ind);
    }

    public int getTryKitSize(){return tryKit.size();}

    public int getOldKitSize(){return oldKit.size();}

    public int getNewKitSize(){return newKit.size();}



    public void incrementCounter(){
        counter++;
    }

    public int getCounter(){
        return counter;
    }



    public int getKolElemInWord(){
        return KOL_ELEM_IN_WORD;
    }

    public int getKolDiffElem() {
        return KOL_DIFF_ELEM;
    }
}
