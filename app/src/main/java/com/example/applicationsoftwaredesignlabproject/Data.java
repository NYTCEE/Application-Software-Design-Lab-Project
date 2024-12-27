package com.example.applicationsoftwaredesignlabproject;

public class Data {
    Result result;

    public static class Result {
        int limit;
        int offset;
        int count;
        String sort;
        Results[] results;

        public static class Results {
            String name;
            String age;
            String occupation;
            int _id;
            String UpdateTime;
        }
    }
}