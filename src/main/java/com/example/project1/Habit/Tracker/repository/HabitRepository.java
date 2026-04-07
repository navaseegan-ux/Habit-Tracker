package com.example.project1.Habit.Tracker.repository;

import com.example.project1.Habit.Tracker.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
