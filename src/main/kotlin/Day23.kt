import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 23")

    // Setup - Read the signal
    val elves = ElfGroup(File("day23input").readLines().flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, c ->
            if (c == '#') ElfPoint(col + 500, row + 500) else null
        }
    })
    println("There are ${elves.size} elves")
    println(elves)

    // Part 1 - iterate the elves' movement
    repeat(10) { elves.move() }
    println(elves.emptyGround)

    // Part 2 - iterate until the elves don't move
    var count = 10
    do {
        val numMoved = elves.move()
        count++
    } while (numMoved > 0)
    println(count)
}

enum class Direction { North, South, West, East, All }
class ElfGroup(val elves: List<ElfPoint>) {
    val size get() = elves.size
    val directions = ArrayDeque<Direction>()
    val elfMap = HashMap<Int, ElfPoint>()

    init {
        directions.addAll(Direction.values().filterNot { it == Direction.All })
        elves.forEach { elfMap[it.index] = it }
    }

    val emptyGround get() = (elves.maxOf { it.x } - elves.minOf { it.x } + 1) * (elves.maxOf { it.y } - elves.minOf { it.y } + 1) - size

    fun move(): Int {
        val proposed = getProposedMovements()
        val moves = proposed.filterNot { move -> proposed.count { other -> other.second == move.second } > 1 }
        moves.forEach { (source, dest) ->
            elfMap.remove(source.index)
            elfMap[dest] = source
            source.index = dest
        }
        directions.addLast(directions.removeFirst())
        return moves.size
    }

    private fun getProposedMovements(): List<Pair<ElfPoint, Int>> {
        val result = ArrayList<Pair<ElfPoint, Int>>()
        val movingElves = elves.filter { it.getNeighborIndexes().any { elfMap.contains(it) } }
        movingElves.forEach { elf ->
            directions.firstNotNullOfOrNull {
                if (shouldMove(elf, it)) elf.getNeighborIndex(it) else null
            }?.let { result.add(Pair(elf, it)) }
        }
        return result
    }

    private fun shouldMove(elf: ElfPoint, dir: Direction): Boolean {
        return elf.getNeighborIndexes(dir).all { !elfMap.contains(it) }
    }

    override fun toString(): String {
        return "$size Elves:\n" + (elves.minOf { it.y }..elves.maxOf { it.y }).map { row ->
            (elves.minOf { it.x }..elves.maxOf { it.x }).map { col ->
                if (elfMap.contains(row * 4096 + col)) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }
}

class ElfPoint(xLoc:Int, yLoc:Int) {
    val yFactor = 4096
    var index = yLoc * yFactor + xLoc

    val x: Int get () {
        return if (y >= 0)
            ((index + yFactor / 2) % yFactor) - yFactor / 2
        else
            (((index + yFactor / 2) * -1) % yFactor) - yFactor /2
    }
    val y get() = if (index >= -yFactor / 2) (index + yFactor / 2) / yFactor else (index - yFactor / 2) / yFactor

    fun getNeighborIndex(dir: Direction): Int {
        return when (dir) {
            Direction.North -> index - yFactor
            Direction.South -> index + yFactor
            Direction.West -> index - 1
            Direction.East -> index + 1
            Direction.All -> index
        }
    }

    fun getNeighborIndexes(dir: Direction = Direction.All): Set<Int> {
        return when (dir) {
            Direction.All -> setOf(index - yFactor - 1, index - yFactor, index - yFactor + 1,
                index - 1, index + 1,
                index + yFactor - 1, index + yFactor, index + yFactor + 1)
            Direction.North -> setOf(index - yFactor - 1, index - yFactor, index - yFactor + 1)
            Direction.South -> setOf(index + yFactor - 1, index + yFactor, index + yFactor + 1)
            Direction.West -> setOf(index - yFactor - 1, index - 1, index + yFactor - 1)
            Direction.East -> setOf(index - yFactor + 1, index + 1, index + yFactor + 1)
        }
    }
}