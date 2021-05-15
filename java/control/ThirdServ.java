package control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import serv2.*;
import serv3.Service3;

import java.io.IOException;

public class ThirdServ{

    private static final FullStat fullStat = new FullStat();

    public static void getResult() throws IOException, InterruptedException {

        String path = SecondC.getPath();

        Service3 service3 = new Service3();

        service3.Unload(GenSchedule.getStat(), path);

        SumStat stat = service3.getStat();
        Schedule sched = service3.getSched();

        fullStat.setSumStat(stat);
        fullStat.setSchedule(sched);

        StatToJson.SumStatToJson(stat);
        StatToJson.ScheduleToJson(sched);
    }

    public static FullStat getFullStat() {
        return fullStat;
    }
}
