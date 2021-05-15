package serv3;

import com.google.gson.stream.JsonReader;
import serv1.Record;
import serv2.FullStat;
import serv2.Schedule;
import serv2.SumStat;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Service3 {
    private static SumStat stat = new SumStat();

    private static Cranes cranesLiquid;
    private static Cranes cranesCont;
    private static Cranes cranesDry;
    private static final FullStat fullStat = new FullStat();

    private static int penaltyWaitingDry;
    private static int penaltyWaitingLiquid;
    private static int penaltyWaitingCont;

    private static final int costCrane = 30000;

    private int queueDrySize;
    private int queueLiquidSize;
    private int queueContSize;

    private static List<Record> arrayDry = new ArrayList<>();
    private static List<Record> arrayLiquid  = new ArrayList<>();
    private static List<Record> arrayCont  = new ArrayList<>();

    private static final Schedule sched = new Schedule();

    public Record readRec(JsonReader reader) throws IOException {

        Record rec = new Record();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "Name" -> rec.setName(reader.nextString());
                case "Arrival date" -> {
                    StringBuilder day = new StringBuilder();
                    StringBuilder hour = new StringBuilder();
                    StringBuilder minute = new StringBuilder();
                    char[] strToArray = reader.nextString().toCharArray();
                    int i = 0;
                    while (strToArray[i] != ':') {
                        day.append(strToArray[i]);
                        i++;
                    }
                    i++;
                    while (strToArray[i] != ':') {
                        hour.append(strToArray[i]);
                        i++;
                    }
                    i++;
                    while (i != (strToArray.length)) {
                        minute.append(strToArray[i]);
                        i++;
                    }
                    Calendar date = new GregorianCalendar();
                    date.set(Calendar.YEAR, 2020);
                    date.set(Calendar.MONTH, 3);
                    date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.toString()));
                    date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.toString()));
                    date.set(Calendar.MINUTE, Integer.parseInt(minute.toString()));
                    rec.setDate(date);
                }
                case "Parking time" -> rec.setParkingTime(reader.nextInt());
                default -> reader.skipValue();
            }
        }
        reader.endObject();

        return rec;
    }

    public void readFromJson(String path) throws IOException {

        FileReader fileReader = new FileReader(path);
        JsonReader jsonReader = new JsonReader(fileReader);

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            switch (name) {
                case "ArrayDry" -> {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayDry.add(readRec(jsonReader));
                    }
                    jsonReader.endArray();
                }
                case "ArrayLiquid" -> {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayLiquid.add(readRec(jsonReader));
                    }
                    jsonReader.endArray();
                }
                case "ArrayCont" -> {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayCont.add(readRec(jsonReader));
                    }
                    jsonReader.endArray();
                }
                default -> jsonReader.skipValue();
            }
        }
    }

    public void Unload(SumStat statistic, String path) throws IOException, InterruptedException {

        readFromJson(path);
        setStartVal(statistic);

        List<Thread> threads = new ArrayList<>();

        while((penaltyWaitingDry >= costCrane) || (penaltyWaitingLiquid>=costCrane)
                || (penaltyWaitingCont >= costCrane)) {

            if (penaltyWaitingDry >= costCrane) cranesDry.incCount();
            if (penaltyWaitingCont >= costCrane) cranesCont.incCount();
            if (penaltyWaitingLiquid >= costCrane) cranesLiquid.incCount();

            Cranes.setArrayDry(arrayDry);
            Cranes.setArrayLiquid(arrayLiquid);
            Cranes.setArrayCont(arrayCont);

            Cranes.setStat(stat);
            Cranes.setStat(stat);
            Cranes.setStat(stat);

            if (penaltyWaitingDry >= costCrane) {
                for (int i = 0; i < cranesDry.getCount(); i++) {
                    Thread t = new Thread(new Cranes("dry"));
                    threads.add(t);
                    t.start();
                }

                Thread.sleep(2000);
                penaltyWaitingDry = Cranes.getPenaltyDry();
                queueDrySize = Cranes.getQueueDrySize();
            }

                if (penaltyWaitingLiquid >= costCrane) {
                    Thread.sleep(3000);
                    for (int i = 0; i < cranesLiquid.getCount(); i++) {
                        Thread t = new Thread(new Cranes("liquid"));
                        threads.add(t);
                        t.start();
                    }
                    Thread.sleep(2000);
                    penaltyWaitingLiquid = Cranes.getPenaltyLiquid();
                    queueLiquidSize = Cranes.getQueueLiquidSize();
                }

                    if (penaltyWaitingCont >= costCrane) {
                        Thread.sleep(3000);
                        for (int i = 0; i < cranesCont.getCount(); i++) {
                            Thread t = new Thread(new Cranes("cont"));
                            threads.add(t);
                            t.start();
                        }
                        Thread.sleep(2000);
                        penaltyWaitingCont = Cranes.getPenaltyCont();
                        queueContSize = Cranes.getQueueContSize();
                    }
                }

        Thread.sleep(10000);
        for (Thread t: threads) {
            t.join();
        }

        arrayDry = Cranes.getArrayDry();
        arrayLiquid = Cranes.getArrayLiquid();
        arrayCont = Cranes.getArrayCont();

       setEndVal();
       printInfo();
    }

    public SumStat getStat(){
        return stat;
    }

    public Schedule getSched(){
        return sched;
    }

    public void setStartVal(SumStat statistic){
        stat = statistic;

        cranesCont = new Cranes("cont");
        cranesCont.setSpeed(3000);

        cranesLiquid = new Cranes("liquid");
        cranesLiquid.setSpeed(2500);

        cranesDry = new Cranes("dry");
        cranesDry.setSpeed(4500);

        arrayDry.sort(Comparator.comparing(Record::getDate));
        arrayLiquid.sort(Comparator.comparing(Record::getDate));
        arrayCont.sort(Comparator.comparing(Record::getDate));

        cranesCont.setCountFree(0);
        cranesDry.setCountFree(0);
        cranesLiquid.setCountFree(0);

        penaltyWaitingDry = 30000;
        penaltyWaitingLiquid = 30000;
        penaltyWaitingCont = 30000;
    }

    public void setEndVal(){
        stat = cranesCont.getStat();

        stat.setQueueSize((queueDrySize + queueLiquidSize + queueContSize)/3);
        if((arrayCont.size()+arrayDry.size()+arrayLiquid.size()) > 0) {
            stat.setMeanWaitTime(stat.getMeanWaitTime()/ (arrayCont.size() + arrayDry.size() + arrayLiquid.size()));
        }
        stat.setNumberOfShips(arrayCont.size()+arrayDry.size()+arrayLiquid.size());
        stat.setPenalty(stat.getPenalty() + penaltyWaitingDry + penaltyWaitingLiquid + penaltyWaitingCont);

        stat.setCountContCrane(cranesCont.getCount());
        stat.setCountDryCrane(cranesDry.getCount());
        stat.setCountLiquidCrane(cranesLiquid.getCount());

        sched.setArrayDry(arrayDry);
        sched.setArrayCont(arrayCont);
        sched.setArrayLiquid(arrayLiquid);

        fullStat.setSumStat(stat);
        fullStat.setSchedule(sched);
    }

    public void printInfo(){
        for (Record t:arrayDry) {
            System.out.println(t);
        }

       System.out.println("----------");
        for (Record t:arrayLiquid) {
            System.out.println(t);
        }

        System.out.println("----------");
        for (Record t:arrayCont) {
            System.out.println(t);
        }

        System.out.println("penaltyWaitingDry = " + penaltyWaitingDry);
        System.out.println("penaltyWaitingLiquid = " + penaltyWaitingLiquid);
        System.out.println("penaltyWaitingCont = " + penaltyWaitingCont);

        System.out.println("Count craneDry = " + stat.getCountDryCrane());
        System.out.println("Count craneLiquid = " + stat.getCountLiquidCrane());
        System.out.println("Count craneCont = " + stat.getCountContCrane());

        System.out.println("Penalty = " + stat.getPenalty());
    }
}

