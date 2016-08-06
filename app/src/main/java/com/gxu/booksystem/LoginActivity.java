package com.gxu.booksystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gxu.booksystem.db.domain.Admin;
import com.gxu.booksystem.db.domain.Student;
import com.gxu.booksystem.receiver.BookSystemReceiver;
import com.gxu.booksystem.utils.CommonUrl;
import com.gxu.booksystem.utils.HttpUtils;
import com.gxu.booksystem.utils.JsonService;
import com.gxu.booksystem.utils.JsonTools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private final String SHARED_PREFERENCES_NAME = "sharedPreferences";
    private EditText user_value;
    private EditText password_value;
    private Button login;
    private Button reset;
    private RadioGroup group;
    private String userType;
    private static Object nowUser = null;
    private static Map<String, Object> map = null;
    private BookSystemReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        receiver=new BookSystemReceiver();

        // 当程序进入主页面的时候，他之后启动肯定就不是第一次启动了。所以我们可以在界面，或者是调用主页面的步骤中将他的状态设为false.
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", true);
        // 提交修改
        editor.commit();
        //那么这样就可以实现了，当程序第二次进入的时候，在启动也就进行了判断。。if() else() 就执行了你想让他执行的操作。

        user_value = (EditText) findViewById(R.id.user_value);
        password_value = (EditText) findViewById(R.id.password_value);
        group = (RadioGroup) this.findViewById(R.id.person);
        RadioButton student = (RadioButton) group.findViewById(R.id.student);
        student.setChecked(true);
        login = (Button) this.findViewById(R.id.login);
        reset = (Button) this.findViewById(R.id.reset);
        login.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

//    //注册广播
//    @Override
//    protected void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(receiver, filter);
//    }
//
//    // 卸载广播
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//        }
//    }


    // 登录按钮监听
    public void login() {
        String username = user_value.getText().toString();
        String password = password_value.getText().toString();
//        //对用户输入的密码进行des加密
//        byte[] bytePassword = DESUtils.desCrypto(password_value.getText().toString().getBytes(),
//                DESUtils.DES_KEY);
//        String password = new String(bytePassword);
        // 判断用户名是否为空
        if (username.equals("")) {
            msg(this, "提示", "用户名不能为空！");
        }
        // 判断密码是否为空
        else if (password.equals("")) {
            msg(this, "提示", "密码不能为空！");
        }

        // 输入不为空
        else {
            int len = group.getChildCount();// 获得单选按钮组的选项个数
            for (int i = 0; i < len; i++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(i);
                if (radioButton.isChecked()) {
                    userType = radioButton.getText().toString();
                    break;
                }
            }
            if (userType == null) {
                msg(this, "提示", "请选择用户类型");
            } else if (userType.equals("学生")) {
                Map<String, String> map = new HashMap<>();
                map.put("s_num", username);
                String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.STU_LOGIN_URL,
                        JsonService.createJsonString(map));
                Student student = JsonTools.getStudent("student", jsonString);
//                //对根据用户名从服务端获取的密码进行des加密
//                byte[] byteServerStuPassword = DESUtils.desCrypto(student.getS_pwd().toString().getBytes(),
//                        DESUtils.DES_KEY);
//                String serverStuPassword = new String(byteServerStuPassword);

                if (student.getS_pwd()!=null && student.getS_pwd().equals(password)) {
//                    msg(this, "成功信息", "学生登录成功！");
                    nowUser = student;
                    setNowUserMap(userType);

                    Bundle bundle = new Bundle();
                    bundle.putString("user_type", userType);
                    bundle.putSerializable("now_user", (Serializable) student);
                    launch(BaseActivity.class, bundle);
                    finish();

                    //将当前用户传递到学生端主界面
//                    launch(StuMainActivity.class,new Intent(),"now_user",student);
                } else {
                    msg(this, "失败信息", "用户名或密码错误，请重新输入！");
                    user_value.setText("");
                    password_value.setText("");
                }

            } else if (userType.equals("管理员")) {
                Map<String, String> map = new HashMap<>();
                map.put("m_num", username);
                String jsonString = HttpUtils.sendInfoToServerGetJsonData(CommonUrl.ADMIN_LOGIN_URL,
                        JsonService.createJsonString(map));
                Admin admin = JsonTools.getAdmin("admin", jsonString);
//                //对根据用户名从服务端获取的密码进行des加密
//                byte[] byteServerAdminPassword = DESUtils.desCrypto(admin.getM_pwd().toString().getBytes(),
//                        DESUtils.DES_KEY);
//                String serverAdminPassword = new String(byteServerAdminPassword);

                if (admin.getM_pwd()!=null && admin.getM_pwd().equals(password)) {
//                  msg(this, "成功信息", "管理员登录成功！");
                    nowUser = admin;
                    setNowUserMap(userType);

                    Bundle bundle = new Bundle();
                    bundle.putString("user_type", userType);
                    bundle.putSerializable("now_user", (Serializable) admin);
                    launch(BaseActivity.class, bundle);
                    finish();


                    //将当前用户传递到管理员端主界面
//                    launch(BaseActivity.class,new Intent(),"now_user",admin);
                } else {
                    msg(this, "失败信息", "用户名或密码错误，请重新输入！");
                    user_value.setText("");
                    password_value.setText("");
                }
            }
        }
    }

    public static void setNowUserMap(String key) {
        map = new HashMap<>();
        if (nowUser != null) {
            map.put(key, nowUser);
        } else {
            map.put(key, null);
        }
    }

    public static Map<String, Object> getNowUserMap() {
        return map;
    }

    public static Object getNowUser() {
        if (nowUser != null) {
            return nowUser;
        }
        return "";
    }

    public static void setNowUser(Object nowUser) {
        LoginActivity.nowUser = nowUser;
    }

    private void reset() {
        user_value.setText("");
        password_value.setText("");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.reset:
                reset();
                break;
            default:
                break;
        }
    }

    // 消息对话框
    public void msg(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_dialog_alert_holo_light);
        builder.setPositiveButton("确定", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void launch(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        launch_slideright2left(intent);
    }

    public void launch_slideright2left(Intent it) {
        startActivity(it);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

}
