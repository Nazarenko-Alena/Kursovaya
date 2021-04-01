package serv2;

import serv1.Generator;
import serv1.Record;
import serv3.CraneCont;
import serv3.CraneDry;
import serv3.CraneLiquid;

import java.util.*;

public class GenSchedule {
    private int sumDelay = 0;

    public Schedule schedule = new Schedule();

    private List<Record> arrayDry;
    private List<Record>  arrayLiquid;
    private List<Record>  arrayCont;

    public SumStat stat = new SumStat();

    public SumStat GenerateSchedule() {
       arrayDry = new ArrayList<>();
       arrayLiquid = new ArrayList<>();
       arrayCont = new ArrayList<>();

       int numberOfShips = (int) (Math.random() * 101) + 1;

       CraneCont craneCont = new CraneCont();
       craneCont.setSpeed(3000);

       CraneLiquid craneLiquid = new CraneLiquid();
       craneLiquid.setSpeed(2500);

       CraneDry craneDry = new CraneDry();
       craneDry.setSpeed(4500);

        ////Generate schedule////
        for(int i = 0; i < numberOfShips; i++)
        {
            Generator gen = new Generator();
            Record record = new Record();

            String addType = "auto";

            if(addType.equals("auto")){
                gen.Generate(record);
            }
            else if(addType.equals("manual")){
                gen.ManualEntry(record);
            }

            int lateness = (int)(Math.random()*15) - 7; //Опоздание
            record.changeDate(lateness);

            int delay = (int) (Math.random() * 1441); //Задержка разгрузки

            if(stat.maxDelay < delay) stat.maxDelay = delay;

            sumDelay += delay;

            stat.penalty += (delay /60) * 100;

            if ("контейнер".equals(record.getTypeOfCargo())) {
                gen.GenerateParkingTime(record, record.getCargoWeight(),craneCont.getSpeed());
                record.changeParkingTime(delay);
                arrayCont.add(record);
            } else if ("жидкий".equals(record.getTypeOfCargo())) {
                gen.GenerateParkingTime(record, record.getCargoWeight(),craneLiquid.getSpeed());
                record.changeParkingTime(delay);
                arrayLiquid.add(record);
            }
            else if ("сыпучий".equals(record.getTypeOfCargo())) {
                gen.GenerateParkingTime(record, record.getCargoWeight(),craneDry.getSpeed());
                record.changeParkingTime(delay);
                arrayDry.add(record);
            }
        }

        stat.meanDelay = sumDelay/ numberOfShips;

        schedule.arrayDry = arrayDry;
        schedule.arrayLiquid = arrayLiquid;
        schedule.arrayCont = arrayCont;

        return stat;
    }

    public Schedule getSchedule(){
        return schedule;
    }
}
