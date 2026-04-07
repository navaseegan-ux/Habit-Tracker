package com.example.project1.Habit.Tracker.service;

import com.example.project1.Habit.Tracker.model.Habit;
import com.example.project1.Habit.Tracker.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  HabitService {

    private final HabitRepository repo;

    public HabitService(HabitRepository repo) {
        this.repo = repo;
    }

    public Habit createHabit(Habit habit) {
        return repo.save(habit);
    }

    public List<Habit> getAllHabits() {
        return repo.findAll();
    }

    public Habit getHabitById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Habit updateHabit(Long id, Habit updatedHabit) {
        Habit habit = repo.findById(id).orElse(null);

        if (habit != null) {
            habit.setName(updatedHabit.getName());
            habit.setDescription(updatedHabit.getDescription());
            habit.setCompleted(updatedHabit.isCompleted());
            return repo.save(habit);
        }

        return null;
    }

    public Habit toggleHabitStatus(Long id) {
        Habit habit = repo.findById(id).orElse(null);

        if (habit != null) {
            habit.setCompleted(!habit.isCompleted());
            return repo.save(habit);
        }

        return null;
    }

    public void deleteHabit(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }

    public boolean habitExists(Long id) {
        return repo.existsById(id);
    }
}
