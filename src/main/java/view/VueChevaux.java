package view;

import controleur.Controleur;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.Cheval;
import modele.ChevalNotFoundException;

import java.util.Collection;

public class VueChevaux implements Vue, ControleurAware {

    private Stage stage;
    private Scene scene;
    private TableView<Cheval> chevalTableView;
    private Controleur controleur;


    private VueChevaux(Stage stage) {
        this.stage = stage;
        BorderPane borderPane = new BorderPane();
        this.chevalTableView = new TableView<Cheval>();
        borderPane.setCenter(this.chevalTableView);

        Label titre = new Label("Les chevaux");
        titre.setFont(Font.font(30));
        borderPane.setTop(titre);
        Button creerCheval = new Button("Ajouter un cheval");
        creerCheval.setOnAction(e -> controleur.creerCheval());
        borderPane.setBottom(creerCheval);
        BorderPane.setAlignment(creerCheval,Pos.CENTER);
        this.scene = new Scene(borderPane);
    }

    public static VueChevaux creer(Stage stage) {
        return new VueChevaux(stage);
    }

    /**
     * Initialisation des données -- récupération des données vers le contrôleur
     */

    public void initialisationDonnees() {
        Collection<Cheval> lesChevaux = this.controleur.getChevaux();
        this.chevalTableView.getItems().addAll(lesChevaux);
        this.preparerTableView();

    }


    /**
     * Préparation du contenu de la TableView
     */
    private void preparerTableView() {
        this.chevalTableView.setEditable(false);


        this.chevalTableView.setRowFactory(tv -> {
            TableRow<Cheval> row = new TableRow<Cheval>();
            row.setOnMouseClicked(mouseEvent -> { doForClick(mouseEvent, row); } );
            return row;
        });


        TableColumn<Cheval,Number> idCol = new TableColumn("Id Cheval");
        TableColumn<Cheval,String> nomChevalCol = new TableColumn("Nom du cheval");

        idCol.setCellValueFactory(
                new PropertyValueFactory<Cheval,Number>("idCheval")
        );
        nomChevalCol.setCellValueFactory(
                new PropertyValueFactory<Cheval,String>("nom")
        );

        this.chevalTableView.getColumns().addAll(idCol,nomChevalCol);
    }

    private void doForClick(MouseEvent e, TableRow<Cheval> chevalTableRow) {
        if (e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY && !chevalTableRow.isEmpty()) {
            try {
                controleur.afficherCheval(chevalTableRow.getItem().getIdCheval());
            } catch (ChevalNotFoundException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Le cheval sélectionné n'existe pas",ButtonType.OK);
                alert.showAndWait();
            }

        }
    }




    @Override
    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    @Override
    public void show() {
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void ajouterCheval(Cheval cheval) {
        this.chevalTableView.getItems().add(cheval);
    }
}
