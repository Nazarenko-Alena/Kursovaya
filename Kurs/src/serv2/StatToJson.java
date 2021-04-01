package serv2;

import serv1.Record;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class StatToJson {

    public static void ScheduleToJson(Schedule schedule) throws IOException {
        FileWriter fileWriter = new FileWriter("C:\\Users\\alena\\IdeaProjects\\Kurs\\Schedule.json");
        JsonWriter jsonWriter = new JsonWriter(fileWriter);


        jsonWriter.beginObject();

        List<Record> array;

        for (int j = 0; j < 3; j++) {
            if (j == 0) {
                array = schedule.arrayDry;
                jsonWriter.name("ArrayDry");
            } else if (j == 1) {
                array = schedule.arrayLiquid;
                jsonWriter.name("ArrayLiquid");
            } else {
                array = schedule.arrayCont;
                jsonWriter.name("ArrayCont");
            }

            jsonWriter.beginArray();

            for (int i = 0; i < array.size(); i++) {
                jsonWriter.beginObject();

                String date = array.get(i).getDate().get(Calendar.DAY_OF_MONTH) + ":" +
                        array.get(i).getDate().get(Calendar.HOUR) + ":" +
                        array.get(i).getDate().get(Calendar.MINUTE);

                String waitingTime = ((array.get(i).getWaitingTime() / 60) / 24) + ":" +
                        (array.get(i).getWaitingTime() / 60 % 24) + ":"
                        + (array.get(i).getWaitingTime() % 60);

                String unloadStart = array.get(i).getUnloadingStart().get(Calendar.DAY_OF_MONTH) + ":"
                        + array.get(i).getUnloadingStart().get(Calendar.HOUR) + ":"
                        + array.get(i).getUnloadingStart().get(Calendar.MINUTE);

                jsonWriter.name("Name").value(array.get(i).getName());
                jsonWriter.name("Arrival date").value(date);
                jsonWriter.name("Waiting time").value(waitingTime);
                jsonWriter.name("Unloading start").value(unloadStart);
                jsonWriter.name("Parking time").value(array.get(i).getParkingTime());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
        }

        jsonWriter.endObject();
        jsonWriter.close();
    }

    public static void SumStatToJson(SumStat stat) throws IOException {
        FileWriter fileWriter = new FileWriter("C:\\Users\\alena\\IdeaProjects\\Kurs\\SumStat.json");
        JsonWriter jsonWriter = new JsonWriter(fileWriter);

        jsonWriter.beginObject();
        jsonWriter.name("Number of ships").value(stat.numberOfShips);
        jsonWriter.name("Average queue length").value(stat.queueSize);
        jsonWriter.name("Average waiting time").value(stat.meanWaitTime);
        jsonWriter.name("Max unloading delay").value(stat.maxDelay);
        jsonWriter.name("Mean unloading delay").value(stat.meanDelay);
        jsonWriter.name("Penalty").value(stat.penalty);
        jsonWriter.name("Number of dry cargo cranes").value(stat.countDryCrane);
        jsonWriter.name("Number of cont cargo cranes").value(stat.countContCrane);
        jsonWriter.name("Number of liquid cargo cranes").value(stat.countLiquidCrane);
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
