package lawnlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javax.management.RuntimeErrorException;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;

public class App extends PApplet {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    public String configPath;

    public int lives;
    public JSONArray levels;

    /**
     * The JSON array of enemies from each level
    */
    public JSONArray enemiesJson;
    public float goal;

    /**
     * The Level index of the current level
    */
    public int levelIndex;

    /**
     * The total no of levels
    */
    public int noofLevels;

    /**
     * The percentage area covered by the grass
    */
    public double completed;
	
	public PImage grass;
    public PImage concrete;
    public PImage pathGreen;
    public PImage pathRed;
    public PImage powerup1;
    public PImage powerup2;

    /**
     * If the powerup is in the  map or not
    */
    public boolean powerup1Enabled;
    public boolean powerup2Enabled;

    /**
     * If the powerup is collected by the player
    */
    public static boolean powerup1Actived;
    public static boolean powerup2Actived;

    /**
     * The powerup time left
    */
    public static int powerup1Timer = 10*App.FPS;
    public static int powerup2Timer = 10*App.FPS;
    
    public Ball ball;

    /**
     * The list of enemies in the current level
    */
    public List<Enemies> enemies = new ArrayList<>();

    /**
     * The list of xy coordinates of the green trail block
    */
    public List<int[]> greenTrailList = new ArrayList<>();

    /** 
     * The map is stored in this 2-d array
    */
    public static String[][] map;

    /**
     * A new map just for the flood fill
    */
    public static String[][] mapNew;

    /**
     * Checks if we should change the greentrail into grass
    */
    public boolean clearPath;

    /** 
     * Checks if we need to remove the greentrail as the player died
    */ 
    public boolean clearPathDead;

    /**
     *  Checks if the enemy touched the tral
    */
    public boolean changeTile;

    /**
     *  Which index to start changing the greentrail from
    */
    public int changeTileIndex;

    public int timer = 0;

    public PFont font;

