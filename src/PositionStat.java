public class PositionStat {
    private final int NUM;
    private boolean isNeed = true;

    private Integer[] position;

    PositionStat(int Num, int kPositions){
        NUM = Num;
        position = new Integer[kPositions];
        for (Integer i : position)
            i = null;
    }

    public void addStat(int pos, int ram){
        if(!isNeed)
            return;
        if(pos>=0 && pos<position.length)
            if(position[pos]==null)
                position[pos] = ram;
            else if(position[pos]!=0)
                position[pos] += ram;
    }



    public String toString(){
        String res = (isNeed ? "> " : "  ") + NUM + ": ";
        for(Integer i : position)
            res+= (i==null ? "n" : i) + " ";
        return res;
    }

    //SET-GET
    public int getNUM(){return NUM;}

    public boolean isNeed(){return isNeed;}

    public void setNeed(boolean n){isNeed = n;}

    public Integer[] getCopyArrPos(){
        Integer[] copy = new Integer[position.length];
        for(int i=0;i<position.length;i++)
            copy[i] = position[i];
        return copy;
    }

    public Integer getStatusPosition(int ind){
        if(ind<0 || ind>=position.length)
            return -1;
        return position[ind];
    }
}
