public class Main {
    public static void main(String[] args) {
//        Kit a = new Kit(new int[] {1,2});
//        Kit b = new Kit(new int[] {1,2,6,8});
//        System.out.println(a.info());
//        System.out.println(b.info());
//        int[] arr = b.getArrNum();
//        b.getArrNum()[2]=44;
//        arr[2] = 88;
//        System.out.println(b.info());

        AI ai = new AI();

        // First stage
        while(true){
            Kit k = ai.genRandomNoRepeatKit();
            System.out.println(k);
            int[] ans = ai.genAnswer();
            k.setSumGR(ans[0]+ans[1]);
            k.setBeTested(true);
            ai.addTryKit(k);
            ai.addOldKit(k.copy());
            ai.incrementCounter();

            int sum = 0;
            for(int i=0; i< ai.getTryKitSize(); i++)
                sum += ai.getTryKit(i).getSumGR();
            if(sum == ai.getKolElemInWord()) {
                break;
            }

            if(ai.getTryKitSize() == ai.getKolDiffElem()/ai.getKolElemInWord()) {
                Kit residue = ai.genRandomNoRepeatKit();
                residue.setSumGR(ai.getKolElemInWord()-sum);
                residue.setBeTested(true);
                ai.addOldKit(residue);
                break;
            }
        }


        //Check
//        int preSumGR = 0;
//        for(int i=0; i<ai.getOldKitSize(); i++)
//            preSumGR = ai.getOldKit(i).getSumGR();
//        if(preSumGR!= ai.getKolElemInWord()){
//            System.out.println("ERROR INPUT");
//            return;
//        }

        // Second stage
        while(true){
            ai.removeNewKit();
            ai.reKit();

            boolean isOver = true;
            for(int i=0; i<ai.getOldKitSize();i++)
                if(ai.getOldKit(i).getArrNumSize() != ai.getOldKit(i).getSumGR())
                    isOver = false;
            if(isOver)
                break;

            while(true){
                Kit def = ai.genDefaultKit();
                Kit base = ai.genBaseKit(def);
                Kit newK = ai.genNewWord(base,def);
                if(newK == null) {
                    break;
                }
                System.out.println(newK);
                int[] ans = ai.genAnswer();
                newK.setSumGR(ans[0]+ans[1]);
                newK.setBeTested(true);
                ai.addTryKit(newK.copy());
                ai.addOldKit(newK.copy());
                ai.incrementCounter();
                ai.analyzer(base,def);

//                ai.info();
            }


            ai.removeOldKit();
            ai.newInOld();
        }

        ai.removeNewKit();
        ai.reKit();

        ai.info();
        System.out.println("TRY: "+ai.getCounter());
    }

}