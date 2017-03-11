package com.dev.jaskiewicz.androidgui.grades;

public class Grade {

    private static final int MINIMUM_VALUE_ALLOWED = 2;
    private static final int MAXIMUM_VALUE_ALLOWED = 5;

    private int id;
    private String label;
    private int value;
    private boolean hasValue = false;

    public Grade(int id) {
        this.id = id;
        this.label = "Ocena " + (id+1);
    }

    public String getLabel() {
        return label;
    }

    public void setValue(int value) {
        if (value < MINIMUM_VALUE_ALLOWED || value > MAXIMUM_VALUE_ALLOWED) {
            throw new IllegalArgumentException(value + " is an unacceptable value for grade. Value for grade must be greater or equal 2 and lower or equal 5");
        }
        this.value = value;
        this.hasValue = true;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public boolean hasValue() {
        return hasValue;
    }
}
