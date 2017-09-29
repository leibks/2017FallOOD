// Assignment 10 part 2
// lei bowen
// bowenleis
// Zhu Xiang
// zhuxiang

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//represents a predicate
interface IPred<T> {
  boolean apply(T t);
}

// represents a predicate of double
class LargerThan100 implements IPred<Double> {

  // determines wither this number is larger than 100
  public boolean apply(Double t) {
    return t > 100;
  }
}

// represents a function
interface IFunc<T, U> {
  U apply(T arg);
}

// represent a function of <integer, Posn>
class MakePosn implements IFunc<Integer, Posn> {

  // determines wither this number is larger than 100
  public Posn apply(Integer t) {
    return new Posn(t - 10, t + 10);
  }
}

// represents a comparator
interface IComparator<T> {

  // return the result of compare the given two items
  int compare(T t1, T t2);

}

// represents comparator of Edge
class CompareEdge implements IComparator<Edge> {

  // return the result of compare the two edge's weight
  public int compare(Edge t1, Edge t2) {
    if (t1.weight == t2.weight) {
      return 0;
    }
    else if (t1.weight < t2.weight) {
      return -1;
    }
    else {
      return 1;
    }
  }
}

// represents the IList<T>
interface IList<T> extends Iterable<T> {

  // filter this list according to the given predicate
  IList<T> filter(IPred<T> pred);

  // return the the length of this IList
  int size();

  // map the given function across this list
  <U> IList<U> map(IFunc<T, U> func);

  // determine whether this IList is a ConsList
  boolean isCons();

  // cast this IList to ConsList
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

  // return a new ListIterator that contains this MtList as an item
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // determine whether this MtList is a ConsList
  public boolean isCons() {
    return false;
  }

  // cast this MtList to ConsList
  public ConsList<T> asCons() {
    throw new RuntimeException("I'm not a Cons!");
  }

  // return the length of this empty list
  public int size() {
    return 0;
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

}

// Represents the ability to produce a sequence of values
// of type T, one at a time
class ListIterator<T> implements Iterator<T> {
  // the list of items that this iterator iterates over
  IList<T> items;
  // the index of the next item to be returned
  int nextIdx;
  // Construct an iterator for a given ArrayList

  ListIterator(IList<T> items) {
    this.items = items;
    this.nextIdx = 0;
  }

  // Does this sequence (of items in the list) have at least one more value?
  public boolean hasNext() {
    return items.isCons();
  }

  // pick up the first and get it rid of list
  // EFFECT: Advance the iterator to the subsequent value
  public T next() {
    ConsList<T> itemsAsCons = this.items.asCons();
    T t = itemsAsCons.first;
    this.items = itemsAsCons.rest;
    return t;
  }

}

// represents an edge class
class Edge {
  int weight;
  Vertex from;
  Vertex to;

  Edge(int weight) {
    new Edge(weight, null, null);
  }

  Edge(int weight, Vertex from, Vertex to) {
    this.weight = weight;
    this.from = from;
    this.to = to;
  }
}

// represents a vertex class
class Vertex {
  int x;
  int y;
  ArrayList<Vertex> outVertex;

  Vertex(int x, int y, ArrayList<Vertex> outVertex) {
    this.x = x;
    this.y = y;
    this.outVertex = outVertex;
  }

  // produce the image of this Vertex, according to the out edges
  WorldImage getImage(Maze m) {
    int width = Maze.VWIDTH;
    // void cover the lines to affect the effects of images
    int height = Maze.VHEIGHT;
    int half = width / 2;
    WorldImage base = new RectangleImage(width, height, OutlineMode.SOLID, Color.GRAY);
    Vertex top = new Vertex(this.x, this.y - 1, null);
    Vertex left = new Vertex(this.x - 1, this.y, null);
    Vertex bottom = new Vertex(this.x, this.y + 1, null);
    Vertex right = new Vertex(this.x + 1, this.y, null);

    if (!new Utils().contains(this.outVertex, top)) {
      base = new OverlayOffsetImage(new LineImage(new Posn(width, 0), Color.BLACK), 0, half, base);
    }
    if (!new Utils().contains(this.outVertex, bottom)) {
      base = new OverlayOffsetImage(new LineImage(new Posn(width, 0), Color.BLACK), 0, -half, base);
    }
    if (!new Utils().contains(this.outVertex, left)) {
      base = new OverlayOffsetImage(new LineImage(new Posn(0, width), Color.BLACK), half, 0, base);
    }
    if (!new Utils().contains(this.outVertex, right)) {
      base = new OverlayOffsetImage(new LineImage(new Posn(0, width), Color.BLACK), -half, 0, base);
    }
    return base;
  }

  // determines whether this vertex is equal to the given vertex
  public boolean vEquals(Vertex v) {
    return this.x == v.x && this.y == v.y;
  }

}

// Represent the player in the maze
class Player {
  int x;
  int y;
  Vertex vertex;

  Player(int x, int y, Vertex vertex) {
    this.x = x;
    this.y = y;
    this.vertex = vertex;
  }

  // move the player according to the given string
  void move(Maze maze, String ke) {
    Vertex top = new Vertex(this.x, this.y - 1, null);
    Vertex left = new Vertex(this.x - 1, this.y, null);
    Vertex bottom = new Vertex(this.x, this.y + 1, null);
    Vertex right = new Vertex(this.x + 1, this.y, null);

    if (ke.equals("up") && new Utils().contains(this.vertex.outVertex, top)) {
      this.y--;
      this.vertex = maze.arrayV.get(top.x).get(top.y);
      maze.playerWalk.add(top);
    }
    else if (ke.equals("left") && new Utils().contains(this.vertex.outVertex, left)) {
      this.x--;
      this.vertex = maze.arrayV.get(left.x).get(left.y);
      maze.playerWalk.add(left);
    }
    else if (ke.equals("down") && new Utils().contains(this.vertex.outVertex, bottom)) {
      this.y++;
      this.vertex = maze.arrayV.get(bottom.x).get(bottom.y);
      maze.playerWalk.add(bottom);
    }
    else if (ke.equals("right") && new Utils().contains(this.vertex.outVertex, right)) {
      this.x++;
      this.vertex = maze.arrayV.get(right.x).get(right.y);
      maze.playerWalk.add(right);
    }
    else {
      return;
    }
  }

}

//Represent the world of a Maze Game
class Maze extends World {

  //To represent the width and height vertices numbers
  static final int MAZE_WIDTH_VERTEX = 30;
  static final int MAZE_HEIGHT_VERTEX = 20;

  // one Vertex width and height
  static final int VWIDTH = 30;
  static final int VHEIGHT = 30;

  //To represent the width and height of the whole maze
  static final int WIDTH = MAZE_WIDTH_VERTEX * VWIDTH;
  static final int HEIGHT = MAZE_HEIGHT_VERTEX * VHEIGHT;
  static final int WIDTH_HALF = WIDTH / 2;
  static final int HEIGHT_HALF = HEIGHT / 2;

  ArrayList<ArrayList<Vertex>> arrayV;
  ArrayList<Edge> initalE;
  ArrayList<Edge> miniTree;
  Player player;
  ArrayList<Vertex> depthSearchList;
  ArrayList<Vertex> breadthSearchList;
  Stack<Vertex> worklistDepth;
  Queue<Vertex> worklistBreadth;
  ArrayList<Vertex> solutionPathForD;
  boolean showDepth;
  boolean showBreadth;
  boolean searchSucess;

