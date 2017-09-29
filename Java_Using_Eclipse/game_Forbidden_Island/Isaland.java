// Assignment 9 part 2
// lei bowen
// bowenleis
// Zhu Xiang
// zhuxiang

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//Represents a single square of the game area
class Cell {

  // represents absolute height of this cell, in feet
  double height;
  // In logical coordinates, with the origin at the top-left corner of the
  // screen
  int x;
  int y;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  // reports whether this cell is flooded or not
  boolean isFlooded;

  Cell(double height, int x, int y, boolean isFlooded) {
    this(height, x, y, null, null, null, null, isFlooded);
  }

  Cell(double height, int x, int y, Cell left, Cell top, Cell right, Cell bottom,
      boolean isFlooded) {
    this.height = height;
    this.x = x;
    this.y = y;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.isFlooded = isFlooded;
  }

  // produce the image of this cell
  WorldImage getImage(ForbiddenIslandWorld w) {
    int widthC = ForbiddenIslandWorld.CELLWIDTH;
    int heightC = ForbiddenIslandWorld.CELLHEIGHT;
    int aboveWater = Math.max(0, (int) (this.height - w.waterHeight));
    int mergerColor = Math.max(0, (255 - (int) (w.waterHeight - this.height)));
    int underWaterRed = Math.min(255, Math.abs((int) (w.waterHeight - this.height)));
    int underWaterGreed = Math.max(0, (255 - (int) (w.waterHeight - this.height)));

    // Cells that are above water
    // should be rendered from green to white
    if (!this.isFlooded && this.height > w.waterHeight) {
      return new RectangleImage(widthC, heightC, OutlineMode.SOLID,
          new Color(aboveWater, 255, aboveWater));

    }
    // Cells that are below the current water level, but that are not flooded,
    // should be rendered on a scale from green (meaning just below the water
    // level) to red
    else if (!this.isFlooded && this.height <= w.waterHeight) {
      return new RectangleImage(widthC, heightC, OutlineMode.SOLID,
          new Color(underWaterRed, underWaterGreed, 0));
    }

    // Cells that are flooded should be rendered from blue (meaning just below
    // the water) to black
    return new RectangleImage(widthC, heightC, OutlineMode.SOLID, new Color(0, 0, mergerColor));

  }

  // judge and change the isFlooded according to this cell's surrounding cells
  // and its height comparing with the given water height
  void changeFlood(int waterHeight) {
    if ((this.height <= waterHeight) && (this.left.isFlooded || this.right.isFlooded
        || this.bottom.isFlooded || this.top.isFlooded)) {
      this.isFlooded = true;
    }
    else {
      return;
    }
  }

  // determines whether this cell is not flooded.
  public boolean isLand() {
    return !this.isFlooded;

  }
}

// Represents a single square of the ocean cell
class OceanCell extends Cell {

  OceanCell(double height, int x, int y, Cell left, Cell top, Cell right, Cell bottom,
      boolean isFlooded) {
    super(height, x, y, left, top, right, bottom, isFlooded);
  }

  // produce the image of this oceanCell
  WorldImage getImage(ForbiddenIslandWorld w) {
    int widthC = ForbiddenIslandWorld.CELLWIDTH;
    int heightC = ForbiddenIslandWorld.CELLHEIGHT;
    return new RectangleImage(widthC, heightC, OutlineMode.SOLID, new Color(0, 0, 153));
  }

  // without any change of this ocean cell's isFlooded status
  void changeFlood(int waterHeight) {
    return;
  }

}

// Represent a player in this Forbidden Island
class Player {
  Posn position;
  Cell cell;
  boolean getSuit;
  boolean wearSuit;

  Player(Posn position, Cell cell, boolean getSuit, boolean wearSuit) {
    this.position = position;
    this.cell = cell;
    this.getSuit = getSuit;
    this.wearSuit = wearSuit;
  }

  // move the player by the given command if the moving direction is not
  // flooded.
  void move(String ke) {

    if (ke.equals("right") && !cell.right.isFlooded
        && this.position.x <= ForbiddenIslandWorld.ISLAND_SIZE) {
      this.position = new Posn(this.position.x + 1, this.position.y);
      this.cell = this.cell.right;
    }
    else if (ke.equals("left") && !cell.left.isFlooded && this.position.x >= 0) {
      this.position = new Posn(this.position.x - 1, this.position.y);
      this.cell = this.cell.left;
    }
    else if (ke.equals("up") && !cell.top.isFlooded && this.position.y >= 0) {
      this.position = new Posn(this.position.x, this.position.y - 1);
      this.cell = this.cell.top;
    }
    else if (ke.equals("down") && !cell.bottom.isFlooded
        && this.position.y <= ForbiddenIslandWorld.ISLAND_SIZE) {
      this.position = new Posn(this.position.x, this.position.y + 1);
      this.cell = this.cell.bottom;
    }
    else {
      return;
    }
  }

  // when wearing the swimming suit, I can swim for several seconds
  void swim(String ke) {
    if (ke.equals("right") && this.position.x <= ForbiddenIslandWorld.ISLAND_SIZE) {
      this.position = new Posn(this.position.x + 1, this.position.y);
      this.cell = this.cell.right;
    }
    else if (ke.equals("left") && this.position.x >= 0) {
      this.position = new Posn(this.position.x - 1, this.position.y);
      this.cell = this.cell.left;
    }
    else if (ke.equals("up") && this.position.y >= 0) {
      this.position = new Posn(this.position.x, this.position.y - 1);
      this.cell = this.cell.top;
    }
    else if (ke.equals("down") && this.position.y <= ForbiddenIslandWorld.ISLAND_SIZE) {
      this.position = new Posn(this.position.x, this.position.y + 1);
      this.cell = this.cell.bottom;
    }
    else {
      return;
    }
  }
}

// to represent everything that the player needs to pick up
class Target {
  Posn position;

  Target(Posn position) {
    this.position = position;
  }

}

// represents the final helicopter's target and that can only be picked up after
// all the other targets have been picked up
class HelicopterTarget extends Target {

  HelicopterTarget(Posn position) {
    super(position);

  }
}

// represent one Scuba: add an underwater swimming suit somewhere on the island.
// If the player finds it, and activates it (with ¡®s¡¯ for ¡°swim¡±),
// allow them to swim through flooded cells for a limited window of time.
class Suit extends Target {

