package com.dev.jaskiewicz.androidgui.grades;

import java.util.List;

public class GradesCalculator {

    private GradesCalculator() {}

    public static double calculateAverageGradeFrom(List<Grade> grades) {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getValue();
        }
        return sum / grades.size();
    }
}