  //Extra credits part:
  ArrayList<Vertex> playerWalk;
  //Provide an option to toggle the viewing of the visited paths.
  boolean viewPath;
  // Construct mazes with a bias in a particular direction 
  //when you run the game, 
  //if you press h, it will change to horizontal maze
  //if you press r, it will change to vertical maze
  //if you press c, it will change to common maze
  //relative codes in the onKeyEvent and createAlAlEdge method

  //at the same time, it allows the user the ability to 
  //start a new maze without restarting the program, you just need to press the keys

  //In addition to animating the solution of the maze, also animate
  //the construction of the maze: on each tick, show a single wall being knocked down.
  //with this time goes up, you will see one by one wall was knocked 
  int knockDoor;

  //keep the scores of wrong moves for either automatic solutions or manual ones
  int depthStep;
  int breadthStep;
  int playerStep;


  Maze() {

    this.arrayV = new Utils().createAlAlVertices(MAZE_WIDTH_VERTEX, MAZE_HEIGHT_VERTEX);
    this.initalE = new Utils().createAlAlEdge(arrayV, 0);
    new Utils().selectionSort(this.initalE, new CompareEdge());
    this.miniTree = new Utils().mimimumSpanningTree(this.initalE, new Utils().createIlist(arrayV));

    this.player = new Player(0, 0, arrayV.get(0).get(0));
    this.solutionPathForD = new ArrayList<Vertex>();  

    //for animating the search process, I added the field and designed search methods here
    this.depthSearchList = new ArrayList<Vertex>();
    this.worklistDepth = new Stack<Vertex>();
    worklistDepth.push(arrayV.get(0).get(0));

    this.breadthSearchList = new ArrayList<Vertex>();
    this.worklistBreadth = new Queue<Vertex>();
    worklistBreadth.enqueue(arrayV.get(0).get(0));
    this.playerWalk =  new ArrayList<Vertex>();

    this.showDepth = false;
    this.showBreadth = false;
    this.searchSucess = false;
    this.viewPath = false;

    this.knockDoor = 0;

    this.depthStep = 0;
    this.breadthStep = 0;
    this.playerStep = 0;

  }

  // the maze will animate the search process if
  // player want to see depth or breadth first search method 
  public void onTick() {
    //if the depth search the end, the world will show the solution and end
    if (this.showDepth && knockDoor == miniTree.size()) {
      Vertex next = worklistDepth.pop();
      if (next.vEquals(arrayV.get(MAZE_WIDTH_VERTEX - 1).get(MAZE_HEIGHT_VERTEX - 1))) {   
        this.searchSucess = true;
      }
      else if (depthSearchList.contains(next)) {
        // do nothing: we've already seen this one
      }
      else {
        // add all the neighbors of next to the worklist for further processing
        for (Vertex v : next.outVertex) {
          worklistDepth.push(v);
        }
        // add next to alreadySeen list for depth search, since we're done with it
        depthSearchList.add(next);
      }
    }
    //if the breadth search achieve the end, the world will show the solution and end
    if (this.showBreadth && knockDoor == miniTree.size()) {
      Vertex next = worklistBreadth.dequeue();
      if (next.vEquals(arrayV.get(MAZE_WIDTH_VERTEX - 1).get(MAZE_HEIGHT_VERTEX - 1))) {
        this.searchSucess = true;
      }
      else if (breadthSearchList.contains(next)) {
        // do nothing: we've already seen this one
      }
      else {
        // add all the neighbors of next to the worklist for further processing
        for (Vertex v : next.outVertex) {
          worklistBreadth.enqueue(v);
        }
        // add next to alreadySeen list for breadth search, since we're done with it
        breadthSearchList.add(next);
      }
    }
    //if the player achieve the end, the world will show the solution and end
    if ((this.player.x == (MAZE_WIDTH_VERTEX - 1)) 
        && (this.player.y == (MAZE_HEIGHT_VERTEX - 1))) {
      this.searchSucess = true;
    }

    // animate the construction of the maze: on each tick, show a single wall being knocked down
    if (knockDoor < miniTree.size()) {
      arrayV.get(miniTree.get(knockDoor).from.x).get(miniTree
          .get(knockDoor).from.y).outVertex.add(miniTree.get(knockDoor).to);
      arrayV.get(miniTree.get(knockDoor).to.x).get(miniTree
          .get(knockDoor).to.y).outVertex.add(miniTree.get(knockDoor).from);
      knockDoor++;
    }

    //if search success or player win, the maze will show the final solution
    if (knockDoor == miniTree.size()) {
      this.solutionPathForD = new Utils().formSolutionDepth(arrayV.get(0).get(0), 
          arrayV.get(MAZE_WIDTH_VERTEX - 1).get(MAZE_HEIGHT_VERTEX - 1));
    }

    //calculate the wrong steps when searching or visiting
    this.depthStep = new Utils().calWrongStep(
        this.depthSearchList, this.solutionPathForD);

    this.breadthStep = new Utils().calWrongStep(
        this.breadthSearchList, this.solutionPathForD);

    this.playerStep = new Utils().calWrongStep(
        this.playerWalk, this.solutionPathForD);

  }

  // move player on key-press
  public void onKeyEvent(String ke) {
    //if press h, means we want to change to horizontal maze
    if (ke.equals("h")) {
      this.reset(1);
    }
    //if press h, means we want to change to vertical maze
    else if (ke.equals("r")) {
      this.reset(2);
    }
    //if press h, means we want to change to common maze
    else if (ke.equals("c")) {
      this.reset(0);
    }
    //if press d, means we want to see the depth-first search
    else if (ke.equals("d")) {
      if (this.showDepth) {
        this.showDepth = false;
      }
      else {
        this.showDepth = true;
      }
    }
    //if press d, means we want to see the breadth-first search
    else if (ke.equals("b")) {
      if ( this.showBreadth) {
        this.showBreadth = false;
      }
      else {
        this.showBreadth = true;
      }
    }
    //if press v, means we want to see the visited path
    else if (ke.equals("v")) {
      if ( this.viewPath) {
        this.viewPath = false;
      }
      else {
        this.viewPath = true;
      }
    }
    else {
      this.player.move(this, ke);
    }
  }

  // reset to create a new random maze again as your preferences 
  void reset(int want) {
    this.arrayV = new Utils().createAlAlVertices(MAZE_WIDTH_VERTEX, MAZE_HEIGHT_VERTEX);
    this.initalE = new Utils().createAlAlEdge(arrayV, want);
    new Utils().selectionSort(this.initalE, new CompareEdge());
    this.miniTree = new Utils().mimimumSpanningTree(this.initalE, new Utils().createIlist(arrayV));

    this.player = new Player(0, 0, arrayV.get(0).get(0));
    this.solutionPathForD = new ArrayList<Vertex>();  

    //for animating the search process, I added the field and designed search methods here
    this.depthSearchList = new ArrayList<Vertex>();
    this.worklistDepth = new Stack<Vertex>();
    worklistDepth.push(arrayV.get(0).get(0));

    this.breadthSearchList = new ArrayList<Vertex>();
    this.worklistBreadth = new Queue<Vertex>();
    worklistBreadth.enqueue(arrayV.get(0).get(0));
    this.playerWalk =  new ArrayList<Vertex>();

    this.showDepth = false;
    this.showBreadth = false;
    this.searchSucess = false;
    this.viewPath = false;

    this.knockDoor = 0;

    this.depthStep = 0;
    this.breadthStep = 0;
    this.playerStep = 0;

  }

