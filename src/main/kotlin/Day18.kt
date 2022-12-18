import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 18")

    // Setup - read the lava droplet scan
    val droplet = File("day18input").readLines().map { line ->
        line.split(",").let { Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
    }.toSet()
    println("There are ${droplet.size} points representing the lava droplet")

    // Part 1 - get the surface area of the droplet
    val blockedSides = droplet.sumOf { it.getTouching().intersect(droplet).size }
    val area = droplet.size * 6 - blockedSides
    println("The surface area of the droplet is $area")

    // Part 2 - get the exterior surface area of the droplet
    // Create a bounding cube
    val bounding = (droplet.minOf { it.x - 1 }..droplet.maxOf { it.x + 1 }).flatMap { x ->
        (droplet.minOf { it.y - 1 }..droplet.maxOf { it.y + 1 }).flatMap { y ->
            (droplet.minOf { it.z - 1 }..droplet.maxOf { it.z + 1 }).map { z ->
                Point3D(x, y, z)
            }
        }
    }.toSet()
    val emptySpace = Graph3D(bounding - droplet)
    emptySpace.fill(Point3D(droplet.minOf { it.x - 1 }, droplet.minOf { it.y - 1 }, droplet.minOf { it.z - 1 }))
    val enclosed = emptySpace.notFilled
    println("There are ${enclosed.size} fully enclosed points in the droplet")
    val enclosedArea =  enclosed.size * 6 - enclosed.sumOf { it.getTouching().intersect(enclosed).size }
    val exposedArea = area - enclosedArea
    println("The surface are of the fully enclosed points is $enclosedArea")
    println("The exterior surface area of the droplet is $exposedArea")
}

data class Point3D (val x: Int, val y:Int, val z:Int) {
    var visited = false

    fun getTouching(): Set<Point3D> {
        return setOf(
            Point3D(x - 1, y, z),
            Point3D(x + 1, y, z),
            Point3D(x, y - 1, z),
            Point3D(x, y + 1, z),
            Point3D(x, y, z - 1),
            Point3D(x, y, z + 1),
        )
    }
}

class Graph3D(private val points: Set<Point3D>) {
    operator fun get(p: Point3D) = points.find { it.x == p.x && it.y == p.y && it.z == p.z }

    fun fill(first: Point3D) {
        var current = this[first]
        val toVisit = ArrayList<Point3D>()
        while (current != null) {
            neighbors(current).filter { !it.visited }.forEach { point ->
                if (!toVisit.contains(point))
                    toVisit.add(point)
            }
            current.visited = true
            current = toVisit.removeFirstOrNull()
        }
    }

    private fun neighbors(p: Point3D): Set<Point3D> {
        return p.getTouching().mapNotNull { this[it] }.toSet()
    }

    val notFilled get() = points.filter { !it.visited }.toSet()
    val size get() = points.size
}