  Suit(Posn position) {
    super(position);
  }

}

// to represent a position
class Posn {
  int x;
  int y;

  Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // determines whether this posn is same as the given posn
  boolean samePosn(Posn other) {
    return this.x == other.x && this.y == other.y;
  }
}

// represents a predicate
interface IPred<T> {

  // return true if T satisfies the requirements
  boolean apply(T t);
}

// represents IPred<Cell>
class FindCell implements IPred<Cell> {

  // determines whether this cell is not flooded
  public boolean apply(Cell t) {
    return t.isLand();
  }
}

// represents a comparator
interface IComparator<T, U> {

  // return the results of comparing T and U
  boolean compare(T t1, U t2);
}

// represents a comparator of target and cell
class SamePosition implements IComparator<Target, Cell> {

  // return true if the given target and cell have the same position
  public boolean compare(Target t2, Cell t1) {
    return t1.x == t2.position.x && t1.y == t2.position.y;
  }
}

// represents a function
interface IFunc<T, U> {
  U apply(T arg);
}

// represents a IFunc<Cell, posn>
class FindPosn implements IFunc<Cell, Posn> {

  // return this cell's position
  public Posn apply(Cell arg) {
    return new Posn(arg.x, arg.y);
  }
}

// Represent the world of a forbidden island
class ForbiddenIslandWorld extends World {

  // Defines the size of Island
  static final int ISLAND_SIZE = 64;

  // Defines an total height of mountain
  static final int ISLAND_TOTAL_HEIGHT = 254;

  // Defines an half of Island size
  static final int ISLAND_HALF_HEIGHT = (int) (ForbiddenIslandWorld.ISLAND_TOTAL_HEIGHT / 2);

  // the size of columns
  static final int INDEX = ForbiddenIslandWorld.ISLAND_SIZE + 1;

  // half size of the island
  static final int HALFSIZE = (int) ForbiddenIslandWorld.ISLAND_SIZE / 2;

  // width, height of the basic world
  static final int WIDTH = INDEX * 10;
  static final int HEIGHT = INDEX * 10;
  static final int WIDTH_HALF = WIDTH / 2;
  static final int HEIGHT_HALF = HEIGHT / 2;

