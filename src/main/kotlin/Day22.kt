import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 22")

    // Setup - Read the map
    val input = File("day22test").readLines()
    val rawDirections = input.last()
    val tileSize = input.dropLast(2).minOf { it.trim().length }
    println("tile size is $tileSize")
    val tiles = input.dropLast(2).windowed(tileSize, tileSize).flatMap { mapStrings ->
        val numTiles = mapStrings.first().trim().length / tileSize
        (0 until numTiles).map { col ->
            MonkeyTile(mapStrings.map { line -> line.trim().drop(col * tileSize).take(tileSize) })
        }
    }
    println("There are ${tiles.size} map tiles")
    val directions = ArrayList<String>()
    var currentMove = 0
    rawDirections.forEach { c ->
        if (c.isDigit()) {
            currentMove = currentMove * 10 + c.digitToInt()
        } else {
            directions.add(currentMove.toString())
            directions.add(c.toString())
            currentMove = 0
        }
    }
    directions.add(currentMove.toString())

    // ----------------------- Define the edge connections -----------------------

    // Test Input
    val part1Connections = listOf(
        Pair(Pair(tiles[0], Edge.Top), Pair(tiles[4], Edge.Bottom)),
        Pair(Pair(tiles[0], Edge.Bottom), Pair(tiles[3], Edge.Top)),
        Pair(Pair(tiles[0], Edge.Left), Pair(tiles[0], Edge.Right)),
        Pair(Pair(tiles[0], Edge.Right), Pair(tiles[0], Edge.Left)),
        Pair(Pair(tiles[1], Edge.Top), Pair(tiles[1], Edge.Bottom)),
        Pair(Pair(tiles[1], Edge.Bottom), Pair(tiles[1], Edge.Top)),
        Pair(Pair(tiles[1], Edge.Left), Pair(tiles[3], Edge.Right)),
        Pair(Pair(tiles[1], Edge.Right), Pair(tiles[2], Edge.Left)),
        Pair(Pair(tiles[2], Edge.Top), Pair(tiles[2], Edge.Bottom)),
        Pair(Pair(tiles[2], Edge.Bottom), Pair(tiles[2], Edge.Top)),
        Pair(Pair(tiles[2], Edge.Left), Pair(tiles[1], Edge.Right)),
        Pair(Pair(tiles[2], Edge.Right), Pair(tiles[3], Edge.Left)),
        Pair(Pair(tiles[3], Edge.Top), Pair(tiles[0], Edge.Bottom)),
        Pair(Pair(tiles[3], Edge.Bottom), Pair(tiles[4], Edge.Top)),
        Pair(Pair(tiles[3], Edge.Left), Pair(tiles[2], Edge.Right)),
        Pair(Pair(tiles[3], Edge.Right), Pair(tiles[1], Edge.Left)),
        Pair(Pair(tiles[4], Edge.Top), Pair(tiles[3], Edge.Bottom)),
        Pair(Pair(tiles[4], Edge.Bottom), Pair(tiles[0], Edge.Top)),
        Pair(Pair(tiles[4], Edge.Left), Pair(tiles[5], Edge.Right)),
        Pair(Pair(tiles[4], Edge.Right), Pair(tiles[5], Edge.Left)),
        Pair(Pair(tiles[5], Edge.Top), Pair(tiles[5], Edge.Bottom)),
        Pair(Pair(tiles[5], Edge.Bottom), Pair(tiles[5], Edge.Top)),
        Pair(Pair(tiles[5], Edge.Left), Pair(tiles[4], Edge.Right)),
        Pair(Pair(tiles[5], Edge.Right), Pair(tiles[4], Edge.Left)),
    )

    // --0-
    // 123-
    // --45
    //
    val part2Connections = listOf(
        Pair(Pair(tiles[0], Edge.Top), Pair(tiles[1], Edge.Top)),
        Pair(Pair(tiles[0], Edge.Bottom), Pair(tiles[3], Edge.Top)),
        Pair(Pair(tiles[0], Edge.Left), Pair(tiles[2], Edge.Top)),
        Pair(Pair(tiles[0], Edge.Right), Pair(tiles[5], Edge.Right)),
        Pair(Pair(tiles[1], Edge.Top), Pair(tiles[0], Edge.Top)),
        Pair(Pair(tiles[1], Edge.Bottom), Pair(tiles[4], Edge.Bottom)),
        Pair(Pair(tiles[1], Edge.Left), Pair(tiles[5], Edge.Bottom)),
        Pair(Pair(tiles[1], Edge.Right), Pair(tiles[2], Edge.Left)),
        Pair(Pair(tiles[2], Edge.Top), Pair(tiles[0], Edge.Left)),
        Pair(Pair(tiles[2], Edge.Bottom), Pair(tiles[4], Edge.Left)),
        Pair(Pair(tiles[2], Edge.Left), Pair(tiles[1], Edge.Right)),
        Pair(Pair(tiles[2], Edge.Right), Pair(tiles[3], Edge.Left)),
        Pair(Pair(tiles[3], Edge.Top), Pair(tiles[0], Edge.Bottom)),
        Pair(Pair(tiles[3], Edge.Bottom), Pair(tiles[4], Edge.Top)),
        Pair(Pair(tiles[3], Edge.Left), Pair(tiles[2], Edge.Right)),
        Pair(Pair(tiles[3], Edge.Right), Pair(tiles[5], Edge.Top)),
        Pair(Pair(tiles[4], Edge.Top), Pair(tiles[3], Edge.Bottom)),
        Pair(Pair(tiles[4], Edge.Bottom), Pair(tiles[1], Edge.Bottom)),
        Pair(Pair(tiles[4], Edge.Left), Pair(tiles[2], Edge.Bottom)),
        Pair(Pair(tiles[4], Edge.Right), Pair(tiles[5], Edge.Left)),
        Pair(Pair(tiles[5], Edge.Top), Pair(tiles[3], Edge.Right)),
        Pair(Pair(tiles[5], Edge.Bottom), Pair(tiles[1], Edge.Left)),
        Pair(Pair(tiles[5], Edge.Left), Pair(tiles[4], Edge.Right)),
        Pair(Pair(tiles[5], Edge.Right), Pair(tiles[0], Edge.Right)),
    )

    tiles[0].position = Point(tiles[0].size * 2, 0)
    tiles[1].position = Point(0, tiles[0].size)
    tiles[2].position = Point(tiles[0].size, tiles[0].size)
    tiles[3].position = Point(tiles[0].size * 2, tiles[0].size)
    tiles[4].position = Point(tiles[0].size * 2, tiles[0].size * 2)
    tiles[5].position = Point(tiles[0].size * 3, tiles[0].size * 2)

    // Real Input
