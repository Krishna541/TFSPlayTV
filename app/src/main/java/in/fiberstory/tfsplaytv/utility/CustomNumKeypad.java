package in.fiberstory.tfsplaytv.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import in.fiberstory.tfsplaytv.R;


public class CustomNumKeypad extends LinearLayout{
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

     private LinearLayout lay_1,lay_2,lay_3,lay_4,lay_5,lay_6,lay_7,lay_8,lay_9,lay_0, lay_clear, lay_next;
    public CustomNumKeypad(Context context) {
        super(context);

    }



    public CustomNumKeypad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.custom_num_keypad, this);
        lay_clear = (LinearLayout) rootView.findViewById(R.id.lay_clear);
        lay_next = (LinearLayout) rootView.findViewById(R.id.lay_next);



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
        lay_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeField.postValue(true);
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

       /* btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeField.postValue(false);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeField.postValue(true);
            }
        });*/


    }


    public void setNumberFocus(){
        spl_state = false;
        lay_1.requestFocus();
    }

    public void setLastNumberFocus(String num){
        spl_state = false;

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

            case "backspace":
                lay_clear.requestFocus();
                break;
        }
    }

    public void hideDoneKey(){
        lay_next.setVisibility(View.GONE);
    }

    public void displayDoneKey(){
        lay_next.setVisibility(View.VISIBLE);
    }



}
