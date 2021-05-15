package serv2;

import serv1.Record;

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
}
