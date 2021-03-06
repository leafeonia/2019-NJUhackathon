package renaissance.njujiaowu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import renaissance.njujiaowu.MyPkg.CourseInfo;
import renaissance.njujiaowu.MyPkg.CourseSoup;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class homeworkItem extends AppCompatActivity {

    TextView mPickedTime;
    EditText mToDoname;
    EditText mToDoContent;
    Spinner mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPickedTime = (TextView)findViewById(R.id.pickedTime);
        mToDoname = (EditText)findViewById(R.id.todo_name);
        mToDoContent = (EditText)findViewById(R.id.todo_content);
        mSpinner = (Spinner)findViewById(R.id.toDo_spinner);
        toolbar.setTitle("作业++");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        SharedPreferences pref = getSharedPreferences("courses",MODE_PRIVATE);
        Map<String,String> all = (Map<String,String>) pref.getAll();
        if (all.size() == 0){
            if(CourseSoup.CourseList != null){
//                int term = 9;
//                for (CourseInfo courseInfo : CourseSoup.CourseList){
//                    term = min(courseInfo.CourseTerm,term);
//                }
//                pref.edit().putString("请选择","");
//                all.put("请选择","");
                SharedPreferences.Editor editor = pref.edit();
                for (CourseInfo courseInfo : CourseSoup.CourseList){
                    if (courseInfo.CourseTerm == -1){
                        editor.putString(courseInfo.CourseName,"");
                        all.put(courseInfo.CourseName,"");
                    }
                }
                editor.commit();
            }
        }
        Set<Map.Entry<String,String>> entrys = all.entrySet();
        List<Map.Entry<String,String>> courses = new ArrayList<>(entrys);

        mSpinner.setAdapter(new courseAdapter(courses,homeworkItem.this));


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)mSpinner.getItemAtPosition(position);
                mToDoname.setText(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_homework_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                Intent i = new Intent(homeworkItem.this,homework.class);
                startActivity(i);
                finish();
                break;
            case R.id.addToDo_tick:
                saveTodo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveTodo(){
        String toDoName = mToDoname.getText().toString().trim();
        String toDoContent = mToDoContent.getText().toString().trim();
        String toDoTime = mPickedTime.getText().toString().trim();
        if (toDoName == null || "".equals(toDoName) ){
            Toast.makeText(homeworkItem.this,"请输入作业名称",Toast.LENGTH_SHORT).show();
            return;
        }
        if(toDoTime == null || "".equals(toDoTime)){
            Toast.makeText(homeworkItem.this,"请输入ddl",Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("homeWorkItem",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("toDoName",toDoName);
//        editor.putString("toDoContent",toDoContent);
//        editor.putString("time",toDoTime);
//        editor.clear();
        editor.putString(toDoName,toDoContent+"|"+toDoTime+"|"+pickedYear+"`"+pickedMonth+"`"+pickedDate+"`"+pickedHour+"`"+pickedMinute);
        editor.commit();
        Intent i = new Intent(homeworkItem.this,homework.class);
        startActivity(i);
        finish();
    }


    int pickedYear,pickedMonth,pickedDate,pickedHour,pickedMinute;
    private void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(homeworkItem.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                pickedYear = year;
                pickedMonth = month+1;
                pickedDate = dayOfMonth;
                showTimePicker();
            }
        },currentYear,currentMonth,currentDate);
        datePickerDialog.show();

    }

    private void showTimePicker(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(homeworkItem.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pickedHour = hourOfDay;
                    pickedMinute = minute;
                    String temp;
                    if(pickedHour < 10) temp = "0"+pickedHour+":";
                    else temp = pickedHour+":";
                    if (pickedMinute < 10) temp += ("0"+pickedMinute);
                    else temp += pickedMinute;
                    mPickedTime.setText(pickedMonth+"/"+pickedDate+" "+temp);
            }
        },0,0,true);
        timePickerDialog.show();
    }

}
