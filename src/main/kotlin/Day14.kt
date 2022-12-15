import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    println("2022 Advent of Code day 14")

    // Setup - Read the list of lines
    val rockLines = File("day14input").readLines().map { line ->
        line.split(" -> ").map { point ->
            point.split(",").let { CavePoint(it.first().toInt(), it.last().toInt(), PointContents.Rock)}
        }
    }

    // Create a map of the cave using the lines
    val caveMap = rockLines.flatMap { lines ->
        lines.windowed(2).flatMap { line ->
            line.first().lineTo(line.last())
        }
    }.toHashSet()
    val source = CavePoint(500, 0, PointContents.Source)
    caveMap.add(source)

    // Part 1 - drop sand until no more accumulates
    val startingSize = caveMap.size
    do {
        val currentCount = caveMap.size
        dropSand(source, caveMap)?.let { caveMap.add(it) }
    } while (caveMap.size > currentCount)
    val part1 = caveMap.size - startingSize
    println("$part1 units of sand can be dropped before no more accumulates")

    // Remove all the sand and add a floor
    caveMap.removeAll { it.contents == PointContents.Sand }
    val floorHeight = caveMap.maxOf { it.y } + 2

    // Part 2 - Drop sand until the sand source is blocked
    do {
        dropSand(source, caveMap, floorHeight)?.let { caveMap.add(it) }
    } while (caveMap.firstOrNull { it.x == source.x && it.y == source.y && it.contents == PointContents.Sand } == null)
    val part2 = caveMap.size - startingSize
    println("$part2 units of sand can be dropped before the source is blocked")
}

enum class PointContents { Rock, Sand, Source }
data class CavePoint(val x:Int, val y:Int, val contents: PointContents) {
    fun lineTo(other: CavePoint): Set<CavePoint> {
        return if (x == other.x) {
            if (this.y > other.y) {
                (other.y..this.y).map { CavePoint(x, it, PointContents.Rock) }.toSet()
            } else {
                (this.y..other.y).map { CavePoint(x, it, PointContents.Rock) }.toSet()
            }
        } else {
            if (this.x > other.x) {
                (other.x..this.x).map { CavePoint(it, y, PointContents.Rock) }.toSet()
            } else {
                (this.x..other.x).map { CavePoint(it, y, PointContents.Rock) }.toSet()
            }
        }
    }
}

fun dropSand (from: CavePoint, cave: HashSet<CavePoint>, floorHeight: Int = 0): CavePoint? {
    // Fall down as much as possible
    var blocking = cave.filter { it.x == from.x && it.y > from.y }.minByOrNull { it.y }
    if (blocking == null && floorHeight > 0) blocking = CavePoint(from.x, floorHeight, PointContents.Rock)
    if (blocking != null) {
        var downLeft = cave.find { it.x == from.x - 1 && it.y == blocking.y }
        var downRight = cave.find { it.x == from.x + 1 && it.y == blocking.y }
        if (blocking.y == floorHeight) blocking.let { downLeft = it; downRight = it }
        if (downLeft != null && downRight != null) return CavePoint(from.x, blocking.y - 1, PointContents.Sand)
        if (downLeft == null) return dropSand(CavePoint(from.x - 1, blocking.y, PointContents.Sand), cave, floorHeight)
        else return dropSand(CavePoint(from.x + 1, blocking.y, PointContents.Sand), cave, floorHeight)
    }
    return null
}

fun printCave(cave: HashSet<CavePoint>, floorHeight: Int = 0) {
    val minX = cave.minOf { it.x }
    val maxX = cave.maxOf { it.x }
    val maxY = max(cave.maxOf { it.y }, floorHeight)
    for (y in 0..maxY) {
        println((minX..maxX).map { x ->
            val p = cave.find { it.x == x && it.y == y }
            if (p != null) {
                when (p.contents) {
                    PointContents.Rock -> '#'
                    PointContents.Source -> '+'
                    PointContents.Sand -> 'o'
                }
            } else {
                if (floorHeight > 0 && y == maxY) '#' else '.'
            }
        }.joinToString(""))
    }
}