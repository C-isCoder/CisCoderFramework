package com.baichang.library.test.common.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrCodeActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.test_generate_qrcode, R.id.test_scan_qrcode})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_scan_qrcode:
                startActivity(new Intent(this, ScanActivity.class));
                break;
            case R.id.test_generate_qrcode:
                startActivity(new Intent(this, GenerateActivity.class));
                break;
        }
    }
}
