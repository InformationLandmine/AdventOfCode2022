import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 20")

    // Setup - read the encrypted file
    val input = File("day20input").readLines()
    println("There are ${input.size} elements in the file")

    // Part 1 - mix the file
    val part1File = GPSFile(input.mapIndexed { i, v -> Pair(v.toLong(), i) })
    part1File.mix()
    val part1 = listOf(1000, 2000, 3000).sumOf { part1File.getAfterZero(it) }
    println(part1)

    // Part 2 - apply the decryption ket and mix the file 10 times
    val key = 811589153L
    val part2File = GPSFile(input.mapIndexed { i, v -> Pair(v.toLong() * key, i) })
    repeat(10) { part2File.mix() }
    val part2 = listOf(1000, 2000, 3000).sumOf { part2File.getAfterZero(it) }
    println(part2)
}

typealias FileEntry = Pair<Long, Int>

class GPSFile(items: List<FileEntry>) : ArrayList<FileEntry>(items) {
    private fun normalizeIndex(i: Long): Int {
        return if (i >= 0)
            (i % size).toInt()
        else
            (((i % size) + size) % size).toInt()
    }

    fun mix() {
        var nextIndexToMix = 0
        repeat (size) {
            val toMove = first { it.second == nextIndexToMix }
            val curIndex = indexOf(toMove)
            remove(toMove)
            val dest = normalizeIndex(curIndex + toMove.first)
            add(dest, toMove)
            nextIndexToMix++
        }
    }

    override fun toString(): String {
        return joinToString("") { item ->
            item.first.toString() + ", "
        }
    }

    fun getAfterZero(count: Int) = this[normalizeIndex((indexOf(find { it.first == 0L }) + count).toLong())].first
}