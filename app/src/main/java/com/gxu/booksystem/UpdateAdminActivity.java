package com.gxu.booksystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;

import java.util.HashMap;
import java.util.Map;


public class UpdateAdminActivity extends BaseActivity implements View.OnClickListener{

    private EditText m_num_value, m_pwd_value,m_permitborrowtime_value;
    private Button update_admin;
    private Admin admin_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin);
        init();


    }

    private void init() {
        //获得管理员详细信息界面传过来的admin
        admin_update = (Admin)getIntent().getSerializableExtra("update_admin");

        m_num_value=(EditText)this.findViewById(R.id.m_num_value);
        m_pwd_value=(EditText)this.findViewById(R.id.m_pwd_value);
        m_permitborrowtime_value=(EditText)this.findViewById(R.id.m_permitborrowtime_value);

        m_num_value.setText(admin_update.getM_num());
        //管理员ID不可修改，修改之后无法插入数据库
        m_num_value.setEnabled(false);
        m_pwd_value.setText(admin_update.getM_pwd());
        m_permitborrowtime_value.setText(admin_update.getM_permitborrowtime());

        update_admin =(Button)this.findViewById(R.id.update_admin);
        update_admin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        updateAdmin();
    }

    private void updateAdmin() {
        Map<String, String> map = new HashMap<>();
        map.put("m_num", m_num_value.getText().toString());
        map.put("m_pwd", m_pwd_value.getText().toString());
        map.put("m_permitborrowtime", m_permitborrowtime_value.getText().toString());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.UPDATE_ADMIN_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "修改管理员成功！");
        } else {
            msg(this, "失败信息", "修改管理员失败！");
        }

    }

}