  // produce the image of this world by adding the cell to the background image
  public WorldScene makeScene() {
    WorldScene result = new WorldScene(WIDTH, HEIGHT);
    WorldImage playerImage = new RectangleImage(VWIDTH, VHEIGHT,
        OutlineMode.SOLID, Color.WHITE);
    WorldImage begin = new RectangleImage(VWIDTH, VHEIGHT,
        OutlineMode.SOLID, Color.WHITE);
    WorldImage end = new RectangleImage(VWIDTH, VHEIGHT,
        OutlineMode.SOLID, Color.RED);
    for (int i = 0; i < arrayV.size(); i++) {
      for (int j = 0; j < arrayV.get(0).size(); j++) {
        result.placeImageXY(arrayV.get(i).get(j).getImage(this),
            arrayV.get(i).get(j).x * VWIDTH + VWIDTH / 2,
            arrayV.get(i).get(j).y * VHEIGHT + VHEIGHT / 2);
      }
    }
    //show depth search process
    if (this.showDepth) {
      for (Vertex v : this.depthSearchList) {
        result.placeImageXY(new RectangleImage(VWIDTH - 2, VWIDTH - 2,
            OutlineMode.SOLID, Color.BLUE),
            v.x * VWIDTH + VWIDTH / 2,
            v.y * VHEIGHT + VHEIGHT / 2);
      }
    }
    //show the breadth search process
    if (this.showBreadth) {
      for (Vertex v : this.breadthSearchList) {
        result.placeImageXY(new RectangleImage(VWIDTH - 2, VWIDTH - 2,
            OutlineMode.SOLID, Color.GREEN),
            v.x * VWIDTH + VWIDTH / 2,
            v.y * VHEIGHT + VHEIGHT / 2);
      }
    }
    result.placeImageXY(begin, VWIDTH / 2, VHEIGHT / 2);
    result.placeImageXY(end, (MAZE_WIDTH_VERTEX - 1) * VWIDTH + VWIDTH / 2,
        (MAZE_HEIGHT_VERTEX - 1) * VHEIGHT + VHEIGHT / 2);
    //show the player walk vertices 
    if (this.viewPath) {
      for (Vertex v : this.playerWalk) {
        result.placeImageXY(new RectangleImage(VWIDTH - 2, VWIDTH - 2,
            OutlineMode.SOLID, Color.YELLOW),
            v.x * VWIDTH + VWIDTH / 2,
            v.y * VHEIGHT + VHEIGHT / 2);
      }
    }
    result.placeImageXY(playerImage, player.x * VWIDTH + VWIDTH / 2, 
        player.y * VHEIGHT + VWIDTH / 2);
    //show the final one solution
    if (this.searchSucess) {
      for (Vertex v1 : this.solutionPathForD) {
        result.placeImageXY(new RectangleImage(VWIDTH - 2, VWIDTH - 2,
            OutlineMode.SOLID, Color.ORANGE),
            v1.x * VWIDTH + VWIDTH / 2,
            v1.y * VHEIGHT + VHEIGHT / 2);
      }
    }
    //show the wrong steps 
    WorldImage text = new TextImage(("Player Wrong Steps: " 
        + String.format("%d", this.playerStep)), HEIGHT * 0.03,
        FontStyle.BOLD, Color.RED);
    result.placeImageXY(text, WIDTH * 4 / 5, HEIGHT * 1 / 20);
    WorldImage text2 = new TextImage(("Depth Wrong Steps: " 
        + String.format("%d", this.depthStep)),  HEIGHT * 0.03,
        FontStyle.BOLD, Color.RED);
    result.placeImageXY(text2, WIDTH * 4 / 5, (int) (HEIGHT * 1 / 20 + HEIGHT * 0.03));
    WorldImage text3 = new TextImage(("Breadth Wrong Steps: " 
        + String.format("%d", this.breadthStep)),  HEIGHT *  0.03,
        FontStyle.BOLD, Color.RED);
    result.placeImageXY(text3, WIDTH * 4 / 5, (int) (HEIGHT * 1 / 20 + HEIGHT * 0.06));
    return result;
  }

