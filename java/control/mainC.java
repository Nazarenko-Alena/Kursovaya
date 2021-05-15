package control;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class mainC {

    @GetMapping("/main")
    public String simulatePort(Model model) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/secondServ/getSched";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println("2.0 " + responseEntity.getStatusCode());

        url = "http://localhost:8080/secondServ/schedToJson";
        responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println("2.1 " + responseEntity.getStatusCode());

        url = "http://localhost:8080/secondServ//jsonFile?name=Schedule";
        responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println("2.2 " + responseEntity.getStatusCode());

        ThirdServ.getResult();

        url = "http://localhost:8080/secondServ/resultStat";

        responseEntity = restTemplate.postForEntity(url, 0, String.class);
        System.out.println("2.3.1 " + responseEntity.getStatusCode());

        String path = "C:\\Users\\alena\\IdeaProjects\\KursStep2\\src\\resources\\SumStat.json";
        model.addAttribute("stat", new String(Files.readAllBytes(Paths.get(path))));

        url = "http://localhost:8080/secondServ/resultSched";
        responseEntity = restTemplate.postForEntity(url, 0, String.class);
        System.out.println("2.3.2 " + responseEntity.getStatusCode());

        path = "C:\\Users\\alena\\IdeaProjects\\KursStep2\\src\\resources\\Schedule.json";
        model.addAttribute("sched", new String(Files.readAllBytes(Paths.get(path))));

        return"files/main";
    }
}
