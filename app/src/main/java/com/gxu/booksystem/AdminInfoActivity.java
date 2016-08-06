package com.gxu.booksystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;


public class AdminInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView m_num_value,m_pwd_value,m_permitborrowtime_value;
    private Button update_admin,delete_admin;
    private Admin admin_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);
        init();

    }

    /*
    *  private TextView s_num_value,s_name_value,s_age_value,s_sex_value,
            s_department_value,s_permitted_value,s_pwd_value;*/
    private void init() {
        //获得图书查询界面传过来的book
        admin_query = (Admin)getIntent().getSerializableExtra("admin_query");

        m_num_value=(TextView)this.findViewById(R.id.m_num_value);
        m_pwd_value=(TextView)this.findViewById(R.id.m_pwd_value);
        m_permitborrowtime_value=(TextView)this.findViewById(R.id.m_permitborrowtime_value);

        m_num_value.setText(admin_query.getM_num());
        m_pwd_value.setText(admin_query.getM_pwd());
        m_permitborrowtime_value.setText(admin_query.getM_permitborrowtime());

        update_admin=(Button)this.findViewById(R.id.update_admin);
        delete_admin=(Button)this.findViewById(R.id.delete_admin);
        update_admin.setOnClickListener(this);
        delete_admin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.update_admin:
                Bundle bundle = new Bundle();
                bundle.putSerializable("update_admin", admin_query);
                launch(UpdateAdminActivity.class,bundle);
                break;
            case R.id.delete_admin:
                deleteAdmin();
                break;
        }
    }

    private void deleteAdmin() {
        //将图书的编码传递到服务端
        Boolean flag = HttpUtils.sendJavaBeanToServer(CommonUrl.DELETE_ADMIN_URL,
                m_num_value.getText().toString());
        if (flag == true) {
            msg(this, "成功信息", "删除管理员成功！");
        } else {
            msg(this, "失败信息", "删除管理员失败！");
        }
    }
}
