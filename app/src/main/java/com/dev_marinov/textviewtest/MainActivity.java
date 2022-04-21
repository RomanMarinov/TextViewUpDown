package com.dev_marinov.textviewtest;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String mLanguageCode = "en"; // для смены языка
    float x, y; // координаты тач
    float centreX, centreY; // значения размеров tvText
    // flag чтобы сделать только первое касание экрана с перемщением tvText без анимации
    boolean flagOnlyStart = true;
    boolean flagStartStopAnim = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWindow(); // установка ui
        getSupportActionBar().hide(); // скрыть экшенбар

        setTransition(); // переход во FragmentAction
    }

    public void setWindow()
    {
        Window window = getWindow();
        // установка градиента анимации на toolbar
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Drawable background = getResources().getDrawable(R.drawable.gradient_backgroud);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS Флаг, указывающий, что это Окно отвечает за отрисовку фона для системных полос.
        // Если установлено, системные панели отображаются с прозрачным фоном, а соответствующие области в этом окне заполняются цветами,
        // указанными в Window#getStatusBarColor()и Window#getNavigationBarColor().
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));
        window.setBackgroundDrawable(background);
    }

    public void setTransition() {
        FragmentAction fragmentAction = new FragmentAction();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llFragAction, fragmentAction);
        fragmentTransaction.commit();
    }

    public void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}