package gui;

import geometry.IntCoordinates;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.MazeState;

import static config.Cell.Content.DOT;

public class CellGraphicsFactory {
    private final double scale;

    public CellGraphicsFactory(double scale) {
        this.scale = scale;
    }

    /**
     * Fonction qui permet de creer les cellules du labyrinthe graphiquement
     * 
     * @param state
     * @param pos
     * @return
     */
    public GraphicsUpdater makeGraphics(MazeState state, IntCoordinates pos) {
        var group = new Group();
        group.setTranslateX(pos.x() * scale);
        group.setTranslateY(pos.y() * scale);
        var cell = state.getConfig().getCell(pos);
        var dot = new Circle();
        group.getChildren().add(dot);
        dot.setRadius(switch (cell.initialContent()) {
            case DOT -> scale / 15;
            case ENERGIZER -> scale / 5;
            case NOTHING -> 0;
        });
        dot.setCenterX(scale / 2);
        dot.setCenterY(scale / 2);
        dot.setFill(Color.YELLOW);
        if (cell.isnorthWall()) {
            var nWall = new Rectangle();
            nWall.setHeight(scale / 50);
            nWall.setWidth(scale);
            nWall.setY(0);
            nWall.setX(0);
            nWall.setFill(Color.BLUE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        if (cell.iseastWall()) {
            var nWall = new Rectangle();
            nWall.setHeight(scale);
            nWall.setWidth(scale / 50);
            nWall.setY(0);
            nWall.setX(9 * scale / 10);
            nWall.setFill(Color.BLUE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        if (cell.issouthWall()) {
            var nWall = new Rectangle();
            nWall.setHeight(scale / 50);
            nWall.setWidth(scale);
            nWall.setY(9 * scale / 10);
            nWall.setX(0);
            nWall.setFill(Color.BLUE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        if (cell.iswestWall()) {
            var nWall = new Rectangle();
            nWall.setHeight(scale);
            nWall.setWidth(scale / 50);
            nWall.setY(0);
            nWall.setX(0);
            nWall.setFill(Color.BLUE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        if (cell.isnorthWhite()) { // si le mur est blanc
            var nWall = new Rectangle();
            nWall.setHeight(scale / 30);
            nWall.setWidth(scale);
            nWall.setY(0);
            nWall.setX(0);
            nWall.setFill(Color.WHITE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        if (cell.iseastWhite()) { // si le mur est blanc
            var nWall = new Rectangle();
            nWall.setHeight(scale);
            nWall.setWidth(scale / 30);
            nWall.setY(0);
            nWall.setX(9 * scale / 10);
            nWall.setFill(Color.WHITE); // Couleur des murs
            group.getChildren().add(nWall);
        }
        // if (cell.issouthWhite()) {
        // var nWall = new Rectangle();
        // nWall.setHeight(scale/30);
        // nWall.setWidth(scale);
        // nWall.setY(9*scale/10);
        // nWall.setX(0);
        // nWall.setFill(Color.WHITE); //Couleur des murs
        // group.getChildren().add(nWall);
        // }
        if (cell.iswestWhite()) { // si le mur est blanc
            var nWall = new Rectangle();
            nWall.setHeight(scale);
            nWall.setWidth(scale / 30);
            nWall.setY(0);
            nWall.setX(0);
            nWall.setFill(Color.WHITE); // Couleur des murs
            group.getChildren().add(nWall);
        }

        return new GraphicsUpdater() {
            @Override
            public void update() {
                dot.setVisible((!state.getGridState(pos)) || state.getConfig().getCell(pos).isEnergizer());
            }

            @Override
            public Node getNode() {
                return group;
            }
        };
    }
}
