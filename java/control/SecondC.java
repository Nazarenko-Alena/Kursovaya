package control;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import serv2.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/secondServ")
public class SecondC {

    private static String path;

    @GetMapping("/getSched")
    public String getSchedPage(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/firstServ/genSched";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        model.addAttribute("fromOne", responseEntity.getBody());

        return "files/getSchedFromOne";
    }

    @GetMapping("/schedToJson")
    public String toJsonPage(Model model) throws IOException {
        StatToJson.ScheduleToJson(GenSchedule.getSchedule());
        String path = "Schedule.json";

        model.addAttribute("toJson",new String(Files.readAllBytes(Paths.get(path))));
        return "files/schedToJson";
    }

    @GetMapping("/jsonFile")
    public String jsonFilePage(@RequestParam("name") String name, Model model) throws IOException {
        String path = "" + name + ".json";
        model.addAttribute("fileName", path);
        if(new File(path).isFile()){
            model.addAttribute("jsonF",new String(Files.readAllBytes(Paths.get(path))));
            SecondC.path = path;
        }
        else{
            model.addAttribute("jsonF","No such file exists");
        }

        return "files/jsonFile";
    }

    @GetMapping("/getPathToSchedule")
    public static String getPath(){
        return path;
    }

    @PostMapping("/resultStat")
    public String resultStat() throws IOException {

        FullStat fullStat = ThirdServ.getFullStat();
        SumStat stat = fullStat.getSumStat();
        StatToJson.SumStatToJson(stat);

        return "files/secondServ/resultStat";
    }

    @PostMapping("/resultSched")
    public String resultSched() throws IOException {

        FullStat fullStat = ThirdServ.getFullStat();
        Schedule schedule = fullStat.getSchedule();
        StatToJson.ScheduleToJson(schedule);

        return "files/secondServ/resultSched";
    }
}
