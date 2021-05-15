package serv1;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Generator {

    public void Generate(Record record){
        GenerateDate(record);
        GenerateName(record);
        GenerateTypeOfCargo(record);
        GenerateCargoWeight(record);
    }

    void GenerateDate(Record record){
        Calendar date = new GregorianCalendar();
        date.set(Calendar.YEAR, 2020);
        date.set(Calendar.MONTH, 3);
        date.set(Calendar.DAY_OF_MONTH, 1 + (int)(Math.random()*29));
        date.set(Calendar.HOUR_OF_DAY, (int)(Math.random()*25));
        date.set(Calendar.MINUTE, (int)(Math.random()*61));
        date.set(Calendar.SECOND, (int)(Math.random()*61));

        record.setDate(date);
    }

    void GenerateName(Record record){
        record.setName("Корабль № " + (int)(Math.random()*101));
    }

    void GenerateTypeOfCargo(Record record) {
        String[] typeOfCargo = {"сыпучий", "жидкий", "контейнер"};
        int i = (int)(Math.random()*3);

        record.setTypeOfCargo(typeOfCargo[i]);
    }

    void GenerateCargoWeight(Record record){
        record.setCargoWeight(1 + (int)(Math.random()*300000));
    }

    public void GenerateParkingTime(Record record, Integer cargoWeight, Integer craneSpeed){
        Integer time = (cargoWeight/craneSpeed) * 60;
        record.setParkingTime(time);
    }

    public void ManualEntry(Record record){
        Scanner in = new Scanner(System.in);

        Calendar data = new GregorianCalendar();

        System.out.println("Введите год прибытия: ");
        if(in.hasNextInt()) {
            int year = in.nextInt();
            data.set(Calendar.YEAR, year);
        }

        System.out.println("Введите месяц прибытия: ");
        if(in.hasNextInt()) {
            int month = in.nextInt();
            data.set(Calendar.MONTH, month);
        }

        System.out.println("Введите день прибытия: ");
        if(in.hasNextInt()) {
            int day = in.nextInt();
            data.set(Calendar.DAY_OF_MONTH, day);
        }

        System.out.println("Введите час прибытия: ");
        if(in.hasNextInt()) {
            int hour = in.nextInt();
            data.set(Calendar.HOUR_OF_DAY, hour);
        }

        System.out.println("Введите минуты прибытия: ");
        if(in.hasNextInt()) {
            int minute = in.nextInt();
            data.set(Calendar.MINUTE, minute);
        }

        System.out.println("Введите секунды прибытия: ");
        if(in.hasNextInt()) {
            int second = in.nextInt();
            data.set(Calendar.SECOND, second);
            record.setDate(data);
        }

        System.out.println("Введите имя корабля: ");
        if(in.hasNextLine()) {
            String name = in.nextLine();
            record.setName(name);
        }

        System.out.println("Выберите тип груза (\"сыпучий\", \"жидкий\", \"контейнер\"): ");
        if(in.hasNextLine()) {
            String typeOfCargo = in.nextLine();
            record.setTypeOfCargo(typeOfCargo);
        }

        System.out.println("Задайте вес корабля: ");
        if(in.hasNextInt()) {
            Integer cargoWeight = in.nextInt();
            record.setCargoWeight(cargoWeight);
        }
    }
}
