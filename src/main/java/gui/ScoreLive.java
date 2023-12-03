package gui;

import datagame.Data;
import config.MazeConfig;

import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ScoreLive {
    private final Group layout = new Group();


    public ScoreLive(){
        Image coin = new Image(/* ../ressources/*/"coin.png");
        ImageView imageView = new ImageView(coin);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        Image live = new Image(/* ../ressources/*/"heart.png");
        ImageView imageView1 = new ImageView(live);
        imageView1.setFitHeight(50);
        imageView1.setFitWidth(50);

        Text text = new Text(": "+Data.getScore());
        text.setFont(Font.font(30));
        text.setFill(Color.WHITE);
        Text text1 = new Text(": "+Data.getLive());
        text1.setFont(Font.font(30));
        text1.setFill(Color.WHITE);

        double score_x = Data.getWidth()*0.02;
        double score_y = Data.getHeight()-(Data.getHeight()*0.15);

        double live_x = Data.getWidth()-(Data.getWidth()*0.2);
        double live_y = Data.getHeight()-(Data.getHeight()*0.15);

        System.out.println(Data.getWidth());

        imageView.setLayoutX(score_x);
        imageView.setLayoutY(score_y);

        imageView1.setLayoutX(live_x);
        imageView1.setLayoutY(live_y);

        text.setLayoutX(score_x+Data.getWidth()*0.07);
        text.setLayoutY(score_y+Data.getHeight()*0.04);

        text1.setLayoutX(live_x+Data.getWidth()*0.07);
        text1.setLayoutY(live_y+Data.getHeight()*0.04);
        layout.getChildren().addAll(imageView, imageView1,text,text1);
    }

    public Node getLayout(){return layout;}
}
