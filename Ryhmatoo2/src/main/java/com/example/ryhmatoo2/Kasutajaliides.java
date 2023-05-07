package com.example.ryhmatoo2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.ryhmatoo2.Mäng.kumbVõidab;
import static com.example.ryhmatoo2.Mäng.väljastaKaardid;

public class Kasutajaliides extends Application { // -1 kui vasak ja 1 parem, 0 viik
    static String mängijaNimi;
    static int mängijaVõitudeArv = 0;
    static int arvutiVõitudeArv = 0;
    static int võitja;

    static List<Kaart> vasakKäsi = new ArrayList<>();
    static List<Kaart> paremKäsi = new ArrayList<>();
    static List<Kaart> ühiskaardid = new ArrayList<>();

    static HBox vasakKaardid=new HBox(10);
    static HBox paremKaardid=new HBox(10);

    static Label tulemusTekst = new Label("");
    static HBox skoorJaParimTulemus = new HBox();
    static HBox viisKaarti = new HBox(10);
    static HBox käed = new HBox(30);
    static HBox käeValikud = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox juur = new VBox(15);

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
        VBox juur =new VBox(20);

        HBox nupud = new HBox(10);

        uusSkoor();
        uuedKaardid();

        //ülemise ääre nupud
        Button juhendiNupp = new Button("JUHEND!");
        juhendiNupp.setOnMouseClicked(event -> näitaJuhendit());
        nupud.getChildren().addAll(juhendiNupp);

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

