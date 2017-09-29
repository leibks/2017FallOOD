// Assignment 5
// lindsay aaron
// aaronlindsay
// lei bowen
// bowenleis

import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)


// to represent the world of fish
class Pond extends World {

  // WORLD CONSTANTS

  // width, height, and center of pond values
  public static final int WIDTH = 1200;
  public static final int HEIGHT = 800;
  public static final int HALF_WIDTH = (int) Pond.WIDTH / 2;
  public static final int HALF_HEIGHT = (int) Pond.HEIGHT / 2;
  public static final int INITIAL_SIZE = (int) (Math.min(Pond.HEIGHT, Pond.WIDTH) * 0.02);

  // images
  public static final WorldImage REEF = new FromFileImage("reef.jpeg");

  public static double getReefScale() {
    if (WIDTH > HEIGHT) {
      return (WIDTH / REEF.getWidth());
    }
    else {
      return (HEIGHT / REEF.getHeight());
    }
  }

  public static final WorldImage POND_BG = new ScaleImage(REEF, getReefScale());

  public static final WorldImage POND_BG2 = new RectangleImage(Pond.WIDTH,
      Pond.HEIGHT, OutlineMode.SOLID, Color.BLUE);

  public static final WorldImage RED_FISH = new FromFileImage("redFish.png");
  public static final WorldImage RED_FISH_FLIP = new FromFileImage("redFishFlip.png");
  public static final WorldImage YLW_FISH = new FromFileImage("ylwFish.png");
  public static final WorldImage YLW_FISH_FLIP = new FromFileImage("ylwFishFlip.png");
  public static final WorldImage BLU_FISH = new FromFileImage("bluFish.png");
  public static final WorldImage BLU_FISH_FLIP = new FromFileImage("bluFishFlip.png");
  public static final WorldImage MAG_FISH = new FromFileImage("magFish.png");
  public static final WorldImage MAG_FISH_FLIP = new FromFileImage("magFishFlip.png");

  // fields
  Player player;
  ILoSwimmer fishies;

  public Pond(Player player, ILoSwimmer fishies) {
    super();
    this.player = player;
    this.fishies = fishies;
  }

  // construct a pond with n fish
  public Pond(int n) {
    this(new Player(), new ConsLoSwimmer(n));
  }

  public WorldScene makeScene() {
    WorldScene bottom = this.fishies.getImages(this)
        .placeImageXY(this.player.getImage(),
            this.player.posn.x, this.player.posn.y);
    WorldImage text = new TextImage(("Score: " + String.format("%d", 
        this.player.size - INITIAL_SIZE)), 30, FontStyle.BOLD, Color.WHITE);

    int scale = (int) (0.05 * Math.min(Pond.WIDTH, Pond.HEIGHT));
    int textX = (int) (Pond.WIDTH - 0.5 * text.getWidth() - scale);
    int textY = (int) (0.5 * text.getHeight() + scale);

    return bottom.placeImageXY(text, textX, textY);

  }

  // move player on key-press
  public World onKeyEvent(String ke) {
    if (ke.equals("x")) {
      try {
        Sound.UNDERWATER.stop();
      }
      catch (NoClassDefFoundError e) {
        System.out.println("NoClassDefFoundError: " + e);
      }
      catch (NullPointerException e) {
        System.out.println("NullPointerException: " + e);
      }
      finally {
        return this.endOfWorld("Goodbye!");
      }
    }
    else {
      return new Pond(this.player.move(ke), this.fishies);
    }
  }

  // move all swimmers according to their velocities on-tick
  public World onTick() {
    return new Pond(this.player.eat(this.fishies).move(),
        this.fishies.checkPopulation(this.player).move());
  }

  // produce end-game image
  public WorldScene lastScene(String s) {
    WorldImage text = new TextImage(s, 30, FontStyle.BOLD, Color.WHITE);
    return this.makeScene()
        .placeImageXY(new RectangleImage(
            (int) text.getWidth() + 10, (int) text.getHeight() + 10,
            OutlineMode.SOLID, new Color(76, 200, 232)),
            Pond.HALF_WIDTH, Pond.HALF_HEIGHT)
            .placeImageXY(new RectangleImage(
                (int) text.getWidth() + 10, (int) text.getHeight() + 10,
                OutlineMode.OUTLINE, Color.WHITE),
                Pond.HALF_WIDTH, Pond.HALF_HEIGHT)
                .placeImageXY(text, Pond.HALF_WIDTH, Pond.HALF_HEIGHT);
  }

