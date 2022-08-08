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

        // First
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


        for(int i=0; i<ai.getOldKitSize(); i++)
            System.out.println(ai.getOldKit(i).info());


    }
}