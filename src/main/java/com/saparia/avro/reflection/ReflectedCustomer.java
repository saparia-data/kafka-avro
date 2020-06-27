package com.saparia.avro.reflection;

import org.apache.avro.reflect.Nullable;

/**
 * Created by Shree on 24-06-2020.
 */
public class ReflectedCustomer {
    private String firstName;
    private String lastName;
    //this means union of null and strings ["null", "string"]
    @Nullable
    private String nickName;

    // needed by the reflection
    public ReflectedCustomer(){}

    public ReflectedCustomer(String firstName, String lastName, String nickName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String fullName(){
        return this.firstName + " " + this.lastName + " " + this.nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}