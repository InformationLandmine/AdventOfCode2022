import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 24")

    // Setup - Read the signal
    val input = File("day24input").readLines()
    val valley = ValleyMap(input.drop(1).dropLast(1).mapIndexed { y, row ->
        row.drop(1).dropLast(1).mapIndexed { x, c ->
            when (c) {
                '<' -> BlizzardDirection.Left
                '>' -> BlizzardDirection.Right
                '^' -> BlizzardDirection.Up
                'v' -> BlizzardDirection.Down
                else ->BlizzardDirection.None
            }
        }
    })

    // First Trip
    val start = Triple(0, -1, 1)
    val end = Pair(valley.width - 1, valley.height - 1)

    // Second Trip
//    val start = Triple(valley.width - 1, valley.height, 240)
//    val end = Pair(0, 0)

    // Third Trip
//    val start = Triple(0, -1, 478)
//    val end = Pair(valley.width - 1, valley.height - 1)

    valley.calculateDistance(start.first, start.second, start.third)
    val n = valley.nodes.filter { it.value.x == end.first && it.value.y == end.second }
    val part1 = n.minOf { it.value.distance } + 1
    println("Crossing the valley took $part1 minutes")

}

enum class BlizzardDirection { Up, Down, Left, Right, None }

class ValleyMap(val blizzard: List<List<BlizzardDirection>>) {
    val height = blizzard.size
    val width = blizzard.first().size
    val maxT = lowestCommonMultiple(height, width)
    val nodes = HashMap<Triple<Int, Int, Int>, ValleyNode>()

    fun calculateDistance(startX: Int, startY: Int, startT: Int) {
        var current: ValleyNode? = ValleyNode(startX, startY, startT)
        current!!.distance = startT
        val toVisit = ArrayList<ValleyNode>()
        while (current != null) {
            val newDistance = current.distance + 1
            val neighbors = current.getNeighbors(newDistance % maxT)
            neighbors.filter { it.first >= 0 && it.first < width && it.second >= 0 && it.second < height && isClearAt(it.first, it.second, it.third) }.map {
                nodes.getOrDefault(it, ValleyNode(it.first, it.second, it.third))
            }.filter { !it.visited }.forEach { node ->
                nodes[Triple(node.x, node.y, node.t)] = node
                if (newDistance < node.distance) node.distance = newDistance
                if (!toVisit.contains(node)) toVisit.add(node)
            }
            current.visited = true
            current = toVisit.removeFirstOrNull()
        }
        println("SOLVED")
        println("Num nodes: ${nodes.size}")
    }
    private fun isClearAt(xLoc: Int, yLoc: Int, t: Int): Boolean {
        val time = t % maxT
        return (blizzard[index(yLoc + time, height)][xLoc] != BlizzardDirection.Up &&
            blizzard[index(yLoc - time, height)][xLoc] != BlizzardDirection.Down &&
            blizzard[yLoc][index(xLoc + time, width)] != BlizzardDirection.Left &&
            blizzard[yLoc][index(xLoc - time, width)] != BlizzardDirection.Right)
    }

    private fun index(i: Int, size: Int) = if (i >= 0) i % size else ((i % size) + size) % size

}

class ValleyNode(val x: Int, val y: Int, val t: Int) {
    var visited = false
    var distance = Int.MAX_VALUE

    fun getNeighbors(nextT: Int): List<Triple<Int, Int, Int>> {
        return listOf(Triple(x + 1, y, nextT),
            Triple(x - 1, y, nextT),
            Triple(x, y + 1, nextT),
            Triple(x, y - 1, nextT),
            Triple(x, y, nextT)
        )
    }
}

fun lowestCommonMultiple (a: Int, b: Int): Int {
    val l = kotlin.math.min(a, b)
    val h = kotlin.math.max(a, b)
    var result = l
    while (!(result % l == 0 && result % h == 0)) {
        result += l
    }
    return result
}