import java.util.ArrayList;
import java.util.List;

public class Kit {
    private int[] arrNum;
    private int sumGR;
    private boolean beTested;

    public Kit(){ this.arrNum = new int[0]; }

    public Kit(int[] arrNum) {
        this.arrNum = arrNum;
    }

    public boolean isFullEqual(Kit eq){
        if(arrNum.length != eq.arrNum.length)
            return false;
        for (int i=0; i<arrNum.length; i++)
            if(arrNum[i]!=eq.arrNum[i])
                return false;
        return true;
    }

    public List<Kit> getListKit(){
        List <Kit> res = new ArrayList<>();
        for (int i : arrNum)
            res.add(new Kit( new int[]{i}));
        return res;
    }

    public Kit copy(){
        Kit copy = new Kit();
        copy.arrNum = new int[arrNum.length];
        for(int i=0;i<arrNum.length;i++)
            copy.arrNum[i] = arrNum[i];
        copy.sumGR = sumGR;
        copy.beTested = beTested;
        return copy;
    }

    public int[] getCopyArr(){
        int[] copy = new int[arrNum.length];
        for(int i=0;i<arrNum.length;i++)
            copy[i] = arrNum[i];
        return copy;
    }

    public boolean isBelong(Kit base){
        if(arrNum.length > base.getArrNumSize())
            return false;
        for(int elThis : arrNum) {
            boolean isFound = false;
            for (int elBase : base.getCopyArr())
                if (elThis == elBase)
                    isFound = true;
            if (isFound == false)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String res = "";
        for (int e : arrNum)
            res += e;
        return res;
    }

    public String info(){
        String res =  beTested + "\t" + sumGR + "\t";
        res += this.toString();
        return res;
    }

    public static Kit sumKit(Kit k1, Kit k2){
        if(k1 == null || k2 == null)
            return null;
        int[] arr = new int[k1.getArrNumSize()+ k2.getArrNumSize()];
        int ind = 0;
        for(int el : k1.getCopyArr()){
            arr[ind] = el;
            ind++;
        }
        for(int el : k2.getCopyArr()){
            arr[ind] = el;
            ind++;
        }
        Kit res = new Kit(arr);
        res.setBeTested(k1.isBeTested() && k2.isBeTested());
        res.setSumGR(k1.getSumGR() + k2.getSumGR());
        return res;
    }



    // SET-GET
    public int[] getArrNum() {return arrNum;}

    public void setSumGR(int sumGR) { if(!beTested) this.sumGR = sumGR;}

    public int getSumGR() {return sumGR;}

    public void setBeTested(boolean beTested) {this.beTested = beTested;}

    public boolean isBeTested() {return beTested;}

    public int getArrNumSize(){
        return arrNum.length;
    }

}
