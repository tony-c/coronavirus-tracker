package com.tracker.coronavirustracker.controllers;

import com.tracker.coronavirustracker.models.LocationStats;
import com.tracker.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();

        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getNewestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDeltaPreviousDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }

}