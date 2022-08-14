import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.abs;

public class AI {
    private static final int KOL_DIFF_ELEM = 10;
    private static final int KOL_ELEM_IN_WORD = 4;
    private int counter = 0;


    private List<Kit> tryKit = new ArrayList<>();
    private List<Kit> oldKit = new ArrayList<>();
    private List<Kit> newKit = new ArrayList<>();
    private List<PositionStat> stat = new ArrayList<>();


    public void addStat(Kit last, int ram){
        if(last.getSumGR()==0)
            return;
        int[] arr = last.getCopyArr();

        outside:
        for(int i=0; i<arr.length; i++)
            if(stat.isEmpty()){
                stat.add(new PositionStat(arr[i],KOL_ELEM_IN_WORD));
                stat.get(0).addStat(i,ram);
            }
            else{
                for (PositionStat ps : stat)
                    if(ps.getNUM()==arr[i]){
                        ps.addStat(i,ram);
                        continue outside;
                    }
                stat.add(new PositionStat(arr[i],KOL_ELEM_IN_WORD));
                stat.get(stat.size()-1).addStat(i,ram);
            }
    }

    public void updateFromStat(){
        outside:
        for(PositionStat ps : stat){
            for(Integer ips : ps.getCopyArrPos())
                if(ips == null || ips != 0)
                    continue outside;
            ps.setNeed(false);
        }

        outside:
        for(PositionStat ps : stat){
            for( Kit k : oldKit)
                for( int i : k.getCopyArr())
                    if(ps.getNUM() == i)
                        continue outside;
            ps.setNeed(false);
        }

        if(!newKit.isEmpty() && newKit.get(0).getArrNumSize()==1)
            for (Kit newK : newKit)
                for (PositionStat ps : stat)
                    if(newK.getCopyArr()[0] == ps.getNUM() && !ps.isNeed())
                        newK.setBeTested(true);
    }

//    public void rec(int lvl, List<Kit> lk,Kit res){
//        if(lvl==0){
//            System.out.println(res);
//            return;
//        }
//        outside:
//        for (Kit k : lk){
//            Kit newRes = Kit.sumKit(res,k);
//            for(Kit r : res.getListKit())
//                if(k.isFullEqual(r))
//                    continue outside;
//                rec(lvl-1,lk,newRes);
//        }
//    }

    public Kit genKitPositionMaxNull(Kit kit,boolean modeThirdStage){
        int numNull = 0;
        int count = 0;

        Kit foundMaxNull = gen(KOL_ELEM_IN_WORD, kit.getListKit(), new Kit());

        if(foundMaxNull==null)
            foundMaxNull = kit;

        while(true){
            int countNull = 0;
            kit = gen(KOL_ELEM_IN_WORD, kit.getListKit(), new Kit());

            if(kit==null)
                break;

            for (int i=0; i<kit.getListKit().size(); i++)
                for (PositionStat ps : stat)
                    if(ps.getNUM() == kit.getListKit().get(i).getCopyArr()[0])
                        if(modeThirdStage){
                            if(ps.getCopyArrPos()[i]!=null){
                                countNull+=ps.getCopyArrPos()[i];
                                break;
                            }
                        }
                        else if(ps.getCopyArrPos()[i]==null){
                            countNull++;
                            break;
                        }
            tryKit.add(kit.copy());
            count++;
            if(countNull>numNull){
                numNull = countNull;
                foundMaxNull = kit;
            }
        }
        for(int i=0;i<count;i++)
            tryKit.remove(tryKit.size()-1);
        return foundMaxNull;
    }

    public Kit gen(int lvl, List<Kit> lk,Kit res){
        if(lvl==0){
            for(Kit tryK : tryKit)
                if(res.isFullEqual(tryK))
                    return null;
            return res;
        }
        outside:
        for (Kit k : lk){
            Kit newRes = Kit.sumKit(res,k);
            for(Kit r : res.getListKit())
                if(k.isFullEqual(r))
                    continue outside;

            for (PositionStat ps : stat)
                if(k.getCopyArr()[0] == ps.getNUM())
                    if (ps.getStatusPosition(abs(lvl-KOL_ELEM_IN_WORD)) != null &&
                        ps.getStatusPosition(abs(lvl-KOL_ELEM_IN_WORD)) < 1)
                        continue outside;

            Kit preRes =  gen(lvl-1,lk,newRes);
            if(preRes == null)
                continue outside;
            return preRes;
        }
        return null;
    }

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

    public void analyzerForNew(Kit newK){
        for (Kit secEl : newKit)
            for (Kit overall : oldKit)
                if (!secEl.isBeTested() && overall.isBelong( Kit.sumKit(newK,secEl) )) {
                    secEl.setSumGR(overall.getSumGR() - newK.getSumGR());
                    secEl.setBeTested(true);
                }
    }

    public void analyzer(Kit baseKit, Kit defaultKit){
        if(baseKit.isBeTested()) {
            for (Kit k : newKit)
                if (tryKit.get(tryKit.size() - 1).isBelong( Kit.sumKit(k, Kit.sumKit(baseKit,defaultKit)) ) && !k.isBelong(baseKit)) {

                    k.setSumGR(tryKit.get(tryKit.size() - 1).getSumGR() - baseKit.getSumGR() - defaultKit.getSumGR());
                    k.setBeTested(true);

                    analyzerForNew(k);
                    return;
                }
        }
        else{
            if(tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() == 0 ||
                tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() == KOL_ELEM_IN_WORD-defaultKit.getArrNumSize()){
                for(Kit thatK : newKit)
                    if( tryKit.get(tryKit.size()-1).isBelong( Kit.sumKit( thatK, Kit.sumKit(baseKit, defaultKit) ) ) ){
                        if(tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() == 0){
                            baseKit.setSumGR(0);
                            thatK.setSumGR(0);
                        }
                        else{
                            baseKit.setSumGR(baseKit.getArrNumSize());
                            thatK.setSumGR(thatK.getArrNumSize());
                        }
                        baseKit.setBeTested(true);
                        thatK.setBeTested(true);

                        analyzerForNew(baseKit);
                        analyzerForNew(thatK);
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
                            if( withOutBase.isBelong( Kit.sumKit(WOB1,WOB2) ) &&
                                tryKit.get(tryKit.size()-1).isBelong( Kit.sumKit( WOB1, Kit.sumKit(baseKit,defaultKit) ) ) &&
                                tryKit.get(tryKit.size()-2).isBelong( Kit.sumKit( WOB2, Kit.sumKit(baseKit,defaultKit) ) ) ){

                                baseKit.setSumGR( ( tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() +
                                                    tryKit.get(tryKit.size()-2).getSumGR() - defaultKit.getSumGR() -
                                                    withOutBase.getSumGR() ) / 2 );
                                baseKit.setBeTested(true);

                                WOB1.setSumGR( tryKit.get(tryKit.size()-1).getSumGR() - defaultKit.getSumGR() - baseKit.getSumGR() );
                                WOB2.setSumGR( tryKit.get(tryKit.size()-2).getSumGR() - defaultKit.getSumGR() - baseKit.getSumGR() );
                                WOB1.setBeTested(true);
                                WOB2.setBeTested(true);

                                analyzerForNew(baseKit);
                                analyzerForNew(WOB1);
                                analyzerForNew(WOB2);
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

    public void statInfo(){
        for (int i=0; i<KOL_DIFF_ELEM; i++)
            for( PositionStat ps : stat)
                if(i == ps.getNUM())
                    System.out.println(ps);
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
