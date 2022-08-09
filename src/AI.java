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

    public void reKit(){
        for(int i=0; i<oldKit.size(); i++)
            if(oldKit.get(i).getSumGR()==0){
                oldKit.remove(i);
                i--;
            }

        int max = 0;
        for(Kit k : oldKit)
            if(k.getArrNumSize() > max) {
                max = k.getArrNumSize();
            }
        if(max<2) {
            return;
        }

        for (int i=0; i<oldKit.size(); i++)
            if(oldKit.get(i).getArrNumSize()==max){
                int[] newArr;
                int[] arrKit = oldKit.get(i).getCopyArr();
                for(int j=0; j<arrKit.length; ){
                    newArr = new int[max/2];
                    for(int g=0; g<newArr.length; g++){
                        newArr[g] = arrKit[j];
                        j++;
                    }
                    if(oldKit.get(i).isBeTested() && oldKit.get(i).getSumGR() == oldKit.get(i).getArrNumSize()){
                        Kit newK = new Kit(newArr);
                        newK.setSumGR( newArr.length );
                        newK.setBeTested(true);
                        newKit.add(newK);
                    }
                    else
                        newKit.add(new Kit(newArr));
                }
            }
            else newKit.add(oldKit.get(i).copy());
    }

    public void newInOld(){
        for(Kit newK : newKit)
            oldKit.add(newK.copy());
    }

    public Kit genNewWord(Kit baseKit, Kit defaultKit){
        if(baseKit==null || defaultKit==null)
            return null;

        outside:
        for(Kit k : newKit)
            if( !k.isBelong(baseKit) && !k.isBelong(defaultKit) && !k.isBeTested() ){
                for(Kit oldK : oldKit)
                    if(Kit.sumKit(baseKit,k).isBelong(oldK))
                        continue outside;
                if(defaultKit.getArrNumSize()>0)
                    return Kit.sumKit( Kit.sumKit(baseKit,k), defaultKit);
                else return Kit.sumKit(baseKit,k);
            }
        return null;
    }

    public Kit genDefaultKit(){
        Kit def = null;
        if(KOL_ELEM_IN_WORD - newKit.get(0).getArrNumSize()*2 != 0){
            for( Kit oldK : oldKit)
                if(KOL_ELEM_IN_WORD - newKit.get(0).getArrNumSize()*2 == oldK.getArrNumSize()){
                    def = oldK;
                    for (int j = 0; j < newKit.size()-1; j++)
                        for (int g = j+1; g < newKit.size(); g++)
                            if(newKit.get(j).isBelong(oldK) && newKit.get(g).isBelong(oldK) &&
                                newKit.get(j).isBeTested() && newKit.get(g).isBeTested())
                                return def;
                }
        }
        else
            def = new Kit();
        return def;
    }

    public Kit genBaseKit(Kit def){
        Kit base = null;
        for(Kit newK : newKit)
            if( !newK.isBelong(def) ){
                base = newK;
                if(!newK.isBeTested() && newK.getArrNumSize()==1)
                    break;
                if(newK.isBeTested() && newK.getArrNumSize()!=1)
                    break;
            }
        return base;
    }

    public void analyzer(Kit baseKit, Kit defaultKit){
        if(baseKit.isBeTested()) {
            for (Kit k : newKit)
                if (k.isBelong(tryKit.get(tryKit.size() - 1)) && !k.isBelong(baseKit)) {
                    k.setSumGR(tryKit.get(tryKit.size() - 1).getSumGR() - baseKit.getSumGR() - defaultKit.getSumGR());
                    k.setBeTested(true);

                    for (Kit secEl : newKit)
                        for (Kit overall : oldKit)
                            if (!secEl.isBeTested() && overall.isBelong( Kit.sumKit(k,secEl) )) {
                                secEl.setSumGR(overall.getSumGR() - k.getSumGR());
                                secEl.setBeTested(true);
                            }
                    return;
                }
        }
        else{
            if(oldKit.get(oldKit.size()-1).getSumGR() - defaultKit.getSumGR() == 0 ||
                oldKit.get(oldKit.size()-1).getSumGR() - defaultKit.getSumGR() == KOL_ELEM_IN_WORD-defaultKit.getArrNumSize()){
                for(Kit thatK : newKit)
                    if( Kit.sumKit( thatK, Kit.sumKit(baseKit, defaultKit) ).isBelong(oldKit.get(oldKit.size()-1)) ){
                        if(oldKit.get(oldKit.size()-1).getSumGR() - defaultKit.getSumGR() == 0){
                            baseKit.setSumGR(0);
                            thatK.setSumGR(0);
                        }
                        else{
                            baseKit.setSumGR(baseKit.getArrNumSize());
                            thatK.setSumGR(thatK.getArrNumSize());
                        }
                        baseKit.setBeTested(true);
                        thatK.setBeTested(true);
                        for( Kit newK : newKit )
                            if( !newK.isBeTested() )
                                for( Kit oldK : oldKit )
                                    if( oldK.isBelong( Kit.sumKit(baseKit, newK) ) ||
                                        oldK.isBelong( Kit.sumKit(thatK, newK) ) ){
                                        newK.setSumGR(oldK.getSumGR()-baseKit.getSumGR());
                                        newK.setBeTested(true);
                                    }
                    }
                return;
            }

            int count = 0;
            for(Kit oldK : oldKit)
                if(baseKit.isBelong(oldK))
                    count++;
            if(count>=3)
                for(Kit withOutBase : oldKit)
                    for(Kit WOB1 : newKit)
                        for(Kit WOB2 : newKit)
                            if( tryKit.get(tryKit.size()-1).isBelong( Kit.sumKit( WOB1, Kit.sumKit(baseKit,defaultKit) ) ) &&
                                tryKit.get(tryKit.size()-2).isBelong( Kit.sumKit( WOB2, Kit.sumKit(baseKit,defaultKit) ) ) &&
                                withOutBase.isBelong( Kit.sumKit(WOB1,WOB2) )){

                                baseKit.setSumGR( ( tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() +
                                                    tryKit.get(tryKit.size()-2).getSumGR() - defaultKit.getSumGR() -
                                                    withOutBase.getSumGR() ) / 2 );
                                baseKit.setBeTested(true);

                                for (Kit secEl : newKit)
                                    for (Kit overall : oldKit)
                                        if (!secEl.isBeTested() && overall.isBelong( Kit.sumKit(baseKit,secEl) )) {
                                            secEl.setSumGR(overall.getSumGR() - baseKit.getSumGR());
                                            secEl.setBeTested(true);
                                        }
                                return;
                            }
        }
    }

    public void info(){
        System.out.println("=== TRY KIT ===");
        for(Kit k : tryKit)
            System.out.println(k.info());

        System.out.println("=== OLD KIT ===");
        for(Kit k : oldKit)
            System.out.println(k.info());

        System.out.println("=== NEW KIT ===");
        for(Kit k : newKit)
            System.out.println(k.info());
    }

    // SET-GET
    public void addTryKit(Kit k){tryKit.add(k);}

    public void addOldKit(Kit k){oldKit.add(k);}

    public void addNewKit(Kit k){newKit.add(k);}

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

    public void removeOldKit(){ while(!oldKit.isEmpty()) oldKit.remove(0);}
    public void removeNewKit(){ while(!newKit.isEmpty()) newKit.remove(0);}



    public void incrementCounter(){counter++;}

    public int getCounter(){return counter;}


    public int getKolElemInWord(){return KOL_ELEM_IN_WORD;}

    public int getKolDiffElem() {return KOL_DIFF_ELEM;}
}
