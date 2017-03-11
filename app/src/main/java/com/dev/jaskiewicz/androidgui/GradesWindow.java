package com.dev.jaskiewicz.androidgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static com.dev.jaskiewicz.androidgui.MainWindow.AMOUNT_OF_GRADES;
import static com.dev.jaskiewicz.androidgui.MainWindow.AVERAGE_GRADE;

public class GradesWindow extends AppCompatActivity {
    public static final String LOG_TAG = GradesWindow.class.getSimpleName();
    public static final String AMOUNT_OF_GRADES_FOR_GRADES_LIST_FRAGMENT = "amount of grades for GradesListFragment";

    private static final String GRADES_LIST_FRAGMENT_KEY = GradesListFragment.class.getSimpleName();
    private static final int DEFAULT_AMOUNT_OF_GRADES = 0;

    private GradesListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_window);
        if (savedInstanceState == null) {
            int amountOfGrades = getAmountOfGradesFromMainWindow();
            createGradesListFragmentWith(amountOfGrades);
        } else {
            restoreFragmentFrom(savedInstanceState);
        }
    }

    private int getAmountOfGradesFromMainWindow() {
        return getIntent().getIntExtra(AMOUNT_OF_GRADES, DEFAULT_AMOUNT_OF_GRADES);
    }

    private void createGradesListFragmentWith(int amountOfGrades) {
        fragment = new GradesListFragment();
        fragment.setArguments(bundleWith(amountOfGrades));
        getFragmentManager()
                .beginTransaction()
                .add(R.id.grades_window, fragment)
                .commit();
    }

    private void restoreFragmentFrom(Bundle savedInstanceState) {
        fragment = (GradesListFragment) getFragmentManager().getFragment(savedInstanceState, GRADES_LIST_FRAGMENT_KEY);
    }

    /**
     * Metoda przygotowuje obiekt Bundle, do którego pakuje liczbę ocen,
     * aby było wiadomo ile elementów listy wyświetlić
     * Służy do przekazania liczby ocen do GradesListFragment
     */
    private Bundle bundleWith(int amountOfGrades) {
        Bundle bundle = new Bundle();
        bundle.putInt(AMOUNT_OF_GRADES_FOR_GRADES_LIST_FRAGMENT, amountOfGrades);
        return bundle;
    }

    /**
     * Metoda wywoływana jako onClick przycisku do zatwierdzenia wybranych wartości z listy ocen
     */
    public void goBackWithTheResult(View view) {
        if (fragment.everyGradeHasValue()) {
            prepareResult();
            finish();
        } else {
            Toast.makeText(this, R.string.enter_all_grades, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareResult() {
        double averageGrade = GradesCalculator.calculateAverageGradeFrom(fragment.getGrades());

        Intent backToMainWindow = new Intent();
        backToMainWindow.putExtras(createBundleWith(averageGrade));
        setResult(RESULT_OK, backToMainWindow);
    }

    private Bundle createBundleWith(double averageGrade) {
        Bundle bundle = new Bundle();
        bundle.putDouble(AVERAGE_GRADE, averageGrade);
        return bundle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, GRADES_LIST_FRAGMENT_KEY, fragment);
        super.onSaveInstanceState(outState);
    }
}