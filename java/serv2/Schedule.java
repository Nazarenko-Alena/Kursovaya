package serv2;

import serv1.Record;

import java.util.Calendar;
import java.util.List;

public class Schedule {
    private List<Record> arrayDry;
    private List<Record>  arrayLiquid;
    private List<Record>  arrayCont;

    public List<Record> getArrayDry() {
        return arrayDry;
    }

    public void setArrayDry(List<Record> arrayDry) {
        this.arrayDry = arrayDry;
    }

    public List<Record> getArrayLiquid() {
        return arrayLiquid;
    }

    public void setArrayLiquid(List<Record> arrayLiquid) {
        this.arrayLiquid = arrayLiquid;
    }

    public List<Record> getArrayCont() {
        return arrayCont;
    }

    public void setArrayCont(List<Record> arrayCont) {
        this.arrayCont = arrayCont;
    }

    public String scheduleToString(List<Record> array) {

        StringBuilder schedule = new StringBuilder();

        for (Record record : array) {

            schedule.append("   -------------   ");
            String date = record.getDate().get(Calendar.DAY_OF_MONTH) + ":" +
                    record.getDate().get(Calendar.HOUR) + ":" +
                    record.getDate().get(Calendar.MINUTE);

            String waitingTime = ((record.getWaitingTime() / 60) / 24) + ":" +
                    (record.getWaitingTime() / 60 % 24) + ":"
                    + (record.getWaitingTime() % 60);

            String unloadStart = record.getUnloadingStart().get(Calendar.DAY_OF_MONTH) + ":"
                    + record.getUnloadingStart().get(Calendar.HOUR) + ":"
                    + record.getUnloadingStart().get(Calendar.MINUTE);

            schedule.append("Name: ").append(record.getName()).append("   Arrival date: ").append(date).append("   Waiting time: ").append(waitingTime).append("   Unloading start: ").append(unloadStart).append("   Parking time: ").append(record.getParkingTime());
        }

        return schedule.toString();
    }

    public String scheduleForDryToString() {
        return scheduleToString(getArrayDry());
    }

    public String scheduleForLiquidToString() {
        return scheduleToString(getArrayLiquid());
    }

    public String scheduleForContToString() {
        return scheduleToString(getArrayCont());
    }
}