// -01
// -2-
// 34-
// 5--
//    val part1Connections = listOf(
//        Pair(Pair(tiles[0], Edge.Top), Pair(tiles[4], Edge.Bottom)),
//        Pair(Pair(tiles[0], Edge.Bottom), Pair(tiles[2], Edge.Top)),
//        Pair(Pair(tiles[0], Edge.Left), Pair(tiles[1], Edge.Right)),
//        Pair(Pair(tiles[0], Edge.Right), Pair(tiles[1], Edge.Left)),
//        Pair(Pair(tiles[1], Edge.Top), Pair(tiles[1], Edge.Bottom)),
//        Pair(Pair(tiles[1], Edge.Bottom), Pair(tiles[1], Edge.Top)),
//        Pair(Pair(tiles[1], Edge.Left), Pair(tiles[0], Edge.Right)),
//        Pair(Pair(tiles[1], Edge.Right), Pair(tiles[0], Edge.Left)),
//        Pair(Pair(tiles[2], Edge.Top), Pair(tiles[0], Edge.Bottom)),
//        Pair(Pair(tiles[2], Edge.Bottom), Pair(tiles[4], Edge.Top)),
//        Pair(Pair(tiles[2], Edge.Left), Pair(tiles[2], Edge.Right)),
//        Pair(Pair(tiles[2], Edge.Right), Pair(tiles[2], Edge.Left)),
//        Pair(Pair(tiles[3], Edge.Top), Pair(tiles[5], Edge.Bottom)),
//        Pair(Pair(tiles[3], Edge.Bottom), Pair(tiles[5], Edge.Top)),
//        Pair(Pair(tiles[3], Edge.Left), Pair(tiles[4], Edge.Right)),
//        Pair(Pair(tiles[3], Edge.Right), Pair(tiles[4], Edge.Left)),
//        Pair(Pair(tiles[4], Edge.Top), Pair(tiles[2], Edge.Bottom)),
//        Pair(Pair(tiles[4], Edge.Bottom), Pair(tiles[0], Edge.Top)),
//        Pair(Pair(tiles[4], Edge.Left), Pair(tiles[3], Edge.Right)),
//        Pair(Pair(tiles[4], Edge.Right), Pair(tiles[3], Edge.Left)),
//        Pair(Pair(tiles[5], Edge.Top), Pair(tiles[3], Edge.Bottom)),
//        Pair(Pair(tiles[5], Edge.Bottom), Pair(tiles[3], Edge.Top)),
//        Pair(Pair(tiles[5], Edge.Left), Pair(tiles[5], Edge.Right)),
//        Pair(Pair(tiles[5], Edge.Right), Pair(tiles[5], Edge.Left)),
//    )
//    tiles[0].position = Point(tiles[0].size * 1, 0)
//    tiles[1].position = Point(tiles[0].size * 2, 0)
//    tiles[2].position = Point(tiles[0].size, tiles[0].size)
//    tiles[3].position = Point(0, tiles[0].size * 2)
//    tiles[4].position = Point(tiles[0].size * 1, tiles[0].size * 2)
//    tiles[5].position = Point(0, tiles[0].size * 3)



    val part1Map = MonkeyMap(tiles, part1Connections)
    directions.forEach {
        if (it == "L") part1Map.turnLeft()
        else if (it == "R") part1Map.turnRight()
        else repeat(it.toInt()) { part1Map.move() }
    }
    println(part1Map.position.second)
    println(part1Map.orientation)
    println(part1Map.globalPosition)
    println(part1Map.password)

    val part2Map = MonkeyMap(tiles, part2Connections)
    directions.forEach {
        if (it == "L") part2Map.turnLeft()
        else if (it == "R") part2Map.turnRight()
        else repeat(it.toInt()) { part2Map.move() }
    }
    println(part2Map.position.second)
    println(part2Map.orientation)
    println(part2Map.globalPosition)
    println(part2Map.password)


}

