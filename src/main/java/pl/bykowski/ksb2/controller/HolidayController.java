package pl.bykowski.ksb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.bykowski.ksb2.model.Holiday;


@Controller
@RequestMapping("/holiday")
public class HolidayController {

    private RestTemplate restTemplate;
    private Holiday[] holidayList;
    private boolean hasErrors;


    public HolidayController() {
        restTemplate = new RestTemplate();
        holidayList = new Holiday[1];
        holidayList[0] = new Holiday();
        hasErrors = false;

    }

    @GetMapping()
    public String getData(Model model) {
        model.addAttribute("holidayList",holidayList);
        model.addAttribute("hasErrors",hasErrors);
        return "holidays";
    }

    @PostMapping()
    public String getHolidays(Model model, @RequestParam String year, String code) {
        StringBuilder url = new StringBuilder();
        url.append("https://date.nager.at/api/v2/publicholidays/").append(year).append("/").append(code);
        try {
            holidayList = restTemplate.getForObject(url.toString(), Holiday[].class);
        }
        catch(Exception e) {
            hasErrors = true;
            model.addAttribute("hasErrors",hasErrors);
        }

        model.addAttribute("holidayList",holidayList);
        return "holidays";
    }





}
