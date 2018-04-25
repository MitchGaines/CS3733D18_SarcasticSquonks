package edu.wpi.cs3733d18.teamS.service;

public class Day {
    private int day_num, num_requests;
    private String type_name;

    public Day(String type_name, int day_num, int num_requests) {
        this.day_num = day_num;
        this.num_requests = num_requests;
        this.type_name = type_name;
    }

    public int getDay_num() {

        return day_num;
    }

    public int getNum_requests() {
        return num_requests;
    }

    @Override
    public String toString() {
        return type_name;
    }

}
