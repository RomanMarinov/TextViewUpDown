package com.dev_marinov.textviewtest;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Time;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentAction extends Fragment{

    View view;
    TextView tvText;
    ConstraintLayout clFragAct;
    float width, height;
    Timer timerEn, timerRU;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.e("333","-зашел в FragmentAction-");
        view = inflater.inflate(R.layout.fragment_action, container, false);

        clFragAct = view.findViewById(R.id.clFragAct);
        tvText = view.findViewById(R.id.tvText);
        timerEn = new Timer();
        timerRU = new Timer();

        // слушатель касания экрана для ConstraintLayout
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("333","-зашел в OnTouchListener-");
                // получение координат ХУ касания
                ((MainActivity)getActivity()).x = (float) motionEvent.getX();
                ((MainActivity)getActivity()).y = (float) motionEvent.getY();
                // считывание действие касания
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                        if(((MainActivity)getActivity()).mLanguageCode.equals("en")) {
                        //Изменить локаль уровня приложения
                        ((MainActivity)getActivity()).setLocale(getActivity(),((MainActivity)getActivity()).mLanguageCode);
                        ((MainActivity)getActivity()).mLanguageCode = "ru";
                        }
                    else if(((MainActivity)getActivity()).mLanguageCode.equals("ru")){
                        //Изменить локаль уровня приложения
                        ((MainActivity)getActivity()).setLocale(getActivity(),((MainActivity)getActivity()).mLanguageCode);
                        ((MainActivity)getActivity()).mLanguageCode = "en";
                    }
                    //Необходимо воссоздать действие, чтобы отразить изменение пользовательского интерфейса.
                    //getActivity().recreate();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.llFragAction, new FragmentAction()).commit();
                }
                // false - второй раз после пересоздания макета onTouch не сработает (срабатывал 2 раза)
                return false;
            }
        };
        // установка слушателя на ConstraintLayout
        clFragAct.setOnTouchListener(onTouchListener);

        // условие для того, чтобы сделать только первое касание экрана с перемщением tvText без анимации
        if(((MainActivity)getActivity()).flagOnlyStart){
            ((MainActivity)getActivity()).flagOnlyStart = false;
        }
        else {
            setCoordinatesXYwithDisplay(); // установка позиции для tvText перед анимацией

            if(((MainActivity)getActivity()).mLanguageCode.equals("en")){ // англ локали
                tvText.setTextColor(Color.parseColor("#005DFF")); // красный цвет
                ((MainActivity)getActivity()).flagStartStopAnim = false;

                    timerEn.schedule(new TimerTask() { // таймер для англ
                        @Override
                        public void run() {
                            try {
                                ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity)getActivity()).flagStartStopAnim = true;
                                        animation(); // метод анимации
                                    }
                                });
                            }
                            catch (Exception e) {
                                Log.e("333","-try catch EN -" + e);
                            }
                        }
                    },5000);
                }

            if(((MainActivity)getActivity()).mLanguageCode.equals("ru")){ // рус локали
                tvText.setTextColor(Color.parseColor("#FB0000")); // синий цвет
                ((MainActivity)getActivity()).flagStartStopAnim = false;

                    timerRU.schedule(new TimerTask() { // таймер для рус
                        @Override
                        public void run() {
                         try {
                             ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     ((MainActivity)getActivity()).flagStartStopAnim = true;
                                     animation(); // метод анимации
                                 }
                             });
                             }
                             catch (Exception e) {
                                 Log.e("333","-try catch RU -" + e);
                             }
                        }
                    },5000);
                }
            }

        // клик по тексту только для того, чтобы его остановить (оставить таймеры и следовательно анимацию)
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerEn.cancel();
                timerRU.cancel();
            }
        });

        return view;
    }

    public void animation() {  // метод анимации
        //Log.e("333","-зашел в animation-");

        //setCoordinatesXYwithDisplay();

        // анимация движения tvText вниз равной половине высоты экрана (т.к. привязка в ConstraintLayout) с задержкой 1 сек
        tvText.animate().translationY(height / 2).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { // callback завершения анимании движения вниз
                super.onAnimationEnd(animation);
                // запуск анимации движения tvText вверх равной минус половине высоты экрана
                tvText.animate().translationY(-height / 2).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {// callback завершения анимании движения вверх
                        super.onAnimationEnd(animation);
                        try {
                            if (((MainActivity)getActivity()).flagStartStopAnim)
                            {
                                animation(); // повторный запуск анимации по циклу (движение вверх - вниз)
                            }
                        }
                        catch (Exception e){
                            Log.e("333","-try catch animation -" + e);
                        }
////                            tvText.animate().translationY(height).setDuration(1000).setListener(null);
////                            clFragAct.setOnTouchListener(null);
////
////                            tvText.animate().translationY(((MainActivity)getActivity()).y)
////                                    .translationX(((MainActivity)getActivity()).x).setDuration(1000).start();
                    }
                }).start();
            }
        }). start();

    }

    // метод вычисляет устанавливает занчение координат ХУ где будет textview с учетом его размеров
    public void setCoordinatesXYwithDisplay(){
        // вычислить размеры экрана
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        ((MainActivity)getActivity()).centreX = tvText.getWidth()  / 2;
        ((MainActivity)getActivity()).centreY = tvText.getHeight() / 2;
        // установка ХУ (последнего тач) с учетом вычисленной середины textview и размеров экраана
        tvText.setX((((MainActivity)getActivity()).x - ((MainActivity)getActivity()).centreX) - (width / 2));
        tvText.setY((((MainActivity)getActivity()).y - ((MainActivity)getActivity()).centreY) - (height / 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.e("333","-зашел в onDestroyView-");
        // закрываем таймеры при пересоздании макета, чтобы не крашилось
        ((MainActivity)getActivity()).flagStartStopAnim = false;
        timerEn.cancel();
        timerRU.cancel();
    }

}


