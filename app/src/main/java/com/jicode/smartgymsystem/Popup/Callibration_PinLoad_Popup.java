package com.jicode.smartgymsystem.Popup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jicode.smartgymsystem.Lib.JCSharingPreferences;
import com.jicode.smartgymsystem.MainActivity;
import com.jicode.smartgymsystem.R;
import com.jicode.smartgymsystem.databinding.ActivityMyinfomationEditBinding;
import com.jicode.smartgymsystem.databinding.PopupPinCallibrationBinding;


public class Callibration_PinLoad_Popup extends Activity {
    Button ok, cancel;
    TextView levelText,contentText;
    int index = 0;
    int pinCount = 0;
    PopupPinCallibrationBinding binding;
    JCSharingPreferences jcSharingPreferences;
    public static Callibration_PinLoad_Popup instance = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = PopupPinCallibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        instance = this;
        jcSharingPreferences = new JCSharingPreferences(this);
        ok=(Button)findViewById(R.id.ok);
        levelText = findViewById(R.id.level);
        contentText = findViewById(R.id.contenttext);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.sendData("$re;");
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClose(view);
            }
        });

        MainActivity.instance.sendData("$rd;");
    }

    public void setDist(String dist)
    {
        if(index == 0) contentText.setText("???????????????\n"+dist+"cm");
    }
    public void mOnClose(View v){
        ++index;
        if(index == 1) {
            ok.setText("??????");
            contentText.setText("???????????? ????????? ??????????????????.");
            binding.countEdit.setVisibility(View.VISIBLE);
            MainActivity.instance.sendData("$de;");
        }else if(index == 2) {
            if (binding.countEdit.getText().length() > 0) {
                pinCount = Integer.parseInt(binding.countEdit.getText().toString());
                binding.countEdit.setVisibility(View.GONE);
                binding.countEdit.setText("");
                levelText.setVisibility(View.VISIBLE);
                ok.setText("??????");
                levelText.setText(String.valueOf(index - 1) + "/" + String.valueOf(pinCount));
                contentText.setText("???????????? " + String.valueOf(index - 1) + "??? ?????? \n????????? ??? ??????????????? \n???????????????.");
            } else {
                Toast.makeText(getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                index--;
            }
        }else if(index-2 < pinCount) {
            ok.setText("??????");
            levelText.setText(String.valueOf(index - 1) + "/" + String.valueOf(pinCount));
            contentText.setText("???????????? " + String.valueOf(index - 1) + "??? ?????? \n????????? ??? ??????????????? \n???????????????.");
            MainActivity.instance.sendData("$c"+(index-2)+";");
        }else if(index-2 == pinCount) {
            ok.setText("??????");
            levelText.setVisibility(View.INVISIBLE);
            contentText.setText("??? ???????????? ????????? ??????????????????(KG)");
            binding.countEdit.setVisibility(View.VISIBLE);
            binding.countEdit.setHint("?????? ??????(kg)");
            MainActivity.instance.sendData("$c"+(index-2)+";");
        }else if(index-2 == pinCount+1) {
            jcSharingPreferences.putKey("weight",Integer.parseInt(binding.countEdit.getText().toString()));
            binding.countEdit.setVisibility(View.GONE);
            binding.countEdit.setText("");
            contentText.setText("?????? ????????? ????????????\n????????????????????? ???????????????.");
            ok.setText("??????");
        }else if(index-2 == pinCount+2) {
            MainActivity.instance.sendData("$ce;");
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
