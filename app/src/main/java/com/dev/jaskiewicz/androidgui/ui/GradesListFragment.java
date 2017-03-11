package com.dev.jaskiewicz.androidgui.ui;


import android.app.ListFragment;
import android.os.Bundle;

import com.dev.jaskiewicz.androidgui.GradesAdapter;
import com.dev.jaskiewicz.androidgui.grades.Grade;

import java.util.ArrayList;
import java.util.List;

import static com.dev.jaskiewicz.androidgui.ui.GradesWindow.AMOUNT_OF_GRADES_FOR_GRADES_LIST_FRAGMENT;

public class GradesListFragment extends ListFragment {

    private int amountOfGrades;
    private List<Grade> grades;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getAmountOfGradesFromMainWindow();
        createGrades();
        addGradesAdapter();
    }

    private void getAmountOfGradesFromMainWindow() {
        amountOfGrades = getArguments().getInt(AMOUNT_OF_GRADES_FOR_GRADES_LIST_FRAGMENT);
    }

    private void createGrades() {
        grades = new ArrayList<>(amountOfGrades);
        for (int i = 0; i < amountOfGrades; i++) {
            grades.add(new Grade(i));
        }
    }

    private void addGradesAdapter() {
        GradesAdapter gradesAdapter = new GradesAdapter(getActivity(), grades);
        setListAdapter(gradesAdapter);
    }

    /**
     * Metoda używana do sprawdzenia czy wszystkie oceny z listy zostały zaznaczone
     */
    public boolean everyGradeHasValue() {
        for (Grade grade : grades) {
            if (!grade.hasValue()) {
                return false;
            }
        }
        return true;
    }

    public List<Grade> getGrades() {
        return grades;
    }
}
