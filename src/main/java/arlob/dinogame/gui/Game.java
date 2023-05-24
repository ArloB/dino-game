package arlob.dinogame.gui;

import arlob.dinogame.Dinosaurs;
import arlob.dinogame.Orientation;
import arlob.dinogame.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Set;

public class Game extends Application {

    private static final int SQUARE_SIZE = 100;
    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;
    private static final int BOARD_WIDTH = 542;
    private static final int BOARD_HEIGHT = 444;
    private static final int BOARD_MARGIN = 71;
    private static final int OBJECTIVE_WIDTH = 162;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 100;
    private static final int OBJECTIVE_MARGIN_Y = 20;
    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_X = MARGIN_X + (3 * SQUARE_SIZE) + SQUARE_SIZE + MARGIN_X;
    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN;
    private static final int PLAY_AREA_X = BOARD_X + BOARD_MARGIN;
    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH + MARGIN_X;
    private static final int GAME_HEIGHT = 620;
    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms

    /* marker for unplaced tiles */
    public static final char NOT_PLACED = 255;

    /* node groups */
    private final Group root = new Group();
    private final Group gtiles = new Group();
    private final Group solution = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group exposed = new Group();
    private final Group objective = new Group();

    private final Popup popup = new Popup();

    private static Set<String> solutions;

    private static final String BASEBOARD_URI = Objects.requireNonNull(Game.class.getClassLoader().getResource("baseboard.png")).toString();

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = Objects.requireNonNull(Game.class.getClassLoader().getResource("211684__oceanictrancer__classic-house-loop-128-bpm.wav")).toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* message on completion */
    private final Text completionText = new Text("Well done!");

    /* the state of the tiles */
    char[] tileState = new char[6];   //  all off screen to begin with

    /* The underlying game */
    Dinosaurs dinosaursGame;

