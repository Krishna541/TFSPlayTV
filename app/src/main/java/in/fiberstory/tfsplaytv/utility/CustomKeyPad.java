package in.fiberstory.tfsplaytv.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import in.fiberstory.tfsplaytv.R;


public class CustomKeyPad extends LinearLayout implements View.OnClickListener {
    private Context context;
    private boolean caps_state = false;
    private boolean spl_state = false;
    MutableLiveData<String> clicked_letter = new MutableLiveData<>();
    public LiveData<String> getClickedLetter(){
        return clicked_letter;
    }
    MutableLiveData<Boolean> clear_state = new MutableLiveData<>();
    public LiveData<Boolean> getClearState(){
        return clear_state;
    }
    MutableLiveData<Boolean> changeField = new MutableLiveData<>();
    public LiveData<Boolean> getFieldChange(){
        return changeField;
    }
    MutableLiveData<Boolean> clearChar = new MutableLiveData<>();
    public LiveData<Boolean> getClearChar(){
        return clearChar;
    }
    View rootView = null;
    LinearLayout lay_caps, lay_email_sugg, lay_clear;
    private TextView txt_l_a,txt_l_b,txt_l_c,txt_l_d,txt_l_e,txt_l_f,txt_l_g,txt_l_h,txt_l_i,txt_l_j,txt_l_k,txt_l_l,txt_l_m,txt_l_n,txt_l_o,txt_l_p,txt_l_q,txt_l_r,txt_l_s,txt_l_t,txt_l_u,txt_l_v,txt_l_w,txt_l_x,txt_l_y,txt_l_z,txt_uscore;
    private LinearLayout lay_a,lay_b,lay_c,lay_d,lay_e,lay_f,lay_g,lay_h,lay_i,lay_j,lay_k,lay_l,lay_m,lay_n,lay_o,lay_p,lay_q,lay_r,lay_s,lay_t,lay_u,lay_v,lay_w,lay_x,lay_y,lay_z,lay_uscore;
    private TextView txt_spl_1,txt_spl_2,txt_spl_3,txt_spl_4,txt_spl_5,txt_spl_6,txt_spl_7,txt_spl_8,txt_spl_9,txt_spl_10,txt_spl_11,txt_spl_12,txt_spl_13,txt_spl_14,txt_spl_15,txt_spl_16,txt_spl_17,txt_spl_18,txt_spl_19,txt_spl_20,txt_spl_21,txt_spl_22,txt_spl_23,txt_spl_24,txt_spl_25,txt_spl_26,txt_spl_27,txt_spl_28,txt_spl_29,txt_spl_30;
    private LinearLayout lay_spl_1,lay_spl_2,lay_spl_3,lay_spl_4,lay_spl_5,lay_spl_6,lay_spl_7,lay_spl_8,lay_spl_9,lay_spl_10,lay_spl_11,lay_spl_12,lay_spl_13,lay_spl_14,lay_spl_15,lay_spl_16,lay_spl_17,lay_spl_18,lay_spl_19,lay_spl_20,lay_spl_21,lay_spl_22,lay_spl_23,lay_spl_24,lay_spl_25,lay_spl_26,lay_spl_27,lay_spl_28,lay_spl_29,lay_spl_30;
    private LinearLayout lay_1,lay_2,lay_3,lay_4,lay_5,lay_6,lay_7,lay_8,lay_9,lay_0;
    private LinearLayout lay_at, lay_com, lay_dot, lay_space, lay_spl_char, lay_spl_chars, lay_chars;
    private TextView txt_btn_chars;
    private LinearLayout lay_email_1, lay_email_2, lay_email_3;
    private Button btn_next, btn_back;
    public CustomKeyPad(Context context) {
        super(context);

    }

    public void hideEmailSugg(){
        lay_email_sugg.setVisibility(View.GONE);
    }
    public void displayEmailSugg(){
        lay_email_sugg.setVisibility(View.VISIBLE);
    }



