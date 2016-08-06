package com.gxu.booksystem.db.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/2/9.
 */

public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;
    private int m_id;
    private String m_num;
    private String m_pwd;
    private String m_permitborrowtime;

    public Admin() {
    }



    public Admin(int m_id, String m_num, String m_pwd, String m_permitborrowtime) {
        super();
        this.m_id = m_id;
        this.m_num = m_num;
        this.m_pwd = m_pwd;
        this.m_permitborrowtime = m_permitborrowtime;
    }



    @Override
    public String toString() {
        return "Admin [m_id=" + m_id + ", m_num=" + m_num + ", m_pwd=" + m_pwd
                + ", m_permitborrowtime=" + m_permitborrowtime + "]";
    }



    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getM_num() {
        return m_num;
    }

    public void setM_num(String m_num) {
        this.m_num = m_num;
    }

    public String getM_pwd() {
        return m_pwd;
    }

    public void setM_pwd(String m_pwd) {
        this.m_pwd = m_pwd;
    }

    public String getM_permitborrowtime() {
        return m_permitborrowtime;
    }

    public void setM_permitborrowtime(String m_permitborrowtime) {
        this.m_permitborrowtime = m_permitborrowtime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }



}