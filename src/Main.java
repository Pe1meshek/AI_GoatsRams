import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("0 - Player Bot");
        System.out.println("any - Player Human");
        System.out.print("mode: ");

        boolean botMode = false;
        int valueLoop = 10000;

        Scanner scan = new Scanner(System.in);
        if(scan.nextInt() == 0)
            botMode = true;
        else botMode = false;

        int maxTry = 0;
        double meanTry = 0d;

        for(int I=0; I<(botMode ? valueLoop : 1); I++)
        {

            AI ai = new AI();
            BotPlayer BP = new BotPlayer();
            boolean isWin = false;

            if(botMode)
                BP.genRandomKit(ai.getKolElemInWord(), ai.getKolDiffElem());

            // First stage
            while(true){
                Kit newK = ai.genRandomNoRepeatKit();
                System.out.println(newK);
                int[] ans = botMode ? BP.genAnswer(newK) : ai.genAnswer();
                newK.setSumGR(ans[0]+ans[1]);
                newK.setBeTested(true);
                ai.addTryKit(newK);
                ai.addOldKit(newK.copy());
                ai.addStat(newK,ans[0]);
                ai.incrementCounter();

                if(ans[0]==ai.getKolElemInWord()){
                    isWin = true;
                    break;
                }

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

            // Second stage
            while(!isWin){
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

                    if(newK == null)
                        break;

                    newK = ai.genKitPositionMaxNull(newK,false);
                    System.out.println(newK);
                    int[] ans = botMode ? BP.genAnswer(newK) : ai.genAnswer();
                    newK.setSumGR(ans[0]+ans[1]);
                    newK.setBeTested(true);
                    ai.addTryKit(newK.copy());
                    ai.addOldKit(newK.copy());
                    ai.addStat(newK,ans[0]);
                    ai.incrementCounter();

                    if(ans[0]==ai.getKolElemInWord()){
                        isWin = true;
                        break;
                    }

                    ai.analyzer(base,def);
                    ai.updateFromStat();

//                ai.info();
                }


                ai.removeOldKit();
                ai.newInOld();
            }

            ai.removeNewKit();
            ai.reKit();
            ai.updateFromStat();


//            System.out.println("TRY: "+ai.getCounter());
//            if(botMode)
//                System.out.println(BP.getKit());
//
            ai.info();
            ai.statInfo();


            Kit baseKit = new Kit();
            for (int i=0; i<ai.getOldKitSize();i++)
                baseKit = Kit.sumKit(baseKit,ai.getOldKit(i));

//            Kit tr;
//            int count = 0;
//            do{
//                tr = ai.gen(ai.getKolElemInWord(), baseKit.getListKit(), new Kit());
//                System.out.println(tr);
//                if(tr!=null){
//                    ai.addTryKit(tr.copy());
//                    count++;
//                }
//            }while(tr!=null);
//            for(int i=0; i<count; i++)
//                ai.tryKit.remove(ai.tryKit.size()-1);


            //First stage
            Kit newK = null;
            while (!isWin)
            {
                newK = ai.genKitPositionMaxNull(baseKit,true);
                System.out.println(newK);

                int[] ans = botMode ? BP.genAnswer(newK) : ai.genAnswer();
//                newK.setSumGR(ans[0]+ans[1]);
//                newK.setBeTested(true);
                ai.addTryKit(newK.copy());
                ai.addStat(newK,ans[0]);
                ai.incrementCounter();
                if(ans[0]== ai.getKolElemInWord())
                    break;
            }
            System.out.println("Your word: " + newK);
            System.out.println("TRY: "+ai.getCounter());

            if(ai.getCounter() > maxTry)
                maxTry = ai.getCounter();
            meanTry += ai.getCounter();
        }

        if(botMode){
            System.out.println("max try: " + maxTry);
            System.out.println("mean try: " + meanTry/valueLoop);
        }
    }
}