  static final WorldImage PLAYER = new RectangleImage(20, 20, OutlineMode.SOLID, Color.GRAY);
  //      new FromFileImage("pilot-icon.png");
  static final WorldImage HELICOPTER = new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);
  //  new FromFileImage("helicopter.png");
  static final WorldImage SOMEHELICOPTER = new RectangleImage(
      20, 20, OutlineMode.SOLID, Color.PINK);
  static final WorldImage SUIT = new RectangleImage(
      20, 20, OutlineMode.SOLID, Color.YELLOW);
  static final int HELICOPTERS = 6;

  // one Cell width and height
  static final int CELLWIDTH = 10;
  static final int CELLHEIGHT = 10;

  IList<Cell> board;
  Player player;
  ArrayList<ArrayList<Cell>> listCell;
  IList<Target> helicopter;
  Target mainHeli;
  //  keeping time: display a countdown timer until the island floods completely
  int  timer; 
  // suit for player to swim
  Target suit;
  // time tick for player can swim
  int timeSwim;
  //count 10 tick and one rise water height
  int floodCount;
  // the current height of the ocean
  int waterHeight;

  // the constructor for this world: according to the string to decide with
  // pattern of this island
  ForbiddenIslandWorld(int waterHeight, String s) {
    if (s.equals("Regular Mountain")) {
      // A perfectly regular mountain
      ArrayList<ArrayList<Double>> listDouble = this.takeNewAlAlDouble();
      this.listCell = this.takeNewAlAlCell(listDouble);
    }
    else if (s.equals("Diamond Island")) {
      // A diamond island
      ArrayList<ArrayList<Double>> listDouble = this.takeDiamond();
      this.listCell = this.takeNewAlAlCell(listDouble);
    }
    else {
      // A random terrain island
      ArrayList<ArrayList<Double>> listDouble = this.setGridRandomly(this.takeNewAlAlDouble1());
      this.listCell = this.createCell(listDouble);
    }
    this.linkCell(listCell);
    this.board = this.buildListForCell(listCell);
    this.waterHeight = waterHeight;
    // find all cells' positions which are not flooded.
    IList<Posn> originalLand = this.board.filter(new FindCell()).map(new FindPosn());
    // random a position that is not flooded
    Posn p = this.putRandom(originalLand);
    this.player = new Player(p, listCell.get(p.x).get(p.y), false, false);
    this.helicopter = this.putRandomList(originalLand);
    this.mainHeli = new HelicopterTarget(this.putRandom(originalLand));
    this.timer = ISLAND_TOTAL_HEIGHT;
    this.suit = new Suit(this.putRandom(originalLand));
    this.floodCount = 0;
    this.timeSwim = 0;

  }

  // produce the value of the waterHeight and this island's type
  ForbiddenIslandWorld(String s) {
    this(0, s);
  }

  // Distribute some number of helicopter's pieces randomly around the island
  IList<Target> putRandomList(IList<Posn> originalLand) {
    IList<Target> result = new MtList<Target>();
    for (int i = 0; i < ForbiddenIslandWorld.HELICOPTERS; i = i + 1) {
      result = new ConsList<Target>(new Target(this.putRandom(originalLand)), result);
    }
    return result;
  }

  // choose a posn randomly in the list of posn
  Posn putRandom(IList<Posn> list) {
    Posn result = new Posn(0, 0);
    int want = new Random().nextInt(list.size());
    int count = 0;
    for (Posn p : list) {
      if (count == want) {
        result = p;
      }
      count = count + 1;
    }
    return result;
  }

  // the Island gradually was flooded
  public void onTick() {
    this.floodCount++;
    // Every ten ticks of the game, the water level should rise by one foot
    if (this.floodCount >= 10) {
      this.waterHeight++;
      this.timer = timer - 1;
      this.floodCount = 0;
    }
    this.flooded(this.board, this.waterHeight);
    if (this.player.wearSuit) {
      this.timeSwim = this.timeSwim - 1;
    } 
    else {
      return;
    }
  }

  // determine whether the each cell in the list is flooded or not
  void flooded(IList<Cell> result, int height) {
    for (Cell c : result) {
      c.changeFlood(height);
    }
  }

  // produce the image of this world by adding the cell to the background image
  public WorldScene makeScene() {
    WorldScene result = new WorldScene(WIDTH, HEIGHT);
    WorldImage text = new TextImage(("Time: " + String.format("%d", this.timer)), 15,
        FontStyle.BOLD, Color.WHITE);
    result.placeImageXY(text, 40, 10);
    for (Cell c : this.board) {
      result.placeImageXY(c.getImage(this), c.x * CELLWIDTH + CELLWIDTH / 2,
          c.y * CELLHEIGHT + CELLHEIGHT / 2);
    } // draw images of background cells
    for (Target t : this.helicopter) {
      result.placeImageXY(SOMEHELICOPTER, t.position.x * CELLWIDTH + CELLWIDTH / 2,
          t.position.y * CELLHEIGHT + CELLHEIGHT / 2);
    } // draw images of helicopter pieces
    result.placeImageXY(HELICOPTER, this.mainHeli.position.x * CELLWIDTH + CELLWIDTH / 2,
        this.mainHeli.position.y * CELLHEIGHT + CELLHEIGHT / 2);// draw a
    // helicopter
    result.placeImageXY(SUIT, this.suit.position.x * CELLWIDTH + CELLWIDTH / 2,
        this.suit.position.y * CELLWIDTH + CELLWIDTH / 2);
    result.placeImageXY(PLAYER, this.player.position.x * CELLWIDTH + CELLWIDTH / 2,
        this.player.position.y * CELLHEIGHT + CELLHEIGHT / 2); // draw a player
    // keeping time: display a count down timer until the island floods
    // completely.
    
    return result;

  }

  // move player on key-press
  public void onKeyEvent(String ke) {
    if (ke.equals("m")) {
      this.reset("Regular Mountain");
    }
    else if (ke.equals("r")) {
      this.reset("Diamond Island");
    }
    else if (ke.equals("t")) {
      this.reset("Random Terrain Island");
    }
    //If the player finds it, and activates it (with ¡®s¡¯ for ¡°swim¡±), allow them to swim
    else if (ke.equals("s") && this.player.getSuit) {
      this.player = new Player(this.player.position, this.player.cell, false, true);
      //start to time keeping, 300 tick 
      this.timeSwim = this.timeSwim + 300;
    }
    else {
      if (this.player.wearSuit && this.timeSwim > 0) {
        this.player.swim(ke);
      }
      else {
        this.player.move(ke);
      }
      this.helicopter = this.helicopter.pickH(this.player.cell, new SamePosition());
      if (this.suit.position.samePosn(this.player.position)) {
        this.player = new Player(this.player.position, this.player.cell, true, false);
        // get it and cannot see it in the Island
        this.suit.position = new Posn(-100, -100);
      }
      else {
        return;
      }
    }
  }

  // reset the world according to the given string
  void reset(String s) {
    if (s.equals("Regular Mountain")) {
      // A perfectly regular mountain
      ArrayList<ArrayList<Double>> listDouble = this.takeNewAlAlDouble();
      this.listCell = this.takeNewAlAlCell(listDouble);
    }
    else if (s.equals("Diamond Island")) {
      // A diamond island
      ArrayList<ArrayList<Double>> listDouble = this.takeDiamond();
      this.listCell = this.takeNewAlAlCell(listDouble);
    }
    else {
      // A random terrain island
      ArrayList<ArrayList<Double>> listDouble = this.setGridRandomly(this.takeNewAlAlDouble1());
      this.listCell = this.createCell(listDouble);
    }
    this.linkCell(listCell);
    this.board = this.buildListForCell(listCell);
    this.waterHeight = 0;
    IList<Posn> originalLand = this.board.filter(new FindCell()).map(new FindPosn());
    Posn p = this.putRandom(originalLand);
    this.player = new Player(p, listCell.get(p.x).get(p.y), false, false);
    this.helicopter = this.putRandomList(originalLand);
    this.mainHeli = new HelicopterTarget(this.putRandom(originalLand));
    this.timer = ISLAND_TOTAL_HEIGHT;
    this.suit = new Suit(this.putRandom(originalLand));
    this.floodCount = 0;
    this.timeSwim = 0;

  }

  // produce end-game image
  public WorldScene lastScene(String s) {
    WorldScene result = this.makeScene();
    result.placeImageXY(new TextImage(s, Color.red), ForbiddenIslandWorld.WIDTH_HALF,
        ForbiddenIslandWorld.HEIGHT_HALF);
    return result;
  }

  // Determine if end the world
  public WorldEnd worldEnds() {
    // if player pick up all things needed and escape
    if (this.helicopter.size() == 0 && this.mainHeli.position.samePosn(this.player.position)) {
      // fly out of the island
      this.mainHeli = new HelicopterTarget(new Posn(-100, -100));
      return new WorldEnd(true, this.lastScene("Escape Sucessfully"));
    }
    // when there is not land cell in the game, game over
    // when player fall into water and did not wear swim suit, game over
    // when player wear the suit, but the swim time over, and in the water,
    // game over
    else if (this.waterHeight >= ISLAND_TOTAL_HEIGHT
        || (this.player.cell.isFlooded && !this.player.wearSuit)
        || (this.player.wearSuit && (this.timeSwim <= 0) && this.player.cell.isFlooded)) {
      return new WorldEnd(true, this.lastScene("You are Drowned In the Sea"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // create a arralyList<ArrayList<Double>> for index numbers
  // x-coordinate index from initial point
  ArrayList<ArrayList<Double>> takeNewAlAlDouble() {
    ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>(INDEX);
    for (int i = 0; i < INDEX; i = i + 1) {
      result.add(this.takeNewAlDouble(i));
    }
    return result;
  }

  // create a ArrayList<Double> for index numbers
  // y-coordinate cells heights for every given x-coordinate cell index
  ArrayList<Double> takeNewAlDouble(int i) {
    ArrayList<Double> result = new ArrayList<Double>(INDEX);
    for (int j = 0; j < INDEX; j = j + 1) {
      // we multiply several times because we want the regular picture,
      // green color become deeper
      result.add((double) (ISLAND_TOTAL_HEIGHT - 4 * calM(i, j)));
    }
    return result;
  }

  // create a arralyList<ArrayList<Double>> for index numbers
  // x-coordinate index from initial point, make all height is 0
  ArrayList<ArrayList<Double>> takeNewAlAlDouble1() {
    ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>(INDEX);
    for (int i = 0; i < INDEX; i = i + 1) {
      result.add(this.takeNewAlDouble1(i));
    }
    return result;
  }

  // create a ArrayList<Double> for index numbers
  // y-coordinate cells heights for every given x-coordinate cell index
  // make all height is 0
  ArrayList<Double> takeNewAlDouble1(int i) {
    ArrayList<Double> result = new ArrayList<Double>(INDEX);
    for (int j = 0; j < INDEX; j = j + 1) {
      // we multiply several times because we want the regular picture,
      // green color become deeper
      result.add(0.0);
    }
    return result;
  }

  // set the heights according to its surrounding cells' heights randomly
  // for producing a random terrain island
  ArrayList<ArrayList<Double>> setGridRandomly(ArrayList<ArrayList<Double>> result) {
    result.get(0).set(0, 0.0);
    result.get(ISLAND_SIZE).set(0, 0.0);
    result.get(0).set(ISLAND_SIZE, 0.0);
    result.get(ISLAND_SIZE).set(ISLAND_SIZE, 0.0);
    result.get(HALFSIZE).set(0, 1.0);
    result.get(0).set(HALFSIZE, 1.0);
    result.get(ISLAND_SIZE).set(HALFSIZE, 1.0);
    result.get(HALFSIZE).set(ISLAND_SIZE, 1.0);
    result.get(HALFSIZE).set(HALFSIZE, (double) ISLAND_TOTAL_HEIGHT);
    // input the coordinate of four corners
    this.subdivisionSet(result, 0, ISLAND_SIZE, 0, ISLAND_SIZE);
    return result;
  }

  // EFFECT: modifies this list and each item is the average of the items
  // surrounding it, plus a random nudge. This procedure creates four smaller
  // rectangles, which then recursively process to create the rest of the
  // island heights.
  void subdivisionSet(ArrayList<ArrayList<Double>> result, int x1, int x2, int y1, int y2) {
    if (x2 - x1 <= 1) {
      return;
    }
    else {
      Double tl = result.get(x1).get(y1);
      Double tr = result.get(x2).get(y1);
      Double bl = result.get(x1).get(y2);
      Double br = result.get(x2).get(y2);

      int halfX = (x1 + x2) / 2;
      int halfY = (y1 + y2) / 2;
      // random according the current area of rectangle
      double area = (x2 - x1) * (y2 - y1) * 0.2;
      double t = Math.min(255, ((Math.random() - 0.5) * area + (tl + tr) / 2));
      double b = Math.min(255, ((Math.random() - 0.5) * area + (bl + br) / 2));
      double l = Math.min(255, ((Math.random() - 0.5) * area + (tl + bl) / 2));
      double r = Math.min(255, ((Math.random() - 0.5) * area + (tr + br) / 2));
      double m = Math.min(255, ((Math.random() - 0.5) * area + (tl + tr + bl + br) / 4));
      // to avoid duplicate
      this.checkZero(result, halfX, y1, t);
      this.checkZero(result, x1, halfY, l);
      this.checkZero(result, halfX, y2, b);
      this.checkZero(result, x2, halfY, r);
      this.checkZero(result, halfX, halfY, m);
      this.checkZero(result, halfX, y1, t);
      // recursively process
      this.subdivisionSet(result, x1, halfX, y1, halfY);
      this.subdivisionSet(result, halfX, x2, y1, halfY);
      this.subdivisionSet(result, x1, halfX, halfY, y2);
      this.subdivisionSet(result, halfX, x2, halfY, y2);

    }
  }

  // Effect: modifies this list and void duplicate (when the procedure creates
  // four smaller rectangles, some edges figure twice )
  void checkZero(ArrayList<ArrayList<Double>> result, int x, int y, double l) {
    if (result.get(x).get(y) > 0) {
      return;
    }
    else {
      result.get(x).set(y, l);
    }
  }

  // create a arralyList<ArrayList<Cell>> for index numbers
  // x-coordinate cell from initial point according to given list-list-double
  ArrayList<ArrayList<Cell>> createCell(ArrayList<ArrayList<Double>> list) {
    ArrayList<ArrayList<Cell>> result = new ArrayList<ArrayList<Cell>>(INDEX);
    for (int i = 0; i < INDEX; i = i + 1) {
      result.add(this.takeCell(i, list));
    }
    return result;
  }

  // create a ArrayList<Double> for index numbers
  // y-coordinate cells for every given x-coordinate cell index
  ArrayList<Cell> takeCell(int i, ArrayList<ArrayList<Double>> list) {
    ArrayList<Cell> result = new ArrayList<Cell>(INDEX);
    ArrayList<Double> exchange = list.get(i);
    for (int j = 0; j < INDEX; j = j + 1) {
      if (exchange.get(j) >= 25) {
        result.add(new Cell(exchange.get(j), i, j, null, null, null, null, false));
      }
      else {
        result.add(new OceanCell(0, i, j, null, null, null, null, true));
      }
    }
    return result;
  } 

  // create an arralyList<ArrayList<Double>> for producing
  // A diamond island of random heights
  ArrayList<ArrayList<Double>> takeDiamond() {
    ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>(INDEX);
    for (int i = 0; i < INDEX; i = i + 1) {
      result.add(this.takeDiamondDouble(i));
    }
    return result;
  }

  // create a ArrayList<Double> for index numbers
  // y-coordinate cells heights for every given x-coordinate cell index
  ArrayList<Double> takeDiamondDouble(int i) {
    ArrayList<Double> result = new ArrayList<Double>(INDEX);
    for (int j = 0; j < INDEX; j = j + 1) {
      result.add((double) new Random().nextInt(ISLAND_TOTAL_HEIGHT));
    }
    return result;
  }

  // calculate the Manhattan distance
  int calM(int i, int j) {
    return (Math.abs(HALFSIZE - i) + Math.abs(HALFSIZE - j));
  }

  // create a arralyList<ArrayList<Cell>> for index numbers
  // x-coordinate cell from initial point according to given list-list-double
  ArrayList<ArrayList<Cell>> takeNewAlAlCell(ArrayList<ArrayList<Double>> listdouble) {
    ArrayList<ArrayList<Cell>> result = new ArrayList<ArrayList<Cell>>(INDEX);
    for (int i = 0; i < INDEX; i = i + 1) {
      result.add(this.takeNewAlCell(i, listdouble));
    }
    return result;
  }

  // create a ArrayList<Double> for index numbers
  // y-coordinate cells for every given x-coordinate cell index
  ArrayList<Cell> takeNewAlCell(int i, ArrayList<ArrayList<Double>> ldouble) {
    ArrayList<Cell> result = new ArrayList<Cell>(INDEX);
    ArrayList<Double> exchange = ldouble.get(i);
    for (int j = 0; j < INDEX; j = j + 1) {
      if (calM(i, j) < 32) {
        result.add(new Cell(exchange.get(j), i, j, null, null, null, null, false));
      }
      else {
        result.add(new OceanCell(0, i, j, null, null, null, null, true));
      }
    }
    return result;
  }

  // link the cell in the given ArrayList<ArrayList<Cell>> with each other
  void linkCell(ArrayList<ArrayList<Cell>> whole) {
    for (int i = 0; i < INDEX; i = i + 1) {
      for (int j = 0; j < INDEX; j = j + 1) {
        whole.get(i).get(j).left = setCell(whole, i - 1, j);
        whole.get(i).get(j).right = setCell(whole, i + 1, j);
        whole.get(i).get(j).top = setCell(whole, i, j - 1);
        whole.get(i).get(j).bottom = setCell(whole, i, j + 1);
      }
    }
  }

  // set the neighbor cells for one cell according to its position
  Cell setCell(ArrayList<ArrayList<Cell>> world, int i, int j) {
    if (i < 0) {
      return world.get(i + 1).get(j);
    }
    else if (i >= INDEX) {
      return world.get(i - 1).get(j);
    }
    else if (j < 0) {
      return world.get(i).get(j + 1);
    }
    else if (j >= INDEX) {
      return world.get(i).get(j - 1);
    }
    else {
      return world.get(i).get(j);
    }
  }

  // select and pick up the cell out to form list
  IList<Cell> buildListForCell(ArrayList<ArrayList<Cell>> whole) {
    IList<Cell> result = new MtList<Cell>();
    for (int i = 0; i < INDEX; i = i + 1) {
      for (int j = 0; j < INDEX; j = j + 1) {
        result = new ConsList<Cell>(whole.get(i).get(j), result);
      }
    }
    return result;
  }
}

// represents the IList<T>
interface IList<T> extends Iterable<T> {

  // filter this list according to the given predicate
  IList<T> filter(IPred<T> pred);

  // filter this list according to the given Comparator
  <U> IList<T> pickH(U u, IComparator<T, U> comp);

  // figure out the size of this list
  int size();

  // map the given function across this list
  <U> IList<U> map(IFunc<T, U> func);

  // determines whether this list is a ConsList
  boolean isCons();

  // cast this list as a ConsList
  ConsList<T> asCons();

}

// represents the MtList<T>
class MtList<T> implements IList<T> {

  // filter this list according to the given predicate
  public IList<T> filter(IPred<T> pred) {
    return this;
  }

  // map the given function across this list
  public <U> IList<U> map(IFunc<T, U> func) {
    return new MtList<U>();
  }

  // return a new ListIterator
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // determines whether this empty list is a ConsList
  public boolean isCons() {
    return false;
  }

  // cast this ConsList list as a ConsList
  public ConsList<T> asCons() {
    throw new RuntimeException("I'm not a Cons!");
  }

  // return the length of this empty list
  public int size() {
    return 0;
  }

  // filter this list according to the given Comparator
  public <U> IList<T> pickH(U u, IComparator<T, U> comp) {
    return this;
  }
}

// represents the ConsList<T>
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // filter this list according to the given predicate
  public IList<T> filter(IPred<T> pred) {
    if (pred.apply(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // map the given function across this list
  public <U> IList<U> map(IFunc<T, U> func) {
    return new ConsList<U>(func.apply(this.first), this.rest.map(func));
  }

  // return a new ListIterator of this ConsList
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // determines whether this ConsList is a ConsList
  public boolean isCons() {
    return true;
  }

  // cast this ConsList as a ConsList
  public ConsList<T> asCons() {
    return this;
  }

  // figure out the length of this ConsList
  public int size() {
    return 1 + this.rest.size();
  }

  // filter this list according to the given Comparator
  public <U> IList<T> pickH(U u, IComparator<T, U> comp) {
    if (comp.compare(this.first, u)) {
      return this.rest;
    }
    else {
      return new ConsList<T>(this.first, this.rest.pickH(u, comp));
    }

  }
}

// Represents the ability to produce a sequence of values
// of type T, one at a time
class ListIterator<T> implements Iterator<T> {
  // the list of items that this iterator iterates over
  IList<T> items;
  // the index of the next item to be returned
  int nextIdx;
  // Construct an iterator for a given List

  ListIterator(IList<T> items) {
    this.items = items;
    this.nextIdx = 0;
  }

  // Does this sequence have at least one more value?
  public boolean hasNext() {
    return items.isCons();
  }

  // Get the next value in this sequence
  // EFFECT: Advance the iterator to the subsequent value
  public T next() {
    ConsList<T> itemsAsCons = this.items.asCons();
    T t = itemsAsCons.first;
    this.items = itemsAsCons.rest;
    return t;
  }
}

// examples of island
class ExampleIsland {

  // test that we can run two different animations concurrently
  // with the events directed to the correct version of the world
  /*
   * // examples of the ForbiddenIslandWorld ForbiddenIslandWorld w1 = new
   * ForbiddenIslandWorld("Regular Mountain"); ForbiddenIslandWorld w2 = new
   * ForbiddenIslandWorld("Diamond Island");
   *
   * boolean runAnimation = this.w1.bigBang(ForbiddenIslandWorld.WIDTH,
   * ForbiddenIslandWorld.HEIGHT, 0.1); boolean runAnimation2 =
   * this.w2.bigBang(ForbiddenIslandWorld.WIDTH, ForbiddenIslandWorld.HEIGHT,
   * 0.1);
   *
   */
  // if using the first regular mountain, we have to wait about 7 seconds to
  // see changing

  // RUN THE GAME
  void testRunGame(Tester t) {
    // run the game
    ForbiddenIslandWorld w1 = new ForbiddenIslandWorld("Regular Mountain");
    ForbiddenIslandWorld w2 = new ForbiddenIslandWorld("Diamond Island");
    ForbiddenIslandWorld w3 = new ForbiddenIslandWorld("Random Terrain Island");
    // if using the first regular mountain, we have to wait about 7 seconds to
    // see changing
    // w1.bigBang(ForbiddenIslandWorld.WIDTH, ForbiddenIslandWorld.HEIGHT, 0.01);
    // w2.bigBang(ForbiddenIslandWorld.WIDTH, ForbiddenIslandWorld.HEIGHT, 0.01);
    w3.bigBang(ForbiddenIslandWorld.WIDTH, ForbiddenIslandWorld.HEIGHT, 0.01);
  }

  ForbiddenIslandWorld regular;
  ForbiddenIslandWorld diamond;
  ForbiddenIslandWorld terrain;

  void initTestConditions() {
    this.regular = new ForbiddenIslandWorld("Regular Mountain");
    this.diamond = new ForbiddenIslandWorld("Diamond Island");
    this.terrain = new ForbiddenIslandWorld("Random Terrain Island");
  }

  // test the method getImage
  boolean testGetImage(Tester t) {
    this.initTestConditions();
    WorldImage ocean = new RectangleImage(10, 10, "SOLID", new Color(0, 0, 153));
    WorldImage highIsland = new RectangleImage(10, 10, "SOLID", new Color(254, 255, 254));
    return t.checkExpect(regular.listCell.get(0).get(0).getImage(regular), ocean)
        && t.checkExpect(regular.listCell.get(32).get(32).getImage(regular), highIsland);
  }

  // test the method changeFlood
  void testChangeFlood(Tester t) {
    this.initTestConditions();
    Cell c1 = new Cell(100.0, 20, 20, true);
    Cell c2 = new Cell(200.0, 21, 20, c1, c1, c1, c1, false);
    t.checkExpect(c2.isFlooded, false);
    c2.changeFlood(200);
    t.checkExpect(c2.isFlooded, true);
  }

  // test the method isLand
  void testIsLand(Tester t) {
    this.initTestConditions();
    Cell c1 = new Cell(100.0, 20, 20, true);
    Cell c2 = new Cell(200.0, 21, 20, c1, c1, c1, c1, false);
    t.checkExpect(c1.isLand(), false);
    t.checkExpect(c2.isLand(), true);
  }

  // test the method takeNewAlDouble
  boolean testTakeNewAlDouble(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.takeNewAlDouble(0).get(0), -2.0)
        && t.checkExpect(regular.takeNewAlDouble(4).get(10), 54.0)
        && t.checkExpect(regular.takeNewAlDouble(54).get(8), 70.0)
        && t.checkExpect(regular.takeNewAlDouble(64).get(64), -2.0);
  }

  // test the method takeNewAlAlDouble
  boolean testTakeNewAlAlDouble(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.takeNewAlAlDouble().get(0).get(0), -2.0)
        && t.checkExpect(regular.takeNewAlAlDouble().get(0).get(1), 2.0)
        && t.checkExpect(regular.takeNewAlAlDouble().get(4).get(10), 54.0)
        && t.checkExpect(regular.takeNewAlAlDouble().get(54).get(8), 70.0)
        && t.checkExpect(regular.takeNewAlAlDouble().get(64).get(64), -2.0);
  }

  // test the calM
  boolean testCalM(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.calM(0, 0), 64) && t.checkExpect(regular.calM(4, 10), 50)
        && t.checkExpect(regular.calM(54, 8), 46) && t.checkExpect(regular.calM(64, 64), 64);
  }

  // test the method takeNewAlCell
  boolean testTakeNewAlCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect( regular.takeNewAlCell(
        0, regular.takeNewAlAlDouble()).get(0).height, 0.0)
        && t.checkExpect(regular.takeNewAlCell(
            44, regular.takeNewAlAlDouble()).get(18).height, 150.0)
            && t.checkExpect(regular.takeNewAlCell(
                30, regular.takeNewAlAlDouble()).get(28).height, 230.0)
                && t.checkExpect(regular.takeNewAlCell(
                    30, regular.takeNewAlAlDouble()).get(29).height, 234.0)
                    && t.checkExpect(regular.takeNewAlCell(
                        64, regular.takeNewAlAlDouble()).get(64).height, 0.0);
  }

  // test the method takeNewAlAlCell
  boolean testTakeNewAlAlCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.takeNewAlAlCell(
        regular.takeNewAlAlDouble()).get(0).get(0).height, 0.0)
        && t.checkExpect(regular.takeNewAlAlCell(
            regular.takeNewAlAlDouble()).get(44).get(18).height, 150.0)
            && t.checkExpect(regular.takeNewAlAlCell(
                regular.takeNewAlAlDouble()).get(30).get(28).height, 230.0)
                && t.checkExpect(regular.takeNewAlAlCell(
                    regular.takeNewAlAlDouble()).get(64).get(64).height, 0.0);

  }

  // test the method takeDiamond
  boolean testTakeDiamond(Tester t) {
    this.initTestConditions();
    return t.checkExpect(diamond.takeDiamond().size(), 65)
        && t.checkExpect(diamond.takeDiamond().get(0).size(), 65)
        && t.checkExpect(Math.max(254.0, diamond.takeDiamond().get(1).get(0)), 254.0)
        && t.checkExpect(Math.min(0.0, diamond.takeDiamond().get(1).get(0)), 0.0)
        && t.checkExpect(diamond.takeDiamond().get(10).get(10) instanceof Double, true);
  }

  // test the method takeDiamondDouble
  boolean testTakeDiamondDouble(Tester t) {
    this.initTestConditions();
    return t.checkExpect(diamond.takeDiamondDouble(1).size(), 65)
        && t.checkExpect(Math.max(254.0, diamond.takeDiamondDouble(1).get(0)), 254.0)
        && t.checkExpect(Math.min(0.0, diamond.takeDiamondDouble(1).get(0)), 0.0)
        && t.checkExpect(diamond.takeDiamond().get(0).size(), 65)
        && t.checkExpect(diamond.takeDiamondDouble(3).get(10) instanceof Double, true);
  }

  // test the method linkCell
  void testLinkCell(Tester t) {
    this.initTestConditions();
    ArrayList<ArrayList<Cell>> loc = regular.takeNewAlAlCell(regular.takeNewAlAlDouble());
    regular.linkCell(loc);
    t.checkExpect(loc.get(0).get(0).height, 0.0);
    t.checkExpect(loc.get(24).get(19).height, 170.0);
    t.checkExpect(loc.get(24).get(38).height, 198.0);
    t.checkExpect(loc.get(64).get(64).height, 0.0);
  }

  // test the method setCell
  boolean testSetCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.setCell(regular.listCell, 1, 2).height,
        regular.listCell.get(1).get(2).height)
        && t.checkExpect(regular.setCell(regular.listCell, -1, 2).height,
            regular.listCell.get(0).get(2).height)
            && t.checkExpect(regular.setCell(regular.listCell, 65, 6).height,
                regular.listCell.get(64).get(6).height)
                && t.checkExpect(regular.setCell(regular.listCell, 1, 65).height,
                    regular.listCell.get(1).get(64).height)
                    && t.checkExpect(regular.setCell(regular.listCell, 1, -1).height,
                        regular.listCell.get(1).get(0).height);

  }

  // test the method buildListForCell
  boolean testBuildListForCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.buildListForCell(
        regular.listCell) instanceof IList, true) 
        && t.checkExpect(regular.buildListForCell(
            regular.listCell).equals(regular.listCell), false);
  }

  // test the method move
  void testMove(Tester t) {
    Posn ps = new Posn(30, 30);
    Cell c = this.regular.listCell.get(30).get(30);
    Player pl = new Player(ps, c, false, false);

    t.checkExpect(pl.position, new Posn(30, 30));
    pl.move("left");
    t.checkExpect(pl.position, new Posn(29, 30));
    pl.move("up");
    t.checkExpect(pl.position, new Posn(29, 29));
    pl.move("down");
    t.checkExpect(pl.position, new Posn(29, 30));
  }

  // test the method swim
  void testSwim(Tester t) {
    Posn ps = new Posn(30, 30);
    Cell c = this.regular.listCell.get(30).get(30);
    Player pl = new Player(ps, c, false, false);

    t.checkExpect(pl.position, new Posn(30, 30));
    pl.swim("left");
    t.checkExpect(pl.position, new Posn(29, 30));
    pl.swim("up");
    t.checkExpect(pl.position, new Posn(29, 29));
    pl.swim("down");
    t.checkExpect(pl.position, new Posn(29, 30));
  }

  // test the method samePosn
  boolean testSamePosn(Tester t) {
    Posn ps1 = new Posn(2, 3);
    Posn ps2 = new Posn(3, 3);
    return t.checkExpect(ps1.samePosn(ps2), false) 
        && t.checkExpect(ps1.samePosn(ps1), true)
        && t.checkExpect(ps2.samePosn(ps2), true);
  }

  // test the method putRandomList
  boolean testPutRandomList(Tester t) {
    this.initTestConditions();
    IList<Posn> lp = new ConsList<Posn>(new Posn(1, 1),
        new ConsList<Posn>(new Posn(1, 2),
            new ConsList<Posn>(new Posn(1, 3),
                new ConsList<Posn>(new Posn(1, 4),
                    new ConsList<Posn>(new Posn(1, 5),
                        new ConsList<Posn>(new Posn(1, 6), new ConsList<Posn>(new Posn(1, 7),
                            new ConsList<Posn>(new Posn(1, 8), new MtList<Posn>()))))))));
    return t.checkExpect(regular.putRandomList(lp).isCons(), true)
        && t.checkExpect(regular.putRandomList(lp) instanceof IList, true)
        && t.checkExpect(regular.putRandomList(lp).size(), 6);
  }

  // test the method putRandom
  boolean testPutRandom(Tester t) {
    this.initTestConditions();
    IList<Posn> lp = new ConsList<Posn>(new Posn(1, 1),
        new ConsList<Posn>(new Posn(1, 2),
            new ConsList<Posn>(new Posn(1, 3),
                new ConsList<Posn>(new Posn(1, 4),
                    new ConsList<Posn>(new Posn(1, 5),
                        new ConsList<Posn>(new Posn(1, 6),
                            new ConsList<Posn>(new Posn(1, 7),
                                new ConsList<Posn>(new Posn(1, 8),
                                    new MtList<Posn>()))))))));
    return t.checkExpect(regular.putRandom(lp).x, 1)
        && t.checkExpect(regular.putRandom(lp).y <= 8, true)
        && t.checkExpect(regular.putRandom(lp).y >= 1, true);
  }

  // test the method takeNewAlDouble1
  boolean testTakeNewAlDouble1(Tester t) {
    this.initTestConditions();
    return t.checkExpect(terrain.takeNewAlDouble1(0).get(0), 0.0)
        && t.checkExpect(terrain.takeNewAlDouble1(4).get(10), 0.0)
        && t.checkExpect(terrain.takeNewAlDouble1(54).get(8), 0.0)
        && t.checkExpect(terrain.takeNewAlDouble1(64).get(64), 0.0);
  }

  // test the method takeNewAlAlDouble1
  boolean testTakeNewAlAlDouble1(Tester t) {
    this.initTestConditions();
    return t.checkExpect(terrain.takeNewAlAlDouble1().get(0).get(0), 0.0)
        && t.checkExpect(terrain.takeNewAlAlDouble1().get(0).get(1), 0.0)
        && t.checkExpect(terrain.takeNewAlAlDouble1().get(4).get(10), 0.0)
        && t.checkExpect(terrain.takeNewAlAlDouble1().get(54).get(8), 0.0)
        && t.checkExpect(terrain.takeNewAlAlDouble1().get(64).get(64), 0.0);
  }

  // test the method setGridRandomly
  boolean testSetGridRandomly(Tester t) {
    this.initTestConditions();
    return t.checkExpect(terrain.setGridRandomly(
        terrain.takeNewAlAlDouble1()).get(0).get(0), 0.0)
        && t.checkExpect(terrain.setGridRandomly(
            terrain.takeNewAlAlDouble1()).get(0).get(64), 0.0)
            && t.checkExpect(terrain.setGridRandomly(
                terrain.takeNewAlAlDouble1()).get(64).get(0), 0.0)
                && t.checkExpect(terrain.setGridRandomly(
                    terrain.takeNewAlAlDouble1()).get(64).get(64), 0.0)
                    && t.checkExpect(terrain.setGridRandomly(
                        terrain.takeNewAlAlDouble1()).get(32).get(32),254.0)
                        && t.checkExpect(terrain.setGridRandomly(
                            terrain.takeNewAlAlDouble1()).get(32).get(0), 1.0)
                            && t.checkExpect(terrain.setGridRandomly(
                                terrain.takeNewAlAlDouble1()).get(0).get(32), 1.0)
                                && t.checkExpect(terrain.setGridRandomly(
                                    terrain.takeNewAlAlDouble1()).get(64).get(32), 1.0)
                                    && t.checkExpect(terrain.setGridRandomly(
                                        terrain.takeNewAlAlDouble1()).get(32).get(64),
                                        1.0);
  }

  // test the method subdivisionSet
  void testSubdivisionSet(Tester t) {
    this.initTestConditions();
    ArrayList<ArrayList<Double>> result = terrain.setGridRandomly(terrain.takeNewAlAlDouble1());
    t.checkExpect(result.get(32).get(0), 1.0);
    t.checkExpect(result.get(32).get(32), 254.0);
    t.checkExpect(result.get(0).get(32), 1.0);
    t.checkExpect(result.get(18).get(10) < 254, true);

  }

  // test the method createCell
  boolean testCreateCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect(
        terrain.createCell(terrain.setGridRandomly(
            terrain.takeNewAlAlDouble1())).get(32).get(0),
            new OceanCell(0, 32, 0, null, null, null, null, true))
            && t.checkExpect(terrain.createCell(
                terrain.setGridRandomly(terrain.takeNewAlAlDouble1()))
                .get(32).get(32), new Cell(254.0, 32, 32, null, null, null, null, false));
  }

  // test the method takeCell
  boolean testTakecell(Tester t) {
    this.initTestConditions();
    return t
        .checkExpect(terrain.takeCell(0, terrain.setGridRandomly(
            terrain.takeNewAlAlDouble1())).get(2).height, 0.0)
            && t.checkExpect(terrain.takeCell(32, terrain.setGridRandomly(
                terrain.takeNewAlAlDouble1())).get(32).height,254.0)
                && t.checkExpect(terrain.takeCell(32, terrain.setGridRandomly(
                    terrain.takeNewAlAlDouble1())).get(0), new OceanCell(
                        0, 32, 0, null, null, null, null, true))
                        && t.checkExpect( terrain.takeCell(
                            32, terrain.setGridRandomly(terrain.takeNewAlAlDouble1())).get(32),
                            new Cell(254.0, 32, 32, null, null, null, null, false));
  }

  // test the method iterator
  boolean testIterator(Tester t) {
    IList<Posn> lop1 = new MtList<Posn>();
    IList<Posn> lop2 = new ConsList<Posn>(new Posn(1, 1), lop1);
    Cell c = new Cell(0.0, 1, 1, false);
    IList<Cell> loc1 = new MtList<Cell>();
    IList<Cell> loc2 = new ConsList<Cell>(c, loc1);
    return t.checkExpect(lop1.iterator().hasNext(), false)
        && t.checkExpect(lop2.iterator().hasNext(), true)
        && t.checkExpect(loc1.iterator().hasNext(), false)
        && t.checkExpect(loc2.iterator().hasNext(), true)
        && t.checkExpect(loc2.iterator().next(), c)
        && t.checkExpect(lop2.iterator().next(), new Posn(1, 1));
  }

  // test the method filter
  boolean testFilter(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.board.filter(new FindCell()).isCons(), true)
        && t.checkExpect(regular.board.filter(new FindCell()).asCons().first.height > 0, true)
        && t.checkExpect(regular.board.filter(new FindCell()).asCons().first.height < 254.0, true);
  }

  // test the method pickH
  boolean testPickH(Tester t) {
    this.initTestConditions();
    return t.checkExpect(regular.helicopter.pickH(regular.player.cell, new SamePosition()).isCons(),
        true)
        && t.checkExpect(regular.helicopter.pickH(regular.player.cell, new SamePosition())
            .asCons().first.position.x > 0, true)
            && t.checkExpect(regular.helicopter.pickH(regular.player.cell, new SamePosition())
                .asCons().first.position.x < 64, true);

  }

  // test the method map
  boolean testMap(Tester t) {
    IList<Posn> lop1 = new MtList<Posn>();
    IList<Posn> lop2 = new ConsList<Posn>(new Posn(1, 1), lop1);
    Cell c = new Cell(0.0, 1, 1, false);
    IList<Cell> loc1 = new MtList<Cell>();
    IList<Cell> loc2 = new ConsList<Cell>(c, loc1);
    return t.checkExpect(loc1.map(new FindPosn()), loc1)
        && t.checkExpect(loc1.map(new FindPosn()).isCons(), false)
        && t.checkExpect(loc2.map(new FindPosn()), lop2)
        && t.checkExpect(loc2.map(new FindPosn()).isCons(), true);
  }

  // test the method size
  boolean testSize(Tester t) {
    ConsList<String> los = new ConsList<String>("I", new MtList<String>());
    return t.checkExpect(los.size(), 1) && t.checkExpect(new MtList<Cell>().size(), 0);
  }

  // test the method isCons
  boolean testIsCons(Tester t) {
    ConsList<String> los = new ConsList<String>("I", new MtList<String>());
    return t.checkExpect(los.isCons(), true) && t.checkExpect(new MtList<Cell>().isCons(), false);
  }

  // test the method asCons
  boolean testAsCons(Tester t) {
    ConsList<String> los = new ConsList<String>("I", new MtList<String>());
    return t.checkExpect(los.asCons(), los)
        && t.checkException(new RuntimeException("I'm not a Cons!"), new MtList<Cell>(), "asCons");
  }

}