            valik(-1);
        });

        paremNupp.setOnMouseClicked(event -> {
            viisKaarti.getChildren().clear();
            viisKaarti.getChildren().clear();
            for (Kaart kaart : ühiskaardid)
                viisKaarti.getChildren().add(kaartJoonis(kaart));
            käeValikud.getChildren().clear();
            käeValikud.getChildren().add(mängiVeel);

            valik(1);
        });

        mängiVeel.setOnMouseClicked(event -> {
            uuedKaardid();
            käeValikud.getChildren().clear();
            käeValikud.getChildren().addAll(vasakNupp, paremNupp);
            tulemusTekst.setText("");
        });

        juur.getChildren().addAll(nupud, skoorJaParimTulemus, tulemusTekst, viisKaarti, käed, käeValikud);

        Scene scene = new Scene(juur);
        main.setScene(scene);
        main.show();
    }

    private void valik(int i) {
        if (i == -1 && võitja == -1) {
            tulemusTekst.setText("Võitsid!");
            mängijaVõitudeArv++;
        } else if (i == 1 && võitja == 1) {
            tulemusTekst.setText("Võitsid!");
            mängijaVõitudeArv++;
        } else if (võitja != 0) {
            tulemusTekst.setText("Kaotasid!");
            arvutiVõitudeArv++;
        } else {
            tulemusTekst.setText("Viik!");
        }
        uusSkoor();
    }

    private void uusSkoor() {
        skoorJaParimTulemus.getChildren().clear();
        Label tekst = new Label("Seis: Mängija " + mängijaVõitudeArv + " - " + arvutiVõitudeArv + " Arvuti");
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
        Rectangle bg=new Rectangle(50,100);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        bg.setFill(Color.RED);

        return new StackPane(bg);
    }

    public static StackPane kaartJoonis(Kaart kaart){
        Rectangle bg=new Rectangle(50,100);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        bg.setFill(Color.WHITE);

        Text tekst=new Text(kaart.getTugevus()+" "+kaart.getMast());
        if (kaart.getMast() == '♥' || kaart.getMast() == '♦')
            tekst.setFill(Color.RED);
        return new StackPane(bg,tekst);

    }

    public static void näitaJuhendit(){
        Stage juhendiAken=new Stage();

        String tekst="""
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

        TextArea juhend=new TextArea(tekst);
        juhend.setWrapText(true);

        VBox juur1 =new VBox();

        juur1.getChildren().add(juhend);

        Button nupp=new Button("Saan aru!");
        nupp.setOnMouseClicked(mouseEvent ->
                juhendiAken.close());

        juur1.getChildren().add(nupp);

        Scene scene=new Scene(juur1);
        juhendiAken.setScene(scene);
        juhendiAken.show();
    }

    /*public static void Mängi(){
        Stage mänguLava=new Stage();

        TilePane juur=new TilePane(10,10);
        juur.setPrefRows(2);
        juur.setPrefColumns(5);


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

       // HBox ühiskaardidJoonis = new HBox();


        HBox vasakKaardid=new HBox(10);
        for (Kaart kaart : vasakKäsi) {
            vasakKaardid.getChildren().add(kaartJoonis(kaart));
        }
        VBox vasakValik=new VBox(20);
        vasakValik.getChildren().add(vasakKaardid);

        Button vasakNupp=new Button("VASAK!");
        vasakValik.getChildren().add(vasakNupp);


        HBox paremKaardid=new HBox(10);
        for (Kaart kaart : paremKäsi) {
            paremKaardid.getChildren().add(kaartJoonis(kaart));
        }
        VBox paremValik=new VBox(20);
        paremValik.getChildren().add(paremKaardid);


        Button paremNupp=new Button("PAREM!");
        paremValik.getChildren().add(paremNupp);

        juur.getChildren().addAll(vasakValik,paremValik);




        SeisuKontroll vasak = new SeisuKontroll(vasakKäsi, ühiskaardid); //loodud isendil on väga palju kasulikke meetodeid, mida hakkame kasutama seisude võrdlemiseks
        SeisuKontroll parem = new SeisuKontroll(paremKäsi, ühiskaardid);

        vasakNupp.setOnMouseClicked(event -> {
            vasakValik(vasak,parem);
            for (Kaart kaart : ühiskaardid) {
                juur.getChildren().add(kaartJoonis(kaart));
            }
        });
        paremNupp.setOnMouseClicked(event ->{
            paremValik(vasak,parem);
            for (Kaart kaart : ühiskaardid) {
                juur.getChildren().add(kaartJoonis(kaart));
            }
        });


        Scene scene=new Scene(juur);
        mänguLava.setScene(scene);
        mänguLava.show();

    }

    public static void vasakValik(SeisuKontroll vasak, SeisuKontroll parem){
        Stage vasakLava=new Stage();
        Group juur=new Group();
        int võitija = kumbVõidab(vasak,parem);
        if(võitija==-1) {
            mängijaVõitudeArv++;
            juur.getChildren().add(new TextArea("Võitsid! seis on: \n Arvuti - "+arvutiVõitudeArv+ "\n Mängija - "+mängijaVõitudeArv));

        }
        if(võitija==1) {
            arvutiVõitudeArv++;
            juur.getChildren().add(new TextArea("Kaotasid! seis on: \n Arvuti - "+arvutiVõitudeArv+ "\n Mängija - "+mängijaVõitudeArv));
        }
        vasakLava.setScene(new Scene(juur));
        vasakLava.show();

    }

    public static void paremValik(SeisuKontroll vasak, SeisuKontroll parem){
        Stage paremLava=new Stage();
        Group juur=new Group();
        int võitija = kumbVõidab(vasak,parem);
        if(võitija==1) {
            mängijaVõitudeArv++;
            juur.getChildren().add(new TextArea("Võitsid! seis on: \n Arvuti - "+arvutiVõitudeArv+ "\n Mängija - "+mängijaVõitudeArv));

        }
        if(võitija==-1) {
            arvutiVõitudeArv++;
            juur.getChildren().add(new TextArea("Kaotasid! seis on: \n Arvuti - "+arvutiVõitudeArv+ "\n Mängija - "+mängijaVõitudeArv));
        }
        paremLava.setScene(new Scene(juur));
        paremLava.show();

    }*/
}
