package com.tank.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tank.game.CanBePickedUp.*;
import com.tank.game.Explosion.Explosion;
import com.tank.game.GameStates.GameState;
import com.tank.game.GameStates.GameStateManager;
import com.tank.game.MiniMap.MiniMap;
import com.tank.game.Projectiles.Missile;
import com.tank.game.Projectiles.Projectiles;
import com.tank.game.Projectiles.UpgradedMissile;
import com.tank.game.Tanks.Tank;
import com.tank.game.UI.UserInterface;
import com.tank.game.Walls.HorizontalWall;
import com.tank.game.Walls.VerticalWall;
import com.tank.game.Walls.Wall;
import com.tank.game.Zombies.Zombie;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TankGame extends Game {

	// All primitive values
	private boolean apocalypseActivated = false;
	private boolean brutalMode = false;
	private boolean hitZombie = false;
	private boolean obtainedIngredient1 = false;
	private boolean obtainedIngredient2 = false;
	private boolean obtainedIngredient3 = false;
	private float elapsedTime = 0f;
	private final float mapMaxX = 2048;
	private final float mapMaxY = 1536;
	private float zombieSpeed = 1.35f;
	private int missileCounter1 = 32;
	private int missileCounter2 = 32;
	private int missileCounter3 = 32;
	private int numberOfZombiesPerSpawnBatch = 6;
	private int respawnZombieCounter = 0;
	private int screenTimer = 0; // Ensures that when the game ends, the endgame screen doesn't blink and go away immediately
	private int zombieCounter1 = 51;
	private int zombieCounter2 = 51;
	private int zombieHP = 30;
	private int zombieSpawnRate = 500; // Original: 500. The higher this number, the slower the respawn rate.

	// All textures
	SpriteBatch batch;
	Texture enemyTankTexture;
	Texture explosionImage;
	Texture mapImage;
	Texture menuImage;
	Texture missile;
	Texture missile2;
	Texture player1TankTexture;
	Texture player2TankTexture;
	Texture PlayerOneUIBackground;
	Texture PlayerTwoUIBackground;
	Texture zombieSpriteSheet;

	// Other aggregate classes used
	private AssetManager assetManager;
	private GameStateManager gameStateManager;
	private MiniMap miniMap;
	private Tank player1Tank;
	private Tank player2Tank;
	private Tank enemyTank;

	// All cameras and viewports
	private Camera cameraMiniMap;
	private OrthographicCamera cameraPlayer1;
	private OrthographicCamera cameraPlayer2;
	private OrthographicCamera cameraPlayer1UI;
	private OrthographicCamera cameraPlayer2UI;
	private UserInterface player1UI;
	private UserInterface player2UI;
	private Viewport viewportMiniMap;
	private Viewport viewportPlayer1;
	private Viewport viewportPlayer2;
	private Viewport viewportPlayer1UI;
	private Viewport viewportPlayer2UI;

	// All lists used
	private List<Explosion> explosions = new ArrayList<>();
	private List<CanBePickedUp> items = new ArrayList<>();
	private List<Rectangle> obstacles = new ArrayList<>();
	private List<Projectiles> projectiles = new ArrayList<>();
	private List<Tank> tanks = new ArrayList<>();
	private List<Wall> walls = new ArrayList<>();
	private List<Zombie> zombies = new ArrayList<>();

	// Explosion-related content
	private Animation<TextureRegion> explosion;

	// All zombie walking animations (8)
	private Animation<TextureRegion> zombieDLWalkingAnimation;
	private Animation<TextureRegion> zombieLWalkingAnimation;
	private Animation<TextureRegion> zombieULWalkingAnimation;
	private Animation<TextureRegion> zombieUWalkingAnimation;
	private Animation<TextureRegion> zombieURWalkingAnimation;
	private Animation<TextureRegion> zombieRWalkingAnimation;
	private Animation<TextureRegion> zombieDRWalkingAnimation;
	private Animation<TextureRegion> zombieDWalkingAnimation;

	// All zombie running animations (8)
	private Animation<TextureRegion> zombieDLRunningAnimation;
	private Animation<TextureRegion> zombieLRunningAnimation;
	private Animation<TextureRegion> zombieULRunningAnimation;
	private Animation<TextureRegion> zombieURunningAnimation;
	private Animation<TextureRegion> zombieURRunningAnimation;
	private Animation<TextureRegion> zombieRRunningAnimation;
	private Animation<TextureRegion> zombieDRRunningAnimation;
	private Animation<TextureRegion> zombieDRunningAnimation;

	// ***** All Core Game Functions *****
	@Override
	public void create () {
		// Manage the game state and load up screens
		gameStateManager = new GameStateManager(this); // Automatically set to load up the Prologue

		// Load the textures and the sprite batch
		loadTexturesAndSprites();

		// Create obstacles (forests, walls, cliffs, water, etc.)
		createObstacles();

		// Load sounds
		loadSounds();

		// Initialize cameras and viewports for player 1 and 2
		setUpCamerasAndViewports();

		// Set up mini-map
		setUpMiniMap();
	}
	@Override
	public void render () {
		// Clear screen
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update timer
		float deltaTime = Gdx.graphics.getDeltaTime();
		elapsedTime += deltaTime;

		// Check game state to determine which screen should be rendered
		switch (gameStateManager.getCurrentState()) {
			case PROLOGUE:
				gameStateManager.getCurrentScreen().show();
				pressAnyKeyToContinue();
				break;
			case START_MENU:
				gameStateManager.getCurrentScreen().show();
				break;
			case GAMEPLAY: // Do nothing; continue to do all the rendering within this class
				renderAll(elapsedTime);
				break;
			case GAME_OVER_LOSE:
			case GAME_OVER_WIN:
				gameStateManager.getCurrentScreen().show();
				screenTimer++;
				if(screenTimer > 100){
					pressAnyKeyToContinue();
					if(gameStateManager.getCurrentState() == GameState.START_MENU) {
						screenTimer = 0;
					}
				}
				break;
		}
	}
	@Override
	public void dispose () {
		batch.dispose();
		enemyTankTexture.dispose();
		explosionImage.dispose();
		mapImage.dispose();
		menuImage.dispose();
		missile.dispose();
		missile2.dispose();
		player1TankTexture.dispose();
		player2TankTexture.dispose();
		PlayerOneUIBackground.dispose();
		PlayerTwoUIBackground.dispose();
		zombieSpriteSheet.dispose();
	}

	// ***** All Other Helper Functions *****
	public void activateApocalypseEvent(){
		apocalypseActivated = true;
		this.zombieSpawnRate = 220;
		this.numberOfZombiesPerSpawnBatch = 10;
		Random random = new Random();
		for(int i = 0; i < 35; i++){
			Zombie newZombie = new Zombie(player1Tank, player2Tank, zombieSpeed, zombieHP, zombieUWalkingAnimation, zombieULWalkingAnimation, zombieLWalkingAnimation, zombieDLWalkingAnimation, zombieDWalkingAnimation, zombieDRWalkingAnimation,    zombieRWalkingAnimation, zombieURWalkingAnimation, zombieURunningAnimation, zombieULRunningAnimation, zombieLRunningAnimation, zombieDLRunningAnimation, zombieDRunningAnimation, zombieDRRunningAnimation, zombieRRunningAnimation, zombieURRunningAnimation, obstacles, walls);
			float yCoordinate = random.nextInt(690);
			float xCoordinate = random.nextInt(750 - 140) + 140;
			newZombie.setPosition(xCoordinate, yCoordinate);
			zombies.add(newZombie);
		}
	}
	public void activateEvent1(Tank tank){
		Random random = new Random();
		for(int i = 0; i < 25; i++){
			Zombie newZombie = new Zombie(player1Tank, player2Tank, zombieSpeed, zombieHP, zombieUWalkingAnimation, zombieULWalkingAnimation, zombieLWalkingAnimation, zombieDLWalkingAnimation, zombieDWalkingAnimation, zombieDRWalkingAnimation,    zombieRWalkingAnimation, zombieURWalkingAnimation, zombieURunningAnimation, zombieULRunningAnimation, zombieLRunningAnimation, zombieDLRunningAnimation, zombieDRunningAnimation, zombieDRRunningAnimation, zombieRRunningAnimation, zombieURRunningAnimation, obstacles, walls);
			float yCoordinate = random.nextInt(1290 - 890) + 890;
			newZombie.setPosition(710, yCoordinate);
			newZombie.setNearestTarget(tank);
			newZombie.setKillMode();
			zombies.add(newZombie);
		}
	}
	public void activateEnemyTankAI(){
		// Scan for players to shoot at them
		if(Math.abs(player1Tank.getxCoordinate() - enemyTank.getxCoordinate()) < 600 && Math.abs(player1Tank.getyCoordinate() - enemyTank.getyCoordinate()) < 600){
			rotateEnemyTankToPlayer(player1Tank);
			addProjectile(enemyTank, missileCounter3);
		}
		else if(Math.abs(player2Tank.getxCoordinate() - enemyTank.getxCoordinate()) < 600 && Math.abs(player2Tank.getyCoordinate() - enemyTank.getyCoordinate()) < 600){
			rotateEnemyTankToPlayer(player2Tank);
			addProjectile(enemyTank, missileCounter3);
		}
	}
	public void adjustTankSpeedIfOnSand(Tank currentTank){
		if(currentTank.getxCoordinate() > 850 && currentTank.getyCoordinate() < 410){
			currentTank.setSpeed(1.85f);
		}
		else{
			currentTank.determineSpeed();
		}
	}
	public void addProjectile(Tank currentTank, int missileCounter){
		if(missileCounter > 31) {
			Projectiles newMissile;
			if(currentTank.isMissileUpgraded()) {
				newMissile = new UpgradedMissile(currentTank.getTankBoundingBox(), currentTank.getxCoordinate() + 13, currentTank.getyCoordinate() + 14, currentTank.getTankSprite().getRotation());
			}
			else{
				if(currentTank == enemyTank){
					newMissile = new Missile(currentTank.getTankBoundingBox(), currentTank.getxCoordinate() + 8, currentTank.getyCoordinate() + 14, currentTank.getTankSprite().getRotation() - 90);
				}
				else {
					newMissile = new Missile(currentTank.getTankBoundingBox(), currentTank.getxCoordinate(), currentTank.getyCoordinate() + 22, currentTank.getTankSprite().getRotation());
				}
			}
			projectiles.add(newMissile);
			Sound explosionSound = assetManager.get("missile_shot.mp3", Sound.class);
			explosionSound.play();
			if(currentTank == player1Tank){
				if(currentTank.isMissileUpgraded()){ // Upgraded missiles can fire faster
					missileCounter1 = 4;
				}
				else {
					missileCounter1 = 0;
				}
			}
			else if(currentTank == player2Tank){
				if(currentTank.isMissileUpgraded()){ // Upgraded missiles can fire faster
					missileCounter2 = 4;
				}
				else {
					missileCounter2 = 0;
				}
			}
			else if(currentTank == enemyTank){
				missileCounter3 = 0;
			}
		}
	}
	public void allIngredientsObtained(){
		if(obtainedIngredient1 && obtainedIngredient2 && obtainedIngredient3){
			activateApocalypseEvent();
		}
	}
	public float calculateDistanceFromBoss(Tank tank){
		return (float) Math.sqrt(Math.pow(enemyTank.getxCoordinate() - tank.getxCoordinate(), 2) + Math.pow(enemyTank.getyCoordinate() - tank.getyCoordinate(), 2));
	}
	public void changeGameState(GameState gameState){
		gameStateManager.setState(gameState);
	}
	public void checkIfProjectileHits(){
		Sound explosionSound = assetManager.get("explosion.mp3", Sound.class);
		Sound zombieDeath = assetManager.get("zombie_dying.mp3", Sound.class);

		// Iterate through every active projectile
		Iterator<Projectiles> iterator = projectiles.iterator();
		while (iterator.hasNext()) {
			Projectiles projectile = iterator.next();
			Rectangle projectileBoundingBox = new Rectangle();
			projectileBoundingBox.set(projectile.getxCoordinate(), projectile.getyCoordinate(), projectile.getWidth(), projectile.getHeight());

			// Update location of flying projectiles
			projectile.updatexCoordinate();
			projectile.updateyCoordinate();
			projectile.getSprite().setPosition(projectile.getxCoordinate(), projectile.getyCoordinate());


			// Check if projectile is out of the map bounds
			if (projectile.getxCoordinate() < 0 || projectile.getxCoordinate() > mapMaxX ||
					projectile.getyCoordinate() < 0 || projectile.getyCoordinate() > mapMaxY
			){
				iterator.remove(); // Remove the projectile since it went out of bounds
			}
			// Check if projectile hits a player
			else if(projectile.getSourceTank() != player1Tank.getTankBoundingBox() && projectileBoundingBox.overlaps(player1Tank.getTankBoundingBox())){ // Check if a missile hits Player 1
				if(projectile instanceof Missile) {
					player1Tank.reduceHP(2);
				}
				else if(projectile instanceof UpgradedMissile){
					player1Tank.reduceHP(3);
				}
				Explosion newExplosion = new Explosion(player1Tank.getxCoordinate(), player1Tank.getyCoordinate());
				explosions.add(newExplosion);
				explosionSound.play();
				iterator.remove();
			}
			else if(projectile.getSourceTank() != player2Tank.getTankBoundingBox() && projectileBoundingBox.overlaps(player2Tank.getTankBoundingBox())){ // Check if a missile hits Player 2
				if(projectile instanceof Missile) {
					player2Tank.reduceHP(2);
				}
				else if(projectile instanceof UpgradedMissile){
					player2Tank.reduceHP(3);
				}
				Explosion newExplosion = new Explosion(player2Tank.getxCoordinate(), player2Tank.getyCoordinate());
				explosions.add(newExplosion);
				explosionSound.play();
				iterator.remove();
			}
			// Check if projectile hits a natural obstacle
			else {
				boolean collidedWithObstacle = false;
				Iterator<Rectangle> obstacleIterator = obstacles.iterator();
				while (obstacleIterator.hasNext() && !collidedWithObstacle) {
					Rectangle obstacle = obstacleIterator.next();
					if (projectileBoundingBox.overlaps(obstacle)) {
						collidedWithObstacle = true;
						explosionSound.play();
						Explosion newExplosion = new Explosion(projectile.getxCoordinate()-32, projectile.getyCoordinate()-15);
						explosions.add(newExplosion);
					}
				}
				// Check if projectile hits a wall
				Iterator<Wall> wallIterator = walls.iterator();
				while (wallIterator.hasNext() && !collidedWithObstacle) {
					Wall wall = wallIterator.next();
					if (projectileBoundingBox.overlaps(wall.getWallBoundingBox())) {
						collidedWithObstacle = true;
						explosionSound.play();
						Explosion newExplosion = new Explosion(projectile.getxCoordinate()-32, projectile.getyCoordinate()-15);
						explosions.add(newExplosion);
						if(projectile instanceof Missile){
							wall.reduceHP(1);
						}
						else if(projectile instanceof  UpgradedMissile){
							wall.reduceHP(2);
						}
						if(wall.getHP() <= 0){
							wallIterator.remove();
						}
					}
				}
				if (collidedWithObstacle) {
					iterator.remove();
				} else { // Check if projectile hits a zombie
					Iterator<Zombie> zombieIterator = zombies.iterator();
					while (zombieIterator.hasNext()) {
						Zombie zombie = zombieIterator.next();
						Rectangle zombieBoundingBox = zombie.getZombieBoundingBox();
						if (projectileBoundingBox.overlaps(zombieBoundingBox)) {
							explosionSound.play();
							zombie.reduceHealth(projectile);
							hitZombie = true;
							Explosion newExplosion = new Explosion(zombie.getxCoordinate(), zombie.getyCoordinate());
							explosions.add(newExplosion);
						}
						if (zombie.getHP() <= 0) {
							zombieDeath.play();
							zombieIterator.remove();
						}
					}
				}
				if (hitZombie) {
					iterator.remove();
				}
				if(tanks.size() == 3){
					if(projectile.getSourceTank() != enemyTank.getTankBoundingBox() && projectileBoundingBox.overlaps(enemyTank.getTankBoundingBox())){ // Check if a missile hits Player 2
						if(projectile instanceof Missile) {
							enemyTank.reduceHP(2);
						}
						else if(projectile instanceof UpgradedMissile){
							enemyTank.reduceHP(3);
						}
						Explosion newExplosion = new Explosion(enemyTank.getxCoordinate() - 15, enemyTank.getyCoordinate() - 10);
						explosions.add(newExplosion);
						explosionSound.play();
						iterator.remove();
					}
				}
			}
		}
	}
	public void createObstacles(){
		// Safehouse
		Rectangle safeHouseEastWall = new Rectangle();
		safeHouseEastWall.set(100, 0, 8, 165);
		obstacles.add(safeHouseEastWall);
		Rectangle safeHouseNorthWall = new Rectangle();
		safeHouseNorthWall.set(0, 310, 115, 4);
		obstacles.add(safeHouseNorthWall);

		// Desert cliffs
		Rectangle westCliff = new Rectangle();
		westCliff.set(812, 0, 4, 330);
		obstacles.add(westCliff);
		Rectangle northCliff = new Rectangle();
		northCliff.set(924, 437, 800, 9);
		obstacles.add(northCliff);

		// Water
		Rectangle northWater = new Rectangle();
		northWater.set(1455, 923, 120, 640);
		obstacles.add(northWater);
		Rectangle southEastWater = new Rectangle();
		southEastWater.set(1708, 430, 345, 221);
		obstacles.add(southEastWater);
		Rectangle northEastWater = new Rectangle();
		northEastWater.set(1945, 810, 117, 260);
		obstacles.add(northEastWater);
		Rectangle eastWater = new Rectangle();
		eastWater.set(1847, 633, 212, 140);
		obstacles.add(eastWater);

		// Forest
		Rectangle westForest = new Rectangle();
		westForest.set(0, 1066, 72, 472);
		obstacles.add(westForest);
		Rectangle westForest2 = new Rectangle();
		westForest2.set(55, 1074, 62, 121);
		obstacles.add(westForest2);
		Rectangle islandForest = new Rectangle();
		islandForest.set(284, 1229, 215, 125);
		obstacles.add(islandForest);
		Rectangle islandForest2 = new Rectangle();
		islandForest2.set(242, 1254, 46, 37);
		obstacles.add(islandForest2);
		Rectangle islandForest3 = new Rectangle();
		islandForest3.set(499, 1273, 45, 54);
		obstacles.add(islandForest3);
		Rectangle northwestForest = new Rectangle();
		northwestForest.set(55, 1360, 45, 178);
		obstacles.add(northwestForest);
		Rectangle northwest2Forest = new Rectangle();
		northwest2Forest.set(94, 1462, 56, 53);
		obstacles.add(northwest2Forest);
		Rectangle northForest = new Rectangle();
		northForest.set(92, 1510, 1360, 26);
		obstacles.add(northForest);
		Rectangle northForest2 = new Rectangle();
		northForest2.set(570, 1457, 500, 60);
		obstacles.add(northForest2);
		Rectangle northForest3 = new Rectangle();
		northForest3.set(681, 1412, 214, 50);
		obstacles.add(northForest3);
		Rectangle northForest4 = new Rectangle();
		northForest4.set(727, 1378, 55, 37);
		obstacles.add(northForest4);
		Rectangle southWestForest = new Rectangle();
		southWestForest.set(0, 986, 652, 94);
		obstacles.add(southWestForest);
		Rectangle southWestHelperForest = new Rectangle();
		southWestHelperForest.set(0, 945, 104, 45);
		obstacles.add(southWestHelperForest);
		Rectangle southWestHelper2Forest = new Rectangle();
		southWestHelper2Forest.set(0, 849, 33, 111);
		obstacles.add(southWestHelper2Forest);
		Rectangle westEntranceForest = new Rectangle();
		westEntranceForest.set(594, 920, 61, 208);
		obstacles.add(westEntranceForest);
		Rectangle westBottomEntranceForest = new Rectangle();
		westBottomEntranceForest.set(466, 947, 129, 47);
		obstacles.add(westBottomEntranceForest);
		Rectangle westTopEntranceForest = new Rectangle();
		westTopEntranceForest.set(542, 1076, 55, 24);
		obstacles.add(westTopEntranceForest);
		Rectangle westTop2EntranceForest = new Rectangle();
		westTop2EntranceForest.set(609, 1122, 31, 43);
		obstacles.add(westTop2EntranceForest);
		Rectangle westTop3EntranceForest = new Rectangle();
		westTop3EntranceForest.set(653, 1059, 39, 47);
		obstacles.add(westTop3EntranceForest);
		Rectangle eastForest = new Rectangle();
		eastForest.set(1374, 856, 87, 509);
		obstacles.add(eastForest);
		Rectangle eastTopForest = new Rectangle();
		eastTopForest.set(1404, 1365, 50, 176);
		obstacles.add(eastTopForest);
		Rectangle eastMidForest = new Rectangle();
		eastMidForest.set(1037, 1256, 358, 105);
		obstacles.add(eastMidForest);
		Rectangle eastMid2Forest = new Rectangle();
		eastMid2Forest.set(1004, 1310, 36, 44);
		obstacles.add(eastMid2Forest);
		Rectangle eastMid3Forest = new Rectangle();
		eastMid3Forest.set(1315, 1211, 59, 48);
		obstacles.add(eastMid3Forest);
		Rectangle southEastForest = new Rectangle();
		southEastForest.set(857, 981, 521, 96);
		obstacles.add(southEastForest);
		Rectangle southEast2Forest = new Rectangle();
		southEast2Forest.set(1120, 950, 257, 35);
		obstacles.add(southEast2Forest);
		Rectangle southEast3Forest = new Rectangle();
		southEast3Forest.set(1330, 892, 40, 60);
		obstacles.add(southEast3Forest);
		Rectangle eastEntranceForest = new Rectangle();
		eastEntranceForest.set(856, 1073, 34, 152);
		obstacles.add(eastEntranceForest);
		Rectangle eastEntrance2Forest = new Rectangle();
		eastEntrance2Forest.set(890, 1074, 44, 120);
		obstacles.add(eastEntrance2Forest);
		Rectangle eastEntrance3Forest = new Rectangle();
		eastEntrance3Forest.set(932, 1074 , 123, 69);
		obstacles.add(eastEntrance3Forest);
		Rectangle eastEntrance4Forest = new Rectangle();
		eastEntrance4Forest.set(840, 900 , 29, 95);
		obstacles.add(eastEntrance4Forest);
		Rectangle eastEntrance5Forest = new Rectangle();
		eastEntrance5Forest.set(870, 930 , 42, 52);
		obstacles.add(eastEntrance5Forest);

		// Vertical Walls
		int[] vWallxCoordinates = new int[] {262, 510, 734, 189};
		int[] vWallyCoordinates = new int[] {162, 0, 421, 817};
		for(int i = 0; i < vWallxCoordinates.length; i++){
			VerticalWall vWall = new VerticalWall(vWallxCoordinates[i], vWallyCoordinates[i]);
			walls.add(vWall);
		}

		// Horizontal Walls
		int[] hWallxCoordinates = new int[] {393, 674, 1103};
		int[] hWallyCoordinates = new int[] {619, 856, 709};
		for(int i = 0; i < hWallxCoordinates.length; i++){
			HorizontalWall hWall = new HorizontalWall(hWallxCoordinates[i], hWallyCoordinates[i]);
			walls.add(hWall);
		}
	}
	public void createTanks(){

		// If restarting the game, reset the tanks
		if(!tanks.isEmpty()){
			tanks.remove(player1Tank);
			tanks.remove(player2Tank);
		}

		player1Tank = new Tank(5, 5, player1TankTexture);
		player2Tank = new Tank(40, 5, player2TankTexture);
		enemyTank = new Tank(1990, 1494, enemyTankTexture);
		enemyTank.setRotationSpeed(4);
		if(brutalMode){
			enemyTank.setHP(300);
		}
		else {
			enemyTank.setHP(200);
		}
		enemyTank.setDirection(0);
		enemyTank.setLives(1);
		enemyTank.setDirection(225);
		enemyTank.getTankSprite().setRotation(enemyTank.getDirection());

		tanks.add(player1Tank);
		tanks.add(player2Tank);
		tanks.add(enemyTank);
	} // When a new Gameplay instance is created, new tanks are created as well

	public List<Tank> getTanks(){
		return tanks;
	}
	private void loadExplosionSprites(){
		explosionImage = new Texture("explosion1.png");
		int explosionFrameHeight = 100;
		int explosionFrameWidth = 100;
		TextureRegion[][] explosionFramesParsed = TextureRegion.split(explosionImage, explosionFrameWidth, explosionFrameHeight);
		int frame = 0;

		// Creating a single Animation object from the explosionFramesParsed
		TextureRegion[] explosionFrames = new TextureRegion[25];
		for (TextureRegion[] textureRegions : explosionFramesParsed) {
			for (TextureRegion textureRegion : textureRegions) {
				explosionFrames[frame] = textureRegion;
				frame++;
			}
		}
		explosion = new Animation<>(0.04f, explosionFrames);
		explosion.setPlayMode(Animation.PlayMode.LOOP);
	}
	private void loadSounds(){
		assetManager = new AssetManager();
		assetManager.load("explosion.mp3", Sound.class);
		assetManager.load("missile_shot.mp3", Sound.class);
		assetManager.load("metal_knock.mp3", Sound.class);
		assetManager.load("background_music.mp3", Sound.class);
		assetManager.load("zombie_eating.mp3", Sound.class);
		assetManager.load("zombie_chasing.mp3", Sound.class);
		assetManager.load("zombie_dying.mp3", Sound.class);
		assetManager.finishLoading();
		Sound backgroundMusic = assetManager.get("background_music.mp3", Sound.class);
		backgroundMusic.play();
	}
	private void loadTexturesAndSprites(){
		// Load the tank sprites
		player1TankTexture = new Texture("tank1.jpg");
		player2TankTexture = new Texture("tank2.jpg");
		enemyTankTexture = new Texture("tank3.jpg");

		batch = new SpriteBatch();
		missile = new Texture("missile.jpg");
		missile2 = new Texture("missile2.jpg");
		mapImage = new Texture("map.jpg");
		menuImage = new Texture("badlogic.jpg");
		PlayerOneUIBackground = new Texture("PlayerOneUIBackground.png");
		PlayerTwoUIBackground = new Texture("PlayerTwoUIBackground.png");
		loadZombieSprites();
		loadExplosionSprites();
	}
	private void loadZombieSprites() {
		zombieSpriteSheet = new Texture("zombie.png");
		// All zombie-related content (textures, frames, animations, etc.)
		int zombieFrameWidth = 128;
		int zombieFrameHeight = 128;
		TextureRegion[][] zombieFrames = TextureRegion.split(zombieSpriteSheet, zombieFrameWidth, zombieFrameHeight);
		int frame;

		// Creating Animation objects from all the corresponding textures (16)
		// Zombie walking textures (8)
		TextureRegion[] zombieDLWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieDLWalkingTextures[col] = zombieFrames[0][col];
		}
		zombieDLWalkingAnimation = new Animation<>(200f, zombieDLWalkingTextures);
		zombieDLWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieLWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieLWalkingTextures[col] = zombieFrames[1][col];
		}
		zombieLWalkingAnimation = new Animation<>(200f, zombieLWalkingTextures);
		zombieLWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieULWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieULWalkingTextures[col] = zombieFrames[2][col];
		}
		zombieULWalkingAnimation = new Animation<>(200f, zombieULWalkingTextures);
		zombieULWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieUWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieUWalkingTextures[col] = zombieFrames[3][col];
		}
		zombieUWalkingAnimation = new Animation<>(200f, zombieUWalkingTextures);
		zombieUWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);


		TextureRegion[] zombieURWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieURWalkingTextures[col] = zombieFrames[4][col];
		}
		zombieURWalkingAnimation = new Animation<>(200f, zombieURWalkingTextures);
		zombieURWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieRWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieRWalkingTextures[col] = zombieFrames[5][col];
		}
		zombieRWalkingAnimation = new Animation<>(200f, zombieRWalkingTextures);
		zombieRWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieDRWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieDRWalkingTextures[col] = zombieFrames[6][col];
		}
		zombieDRWalkingAnimation = new Animation<>(200f, zombieDRWalkingTextures);
		zombieDRWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);

		TextureRegion[] zombieDWalkingTextures = new TextureRegion[4];
		for (int col = 0; col < 4; col++) {
			zombieDWalkingTextures[col] = zombieFrames[7][col];
		}
		zombieDWalkingAnimation = new Animation<>(200f, zombieDWalkingTextures);
		zombieDWalkingAnimation.setPlayMode(Animation.PlayMode.LOOP);


		// Now to load the running textures... 8 left

		// Zombie running textures (8)
		TextureRegion[] zombieDLRunningTextures = new TextureRegion[8];
		frame = 0;
		for (int col = 4; col < 12; col++) {
			zombieDLRunningTextures[frame] = zombieFrames[0][col];
			frame++;
		}
		zombieDLRunningAnimation = new Animation<>(80f, zombieDLRunningTextures);
		zombieDLRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieLRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieLRunningTextures[frame] = zombieFrames[1][col];
			frame++;
		}
		zombieLRunningAnimation = new Animation<>(80f, zombieLRunningTextures);
		zombieLRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieULRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieULRunningTextures[frame] = zombieFrames[2][col];
			frame++;

		}
		zombieULRunningAnimation = new Animation<>(80f, zombieULRunningTextures);
		zombieULRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieURunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieURunningTextures[frame] = zombieFrames[3][col];
			frame++;
		}
		zombieURunningAnimation = new Animation<>(80f, zombieURunningTextures);
		zombieURunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieURRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieURRunningTextures[frame] = zombieFrames[4][col];
			frame++;
		}
		zombieURRunningAnimation = new Animation<>(80f, zombieURRunningTextures);
		zombieURRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieRRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieRRunningTextures[frame] = zombieFrames[5][col];
			frame++;
		}
		zombieRRunningAnimation = new Animation<>(80f, zombieRRunningTextures);
		zombieRRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieDRRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieDRRunningTextures[frame] = zombieFrames[6][col];
			frame++;
		}
		zombieDRRunningAnimation = new Animation<>(80f, zombieDRRunningTextures);
		zombieDRRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

		frame = 0;
		TextureRegion[] zombieDRunningTextures = new TextureRegion[8];
		for (int col = 4; col < 12; col++) {
			zombieDRunningTextures[frame] = zombieFrames[7][col];
			frame++;
		}
		zombieDRunningAnimation = new Animation<>(80f, zombieDRunningTextures);
		zombieDRunningAnimation.setPlayMode(Animation.PlayMode.LOOP);

	}
	public void moveTank(Tank currentPlayer, float player1NewY, float player1NewX){
		if(!willCollideWithObstacles(currentPlayer.getTankBoundingBox(), player1NewX, player1NewY)){
			currentPlayer.setxCoordinate(player1NewX);
			currentPlayer.setyCoordinate(player1NewY);
			currentPlayer.getTankSprite().setPosition(currentPlayer.getxCoordinate(), currentPlayer.getyCoordinate());
			currentPlayer.getTankBoundingBox().setPosition(currentPlayer.getxCoordinate(), currentPlayer.getyCoordinate());
		}
	}
	public boolean moveZombieIfPossible(Zombie zombie){
		if(!willCollideWithObstacles(zombie.getZombieBoundingBox(), zombie.potentialXMove(), zombie.potentialYMove())){
			zombie.move(zombie.potentialXMove(), zombie.potentialYMove());
			return true;
		}
		return false;
	}
	public float newXPosition(Tank currentTank, Sprite currentSprite, int upOrDown){
		float currentXPosition = currentTank.getxCoordinate();
		float newXPosition;
		if(currentSprite.getRotation() == 0 || currentSprite.getRotation() == 180){
			return currentTank.getxCoordinate();
		}
		else if((currentSprite.getRotation() > 180 && currentSprite.getRotation() < 360) && upOrDown == 1){
			newXPosition = (float) (currentTank.getxCoordinate() + (currentTank.getSpeed() * Math.cos(Math.toRadians(currentSprite.getRotation() + 90))));

		}
		else if((currentSprite.getRotation() < 180 && currentSprite.getRotation() > 0) && upOrDown == 1){
			newXPosition = (float) (currentTank.getxCoordinate() + (currentTank.getSpeed() * Math.cos(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		else if((currentSprite.getRotation() < 180 && currentSprite.getRotation() > 0) && upOrDown == 0){
			newXPosition = (float) (currentTank.getxCoordinate() - (currentTank.getSpeed() * Math.cos(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		else{
			newXPosition = (float) (currentTank.getxCoordinate() - (currentTank.getSpeed() * Math.cos(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		if(newXPosition > 2020 || newXPosition < 0){
			return currentXPosition;
		}
		else {
			return newXPosition;
		}
	}
	public float newYPosition(Tank currentTank, Sprite currentSprite, int upOrDown){
		float currentYPosition = currentTank.getyCoordinate();
		float newYPosition;
		if((currentSprite.getRotation() == 90 || currentSprite.getRotation() == 270 )){
			return currentTank.getyCoordinate();
		}
		else if((currentSprite.getRotation() < 90 || currentSprite.getRotation() > 270) && upOrDown == 1) {
			newYPosition = (float) (currentTank.getyCoordinate() + (currentTank.getSpeed() * Math.sin(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		else if((currentSprite.getRotation() > 90 && currentSprite.getRotation() < 270) && upOrDown == 1) {
			newYPosition = (float) (currentTank.getyCoordinate() + (currentTank.getSpeed() * Math.sin(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		else {
			newYPosition = (float) (currentTank.getyCoordinate() - (currentTank.getSpeed() * Math.sin(Math.toRadians(currentSprite.getRotation() + 90))));
		}
		if(newYPosition > 1484 || newYPosition < 0){
			return currentYPosition;
		}
		else {
			return newYPosition;
		}
	}
	public void obtainedIngredient1(){
		this.obtainedIngredient1 = true;
	}
	public void obtainedIngredient2(){
		this.obtainedIngredient2 = true;
	}
	public void obtainedIngredient3(){
		this.obtainedIngredient3 = true;
	}
	public void pressAnyKeyToContinue(){
		if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) ||
				Gdx.input.isButtonPressed(Input.Buttons.LEFT) ||
				Gdx.input.isButtonPressed(Input.Buttons.RIGHT) ||
				Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
			gameStateManager.setState(GameState.START_MENU);
		}
	}
	public void readInput(){
		adjustTankSpeedIfOnSand(player1Tank);
		adjustTankSpeedIfOnSand(player2Tank);

		player1Tank.getTankSprite().setOrigin(player1Tank.getTankSprite().getWidth() / 2, player1Tank.getTankSprite().getHeight() / 2);
		player2Tank.getTankSprite().setOrigin(player2Tank.getTankSprite().getWidth() / 2, player2Tank.getTankSprite().getHeight() / 2);

		// Reading input for Player 1:
		if (Gdx.input.isKeyPressed(Input.Keys.F) && !Gdx.input.isKeyPressed(Input.Keys.T) && !Gdx.input.isKeyPressed(Input.Keys.G)) {
			player1Tank.getTankSprite().rotate(player1Tank.getRotationSpeed());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.H) && !Gdx.input.isKeyPressed(Input.Keys.T) && !Gdx.input.isKeyPressed(Input.Keys.G)) {
			player1Tank.getTankSprite().rotate(-player1Tank.getRotationSpeed());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			if (Gdx.input.isKeyPressed(Input.Keys.F)) {
				player1Tank.getTankSprite().rotate(player1Tank.getRotationSpeed());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.H)) {
				player1Tank.getTankSprite().rotate(-player1Tank.getRotationSpeed());
			}
			float player1NewX = newXPosition(player1Tank, player1Tank.getTankSprite(), 1);
			float player1NewY = newYPosition(player1Tank, player1Tank.getTankSprite(), 1);
			moveTank(player1Tank, player1NewY, player1NewX);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.G)) {
			if (Gdx.input.isKeyPressed(Input.Keys.F)) {
				player1Tank.getTankSprite().rotate(-player1Tank.getRotationSpeed());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.H)) {
				player1Tank.getTankSprite().rotate(player1Tank.getRotationSpeed());
			}
			float player1NewX = newXPosition(player1Tank, player1Tank.getTankSprite(), 0);
			float player1NewY = newYPosition(player1Tank, player1Tank.getTankSprite(), 0);
			moveTank(player1Tank, player1NewY, player1NewX);
		}

		// Reading input for Player 2:
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player2Tank.getTankSprite().rotate(player2Tank.getRotationSpeed());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player2Tank.getTankSprite().rotate(-player2Tank.getRotationSpeed());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player2Tank.getTankSprite().rotate(player2Tank.getRotationSpeed());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				player2Tank.getTankSprite().rotate(-player2Tank.getRotationSpeed());
			}
			float player2NewX = newXPosition(player2Tank, player2Tank.getTankSprite(), 1);
			float player2NewY = newYPosition(player2Tank, player2Tank.getTankSprite(), 1);
			moveTank(player2Tank, player2NewY, player2NewX);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player2Tank.getTankSprite().rotate(-player2Tank.getRotationSpeed());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				player2Tank.getTankSprite().rotate(player2Tank.getRotationSpeed());
			}
			float player2NewX = newXPosition(player2Tank, player2Tank.getTankSprite(), 0);
			float player2NewY = newYPosition(player2Tank, player2Tank.getTankSprite(), 0);
			moveTank(player2Tank, player2NewY, player2NewX);
		}

		// Reset rotation if it exceeds +360 or -360
		if(player1Tank.getTankSprite().getRotation() > 360){
			player1Tank.getTankSprite().setRotation(0);
		}
		if(player2Tank.getTankSprite().getRotation() > 360){
			player2Tank.getTankSprite().setRotation(0);
		}
		if(player1Tank.getTankSprite().getRotation() < 0){
			player1Tank.getTankSprite().setRotation(360);
		}
		if(player2Tank.getTankSprite().getRotation() < 0){
			player2Tank.getTankSprite().setRotation(360);
		}

		// Sets the direction of the tank to new direction
		player1Tank.setDirection(player1Tank.getTankSprite().getRotation());
		player2Tank.setDirection(player2Tank.getTankSprite().getRotation());

		// Reads key input for shooting a projectile
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			addProjectile(player1Tank, missileCounter1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SEMICOLON)) {
			addProjectile(player2Tank, missileCounter2);
		}
	}
	public void renderAll(float elapsedTime){ // The purpose of this function is to prepare everything before rendering
		// Read both players' input
		readInput();

		// Update everything before rendering
		update();

		// Render Player 1's Camera View
		viewportPlayer1.apply();
		viewportPlayer1.setScreenPosition(0, 192);
		batch.setProjectionMatrix(cameraPlayer1.combined);
		batch.begin();
		renderHelperFunction();
		batch.end();

		// Render Player 1's User Interface
		viewportPlayer1UI.apply();
		viewportPlayer1UI.setScreenBounds(0, 0, 1200, 384);
		viewportPlayer1UI.setScreenPosition(-600,-192);
		batch.setProjectionMatrix(cameraPlayer1UI.combined);
		batch.begin();
		batch.draw(PlayerOneUIBackground, 0, 0);
		player1UI.renderHPandLivesOnUI(); // render lives and HP
		batch.end();

		// Render Player 2's Camera View
		viewportPlayer2.apply();
		viewportPlayer2.setScreenPosition(Gdx.graphics.getWidth() / 2, 192);
		batch.setProjectionMatrix(cameraPlayer2.combined);
		batch.begin();
		renderHelperFunction();
		batch.end();

		// Render Player 2's User Interface
		viewportPlayer2UI.apply();
		viewportPlayer2UI.setScreenBounds(0, 0, 1200, 384);
		viewportPlayer2UI.setScreenPosition(248,-192);
		batch.setProjectionMatrix(cameraPlayer2UI.combined);
		batch.begin();
		batch.draw(PlayerTwoUIBackground, 0, 0);
		player2UI.renderHPandLivesOnUI(); // render lives and HP
		batch.end();

		// Render the mini-map
		viewportMiniMap.apply();
		viewportMiniMap.setScreenPosition((Gdx.graphics.getWidth() / 2) - 384, -192);
		batch.setProjectionMatrix(cameraMiniMap.combined);
		miniMap.setBothMiniMapPositions(player1Tank, player2Tank);
		batch.begin();
		batch.draw(miniMap.getMapTexture(), 0, 0);
		miniMap.getTankIconSprite1().draw(batch);
		miniMap.getTankIconSprite2().draw(batch);
		batch.end();
	}
	public void renderHelperFunction(){ // Responsible for the actual drawing
		batch.draw(mapImage, 0, 0);

		// Draw all items
		for(CanBePickedUp item : items){
			batch.draw(item.getSprite(), item.getxCoordinate(), item.getyCoordinate());
		}

		// Draw all walls
		for(Wall wall : walls){
			wall.getSprite().draw(batch);
		}

		// Draw all active projectiles
		for(Projectiles projectile : projectiles){
			projectile.getSprite().draw(batch);
		}

		// Draw all tanks
		for(Tank tank : tanks){
			tank.getTankSprite().draw(batch);
		}

		// Draw all live zombies
		for(Zombie zombie : zombies){
			batch.draw(zombie.getCurrentFrame(), zombie.getxCoordinate(), zombie.getyCoordinate());
		}

		// Draw all active explosions
		for(Explosion explosion : explosions){
			explosion.setAlphaValue(explosion.getAlphaValue() - 0.025f);
			batch.setColor(1.0f, 1.0f, 1.0f, explosion.getAlphaValue());
			batch.draw(explosion.getCurrentFrame(), explosion.getxCoordinate(), explosion.getyCoordinate());
		}
		batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	public void reset(){
		createTanks();
		spawnZombies();
		spawnAllItems();
		setUpUI();
		obtainedIngredient1 = false;
		obtainedIngredient2 = false;
		obtainedIngredient3 = false;
		this.zombieSpawnRate = 500;
		this.numberOfZombiesPerSpawnBatch = 6;
		this.zombieSpeed = 1.35f;
		apocalypseActivated = false;
	}
	@Override
	public void resize(int width, int height) {
		int halfWidth = width / 2;

		viewportPlayer1.update(halfWidth, height, true);
		viewportPlayer2.update(halfWidth, height, true);

		cameraPlayer1.position.set((float) halfWidth / 2, (float) height / 2, 0);
		cameraPlayer2.position.set(halfWidth + (float) halfWidth / 2, (float) height / 2, 0);
	}
	public void respawnPlayer(Tank currentPlayer){
		currentPlayer.deleteLife();
		currentPlayer.setyCoordinate(5f);
		if(currentPlayer == player1Tank) {
			currentPlayer.setxCoordinate(5f);
		} else if(currentPlayer == player2Tank){
			currentPlayer.setxCoordinate(40f);
		}
		currentPlayer.setDirection(0);
		currentPlayer.getTankSprite().setPosition(currentPlayer.getxCoordinate(), currentPlayer.getyCoordinate());
		currentPlayer.getTankSprite().setRotation(0);
		if(currentPlayer.getLives() > 0){
			currentPlayer.setHP(100);
		}
		else{
			currentPlayer.setHP(1);
		}
	}
	public void rotateEnemyTankToPlayer(Tank targetTank){
		// Since the enemy tank only shoots in the 3rd quadrant of a circle, we will only use 180 to 270 degrees to fire projectiles
		// The enemy tank finds a ratio by finding the distance between itself and the player's tank, as well as the difference between its x-coordinate and the players' tanks
		// Using that ratio, the enemy tank then determines a direction between 180-270 degrees
		float distance = calculateDistanceFromBoss(targetTank);
		float deltaX = Math.abs(targetTank.getxCoordinate() - enemyTank.getxCoordinate());
		float ratio = distance/deltaX;
		float direction;

		// The enemy tank is not perfectly in the top-right corner, so players can be slightly to the "right" of the enemy. Shooting at 273 degrees will ensure a hit to the player.
		if(targetTank.getxCoordinate() > enemyTank.getxCoordinate()){
			direction = 273;
		}
		else { // Use ratios to handle extremities more easily (near 180 degrees and near 270 degrees)
			if (ratio > 1.00 && ratio < 1.01) {
				direction = 180;
			} else if (ratio > 1.01 && ratio < 1.02) {
				direction = 185;
			} else if (ratio > 1.02 && ratio < 1.03) {
				direction = 190;
			} else if (ratio > 3.9f && ratio < 6.25f) {
				direction = 260;
			} else if (ratio > 6.25f && ratio < 8.3f) {
				direction = 263;
			} else if (ratio > 8.3f && ratio < 12.5f) {
				direction = 266;
			} else if (ratio > 12.5f) {
				direction = 268;
			} else { // Otherwise, calculate a direction from the formula and adjust the direction a little bit
				direction = (-9.1217f * ratio * ratio) + (71.219f * ratio) + 132.61f;
				if (direction > 200 && direction < 240) {
					direction = direction + 5;
				} else if (direction > 250 && direction < 260) {
					direction = direction - 6;
				}
			}
		}
		enemyTank.setDirection(direction);
		enemyTank.getTankSprite().setRotation(enemyTank.getDirection());
	}
	public void setBrutalMode(boolean trueOrFalse){
		this.brutalMode = trueOrFalse;
		if(brutalMode){
			zombieSpawnRate = 360; // lower means faster spawn rate. Norm is 500
			zombieHP = 50;
			zombieSpeed = 1.5f;
		}
	}
	private void setUpCamerasAndViewports(){
		cameraPlayer1 = new OrthographicCamera();
		cameraPlayer2 = new OrthographicCamera();
		cameraPlayer1.position.set(cameraPlayer1.viewportWidth / 2, (cameraPlayer1.viewportHeight / 2), 0); // Player 1 camera is placed on left side of the screen
		cameraPlayer2.position.set(cameraPlayer1.viewportWidth + cameraPlayer2.viewportWidth / 2, (cameraPlayer2.viewportHeight / 2), 0); // Player 2 camera is placed on the right side of the screen
		cameraPlayer1.viewportHeight = Gdx.graphics.getHeight();
		cameraPlayer2.viewportHeight = Gdx.graphics.getHeight();
		cameraPlayer1.viewportWidth = Gdx.graphics.getWidth() / 2f;
		cameraPlayer2.viewportWidth = Gdx.graphics.getWidth() / 2f;
		cameraPlayer1UI = new OrthographicCamera();
		cameraPlayer2UI = new OrthographicCamera();
		cameraPlayer1UI.viewportHeight = Gdx.graphics.getHeight();
		cameraPlayer2UI.viewportHeight = Gdx.graphics.getHeight();
		cameraPlayer1UI.viewportWidth = Gdx.graphics.getWidth() / 2f;
		cameraPlayer2UI.viewportWidth = Gdx.graphics.getWidth() / 2f;
		viewportPlayer1 = new FitViewport(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight(), cameraPlayer1);
		viewportPlayer2 = new FitViewport(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight(), cameraPlayer2);
		viewportPlayer1UI = new FitViewport(Gdx.graphics.getWidth() / 2f, 192, cameraPlayer1UI);
		viewportPlayer2UI = new FitViewport(Gdx.graphics.getWidth() / 2f, 192, cameraPlayer2UI);

	}
	private void setUpMiniMap(){
		cameraMiniMap = new OrthographicCamera();
		viewportMiniMap = new FitViewport(512, 384, cameraMiniMap);
		viewportMiniMap.setScreenBounds((Gdx.graphics.getWidth() / 2), 0, 512, 384);
		miniMap = new MiniMap(this);
	}
	public void setUpUI(){ 		// Set up both players' user interface data
		player1UI = new UserInterface(player1Tank, batch);
		player2UI = new UserInterface(player2Tank, batch);
	}
	public void spawnAllItems(){
		Ingredient1 ingredient1 = new Ingredient1();
		Ingredient3 ingredient3 = new Ingredient3();
		DoubleFirepower doubleFirepower = new DoubleFirepower();
		ExtraLife extraLife = new ExtraLife();
		Invincibility invincibility = new Invincibility();
		HPBoost hpBoost = new HPBoost();
		IncreasedSpeed increasedSpeed = new IncreasedSpeed();
		items.add(ingredient1);
		items.add(ingredient3);
		items.add(doubleFirepower);
		items.add(extraLife);
		items.add(invincibility);
		items.add(hpBoost);
		items.add(increasedSpeed);
	}
	public void spawnZombies(){
		// Spawn zombies
		for(int i = 0; i < 50; i++){ // Original: 50
			Zombie newZombie = new Zombie(player1Tank, player2Tank,zombieSpeed, zombieHP, zombieUWalkingAnimation, zombieULWalkingAnimation, zombieLWalkingAnimation, zombieDLWalkingAnimation, zombieDWalkingAnimation, zombieDRWalkingAnimation,    zombieRWalkingAnimation, zombieURWalkingAnimation, zombieURunningAnimation, zombieULRunningAnimation, zombieLRunningAnimation, zombieDLRunningAnimation, zombieDRunningAnimation, zombieDRRunningAnimation, zombieRRunningAnimation, zombieURRunningAnimation, obstacles, walls);
			zombies.add(newZombie);
		}
	}
	public void update(){
		// Update all counters
		updateAllCounters();

		// Reset hitZombie
		hitZombie = false;

		// Activate the enemy tank AI's ability to target nearby players
		if(tanks.size() == 3) {
			activateEnemyTankAI();
		}

		// Constantly spawn new zombies onto the map
		if(respawnZombieCounter > zombieSpawnRate){
			for(int i = 0; i < numberOfZombiesPerSpawnBatch; i++){
				Zombie newZombie = new Zombie(player1Tank, player2Tank,zombieSpeed, zombieHP, zombieUWalkingAnimation, zombieULWalkingAnimation, zombieLWalkingAnimation, zombieDLWalkingAnimation, zombieDWalkingAnimation, zombieDRWalkingAnimation, zombieRWalkingAnimation, zombieURWalkingAnimation, zombieURunningAnimation, zombieULRunningAnimation, zombieLRunningAnimation, zombieDLRunningAnimation, zombieDRunningAnimation, zombieDRRunningAnimation, zombieRRunningAnimation, zombieURRunningAnimation, obstacles, walls);
				zombies.add(newZombie);
				respawnZombieCounter = 0; // Reset
			}
		}
		else{
			respawnZombieCounter++;
		}

		// Move zombies where they are supposed to
		updateZombieMovements();

		// Check if projectiles come into contact with anything
		checkIfProjectileHits();

		// Check if any tanks has lost a life. If so, respawn.
		if(player1Tank.getHP() <= 0){
			respawnPlayer(player1Tank);
		}
		if(player2Tank.getHP() <= 0){
			respawnPlayer(player2Tank);
		}
		if(enemyTank.getHP() <= 0){
			enemyTank.deleteLife();
		}

		// If the enemy tank has been defeated, it will be deleted and ingredient 2 will be dropped
		if(!obtainedIngredient2){
			if(enemyTank.getLives() == 0){
				Ingredient2 ingredient2 = new Ingredient2(enemyTank.getxCoordinate(), enemyTank.getyCoordinate());
				items.add(ingredient2);
				tanks.remove(enemyTank);
			}
		}

		// Check if both players have lost all lives. If so, end the game.
		if(player1Tank.getLives() <= 0 && player2Tank.getLives() <= 0){
			gameStateManager.setState(GameState.GAME_OVER_LOSE);
		}

		// Check if players have picked up an item
		Iterator<CanBePickedUp> itemIterator = items.iterator();
		while(itemIterator.hasNext()){
			CanBePickedUp item = itemIterator.next();
			if(player1Tank.getTankBoundingBox().overlaps(item.getBoundingBox())){
				item.performAction(player1Tank, this);
				itemIterator.remove();
			}
			if(player2Tank.getTankBoundingBox().overlaps(item.getBoundingBox())){
				item.performAction(player2Tank, this);
				itemIterator.remove();
			}
		}

		// Check if all ingredients have been attained. If so, then activate the apocalypse
		if(!apocalypseActivated) {
			allIngredientsObtained();
		}

		// Check if players have lost
		if(player1Tank.getLives() == 0 && player2Tank.getLives() == 0){
			changeGameState(GameState.GAME_OVER_LOSE);
		}

		// Check if game has been won
		if(wonGame()){
			changeGameState(GameState.GAME_OVER_WIN);
		}

		// Update cameras and viewports
		updateCameras();

		// Prepare animations for rendering
		updateZombieAnimations(elapsedTime);
		updateExplosionAnimations(elapsedTime);
	}
	private void updateAllCounters(){
		// Delays the ability to shoot a missile (so you can't spam missiles)
		missileCounter1++;
		missileCounter2++;
		missileCounter3++;

		// Delays the timing of when a player can take damage, so zombies don't spam kill the tanks when near
		zombieCounter1++;
		zombieCounter2++;

		// Increment explosion timers so they can fade away in time
		Iterator<Explosion> explosionIterator = explosions.iterator();
		while(explosionIterator.hasNext()){
			Explosion explosion = explosionIterator.next();
			explosion.incrementTimer();
			if(explosion.getTimerValue() > 60){
				explosionIterator.remove();
			}
		}

		// If a player has a temporary power-up (such as invincibility), then decrement the timer in the Tank class
		if(player1Tank.isTempPowerUpActive()){
			player1Tank.reduceTimer();
			if(player1Tank.getPowerUpCounter() <= 0){
				player1Tank.setTempPowerUpActive();
			}
		}
		if(player2Tank.isTempPowerUpActive()){
			player2Tank.reduceTimer();
			if(player2Tank.getPowerUpCounter() <= 0){
				player2Tank.setTempPowerUpActive();
			}
		}
	}
	private void updateCameras() {
		// Player 1 Camera Follows Tank 1
		cameraPlayer1.position.set(player1Tank.getxCoordinate() + player1Tank.getTankSprite().getWidth() / 2,
				(player1Tank.getyCoordinate() + player1Tank.getTankSprite().getHeight() / 2) + 96, 0);
		// Limit the camera within the map bounds
		cameraPlayer1.position.x = MathUtils.clamp(cameraPlayer1.position.x, cameraPlayer1.viewportWidth / 2, mapMaxX - cameraPlayer1.viewportWidth / 2);
		cameraPlayer1.position.y = MathUtils.clamp(cameraPlayer1.position.y, cameraPlayer1.viewportHeight / 2, (mapMaxY - cameraPlayer1.viewportHeight / 2) + 192);
		cameraPlayer1.update();

		// Player 2 Camera Follows Tank 2
		cameraPlayer2.position.set(player2Tank.getxCoordinate() + player2Tank.getTankSprite().getWidth() / 2,
				(player2Tank.getyCoordinate() + player2Tank.getTankSprite().getHeight() / 2) + 96, 0);
		// Limit the camera within the map bounds
		cameraPlayer2.position.x = MathUtils.clamp(cameraPlayer2.position.x, cameraPlayer2.viewportWidth / 2, mapMaxX - cameraPlayer2.viewportWidth / 2);
		cameraPlayer2.position.y = MathUtils.clamp(cameraPlayer2.position.y, cameraPlayer2.viewportHeight / 2, (mapMaxY - cameraPlayer2.viewportHeight / 2) + 192);
		cameraPlayer2.update();
	}
	public void updateExplosionAnimations(float stateTime){
		for(Explosion explosion1 : explosions){
			TextureRegion currentFrame = explosion.getKeyFrame(stateTime);
			explosion1.setCurrentFrame(currentFrame);
		}
	}
	public void updateZombieAnimations(float deltaTime){
		for(Zombie zombie : zombies){
			zombie.updateStateTime(deltaTime);
			zombie.updateAnimation();
		}
	}
	public void updateZombieMovements(){
		// Load sounds to play
		Sound zombieChase = assetManager.get("zombie_chasing.mp3", Sound.class);
		Sound zombieMunch = assetManager.get("zombie_eating.mp3", Sound.class);

		// Update location of zombies && check for collisions with tanks and walls
		for(Zombie zombie : zombies){
			if (zombie.checkIfPlayersAreInSafeZone(player1Tank, player2Tank)) { // Check if both players or current target is in a safe zone
				zombie.idleWalking();
			}
			else if(zombie.hasTarget()) { // Zombie already has a target, but needs to see if that player is in the safe zone
				if(zombie.checkIfTargetIsInSafeZone()){
					zombie.idleWalking();
				}
				else{
					zombie.findDirectionToPlayer();
				}
			}
			else{ // Zombie has no target
				zombie.scanForPlayers(player1Tank, player2Tank);
				if(zombie.hasTarget()){ // Found a player nearby to chase
					zombie.findDirectionToPlayer();
					zombieChase.play();
				}
				else{
					zombie.idleWalking(); // Cannot find any players nearby
				}
			}

			if(!moveZombieIfPossible(zombie)){
				if(zombie.hasTarget()) {
					// Check diagonals with an up component
					if (zombie.getDirection() == 2 || zombie.getDirection() == 8) {
						if (Math.abs(zombie.getyCoordinate() - zombie.getTarget().getyCoordinate()) > Math.abs(zombie.getxCoordinate() - zombie.getTarget().getxCoordinate())) {
							zombie.setDirection(1); // Attempt to go up
							moveZombieIfPossible(zombie);
						} else {
							if (zombie.getDirection() == 2) {
								zombie.setDirection(3); // Attempt to go left
								moveZombieIfPossible(zombie);
							} else {
								zombie.setDirection(7); // Attempt to go right
								moveZombieIfPossible(zombie);
							}
						}
						// Check diagonals with a down component
					} else if (zombie.getDirection() == 4 || zombie.getDirection() == 6) {
						if (Math.abs(zombie.getyCoordinate() - zombie.getTarget().getyCoordinate()) > Math.abs(zombie.getxCoordinate() - zombie.getTarget().getxCoordinate())) {
							zombie.setDirection(5); // Attempt to go down
							moveZombieIfPossible(zombie);
						} else {
							if (zombie.getDirection() == 4) {
								zombie.setDirection(3); // Attempt to go left
								moveZombieIfPossible(zombie);
							} else {
								zombie.setDirection(7); // Attempt to go right
								moveZombieIfPossible(zombie);
							}
						}
					}
					// Check if the zombie can freely move up or down, assuming collision is left or right of the zombie
					else if(zombie.getDirection() == 3 || zombie.getDirection() == 7){
						if(zombie.getTarget().getyCoordinate() - zombie.getyCoordinate() > 0){
							zombie.setDirection(1);
							moveZombieIfPossible(zombie);
						}
						else{
							zombie.setDirection(5);
							moveZombieIfPossible(zombie);
						}
					}
					// Lastly, check if the zombie can freely move left or right, assuming the obstacle is up or below the zombie
					else{
						if(zombie.getTarget().getxCoordinate() - zombie.getxCoordinate() > 0){
							zombie.setDirection(7);
							moveZombieIfPossible(zombie);
						}
						else{
							zombie.setDirection(3);
							moveZombieIfPossible(zombie);
						}
					}
				}
			}


			// Check for zombie collisions with tanks
			Sound clank = assetManager.get("metal_knock.mp3", Sound.class);
			if(zombie.getZombieBoundingBox().overlaps(player1Tank.getTankBoundingBox()) && zombieCounter1 > 30){
				clank.play();
				zombieMunch.play();
				player1Tank.reduceHP(1);
				zombieCounter1 = 0;
			}
			if(zombie.getZombieBoundingBox().overlaps(player2Tank.getTankBoundingBox()) && zombieCounter2 > 30){
				clank.play();
				zombieMunch.play();
				player2Tank.reduceHP(1);
				zombieCounter2 = 0;
			}
		}
	}
	public boolean willCollideWithObstacles(Rectangle object, float x, float y){
		Rectangle potentialNewPosition = object;
		potentialNewPosition.setPosition(x, y);
		boolean willCollide = false;
		for(Rectangle obstacle : obstacles){
			if(potentialNewPosition.overlaps(obstacle)){
				willCollide = true;
			}
		}
		for(Wall wall : walls){
			if(potentialNewPosition.overlaps(wall.getWallBoundingBox())){
				willCollide = true;
			}
		}
		return willCollide;
	}
	public boolean wonGame(){
		return obtainedIngredient1 && obtainedIngredient2 && obtainedIngredient3 && (player1Tank.getxCoordinate() < 100) && (player1Tank.getyCoordinate() < 305) && (player2Tank.getxCoordinate() < 100) && (player2Tank.getyCoordinate() < 305);
	}
}
