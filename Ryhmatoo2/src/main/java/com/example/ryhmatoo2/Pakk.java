package com.example.ryhmatoo2;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class Pakk {
    private List<Kaart> pakk;
    public Pakk() {
        this.pakk = new ArrayList<Kaart>();
        String[] tugevused = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
        char[] mastid = {'♣', '♦', '♠', '♥'};

        for (char mast : mastid) {
            for (String tugevus : tugevused) {
                pakk.add(new Kaart(tugevus, mast));
            }

        }
    }

    public Pakk(List<Kaart> võimalikPakk) {
        boolean kontroll = true;
        for (int i = 0; i < võimalikPakk.size(); i++) {
            int arv = 0;
            for (int j = 0; j < võimalikPakk.size(); j++) {
                if (võimalikPakk.get(i).getMast() == võimalikPakk.get(j).getMast() && võimalikPakk.get(i).getTugevus().equals(võimalikPakk.get(j).getTugevus()))
                    arv++;
            }
            if (arv != 1)
                kontroll = false;
        }

        if (!kontroll)
            throw new RuntimeException("Pakis on korduvaid kaarte.");
        else {
            List<Kaart> abi = new ArrayList<>();
            for (Kaart kaart : võimalikPakk) {
                abi.add(kaart);
            }
            this.pakk = abi;
        }

    }

    public List<Kaart> getKaardid() {
        List<Kaart> abi = new ArrayList<>();
        for (Kaart kaart : pakk) {
            abi.add(kaart);
        }
        return abi;
    }

    public void sega() {
        shuffle(pakk);
    }

    public Kaart võtaKaart() {
        if (pakk.size() == 0)
            throw new RuntimeException("Pakk on tühi");
        Kaart kaart = pakk.get(0);
        pakk.remove(kaart);
        return kaart;
    }
}
