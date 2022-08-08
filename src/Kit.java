public class Kit {
    private int[] arrNum;
    private int sumGR;
    private boolean beTested;

    public Kit(){}

    public Kit(int[] arrNum) {
        this.arrNum = arrNum;
    }


//    public void setArrNum(int[] arrNum) {
//        this.arrNum = arrNum;
//    }

    public Kit copy(){
        Kit copy = new Kit();
        copy.arrNum = new int[arrNum.length];
        for(int i=0;i<arrNum.length;i++){
            copy.arrNum[i] = arrNum[i];
        }
        copy.sumGR = sumGR;
        copy.beTested = beTested;
        return copy;
    }

    public int[] getCopyArr(){
        int[] copy = new int[arrNum.length];
        for(int i=0;i<arrNum.length;i++){
            copy[i] = arrNum[i];
        }
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
        for (int e : arrNum) {
            res += e;
        }
        return res;
    }

    public String info(){
        String res =  beTested + "\t" + sumGR + "\t";
        res += this.toString();
        return res;
    }

    public int[] getArrNum() {
        return arrNum;
    }

    public void setSumGR(int sumGR) {
        this.sumGR = sumGR;
    }

    public int getSumGR() {
        return sumGR;
    }

    public void setBeTested(boolean beTested) {
        this.beTested = beTested;
    }

    public boolean isBeTested() {
        return beTested;
    }
    
    public int getArrNumSize(){
        return arrNum.length;
    }
}