  //determine if the world will end
  public WorldEnd worldEnds() {
    // if player pick up all things needed and escape
    if (this.searchSucess) {
      return new WorldEnd(true, this.lastScene("Solve The Maze Sucessfully"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // produce end-game image
  public WorldScene lastScene(String s) {
    WorldScene result = this.makeScene();
    result.placeImageXY(new TextImage(s, Color.RED), Maze.WIDTH_HALF,
        Maze.HEIGHT_HALF);
    return result;
  }
}


// represents a Utils class
class Utils {

  // according to the given width and height numbers of Vertices we want to
  // create ArrayList<ArrayList<Vertex>> to represent the basic cell in maze
  ArrayList<ArrayList<Vertex>> createAlAlVertices(int width, int height) {
    ArrayList<ArrayList<Vertex>> allAll = new ArrayList<ArrayList<Vertex>>(width);
    for (int i = 0; i < width; i++) {
      ArrayList<Vertex> all = new ArrayList<Vertex>(height);
      for (int j = 0; j < height; j++) {
        all.add(new Vertex(i, j, new ArrayList<Vertex>()));
      }
      allAll.add(all);
    }
    return allAll;
  }

  //calculate the numbers of different vertex between final unique solution 
  //and searches or visited vertex
  int calWrongStep(ArrayList<Vertex> list, ArrayList<Vertex> solutionPathForD) {
    int result = 0;
    for (Vertex v : list) {
      if (this.contains(solutionPathForD, v)) {
        result++;
      }
    }
    return list.size() - result;
  }


  // does this list of outVertex contains given vertex
  boolean contains(ArrayList<Vertex> outVertex, Vertex given) {
    boolean result = false;
    for (Vertex v : outVertex) {
      if (v.vEquals(given)) {
        result = true;
      }
    }
    return result;
  }

  // according to the given ArrayList<ArrayList<Vertex>> (all vertices),
  // it create the all edges that shows connection between all vertices.
  ArrayList<Edge> createAlAlEdge(ArrayList<ArrayList<Vertex>> whole, int want) {
    ArrayList<Edge> all = new ArrayList<Edge>();

    //create the original edges weights for showing the horizontal or vertical maze 
    //because we added more heavy weights on the vertical or horizontal edges,
    //hence, the minimum spanning tree will choose the horizontal or vertical edges more.

    // create every vertical connective edges
    for (int i = 0; i < whole.size(); i++) {
      for (int j = 0; j < whole.get(0).size() - 1; j++) {
        //if want is 1, means we need the horizontal maze (vertical edges have more weights)
        if (want == 1) {
          all.add(new Edge(this.createRanWeightMuch(), 
              whole.get(i).get(j), whole.get(i).get(j + 1)));
        }
        //if want is 2, means we need the vertical maze (vertical edges have common weights)
        else if (want == 2) {
          all.add(new Edge(this.createRanWeight(),
              whole.get(i).get(j), whole.get(i).get(j + 1)));
        }
        //if others, means we just need the common maze
        else {
          all.add(new Edge(this.createRanWeight(), 
              whole.get(i).get(j), whole.get(i).get(j + 1)));
        }
      }
    }

    // create every horizontal connective edges
    for (int k = 0; k < whole.size() - 1; k++) {
      for (int h = 0; h < whole.get(0).size(); h++) {
        //if want is 1, means we need the horizontal maze (vertical edges have more weights)
        if (want == 1) {
          all.add(new Edge(this.createRanWeight(),
              whole.get(k).get(h), whole.get(k + 1).get(h)));
        }
        //if want is 2, means we need the vertical maze (vertical edges have common weights)
        else if (want == 2) {
          all.add(new Edge(this.createRanWeightMuch(),
              whole.get(k).get(h), whole.get(k + 1).get(h)));
        }
        //if others, means we just need the common maze
        else {
          all.add(new Edge(this.createRanWeight(),
              whole.get(k).get(h), whole.get(k + 1).get(h)));
        }
      }
    }
    return all;
  }

  // create one random weight for edges basing on range of maze
  int createRanWeight() {
    int width = Maze.MAZE_WIDTH_VERTEX;
    int height = Maze.MAZE_HEIGHT_VERTEX;
    return new Random().nextInt(width * height);
  }

  // create one more heavy weight for edges basing on range of maze
  int createRanWeightMuch() {
    int width = Maze.MAZE_WIDTH_VERTEX;
    int height = Maze.MAZE_HEIGHT_VERTEX;
    return new Random().nextInt(width * height) + width * height * 3 / 2;
  }

  // EFFECT: modifies this given ArrayList
  // by using the selection sorted the give ArrayList<Edges>
  <T> void selectionSort(ArrayList<T> arr, IComparator<T> comp) {
    for (int i = 0; i < arr.size(); i = i + 1) {
      int minIdx = findMinIndex(arr, i, comp);
      swap(arr, i, minIdx);
    }
  }

  // EFFECT: modifies this given ArrayList
  // and swap the value of the given two index
  <T> void swap(ArrayList<T> arr, int index1, int index2) {
    T oldValueAtIndex2 = arr.get(index2);

    arr.set(index2, arr.get(index1));
    arr.set(index1, oldValueAtIndex2);
  }

  // return the minIndex of the given ArrayList
  <T> int findMinIndex(ArrayList<T> arr, int startFrom, IComparator<T> comp) {
    T minSoFar = arr.get(startFrom);
    int bestSoFar = startFrom;
    for (int i = startFrom; i < arr.size(); i = i + 1) {
      if (comp.compare(arr.get(i), minSoFar) < 0) {
        minSoFar = arr.get(i);
        bestSoFar = i;
      }
    }
    return bestSoFar;
  }

  // use Kruskal's algorithm and union/find data structure
  // to form the minimum spanning tree
  ArrayList<Edge> mimimumSpanningTree(ArrayList<Edge> initalE, IList<Vertex> listV) {

    HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
    // initialize every node's representative to itself
    for (Vertex v : listV) {
      map.put(v, v);
    }
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
    // all edges in graph, sorted by edge weights
    ArrayList<Edge> worklist = initalE;
    for (Edge e : worklist) {
      Vertex fromVR = new Utils().findR(map, e.from);
      Vertex fromTR = new Utils().findR(map, e.to);
      if (fromVR.vEquals(fromTR)) {
        // do nothing and ignore this edge,
        // which will form one cycle, discard it.
      }
      else {
        // add it will not create cycle,
        // add it and union vertices in this edge
        edgesInTree.add(e);
        union(map, fromVR, fromTR);
      }
    }
    return edgesInTree;
  }

  // find the given vertex's representations(root) in the given hashMap
  Vertex findR(HashMap<Vertex, Vertex> map, Vertex v) {
    if (map.get(v).vEquals(v)) {
      return map.get(v);
    }
    else {
      return new Utils().findR(map, map.get(v));
    }
  }

  // union two representations,
  // simply set the value of one representative's representative to the other.
  void union(HashMap<Vertex, Vertex> map, Vertex v1, Vertex v2) {
    map.put(v1, v2);
  }

  // change the given ArrayList to List
  IList<Vertex> createIlist(ArrayList<ArrayList<Vertex>> whole) {
    IList<Vertex> result = new MtList<Vertex>();
    for (int i = 0; i < whole.size(); i = i + 1) {
      for (int j = 0; j < whole.get(0).size(); j = j + 1) {
        result = new ConsList<Vertex>(whole.get(i).get(j), result);
      }
    }
    return result;
  }

  // I used this method to build the stable maze
  // EFFECT: modifies this given ArrayList and
  // according to the given ArrayList<Edge> -- minimum spanning trees,
  // to change everyEdges' connecting vertices
  void changeOutEdge(ArrayList<Edge> listE, ArrayList<ArrayList<Vertex>> listV) {
    for (Edge e : listE) {
      listV.get(e.from.x).get(e.from.y).outVertex.add(e.to);
      listV.get(e.to.x).get(e.to.y).outVertex.add(e.from);
    }
  }

  ArrayList<Vertex> formSolutionDepth(Vertex start, Vertex end) {

    // This map is used to reconstruct the path from the source to the given
    // target node, simply by following the edges backward, from the target
    // node to the node that it came from, and so on back to the source node
    HashMap<Vertex, Vertex> cameFromVertex = new HashMap<Vertex, Vertex>();
    ArrayList<Vertex> alreadySeen = new ArrayList<Vertex>();
    Stack<Vertex> worklist = new Stack<Vertex>();
    worklist.push(start);
    cameFromVertex.put(start, start);
    // Initialize the worklist with the starting vertex
    while (!worklist.isEmpty()) {
      Vertex next = worklist.pop();
      if (alreadySeen.contains(next)) {
        // do nothing: we've already seen this one
      }
      else if (next.vEquals(end)) {
        return this.reconstruct(cameFromVertex, next);
      }
      else {
        // add all the neighbors of next to the work list for further processing
        for (Vertex v : next.outVertex) {
          // add next to alreadySeen, since we're done with it
          if (alreadySeen.contains(v)) {
            // without any change
          } 
          else {
            worklist.push(v);
            cameFromVertex.put(v, next);
          }
        }
        alreadySeen.add(next);
      }
    }
    return this.reconstruct(cameFromVertex, end);

  }

  // record all the important vertex and transfer them to one solution path
  ArrayList<Vertex> reconstruct(HashMap<Vertex, Vertex> cameFromVertex, Vertex next) {
    return this.reconstructHelp(cameFromVertex, next, new ArrayList<Vertex>());
  }

  // reconstruct helper method because we need accumulator
  ArrayList<Vertex> reconstructHelp(HashMap<Vertex, Vertex> map, Vertex next,
      ArrayList<Vertex> array) {
    if (map.get(next).vEquals(next)) {
      array.add(next);
      return array;
    }
    else {
      array.add(next);
      return this.reconstructHelp(map, map.get(next), array);
    }
  }

}


//the following part we used are homework and lab codes we write in the past

// to represent an abstract ANode
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // counts the number of nodes in this ANode
  abstract int size();

  // help method for counting
  abstract int sizeHelp();

  // EFFECT: modifies this ANode field to refer to the given T, ANode, ANode
  void addHelper(T t, ANode<T> t1, ANode<T> t2) {
    new Node<T>(t, t1, t2);
  }

  // get the value of the T that remove from the deque
  abstract T removeHelper();

  // EFFECT: modifies this ANode field to remove this in Sentinel
  abstract void removeNodeHelp(Sentinel<T> s);

  // EFFECT: modifies this ANode field to remove the given T
  abstract void removeNode(T t);

  // determines whether this ANode is Node
  abstract boolean isNode();

  // cast this ANode as Node
  abstract Node<T> asNode();

  // Gets the next element of this deque
  ANode<T> getNext() {
    return this.next;
  }

  // return the data value if this ANode is Node
  abstract T getData();
}

// to represent a sentinel
class Sentinel<T> extends ANode<T> {

  // a new constructor that takes zero augment
  Sentinel() {
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  // help method for counting nodes
  int sizeHelp() {
    return 0;
  }

  // counts the number of nodes in this sentinel
  public int size() {
    return this.next.sizeHelp();
  }

  // consumes a value of type T and inserts it at the front of the list
  void addAthead(T t) {
    this.addHelper(t, this.next, this);

  }

  // consumes a value of type T and inserts it at the tail of this list
  void addAtTail(T t) {
    this.addHelper(t, this, this.prev);
  }

  // get the removed value T
  T removeFromHead() {
    return this.next.removeHelper();
  }

  // removes the last node from this Deque
  T removeFromTail() {
    return this.prev.removeHelper();
  }

  // throw a RunTimeException if this is a Sentinel
  T removeHelper() {
    throw new RuntimeException("cannot remove the Node from empty list");
  }

  // find the suitable node according to given IPred
  ANode<T> find(IPred<T> pred) {
    return this;
  }

  // EFFECT: modifies this Sentinel field without change
  void removeNodeHelp(Sentinel<T> s) {
    return;
  }

  // EFFECT: modifies this Sentinel field to remove a Node based on the given T,
  // and stop when the next field is null
  void removeNode(T t) {
    // use the side method better
    if (!this.next.isNode()) {
      return;
    }
    else {
      this.next.removeNode(t);
    }
  }

  // determines whether this Sentinel is Node
  boolean isNode() {
    return false;
  }

  // cast this Sentinel as Node
  Node<T> asNode() {
    throw new RuntimeException("I'm not a node!");
  }

  // EFFECT: modifies this Sentinel field
  // and connect this Sentinel with the given Sentinel
  void append(Sentinel<T> s) {
    this.prev.next = s.next;
    s.next.prev = this.prev;
    this.prev = s.prev;
    s.prev.next = this;
  }

  // return the data value if this is Node
  T getData() {
    return null;
  }
}

// to represent a node
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    super(null, null);
    this.data = data;
  }
  // This will always throw an exception

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    if (next == null || prev == null) {
      throw new IllegalArgumentException("cannot find suitable node");
    }
    else {
      this.data = data;
      this.prev.next = this;
      this.next.prev = this;
    }
  }

  // compute the size of this Node
  public int size() {
    return this.next.size();
  }

  // compute this size of this Node
  int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }

