import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 12")

    // Setup - Load the height map
    val nodes = File("day12input").readLines().flatMapIndexed { y, line ->
        line.mapIndexed { x, h ->
            val height = when (h) {
                'S' -> 1
                'E' -> 26
                else -> h.code - 96
            }
            Triple(h, height, PathPoint(x, y))
        }
    }.map { PathNode(it.first, it.second, it.third) }

    // Part 1 - find the shortest route to the end
    // Establish the possible paths from each point
    nodes.forEach { node ->
        node.connections.addAll(nodes.filter { node.loc.getAdjacent().contains(it.loc) && it.height <= node.height + 1 })
    }
    var start = nodes.first { it.code == 'S' }
    var end = nodes.first { it.code == 'E' }
    start.distance = 0
    calculateCost(start)
    println("The shortest distance to the end is ${end.distance}")

    nodes.forEach { it.reset() }

    // Part 2 - find the shortest route from any low point to the end
    // Establish the possible paths from each point. This time we are working backwards so reverse the traversal rule.
    nodes.forEach { node ->
        node.connections.addAll(nodes.filter { node.loc.getAdjacent().contains(it.loc) && it.height >= node.height - 1 })
    }
    start = nodes.first { it.code == 'E' }
    start.distance = 0
    calculateCost(start)
    end = nodes.filter { it.height == 1 }.minBy { it.distance }
    println("The shortest distance to the end from any starting point is ${end.distance}")
}

fun calculateCost(start: PathNode) {
    var current: PathNode? = start
    val toVisit = ArrayList<PathNode>()
    while (current != null) {
        val newDistance = current.distance + 1
        current.connections.filter { !it.visited }.forEach { node ->
            if (newDistance < node.distance) node.distance = newDistance
            if (!toVisit.contains(node))
                toVisit.add(node)
        }
        current.visited = true
        current = toVisit.removeFirstOrNull()
    }
}

data class PathPoint(val x:Int, val y:Int) {
    fun getAdjacent() = setOf(
        PathPoint(x, y - 1),
        PathPoint(x, y + 1),
        PathPoint(x - 1, y),
        PathPoint(x + 1, y),
    )
}

class PathNode(val code: Char, val height: Int, val loc: PathPoint) {
    val connections = ArrayList<PathNode>()
    var visited = false
    var distance = Int.MAX_VALUE

    fun reset() {
        connections.clear()
        visited = false
        distance = Int.MAX_VALUE
    }
}
