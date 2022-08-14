import java.util.Random;

public class BotPlayer {
    private Kit kit;

    public void genRandomKit(int wordEl, int diffEl){
        Random rand = new Random();
        int[] arrK = new int[wordEl];
        for( int i=0; i<arrK.length; i++){
            arrK[i] = rand.nextInt(diffEl);
            for(int g=0; g<i; g++)
                if(arrK[i] == arrK[g]){
                    i--;
                    break;
                }
        }
        kit = new Kit(arrK);
    }

    public int[] genAnswer(Kit tryKit){
        int[] res = {0,0};
        for(int i : tryKit.getCopyArr())
            for (int j : kit.getCopyArr())
                if(i==j)
                    res[1]++;
        for(int i=0; i<tryKit.getArrNumSize();i++)
            if(tryKit.getCopyArr()[i] == kit.getCopyArr()[i])
                res[0]++;
        res[1]-=res[0];
        return res;
    }

    public void setKit(Kit k){kit = k;}
    public Kit getKit(){return kit;}
}
