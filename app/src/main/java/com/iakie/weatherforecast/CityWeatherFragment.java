package com.iakie.weatherforecast;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iakie.weatherforecast.bean.WeatherBean;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener{

    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothIndexTv,carIndexTv,coldIndexTv,sporttIndexTv,raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;
    String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    private List<WeatherBean.ResultsBean.IndexBean> indexList;
    private WeatherBean.ResultsBean.WeatherDataBean todayDataBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);

        // 可以通过activity传值获取到当前fregment加载的是哪个地方的天气情况
        Bundle bundle = getArguments();
        String city = bundle.getString("city");
        String url = url1 + city + url2;

        // 调用父类获取数据的方法
        loadData(url);

        return view;
    }

    @Override
    public void onSuccess(String result) {
        // 成功请求，解析并展示数据
        parseShowData(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
    }

    private void parseShowData(String result) {
        // 使用Gson来解析数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
        // 获取指数信息集合列表
        indexList = resultsBean.getIndex();
        // 设置TextView
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
        // 获取今日天气情况
        todayDataBean = resultsBean.getWeather_data().get(0);
        windTv.setText(todayDataBean.getWind());
        tempRangeTv.setText(todayDataBean.getTemperature());
        conditionTv.setText(todayDataBean.getWeather());
        // 获取实时天气温度情况，需要处理字符串
        String[] split = todayDataBean.getDate().split(": ");
        String todayTemp = split[1].replace(")","");
        tempTv.setText(todayTemp);
        // 设置显示的天气情况图片
        Picasso.with(getActivity()).load(todayDataBean.getDayPictureUrl()).into(dayIv);

        // 获取未来三天的天气情况，加载到layout当中
        List<WeatherBean.ResultsBean.WeatherDataBean> futureList = resultsBean.getWeather_data();
        futureList.remove(0);
        for (int i = 0; i < futureList.size(); i++){
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);
            TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp);
            ImageView iTv = itemView.findViewById(R.id.item_center_iv);
            // 获取对应的位置的天气情况
            WeatherBean.ResultsBean.WeatherDataBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getWeather());
            itemprangeTv.setText(dataBean.getTemperature());
            Picasso.with(getActivity()).load(dataBean.getDayPictureUrl()).into(iTv);
        }
    }

    private void initView(View view) {
        // 用于初始化控件操作
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        coldIndexTv = view.findViewById(R.id.frag_index_tv_cold);
        sporttIndexTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rays);
        dayIv = view.findViewById(R.id.frag_iv_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);

        // 设置点击事件的监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sporttIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                WeatherBean.ResultsBean.IndexBean indexBean = indexList.get(0);
                String msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                indexBean = indexList.get(1);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                indexBean = indexList.get(2);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动指数");
                indexBean = indexList.get(3);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                indexBean = indexList.get(4);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
