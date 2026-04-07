package com.example.project1.Habit.Tracker.controller;

import com.example.project1.Habit.Tracker.model.Habit;
import com.example.project1.Habit.Tracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HabitPageController {

    private final HabitService habitService;

    public HabitPageController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/")
    public String showHabits(Model model) {
        var habits = habitService.getAllHabits();
        long completedCount = habits.stream().filter(Habit::isCompleted).count();

        model.addAttribute("habits", habits);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("pendingCount", habits.size() - completedCount);
        return "dashboard";
    }

    @PostMapping("/add")
    public String addHabit(@RequestParam String name, @RequestParam String description) {
        habitService.createHabit(new Habit(name, description, false));
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateHabit(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam(defaultValue = "false") boolean completed) {
        Habit updatedHabit = new Habit(name, description, completed);
        habitService.updateHabit(id, updatedHabit);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleHabit(@PathVariable Long id) {
        habitService.toggleHabitStatus(id);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return "redirect:/";
    }
}
