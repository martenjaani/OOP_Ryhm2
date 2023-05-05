package com.example.ryhmatoo2;

public class Kaart implements Comparable<Kaart> {
    private String tugevus;
    private char mast;

    public Kaart(String tugevus, char mast) {
        String[] tugevused = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
        char[] mastid = {'♣', '♦', '♠', '♥'};
        boolean tugevusKontroll = false;
        boolean mastiKontroll = false;

        for (String t : tugevused) {
            if (t.equals(tugevus)) {
                this.tugevus = tugevus;
                tugevusKontroll = true;
                break;
            }
        }
        if (!tugevusKontroll)
            throw new RuntimeException("Ebasobiv kaardi tugevus.");

        for (char m : mastid) {
            if (m == mast) {
                this.mast = m;
                mastiKontroll = true;
                break;
            }
        }
        if (!mastiKontroll)
            throw new RuntimeException("Ebasobiv kaardi mast.");
    }

    public Kaart(String kaart) {
        String ajutineTugevus = kaart.substring(0, 1);
        String ajutineMast = kaart.substring(1);
        if (kaart.length() == 3) {
            ajutineTugevus = kaart.substring(0, 2);
            ajutineMast = kaart.substring(2);
        }

        String[] tugevused = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] mastid = {"♣", "♦", "♠", "♥"};
        boolean tugevusKontroll = false;
        boolean mastiKontroll = false;

        for (String t : tugevused) {
            if (t.equals(ajutineTugevus)) {
                this.tugevus = ajutineTugevus;
                tugevusKontroll = true;
                break;
            }
        }
        if (!tugevusKontroll)
            throw new RuntimeException("Ebasobiv kaardi tugevus.");

        for (String m : mastid) {
            if (m.equals(ajutineMast)) {
                this.mast = ajutineMast.charAt(0);
                mastiKontroll = true;
                break;
            }
        }
        if (!mastiKontroll)
            throw new RuntimeException("Ebasobiv kaardi mast.");
    }

    public String getTugevus() {
        return tugevus;
    }

    public char getMast() {
        return mast;
    }

    public int getTugevusArv() {
        switch (tugevus) {
            case "2" -> {
                return 2;
            }
            case "3" -> {
                return 3;
            }
            case "4" -> {
                return 4;
            }
            case "5" -> {
                return 5;
            }
            case "6" -> {
                return 6;
            }
            case "7" -> {
                return 7;
            }
            case "8" -> {
                return 8;
            }
            case "9" -> {
                return 9;
            }
            case "10" -> {
                return 10;
            }
            case "J" -> {
                return 11;
            }
            case "Q" -> {
                return 12;
            }
            case "K" -> {
                return 13;
            }
            case "A" -> {
                return 14;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return tugevus + mast;
    }

    @Override
    public int compareTo(Kaart o) {
        if (this.getTugevusArv() > o.getTugevusArv())
            return 1;
        else if (this.getTugevusArv() < o.getTugevusArv())
            return -1;
        return 0;
    }
}