  // remove the Node and return the removed Node's value T
  T removeHelper() {
    T want = this.data;
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return want;

  }

  // EFFECT: modifies the given Sentinel field to remove this Node in the given
  // Sentinel
  void removeNodeHelp(Sentinel<T> s) {
    s.removeNode(this.data);
  }

  // EFFECT: modifies the Node field to remove the Node that has the same value
  // with the given T
  void removeNode(T t) {
    if (this.data.equals(t)) {
      this.removeHelper();
    }
    else if (!this.next.isNode()) {
      return;
    }
    else {
      this.next.removeNode(t);
    }
  }

  // determines whether this Node is Node
  boolean isNode() {
    return true;
  }

  // cast this Node as Node
  Node<T> asNode() {
    return this;
  }

  // return the data value
  T getData() {
    return this.data;
  }
}

// to represent a deque
class Deque<T> implements Iterable<T> {
  Sentinel<T> header;

  // constructor 1
  Deque() {
    this.header = new Sentinel<T>();
  }

  // constructor 2
  Deque(Sentinel<T> value) {
    this.header = value;
  }

  // EFFECT: modifies the given Node field to
  // connect this Node with the given Deque
  void append(Deque<T> give) {
    this.header.append(give.header);
  }

  // counts the number of nodes in a list Deque
  int size() {
    return this.header.size();
  }

  // consumes a value of type T and inserts it at the front of the list
  void addAtHead(T t) {
    this.header.addAthead(t);
  }

  // consumes a value of type T and inserts it at the tail of this list
  void addAtTail(T t) {
    this.header.addAtTail(t);
  }

  // removes the first node from this Deque
  T removeFromHead() {
    return this.header.removeFromHead();
  }

  // removes the last node from this Deque
  T removeFromTail() {
    return this.header.removeFromTail();
  }

  // EFFECT: modifies the given Node field and
  // removes the given node from this Deque
  void removeNode(ANode<T> n) {
    n.removeNodeHelp(this.header);
  }

  // return a new ForwardDequeIterator
  public Iterator<T> iterator() {
    return new ForwardDequeIterator<T>(this);
  }
}

// represents ForwardDequeIterator class
class ForwardDequeIterator<T> implements Iterator<T> {
  ANode<T> curr;

  ForwardDequeIterator(Deque<T> deque) {
    // this.curr = deque.header.next;
    this.curr = deque.header.getNext();
  }

  // determines whether this curr has next value
  public boolean hasNext() {
    return curr.isNode();
  }

  // return the next value
  public T next() {
    T next = curr.getData();
    // change to next one, like loop
    curr = curr.next;
    return next;
  }

}

// represents Stack class
class Stack<T> {
  Deque<T> contents;

  Stack(Deque<T> contents) {
    this.contents = contents;
  }

  Stack() {
    this(new Deque<T>());
  }

  // EFFECT: modifies the Stack field and
  // adds an item to the head of the list
  void push(T item) {
    this.contents.addAtHead(item);
  }

  // determines whether this Stack's filed is empty
  boolean isEmpty() {
    return this.contents.size() == 0;
  }

  // removes and returns the head of the list
  T pop() {
    return this.contents.removeFromHead();
  }

}

// represents Queue class
class Queue<T> {
  Deque<T> contents;

  Queue(Deque<T> contents) {
    this.contents = contents;
  }

  Queue() {
    this(new Deque<T>());
  }

  // EFFECT: modifies the Queue field and
  // adds an item to the tail of the list
  void enqueue(T item) {
    this.contents.addAtTail(item);
  }

  // determines whether this Queue's filed is empty
  boolean isEmpty() {
    return this.contents.size() == 0;
  }

  // removes and returns the head of the list
  T dequeue() {
    return this.contents.removeFromHead();
  }

}

// Examples of maze
class ExamplesMaze {

  // test that we can run two different animations concurrently
  // with the events directed to the correct version of the world
  /*
   * example of maze Maze m = new Maze();
   * 
   * boolean runAnimation = this.m.bigBang(Maze.WIDTH, Maze.HEIGHT, 0.1);
   */

  // RUN THE GAME
  void testRunGame(Tester t) {
    // run the game
    Maze m = new Maze();

    m.bigBang(Maze.WIDTH, Maze.HEIGHT, 0.01);
  }

  Maze maze;

  Node<String> abc;
  Node<String> bcd;
  Node<String> cde;
  Node<String> def;
  Node<String> cat;
  Node<String> bat;
  Node<String> alarm;
  Node<String> deep;
  Node<String> foot;
  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;

  Sentinel<String> mt = new Sentinel<String>();
  Sentinel<String> s1 = new Sentinel<String>();
  Sentinel<String> s2 = new Sentinel<String>();

