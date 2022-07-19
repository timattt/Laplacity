package steelUnicorn.laplacity.field.tiles;

public enum TileType {
    nonPhysical,
    barrier,
    wall,
    deadly,
    finish,
    depleted, // used when merging columns into rectangles to tell that this column had arleady been merged
}
