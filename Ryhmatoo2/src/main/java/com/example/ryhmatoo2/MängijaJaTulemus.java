package com.example.ryhmatoo2;

public class MängijaJaTulemus implements Comparable<MängijaJaTulemus> {
    private String nimi;
    private int vahe;

    public MängijaJaTulemus(String nimi, int vahe) {
        this.nimi = nimi;
        this.vahe = vahe;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getVahe() {
        return vahe;
    }

    public void setVahe(int vahe) {
        this.vahe = vahe;
    }

    @Override
    public int compareTo(MängijaJaTulemus o) {
        return o.vahe-this.vahe;
    }

    @Override
    public String toString() {
        return "Nimi: "+nimi+"\nvõitude-kaotuste vahe: "+vahe+"\n";
    }
}