  Stack<String> st1;
  Stack<String> st2;

  Queue<String> d1;
  Queue<String> d2;

  void initTestConditions() {
    this.maze = new Maze();

    this.abc = new Node<String>("abc", s1, s1);
    this.bcd = new Node<String>("bcd", s1, abc);
    this.cde = new Node<String>("cde", s1, bcd);
    this.def = new Node<String>("def", s1, cde);

    this.cat = new Node<String>("cat", s2, s2);
    this.bat = new Node<String>("bat", s2, cat);
    this.deep = new Node<String>("deep", s2, bat);
    this.alarm = new Node<String>("alarm", s2, deep);
    this.foot = new Node<String>("foot", s2, alarm);

    this.deque1 = new Deque<String>(mt);
    this.deque2 = new Deque<String>(s1);
    this.deque3 = new Deque<String>(s2);

    this.st1 = new Stack<String>(this.deque2);
    this.st2 = new Stack<String>(this.deque3);

    this.d1 = new Queue<String>(this.deque2);
    this.d2 = new Queue<String>(this.deque3);
  }

  // test the method filter
  boolean testFilter(Tester t) {
    IList<Double> l1 = new MtList<Double>();
    IList<Double> l2 = new ConsList<Double>(12.0,
        new ConsList<Double>(200.0, new ConsList<Double>(100.2, l1)));
    IList<Double> l3 = new ConsList<Double>(12.0,
        new ConsList<Double>(20.0, new ConsList<Double>(10.2, l1)));
    LargerThan100 p = new LargerThan100();

    return t.checkExpect(l1.filter(p), l1)
        && t.checkExpect(l2.filter(p), new ConsList<Double>(200.0, new ConsList<Double>(100.2, l1)))
        && t.checkExpect(l3.filter(p), l1);
  }

  // test the method map
  boolean testMap(Tester t) {
    IList<Integer> l1 = new MtList<Integer>();
    IList<Integer> l2 = new ConsList<Integer>(1,
        new ConsList<Integer>(2, new ConsList<Integer>(3, l1)));
    IList<Posn> p1 = new MtList<Posn>();
    IList<Posn> p2 = new ConsList<Posn>(new Posn(-9, 11),
        new ConsList<Posn>(new Posn(-8, 12), new ConsList<Posn>(new Posn(-7, 13), p1)));
    MakePosn f = new MakePosn();

    return t.checkExpect(l1.map(f), p1) && t.checkExpect(l2.map(f), p2);
  }

  // test the method size
  boolean testSize2(Tester t) {
    IList<Double> l1 = new MtList<Double>();
    IList<Double> l2 = new ConsList<Double>(12.0,
        new ConsList<Double>(200.0, new ConsList<Double>(100.2, l1)));
    IList<Double> l3 = new ConsList<Double>(12.0, new ConsList<Double>(10.2, l1));

    return t.checkExpect(l1.size(), 0) && t.checkExpect(l2.size(), 3)
        && t.checkExpect(l3.size(), 2);
  }

  // test the method isCons
  boolean testIsCons(Tester t) {
    ConsList<String> los = new ConsList<String>("I", new MtList<String>());
    return t.checkExpect(los.isCons(), true) && t.checkExpect(new MtList<String>().isCons(), false);
  }

  // test the method asCons
  boolean testAsCons(Tester t) {
    ConsList<String> los = new ConsList<String>("I", new MtList<String>());
    return t.checkExpect(los.asCons(), los) && t
        .checkException(new RuntimeException("I'm not a Cons!"), new MtList<String>(), "asCons");
  }

  // test the method iterator
  // that include the method next and hasNext
  boolean testIterator(Tester t) {
    IList<Posn> lop1 = new MtList<Posn>();
    IList<Posn> lop2 = new ConsList<Posn>(new Posn(1, 1), lop1);
    IList<Double> lod1 = new MtList<Double>();
    IList<Double> lod2 = new ConsList<Double>(12.0, new ConsList<Double>(10.2, lod1));
    ListIterator<Double> l = new ListIterator<Double>(lod2);
    return t.checkExpect(lop1.iterator().hasNext(), false)
        && t.checkExpect(lop2.iterator().hasNext(), true)
        && t.checkExpect(lod1.iterator().hasNext(), false)
        && t.checkExpect(lod2.iterator().hasNext(), true)
        && t.checkExpect(lod2.iterator().next(), 12.0)
        && t.checkExpect(lop2.iterator().next(), new Posn(1, 1)) && t.checkExpect(l.hasNext(), true)
        && t.checkExpect(l.next(), 12.0);
  }

  // test the method vEquals
  boolean testVEquals(Tester t) {
    Vertex v1 = new Vertex(1, 2, null);
    Vertex v2 = new Vertex(2, 2, null);
    Vertex v3 = new Vertex(3, 2, null);
    return t.checkExpect(v1.vEquals(v2), false) && t.checkExpect(v2.vEquals(v2), true)
        && t.checkExpect(v3.vEquals(v1), false) && t.checkExpect(v2.vEquals(v3), false);
  }

