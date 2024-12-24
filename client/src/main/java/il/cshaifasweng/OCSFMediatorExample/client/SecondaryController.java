package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CurrentStatusB;
import il.cshaifasweng.OCSFMediatorExample.entities.update;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondaryController {

    @FXML
    private Button btn00;

    @FXML
    private Button btn01;

    @FXML
    private Button btn02;

    @FXML
    private Button btn10;

    @FXML
    private Button btn11;

    @FXML
    private Button btn12;

    @FXML
    private Button btn20;

    @FXML
    private Button btn21;

    @FXML
    private Button btn22;

    @FXML
    private Label statusLbl;

    private CurrentStatusB currentStatusB;

    @FXML
    void onGridBtnClick(ActionEvent event) {
        if (((Button) event.getSource()).getText().trim() == "") {
            int row = 0, col = 0;
            if (((Button) event.getSource()) == btn00) {
                row = 0;
                col = 0;
            } else if (((Button) event.getSource()) == btn01) {
                row = 1;
                col = 0;
            } else if (((Button) event.getSource()) == btn02) {
                row = 2;
                col = 0;
            } else if (((Button) event.getSource()) == btn10) {
                row = 0;
                col = 1;
            } else if (((Button) event.getSource()) == btn11) {
                row = 1;
                col = 1;
            } else if (((Button) event.getSource()) == btn12) {
                row = 2;
                col = 1;
            } else if (((Button) event.getSource()) == btn20) {
                row = 0;
                col = 2;
            } else if (((Button) event.getSource()) == btn21) {
                row = 1;
                col = 2;
            } else if (((Button) event.getSource()) == btn22) {
                row = 2;
                col = 2;
            }
            try {
                SimpleClient.getClient().sendToServer(new update(row, col));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
    }

    public void initializeBoard(CurrentStatusB bm){
        this.currentStatusB = bm;
        updateMoves();
    }

    public void updateMoves(){
        if (currentStatusB != null){
            char[][] board = currentStatusB.getBoard();
            btn00.setText(board[0][0] + "");
            btn01.setText(board[1][0] + "");
            btn02.setText(board[2][0] + "");
            btn10.setText(board[0][1] + "");
            btn11.setText(board[1][1] + "");
            btn12.setText(board[2][1] + "");
            btn20.setText(board[0][2] + "");
            btn21.setText(board[1][2] + "");
            btn22.setText(board[2][2] + "");
        }
    }



    @Subscribe
    public void OnCurrentStatusB(CurrentStatusB bm){
        this.currentStatusB = bm;
        Platform.runLater(this::updateMoves);
    }
}
