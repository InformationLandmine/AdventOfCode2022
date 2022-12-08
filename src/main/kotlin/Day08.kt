import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 8")

    // Setup - Read the map
    val map = File("day8input").readLines().map { line -> line.map { it.digitToInt() } }

    // Part 1 - get the visible tree count
    val part1 = map.flatMapIndexed { r, row -> row.mapIndexed { c, h -> isVisible(map, r, c, h) } }.count { it }
    println(part1)

    // Part 2 - get the max scenic score
    val part2 = map.flatMapIndexed { r, row -> row.mapIndexed { c, h -> scenicScore(map, r, c, h) } }.max()
    println(part2)
}

fun isVisible(map: List<List<Int>>, row: Int, col: Int, height: Int): Boolean {
    // Check up
    val up = !(0 until row).any { map[it][col] >= height }
    // Check down
    val down = !(row + 1..map.lastIndex).any { map[it][col] >= height }
    // Check left
    val left = !(0 until col).any { map[row][it] >= height }
    // Check right
    val right = !(col + 1..map[0].lastIndex).any { map[row][it] >= height }

    return up || down || left || right
}

fun scenicScore(map: List<List<Int>>, row: Int, col: Int, height: Int): Int {
    // Check up
    var up = (row - 1 downTo 0).firstOrNull { map[it][col] >= height }?.let {
        row - it
    }
    if (up == null) up = row
    // Check down
    var down = (row + 1..map.lastIndex).firstOrNull { map[it][col] >= height }?.let {
        it - row
    }
    if (down == null) down = map.lastIndex - row
    // Check left
    var left = (col - 1 downTo  0).firstOrNull { map[row][it] >= height }?.let {
        col - it
    }
    if (left == null) left = col
    // Check right
    var right = (col + 1..map[0].lastIndex).firstOrNull { map[row][it] >= height }?.let {
        it - col
    }
    if (right == null) right = map[0].lastIndex - col

    return up * down * left * right
}