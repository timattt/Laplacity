package steelUnicorn.laplacity.field.tiles;

/** 
 * Lists all types of available tiles.
 * Tiles ignored by collision detector should be marked as {@link TileType#nonPhysical}.
 */
public enum TileType {
    nonPhysical,
    barrier,
    wall,
    deadly,
    finish,
    depleted, // used when merging columns into rectangles to tell that this column had arleady been merged
}
