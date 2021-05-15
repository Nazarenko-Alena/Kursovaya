package serv2;

import serv1.Record;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class StatToJson {

    public static void ScheduleToJson(Schedule schedule) throws IOException {
        FileWriter fileWriter = new FileWriter("Port/resources/Schedule.json");
        JsonWriter jsonWriter = new JsonWriter(fileWriter);

        jsonWriter.beginObject();

        List<Record> array;

        for (int j = 0; j < 3; j++) {
            if (j == 0) {
                array = schedule.getArrayDry();
                jsonWriter.name("ArrayDry");
            } else if (j == 1) {
                array = schedule.getArrayLiquid();
                jsonWriter.name("ArrayLiquid");
            } else {
                array = schedule.getArrayCont();
                jsonWriter.name("ArrayCont");
            }

            jsonWriter.beginArray();

            for (Record record : array) {
                jsonWriter.beginObject();

                String date = record.getDate().get(Calendar.DAY_OF_MONTH) + ":" +
                        record.getDate().get(Calendar.HOUR) + ":" +
                        record.getDate().get(Calendar.MINUTE);

                String waitingTime = ((record.getWaitingTime() / 60) / 24) + ":" +
                        (record.getWaitingTime() / 60 % 24) + ":"
                        + (record.getWaitingTime() % 60);

                String unloadStart = record.getUnloadingStart().get(Calendar.DAY_OF_MONTH) + ":"
                        + record.getUnloadingStart().get(Calendar.HOUR) + ":"
                        + record.getUnloadingStart().get(Calendar.MINUTE);

                jsonWriter.name("Name").value(record.getName());

                jsonWriter.name("Arrival date").value(date);
                jsonWriter.name("Waiting time").value(waitingTime);
                jsonWriter.name("Unloading start").value(unloadStart);
                jsonWriter.name("Parking time").value(record.getParkingTime());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
        }

        jsonWriter.endObject();
        jsonWriter.close();
    }

    public static void SumStatToJson(SumStat stat) throws IOException {
        FileWriter fileWriter = new FileWriter("Port/resources/SumStat.json");
        JsonWriter jsonWriter = new JsonWriter(fileWriter);

        jsonWriter.beginObject();
        jsonWriter.name("Number of ships").value(stat.getNumberOfShips());
        jsonWriter.name("Average queue length").value(stat.getQueueSize());
        jsonWriter.name("Average waiting time").value(stat.getMeanWaitTime());
        jsonWriter.name("Max unloading delay").value(stat.getMaxDelay());
        jsonWriter.name("Mean unloading delay").value(stat.getMeanDelay());
        jsonWriter.name("Penalty").value(stat.getPenalty());
        jsonWriter.name("Number of dry cargo cranes").value(stat.getCountDryCrane());
        jsonWriter.name("Number of cont cargo cranes").value(stat.getCountContCrane());
        jsonWriter.name("Number of liquid cargo cranes").value(stat.getCountLiquidCrane());
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
