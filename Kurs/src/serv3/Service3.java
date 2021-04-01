package serv3;

import com.google.gson.stream.JsonReader;
import serv1.Record;
import serv2.Schedule;
import serv2.SumStat;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Service3 {
    private static int queueSize = 0;

    public static SumStat stat = new SumStat();

    private static int queueDrySize = 0;
    private static int queueLiquidSize = 0;
    private static int queueContSize = 0;

    private static CraneLiquid craneLiquid;
    private static CraneCont craneCont;
    private static CraneDry craneDry;

    private static int penaltyWaitingDry;
    private static int penaltyWaitingLiquid;
    private static int penaltyWaitingCont;

    private static ArrayList<Record> arrayDry = new ArrayList<>();
    private static ArrayList<Record> arrayLiquid  = new ArrayList<>();
    private static ArrayList<Record> arrayCont  = new ArrayList<>();

    private static  int flag = 0;

    public static Schedule sched = new Schedule();

    private static final int costCrane = 30000;

    public Record readRec(JsonReader reader) throws IOException {

        Record rec = new Record();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "Name":
                    rec.setName(reader.nextString());
                    break;
                case "Arrival date":
                    StringBuilder day = new StringBuilder();
                    StringBuilder hour = new StringBuilder();
                    StringBuilder minute = new StringBuilder();

                    char[] strToArray = reader.nextString().toCharArray();

                    int i = 0;

                    while(strToArray[i] != ':') {
                        day.append(strToArray[i]);
                        i++;
                    }

                    i++;

                    while(strToArray[i]!=':') {
                        hour.append(strToArray[i]);
                        i++;
                    }

                    i++;

                    while(i != (strToArray.length)) {
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
                    break;
                case "Parking time":
                    rec.setParkingTime(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return rec;
    }

    public void Unload(SumStat statistic) throws IOException {

        FileReader fileReader = new FileReader("C:\\Users\\alena\\IdeaProjects\\Kurs\\Schedule.json");
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

        stat = statistic;

        craneCont = new CraneCont();
        craneCont.setSpeed(3000);

        craneLiquid = new CraneLiquid();
        craneLiquid.setSpeed(2500);

        craneDry = new CraneDry();
        craneDry.setSpeed(4500);

        arrayDry.sort(Comparator.comparing(Record::getDate));
        arrayLiquid.sort(Comparator.comparing(Record::getDate));
        arrayCont.sort(Comparator.comparing(Record::getDate));

        craneDry.setCountFree(0);
        craneLiquid.setCountFree(0);
        craneCont.setCountFree(0);

        penaltyWaitingDry = 30000;
        penaltyWaitingLiquid = 30000;
        penaltyWaitingCont = 30000;

        List<Thread> threads = new LinkedList<>();

        if(arrayDry.size() != 0) {
            ////////Unloading for Dry///////
            Thread threadDry = new Thread(() -> {
                while (penaltyWaitingDry >= costCrane) {
                    List<Record> array = new ArrayList<>(arrayDry);
                    penaltyWaitingDry = 0;
                    craneDry.incCount();
                    flag = 0;
                    queueSize = 0;
                    int size = array.size();
                    for (int i = 0; i < size - 1; i++) {

                        if (craneDry.getCountFree() > 1) {
                            craneDry.decCountFree();
                            craneDry.decCountFree();
                            flag = 1;
                            arrayDry.get(i).setParkingTime(array.get(0).getParkingTime() / 2);
                        }

                        if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) > 0) {
                            queueSize += 1;

                            penaltyWaitingDry += (array.get(0).getParkingTime() / 60) * 100;

                            arrayDry.get(i + 1).changeWaitingTime(array.get(0).getParkingTime());
                            stat.meanWaitTime += array.get(0).getParkingTime();

                        } else if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) < 0) {
                            if (queueSize > queueDrySize) {
                                queueDrySize = queueSize;
                            }
                            queueSize = 0;
                        }
                        array.remove(0);
                        if (flag == 1) {
                            craneDry.incCountFree();
                            craneDry.incCountFree();
                            flag = 0;
                        } else {
                            craneDry.incCountFree();
                        }
                    }

                    if (craneDry.getCountFree() > 1) {
                        craneDry.decCountFree();
                        craneDry.decCountFree();
                        flag = 1;
                        arrayDry.get(size - 1).changeParkingTime(array.get(0).getParkingTime() / 2);
                    }

                    array.remove(0);

                    if (flag == 1) {
                        craneDry.incCountFree();
                        craneDry.incCountFree();
                        flag = 0;
                    } else {
                        craneDry.incCountFree();
                    }
                }
            });
            threads.add(threadDry);
            threadDry.start();
        }

        if(arrayLiquid.size() != 0) {
            ///////////////Unloading for liquid/////////
            Thread threadLiquid = new Thread(() -> {
                while (penaltyWaitingLiquid >= costCrane) {
                    List<Record> array = new ArrayList<>(arrayLiquid);
                    penaltyWaitingLiquid = 0;
                    craneLiquid.incCount();
                    flag = 0;
                    queueSize = 0;
                    int size = array.size();
                    for (int i = 0; i < size - 1; i++) {
                        if (craneLiquid.getCountFree() > 1) {
                            craneLiquid.decCountFree();
                            craneLiquid.decCountFree();
                            flag = 1;
                            arrayLiquid.get(i).setParkingTime(array.get(0).getParkingTime() / 2);
                        }

                        if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) > 0) {
                            queueSize += 1;

                            penaltyWaitingLiquid += (array.get(0).getParkingTime() / 60) * 100;

                            arrayLiquid.get(i + 1).changeWaitingTime(array.get(0).getParkingTime());
                            stat.meanWaitTime += array.get(0).getParkingTime();
                        } else if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) < 0) {
                            if (queueSize > queueLiquidSize) {
                                queueLiquidSize += queueSize;
                            }
                            queueSize = 0;
                        }
                        array.remove(0);
                        if (flag == 1) {
                            craneLiquid.incCountFree();
                            craneLiquid.incCountFree();
                            flag = 0;
                        } else {
                            craneLiquid.incCountFree();
                        }
                    }
                    if (craneLiquid.getCountFree() > 1) {
                        craneLiquid.decCountFree();
                        craneLiquid.decCountFree();
                        flag = 1;
                        arrayLiquid.get(size - 1).changeParkingTime(array.get(0).getParkingTime() / 2);
                    }
                    array.remove(0);

                    if (flag == 1) {
                        craneLiquid.incCountFree();
                        craneLiquid.incCountFree();
                        flag = 0;
                    } else {
                        craneLiquid.incCountFree();
                    }
                }
            });
            threads.add(threadLiquid);
            threadLiquid.start();
        }

        if(arrayCont.size() != 0) {
            ////////Unloading for Cont/////////////
            Thread threadCont = new Thread(() -> {
                while (penaltyWaitingCont >= costCrane) {
                    List<Record> array = new ArrayList<>(arrayCont);
                    penaltyWaitingCont = 0;
                    craneCont.incCount();
                    flag = 0;
                    queueSize = 0;
                    int size = array.size();
                    for (int i = 0; i < size - 1; i++) {

                        if (craneCont.getCountFree() > 1) {
                            craneCont.decCountFree();
                            craneCont.decCountFree();
                            flag = 1;
                            arrayCont.get(i).setParkingTime(array.get(0).getParkingTime() / 2);
                        }

                        if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) > 0) {
                            queueSize += 1;

                            penaltyWaitingCont += (array.get(0).getParkingTime() / 60) * 100;

                            arrayCont.get(i + 1).changeWaitingTime(array.get(0).getParkingTime());
                            stat.meanWaitTime += array.get(0).getParkingTime();
                        } else if (array.get(0).getDateDeparture().compareTo(array.get(1).getDate()) < 0) {
                            if (queueSize > queueContSize) {
                                queueContSize += queueSize;
                            }
                            queueSize = 0;
                        }
                        array.remove(0);
                        if (flag == 1) {
                            craneCont.incCountFree();
                            craneCont.incCountFree();
                            flag = 0;
                        } else {
                            craneCont.incCountFree();
                        }
                    }
                    if (craneCont.getCountFree() > 1) {
                        craneCont.decCountFree();
                        craneCont.decCountFree();
                        flag = 1;
                        arrayCont.get(size - 1).changeParkingTime(array.get(0).getParkingTime() / 2);
                    }
                    array.remove(0);

                    if (flag == 1) {
                        craneCont.incCountFree();
                        craneCont.incCountFree();
                        flag = 0;
                    } else {
                        craneCont.incCountFree();
                    }
                }
            });
            threads.add(threadCont);
            threadCont.start();
        }

        for (Thread t:threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        stat.queueSize = (queueDrySize + queueLiquidSize + queueContSize)/3;
        stat.meanWaitTime = stat.meanWaitTime/(arrayCont.size()+arrayDry.size()+arrayLiquid.size());
        stat.numberOfShips = arrayCont.size()+arrayDry.size()+arrayLiquid.size();
        stat.penalty = stat.penalty + penaltyWaitingDry + penaltyWaitingLiquid + penaltyWaitingCont;

        stat.countContCrane = craneCont.getCount();
        stat.countDryCrane = craneDry.getCount();
        stat.countLiquidCrane = craneLiquid.getCount();

        sched.arrayDry = arrayDry;
        sched.arrayCont = arrayCont;
        sched.arrayLiquid = arrayLiquid;

        System.out.println("penaltyWaitingDry = " + penaltyWaitingDry);
        System.out.println("penaltyWaitingLiquid = " + penaltyWaitingLiquid);
        System.out.println("penaltyWaitingCont = " + penaltyWaitingCont);

        System.out.println("Count craneLiquid = " + stat.countLiquidCrane);
        System.out.println("Count craneDry = " + stat.countDryCrane);
        System.out.println("Count craneCont = " + stat.countContCrane);
    }

    public SumStat getStat(){
        return stat;
    }

    public Schedule getSched(){
        return sched;
    }
}


