package com.dev.jaskiewicz.androidgui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class GradesAdapter extends ArrayAdapter<Grade> {
    public static final String LOG_TAG = GradesAdapter.class.getSimpleName();

    private static final int RESOURCE_NOT_NEEDED = 0;
    private static final boolean DO_NOT_ATTACH_TO_ROOT = false;
    private static final int CLEAR_CHECK = -1;

    private Activity context;
    private List<Grade> grades;

    public GradesAdapter(Context context, List<Grade> grades) {
        super(context, RESOURCE_NOT_NEEDED, grades);
        this.context = (Activity) context;
        this.grades = grades;
    }

    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {
        ViewHolder holder;
        if (isCreatedForTheFirstTime(listItemView)) {
            listItemView = LayoutInflater.from(context)
                    .inflate(R.layout.select_grade_list_item, parent, DO_NOT_ATTACH_TO_ROOT);
            holder = new ViewHolder();
            holder.radioGroup = (RadioGroup) listItemView.findViewById(R.id.grades_radio_group);
            holder.gradeLabel = (TextView) listItemView.findViewById(R.id.grade_label);
            listItemView.setTag(holder);
        } else {
            Log.d(LOG_TAG, "Recycle view on position: " + position);
            holder = (ViewHolder) listItemView.getTag();

            // muszę odznaczyć radio buttony, dlatego, że ten list item view powiazany bedzie z inną oceną
            holder.radioGroup.clearCheck();
        }
        Grade currentGrade = grades.get(position);
        updateViewHolderWithCurrentGrade(holder, currentGrade);

        return listItemView;
    }

    private void updateViewHolderWithCurrentGrade(ViewHolder holder, Grade currentGrade) {
        holder.gradeLabel.setText(currentGrade.getLabel());
        holder.radioGroup.setOnCheckedChangeListener(checkedChangeListenerFor(currentGrade));
        holder.radioGroup.check(radioButtonIdFor(currentGrade));
    }

    private int radioButtonIdFor(Grade grade) {
        int value = grade.getValue();
        if (value == 2) {
            return R.id.grade_two;
        } else if (value == 3) {
            return R.id.grade_three;
        } else if (value == 4) {
            return R.id.grade_four;
        } else if (value == 5) {
            return R.id.grade_five;
        } else {
            return CLEAR_CHECK;
        }
    }

    private boolean isCreatedForTheFirstTime(View listItemView) {
        return listItemView == null;
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListenerFor(final Grade currentGrade) {

        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.grade_two) {
                    currentGrade.setValue(2);
                } else if (checkedId == R.id.grade_three) {
                    currentGrade.setValue(3);
                } else if (checkedId == R.id.grade_four) {
                    currentGrade.setValue(4);
                } else if (checkedId == R.id.grade_five) {
                    currentGrade.setValue(5);
                }
            }
        };
    }

    private class ViewHolder {
        TextView gradeLabel;
        RadioGroup radioGroup;
    }
}
