package serv1;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Record {
    private Calendar date;
    private Calendar unloadingStart = new GregorianCalendar();
    private String name;
    private String typeOfCargo;
    private Integer cargoWeight;
    private Integer parkingTime;
    private Integer waitingTime = 0;

    public Record() {
        this.name = "";
        this.typeOfCargo = "";
        this.cargoWeight = 0;
        this.parkingTime = 0;
    }

    public Calendar getDate() {
        return date;
    }

    public void changeDate(Integer numberDay) {
        this.date.add(Calendar.DAY_OF_MONTH, numberDay);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfCargo() {
        return typeOfCargo;
    }

    public void setTypeOfCargo(String typeOfCargo) {
        this.typeOfCargo = typeOfCargo;
    }

    public Integer getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(Integer cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Integer getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(Integer parkingTime) {
        this.parkingTime = parkingTime;
    }

    public void changeParkingTime(Integer parkingTime) {
        this.parkingTime += parkingTime;
    }

    public void changeWaitingTime(Integer waitingTime){
        this.waitingTime += waitingTime;
    }

    public int getWaitingTime(){
        return waitingTime;
    }

    public Calendar getDateDeparture(){
        Calendar dateDep = new GregorianCalendar();
        dateDep.setTime(this.date.getTime());
        dateDep.add(Calendar.MINUTE, this.parkingTime);
        return dateDep;
    }

    public void setUnloadingStart(){
        unloadingStart.setTime(this.date.getTime());
        unloadingStart.add(Calendar.MINUTE, this.waitingTime);
    }

    public Calendar getUnloadingStart(){
        setUnloadingStart();
        return unloadingStart;
    }

    @Override
    public String toString() {
           return "Record{" +
                "date = " + date.get(Calendar.DAY_OF_MONTH) + ":" +
                date.get(Calendar.HOUR) + ":" +
                date.get(Calendar.MINUTE) +
                ", name = " + name  +
                ", waitingTime = " + ((waitingTime/60)/24) + ":" +
                (waitingTime/60) + ":" + (waitingTime % 60) +
                ", unloadingStart = " + unloadingStart.get(Calendar.DAY_OF_MONTH) + ":" +
                unloadingStart.get(Calendar.HOUR) + ":" +
                unloadingStart.get(Calendar.MINUTE) +
                ", parkingTime = " + parkingTime + " минут " +
                ", dateDeparture = " + getDateDeparture().get(Calendar.DAY_OF_MONTH) + ":" +
                getDateDeparture().get(Calendar.HOUR) + ":" +  date.get(Calendar.MINUTE) +
                "}";
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
