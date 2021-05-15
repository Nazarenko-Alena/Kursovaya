package serv2;

import serv3.Service3;

import java.io.IOException;

import static serv2.StatToJson.ScheduleToJson;
import static serv2.StatToJson.SumStatToJson;

public class Main {
    public static void main(String[] args) {
       
        GenSchedule genSchedule = new GenSchedule();
        
        SumStat stat = genSchedule.GenerateSchedule();
        Schedule schedule = genSchedule.getSchedule();

        try {
            ScheduleToJson(schedule);

            Service3 serv3 = new Service3();
            schedule = serv3.getSched();

            Thread.sleep(2000);
            serv3.Unload(stat);

            stat = serv3.getStat();

            ScheduleToJson(schedule);
            SumStatToJson(stat);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
