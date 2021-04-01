package serv3;

public class CraneCont {
    private int count;
    private int speed;
    private int countFree;

    public int getCount() {
        return count;
    }

    public void incCount(){
        count += 1;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void incCountFree(){
        this.countFree += 1;
    }

    public void decCountFree(){
        this.countFree -= 1;
    }

    public int getCountFree(){
        return countFree;
    }

    public void setCountFree(int countFree) {
        this.countFree = countFree;
    }
}
