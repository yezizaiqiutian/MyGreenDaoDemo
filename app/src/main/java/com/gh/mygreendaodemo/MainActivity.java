package com.gh.mygreendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wyk.greendaodemo.greendao.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameET,et_name2;
    private Button mAddBtn, btn_delete,
            btn_update,
            btn_select;

    private ListView mUserLV;

    private UserAdapter mUserAdapter;
    private List<User> mUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mNameET = (EditText) findViewById(R.id.et_name);
        et_name2 = (EditText) findViewById(R.id.et_name2);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_select = (Button) findViewById(R.id.btn_select);
        mUserLV = (ListView) findViewById(R.id.lv_user);

        mAddBtn.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_select.setOnClickListener(this);
    }

    private void initData() {
        mUserList = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder().build().list();
        mUserAdapter = new UserAdapter(this, mUserList);
        mUserLV.setAdapter(mUserAdapter);
    }

    /**
     * 根据名字更新某条数据的名字
     *
     * @param prevName 原名字
     * @param newName  新名字
     */
    private void updateUser(String prevName, String newName) {
        User findUser = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Name.eq(prevName)).build().unique();
        if (findUser != null) {
            findUser.setName(newName);
            GreenDaoManager.getInstance().getSession().getUserDao().update(findUser);
            Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "用户不存在", Toast.LENGTH_SHORT).show();
        }

        mUserList.clear();
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    private void selectUser(String name) {
//        GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder()
//                .where(UserDao.Properties.Name.eq(name)).build().unique();
        List<User> list = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Name.eq(name)).build().list();

        mUserList.clear();
        mUserList.addAll(list);
        mUserAdapter.notifyDataSetChanged();
    }

    /**
     * 根据名字删除某用户
     *
     * @param name
     */
    private void deleteUser(String name) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        //如果有两个一样的名字会报错
        User findUser = userDao.queryBuilder().where(UserDao.Properties.Name.eq(name)).build().unique();
        if (findUser != null) {
            userDao.deleteByKey(findUser.getId());
        }

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    /**
     * 本地数据里添加一个User
     *
     * @param id   id
     * @param name 名字
     */
    private void insertUser(Long id, String name) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User user = new User(id, name);
        userDao.insert(user);
        mNameET.setText("");

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_add:
                insertUser(null, mNameET.getText().toString());
                break;
            case R.id.btn_delete:
                deleteUser(mNameET.getText().toString());
                break;
            case R.id.btn_update:
                updateUser(mNameET.getText().toString(),et_name2.getText().toString());
                break;
            case R.id.btn_select:
                selectUser(mNameET.getText().toString());
                break;

            default:
                break;
        }
    }
}