typealias EdgeConnection = Pair<Pair<MonkeyTile, Edge>, Pair<MonkeyTile, Edge>>
enum class Orientation { Left, Right, Up, Down }
enum class Edge { Left, Right, Top, Bottom }

class MonkeyMap(private val tiles: List<MonkeyTile>, val connections: List<EdgeConnection>) {
    var position = Pair(tiles.first(), Point(0, 0))
    var orientation = Orientation.Right
    val globalPosition get() = Point(position.first.position.x + position.second.x + 1, position.first.position.y + position.second.y + 1)
    val password get() = 1000 * globalPosition.y + 4 * globalPosition.x + when (orientation){
        Orientation.Up -> 3
        Orientation.Down -> 1
        Orientation.Left -> 2
        Orientation.Right -> 0
    }

    fun willMoveTo(): Pair<MonkeyTile, Point> {
        var result = position
        var moveTo = Point(0, 0)
        //var newOrientation = Orientation.Left
        var dest = Pair(position.first, Edge.Left)
        when (orientation) {
            Orientation.Up -> {
                if (position.second.y == 0) {
                    dest = connections.first { it.first == Pair(position.first, Edge.Top) }.second
                    when (dest.second) {
                        Edge.Top -> moveTo = Point(position.second.x, position.second.y)
                        Edge.Bottom -> moveTo = Point(position.second.x, dest.first.size - 1)
                        Edge.Left -> moveTo = Point(0, position.second.x)
                        Edge.Right -> moveTo = Point(dest.first.size - 1, dest.first.size - position.second.x - 1)
                    }
                } else
                    moveTo = Point(position.second.x, position.second.y - 1)
            }
            Orientation.Down -> {
                if (position.second.y == position.first.size - 1) {
                    dest = connections.first { it.first == Pair(position.first, Edge.Bottom) }.second
                    when (dest.second) {
                        Edge.Top -> moveTo = Point(position.second.x, 0)
                        Edge.Bottom -> moveTo = Point(position.second.x, dest.first.size - 1)
                        Edge.Left -> moveTo = Point(0, dest.first.size - position.second.x - 1)
                        Edge.Right -> moveTo = Point(dest.first.size - 1, position.second.x)
                    }
                } else
                    moveTo = Point(position.second.x, position.second.y + 1)
            }
            Orientation.Left -> {
                if (position.second.x == 0) {
                    dest = connections.first { it.first == Pair(position.first, Edge.Left) }.second
                    when (dest.second) {
                        Edge.Top -> moveTo = Point(position.second.x, 0)
                        Edge.Bottom -> moveTo = Point(position.second.x, dest.first.size - 1)
                        Edge.Left -> moveTo = Point(0, position.second.y)
                        Edge.Right -> moveTo = Point(dest.first.size - 1, position.second.y)
                    }
                } else
                    moveTo = Point(position.second.x - 1, position.second.y)
            }
            Orientation.Right -> {
                if (position.second.x == position.first.size - 1) {
                    dest = connections.first { it.first == Pair(position.first, Edge.Right) }.second
                    when (dest.second) {
                        Edge.Top -> moveTo = Point(position.second.x, 0)
                        Edge.Bottom -> moveTo = Point(position.second.x, dest.first.size - 1)
                        Edge.Left -> moveTo = Point(0, position.second.y)
                        Edge.Right -> moveTo = Point(dest.first.size - 1, position.second.y)
                    }
                } else
                    moveTo = Point(position.second.x + 1, position.second.y)
            }
        }
        if (!dest.first.points.contains(moveTo)) {
            result = Pair(dest.first, moveTo)
            //orientation = newOrientation
        }

        return result
    }

    fun move() {
        position = willMoveTo()
        //printPosition()
    }

    fun printPosition() {
        val tileIndex = tiles.indexOf(position.first)
        println("Tile $tileIndex: ${position.second}, $orientation")
    }

    fun turnLeft() {
        orientation = when (orientation) {
            Orientation.Up -> Orientation.Left
            Orientation.Left -> Orientation.Down
            Orientation.Down -> Orientation.Right
            Orientation.Right -> Orientation.Up
        }
    }

    fun turnRight() {
        orientation = when (orientation) {
            Orientation.Up -> Orientation.Right
            Orientation.Left -> Orientation.Up
            Orientation.Down -> Orientation.Left
            Orientation.Right -> Orientation.Down
        }
    }
}

class MonkeyTile(map: List<String>) {
    var position = Point(0, 0)
    val size = map.size
    val points = map.flatMapIndexed { row, s ->
        s.mapIndexedNotNull { col, c ->
            if (c == '#') Point(col, row) else null
        }
    }.toSet()
}