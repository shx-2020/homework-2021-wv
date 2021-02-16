package xyz.imaginarycrisis.wanandroidapp;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class

Tools {
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setStatusBarLightMode(Activity activity) {
        try{if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String streamToString(InputStream in){
        StringBuilder sb = new StringBuilder();
        String oneLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while((oneLine = reader.readLine())!=null){
                sb.append(oneLine).append('\n');
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    public static void setupTopBarViews(Activity activity, String title, int tarLayoutId,
                                        boolean defaultBackButton, @Nullable View.OnClickListener backListener,
                                        boolean defaultRefreshMethod, @Nullable View.OnClickListener refreshListener){
        TextView titleTv;
        titleTv = activity.findViewById(tarLayoutId).findViewById(R.id.top_view_bar_title);
        titleTv.setText(title);
        Tools.setWindowStatusBarColor(activity,R.color.orange_for_top_view_bar);
        ImageButton btnBack = activity.findViewById(tarLayoutId).findViewById(R.id.top_view_bar_back_button);
        TextView tvRefresh = activity.findViewById(tarLayoutId).findViewById(R.id.top_view_bar_refresh);
        if(defaultBackButton) {
            backListener = v -> activity.finish();
        }
        btnBack.setOnClickListener(backListener);
        if(defaultRefreshMethod) {
            tvRefresh.setOnClickListener(v -> Toast.makeText(activity, "刷新按钮未定义", Toast.LENGTH_SHORT).show());
        }
        else{
            tvRefresh.setOnClickListener(refreshListener);
        }
    }

}
