import java.io.File
import org.paukov.combinatorics3.Generator

fun main(args: Array<String>) {
    println("2022 Advent of Code day 14")

    // Setup - Read the volcano map
    val volcanoNodeRegex = """Valve (\S+) has flow rate=(\d+); tunnels? leads? to valves? ([\S+,? ?]+)""".toRegex()
    val volcanoNodes = File("day16input").readLines().map { line ->
        val match = volcanoNodeRegex.matchEntire(line)!!
        val connections = match.groups[3]!!.value.split(",").map { it.trim() }
        VolcanoNode(match.groups[1]!!.value, match.groups[2]!!.value.toInt(), connections)
    }
    val volcano = Volcano(volcanoNodes)

    val start = volcanoNodes.first { it.name == "AA" }

    // Part 1
    val part1 = start.bestPath(30, volcano.workingValves, volcano.distance)
    println("The most pressure that can be released in 30 seconds is $part1")

    // Part 2
    val gen = Generator.combination(volcano.workingValves)
    val combinations = (0..volcano.workingValves.size / 2).flatMap {
        gen.simple(it).toList()
    }.map {
        Pair(it, volcano.workingValves - it)
    }
    val startTime = System.currentTimeMillis()
    val part2 = combinations.maxOf {
        start.bestPath(26, it.first, volcano.distance) +
        start.bestPath(26, it.second, volcano.distance)
    }
    val totalTime = System.currentTimeMillis() - startTime
    println("The most pressure that can be released in 24 second by you and an elephant is $part2")
    println("total time: $totalTime")
}

class Volcano(private val nodes: List<VolcanoNode>) {
    val distance = HashMap<Pair<VolcanoNode, VolcanoNode>, Int>()
    val workingValves = nodes.filter { it.flowRate > 0 }

    init {
        nodes.forEach { it.makeConnections(nodes) }
        // Solve the distances between each node
        nodes.forEach { start ->
            nodes.forEach { end ->
                nodes.forEach { it.reset() }
                distance[Pair(start, end)] = start.distanceTo(end)
            }
        }
    }
}


class VolcanoNode(val name: String, val flowRate: Int, private val connectionNames: List<String>) {
    // Connected nodes
    private val connections = ArrayList<VolcanoNode>()

    // For pathing
    private var visited = false
    private var distance = Int.MAX_VALUE

    fun makeConnections(nodes: List<VolcanoNode>) {
        connectionNames.forEach { name -> connections.add(nodes.first { it.name == name })  }
    }

    fun bestPath(timeRemaining: Int,
                 remainingNodes: List<VolcanoNode>,
                 distances: HashMap<Pair<VolcanoNode, VolcanoNode>, Int>
    ): Int {
        if (timeRemaining > 0) {
            // Calculate new score
            val score = flowRate * timeRemaining
            // Get the best score from remaining nodes
            return if (remainingNodes.isNotEmpty()) {
                score + remainingNodes.map { toCheck ->
                    toCheck.bestPath(
                        timeRemaining - distances.getValue(Pair(this, toCheck)) - 1,
                        remainingNodes.filter { it != toCheck },
                        distances
                    )
                }.max()
            } else 0
        } else return 0
    }

    fun distanceTo(other: VolcanoNode): Int {
        distance = 0
        var current: VolcanoNode? = this
        val toVisit = ArrayList<VolcanoNode>()
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
        return other.distance
    }

    fun reset() {
        visited = false
        distance = Int.MAX_VALUE
    }
}