  // check whether the player was eaten or is the largest fish in the pond
  public WorldEnd worldEnds() {

    // if one of the fishies can eat the player:
    if (this.fishies.canEat(this.player)) {
      try {
        Sound.UNDERWATER.stop();
      }
      catch (NoClassDefFoundError e) {
        System.out.println("NoClassDefFoundError: " + e);
      }
      catch (NullPointerException e) {
        System.out.println("NullPointerException: " + e);
      }
      finally {
        return new WorldEnd(true, this.lastScene("You have been eaten :o("));
      }
    }
    // if the player is the largest fish in the pond:
    else if (this.fishies.areSmallerThan(this.player)) {
      try {
        Sound.UNDERWATER.stop();
      }
      catch (NoClassDefFoundError e) {
        System.out.println("NoClassDefFoundError: " + e);
      }
      catch (NullPointerException e) {
        System.out.println("NullPointerException: " + e);
      }
      finally {
        return new WorldEnd(true, this.lastScene("You are the biggest fish! :^)"));
      }
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

}

class ExamplesPond {

  // player size & smaller & bigger
  int pSize = ((int) (Math.min(Pond.HEIGHT, Pond.WIDTH) * 0.03));
  int pSizeSmall = pSize - 5;
  int pSizeLarge = pSize + 5;

  // examples of players
  Player p1 = new Player();
  Player bigPlayer = new Player(new Posn(Pond.HALF_WIDTH, 
      Pond.HALF_HEIGHT), new Vel(0, 0), 500, Color.RED);
  Player p2 = new Player(new Posn(100, 100), new Vel(0, 0), pSize, Color.RED);
  Player p2v2 = new Player(new Posn(100 + Pond.WIDTH, 100), new Vel(0, 0),
      pSize, Color.RED);
  Player p1L = new Player(new Posn(95, 100), new Vel(-5, 0), pSize, Color.RED);
  Player p1L2 = new Player(new Posn(90, 100), new Vel(-4, 0), pSize, Color.RED);
  Player p1R = new Player(new Posn(105, 100), new Vel(5, 0), pSize, Color.RED);
  Player p1R2 = new Player(new Posn(110, 100), new Vel(4, 0), pSize, Color.RED);
  Player p1U = new Player(new Posn(100, 95), new Vel(0, -5), pSize, Color.RED);
  Player p1U2 = new Player(new Posn(100, 90), new Vel(0, -4), pSize, Color.RED);
  Player p1D = new Player(new Posn(100, 105), new Vel(0, 5), pSize, Color.RED);
  Player p1D2 = new Player(new Posn(100, 110), new Vel(0, 4), pSize, Color.RED);
  Player p3 = new Player(new Posn(200, 100), new Vel(0, 0), pSize, Color.RED);
  Player p4 = new Player(new Posn(300, 100), new Vel(0, 0), pSize, Color.RED);
  Player player1 = new Player(new Posn(50, 50), new Vel(2, 2), 10, Color.MAGENTA);
  Player player1v2 = new Player(new Posn(52, 52), new Vel(1, 1), 10, Color.MAGENTA);
  Player player2 = new Player(new Posn(20, 30), new Vel(-1, 2), 20, Color.YELLOW);
  Player player3 = new Player(new Posn(80, 110), new Vel(2, -1), 80, Color.CYAN);
  Player player4 = new Player(new Posn(100, 150), new Vel(-3, -3), 60, Color.RED);
  Player player5 = new Player(new Posn(3000, 3000), new Vel(-3, -3), 60, Color.RED);


  // examples of fish
  ASwimmer f1 = new Fish(new Posn(100, 100), new Vel(0, 0), pSizeSmall, Color.YELLOW);
  ASwimmer f1L = new Fish(new Posn(95, 100), new Vel(-5, 0), pSizeSmall, Color.YELLOW);
  ASwimmer f1L2 = new Fish(new Posn(90, 100), new Vel(-5, 0), pSizeSmall, Color.YELLOW);
  ASwimmer f1R = new Fish(new Posn(105, 100), new Vel(5, 0), pSizeSmall, Color.YELLOW);
  ASwimmer f1U = new Fish(new Posn(100, 95), new Vel(0, -5), pSizeSmall, Color.YELLOW);
  ASwimmer f1D = new Fish(new Posn(100, 105), new Vel(0, 5), pSizeSmall, Color.YELLOW);
  ASwimmer f2 = new Fish(new Posn(200, 100), new Vel(0, 0), pSizeLarge, Color.CYAN);
  ASwimmer f3 = new Fish(new Posn(300, 100), new Vel(10, 10), pSizeSmall, Color.MAGENTA);
  Fish fish1 = new Fish(new Posn(80, 120), new Vel(2, 2), 40, Color.RED);
  Fish fish2 = new Fish(new Posn(80, 100), new Vel(-3, 3), 50, Color.YELLOW);
  Fish fish3 = new Fish(new Posn(40, 40), new Vel(4, -2), 80, Color.RED);
  Fish fish4 = new Fish(new Posn(25, 20), new Vel(-2, -2), 100, Color.MAGENTA);
  Fish fish5 = new Fish(new Posn(150, 180), new Vel(6, 1), 120, Color.CYAN);
  Fish fish6 = new Fish(new Posn(3000, 2000), new Vel(6, 1), 120, Color.CYAN);

  // examples of fishies
  ILoSwimmer mt = new MtLoSwimmer();
  ILoSwimmer fishies1 = new ConsLoSwimmer(f1,
      new ConsLoSwimmer(f2,
          new ConsLoSwimmer(f3, mt)));
  ILoSwimmer list1 = new ConsLoSwimmer(fish2,  new ConsLoSwimmer(fish1, mt));
  ILoSwimmer list2 = new ConsLoSwimmer(fish1,  new ConsLoSwimmer(fish2,
      new ConsLoSwimmer(fish3, mt)));
  ILoSwimmer list3 = new ConsLoSwimmer(fish5, mt);
  ILoSwimmer list4 = new ConsLoSwimmer(fish2,  new ConsLoSwimmer(fish6, mt));
  ILoSwimmer list5 = new ConsLoSwimmer(fish1, mt);

  // examples of worlds
  Pond mtW = new Pond(null, this.mt);
  Pond tw1 = new Pond(this.p2, this.fishies1);
  Pond playerEats = new Pond(p3, fishies1);
  Pond playerEaten = new Pond(p2, fishies1);
  Pond pond1 = new Pond(player1, list1);

  // examples of images
  WorldImage fish1I = new ScaleImage(new RotateImage(Pond.RED_FISH, (360 * (
      Math.atan(1) / (2 * Math.PI)))) ,
      (fish1.size / Pond.RED_FISH.getHeight()));
  WorldImage fish2I = new ScaleImage(new RotateImage(
      Pond.YLW_FISH_FLIP, (360 * ( Math.atan(-1) / (2 * Math.PI))) ) ,
      (fish2.size / Pond.RED_FISH.getHeight()));

  WorldImage fish3I = new ScaleImage(new RotateImage(
      Pond.RED_FISH, (360 * ( Math.atan(-0.5) / (2 * Math.PI)))) ,
      (fish3.size / Pond.RED_FISH.getHeight()) );

  // examples of World Scenes
  WorldScene pond1I = pond1.getEmptyScene().placeImageXY(
      Pond.POND_BG, Pond.HALF_WIDTH, Pond.HALF_HEIGHT);
  Pond pond2 = new Pond(player2, list2);
  WorldScene pond2I = pond2.getEmptyScene().placeImageXY(
      Pond.POND_BG, Pond.HALF_WIDTH, Pond.HALF_HEIGHT);

  // TESTS:

  // test sameSwimmer()
  boolean testSameSwimmer(Tester t) {
    return t.checkExpect(this.p1.sameSwimmer(this.p1), true)
        && t.checkExpect(this.p1.sameSwimmer(this.p2), false)
        && t.checkExpect(this.p2.sameSwimmer(this.p2v2), true)
        && t.checkExpect(this.p2.sameSwimmer(this.f1), false)
        && t.checkExpect(this.f1.sameSwimmer(this.f1), true);
  }

  // test the move method that computes according to velocity
  boolean testMove(Tester t) {
    return t.checkExpect(this.p1.move().sameSwimmer(this.p1), true)
        && t.checkExpect(this.p1.move().sameSwimmer(this.p2), false)
        && t.checkExpect(this.p1L.move().sameSwimmer(this.p1L2), true)
        && t.checkExpect(this.p1R.move().sameSwimmer(this.p1R2), true)
        && t.checkExpect(this.p1U.move().sameSwimmer(this.p1U2), true)
        && t.checkExpect(this.p1D.move().sameSwimmer(this.p1D2), true)
        && t.checkExpect(this.f1L.move().sameSwimmer(this.f1L2), true);
  }

  // test isNear()
  boolean testIsNear(Tester t) {
    return t.checkExpect(p2.isNear(f2), false)
        && t.checkExpect(p3.isNear(f2), true);
  }

  // test worldEnds()
  boolean testWorldEnds(Tester t) {
    boolean check = false;
    try {
      check = t.checkExpect(mtW.worldEnds(), new WorldEnd(false, mtW.makeScene()));
    }
    catch (NullPointerException e) {
      System.out.println("NullPointerException: " + e);
    }
    finally {
      return check;
    }
  }

  // test getImages()
  boolean testgetImages(Tester t) {
    return t.checkExpect(mt.getImages(pond1), pond1I)
        &&  t.checkExpect(list5.getImages(pond1), pond1I.placeImageXY(
            fish1I,fish1.posn.x, fish1.posn.y))
            &&  t.checkExpect(mt.getImages(pond2), pond2I)
            &&  t.checkExpect(list1.getImages(pond2), 
                pond1I.placeImageXY(fish1I, 
                    fish1.posn.x, fish1.posn.y ).placeImageXY(fish2I, 
                        fish2.posn.x, fish2.posn.y ));
  }

  // test getImagesHelp()
  boolean testgetImagesHelp(Tester t) {
    return t.checkExpect(mt.getImagesHelp(pond1, fish1), 
        pond1I.placeImageXY(fish1I, fish1.posn.x, fish1.posn.y))
        &&  t.checkExpect(list1.getImagesHelp(pond1, fish3), 
            pond1I.placeImageXY(fish1I, 
                fish1.posn.x, fish1.posn.y ).placeImageXY(fish2I, 
                    fish2.posn.x, fish2.posn.y ).placeImageXY(fish3I, 
                        fish3.posn.x, fish3.posn.y ))
                        &&  t.checkExpect(mt.getImagesHelp(pond2, fish3), pond2I
                            .placeImageXY(fish3I, fish3.posn.x, fish3.posn.y))
                            &&  t.checkExpect(list2.getImagesHelp(pond2, fish1), 
                                pond2I.placeImageXY(fish3I, 
                                    fish3.posn.x, fish3.posn.y ).placeImageXY(fish2I, 
                                        fish2.posn.x, fish2.posn.y ).placeImageXY(fish1I, 
                                            fish1.posn.x, fish1.posn.y ).placeImageXY(fish1I, 
                                                fish1.posn.x, fish1.posn.y));
  }

  // test move()
  boolean testmove(Tester t) {
    return t.checkExpect(player1.move(), 
        new Player(new Posn(52, 52),  new Vel(1, 1),  10, Color.MAGENTA))
        && t.checkExpect(player2.move(), 
            new Player(new Posn(19, 32), new Vel(0, 1), 20, Color.YELLOW))
            && t.checkExpect(fish1.move(), 
                new Fish(new Posn(82, 122), new Vel(2, 2), 40, Color.RED))
                && t.checkExpect(mt.move(), mt)
                && t.checkExpect(list1.move(), 
                    new ConsLoSwimmer(new Fish(new Posn(77, 103),
                        new Vel(-3, 3), 50, Color.YELLOW), 
                        new ConsLoSwimmer(new Fish(new Posn(82, 122), 
                            new Vel(2, 2), 40, Color.RED), mt)))
                            && t.checkExpect(list3.move(),
                                new ConsLoSwimmer(new Fish(new Posn(156, 181), 
                                    new Vel(6, 1), 120, Color.CYAN), mt.move()));
  }

  // test canEat()
  boolean testcanEat(Tester t) {
    return t.checkExpect(mt.canEat(player1), false)
        && t.checkExpect(list1.canEat(player4), false)
        && t.checkExpect(list2.canEat(player2), true)
        && t.checkExpect(list3.canEat(player4), true)
        && t.checkExpect(player1.canEat(fish1), false)
        && t.checkExpect(player4.canEat(fish1), true)
        && t.checkExpect(fish1.canEat(player4), false)
        && t.checkExpect(fish3.canEat(player2), true);
  }

  // test areEatenBy()
  boolean testareEatenBy(Tester t) {
    return t.checkExpect(mt.areEatenBy(player1), player1)
        && t.checkExpect(list3.areEatenBy(player1), player1)
        && t.checkExpect(list1.areEatenBy(player4), 
            new Player(new Posn(100, 150), new Vel(-3, -3), 63, Color.RED))
            && t.checkExpect(list2.areEatenBy(player3), 
                new Player(new Posn(80, 110), new Vel(2, -1), 83, Color.CYAN));    
  }

  // test areSmallerThan()
  boolean testareSmallerThan(Tester t) {
    return t.checkExpect(mt.areSmallerThan(player1), true)
        && t.checkExpect(list1.areSmallerThan(player1), false)
        && t.checkExpect(list1.areSmallerThan(player4), true)
        && t.checkExpect(list3.areSmallerThan(player3), false);    
  }

  // test areCheckPopulation()
  boolean testarecheckPopulation(Tester t) {
    return t.checkExpect(mt.checkPopulation(player1), mt)
        && t.checkExpect(list1.checkPopulation(player1), list1)
        && t.checkExpect(list3.checkPopulation(player4), list3);  
  }

  // test getImage()
  boolean testgetImage(Tester t) {
    return t.checkExpect(fish1.getImage(), fish1I)
        &&  t.checkExpect(fish2.getImage(), fish2I)
        &&  t.checkExpect(fish3.getImage(),fish3I)
        &&  t.checkExpect(fish4.getImage(), new ScaleImage(new RotateImage(
            Pond.MAG_FISH_FLIP,   (360 * ( Math.atan(1) / (2 * Math.PI)))) ,
            (fish4.size / Pond.RED_FISH.getHeight()) ))
            &&  t.checkExpect(player1.getImage(), new ScaleImage(new RotateImage(
                Pond.MAG_FISH, (360 * ( Math.atan(1) / (2 * Math.PI)))) ,
                (player1.size / Pond.RED_FISH.getHeight()) ))
                &&  t.checkExpect(player2.getImage(), new ScaleImage(
                    new RotateImage( Pond.YLW_FISH_FLIP, (360 * (
                        Math.atan(-2) / (2 * Math.PI)))) ,
                        (player2.size / Pond.RED_FISH.getHeight()) ))
                        &&  t.checkExpect(player3.getImage(),  
                            new ScaleImage(new RotateImage(
                                Pond.BLU_FISH, (360 * ( 
                                    Math.atan(-0.5) / (2 * Math.PI))) ) ,
                                    (player3.size / Pond.RED_FISH.getHeight()) ))
                                    &&  t.checkExpect(player4.getImage(),  
                                        new ScaleImage(new RotateImage(
                                            Pond.RED_FISH_FLIP, (360 * (
                                                Math.atan(1) / (2 * Math.PI))) ) ,
                                                (player4.size 
                                                    / Pond.RED_FISH.getHeight())));
  }

  // test isBiggerThan()
  boolean testisBiggerThan(Tester t) {
    return t.checkExpect(fish1.isBiggerThan(player1), true)
        && t.checkExpect(fish2.isBiggerThan(player4), false)
        && t.checkExpect(fish4.isBiggerThan(player1), true)
        && t.checkExpect(player4.isBiggerThan(fish1), true)
        && t.checkExpect(player1.isBiggerThan(fish1), false);    
  }

  // test isNear()
  boolean testisNear(Tester t) {
    return t.checkExpect(fish1.isNear(player3), true)
        && t.checkExpect(fish2.isNear(player2), false)
        && t.checkExpect(fish3.isNear(player1), true)
        && t.checkExpect(player3.isNear(fish2), true)
        && t.checkExpect(player1.isNear(fish5), false)
        && t.checkExpect(player3.isNear(fish3), false);
  }

  // test sameSwimmer()
  boolean testsameSwimmer(Tester t) {
    return t.checkExpect(fish1.sameSwimmer(fish1), true)
        && t.checkExpect(fish2.sameSwimmer(player2), false)
        && t.checkExpect(player3.sameSwimmer(fish3), false)
        && t.checkExpect(player1.sameSwimmer(player1), true);
  }

  // test outOfBounds()
  boolean testoutOfBounds(Tester t) {
    return t.checkExpect(fish1.outOfBounds(), false)
        && t.checkExpect(fish6.outOfBounds(), true)
        && t.checkExpect(player1.outOfBounds(), false);
  }

  // test move() pt. 2
  boolean testmove2(Tester t) {
    return t.checkExpect(player1.move("right"), 
        new Player(new Posn(50, 50), new Vel(8,  2), 10, Color.MAGENTA))
        && t.checkExpect(player2.move("left"), 
            new Player(new Posn(20, 30), new Vel(-7, 2), 20, Color.YELLOW))
            && t.checkExpect(player3.move("up"), 
                new Player(new Posn(80, 110), new Vel(2, -7), 80, Color.CYAN))
                && t.checkExpect(player4.move("down"), 
                    new Player(new Posn(100, 150), new Vel(-3, 3), 60, Color.RED));
  }

  // test getReefScale()
  boolean testGetReefScale(Tester t) {
    return t.checkExpect(Pond.getReefScale(), Pond.WIDTH / Pond.REEF.getWidth());
  }

  // test modulus()
  boolean testModulus(Tester t) {
    return t.checkExpect(new Utils().modulus(2, 3), 2)
        && t.checkExpect(new Utils().modulus(4, 3), 1)
        && t.checkExpect(new Utils().modulus(-1, 3), 2);
  }

  // test calcAngle()
  boolean testCalcAngle(Tester t) {
    return t.checkExpect(new Utils().calcAngle(2, 0), Math.PI / 2)
        && t.checkExpect(new Utils().calcAngle(-2, 0), 3 * Math.PI / 2)
        && t.checkExpect(new Utils().calcAngle(0, 0), 0.0)
        && t.checkExpect(new Utils().calcAngle(2, 2), Math.atan(1));
  }

  // test getImage()
  boolean testGetImage(Tester t) {
    return t.checkExpect(new Utils().getImage("magenta", new Vel(2, 0)), Pond.MAG_FISH)
        && t.checkExpect(new Utils().getImage("magenta", new Vel(-2, 0)), Pond.MAG_FISH_FLIP);
  }

  // test posnMove()
  boolean testPosnMove(Tester t) {
    return t.checkExpect(new Posn(1, 1).move(new Vel(1, 1)), new Posn(2, 2))
        && t.checkExpect(new Posn(1, 1).move(new Vel(-1, -1)), new Posn(0, 0));
  }

  // test posnSame()
  boolean testPosnSame(Tester t) {
    return t.checkExpect(new Posn(1, 1).isSameAs(new Posn(1, 1)), true)
        && t.checkExpect(new Posn(1, 1).isSameAs(new Posn(0, 0)), false);
  }

  // test velSame()
  boolean testVelSame(Tester t) {
    return t.checkExpect(new Vel(1, 1).isSameAs(new Vel(1, 1)), true)
        && t.checkExpect(new Vel(1, 1).isSameAs(new Vel(0, 0)), false);
  }

  // test drag()
  boolean testDrag(Tester t) {
    return t.checkExpect(new Vel(1, 1).drag().isSameAs(new Vel(0, 0)), true)
        && t.checkExpect(new Vel(2, 0).drag().isSameAs(new Vel(1, 0)), true)
        && t.checkExpect(new Vel(-1, -1).drag().isSameAs(new Vel(0, 0)), true)
        && t.checkExpect(new Vel(0, -2).drag().isSameAs(new Vel(0, -1)), true)
        && t.checkExpect(new Vel(0, 0).drag().isSameAs(new Vel(0, 0)), true)
        && t.checkExpect(new Vel(1, 1).drag().isSameAs(new Vel(1, 1)), false);
  }

  // RUN THE GAME
  boolean testRunGame(Tester t) {
    // run the game
    Pond w = new Pond(20);
    w.bigBang(Pond.WIDTH, Pond.HEIGHT, 0.04);
    try {
      Sound.UNDERWATER.loop();
    }
    catch (NoClassDefFoundError e) {
      System.out.println("NoClassDefFoundError: " + e);
    }
    catch (NullPointerException e) {
      System.out.println("NullPointerException: " + e);
    }
    finally {
      return true;
    }
  }

}