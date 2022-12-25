import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("2022 Advent of Code day 23")

    // Setup - Read the signal
    val elves = ElfGroup(File("day23input").readLines().flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, c ->
            if (c == '#') ElfPoint(col, row) else null
        }
    }.toHashSet())
    println("There are ${elves.size} elves")

    // Part 1 - iterate the elves' movement
    repeat(10) { elves.move() }
    println(elves.emptyGround)

    // Part 2 - iterate until the elves don't move
    var count = 10
    do {
        val numMoved = elves.move()
        count++
        if (count % 100 == 0) println("$count iterations")
    } while (numMoved > 0)
    println(count)
}

enum class Direction { North, South, West, East, All }
class ElfGroup(val elves: HashSet<ElfPoint>) {
    val size get() = elves.size
    val directions = ArrayDeque<Direction>()

    init {
        directions.addAll(Direction.values().filterNot { it == Direction.All })
    }

    val emptyGround get() = (elves.maxOf { it.x } - elves.minOf { it.x } + 1) * (elves.maxOf { it.y } - elves.minOf { it.y } + 1) - size
    fun move(): Int {
        val proposed = getProposedMovements()
        val moves = proposed.filterNot { move -> proposed.count { other -> other.second == move.second } > 1 }
        moves.forEach { (source, dest) ->
            elves.remove(source)
            elves.add(dest)
        }
        directions.addLast(directions.removeFirst())
        return moves.size
    }

    private fun getProposedMovements(): List<Pair<ElfPoint, ElfPoint>> {
        val result = ArrayList<Pair<ElfPoint, ElfPoint>>()
        val movingElves = elves.filter { elves.intersect(it.getNeighbors()).isNotEmpty() }
        movingElves.forEach { elf ->
            directions.firstNotNullOfOrNull {
                if (shouldMove(elf, it)) elf.getNeighbor(it) else null
            }?.let { result.add(Pair(elf, it)) }
        }
        return result
    }

    private fun shouldMove(elf: ElfPoint, dir: Direction): Boolean {
        return elves.intersect(elf.getNeighbors(dir)).isEmpty()
    }

    override fun toString(): String {
        return "$size Elves:\n" + (elves.minOf { it.y }..elves.maxOf { it.y }).map { row ->
            (elves.minOf { it.x }..elves.maxOf { it.x }).map { col ->
                if (elves.contains(ElfPoint(col, row))) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }
}

data class ElfPoint(val x:Int, val y:Int) {
    fun isAdjacent(other: ElfPoint) = abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    fun getNeighbor(dir: Direction): ElfPoint {
        return when (dir) {
            Direction.North -> ElfPoint(x, y-1)
            Direction.South -> ElfPoint(x, y+1)
            Direction.West -> ElfPoint(x-1, y)
            Direction.East -> ElfPoint(x+1, y)
            Direction.All -> ElfPoint(x, y)
        }
    }
    fun getNeighbors(dir: Direction = Direction.All): Set<ElfPoint> {
        return when (dir) {
            Direction.All -> setOf(ElfPoint(x-1, y-1), ElfPoint(x, y-1), ElfPoint(x+1, y-1),
                ElfPoint(x-1, y), ElfPoint(x+1, y),
                ElfPoint(x-1, y+1), ElfPoint(x, y+1), ElfPoint(x+1, y+1))
            Direction.North -> setOf(ElfPoint(x-1, y-1), ElfPoint(x, y-1), ElfPoint(x+1, y-1))
            Direction.South -> setOf(ElfPoint(x-1, y+1), ElfPoint(x, y+1), ElfPoint(x+1, y+1))
            Direction.West -> setOf(ElfPoint(x-1, y-1), ElfPoint(x-1, y), ElfPoint(x-1, y+1))
            Direction.East -> setOf(ElfPoint(x+1, y-1), ElfPoint(x+1, y), ElfPoint(x+1, y+1))
        }
    }
}