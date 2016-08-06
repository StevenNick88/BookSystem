package com.gxu.booksystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;

import java.util.HashMap;
import java.util.Map;


public class UpdateStuActivity extends BaseActivity implements View.OnClickListener{

    private EditText s_num_value, s_name_value, s_age_value, s_sex_value, s_department_value,
            s_pwd_value,s_permitborrowtime_value;
    private Button update_stu;
    private Student stu_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stu);
        init();


    }

    private void init() {
        //获得学生详细信息界面传过来的book
        stu_update = (Student)getIntent().getSerializableExtra("update_stu");

        s_num_value=(EditText)this.findViewById(R.id.s_num_value);
        s_name_value=(EditText)this.findViewById(R.id.s_name_value);
        s_age_value=(EditText)this.findViewById(R.id.s_age_value);
        s_sex_value=(EditText)this.findViewById(R.id.s_sex_value);
        s_department_value=(EditText)this.findViewById(R.id.s_department_value);
        s_pwd_value=(EditText)this.findViewById(R.id.s_pwd_value);
        s_permitborrowtime_value=(EditText)this.findViewById(R.id.s_permitborrowtime_value);

        s_num_value.setText(stu_update.getS_num());
        //学生ID不可修改，修改之后无法插入数据库
        s_num_value.setEnabled(false);
        s_name_value.setText(stu_update.getS_name());
        s_age_value.setText(stu_update.getS_age());
        s_sex_value.setText(stu_update.getS_sex());
        s_department_value.setText(stu_update.getS_department());
        s_pwd_value.setText(stu_update.getS_pwd());
        s_permitborrowtime_value.setText(stu_update.getS_permitborrowtime());

        update_stu =(Button)this.findViewById(R.id.update_stu);
        update_stu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        updateStu();
    }

    private void updateStu() {
        Map<String, String> map = new HashMap<>();
        map.put("s_num", s_num_value.getText().toString());
        map.put("s_name", s_name_value.getText().toString());
        map.put("s_age", s_age_value.getText().toString());
        map.put("s_sex", s_sex_value.getText().toString());
        map.put("s_department", s_department_value.getText().toString());
        map.put("s_pwd", s_pwd_value.getText().toString());
        map.put("s_permitborrowtime", s_permitborrowtime_value.getText().toString());
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.UPDATE_STUDENT_URL,
                JsonService.createJsonString(map));
        if (flag == true) {
            msg(this, "成功信息", "修改学生成功！");
        } else {
            msg(this, "失败信息", "修改学生失败！");
        }

    }

}
