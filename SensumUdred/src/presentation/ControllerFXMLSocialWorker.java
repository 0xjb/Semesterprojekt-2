/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import acq.IBusiness;
import acq.IPresentation;
import acq.ICase;
import acq.ICitizen;
import acq.IInquiry;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class ControllerFXMLSocialWorker implements Initializable, IPresentation {

    private PresentationFacade pf;
    private IBusiness ib;
    private AlertBox ab;

    @FXML
    private Label loginInfoLabelSW;
    @FXML
    private ListView<ICase> caseListView;
    @FXML
    private Tab casesTab;
    @FXML
    private Tab inquiriesTab;
    @FXML
    private Button logoutButtonSW;
    @FXML
    private Button createCaseButton;
    @FXML
    private Button editCaseButton;
    @FXML
    private Button createCitizenButton;
    @FXML
    private Button editCitizenButton;
    @FXML
    private Button deleteCitizenButton;
    @FXML
    private Button updateCitizenListButton;
    @FXML
    private Button deleteCaseButton;
    @FXML
    private Button updateCaseListButton;
    @FXML
    private ListView<ICitizen> citizenListView;
    @FXML
    private Label caseLabel;
    @FXML
    private Button editInquiryButton;
    @FXML
    private ListView<IInquiry> inquiriesListView;
    @FXML
    private Button deleteInquiryButton;
    @FXML
    private Button updateInquiryListView;
    @FXML
    private Button createInquiryButton;
    @FXML
    private ListView<IInquiry> inquiryListView1;
    @FXML
    private Label inquiryLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ab = new AlertBox();
//        ib = PresentationFacade.getIData().getIBusiness();
//        loginInfoLabelSW.setText("Logged in as: " + ib.getActiveUser().getName());
//        System.out.println("Logged in as: " + ib.getActiveUser().getName());
//        ib.getCitizen();
//        updateCaseList();
//        updateCitizenList();
//        updateInquiryList();
    }

    public void updateCaseList() {
        if (ib.getCases() == null) {
            caseLabel.setText("No Useres Installed");
        } else {
            caseListView.setItems(ib.getCases());
        }
    }

    @FXML
    private void createCaseAction(ActionEvent event) {

        updateCaseList();
        if (citizenListView.getSelectionModel().getSelectedItem() != null) {
            ab.displayCaseCreation("Create case", ib, (ICitizen) citizenListView.getSelectionModel().getSelectedItem());
        } else {
            caseLabel.setText("you must select a Citizen from the list to create a case");
        }
        updateCaseList();
    }

    @FXML
    private void DeleteCaseAction(ActionEvent event) {
        ib.deleteCase(caseListView.getSelectionModel().getSelectedItem());
        updateCaseList();
    }

    @Override
    public void injectBusiness(IBusiness businessFacade) {
        ib = businessFacade;
    }

    @Override
    public void openUI() {
        loginInfoLabelSW.setText("Logged in as: " + ib.getActiveUser().getName());
        System.out.println("Logged in as: " + ib.getActiveUser().getName());
        ib.getCitizen();
        updateCaseList();
        updateCitizenList();
        updateInquiryList();
    }

    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        ib.logOutActiveUser();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLlogin.fxml"));

        GridPane gridPane = loader.load();
        IPresentation controller = loader.getController();
        controller.injectBusiness(ib);

        Scene scene2 = new Scene(gridPane);
        //Get Stage information

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.setWidth(400);
        window.setHeight(300);
        window.setScene(scene2);
        window.show();
    }

    @FXML
    private void createCitizenAction(ActionEvent event) {
        ab.displayCitizenCreation("Create citizen", ib);
        updateCitizenList();
    }

    @FXML
    private void deleteCitizenAction(ActionEvent event) {
        if (citizenListView.getSelectionModel().getSelectedItem() != null) {
            ib.deleteCitizen(citizenListView.getSelectionModel().getSelectedItem());
            updateCitizenList();
            updateCaseList();
            updateInquiryList();
        }
    }

    @FXML
    private void updateCitizenListAction(ActionEvent event) {
        updateCitizenList();
    }

    @FXML
    private void updateCaseListAction(ActionEvent event) {
        updateCaseList();
    }

    public void updateCitizenList() {
        if (ib.getCitizen() == null) {
            //indsæt ting på label
        } else {
            ObservableList<ICitizen> list = ib.getCitizen();
            citizenListView.setItems(list);
            updateInquiryList();
        }
    }

    public void updateInquiryList() {
        inquiriesListView.setItems(ib.getInquiries());
        inquiryListView1.setItems(ib.getInquiries());
    }

    @FXML
    private void deleteInquiryAction(ActionEvent event) {
        if (inquiriesListView.getSelectionModel().getSelectedItem() != null) {
            ib.deleteInquiry(inquiriesListView.getSelectionModel().getSelectedItem());
            updateInquiryList();
        }
    }

    @FXML
    private void updateInquiryListAction(ActionEvent event) {
        updateInquiryList();

    }

    @FXML
    private void createInquiryAction(ActionEvent event) {
        if (citizenListView.getSelectionModel().getSelectedItem() != null) {
            ab.displayInquiryCreation("Inquiry", ib, citizenListView.getSelectionModel().getSelectedItem());
            updateInquiryList();
        }
    }

    @FXML
    private void editInquiryAction(ActionEvent event) {
        if (inquiriesListView.getSelectionModel().getSelectedItem() != null) {
            ab.displayInquiryEdit("Inquiry", ib, inquiriesListView.getSelectionModel().getSelectedItem());
        } else {
            inquiryLabel.setText("you must select a Inquiry from the list to view details");
            System.out.println("lol");
        }
        updateCitizenList();

    }

    @FXML
    private void editCitizenAction(ActionEvent event) {
        if (citizenListView.getSelectionModel().getSelectedItem() != null) {
            ab.displayCitizenEdit("Citizen", ib, citizenListView.getSelectionModel().getSelectedItem());
        } else {
            caseLabel.setText("you must select a Citizen from the list to view details");
        }
        updateCaseList();
        updateCitizenList();
    }

    @FXML
    private void editCaseAction(ActionEvent event) {
        if (caseListView.getSelectionModel().getSelectedItem() != null) {
            ab.displayCaseEdit("Citizen", ib, caseListView.getSelectionModel().getSelectedItem());
        } else {
            caseLabel.setText("you must select a Case from the list to view details");
        }
        updateCaseList();
    }

}
