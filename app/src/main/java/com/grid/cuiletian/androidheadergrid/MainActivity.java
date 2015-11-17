package com.grid.cuiletian.androidheadergrid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.grid.cuiletian.library.Day;
import com.grid.cuiletian.library.GridContent;
import com.grid.cuiletian.library.GridItemAdapter;
import com.grid.cuiletian.library.HeaderGridView;
import com.grid.cuiletian.library.ScreenUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private HeaderGridView headerGridView;
    private GridView grid;
    private GridItemAdapter adapter;
    private List<GridContent> gridList = new ArrayList<>();
    private List<String> tittleList = new ArrayList<>();
    private List<Day> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        headerGridView = (HeaderGridView) findViewById(R.id.header_grid);
        grid = (GridView) findViewById(R.id.content_grid);
        adapter = new GridItemAdapter(this, gridList);
        grid.setAdapter(adapter);
        headerGridView.setTittles(tittleList);
        headerGridView.setDays(days);
        grid.setNumColumns(tittleList.size());
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));

        FrameLayout.LayoutParams gridParams = (FrameLayout.LayoutParams) grid.getLayoutParams(); // 取控件mGrid当前的布局参数
        gridParams.width = tittleList.size() * ScreenUtil.dip2px(this, 70) + 1 * tittleList.size();
        grid.setLayoutParams(gridParams);


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridList.get(position).setIsSelect(!gridList.get(position).isSelect());
                adapter.notifyDataSetChanged();
            }
        });
        headerGridView.setOnTittleClickListener(new HeaderGridView.TittleClickListener() {

            @Override
            public void onTittleClick(int position) {
                Toast.makeText(MainActivity.this, String.valueOf(position) + " is Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initData() {
        int gridSize = 200;
        int tittleSize = 8;
        int dayNum = gridSize / tittleSize;
        for (int i = 0; i < gridSize; i++){
            GridContent content = new GridContent();
            content.setContentStr("content");
            if (i % 11 == 0){
                content.setBgColor(this.getResources().getColor(R.color.karekusairo));
            }
            if (i % 14 == 0){
                content.setBgColor(this.getResources().getColor(R.color.sekitiku));
            }
            if (i % 17 == 0){
                content.setBgColor(this.getResources().getColor(R.color.ensyuunezu));
            }
            if (i % 23 == 0){
                content.setBgColor(this.getResources().getColor(R.color.hatobamurasaki));
            }
            content.setContentStr("content" + String.valueOf(i));
            gridList.add(content);
        }

        for (int i = 0; i < tittleSize; i++){
            tittleList.add(String.valueOf(i));
        }

        Calendar calendarDay = Calendar.getInstance();
        for (int i = 0; i < dayNum; i++){
            Day day = new Day();
            day.setDayOfWeek(getDayOfWeek(calendarDay));
            day.setDateDisplay(formatCalendar(calendarDay));
            day.setIsHoliday(calendarDay.get(Calendar.DAY_OF_WEEK) == 7 || calendarDay.get(Calendar.DAY_OF_WEEK) == 1);
            days.add(day);
            calendarDay.add(Calendar.DATE, 1);
        }
    }

    private String formatCalendar(Calendar date){
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd");
        return bartDateFormat.format(date.getTime());
    }

    private String getDayOfWeek(Calendar date){
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("E");
        return bartDateFormat.format(date.getTime());
    }
}
