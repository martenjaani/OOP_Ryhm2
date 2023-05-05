package com.example.ryhmatoo2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Kasutajaliides extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane juur=new BorderPane();

        Button juhendiNupp=new Button("JUHEND!");

        juhendiNupp.setOnMouseClicked(event -> {

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
        });

        HBox ühiskaardid =new HBox();
        ühiskaardid.getChildren().add(kaartJoonis(new Kaart("5",'♣')));

        juur.setCenter(ühiskaardid);
        juur.setTop(juhendiNupp);

        Scene scene=new Scene(juur);
        scene.setFill(Color.ORANGE);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static StackPane kaartJoonis(Kaart kaart){
        Rectangle bg=new Rectangle(50,100);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        bg.setFill(Color.WHITE);

        Text tekst=new Text(kaart.getTugevus()+" "+kaart.getMast());
        return new StackPane(bg,tekst);

    }
}
