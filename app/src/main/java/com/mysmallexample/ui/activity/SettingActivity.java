package com.mysmallexample.ui.activity;

import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mysmallexample.customview.switchbutton.SwitchButton;
import com.mysmallexample.ui.listener.TestListener;
import com.mysmallexample.ui.utils.Const;
import com.mysmallexample.ui.utils.LocationUtils;
import com.mysmallexample.ui.utils.Log;
import com.mysmallexample.ui.utils.PropertiesUtils;

import java.util.Properties;

import cn.jpush.android.api.JPushInterface;
import example.mysmallexample.R;

/**
 * Created by taoshuang on 2016/6/1.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static final String TAG = "SettingActivity";
    private static final int UPDATE_TEXT = 1;

    private View back;
    private SwitchButton notify_switcher;
    private TextView title;
    private View setting_font_size;
    private TextView font_size;
    private TestListener testListener;
    private SwitchButton net_switcher;

    private void setMyOnClick(TestListener testListener) {
        this.testListener = testListener;
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    font_size.setText("19sp");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 定位不成功
         */
        LocationUtils.getLocation(getApplicationContext());
        setContentView(R.layout.activity_setting);
        initViews();
        Log.i(TAG, "JPush is Stop=" + JPushInterface.isPushStopped(this));

        Log.i(TAG, "getMacAddress()=" + getMacAddress() + "\ngetMobileIMEI()=" + getMobileIMEI());
        Properties prop = new Properties();
        prop.put("prop1", "prop1");
        prop.put("prop2", "prop2");
        prop.put("prop3", "prop3");
        PropertiesUtils.saveConfig(this, "/sdcard/config.txt", prop);

        prop = PropertiesUtils.loadConfig(this, "/sdcard/config.txt");
        String prop1 = prop.getProperty("prop1");
        Log.i(TAG, "prop1:" + prop1);
//        testListener.onTestListener("123213");

    }

    /**
     * 初始化控件
     */
    private void initViews() {

        back = findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.setting));

        back.setOnClickListener(this);
        notify_switcher = (SwitchButton) findViewById(R.id.notify_switcher);
        if (spu.getOffTuiSong()) {
            notify_switcher.setChecked(true);
        } else {
            notify_switcher.setChecked(false);
        }
        notify_switcher.setOnCheckedChangeListener(this);

        setting_font_size = findViewById(R.id.setting_font_size);
        setting_font_size.setOnClickListener(this);
        font_size = (TextView) findViewById(R.id.font_size);
        font_size.setText("Handler测试");

        net_switcher= (SwitchButton) findViewById(R.id.net_switcher);
net_switcher.setOnCheckedChangeListener(this);



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.notify_switcher:
                pushSwitch(isChecked);
                break;
            case R.id.net_switcher:
                //TODO 省流量模式
                Const.toggleMode();
                checkMode();
                if (Const.isSaveMode()) {
                    Toast.makeText(this,""+R.string.switch_normal,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,""+R.string.switch_save_flow,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkMode() {
        if (Const.isSaveMode()) {
            Toast.makeText(this,""+R.string.page_best_mode,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,""+R.string.page_save_mode,Toast.LENGTH_SHORT).show();
        }
    }
    private void pushSwitch(boolean isChecked) {
//		保存推送开关状态
        spu.setOffTuiSong(isChecked);
        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.init(this);
            JPushInterface.resumePush(this);
        }
        if (!isChecked) {
            JPushInterface.stopPush(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                testListener.onTestListener("数据");
                finish();
                break;
            case R.id.setting_font_size:
                Message message = new Message();
                message.what = UPDATE_TEXT;
                handler.sendMessage(message);

                new AlertDialog.Builder(this)
                        .setTitle("字体大小")
                        .setIcon(R.drawable.ic_dialog_info2)
                        .setSingleChoiceItems(new String[] { "小号", "中号","大号" }, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null).show();

                break;
        }
    }

    /**
     * 获取Mac地址
     *
     * @return
     */
    public String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String address = wifiInfo.getMacAddress();
        return address;
    }

    /**
     * 获取IMEI号
     *
     * @return
     */
    public String getMobileIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;

    }
}
