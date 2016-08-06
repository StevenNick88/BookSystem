package com.gxu.booksystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;


public class StuInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView s_num_value,s_name_value,s_age_value,s_sex_value,
            s_department_value,s_pwd_value,s_permitborrowtime_value;
    private Button update_stu,delete_stu;
    private Student stu_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_info);
        init();

    }

    /*
    *  private TextView s_num_value,s_name_value,s_age_value,s_sex_value,
            s_department_value,s_permitted_value,s_pwd_value;*/
    private void init() {
        //获得图书查询界面传过来的book
        stu_query = (Student)getIntent().getSerializableExtra("stu_query");

        s_num_value=(TextView)this.findViewById(R.id.s_num_value);
        s_name_value=(TextView)this.findViewById(R.id.s_name_value);
        s_age_value=(TextView)this.findViewById(R.id.s_age_value);
        s_sex_value=(TextView)this.findViewById(R.id.s_sex_value);
        s_department_value=(TextView)this.findViewById(R.id.s_department_value);
        s_pwd_value=(TextView)this.findViewById(R.id.s_pwd_value);
        s_permitborrowtime_value=(TextView)this.findViewById(R.id.s_permitborrowtime_value);

        s_num_value.setText(stu_query.getS_num());
        s_name_value.setText(stu_query.getS_name());
        s_age_value.setText(stu_query.getS_age());
        s_sex_value.setText(stu_query.getS_sex());
        s_department_value.setText(stu_query.getS_department());
        s_pwd_value.setText(stu_query.getS_pwd());
        s_permitborrowtime_value.setText(stu_query.getS_permitborrowtime());

        update_stu=(Button)this.findViewById(R.id.update_stu);
        delete_stu=(Button)this.findViewById(R.id.delete_stu);
        update_stu.setOnClickListener(this);
        delete_stu.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.update_stu:
                Bundle bundle = new Bundle();
                bundle.putSerializable("update_stu", stu_query);
                launch(UpdateStuActivity.class,bundle);
                break;
            case R.id.delete_stu:
                deleteStu();
                break;
        }
    }

    private void deleteStu() {
        //将图书的编码传递到服务端
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.DELETE_STUDENT_URL,
                s_num_value.getText().toString());
        if (flag == true) {
            msg(this, "成功信息", "删除学生成功！");
        } else {
            msg(this, "失败信息", "删除学生失败！");
        }
    }
}
