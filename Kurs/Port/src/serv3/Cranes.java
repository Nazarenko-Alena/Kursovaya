package serv3;

import serv1.Record;
import serv2.SumStat;

import java.util.ArrayList;
import java.util.List;

public class Cranes implements Runnable{
    private int count;
    private int speed;
    private final String type;
    private int countFree;
    private static int penalty;

    private static int penaltyDry;
    private static int penaltyLiquid;
    private static int penaltyCont;

    private int queueTypeSize;
    private static int queueLiquidSize;
    private static int queueContSize;
    private static int queueDrySize;

    private int countShip;

    private static List<Record> arrayDry = new ArrayList<>();
    private static List<Record> arrayLiquid = new ArrayList<>();
    private static List<Record> arrayCont = new ArrayList<>();

    private static List<Record> arrayType = new ArrayList<>();

    private static SumStat stat = new SumStat();

    private final Object mut = new Object();

    public Cranes(String t){
        type = t;

        switch (type){
            case "liquid" : Cranes.arrayType = arrayLiquid;
            case "dry" :  Cranes.arrayType = arrayDry;
            case "cont" :  Cranes.arrayType = arrayCont;
        }
    }

    public static int getPenaltyCont() {
        return penaltyCont;
    }

    public static int getPenaltyLiquid() {
        return penaltyLiquid;
    }

    public static int getPenaltyDry() {
        return penaltyDry;
    }

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

    public void decCountFree(){
        if(countFree > 1) {
            countFree -= 1;
        }
    }

    public int getCountFree(){
        return countFree;
    }

    public static List<Record> getArrayCont(){
        return arrayCont;
    }

    public static List<Record> getArrayLiquid(){
        return arrayLiquid;
    }

    public static List<Record> getArrayDry(){
        return arrayDry;
    }

    public void setCountFree(int countF) {
        countFree = countF;
    }

    public static void setArrayDry(List<Record> arr) {
        Cranes.arrayDry = arr;
    }

    public static void setArrayLiquid(List<Record> arr) {
        Cranes.arrayLiquid = arr;
    }

    public static void setArrayCont(List<Record> arr) {
        Cranes.arrayCont = arr;
    }

    public SumStat getStat(){
        return stat;
    }

    public static void setStat(SumStat stat){
        Cranes.stat = stat;
    }

    public void changePen(int val){
        penalty += val;
    }

    public void incCountShip(){ countShip++;}

    @Override
    public void run() {
        synchronized (mut){
            System.out.println("start " + Thread.currentThread().getName() + Thread.currentThread().getState());
            int flag = 0;
            List<Record> array = new ArrayList<>(arrayType);
            penalty = 0;
            int queueSize = 0;
            int size = array.size();

        for (int i = 0; i < size - 1 ; i++) {
            if (getCountFree() > 1) {
                decCountFree();
                decCountFree();
                flag = 1;
                arrayType.get(i).setParkingTime(array.get(i).getParkingTime() / 2);
            }

            if (countShip != (size - 1)) {
                if (array.get(i).getDateDeparture().compareTo(array.get(i + 1).getDate()) > 0) {
                    queueSize += 1;

                    changePen((array.get(i).getParkingTime() / 60) * 100);

                    arrayType.get(i + 1).changeWaitingTime(array.get(i).getParkingTime());
                    stat.changeWaitTime(array.get(i).getParkingTime());
                } else if (array.get(i).getDateDeparture().compareTo(array.get(i + 1).getDate()) < 0) {
                    if (queueSize > queueTypeSize) {
                        queueTypeSize = queueSize;
                    }
                    queueSize = 0;
                }
            }

            if (flag == 1) {
                countFree += 2;
            } else {
                countFree += 1;
            }

            incCountShip(); }

            switch (type){
                case "liquid" : {
                    Cranes.arrayLiquid = arrayType;
                    penaltyLiquid = penalty;
                    queueLiquidSize = queueTypeSize;
                }
                case "dry" :  {
                    Cranes.arrayDry = arrayType;
                    penaltyDry = penalty;
                    queueDrySize = queueTypeSize;
                }
                case "cont" :  {
                    Cranes.arrayCont = arrayType;
                    penaltyCont = penalty;
                    queueContSize = queueTypeSize;
                }
            }
        }
        try {
            Thread.sleep(6000);
            System.out.println("end " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getQueueLiquidSize() {
        return queueLiquidSize;
    }

    public static int getQueueDrySize() {
        return queueDrySize;
    }

    public static int getQueueContSize() {
        return queueContSize;
    }
}