    public App() {
        this.configPath = "config.json";
        this.ball = new Ball();
        map = new String[32][64];
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     * Also read in the Json file and create the font.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.pathGreen = loadImage(this.getClass().getResource("path_green.png").getPath());
        this.pathRed = loadImage(this.getClass().getResource("path_red.png").getPath());
        this.powerup1 = loadImage(this.getClass().getResource("powerup1.png").getPath());
        this.powerup2 = loadImage(this.getClass().getResource("pokeball.png").getPath());


        try{
            JSONObject configFile = new JSONObject(new FileReader(this.configPath));
            this.lives = (int) configFile.getInt("lives");
            this.levels = configFile.getJSONArray("levels");
            this.noofLevels = this.levels.size();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        // Reading the file
        this.readFile();

        this.ball.setSprite(loadImage(this.getClass().getResource("ball.png").getPath()));

        font = createFont("PTSans-Regular", 100);
        
    }

    /**
     * The current level elements is determined from the JSON file and loaded in.
    */
    public void readFile(){
        this.goal = this.levels.getJSONObject(this.levelIndex).getFloat("goal");

        this.enemiesJson = this.levels.getJSONObject(this.levelIndex).getJSONArray("enemies");
        for (int i = 0; i<this.enemiesJson.size(); i++){
            int[] therandomlocations = this.pickRandomLocation();
            int x = therandomlocations[0];
            int y = therandomlocations[1];
            if (!this.enemiesJson.getJSONObject(i).getString("spawn").equals("random")){
                String[] xy = this.enemiesJson.getJSONObject(i).getString("spawn").split(",");
                x = Integer.parseInt(xy[0]);
                y = Integer.parseInt(xy[1]);
            }
            if (this.enemiesJson.getJSONObject(i).getInt("type") == 0){
                this.enemies.add( new Worm(x,y));
                this.enemies.get(i).setSprite(loadImage(this.getClass().getResource("worm.png").getPath()));
            }
            else{
                this.enemies.add( new Beetle(x,y));
                this.enemies.get(i).setSprite(loadImage(this.getClass().getResource("beetle.png").getPath()));
            }
        }

        File f = new File(this.levels.getJSONObject(this.levelIndex).getString("outlay"));
        try{
            Scanner scan = new Scanner(f);
            int x = 0;
            while (scan.hasNext()){
                String eachLine = scan.nextLine();
                for (int i = 0; i<eachLine.length(); i++){
                if (eachLine.charAt(i) == 'X'){
                    map[x][i] =  "X";
                    }
                }
                x++;
            }
            scan.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            throw new RuntimeErrorException(null);
        }

    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {

        // Removing the previous frame
        this.rect(-1, -1, WIDTH, HEIGHT);

        // Setting the background color
        background(97, 59, 26);

        // Incrementing the timer
        this.timer++;

        if (this.lives >0 && this.levelIndex < this.noofLevels){


            powerup1();

            powerup2();

            // Drawing the map
            // “X” = concrete
            // “O” = Green Trail
            // “R” = Red Trail
            // “G” = Grass
            // “P1” = Power Up 1
            // “P2” = Power Up 2
            double denominator = 2048;
            double numerator = 0;
            double[] fraction = this.mapDraw();
            numerator = fraction[0];
            denominator = fraction[1];

            // Flood fill Grass
            if (this.clearPath){
                this.changingMapforFill();
            }

            // Initializing the clear bolleans for clearing path
            this.clearPath = false;
            this.clearPathDead = false;

            this.completed = (numerator/denominator) * 100;

            // Green Trail logic
            int x = this.ball.getX();
            int y = this.ball.getY();

            if ((x%20 == 0 && y%20 == 0)){
                if (map[(y - TOPBAR) / SPRITESIZE][x/SPRITESIZE] == "O"){
                    this.dead();
                }
                else if (map[(y - TOPBAR) / SPRITESIZE][x/SPRITESIZE] == null){
                    map[(y - TOPBAR) / SPRITESIZE][x/SPRITESIZE] = "O";
                    int[] xy = {x/SPRITESIZE,(y - TOPBAR) / SPRITESIZE};
                    this.greenTrailList.add(xy);
                }
                else{
                    this.clearPath = true;
                    this.changeTile = false;
                    this.changeTileIndex = 0;
                    this.greenTrailList = new ArrayList<>();
                }
            }

            // Enemy Movement (also checks if the enemy touches the trail)
            for (int i = 0; i<this.enemies.size(); i++){
                int[] didItinitial = this.enemies.get(i).Collide(map);
                if (didItinitial != null && map[didItinitial[1]][didItinitial[0]] == "O"){
                    this.changeTile = true;
                    int[] xyz = {didItinitial[0],didItinitial[1]};
                    for (int j = 0; j<this.greenTrailList.size(); j++){
                        if (greenTrailList.get(j)[0] == xyz[0] && greenTrailList.get(j)[1] == xyz[1]){
                            this.changeTileIndex = j;
                            break;
                        }
                    }
                }
                else if (didItinitial != null && map[didItinitial[1]][didItinitial[0]] == "G" && this.enemies.get(i) instanceof Beetle){
                    map[didItinitial[1]][didItinitial[0]] = null;
                }
                this.enemies.get(i).tick();
                this.enemies.get(i).draw(this);
            }

            // Change tile from green to red
            this.changetilefromgreentored();



            // Ball Movement
            this.ball.isConcrete();
            this.ball.tick();
            this.ball.draw(this);

            // If the player completes the level
            // Redraw the map and other variables from the next level
            if (this.completed >= this.goal * 100)
                this.changelevel();
        }

        else{
            this.gameResult();
        }

        // Resetting the timer every 30 seconds
        if (this.timer >= 30*FPS){
            this.timer =0;
        }
        
        // Information Bar
        this.informationBar();
    }
    
    /**
    * Checks which key the player pressed and the movement of the ball is adjusted accordingly
    */
    public void keyPressed() {
        // Left: 37
        // Up: 38
        // Right: 39
        // Down: 40
        if (this.keyPressed){
            if (this.keyCode == 37) {
                this.ball.pressLeft();
            } else if (this.keyCode == 39) {
                this.ball.pressRight();
            } else if (this.keyCode == 38) {
                this.ball.pressUp();
            } else if (this.keyCode == 40) {
                this.ball.pressDown();
            }
        }
    }

    /**
     * The map elements such as grass, concrete, green trail, red trail and powerups are drawn here on the window
     * “X” = concrete
     * “O” = Green Trail
     * “R” = Red Trail
     * “G” = Grass
     * “P1” = Power Up 1
     * “P2” = Power Up 2
     * @return the numerator and denominator for area covered
     */
    public double[] mapDraw(){
        // Initializing the numerator and denominator for percentage of area covered
        double denominator = 2048;
        double numerator = 0;
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if (map[y][x] == "X"){
                    image(this.concrete, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                    denominator--;
                }
                else if (map[y][x] == "O"){
                    if (this.clearPath){
                        map[y][x] = "G";
                    }
                    else if (this.clearPathDead){
                        map[y][x] = null;
                    }
                    image(this.pathGreen, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                }
                else if (map[y][x] == "R"){
                    if (this.clearPath){
                        map[y][x] = "G";
                    }
                    else if (this.clearPathDead){
                        map[y][x] = null;
                    }
                    image(this.pathRed, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                }
                else if (map[y][x] == "P1"){
                    if (!this.powerup1Enabled || powerup1Actived){
                        map[y][x] = null;
                    }
                    image(this.powerup1, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                }
                else if (map[y][x] == "P2"){
                    if (!this.powerup2Enabled || powerup2Actived){
                        map[y][x] = null;
                    }
                    image(this.powerup2, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                }
                if (map[y][x] == "G"){
                    image(this.grass, x * SPRITESIZE, (y * SPRITESIZE) + TOPBAR);
                    numerator++;
                }
            }
        }
        double[] xy = {numerator,denominator};
        return xy;
    }

    /**
     * This function decreases the lives of the player and resets the ball position
     */
    public void dead(){
        this.lives--;
        this.ball.restartBall();
        this.clearPathDead = true;
        this.ball.timer =0;
        this.changeTile = false;
        this.changeTileIndex = 0;
        this.greenTrailList = new ArrayList<>();
    }

    /**
     * The information bar is created here. All text at the top is written here.
     * @return if no issues occurred
     */
    public boolean informationBar(){
        try{
            textFont(font, 35);
            if (powerup1Actived){
                fill(182,33,45);
                text("Powerup: " + str(powerup1Timer/FPS) + "s",450, 40);
            }
            fill(255,255,255);
            text("Lives: " + str(this.lives), 230, 41);
            text(str((int)(this.completed)) + "% / " + str((int) (this.goal*100)) + "%", 800, 41);
            textFont(font, 25);
            text("Level " + str(this.levelIndex + 1), 1180, 70);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * The final result text is shown with this function.
     * The player wins if they complete the levels
     * The player loses if it has no lives left.
     * @return true if the player wins false otherwise
     */
    public boolean gameResult(){
        // If the player wins the game
        if (levelIndex == this.noofLevels){
            textFont(font, 100);
            fill(0,200,0);
            text("You Win", 450, 375);
            return true;
        }
        // If the player loses the game
        else{
            textFont(font, 100);
            fill(200,0,0);
            text("Game Over", 400, 375);
        }
        return false;
    }

    /**
     * Powerup 1 logic. 
     * This powerup spawns after 10 seconds into the game and is on the map for 10 seconds
     * When collected the effect stays for 10 secinds.
     * The timer of these are called here.
     */
    public void powerup1(){
        if (this.timer >= 10*FPS && this.timer<=20*FPS){
            if (!this.powerup1Enabled){
                int[] therandomlocations = this.pickRandomLocation();
                int x = therandomlocations[0];
                int y = therandomlocations[1];
                map[(y-TOPBAR)/20][x/20] = "P1";
                this.powerup1Enabled = true;
            }
            if (map[(this.ball.getY() - TOPBAR) / SPRITESIZE][this.ball.getX()/SPRITESIZE] == "P1"){
                powerup1Actived = true;
            }
        }
        else if (!powerup1Actived){
            this.powerup1Enabled = false;
        }

        if (powerup1Actived){
            if (powerup1Timer == 0){
                powerup1Timer = 10*App.FPS;
                powerup1Actived = false;
            }
            powerup1Timer--;
        }
    }

    /**
     * The function changes the green trail tiles in the map to red from where the enemy touched it
     */
    public void changetilefromgreentored(){
        if (this.changeTile){
            if (this.timer % 3 == 0 && this.greenTrailList.size() != 0){
                map[this.greenTrailList.get(this.changeTileIndex)[1]][this.greenTrailList.get(this.changeTileIndex)[0]] = "R"; // Bug
                this.changeTileIndex++;
                if (this.changeTileIndex == this.greenTrailList.size()){
                    this.dead();
                }
            }

        }
    }

    /**
     * Powerup 2 logic. 
     * This powerup spawns after 15 seconds into the game and is on the map for 10 seconds
     * If red tiles are present they are changed to green tiles.
     */
    public void powerup2(){
        if (this.timer >= 15*FPS && this.timer<=30*FPS){
            if (!this.powerup2Enabled){
                int[] therandomlocations = this.pickRandomLocation();
                int x = therandomlocations[0];
                int y = therandomlocations[1];
                map[(y-TOPBAR)/20][x/20] = "P2";
                this.powerup2Enabled = true;
            }
            if (map[(this.ball.getY() - TOPBAR) / SPRITESIZE][this.ball.getX()/SPRITESIZE] == "P2"){
                powerup2Actived = true;
                this.changeTile = false;
                this.changeTileIndex = 0;
                for (int k = 0; k<this.greenTrailList.size(); k++){
                    map[this.greenTrailList.get(k)[1]][this.greenTrailList.get(k)[0]] = "O";
                }
            }
        }
        else if (!powerup2Actived){
            this.powerup2Enabled = false;
        }

        if (powerup2Actived){
            if (powerup2Timer == 0){
                powerup2Timer = 10*App.FPS;
                powerup2Actived = false;
            }
            powerup2Timer--;
        }
    }

    /**
     * The level Index is increased and checks if there are more levels.
     * If there are more levels, the game file of the next level is read and the game is resetted accordingly.
     */
    public void changelevel(){
        this.completed = 0;
        this.levelIndex++;
        if (this.levelIndex < this.noofLevels){
            this.enemies = new ArrayList<>();
            this.greenTrailList = new ArrayList<>();
            this.ball.restartBall();
            map = new String[32][64];
            this.timer = 0;
            this.readFile();
        }
    }

    /**
     * A random location on the map is selected (which is empty)
     * @return the random empty location on the map
    */
    public int[] pickRandomLocation(){
        Random rand = new Random();
        int x = rand.nextInt(WIDTH- (4*SPRITESIZE)) + (2*SPRITESIZE);
        int y = rand.nextInt(HEIGHT-(4*SPRITESIZE)-TOPBAR) + (2*SPRITESIZE) + TOPBAR;
        while (map[(y - TOPBAR)/20][x/20] != null){
            x = rand.nextInt(WIDTH- (4*SPRITESIZE)) + (2*SPRITESIZE);
            y = rand.nextInt(HEIGHT-(4*SPRITESIZE)-TOPBAR) + (2*SPRITESIZE) + TOPBAR;
        }
        int[] therandomlocations = {x,y};
        return therandomlocations;
    }

    /**
     * This function is used for the floodfill
     * The map is cloned and flood filled the regions from the nemies positions with a placeholder
     * The null position in the map is filled and the placeholder is replaced with null (empty space)
    */
    public void changingMapforFill(){
        mapNew = new String[32][64];
        mapNew = map.clone();

        for (int i = 0; i<this.enemies.size(); i++){
            this.floodFill(mapNew, this.enemies.get(i).getX()/20, (this.enemies.get(i).getY()-TOPBAR)/20);
        }

        // Converting the placeholder into null and null to grass
        for (int i = 0; i<map.length; i++){
            for (int j = 0; j<map[i].length; j++){
                if (mapNew[i][j] == null || mapNew[i][j] == "P1"|| mapNew[i][j] == "P2"){
                    mapNew[i][j] = "G";
                }
                else if (mapNew[i][j] == "IN"){
                    mapNew[i][j] = null;
                }
                else if (mapNew[i][j] == "P11"){
                    mapNew[i][j] = "P1";
                }
                else if (mapNew[i][j] == "P22"){
                    mapNew[i][j] = "P2";
                }
                
            }
        }
        map = mapNew.clone();
    }

    /**
     * This is the function to call the dfs recursive function for floodfill.
     * This method is taken from a youtube video tutorial
     * @param grid is the map
     * @param x the x coordinate of the starting position of the floodfill
     * @param y the y coordinate of the starting position of the floodfill
    */
    public void floodFill(String[][]grid, int x, int y){
        if (grid[y][x] == "IN"){
            return;
        }
        dfs(grid, x, y);
    }

    /**
     * The dfs fucntion which fill all regions in all four directions from the given input
     * @param grid is the map
     * @param x the x coordinate of the starting position of the floodfill dfs
     * @param y the y coordinate of the starting position of the floodfill dfs
    */
    public void dfs(String[][]grid, int x, int y){
        int n = grid.length;
        int m = grid[0].length;
        if (grid[y][x] == "P1"){
            grid[y][x] = "P11";
            return;
        }
        if (grid[y][x] == "P2"){
            grid[y][x] = "P22";
            return;
        }
        if (x<0 || y>=n || y<0 || x>=m || grid[y][x] != null){
            return;
        }
        grid[y][x] = "IN";
        dfs(grid, x+1, y);
        dfs(grid, x-1, y);
        dfs(grid, x, y+1);
        dfs(grid, x, y-1);
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
