package com.example.ryhmatoo2;

import java.util.*;

/*
Listis 'Seisud' vastab igale indeksile mingi list, kus on listid kaardidest, mis moodustavad vastava seisu
0 straight flush
1 nelik
2 maja-
3 mastid-
4 rida
5 kolmik
6 2 paari
7 paar
8 korge
 */
public class SeisuKontroll {
    private List<Kaart> käsi;
    private List<Kaart> ühiskaardid;
    private List<Kaart> kõikKaardid = new ArrayList<>();
    private List<List<List<Kaart>>> seisud; //kõikvõimalikud seisud salvestame siia mitmemõõtmelisse list, mille igale indeksile (vt kõige ülemine comment) vastab sellele seisule vastavad kaardikombinatisoonid

    public SeisuKontroll(List<Kaart> käsi, List<Kaart> ühiskaardid) {
        this.käsi = käsi;
        Collections.sort(this.käsi);// kaardid sorteeritakse madalamast kõrgemale vastavalt tugevusele, teeb töö palju lihtsamaks
        this.ühiskaardid = ühiskaardid;
        kõikKaardid.addAll(käsi);
        kõikKaardid.addAll(ühiskaardid);
        Collections.sort(kõikKaardid);// kaardid sorteeritakse madalamast kõrgemale vastavalt tugevusele, teeb töö palju lihtsamaks
        seisud = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            seisud.add(i, new ArrayList<>());// loon tühjad listid kõigepealt
        }
        kõikVõimalused(); //konstruktor kutsub kohe välja meetodi, mis lisab listi 'seisud' kõik võimalikud seisud
    }

    public void kõikVõimalused() {
        int[][] võimalused = { //kõik kombinatsioonid 7st 5 kaupa (seisu muudustab 7st kaardist (ühiskaardid ja käsi) maksimaalselt viis)
                {0, 1, 2, 3, 4},
                {0, 1, 2, 3, 5},
                {0, 1, 2, 3, 6},
                {0, 1, 2, 4, 5},
                {0, 1, 2, 4, 6},
                {0, 1, 2, 5, 6},
                {0, 1, 3, 4, 5},
                {0, 1, 3, 4, 6},
                {0, 1, 3, 5, 6},
                {0, 1, 4, 5, 6},
                {0, 2, 3, 4, 5},
                {0, 2, 3, 4, 6},
                {0, 2, 3, 5, 6},
                {0, 2, 4, 5, 6},
                {0, 3, 4, 5, 6},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 6},
                {1, 2, 3, 5, 6},
                {1, 2, 4, 5, 6},
                {1, 3, 4, 5, 6},
                {2, 3, 4, 5, 6}
        };

        for (int[] ints : võimalused) {
            List<Kaart> kontrollitavadKaardid = new ArrayList<>();
            kontrollitavadKaardid.add(kõikKaardid.get(ints[0]));
            kontrollitavadKaardid.add(kõikKaardid.get(ints[1]));
            kontrollitavadKaardid.add(kõikKaardid.get(ints[2]));
            kontrollitavadKaardid.add(kõikKaardid.get(ints[3]));
            kontrollitavadKaardid.add(kõikKaardid.get(ints[4]));

            paar(kontrollitavadKaardid); //kontrollin, kas antud viie kaardi kombinatsioonis leidub paare, meetod salvestab paarid listi 'seisus'
            kolmik(kontrollitavadKaardid);// sama aga kolmik
            rida(kontrollitavadKaardid); // jne
            mast(kontrollitavadKaardid);
            maja(kontrollitavadKaardid);
            nelik(kontrollitavadKaardid);
            mastiRida(kontrollitavadKaardid);
        }
        kõrge(); // lihtsalt suurim kaart listis 'käsi'
        kaksPaari(); // 2 suurimat leitud paari
    }


    public void nelik(List<Kaart> viisKaarti) { // meetod proovib otsida neliku ja lisab selle listi 'seisud' vastavale indeksile
        for (int i = 0; i < 2; i++) {
            int mituKordaEsineb = 1;
            for (int j = i + 1; j < viisKaarti.size(); j++) {
                if (viisKaarti.get(i).getTugevusArv() == viisKaarti.get(j).getTugevusArv()) {
                    mituKordaEsineb++;
                }
                if (mituKordaEsineb == 4 && !tugevused(seisud.get(1)).contains(viisKaarti.get(i).getTugevusArv())) {
                    seisud.get(1).add(viisKaarti.subList(i, i + 4));
                    return;


                }

            }
        }

    }

    public void kolmik(List<Kaart> viisKaarti) { // meetod proovib otsida kolmiku ja lisab selle listi 'seisud' vastavale indeksile
        for (int i = 0; i < 3; i++) {
            //mitu korda esineb
            int mituKordaEsineb = 1;
            for (int j = i + 1; j < viisKaarti.size(); j++) {
                if (viisKaarti.get(i).getTugevusArv() == viisKaarti.get(j).getTugevusArv()) {
                    mituKordaEsineb++;
                }
                if (mituKordaEsineb == 3 && !tugevused(seisud.get(5)).contains(viisKaarti.get(i).getTugevusArv())) {
                    seisud.get(5).add(viisKaarti.subList(i, i + 3));
                    return;
                }

            }
        }

    }

    public static List<Kaart> tagastaKolmik(List<Kaart> viisKaarti) { // meetod on vajalik seisu 'maja' jaoks
        for (int i = 0; i < 3; i++) {
            //mitu korda esineb
            int mituKordaEsineb = 1;
            for (int j = i + 1; j < viisKaarti.size(); j++) {
                if (viisKaarti.get(i).getTugevusArv() == viisKaarti.get(j).getTugevusArv()) {
                    mituKordaEsineb++;
                }
                if (mituKordaEsineb == 3) {
                    return viisKaarti.subList(i, i + 3);
                }

            }

        }
        return Collections.emptyList();

    }


    public void paar(List<Kaart> viisKaarti) { //meetod proovib otsida paare ja lisab need listi 'seisud' vastavale indeksile
        int paare = 0;
        for (int i = 0; i < 4; i++) {
            int mituKordaEsineb = 1;
            for (int j = i + 1; j < viisKaarti.size(); j++) {
                if (viisKaarti.get(i).getTugevusArv() == viisKaarti.get(j).getTugevusArv()) {
                    mituKordaEsineb++;
                }
                if (mituKordaEsineb == 2 && paare != 2 && !tugevused(seisud.get(7)).contains(viisKaarti.get(i).getTugevusArv())) {
                    seisud.get(7).add(viisKaarti.subList(i, i + 2));
                    mituKordaEsineb = 0;
                    paare++;

                }

            }
        }

    }

    public List<Kaart> getKäsi() {
        return käsi;
    }

    public void kaksPaari() { //meetod võtab kaks suurimat paari ja lisab selle listi 'seisud' vastavale indeksile
        List<List<Kaart>> paarid = seisud.get(7);
        List<Kaart> kaardid = new ArrayList<>();
        if (paarid.size() > 1) {
            for (int i = paarid.size() - 1; i >= 0; i--) {
                kaardid.addAll(paarid.get(i));

            }
            Collections.sort(kaardid);
            seisud.get(6).add(kaardid.subList(kaardid.size()-4, kaardid.size()));


        }


    }


    public static List<Integer> tugevused(List<List<Kaart>> needSeisud) { // tagastab kaardi tugevused täisarvu listina
        List<Integer> tugevustList = new ArrayList<>();
        for (int i = 0; i < needSeisud.size(); i++) {
            for (int j = 0; j < needSeisud.get(i).size(); j++) {
                tugevustList.add(needSeisud.get(i).get(j).getTugevusArv());

            }

        }
        return tugevustList;
    }

    public static List<Integer> tugevusedListist(List<Kaart> needSeisud) { // sarnane meetod eelmisega
        List<Integer> tugevustList = new ArrayList<>();
        for (int i = 0; i < needSeisud.size(); i++) {
            tugevustList.add(needSeisud.get(i).getTugevusArv());
        }
        return tugevustList;
    }

    public void maja(List<Kaart> viisKaarti) { //meetod proovib otsida maja ja lisab selle listi 'seisud' vastavale indeksile
        List<Kaart> kolmik = tagastaKolmik(viisKaarti);
        if (!kolmik.isEmpty()) {
            List<Kaart> koopia = new ArrayList<>(viisKaarti);
            koopia.removeAll(kolmik);
            if (koopia.get(0).getTugevusArv() == koopia.get(1).getTugevusArv() && kolmik.get(0).getTugevusArv() != koopia.get(0).getTugevusArv()) {
                seisud.get(2).add(viisKaarti);

            }


        }
    }

    public static List<List<Kaart>> majaPaarJaKolmik(List<Kaart> viisKaarti) { //meetod tagastab maja moodustava paari ja kolmiku eraldi listidena
        List<List<Kaart>> kaardid = new ArrayList<>();
        List<Kaart> kolmik = tagastaKolmik(viisKaarti);
        kaardid.add(kolmik);
        viisKaarti.removeAll(kolmik);
        kaardid.add(viisKaarti);
        return kaardid;
    }

    public void rida(List<Kaart> viisKaarti) { //meetod proovib otsid rea ja lisab selle listi 'seisud' vastavale indeksile
        int mituKorda = 0;
        List<List<Kaart>> kaardid = Arrays.asList(viisKaarti);

        for (int i = 0; i < viisKaarti.size() - 1; i++) {
            if (viisKaarti.get(i).getTugevusArv() + 1 == viisKaarti.get(i + 1).getTugevusArv())
                mituKorda++;
        }
        if (viisKaarti.get(viisKaarti.size() - 2).getTugevusArv() + 1 == viisKaarti.get(viisKaarti.size() - 1).getTugevusArv())
            mituKorda++;

        if (mituKorda == 5 && !new HashSet<>(tugevused(seisud.get(4))).containsAll(tugevused(kaardid)))
            seisud.get(4).add(viisKaarti);
    }

    public void mast(List<Kaart> viisKaarti) { //meetod proovib otsida masti ja lisab selle listi 'seisud' vastavale indeksile
        char mast = viisKaarti.get(0).getMast();
        for (int i = 1; i < viisKaarti.size(); i++) {
            if (viisKaarti.get(i).getMast() != mast) {
                return;
            } else if (i == 4) {
                seisud.get(3).addAll(Arrays.asList(viisKaarti));
            }
        }

    }

    public boolean kasMast(List<Kaart> viisKaarti) { //meetod tagastab tõeväärtuse olenevalt sellest, kas viis kaarti on samast mastist
        char mast = viisKaarti.get(0).getMast();
        for (int i = 1; i < viisKaarti.size(); i++) {
            if (viisKaarti.get(i).getMast() != mast) {
                return false;
            }

        }
        return true;

    }

    public boolean kasRida(List<Kaart> viisKaarti) { //meetod tagastab tõeväärtuse olenevalt sellest, kas viis kaarti moodustavad rea
        int mituKorda = 0;

        for (int i = 0; i < viisKaarti.size() - 1; i++) {
            if (viisKaarti.get(i).getTugevusArv() + 1 == viisKaarti.get(i + 1).getTugevusArv())
                mituKorda++;
        }
        if (viisKaarti.get(viisKaarti.size() - 2).getTugevusArv() + 1 == viisKaarti.get(viisKaarti.size() - 1).getTugevusArv())
            mituKorda++;

        if (mituKorda == 5) {
            return true;
        }
        return false;
    }


    public void mastiRida(List<Kaart> viisKaarti) { //meetod proovib otsida mastirida ja lisav selle listi 'seisud' vastavale indeksile
        if (kasRida(viisKaarti) && kasMast(viisKaarti)) {
            seisud.get(0).addAll(Arrays.asList(viisKaarti));
        }
    }

    public void kõrge() { //lisab käe kõrgeima kaardi listi 'seisud' vastavale indeksile
        seisud.get(8).add(käsi.subList(1, 2));
    }


    public List<List<List<Kaart>>> getSeisud() {
        return seisud;
    }

    public List<Kaart> getKõikKaardid() {
        return kõikKaardid;
    }

    public int tugevaimSeis() {// kuna tugevaim seis asub madalamail mittetühjal indeksil, siis meetod on lihtne
        for (int i = 0; i < seisud.size(); i++) {
            if (seisud.get(i).size() != 0)
                return i;
        }
        return -1;
    }

    public String väljastaSeis() { // meetod oli oluline debuggimisel, enam pole vajalik
        String väljasta = "";
        for (int i = 0; i < seisud.size(); i++) {
            String rida = "";
            rida += indeksSeisuks(i) + " - ";
            for (int j = 0; j < seisud.get(i).size(); j++) {
                rida += "{";
                for (int k = 0; k < seisud.get(i).get(j).size(); k++) {
                    rida += seisud.get(i).get(j).get(k);
                }
                rida += "} ";
            }
            rida += "\n";
            väljasta += rida;
        }
        return väljasta;
    }

    public List<Kaart> tugevaimadViis() { // tugevaimale seisule vastavad viis kaarti
        if (tugevaimSeis() == -1)
            return null;
        int suurimSumma = 0;
        int suurimIndeks = 0;
        if (tugevaimSeis() != 2) {
            for (int i = 0; i < seisud.get(tugevaimSeis()).size(); i++) {
                int ajutineSumma = 0;
                for (int j = 0; j < seisud.get(tugevaimSeis()).get(i).size(); j++) {
                    ajutineSumma += seisud.get(tugevaimSeis()).get(i).get(j).getTugevusArv();
                }
                if (ajutineSumma > suurimSumma) {
                    suurimSumma = ajutineSumma;
                    suurimIndeks = i;
                }
            }
        }
        return seisud.get(tugevaimSeis()).get(suurimIndeks);
    }

    public static String indeksSeisuks(int i) {
        switch (i) {
            case (0):
                return "Mastirida";
            case (1):
                return "Nelik";
            case (2):
                return "Maja";
            case (3):
                return "Mast";
            case (4):
                return "Rida";
            case (5):
                return "Kolmik";
            case (6):
                return "Kaks paari";
            case (7):
                return "Paar";
            case (8):
                return "Kõrge";
            default:
                return null;
        }
    }
}
