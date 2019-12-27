package com.iakie.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    ViewPager mainVp;

    // ViewPager的数据源
    List<Fragment> fragmentList;
    // 表示需要显示的城市的集合
    List<String>cityList;
    // 表示ViewPager的页数指示器显示集合
    List<ImageView>imgList;
    private CityFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainVp = findViewById(R.id.main_vp);

        // 添加点击事件
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        cityList = new ArrayList<>();
        imgList = new ArrayList<>();

        if (cityList.size()==0) {
            cityList.add("北京");
        }

        // 初始化ViewPager页面的方法
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        
        // 创建小圆点指示器
        initPoint();
    }

    private void initPoint() {
    }

    private void initPager() {
        // 创建Fragment对象，添加到ViewPager数据源当中
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_add:

                break;
            case R.id.main_iv_more:

                break;
        }
    }
}
