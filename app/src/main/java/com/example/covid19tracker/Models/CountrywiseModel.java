package com.example.covid19tracker.Models;

public class CountrywiseModel
{
    private String country, confirmed, confirmed_new, active, death, death_new, recovered, tests, flag, recovered_new;



    public CountrywiseModel(String country, String confirmed, String confirmed_new, String active, String death,
                            String death_new, String recovered, String tests, String flag, String recovered_new)
    {
        this.country = country;
        this.confirmed = confirmed;
        this.confirmed_new = confirmed_new;
        this.active = active;
        this.death = death;
        this.death_new = death_new;
        this.recovered = recovered;
        this.tests = tests;
        this.flag = flag;
        this.recovered_new = recovered_new;
    }

    public String getCountry() {
        return country;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getConfirmed_new() {
        return confirmed_new;
    }

    public String getActive() {
        return active;
    }

    public String getDeath() {
        return death;
    }

    public String getDeath_new() {
        return death_new;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getTests() {
        return tests;
    }

    public String getFlag() {
        return flag;
    }

    public String getRecovered_new() {
        return recovered_new;
    }
}