    /* Define a drop shadow effect that we will appy to tiles */
    private static final DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ static {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /* Graphical representations of tiles */
    static class GTile extends ImageView {
        int tileID;


        /**
         * Construct a particular playing tile
         *
         * @param tile The letter representing the tile to be created.
         */
        GTile(char tile) {
            if (tile > 'f' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tileID = tile - 'a';
            setFitHeight(2 * SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            setEffect(dropShadow);
        }

        /**
         * Construct a playing tile, which is placed on the board at the start of the game,
         * as a part of some challenges
         *
         * @param tile        The letter representing the tile to be created.
         * @param orientation The integer representation of the tile to be constructed
         */
        GTile(char tile, int orientation) {
            if (tile > 'f' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            this.tileID = tile - 'a';
            if (orientation % 2 == 0) {
                setFitHeight(2 * SQUARE_SIZE);
                setFitWidth(SQUARE_SIZE);
            } else {
                setFitHeight(SQUARE_SIZE);
                setFitWidth(2 * SQUARE_SIZE);
            }
            setImage(new Image(Objects.requireNonNull(Game.class.getClassLoader().getResource(tile + "-" + (char) (orientation + '0') + ".png")).toString()));
            setEffect(dropShadow);
        }

        /**
         * A constructor used to build the objective tile.
         *
         * @param tile The tile to be displayed (one of 80 objectives)
         * @param x    The x position of the tile
         * @param y    The y position of the tile
         */
        GTile(int tile, int x, int y) {
            if (!(tile <= 80 && tile >= 1)) {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }

            String t = String.format("%02d", tile);
            setImage(new Image(Objects.requireNonNull(Game.class.getClassLoader().getResource(t + ".png")).toString()));
            this.tileID = tile;
            setFitHeight(OBJECTIVE_HEIGHT);
            setFitWidth(OBJECTIVE_WIDTH);
            setEffect(dropShadow);

            setLayoutX(x);
            setLayoutY(y);
        }
    }

    /**
     * This class extends Tile with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class DraggableTile extends GTile {
        int homeX, homeY;           // the position in the window where
        // the tile should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)
        Image[] images = new Image[4];
        int orientation;    // 0=North... 3=West
        long lastRotationTime = System.currentTimeMillis(); // only allow rotation every ROTATION_THRESHOLD (ms)
        // This caters for mice which send multiple scroll events per tick.

        /**
         * Construct a draggable tile
         *
         * @param tile The tile identifier ('a' - 'f')
         */
        DraggableTile(char tile) {
            super(tile);
            for (int i = 0; i < 4; i++) {
                char idx = (char) (i + '0');
                images[i] = new Image(Objects.requireNonNull(Game.class.getClassLoader().getResource(tile + "-" + idx + ".png")).toString());
            }
            setImage(images[0]);
            orientation = 0;
            tileState[tile - 'a'] = NOT_PLACED; // start out off board
            homeX = MARGIN_X + ((tile - 'a') % 3) * SQUARE_SIZE;
            setLayoutX(homeX);
            homeY = OBJECTIVE_MARGIN_Y + OBJECTIVE_HEIGHT + MARGIN_Y + ((tile - 'a') / 3) * 2 * SQUARE_SIZE;
            setLayoutY(homeY);

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD) {
                    lastRotationTime = System.currentTimeMillis();
                    hideCompletion();
                    rotate();
                    event.consume();
                    checkCompletion();
                }
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });
        }

        /**
         * Snap the tile to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid() {

            if (onBoard() && (!alreadyOccupied())) {
                if ((getLayoutX() >= (PLAY_AREA_X - (SQUARE_SIZE / 2.0))) && (getLayoutX() < (PLAY_AREA_X + (SQUARE_SIZE / 2.0)))) {
                    setLayoutX(PLAY_AREA_X);
                } else if ((getLayoutX() >= PLAY_AREA_X + (SQUARE_SIZE / 2.0)) && (getLayoutX() < PLAY_AREA_X + 1.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 1.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 2.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 2 * SQUARE_SIZE);
                } else if ((getLayoutX() >= PLAY_AREA_X + 2.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 3.5 * SQUARE_SIZE)) {
                    setLayoutX(PLAY_AREA_X + 3 * SQUARE_SIZE);
                }

                if ((getLayoutY() >= (PLAY_AREA_Y - (SQUARE_SIZE / 2.0))) && (getLayoutY() < (PLAY_AREA_Y + (SQUARE_SIZE / 2.0)))) {
                    setLayoutY(PLAY_AREA_Y);
                } else if ((getLayoutY() >= PLAY_AREA_Y + (SQUARE_SIZE / 2.0)) && (getLayoutY() < PLAY_AREA_Y + 1.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + SQUARE_SIZE);
                } else if ((getLayoutY() >= PLAY_AREA_Y + 1.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 2.5 * SQUARE_SIZE)) {
                    setLayoutY(PLAY_AREA_Y + 2 * SQUARE_SIZE);
                }
                setPosition();
            } else {
                snapToHome();
            }
            checkCompletion();
        }


        /**
         * @return true if the tile is on the board
         */
        private boolean onBoard() {
            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2.0)) && (getLayoutX() < (PLAY_AREA_X + 3.5 * SQUARE_SIZE))
                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2.0)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
        }

        /**
         * a function to check whether the current destination cell
         * is already occupied by another tile
         *
         * @return true if the destination cell for the current tile
         * is already occupied, and false otherwise
         */
        private boolean alreadyOccupied() {
            int x = (int) (getLayoutX() + (SQUARE_SIZE / 2) - PLAY_AREA_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() + (SQUARE_SIZE / 2) - PLAY_AREA_Y) / SQUARE_SIZE;

            // it occupies two cells
            int idx1 = y * 4 + x;
            int idx2;

            if (orientation % 2 == 0)
                idx2 = (y + 1) * 4 + x;
            else
                idx2 = y * 4 + x + 1;

            for (int i = 0; i < 6; i++) {
                if (tileState[i] == NOT_PLACED)
                    continue;

                int tIdx1 = tileState[i] / 4;
                int tIdx2;
                int tOrn = tileState[i] % 4;

                if (tOrn % 2 == 0)
                    tIdx2 = tIdx1 + 4;
                else
                    tIdx2 = tIdx1 + 1;

                if (tIdx1 == idx1 || tIdx2 == idx1 || tIdx1 == idx2 || tIdx2 == idx2)
                    return true;
            }
            return false;
        }


        /**
         * Snap the tile to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setFitHeight(2 * SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            setImage(images[0]);
            orientation = 0;
            tileState[tileID] = NOT_PLACED;
            dinosaursGame.removeTile((char) (this.tileID + 'a'));
        }


        /**
         * Rotate the tile by 90 degrees and update any relevant state
         */
        private void rotate() {
            orientation = (orientation + 1) % 4;
            setImage(images[(orientation)]);
            setFitWidth((1 + (orientation % 2)) * SQUARE_SIZE);
            setFitHeight((2 - (orientation % 2)) * SQUARE_SIZE);
            toFront();
            setPosition();
        }


        /**
         * Determine the grid-position of the origin of the tile
         * or 'NOT_PLACED' if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            setPosition((int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE, (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE);
        }

        private void setPosition(int x, int y) {
            if (x < 0) {
                tileState[tileID] = NOT_PLACED;
                dinosaursGame.removeTile((char) (this.tileID + 'a'));
            } else {
                char o = 'N';

                if (orientation == 1) o = 'E';
                else if (orientation == 2) o = 'S';
                else if (orientation == 3) o = 'W';

                String t = Character.toString(this.tileID + 'a') + x + y + o + "";

                if (dinosaursGame.validPlacement(t)) {
                    if (tileState[tileID] == NOT_PLACED) {
                        dinosaursGame.addTileToBoard(t);
                    } else {
                        dinosaursGame.updateTileOnBoard(t);
                    }
                } else {
                    snapToHome();
                    return;
                }

                char val = (char) ((y * 4 + x) * 4 + orientation);
                tileState[tileID] = val;
            }
        }


        /**
         * @return the mask placement represented as a string
         */
        public String toString() {
            return "" + tileState[tileID];
        }
    }

    /**
     * Set up event handlers for the main game
     *
     * @param scene        The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case M -> {
                    toggleSoundLoop();
                    event.consume();
                }
                case Q -> {
                    Platform.exit();
                    event.consume();
                }
                case SLASH -> {
                    solution.setOpacity(1.0);
                    gtiles.setOpacity(0);
                    event.consume();
                }
            }
        });
        scene.setOnKeyReleased(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.SLASH) {
                solution.setOpacity(0);
                gtiles.setOpacity(1.0);
                event.consume();
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened (" + LOOP_URI + "): " + e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }

    /**
     * Set up the group that represents the solution (and make it transparent)
     *
     * @param solution The solution as an array of chars.
     */
    private void makeSolution(String solution) {
        this.solution.getChildren().clear();

        if (solution.length() == 0) {
            return;
        }

        if (solution.length() != 24) {
            throw new IllegalArgumentException("Solution incorrect length: " + solution);
        }

        for (int i = 0; i < solution.length(); i += 4) {
            GTile gtile = new GTile(solution.charAt(i), Tile.placementToOrientation(solution.substring(i, i + 4)).ordinal());
            int x = solution.charAt(i + 1) - '0';
            int y = solution.charAt(i + 2) - '0';

            gtile.setLayoutX(PLAY_AREA_X + (x * SQUARE_SIZE));
            gtile.setLayoutY(PLAY_AREA_Y + (y * SQUARE_SIZE));

            this.solution.getChildren().add(gtile);
        }
        this.solution.setOpacity(0);
    }


    /**
     * Set up the group that represents the places that make the board
     */
    private void makeBoard() {
        board.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(BOARD_WIDTH);
        baseboard.setFitHeight(BOARD_HEIGHT);
        baseboard.setLayoutX(BOARD_X);
        baseboard.setLayoutY(BOARD_Y);
        board.getChildren().add(baseboard);

        board.toBack();
    }


    /**
     * Set up each of the six tiles
     */
    private void makeTiles() {
        gtiles.getChildren().clear();
        for (char m = 'a'; m <= 'f'; m++) {
            gtiles.getChildren().add(new DraggableTile(m));
        }
    }


    private void placeTiles(String starting) {
        if (starting.length() > 0) {
            for (int i = 0; i < starting.length(); i += 4) {
                String s = starting.substring(i, i + 4);

                for (Node n : gtiles.getChildren()) {
                    DraggableTile t = ((DraggableTile) n);

                    int id = new Tile(s).getTileType().ordinal();

                    if (t.tileID == id) {
                        int orientation = Tile.placementToOrientation(s).ordinal();

                        int x = s.charAt(1) - '0';
                        int y = s.charAt(2) - '0';

                        n.setLayoutX(PLAY_AREA_X + (x * SQUARE_SIZE));
                        n.setLayoutY(PLAY_AREA_Y + (y * SQUARE_SIZE));

                        t.setImage(t.images[(orientation)]);
                        t.setFitWidth((1 + (orientation % 2)) * SQUARE_SIZE);
                        t.setFitHeight((2 - (orientation % 2)) * SQUARE_SIZE);

                        tileState[id] = (char) ((y * 4 + x) * 4 + orientation);
                    }
                }
            }
        }
    }


    /**
     * Add the objective to the board
     */
    private void addObjectiveToBoard() {
        objective.getChildren().clear();
        objective.getChildren().add(new GTile(dinosaursGame.getObjective().getProblemNumber(), OBJECTIVE_MARGIN_X, OBJECTIVE_MARGIN_Y));
    }


    /**
     * Check game completion and update status
     */
    private void checkCompletion() {
        StringBuilder state = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            if (tileState[i] == NOT_PLACED)
                return;
            state.append((char) (i + 'a')).append((char) (((tileState[i] / 4) % 4) + '0')).append((char) (((tileState[i] / 4) / 4) + '0')).append(Orientation.values()[tileState[i] % 4].toChar());
        }

        if (solutions.contains(state.toString()))
            showCompletion();
    }


    /**
     * Put all of the tiles back in their home position
     */
    private void resetPieces() {
        gtiles.toFront();
        for (Node n : gtiles.getChildren()) {
            ((DraggableTile) n).snapToHome();
        }
    }


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(BOARD_X + BOARD_MARGIN + 240);
        button.setLayoutY(GAME_HEIGHT - 55);
        button.setOnAction(e -> newGame());
        controls.getChildren().add(button);

        difficulty.setMin(1);
        difficulty.setMax(4);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(BOARD_X + BOARD_MARGIN + 70);
        difficulty.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(BOARD_X + BOARD_MARGIN);
        difficultyCaption.setLayoutY(GAME_HEIGHT - 50);
        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(20);
        completionText.setLayoutY(375);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            dinosaursGame = new Dinosaurs((int) difficulty.getValue() - 1);
            dinosaursGame.initializeBoardState(dinosaursGame.getObjective().getInitialState());
            solutions = dinosaursGame.getSolutions();
            if (solutions.size() > 0)
                makeSolution((String) solutions.toArray()[0]);
            makeTiles();
            addObjectiveToBoard();
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
        placeTiles(dinosaursGame.getObjective().getInitialState());
    }


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("DINOSAURS - Mystic Islands");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);

        popup.setX(300);
        popup.setY(200);

        root.getChildren().add(gtiles);
        root.getChildren().add(board);
        root.getChildren().add(solution);
        root.getChildren().add(controls);
        root.getChildren().add(exposed);
        root.getChildren().add(objective);

        setUpHandlers(scene);
        setUpSoundLoop();
        makeBoard();
        makeControls();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