    public CustomKeyPad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.custom_keypad, this);
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_next = (Button)findViewById(R.id.btn_next);
        lay_email_sugg = (LinearLayout)rootView.findViewById(R.id.lay_email_sugg);
        lay_email_1 = (LinearLayout)rootView.findViewById(R.id.lay_email_1);
        lay_email_2 = (LinearLayout)rootView.findViewById(R.id.lay_email_2);
        lay_email_3 = (LinearLayout)rootView.findViewById(R.id.lay_email_3);
        lay_clear = (LinearLayout)rootView.findViewById(R.id.lay_clear);
        lay_caps = (LinearLayout)rootView.findViewById(R.id.lay_caps);
        lay_caps.setOnClickListener(this);
        txt_l_a = (TextView) rootView.findViewById(R.id.txt_l_a);
        txt_l_b = (TextView) rootView.findViewById(R.id.txt_l_b);
        txt_l_c = (TextView) rootView.findViewById(R.id.txt_l_c);
        txt_l_d = (TextView) rootView.findViewById(R.id.txt_l_d);
        txt_l_e = (TextView) rootView.findViewById(R.id.txt_l_e);
        txt_l_f = (TextView) rootView.findViewById(R.id.txt_l_f);
        txt_l_g = (TextView) rootView.findViewById(R.id.txt_l_g);
        txt_l_h = (TextView) rootView.findViewById(R.id.txt_l_h);
        txt_l_i = (TextView) rootView.findViewById(R.id.txt_l_i);
        txt_l_j = (TextView) rootView.findViewById(R.id.txt_l_j);
        txt_l_k = (TextView) rootView.findViewById(R.id.txt_l_k);
        txt_l_l = (TextView) rootView.findViewById(R.id.txt_l_l);
        txt_l_m = (TextView) rootView.findViewById(R.id.txt_l_m);
        txt_l_n = (TextView) rootView.findViewById(R.id.txt_l_n);
        txt_l_o = (TextView) rootView.findViewById(R.id.txt_l_o);
        txt_l_p = (TextView) rootView.findViewById(R.id.txt_l_p);
        txt_l_q = (TextView) rootView.findViewById(R.id.txt_l_q);
        txt_l_r = (TextView) rootView.findViewById(R.id.txt_l_r);
        txt_l_s = (TextView) rootView.findViewById(R.id.txt_l_s);
        txt_l_t = (TextView) rootView.findViewById(R.id.txt_l_t);
        txt_l_u = (TextView) rootView.findViewById(R.id.txt_l_u);
        txt_l_v = (TextView) rootView.findViewById(R.id.txt_l_v);
        txt_l_w = (TextView) rootView.findViewById(R.id.txt_l_w);
        txt_l_x = (TextView) rootView.findViewById(R.id.txt_l_x);
        txt_l_y = (TextView) rootView.findViewById(R.id.txt_l_y);
        txt_l_z = (TextView) rootView.findViewById(R.id.txt_l_z);
        txt_uscore = (TextView) rootView.findViewById(R.id.txt_uscore);

        lay_a = (LinearLayout)rootView.findViewById(R.id.lay_a);
        lay_b = (LinearLayout)rootView.findViewById(R.id.lay_b);
        lay_c = (LinearLayout)rootView.findViewById(R.id.lay_c);
        lay_d = (LinearLayout)rootView.findViewById(R.id.lay_d);
        lay_e = (LinearLayout)rootView.findViewById(R.id.lay_e);
        lay_f = (LinearLayout)rootView.findViewById(R.id.lay_f);
        lay_g = (LinearLayout)rootView.findViewById(R.id.lay_g);
        lay_h = (LinearLayout)rootView.findViewById(R.id.lay_h);
        lay_i = (LinearLayout)rootView.findViewById(R.id.lay_i);
        lay_j = (LinearLayout)rootView.findViewById(R.id.lay_j);
        lay_k = (LinearLayout)rootView.findViewById(R.id.lay_k);
        lay_l = (LinearLayout)rootView.findViewById(R.id.lay_l);
        lay_m = (LinearLayout)rootView.findViewById(R.id.lay_m);
        lay_n = (LinearLayout)rootView.findViewById(R.id.lay_n);
        lay_o = (LinearLayout)rootView.findViewById(R.id.lay_o);
        lay_p = (LinearLayout)rootView.findViewById(R.id.lay_p);
        lay_q = (LinearLayout)rootView.findViewById(R.id.lay_q);
        lay_r = (LinearLayout)rootView.findViewById(R.id.lay_r);
        lay_s = (LinearLayout)rootView.findViewById(R.id.lay_s);
        lay_t = (LinearLayout)rootView.findViewById(R.id.lay_t);
        lay_u = (LinearLayout)rootView.findViewById(R.id.lay_u);
        lay_v = (LinearLayout)rootView.findViewById(R.id.lay_v);
        lay_w = (LinearLayout)rootView.findViewById(R.id.lay_w);
        lay_x = (LinearLayout)rootView.findViewById(R.id.lay_x);
        lay_y = (LinearLayout)rootView.findViewById(R.id.lay_y);
        lay_z = (LinearLayout)rootView.findViewById(R.id.lay_z);
        lay_uscore = (LinearLayout)rootView.findViewById(R.id.lay_uscore);

        txt_spl_1 = (TextView)rootView.findViewById(R.id.txt_spl_1);
        txt_spl_2 = (TextView)rootView.findViewById(R.id.txt_spl_2);
        txt_spl_3 = (TextView)rootView.findViewById(R.id.txt_spl_3);
        txt_spl_4 = (TextView)rootView.findViewById(R.id.txt_spl_4);
        txt_spl_5 = (TextView)rootView.findViewById(R.id.txt_spl_5);
        txt_spl_6 = (TextView)rootView.findViewById(R.id.txt_spl_6);
        txt_spl_7 = (TextView)rootView.findViewById(R.id.txt_spl_7);
        txt_spl_8 = (TextView)rootView.findViewById(R.id.txt_spl_8);
        txt_spl_9 = (TextView)rootView.findViewById(R.id.txt_spl_9);
        txt_spl_10 = (TextView)rootView.findViewById(R.id.txt_spl_10);
        txt_spl_11 = (TextView)rootView.findViewById(R.id.txt_spl_11);
        txt_spl_12 = (TextView)rootView.findViewById(R.id.txt_spl_12);
        txt_spl_13 = (TextView)rootView.findViewById(R.id.txt_spl_13);
        txt_spl_14 = (TextView)rootView.findViewById(R.id.txt_spl_14);
        txt_spl_15 = (TextView)rootView.findViewById(R.id.txt_spl_15);
        txt_spl_16 = (TextView)rootView.findViewById(R.id.txt_spl_16);
        txt_spl_17 = (TextView)rootView.findViewById(R.id.txt_spl_17);
        txt_spl_18 = (TextView)rootView.findViewById(R.id.txt_spl_18);
        txt_spl_19 = (TextView)rootView.findViewById(R.id.txt_spl_19);
        txt_spl_20 = (TextView)rootView.findViewById(R.id.txt_spl_20);
        txt_spl_21 = (TextView)rootView.findViewById(R.id.txt_spl_21);
        txt_spl_22 = (TextView)rootView.findViewById(R.id.txt_spl_22);
        txt_spl_23 = (TextView)rootView.findViewById(R.id.txt_spl_23);
        txt_spl_24 = (TextView)rootView.findViewById(R.id.txt_spl_24);
        txt_spl_25 = (TextView)rootView.findViewById(R.id.txt_spl_25);
        txt_spl_26 = (TextView)rootView.findViewById(R.id.txt_spl_26);
        txt_spl_27 = (TextView)rootView.findViewById(R.id.txt_spl_27);
        txt_spl_28 = (TextView)rootView.findViewById(R.id.txt_spl_28);
        txt_spl_29 = (TextView)rootView.findViewById(R.id.txt_spl_29);
        txt_spl_30 = (TextView)rootView.findViewById(R.id.txt_spl_30);

        lay_spl_1 = (LinearLayout)rootView.findViewById(R.id.lay_spl_1);
        lay_spl_2 = (LinearLayout)rootView.findViewById(R.id.lay_spl_2);
        lay_spl_3 = (LinearLayout)rootView.findViewById(R.id.lay_spl_3);
        lay_spl_4 = (LinearLayout)rootView.findViewById(R.id.lay_spl_4);
        lay_spl_5 = (LinearLayout)rootView.findViewById(R.id.lay_spl_5);
        lay_spl_6 = (LinearLayout)rootView.findViewById(R.id.lay_spl_6);
        lay_spl_7 = (LinearLayout)rootView.findViewById(R.id.lay_spl_7);
        lay_spl_8 = (LinearLayout)rootView.findViewById(R.id.lay_spl_8);
        lay_spl_9 = (LinearLayout)rootView.findViewById(R.id.lay_spl_9);
        lay_spl_10 = (LinearLayout)rootView.findViewById(R.id.lay_spl_10);
        lay_spl_11 = (LinearLayout)rootView.findViewById(R.id.lay_spl_11);
        lay_spl_12 = (LinearLayout)rootView.findViewById(R.id.lay_spl_12);
        lay_spl_13 = (LinearLayout)rootView.findViewById(R.id.lay_spl_13);
        lay_spl_14 = (LinearLayout)rootView.findViewById(R.id.lay_spl_14);
        lay_spl_15 = (LinearLayout)rootView.findViewById(R.id.lay_spl_15);
        lay_spl_16 = (LinearLayout)rootView.findViewById(R.id.lay_spl_16);
        lay_spl_17 = (LinearLayout)rootView.findViewById(R.id.lay_spl_17);
        lay_spl_18 = (LinearLayout)rootView.findViewById(R.id.lay_spl_18);
        lay_spl_19 = (LinearLayout)rootView.findViewById(R.id.lay_spl_19);
        lay_spl_20 = (LinearLayout)rootView.findViewById(R.id.lay_spl_20);
        lay_spl_21 = (LinearLayout)rootView.findViewById(R.id.lay_spl_21);
        lay_spl_22 = (LinearLayout)rootView.findViewById(R.id.lay_spl_22);
        lay_spl_23 = (LinearLayout)rootView.findViewById(R.id.lay_spl_23);
        lay_spl_24 = (LinearLayout)rootView.findViewById(R.id.lay_spl_24);
        lay_spl_25 = (LinearLayout)rootView.findViewById(R.id.lay_spl_25);
        lay_spl_26 = (LinearLayout)rootView.findViewById(R.id.lay_spl_26);
        lay_spl_27 = (LinearLayout)rootView.findViewById(R.id.lay_spl_27);
        lay_spl_28 = (LinearLayout)rootView.findViewById(R.id.lay_spl_28);
        lay_spl_29 = (LinearLayout)rootView.findViewById(R.id.lay_spl_29);
        lay_spl_30 = (LinearLayout)rootView.findViewById(R.id.lay_spl_30);

        lay_1 = (LinearLayout)rootView.findViewById(R.id.lay_1);
        lay_2 = (LinearLayout)rootView.findViewById(R.id.lay_2);
        lay_3 = (LinearLayout)rootView.findViewById(R.id.lay_3);
        lay_4 = (LinearLayout)rootView.findViewById(R.id.lay_4);
        lay_5 = (LinearLayout)rootView.findViewById(R.id.lay_5);
        lay_6 = (LinearLayout)rootView.findViewById(R.id.lay_6);
        lay_7 = (LinearLayout)rootView.findViewById(R.id.lay_7);
        lay_8 = (LinearLayout)rootView.findViewById(R.id.lay_8);
        lay_9 = (LinearLayout)rootView.findViewById(R.id.lay_9);
        lay_0 = (LinearLayout)rootView.findViewById(R.id.lay_0);

        lay_at = (LinearLayout)rootView.findViewById(R.id.lay_at);
        lay_com = (LinearLayout)rootView.findViewById(R.id.lay_com);
        lay_dot = (LinearLayout)rootView.findViewById(R.id.lay_dot);
        lay_space = (LinearLayout)rootView.findViewById(R.id.lay_space);
        lay_spl_char = (LinearLayout)rootView.findViewById(R.id.lay_spl_char);
        lay_spl_chars = (LinearLayout)rootView.findViewById(R.id.lay_spl_chars);
        lay_chars = (LinearLayout)rootView.findViewById(R.id.lay_chars);
        txt_btn_chars = (TextView)rootView.findViewById(R.id.txt_btn_chars);
        txt_btn_chars.setText("!#$");
        lay_chars.setVisibility(View.VISIBLE);
        lay_spl_chars.setVisibility(View.GONE);
        lay_spl_char.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spl_state){
                    spl_state = false;
                    lay_chars.setVisibility(View.VISIBLE);
                    lay_spl_chars.setVisibility(View.GONE);
                    txt_btn_chars.setText("!#$");
                }else{
                    spl_state = true;
                    lay_chars.setVisibility(View.GONE);
                    lay_spl_chars.setVisibility(View.VISIBLE);
                    txt_btn_chars.setText("ABC");
                }
            }
        });


        lay_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("A");
                }else{
                    clicked_letter.postValue("a");
                }
            }
        });
        lay_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("B");
                }else{
                    clicked_letter.postValue("b");
                }
            }
        });
        lay_c.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("C");
                }else{
                    clicked_letter.postValue("c");
                }
            }
        });
        lay_d.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("D");
                }else{
                    clicked_letter.postValue("d");
                }
            }
        });
        lay_e.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("E");
                }else{
                    clicked_letter.postValue("e");
                }
            }
        });
        lay_f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("F");
                }else{
                    clicked_letter.postValue("f");
                }
            }
        });
        lay_g.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("G");
                }else{
                    clicked_letter.postValue("g");
                }
            }
        });
        lay_h.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("H");
                }else{
                    clicked_letter.postValue("h");
                }
            }
        });
        lay_i.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("I");
                }else{
                    clicked_letter.postValue("i");
                }
            }
        });
        lay_j.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("J");
                }else{
                    clicked_letter.postValue("j");
                }
            }
        });
        lay_k.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("K");
                }else{
                    clicked_letter.postValue("k");
                }
            }
        });
        lay_l.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("L");
                }else{
                    clicked_letter.postValue("l");
                }
            }
        });
        lay_m.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("M");
                }else{
                    clicked_letter.postValue("m");
                }
            }
        });
        lay_n.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("N");
                }else{
                    clicked_letter.postValue("n");
                }
            }
        });
        lay_o.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("O");
                }else{
                    clicked_letter.postValue("o");
                }
            }
        });
        lay_p.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("P");
                }else{
                    clicked_letter.postValue("p");
                }
            }
        });
        lay_q.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("Q");
                }else{
                    clicked_letter.postValue("q");
                }
            }
        });
        lay_r.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("R");
                }else{
                    clicked_letter.postValue("r");
                }
            }
        });
        lay_s.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("S");
                }else{
                    clicked_letter.postValue("s");
                }
            }
        });
        lay_t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("T");
                }else{
                    clicked_letter.postValue("t");
                }
            }
        });
        lay_u.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("U");
                }else{
                    clicked_letter.postValue("u");
                }
            }
        });
        lay_v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("V");
                }else{
                    clicked_letter.postValue("v");
                }
            }
        });
        lay_w.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("W");
                }else{
                    clicked_letter.postValue("w");
                }
            }
        });
        lay_x.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("X");
                }else{
                    clicked_letter.postValue("x");
                }
            }
        });
        lay_y.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("Y");
                }else{
                    clicked_letter.postValue("y");
                }
            }
        });
        lay_z.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(caps_state){
                    clicked_letter.postValue("Z");
                }else{
                    clicked_letter.postValue("z");
                }
            }
        });
        lay_uscore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("_");
            }
        });
        lay_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearChar.postValue(true);
                //clear_state.postValue(true);
            }
        });
        lay_clear.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clear_state.postValue(true);
                return true;
            }
        });
        lay_email_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("@hotmail.com");
            }
        });
        lay_email_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("@yahoo.com");
            }
        });
        lay_email_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("@gmail.com");
            }
        });
        lay_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("1");
            }
        });
        lay_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("2");
            }
        });
        lay_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("3");
            }
        });
        lay_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("4");
            }
        });
        lay_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("5");
            }
        });
        lay_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("6");
            }
        });
        lay_7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("7");
            }
        });
        lay_8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("8");
            }
        });
        lay_9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("9");
            }
        });
        lay_0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("0");
            }
        });
        lay_at.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue("@");
            }
        });
        lay_com.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(".com");
            }
        });
        lay_dot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(".");
            }
        });
        lay_space.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(" ");
            }
        });
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeField.postValue(false);
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeField.postValue(true);
            }
        });
        hideEmailSugg();

        lay_spl_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_1.getText().toString());
            }
        });
        lay_spl_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_2.getText().toString());
            }
        });
        lay_spl_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_3.getText().toString());
            }
        });
        lay_spl_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_4.getText().toString());
            }
        });
        lay_spl_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_5.getText().toString());
            }
        });
        lay_spl_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_6.getText().toString());
            }
        });
        lay_spl_7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_7.getText().toString());
            }
        });
        lay_spl_8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_8.getText().toString());
            }
        });
        lay_spl_9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_9.getText().toString());
            }
        });
        lay_spl_10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_10.getText().toString());
            }
        });
        lay_spl_11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_11.getText().toString());
            }
        });
        lay_spl_12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_12.getText().toString());
            }
        });
        lay_spl_13.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_13.getText().toString());
            }
        });
        lay_spl_14.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_14.getText().toString());
            }
        });
        lay_spl_15.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_15.getText().toString());
            }
        });
        lay_spl_16.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_16.getText().toString());
            }
        });
        lay_spl_17.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_17.getText().toString());
            }
        });
        lay_spl_18.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_18.getText().toString());
            }
        });
        lay_spl_19.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_19.getText().toString());
            }
        });
        lay_spl_20.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_20.getText().toString());
            }
        });
        lay_spl_21.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_21.getText().toString());
            }
        });
        lay_spl_22.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_22.getText().toString());
            }
        });
        lay_spl_23.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_23.getText().toString());
            }
        });
        lay_spl_24.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_24.getText().toString());
            }
        });
        lay_spl_25.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_25.getText().toString());
            }
        });
        lay_spl_26.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_26.getText().toString());
            }
        });
        lay_spl_27.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_27.getText().toString());
            }
        });
        lay_spl_28.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_28.getText().toString());
            }
        });
        lay_spl_29.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_29.getText().toString());
            }
        });
        lay_spl_30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_letter.postValue(txt_spl_30.getText().toString());
            }
        });
    }

    public void hideBack(){
        btn_back.setVisibility(View.GONE);
    }

    public void hideNext(){
        btn_next.setVisibility(View.GONE);
    }

    public void setTextFocus(){
        spl_state = false;
        lay_chars.setVisibility(View.VISIBLE);
        lay_spl_chars.setVisibility(View.GONE);
        lay_a.requestFocus();
    }

    public void setNumberFocus(){
        spl_state = false;
        lay_chars.setVisibility(View.VISIBLE);
        lay_spl_chars.setVisibility(View.GONE);
        lay_1.requestFocus();
    }

    public void setLastNumberFocus(String num){
        spl_state = false;
        lay_chars.setVisibility(View.VISIBLE);
        lay_spl_chars.setVisibility(View.GONE);
        switch(num){
            case "1":
                lay_1.requestFocus();
                break;
            case "2":
                lay_2.requestFocus();
                break;
            case "3":
                lay_3.requestFocus();
                break;
            case "4":
                lay_4.requestFocus();
                break;
            case "5":
                lay_5.requestFocus();
                break;
            case "6":
                lay_6.requestFocus();
                break;
            case "7":
                lay_7.requestFocus();
                break;
            case "8":
                lay_8.requestFocus();
                break;
            case "9":
                lay_9.requestFocus();
                break;
            case "0":
                lay_0.requestFocus();
                break;
            case "_":
                lay_uscore.requestFocus();
                break;
            case "backspace":
                lay_clear.requestFocus();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lay_caps :
                if(caps_state){
                    caps_state = false;
                }else{
                    caps_state = true;
                }
                updateLetterCase();
        }
    }


    private void updateLetterCase(){
        if(caps_state){
            txt_l_a.setText("A");
            txt_l_b.setText("B");
            txt_l_c.setText("C");
            txt_l_d.setText("D");
            txt_l_e.setText("E");
            txt_l_f.setText("F");
            txt_l_g.setText("G");
            txt_l_h.setText("H");
            txt_l_i.setText("I");
            txt_l_j.setText("J");
            txt_l_k.setText("K");
            txt_l_l.setText("L");
            txt_l_m.setText("M");
            txt_l_n.setText("N");
            txt_l_o.setText("O");
            txt_l_p.setText("P");
            txt_l_q.setText("Q");
            txt_l_r.setText("R");
            txt_l_s.setText("S");
            txt_l_t.setText("T");
            txt_l_u.setText("U");
            txt_l_v.setText("V");
            txt_l_w.setText("W");
            txt_l_x.setText("X");
            txt_l_y.setText("Y");
            txt_l_z.setText("Z");
        }else{
            txt_l_a.setText("a");
            txt_l_b.setText("b");
            txt_l_c.setText("c");
            txt_l_d.setText("d");
            txt_l_e.setText("e");
            txt_l_f.setText("f");
            txt_l_g.setText("g");
            txt_l_h.setText("h");
            txt_l_i.setText("i");
            txt_l_j.setText("j");
            txt_l_k.setText("k");
            txt_l_l.setText("l");
            txt_l_m.setText("m");
            txt_l_n.setText("n");
            txt_l_o.setText("o");
            txt_l_p.setText("p");
            txt_l_q.setText("q");
            txt_l_r.setText("r");
            txt_l_s.setText("s");
            txt_l_t.setText("t");
            txt_l_u.setText("u");
            txt_l_v.setText("v");
            txt_l_w.setText("w");
            txt_l_x.setText("x");
            txt_l_y.setText("y");
            txt_l_z.setText("z");
        }
    }
}
