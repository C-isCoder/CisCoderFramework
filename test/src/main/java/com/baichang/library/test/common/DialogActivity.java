package com.baichang.library.test.common;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.baichang.android.utils.BCDateUtil;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends CommonActivity {
    private static final String[] ITEMS = new String[]{"item1", "item2", "item3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                BCDialogUtil.showDialog(getAty(), "标题", "内容");
                break;
            case R.id.button1:
                BCDialogUtil.getDialogItem(getAty(), "标题", ITEMS,
                        (dialog, which) -> {
                            showMessage(ITEMS[which]);
                        });
                break;
            case R.id.button2:
                BCDialogUtil.choiceTime(getAty(), BCDateUtil.getCurrentDate("yyyy-MM-dd"),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleData = new SimpleDateFormat("yyyy-MM-dd");
                            simpleData.setCalendar(date);
                            String strDate = simpleData.format(date.getTime());
                            showMessage(strDate);
                        });
                break;
            case R.id.button3:
                BCDialogUtil.choiceYear(getAty(), BCDateUtil.getCurrentDate("yyyy"),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleData = new SimpleDateFormat("yyyy");
                            simpleData.setCalendar(date);
                            String strDate = simpleData.format(date.getTime());
                            showMessage(strDate);
                        });
                break;
            case R.id.button4:
                BCDialogUtil.getInputDialog(getAty(), new EditText(getAty()), "标题",
                        (dialog, which) -> {
                            showMessage("确认");
                        }, (dialog, which) -> {
                            showMessage("取消");
                        });
                break;
            case R.id.button5:
                BCDialogUtil.showDialog(getAty(), "标题", "内容",
                        (dialog, which) -> {
                            showMessage("确定");
                        }, (dialog, which) -> {
                            showMessage("取消");
                        });
                break;

        }
    }
}
