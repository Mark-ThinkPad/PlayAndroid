package com.example.bmicalculator;

public class BMI {
    private long ID;
    private String name;
    private String sex;
    private String date;
    private float height;
    private float weight;
    private float bmi;

    public BMI() {}

    public BMI(long ID, String name, String sex, String date,
               float height, float weight, float bmi) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDate() {
        return date;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return  weight;
    }

    public float getBmi() {
        return bmi;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
