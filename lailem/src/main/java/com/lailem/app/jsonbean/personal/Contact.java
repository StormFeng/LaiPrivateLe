package com.lailem.app.jsonbean.personal;

import java.io.Serializable;

/**
 * Created by XuYang on 15/10/7.
 */
public class Contact implements Serializable {
    private String name;
    private String phone;

    public Contact(){

    }

    public Contact(String name, String phone) {
        super();
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
