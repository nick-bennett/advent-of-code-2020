package com.nickbenn.advent.day20;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Tile {

  private static final Transform IDENTITY = (data, row, col) -> data[row][col];
  private static final Transform RIGHT_ROTATION =
      (data, row, col) -> data[data.length - col - 1][row];
  private static final Transform EVEN =
      (data, row, col) -> data[data.length - row - 1][data.length - col - 1];
  private static final Transform LEFT_ROTATION =
      (data, row, col) -> data[col][data.length - row - 1];
  private static final Transform MAIN_TRANSPOSITION = (data, row, col) -> data[col][row];
  private static final Transform HORIZONTAL_FLIP =
      (data, row, col) -> data[row][data.length - col - 1];
  private static final Transform COUNTER_TRANSPOSITION =
      (data, row, col) -> data[data.length - col - 1][data.length - row - 1];
  private static final Transform VERTICAL_FLIP =
      (data, row, col) -> data[data.length - row - 1][col];

  private static final List<Transform> transforms = List.of(
      IDENTITY, RIGHT_ROTATION, EVEN, LEFT_ROTATION,
      MAIN_TRANSPOSITION, HORIZONTAL_FLIP, COUNTER_TRANSPOSITION, VERTICAL_FLIP
  );

  private final int id;
  private final boolean[][] data;
  private final int[] edgeValues;
  private final Set<Integer> edges;

  public Tile(int id, boolean[][] data) {
    this.id = id;
    this.data = data;
    edgeValues = new int[Edge.values().length];
    for (Edge edge : Edge.values()) {
      edgeValues[edge.ordinal()] = sliceValue(edge.index, edge.horizontal, edge.naturalOrder);
    }
    edges = IntStream.of(edgeValues)
        .boxed()
        .collect(Collectors.toUnmodifiableSet());
  }

  public int getEdge(Edge edge) {
    return edgeValues[edge.ordinal()];
  }

  public int getId() {
    return id;
  }

  public int size() {
    return data.length;
  }

  public Set<Integer> getEdges() {
    return edges;
  }

  public boolean[][] getData() {
    return data;
  }

  public Tile match(Edge edge, int value) {
    Tile variation = null;
    for (Transform transform : transforms) {
      variation = getTransformedInstance(transform);
      if (variation.getEdge(edge) == value) {
        break;
      }
    }
    return variation;
  }

  public Iterable<Tile> variations() {
    return TileIterator::new;
  }

  private int sliceValue(int index, boolean horizontal, boolean naturalOrder) {
    int value = 0;
    int start = 0;
    int finish = data.length;
    int step = 1;
    if (!naturalOrder) {
      start = data.length - 1;
      finish = -1;
      step = -1;
    }
    index = (data.length + index) % data.length;
    if (horizontal) {
      for (int col = start; col != finish; col += step) {
        value *= 2;
        value += data[index][col] ? 1 : 0;
      }
    } else {
      for (int row = start; row != finish; row += step) {
        value *= 2;
        value += data[row][index] ? 1 : 0;
      }
    }
    return value;
  }

  private Tile getTransformedInstance(Transform transform) {
    boolean[][] data = new boolean[this.data.length][this.data.length];
    for (int row = 0; row < data.length; row++) {
      for (int col = 0; col < data.length; col++) {
        data[row][col] = transform.get(this.data, row, col);
      }
    }
    return new Tile(id, data);
  }

  public class TileIterator implements Iterator<Tile> {

    private final Iterator<Transform> transformIterator = transforms.iterator();

    @Override
    public boolean hasNext() {
      return transformIterator.hasNext();
    }

    @Override
    public Tile next() {
      return Tile.this.getTransformedInstance(transformIterator.next());
    }

  }

  public enum Edge {

    TOP_LEFT_TO_RIGHT(0, true, true),
    TOP_RIGHT_TO_LEFT(0, true, false),
    BOTTOM_LEFT_TO_RIGHT(-1, true, true),
    BOTTOM_RIGHT_TO_LEFT(-1, true, false),
    LEFT_TOP_TO_BOTTOM(0, false, true),
    LEFT_BOTTOM_TO_TOP(0, false, false),
    RIGHT_TOP_TO_BOTTOM(-1, false, true),
    RIGHT_BOTTOM_TO_TOP(-1, false, false);

    private final int index;
    private final boolean horizontal;
    private final boolean naturalOrder;

    Edge(int index, boolean horizontal, boolean naturalOrder) {
      this.index = index;
      this.horizontal = horizontal;
      this.naturalOrder = naturalOrder;
    }

  }

  private interface Transform {

    boolean get(boolean[][] data, int row, int col);

  }

}
