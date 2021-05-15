package serv2;

public class SumStat {
    private int numberOfShips;
    private int queueSize;
    private int meanWaitTime;
    private int meanDelay;
    private int maxDelay;
    private int penalty;
    private int countDryCrane;
    private int countLiquidCrane;
    private int countContCrane;

    public int getNumberOfShips() {
        return numberOfShips;
    }

    public void setNumberOfShips(int numberOfShips) {
        this.numberOfShips = numberOfShips;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getMeanWaitTime() {
        return meanWaitTime;
    }

    public void setMeanWaitTime(int meanWaitTime) {
        this.meanWaitTime = meanWaitTime;
    }

    public int getMeanDelay() {
        return meanDelay;
    }

    public void setMeanDelay(int meanDelay) {
        this.meanDelay = meanDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getCountDryCrane() {
        return countDryCrane;
    }

    public void setCountDryCrane(int countDryCrane) {
        this.countDryCrane = countDryCrane;
    }

    public int getCountLiquidCrane() {
        return countLiquidCrane;
    }

    public void setCountLiquidCrane(int countLiquidCrane) {
        this.countLiquidCrane = countLiquidCrane;
    }

    public int getCountContCrane() {
        return countContCrane;
    }

    public void setCountContCrane(int countContCrane) {
        this.countContCrane = countContCrane;
    }

    public void changeWaitTime(int val){
        meanWaitTime += val;
    }

    public void changePenalty(int val){
        penalty += val;
    }
}
