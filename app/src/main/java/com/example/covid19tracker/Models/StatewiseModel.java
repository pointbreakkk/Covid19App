package com.example.covid19tracker.Models;

public class StatewiseModel
{
    private String state, confirmed, confirmed_new, active, death, death_new, recovered, recovered_new, lastupdated;

    public StatewiseModel(String state, String confirmed, String confirmed_new, String active, String death,
                          String death_new, String recovered, String recovered_new, String lastupdated)
    {
        this.state = state;
        this.confirmed = confirmed;
        this.confirmed_new = confirmed_new;
        this.active = active;
        this.death = death;
        this.death_new = death_new;
        this.recovered = recovered;
        this.recovered_new = recovered_new;
        this.lastupdated = lastupdated;
    }

    public String getState() {
        return state;
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

    public String getRecovered_new() {
        return recovered_new;
    }

    public String getLastupdated() {
        return lastupdated;
    }
}
