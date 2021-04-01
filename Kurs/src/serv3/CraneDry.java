package serv3;

public class CraneDry {

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
        if(this.countFree < this.count) {
            this.countFree += 1;
        }
    }

    public void decCountFree(){
        if(countFree > 1) {
            this.countFree -= 1;
        }
    }

    public int getCountFree(){
        return countFree;
    }

    public void setCountFree(int countFree) {
        if(this.countFree > this.count) {
            this.count = countFree;
        }
        this.countFree = countFree;
    }
}
