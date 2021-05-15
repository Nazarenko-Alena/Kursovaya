package control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serv2.GenSchedule;
import serv2.Schedule;

@Controller
@RequestMapping("/firstServ")
public class FirstC {

    @GetMapping("/genSched")
    public String getGenSchedule(Model model){
        GenSchedule genSchedule = new GenSchedule();
        genSchedule.GenerateSchedule();
        Schedule schedule = GenSchedule.getSchedule();

        model.addAttribute("dry", schedule.scheduleForDryToString());
        model.addAttribute("liquid", schedule.scheduleForLiquidToString());
        model.addAttribute("cont", schedule.scheduleForContToString());

        return "files/first";
    }


}
