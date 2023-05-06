package com.example.ryhmatoo2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mäng {
    public static void mäng() {
        int mängijaVõitudeArv = 0;
        int arvutiVõitudeArv = 0;

        while (true) {
            Pakk mängukaardid = new Pakk();
            mängukaardid.sega();

            //Kaartide jagamine
            List<Kaart> vasakKäsi = new ArrayList<>();
            List<Kaart> paremKäsi = new ArrayList<>();
            List<Kaart> ühiskaardid = new ArrayList<>();

            mängukaardid.võtaKaart();
            for (int i = 0; i < 2; i++) {
                vasakKäsi.add(mängukaardid.võtaKaart());
                paremKäsi.add(mängukaardid.võtaKaart());
            }

            mängukaardid.võtaKaart();
            for (int i = 0; i < 3; i++) {
                ühiskaardid.add(mängukaardid.võtaKaart());
            }
            for (int i = 0; i < 2; i++) {
                mängukaardid.võtaKaart();
                ühiskaardid.add(mängukaardid.võtaKaart());
            }

            System.out.println(väljastaKaardid(vasakKäsi) + " ja " + väljastaKaardid(paremKäsi));

            SeisuKontroll vasak = new SeisuKontroll(vasakKäsi, ühiskaardid); //loodud isendil on väga palju kasulikke meetodeid, mida hakkame kasutama seisude võrdlemiseks
            SeisuKontroll parem = new SeisuKontroll(paremKäsi, ühiskaardid);

            Scanner scan = new Scanner(System.in);
            System.out.println("Mis kaarte sa endale soovid?(vasak/parem)");
            String mängija = scan.nextLine();

            while (!mängija.equals("vasak") && !mängija.equals("parem")) {
                System.out.println("Vigane sisend.");
                Scanner scan1 = new Scanner(System.in);
                System.out.println("Mis kaarte sa endale soovid?(vasak/parem)");
                mängija = scan1.nextLine();
            }

            System.out.println();
            System.out.println("Ühiskaardid:");
            System.out.println(väljastaKaardid(ühiskaardid));
            System.out.println();

            System.out.println(SeisuKontroll.indeksSeisuks(vasak.tugevaimSeis()) + " ja " + SeisuKontroll.indeksSeisuks(parem.tugevaimSeis()));



            int võitja=kumbVõidab(vasak,parem);

            if ((võitja == -1 && mängija.equals("vasak")) || (võitja == 1 && mängija.equals("parem")))
                mängijaVõitudeArv++;
            else if (võitja != 0)
                arvutiVõitudeArv++;

            System.out.println();
            System.out.println("Mängu seis: mängija " + mängijaVõitudeArv + " - " + arvutiVõitudeArv + " arvuti");

            System.out.println();
            Scanner scan2 = new Scanner(System.in);
            System.out.println("Kas soovid mängimist jätkata?(jah/ei)");
            String jätka = scan2.nextLine();

            while (!jätka.equals("ei") && !jätka.equals("jah")) {
                System.out.println("Vigane sisend.");
                Scanner scan3 = new Scanner(System.in);
                System.out.println("Kas soovid mängimist jätkata?(jah/ei)");
                jätka = scan3.nextLine();
            }

            if (jätka.equals("ei"))
                break;
            System.out.println();
        }
    }

    public static int kumbVõidab(SeisuKontroll vasak, SeisuKontroll parem){

        if (parem.tugevaimSeis() < vasak.tugevaimSeis() && parem.tugevaimSeis() != -1) { // tugevama seisuga mängija võidab
            System.out.println("Võidab parem, kellel on " + SeisuKontroll.indeksSeisuks(parem.tugevaimSeis()));
            for (Kaart kaart : parem.tugevaimadViis()) {
                System.out.print(kaart + " ");
            }
            return 1;
        } else if (parem.tugevaimSeis() > vasak.tugevaimSeis() && vasak.tugevaimSeis() != -1) {
            System.out.println("Võidab vasak, kellel on " + SeisuKontroll.indeksSeisuks(vasak.tugevaimSeis()));
            for (Kaart kaart1 : vasak.tugevaimadViis()) {
                System.out.print(kaart1 + " ");
            }
            return -1;
        } else if (vasak.tugevaimSeis() == parem.tugevaimSeis()) { // kui on samad seisud, siis kontrollin kummal tugevam sama seis
            int seis = vasak.tugevaimSeis();
            List<Kaart> vasakKõigeTugevam = vasak.getSeisud().get(seis).get(vasak.getSeisud().get(seis).size() - 1); //kaartide list, mis moodustav kõige tugevama antud seisu
            List<Kaart> paremKõigeTugevam = parem.getSeisud().get(seis).get(parem.getSeisud().get(seis).size() - 1);
            if (seis == 2) {//võrdlen kahte maja, kumb tugevam
                List<List<Kaart>> vasakuMajaPaarJaKolmik = SeisuKontroll.majaPaarJaKolmik(vasakKõigeTugevam);
                List<List<Kaart>> paremaMajaPaarJaKolmik = SeisuKontroll.majaPaarJaKolmik(paremKõigeTugevam);
                int vasakuKolmikuTugevus = vasakuMajaPaarJaKolmik.get(0).get(0).getTugevusArv();
                int paremaKolmikuTugevus = paremaMajaPaarJaKolmik.get(0).get(0).getTugevusArv();
                int vasakuPaariTugevus = paremaMajaPaarJaKolmik.get(1).get(0).getTugevusArv();
                int paremaPaariTugevus = paremaMajaPaarJaKolmik.get(1).get(0).getTugevusArv();
                if (vasakuKolmikuTugevus > paremaKolmikuTugevus) {
                    System.out.println("Võitja vasak!, tugevam maja");
                    return -1;
                } else if (vasakuKolmikuTugevus < paremaKolmikuTugevus) {
                    System.out.println("Võitja parem!, tugevam maja");
                    return 1;
                } else if (vasakuPaariTugevus > paremaPaariTugevus) {
                    System.out.println("Võitja vasak!, tugevam maja");
                    return -1;
                } else if (vasakuPaariTugevus < paremaPaariTugevus) {
                    System.out.println("Võitja parem!, tugevam maja");
                    return 1;
                } else System.out.println("Viik!");
            } else if (seis == 3) {//võrdlen kahte masti, kummal tugevam
                if (vasakKõigeTugevam.get(vasakKõigeTugevam.size() - 1).getTugevusArv() > paremKõigeTugevam.get(paremKõigeTugevam.size() - 1).getTugevusArv()) {
                    System.out.println("Vasakul tugevam mast");
                    return -1;
                } else if (vasakKõigeTugevam.get(vasakKõigeTugevam.size() - 1).getTugevusArv() < paremKõigeTugevam.get(paremKõigeTugevam.size() - 1).getTugevusArv()) {
                    System.out.println("Paremal tugevam mast");
                    return 1;
                } else System.out.println("Viik");
            } else if (seis == 6) {// võrdlen kahte 2paari, kummal tugeval
                if (vasakKõigeTugevam.get(2).getTugevusArv() > paremKõigeTugevam.get(2).getTugevusArv()) {
                    System.out.println("Vasakul kõrgem paar tugevam");
                    return -1;
                } else if (vasakKõigeTugevam.get(2).getTugevusArv() < paremKõigeTugevam.get(2).getTugevusArv()) {
                    System.out.println("Paremal kõrgem paar tugevam");
                    return 1;
                } else if (vasakKõigeTugevam.get(0).getTugevusArv() > paremKõigeTugevam.get(0).getTugevusArv()) {
                    System.out.println("Vasakul üks paaridest tugevam");
                    return -1;
                } else if (vasakKõigeTugevam.get(0).getTugevusArv() < paremKõigeTugevam.get(0).getTugevusArv()) {
                    System.out.println("Paremal üks paaridest tugevam");
                    return 1;
                } else {
                    if (vasak.getSeisud().get(8).get(0).get(0).getTugevusArv() > parem.getSeisud().get(8).get(0).get(0).getTugevusArv()) {
                        System.out.println("Vasak võidab kõrgema kaardiga");
                        return -1;
                    } else if (vasak.getSeisud().get(8).get(0).get(0).getTugevusArv() < parem.getSeisud().get(8).get(0).get(0).getTugevusArv()) {
                        System.out.println("Parem võidab kõrgema kaardiga");
                        return 1;
                    } else System.out.println("Viik!");
                }
            } else {// ülejäänud seisude tugevust saab võrrelda kaartide tugevuste summana
                int vasakSumma = SeisuKontroll.tugevusedListist(vasakKõigeTugevam).stream().mapToInt(Integer::intValue).sum();
                int paremSumma = SeisuKontroll.tugevusedListist(paremKõigeTugevam).stream().mapToInt(Integer::intValue).sum();
                if (vasakSumma > paremSumma) {
                    System.out.println("Vasak võidab tänu kõrgematele kaartidele");
                    return -1;
                } else if (vasakSumma < paremSumma) {
                    System.out.println("Parem võidab tänu kõrgematele kaartidele");
                    return 1;

                } else if (seis == 4) {
                    System.out.println("Viik");

                } else if (vasak.getSeisud().get(8).get(0).get(0).getTugevusArv() > parem.getSeisud().get(8).get(0).get(0).getTugevusArv()) {
                    System.out.println("Vasak võidab kõrgema kaardiga");
                    return -1;
                } else if (vasak.getSeisud().get(8).get(0).get(0).getTugevusArv() < parem.getSeisud().get(8).get(0).get(0).getTugevusArv()) {
                    System.out.println("Parem võidav kõrgema kaardiga");
                    return 1;
                } else if (seis == 5 || seis == 7) {
                    if (vasak.getKäsi().get(0).getTugevusArv() > parem.getKäsi().get(0).getTugevusArv()) {
                        System.out.println("Vasak võidab kõrgema käega");
                        return -1;
                    } else if (vasak.getKäsi().get(0).getTugevusArv() < parem.getKäsi().get(0).getTugevusArv()) {
                        System.out.println("Parem võidab kõrgema käega");
                        return 1;
                    } else System.out.println("Viik");

                } else System.out.println("Viik");
            }
        }
        return 0;
    }

    public static String väljastaKaardid(List<Kaart> kaardid) {
        String väljund = "";
        for (int i = 0; i < kaardid.size() - 1; i++) {
            väljund += kaardid.get(i) + " ";
        }
        väljund += kaardid.get(kaardid.size() - 1);
        return väljund;
    }

    public static void main(String[] args) {
        mäng();
    }
}

