package com.dev.jaskiewicz.androidgui.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.jaskiewicz.androidgui.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainWindow extends AppCompatActivity {

    public static final String AMOUNT_OF_GRADES = "amount of grades";
    public static final String AVERAGE_GRADE = "average grade";

    public static final String AVERAGE_GRADE_RESULT_TAG = "average grade result";
    private static final String RETURN_TAG = "after return from grades window";
    private static final int SUMMARY_OF_GRADES_REQUEST = 1;
    private static final int MIN_AMOUNT_OF_GRADES = 5;
    private static final int MAX_AMOUNT_OF_GRADES = 15;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText amountOfGradesEditText;
    private Button gradesButton;
    private Button finishButton;
    private TextView gradesSummaryTextView;
    private double averageGradeResult;
    private boolean isAfterReturnFromGradesWindow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_window);
        setContentView(R.layout.main_window_relative);
        findAllViews();
        addTextChangeListenerToEditTexts();
        addOnFocusChangeListenersToEditTexts();
    }

    private void findAllViews() {
        firstNameEditText = (EditText) findViewById(R.id.first_name_edit_text);
        lastNameEditText = (EditText) findViewById(R.id.last_name_edit_text);
        amountOfGradesEditText = (EditText) findViewById(R.id.grades_edit_text);
        gradesButton = (Button) findViewById(R.id.grades_button);
        finishButton = (Button) findViewById(R.id.finish_button);
        gradesSummaryTextView = (TextView) findViewById(R.id.grades_summary_text_view);
    }

    /**
     * Tworzy obiekt TextWatcher, który pozwala obsłużyć zdarzenie zmiany tekstu w polach typu EditText
     * Przypisuje ten obiekt do wszystkich pól EditText, które mamy widoczne w tym oknie
     */
    private void addTextChangeListenerToEditTexts() {
        final TextWatcher textWatcher = createTextWatcher();
        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        amountOfGradesEditText.addTextChangedListener(textWatcher);
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            /**
             * @param s - niewykorzystywany, ponieważ w tym miejscu
             * nie potrzebuję przechować wpisanego przez użytkownika tekstu
             */
            @Override
            public void afterTextChanged(Editable s) {
                    if (inputFromAllEditTextsIsCorrect()) {
                        showGradesButton();
                    } else {
                        hideGradesButton();
                }
            }
        };
    }

    private boolean inputFromAllEditTextsIsCorrect() {
        return isFirstNameNotEmpty() &&
                isLastNameNotEmpty() &&
                properAmountOfGrades();
    }

    private boolean isFirstNameNotEmpty() {
        return !TextUtils.isEmpty(firstNameEditText.getText());
    }

    private boolean isLastNameNotEmpty() {
        return !TextUtils.isEmpty(lastNameEditText.getText());
    }

    private boolean properAmountOfGrades() {
        if (isAmountOfGradesEmpty()) {
            return false;
        }
        final int amountOfGrades = getAmountOfGrades();

        return amountOfGrades >= MIN_AMOUNT_OF_GRADES &&
                amountOfGrades <= MAX_AMOUNT_OF_GRADES;
    }

    private int getAmountOfGrades() {
        return Integer.parseInt(amountOfGradesEditText.getText().toString());
    }

    private boolean isAmountOfGradesEmpty() {
        return TextUtils.isEmpty(amountOfGradesEditText.getText());
    }

    private void showGradesButton() {
        gradesButton.setVisibility(VISIBLE);
    }

    private void hideGradesButton() {
        gradesButton.setVisibility(INVISIBLE);
    }

    /**
     * Tworzy listenery, które mają obsłużyć zdarzenie utraty focusu przez pola EditText
     * Dodaje je do każdego z z 3 pól (imie, nazwisko i liczba ocen
     * Dla imienia i nazwiska tworzony jest wspólny listener, ponieważ są one walidowane w ten sam sposób
     * Dla
     */
    private void addOnFocusChangeListenersToEditTexts() {
        addOnNameFocusChangeListenerToEditTexts();
        amountOfGradesEditText.setOnFocusChangeListener(new OnGradesFocusChangeListener());
    }

    private void addOnNameFocusChangeListenerToEditTexts() {
        final OnNameFocusChangeListener onOnNameFocusChangeListener = new OnNameFocusChangeListener();
        firstNameEditText.setOnFocusChangeListener(onOnNameFocusChangeListener);
        lastNameEditText.setOnFocusChangeListener(onOnNameFocusChangeListener);
    }

    private void showShortToastWith(String text) {
        Toast.makeText(MainWindow.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(RETURN_TAG, isAfterReturnFromGradesWindow);
        outState.putDouble(AVERAGE_GRADE_RESULT_TAG, averageGradeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        averageGradeResult = savedInstanceState.getDouble(AVERAGE_GRADE_RESULT_TAG);
        isAfterReturnFromGradesWindow = savedInstanceState.getBoolean(RETURN_TAG);
        if (isAfterReturnFromGradesWindow ) {
            setUIConfigurationForAfterReturnState();
        }
    }

    private void setUIConfigurationForAfterReturnState() {
        hideGradesButton();
        disableAllEditTexts();
        showGradesSummary();
        showFinishButton();
    }

    private void disableAllEditTexts() {
        firstNameEditText.setEnabled(false);
        lastNameEditText.setEnabled(false);
        amountOfGradesEditText.setEnabled(false);
    }

    public void onGradesClick(View view) {
        Intent intent = new Intent(this, GradesWindow.class);
        intent.putExtra(AMOUNT_OF_GRADES, getAmountOfGrades());
        startActivityForResult(intent, SUMMARY_OF_GRADES_REQUEST);
        registerThatWindowIsWaitingForTheResult();
    }

    private void registerThatWindowIsWaitingForTheResult() {
        isAfterReturnFromGradesWindow = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUMMARY_OF_GRADES_REQUEST && resultCode == RESULT_OK) {
            registerReturnFromGradesWindowWithTheResult();
            Bundle bundle = data.getExtras();
            averageGradeResult = bundle.getDouble(AVERAGE_GRADE);
            setUIConfigurationForAfterReturnState();
        }
    }

    /**
     * Ustawia flagę świadczącą o tym, że otrzymano wynik - średnią ocen z GradesWindow
     */
    private void registerReturnFromGradesWindowWithTheResult() {
        isAfterReturnFromGradesWindow = true;
    }

    private void showFinishButton() {
        finishButton.setText(createProperTextForFinishButton());
        finishButton.setVisibility(VISIBLE);
    }

    private String createProperTextForFinishButton() {
        if (studentFailed()) {
            return getString(R.string.student_failled_finish_button);
        } else {
            return getString(R.string.super_finish_button);
        }
    }

    private boolean studentFailed() {
        return averageGradeResult < 3.0;
    }

    private void showGradesSummary() {
        gradesSummaryTextView.setText(getString(R.string.your_average_grade_is));
        gradesSummaryTextView.append(String.format("%.2f", averageGradeResult));
        gradesSummaryTextView.setVisibility(VISIBLE);
    }

    public void onFinishClick(View view) {
        showShortToastWith(createProperGoodbyeMessage());
        finish();
    }

    private String createProperGoodbyeMessage() {
        if (studentFailed()) {
            return getString(R.string.student_failled_message);
        } else {
            return getString(R.string.succes_student_pass_message);
        }
    }

    /**
     * Listener wykorzystywany dla firstNameEditText i lastNameEditText
     * Używam jednego listenera, dlatego że dla obu tych pól stosuję jedną metodę walidacji
     */
    private class OnNameFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                EditText nameEditText = (EditText) view;
                if (isEmpty(nameEditText)) {
                    showMessageFor(nameEditText);
                }
            }
        }

        private boolean isEmpty(EditText editText) {
            return TextUtils.isEmpty(editText.getText());
        }

        private void showMessageFor(EditText nameEditText) {
            if (nameEditText.equals(firstNameEditText)) {
                showShortToastWith(getString(R.string.enter_first_name));
            } else if(nameEditText.equals(lastNameEditText)) {
                showShortToastWith(getString(R.string.enter_last_name));
            }
        }
    }

    private class OnGradesFocusChangeListener implements View.OnFocusChangeListener {
        /**
         * Podczas utraty focusu przez pole do wprowadzania liczby ocen
         * walidowana jest poprawność wprowadzonych danych i w razie potrzeby
         * wyświetlany jest komunikat z informacją o nieprawidłowym wypełnieniu pola (lub braku wypełnienia)
         */
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus && !properAmountOfGrades())
                showShortToastWith(getString(R.string.proper_amount_of_grades_message));
        }
    }
}