  // test the method move
  void testMove(Tester t) {
    this.initTestConditions();
    Vertex v1 = new Vertex(9, 7, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(9, 8, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(9, 9, new ArrayList<Vertex>());
    Vertex v4 = new Vertex(8, 8, new ArrayList<Vertex>());
    ArrayList<Vertex> al = new ArrayList<Vertex>();
    al.add(v1);
    al.add(v2);
    al.add(v3);
    al.add(v4);
    Vertex v = new Vertex(10, 10, al);
    Player player = new Player(9, 8, v);
    t.checkExpect(player.y, 8);
    player.move(maze, "up");
    t.checkExpect(player.y, 7);
  }

  // test the method createAlAlVertices
  boolean testCreateAlAlVertices(Tester t) {
    Utils u = new Utils();

    return t.checkExpect(u.createAlAlVertices(0, 0).size(), 0)
        && t.checkExpect(u.createAlAlVertices(100, 200).size(), 100)
        && t.checkExpect(u.createAlAlVertices(100, 200).get(1).get(10).outVertex,
            new ArrayList<Vertex>())
            && t.checkExpect(u.createAlAlVertices(100, 200).get(90).get(100).outVertex,
                new ArrayList<Vertex>())
                && t.checkExpect(u.createAlAlVertices(100, 200).get(90).get(100).x, 90)
                && t.checkExpect(u.createAlAlVertices(100, 200).size(), 100)
                && t.checkExpect(u.createAlAlVertices(200, 0).get(1).size(), 0)
                && t.checkExpect(u.createAlAlVertices(200, 0).size(), 200)
                && t.checkExpect(u.createAlAlVertices(0, 100).size(), 0);

  }

  // test the method Contains
  boolean testContains(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, null);
    Vertex v2 = new Vertex(200, 30, null);
    ArrayList<Vertex> lv1 = u.createAlAlVertices(100, 200).get(1);
    ArrayList<Vertex> lv2 = u.createAlAlVertices(300, 300).get(200);

    return t.checkExpect(u.contains(lv1, v1), true) && t.checkExpect(u.contains(lv1, v2), false)
        && t.checkExpect(u.contains(lv2, v1), false) && t.checkExpect(u.contains(lv2, v2), true);

  }

  // test the method createRanWeight
  boolean testCreateRanWeight(Tester t) {
    Utils u = new Utils();

    return t.checkExpect(u.createRanWeight() >= 0, true)
        && t.checkExpect(u.createRanWeight() <= 6000, true)
        && t.checkExpect(u.createRanWeight() > 6000, false)
        && t.checkExpect(u.createRanWeight() < 0, false);

  }

  // test the method createAlAlEdge
  boolean testCreateAlAlEdge(Tester t) {
    Utils u = new Utils();
    ArrayList<ArrayList<Vertex>> lv1 = new ArrayList<ArrayList<Vertex>>();
    ArrayList<ArrayList<Vertex>> lv2 = u.createAlAlVertices(100, 200);

    return t.checkExpect(u.createAlAlEdge(lv1, 0).size(), 0)
        && t.checkExpect(u.createAlAlEdge(lv2, 0).get(1).from,
            new Vertex(0, 1, new ArrayList<Vertex>()))
            && t.checkExpect(u.createAlAlEdge(lv2, 0).get(1).to, 
                new Vertex(0, 2, new ArrayList<Vertex>()))
                && t.checkExpect(u.createAlAlEdge(lv2, 0).get(11000).from,
                    new Vertex(55, 55, new ArrayList<Vertex>()))
                    && t.checkExpect(u.createAlAlEdge(lv2, 0).get(11000).to,
                        new Vertex(55, 56, new ArrayList<Vertex>()));
  }

  // test the method swap
  void testSwap(Tester t) {
    Utils u = new Utils();
    ArrayList<String> los1 = new ArrayList<String>();
    los1.add("hello");
    los1.add("hi");
    los1.add("ahh");

    t.checkExpect(los1.get(1), "hi");
    t.checkExpect(los1.get(2), "ahh");
    u.swap(los1, 1, 2);
    t.checkExpect(los1.get(1), "ahh");
    t.checkExpect(los1.get(2), "hi");
  }

  // test the method findMinIndex
  boolean testFindMinIndex(Tester t) {
    Utils u = new Utils();
    CompareEdge comp = new CompareEdge();
    ArrayList<Edge> loe1 = u.createAlAlEdge(u.createAlAlVertices(1, 2), 0);
    ArrayList<Edge> loe2 = u.createAlAlEdge(u.createAlAlVertices(2, 1), 0);
    ArrayList<Edge> loe3 = u.createAlAlEdge(u.createAlAlVertices(10, 20), 0);

    return t.checkExpect(u.findMinIndex(loe1, 0, comp), 0)
        && t.checkExpect(u.findMinIndex(loe2, 0, comp), 0)
        && t.checkExpect(u.findMinIndex(loe3, 20, comp) >= 20, true)
        && t.checkExpect(u.findMinIndex(loe3, 30, comp) >= 30, true);
  }

  // test the method selectionSort
  void testSelectionSort(Tester t) {
    Utils u = new Utils();
    CompareEdge comp = new CompareEdge();
    this.initTestConditions();
    ArrayList<Edge> loe = maze.initalE;

    u.selectionSort(loe, comp);
    t.checkExpect(loe.get(0).weight <= loe.get(1).weight, true);
    t.checkExpect(loe.get(2).weight <= loe.get(3).weight, true);
    t.checkExpect(loe.get(20).weight > loe.get(32).weight, false);
  }

  // test the method mimimumSpanningTree
  boolean testMimimumSpanningTree(Tester t) {
    Utils u = new Utils();
    CompareEdge comp = new CompareEdge();
    this.initTestConditions();
    ArrayList<Edge> loeOriginal = maze.miniTree;
    ArrayList<Edge> loeSorted = maze.miniTree;
    u.selectionSort(loeSorted, comp);
    return t.checkExpect(loeOriginal.size(), 
        Maze.MAZE_HEIGHT_VERTEX * Maze.MAZE_WIDTH_VERTEX - 1)
        && t.checkExpect(loeOriginal.get(10).weight == loeSorted.get(10).weight, true);
  }

  // test the method findR
  boolean testFindR(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(1, 2, new ArrayList<Vertex>());
    HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
    map.put(v1, v2);
    map.put(v2, v3);
    map.put(v3, v1);
    return t.checkExpect(u.findR(map, v1), v2) && t.checkExpect(u.findR(map, v2), v3)
        && t.checkExpect(u.findR(map, v3), v1);
  }

  // test the method union
  void testUnion(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(1, 2, new ArrayList<Vertex>());
    HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
    map.put(v1, v2);
    map.put(v2, v3);

    t.checkExpect(map.get(v1), v2);
    t.checkExpect(map.get(v2), v3);
    t.checkExpect(map.get(v3), null);
    u.union(map, v3, v1);
    t.checkExpect(map.get(v3), v1);
  }

  // test the method createIlist
  boolean testCreateIlist(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(2, 3, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(3, 4, new ArrayList<Vertex>());
    ArrayList<ArrayList<Vertex>> loiEmpty = new ArrayList<ArrayList<Vertex>>();
    ArrayList<ArrayList<Vertex>> loiCons = new ArrayList<ArrayList<Vertex>>();
    ArrayList<Vertex> loi = new ArrayList<Vertex>();
    loi.add(v1);
    loi.add(v2);
    loi.add(v3);
    loiCons.add(loi);

    return t.checkExpect(u.createIlist(loiEmpty), new MtList<Vertex>())
        && t.checkExpect(u.createIlist(loiCons), new ConsList<Vertex>(v3,
            new ConsList<Vertex>(v2, new ConsList<Vertex>(v1, new MtList<Vertex>()))));
  }

  // test the method changeOutEdge
  void testChangeOutEdge(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(0, 0, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(1, 1, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(0, 1, new ArrayList<Vertex>());
    Vertex v4 = new Vertex(1, 0, new ArrayList<Vertex>());
    ArrayList<ArrayList<Vertex>> loiCons = new ArrayList<ArrayList<Vertex>>();
    ArrayList<Vertex> loi0 = new ArrayList<Vertex>();
    ArrayList<Vertex> loi1 = new ArrayList<Vertex>();
    loi0.add(v1);
    loi0.add(v3);
    loi1.add(v4);
    loi1.add(v2);
    loiCons.add(loi0);
    loiCons.add(loi1);
    ArrayList<Edge> loe = new ArrayList<Edge>();
    Edge e1 = new Edge(10, v1, v2);
    Edge e2 = new Edge(10, v2, v3);
    Edge e3 = new Edge(20, v2, v4);
    loe.add(e1);
    loe.add(e2);
    loe.add(e3);

    t.checkExpect(loiCons.get(0).get(1).outVertex, new ArrayList<Vertex>());
    t.checkExpect(loiCons.get(1).get(0).outVertex, new ArrayList<Vertex>());
    u.changeOutEdge(loe, loiCons);
    t.checkExpect(loiCons.get(0).get(1).outVertex.size(), 1);
    t.checkExpect(loiCons.get(1).get(0).outVertex.size(), 1);
  }

  //  //we test this method successfully before we animating the construction of the maze
  //  //now we cannot test it because at the beginning of world, there is not one path can be found
  //  //and the maze is just starting to construct
  //  // test the method formSolutionDepth
  //  boolean testFormSolutionDepth(Tester t) {
  //    Utils u = new Utils();
  //    this.initTestConditions();
  //    this.maze.searchSucess = true;
  //    Vertex start = this.maze.arrayV.get(0).get(0);
  //    Vertex end = this.maze.arrayV.get(Maze.MAZE_WIDTH_VERTEX - 1).
  //        get(Maze.MAZE_HEIGHT_VERTEX);
  //    return t.checkExpect(u.formSolutionDepth(start, end).contains(start), true)
  //        && t.checkExpect(u.formSolutionDepth(start, end).contains(end), true);
  //  }


  // test the method reconstruct
  boolean testReconstruct(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(1, 2, new ArrayList<Vertex>());
    HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
    map.put(v1, v2);
    map.put(v2, v3);

    return t.checkExpect(new ArrayList<Vertex>().size(), 0)
        && t.checkExpect(u.reconstruct(map, v1).size(), 1)
        && t.checkExpect(u.reconstruct(map, v2).size(), 1);
  }

  // test the method reconstructHelp
  boolean testReconstructHelp(Tester t) {
    Utils u = new Utils();
    Vertex v1 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v2 = new Vertex(1, 2, new ArrayList<Vertex>());
    Vertex v3 = new Vertex(1, 2, new ArrayList<Vertex>());
    HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
    map.put(v1, v2);
    map.put(v2, v3);

    return t.checkExpect(new ArrayList<Vertex>().size(), 0)
        && t.checkExpect(u.reconstructHelp(map, v1, new ArrayList<Vertex>()).size(), 1)
        && t.checkExpect(u.reconstructHelp(map, v2, new ArrayList<Vertex>()).size(), 1);
  }

  // test the method size in deque class
  boolean testSize(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.deque1.size(), 0) && t.checkExpect(this.deque2.size(), 4)
        && t.checkExpect(this.deque3.size(), 5) && t.checkExpect(this.deque1.header.sizeHelp(), 0)
        && t.checkExpect(this.deque2.header.sizeHelp(), 0)
        && t.checkExpect(this.deque3.header.sizeHelp(), 0);
  }

  // test the method addAtHead
  void testAddAtHead(Tester t) {

    this.deque1.addAtHead("abc");
    t.checkExpect(this.deque1.removeFromHead(), "abc");
    this.deque2.addAtHead("zab");
    t.checkExpect(this.deque2.removeFromHead(), "zab");
    this.deque3.addAtHead("bcd");
    t.checkExpect(this.deque3.removeFromHead(), "bcd");

  }

  // test the method addAtTail
  void testAddAtTail(Tester t) {
    this.initTestConditions();

    this.deque1.addAtTail("abc");
    t.checkExpect(this.deque1.removeFromTail(), "abc");
    this.deque2.addAtTail("zab");
    t.checkExpect(this.deque2.removeFromTail(), "zab");
    this.deque3.addAtTail("bcd");
    t.checkExpect(this.deque3.removeFromTail(), "bcd");

  }

  // test the method removeFromHead
  boolean testRemoveFromHead(Tester t) {
    this.initTestConditions();

    return t.checkException(new RuntimeException("cannot remove the Node from empty list"),
        this.deque1, "removeFromHead") && t.checkExpect(this.deque2.removeFromHead(), "abc")
        && t.checkExpect(this.deque3.removeFromHead(), "cat")
        && t.checkExpect(this.deque2.header.next.removeHelper(), "bcd")
        && t.checkExpect(this.deque2.header.next.removeHelper(), "cde");
  }

  // test the method removeFromTail
  boolean testRemoveFromTail(Tester t) {
    this.initTestConditions();

    return t.checkException(new RuntimeException("cannot remove the Node from empty list"),
        this.deque1, "removeFromTail") && t.checkExpect(this.deque2.removeFromTail(), "def")
        && t.checkExpect(this.deque3.removeFromTail(), "foot");
  }

  // test the method isNode
  boolean testIsNode(Tester t) {
    this.initTestConditions();

    return t.checkExpect(this.cat.isNode(), true) && t.checkExpect(this.bat.isNode(), true)
        && t.checkExpect(this.s1.isNode(), false) && t.checkExpect(this.s2.isNode(), false);
  }

  // test the method asNode
  boolean testAsNode(Tester t) {
    this.initTestConditions();

    return t.checkException(new RuntimeException("I'm not a node!"), this.s1, "asNode")
        && t.checkExpect(this.cat.asNode(), this.cat) && t.checkExpect(this.bat.asNode(), this.bat);
  }

  // test the method getNext
  boolean testGetNext(Tester t) {
    this.initTestConditions();

    return t.checkExpect(this.cat.getNext(), this.bat)
        && t.checkExpect(this.bat.getNext(), this.deep)
        && t.checkExpect(this.s2.getNext(), this.cat);
  }

  // test the method getData
  boolean testGetData(Tester t) {
    this.initTestConditions();

    return t.checkExpect(this.cat.getData(), "cat") && t.checkExpect(this.bat.getData(), "bat")
        && t.checkExpect(this.s2.getData(), null);
  }

  // test the method append
  void testAppend(Tester t) {
    this.initTestConditions();

    t.checkExpect(this.s1.size(), 4);
    this.s1.append(this.s2);
    t.checkExpect(this.s1.size(), 9);
  }

  // test the method iterator
  boolean testIterator2(Tester t) {
    this.initTestConditions();

    return t.checkExpect(this.deque1.iterator().hasNext(), false)
        && t.checkExpect(this.deque2.iterator().hasNext(), true)
        && t.checkExpect(this.deque3.iterator().hasNext(), true)
        && t.checkExpect(this.deque2.iterator().next(), "abc")
        && t.checkExpect(this.deque3.iterator().next(), "cat");
  }

  // test the method push in Stack class
  void testPush(Tester t) {
    this.initTestConditions();

    t.checkExpect(st1.contents.size(), 4);
    t.checkExpect(st2.contents.size(), 5);
    st1.push("Bob");
    st2.push("Wow");
    t.checkExpect(st1.contents.size(), 5);
    t.checkExpect(st2.contents.size(), 6);
  }

  // test the method isEmpty in Stack class
  boolean testIsEmpty(Tester t) {
    this.initTestConditions();
    Stack<String> s1 = new Stack<String>();

    return t.checkExpect(s1.isEmpty(), true) && t.checkExpect(st1.isEmpty(), false)
        && t.checkExpect(st2.isEmpty(), false);
  }

  // test the method pop in Stack class
  boolean testPop(Tester t) {
    this.initTestConditions();

    return t.checkExpect(st1.pop(), "abc") && t.checkExpect(st2.pop(), "cat");
  }

  // test the method enqueue in Queue class
  void testEnqueue(Tester t) {
    this.initTestConditions();

    t.checkExpect(d1.contents.size(), 4);
    t.checkExpect(d2.contents.size(), 5);
    d1.enqueue("Bob");
    d2.enqueue("Wow");
    t.checkExpect(d1.contents.size(), 5);
    t.checkExpect(d2.contents.size(), 6);
  }

  // test the method isEmpty in Queue class
  boolean testIsEmpty1(Tester t) {
    this.initTestConditions();
    Queue<String> s1 = new Queue<String>();

    return t.checkExpect(s1.isEmpty(), true) && t.checkExpect(d1.isEmpty(), false)
        && t.checkExpect(d2.isEmpty(), false);
  }

  // test the method dequeue in Queue class
  boolean testDequeue(Tester t) {
    this.initTestConditions();

    return t.checkExpect(d1.dequeue(), "abc") && t.checkExpect(d2.dequeue(), "cat");
  }
}