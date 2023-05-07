package com.example.ryhmatoo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static com.example.ryhmatoo2.Mäng.kumbVõidab;

public class Kasutajaliides extends Application { // -1 kui vasak ja 1 parem, 0 viik
    static String mängijaNimi;
    static int mängijaVõitudeArv = 0;
    static int arvutiVõitudeArv = 0;
    static int võitja;
    static List<Kaart> vasakKäsi = new ArrayList<>();
    static List<Kaart> paremKäsi = new ArrayList<>();
    static List<Kaart> ühiskaardid = new ArrayList<>();

    static HBox vasakKaardid = new HBox(10);
    static HBox paremKaardid = new HBox(10);
    static String tühjadRead = "\n\n";

    static Text seisuTekst = new Text(tühjadRead);
    static Text tulemusTekst = new Text("");

    static HBox skoorJaParimTulemus = new HBox();
    static HBox viisKaarti = new HBox(10);
    static HBox käed = new HBox(30);
    static HBox käeValikud = new HBox(10);
    static Text edetabel=new Text();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox juur = new VBox(10);

        Label tekst = new Label("Sisesta nimi: ");
        TextField sisend = new TextField();
        Button kinnita = new Button("KINNITA");


        juur.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                mängijaNimi = sisend.getText();
                primaryStage.close();
                mäng();
            }
        });

        kinnita.setOnMouseClicked(event -> {
            mängijaNimi = sisend.getText();
            primaryStage.close();
            mäng();
        });

        juur.getChildren().addAll(tekst, sisend, kinnita);

        Scene scene = new Scene(juur);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mäng() {
        Stage main = new Stage();

        seisuTekst.setFont(Font.font("Gotham", 14));
        //tulemusTekst.setStyle("-fx-font-weight: bold");
        Effect fx = new Glow();
        seisuTekst.setEffect(fx);

        Button edetabeliNupp=new Button("EDETABEL!");
        edetabeliNupp.setOnMouseClicked(event -> {
            try {
                kuvaEdetabel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        GridPane juur = new GridPane();
        juur.setMinSize(400, 400);
        juur.setPadding(new Insets(10, 5, 10, 5));
        juur.setHgap(10);
        juur.setVgap(10);
        juur.setAlignment(Pos.CENTER);

        HBox nupud = new HBox(10);

        uusSkoor();
        uuedKaardid();

        //ülemise ääre nupud
        Button juhendiNupp = new Button("JUHEND!");
        juhendiNupp.setOnMouseClicked(event -> näitaJuhendit());
        nupud.getChildren().addAll(juhendiNupp,edetabeliNupp);

        //alumises ääres oloevad nupud
        Button vasakNupp = new Button("VASAK!");
        Button paremNupp = new Button("PAREM!");
        Button mängiVeel = new Button("MÄNGI VEEL");

        käeValikud.getChildren().addAll(vasakNupp, paremNupp);

        vasakNupp.setOnMouseClicked(event -> {
            viisKaarti.getChildren().clear();
            viisKaarti.getChildren().clear();
            for (Kaart kaart : ühiskaardid)
                viisKaarti.getChildren().add(kaartJoonis(kaart));
            käeValikud.getChildren().clear();
            käeValikud.getChildren().add(mängiVeel);

            SeisuKontroll vasak = new SeisuKontroll(vasakKäsi, ühiskaardid);
            SeisuKontroll parem = new SeisuKontroll(paremKäsi, ühiskaardid);
            seisuTekst.setText("Valisid vasakpoolse käe\nSul on: " + SeisuKontroll.indeksSeisuks(vasak.tugevaimSeis()) +
                    "\nArvutil on: " + SeisuKontroll.indeksSeisuks(parem.tugevaimSeis()));

            valik(-1);


        });

        paremNupp.setOnMouseClicked(event -> {
            viisKaarti.getChildren().clear();
            viisKaarti.getChildren().clear();
            for (Kaart kaart : ühiskaardid)
                viisKaarti.getChildren().add(kaartJoonis(kaart));
            käeValikud.getChildren().clear();
            käeValikud.getChildren().add(mängiVeel);

            SeisuKontroll vasak = new SeisuKontroll(vasakKäsi, ühiskaardid);
            SeisuKontroll parem = new SeisuKontroll(paremKäsi, ühiskaardid);
            seisuTekst.setText("Valisid parempoolse käe\nSul on: " + SeisuKontroll.indeksSeisuks(parem.tugevaimSeis()) +
                    "\nArvutil on: " + SeisuKontroll.indeksSeisuks(vasak.tugevaimSeis()));

            valik(1);
        });

        mängiVeel.setOnMouseClicked(event -> {
            uuedKaardid();
            käeValikud.getChildren().clear();
            tulemusTekst.setText(" ");
            käeValikud.getChildren().addAll(vasakNupp, paremNupp);
            seisuTekst.setText(tühjadRead);
        });

        //juur.getChildren().addAll(nupud, skoorJaParimTulemus, tulemusTekst, viisKaarti, käed, käeValikud);

        juur.add(nupud, 0, 0);
        juur.add(skoorJaParimTulemus, 0, 1);
        juur.add(seisuTekst, 0, 2);
        juur.add(tulemusTekst, 0, 3);
        juur.add(viisKaarti, 0, 4);
        juur.add(käed, 0, 5);
        juur.add(käeValikud, 0, 7);
        juur.add(new Text(""), 0, 7);

        juur.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(juur, 400, 450);

        main.setMinHeight(500);
        main.setMinWidth(400);
        main.setScene(scene);

        main.show();

        main.setOnCloseRequest(event -> {
            try {
                kirjutaTulemus();
            } catch (IOException e) {
                main.close();
            }
        });
    }

    private void valik(int i) {
        Effect fx = new Bloom(10);
        tulemusTekst.setEffect(fx);
        tulemusTekst.setFont(Font.font("Gotham", FontWeight.BOLD, 20));
        if (i == -1 && võitja == -1) {
            tulemusTekst.setText("VÕITSID!");
            mängijaVõitudeArv++;
        } else if (i == 1 && võitja == 1) {
            tulemusTekst.setText("VÕITSID!");
            mängijaVõitudeArv++;
        } else if (võitja != 0) {
            tulemusTekst.setText("KAOTASID!");
            arvutiVõitudeArv++;
        } else {
            tulemusTekst.setText("VIIK!");
        }
        uusSkoor();
    }

    private void uusSkoor() {
        skoorJaParimTulemus.getChildren().clear();
        Text tekst = new Text("Seis: Mängija " + mängijaVõitudeArv + " - " + arvutiVõitudeArv + " Arvuti");
        tekst.setFont(Font.font("Gotham", 20));
        tekst.setStyle("-fx-font-weight: bold");
        skoorJaParimTulemus.getChildren().add(tekst);
    }

    private void uuedKaardid() {
        viisKaarti.getChildren().clear();
        käed.getChildren().clear();
        vasakKaardid.getChildren().clear();
        paremKaardid.getChildren().clear();

        vasakKäsi = new ArrayList<>();
        paremKäsi = new ArrayList<>();
        ühiskaardid = new ArrayList<>();

        //käed
        Pakk mängukaardid = new Pakk();
        mängukaardid.sega();

        //Kaartide jagamine
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

        //leitakse võitja
        SeisuKontroll vasak = new SeisuKontroll(vasakKäsi, ühiskaardid);
        SeisuKontroll parem = new SeisuKontroll(paremKäsi, ühiskaardid);
        võitja = kumbVõidab(vasak, parem);

        // kkartide joonistamine
        for (Kaart kaart : vasakKäsi) {
            vasakKaardid.getChildren().add(kaartJoonis(kaart));
        }

        for (Kaart kaart : paremKäsi) {
            paremKaardid.getChildren().add(kaartJoonis(kaart));
        }

        käed.getChildren().addAll(vasakKaardid, paremKaardid);

        for (int i = 0; i < 5; i++) {
            viisKaarti.getChildren().add(kaartKinni());
        }
    }

    public static StackPane kaartKinni() {
        Rectangle bg = new Rectangle(50, 100);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        bg.setFill(Color.BLACK);
        Effect fx = new DropShadow(5, Color.BLACK);
        bg.setEffect(fx);

        return new StackPane(bg);
    }

    public static StackPane kaartJoonis(Kaart kaart) {
        Rectangle bg = new Rectangle(50, 100);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        bg.setFill(Color.LIGHTGRAY);

        Effect fx = new DropShadow(5, Color.BLACK);
        bg.setEffect(fx);

        Text tekst = new Text(kaart.getTugevus() + " " + kaart.getMast());
        if (kaart.getMast() == '♥' || kaart.getMast() == '♦')
            tekst.setFill(Color.RED);
        return new StackPane(bg, tekst);

    }

    public static void näitaJuhendit() {
        Stage juhendiAken = new Stage();

        String tekst = """
                Mängu eesmärgiks on valida kahe juhusliku pokkerikäe vahel. Pärast valiiku tegemist väljastatakse 5 ühiskaarti ja öeldakse kumb kätest võitis. Mängija võidu korral suurendatakse mängija punkte ühe võrra. Vastasel juhul suurenevad arvuti punktid. Kui käed olid sama tugevad, siis punktid jäävad samaks. Kaartide tugevuste arvestamisel vaadatakse maksimaalselt 5 kaarti korraga.
                            
                Pokkerikäte tugevusjärjestus (alates tugevaimast):
                            
                Mastirida - 5 järjestikust kaarti, mis on samast mastist
                Nelik - 4 sama tugevusega kaarti
                Maja - kolmik ja paar
                Mast - 5 kaarti samast mastist
                Rida - 5 järjestikust kaarti
                Kolmik - 3 sama tugevusega kaarti
                Kaks paari
                Üks paar
                Kõrge kaart - Kõrgeim käesolev kaart.
                            
                \s""";

        Label juhend = new Label(tekst);
        juhend.setWrapText(true);

        VBox juur1 = new VBox();
        juur1.setPadding(new Insets(10, 10, 10, 10));

        juur1.getChildren().add(juhend);


        Scene scene = new Scene(juur1, 300, 400);
        juhendiAken.setScene(scene);
        juhendiAken.setMinWidth(350);
        juhendiAken.setMinHeight(450);
        juhendiAken.show();
    }

    static void kirjutaTulemus() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("pokkeri_tulemused.txt",true))) {
            pw.println(mängijaNimi + ";" + mängijaVõitudeArv + ";" + arvutiVõitudeArv);

        }
    }

    static List<MängijaJaTulemus> Edetabel() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("pokkeri_tulemused.txt"))) {
            List<MängijaJaTulemus> stats=new ArrayList<>();
            String rida = br.readLine();
            while (rida != null) {
                boolean nimiOlemas=false;
                String[] tükid = rida.split(";");
                String nimi=tükid[0];
                int vahe=Integer.parseInt(tükid[1])-Integer.parseInt(tükid[2]);
                for (int i = 0; i < stats.size(); i++) {
                    if(stats.get(i).getNimi().equals(nimi)) {
                        nimiOlemas=true;
                        stats.get(i).setVahe(stats.get(i).getVahe()+vahe);
                    }
                }
                if(!nimiOlemas){
                    stats.add(new MängijaJaTulemus(nimi,vahe));
                }
                rida = br.readLine();
            }
            Collections.sort(stats);
            return stats;
        }
    }
    static void kuvaEdetabel() throws IOException {
        Stage lava = new Stage();

        StringBuilder sb=new StringBuilder();
        sb.append("EDETABEL!\n\n");
        List<MängijaJaTulemus> mangijad=Edetabel();
        int mangijateArv=mangijad.size();

        for (int i = 0; i < Math.min(10,mangijateArv); i++) {
            sb.append(i+1+". "+mangijad.get(i)+"\n");
        }



        Label juhend = new Label(sb.toString());
        juhend.setWrapText(true);

        VBox juur1 = new VBox();
        juur1.setPadding(new Insets(10, 10, 10, 10));

        juur1.getChildren().add(juhend);


        Scene scene = new Scene(juur1, 300, 400);
        lava.setScene(scene);
        lava.setMinWidth(350);
        lava.setMinHeight(450);
        lava.show();

    }
}
