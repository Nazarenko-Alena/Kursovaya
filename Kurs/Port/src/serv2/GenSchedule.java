package serv2;

import serv1.Generator;
import serv1.Record;
import serv3.Cranes;

import java.util.*;

public class GenSchedule {
    private int sumDelay = 0;

    private final Schedule schedule = new Schedule();

    private final SumStat stat = new SumStat();

    public SumStat GenerateSchedule() {
        List<Record> arrayDry = new ArrayList<>();
        List<Record> arrayLiquid = new ArrayList<>();
        List<Record> arrayCont = new ArrayList<>();

       int numberOfShips = (int) (Math.random() * 101) + 50;

       Cranes cranesCont = new Cranes("cont");
       cranesCont.setSpeed(3000);

       Cranes cranesLiquid = new Cranes("liquid");
       cranesLiquid.setSpeed(2500);

       Cranes cranesDry = new Cranes("dry");
       cranesDry.setSpeed(4500);

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

            if(stat.getMaxDelay() < delay) stat.setMaxDelay(delay);

            sumDelay += delay;

            stat.changePenalty((delay /60) * 100);

            switch(record.getTypeOfCargo()){
                case "контейнер": {
                    gen.GenerateParkingTime(record, record.getCargoWeight(), cranesCont.getSpeed());
                    record.changeParkingTime(delay);
                    arrayCont.add(record);
                }

                case "жидкий":{
                    gen.GenerateParkingTime(record, record.getCargoWeight(), cranesLiquid.getSpeed());
                    record.changeParkingTime(delay);
                    arrayLiquid.add(record);
                }
                case "сыпучий":{
                    gen.GenerateParkingTime(record, record.getCargoWeight(), cranesDry.getSpeed());
                    record.changeParkingTime(delay);
                    arrayDry.add(record);
                }
            }
        }

        stat.setMeanDelay(sumDelay/ numberOfShips);

        schedule.setArrayDry(arrayDry);
        schedule.setArrayLiquid(arrayLiquid);
        schedule.setArrayCont(arrayCont);

        return stat;
    }

    public Schedule getSchedule(){
        return schedule;
    }
}
