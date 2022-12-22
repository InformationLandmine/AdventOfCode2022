import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 20")

    // Setup - read the encrypted file
    val input = File("day20input").readLines()
    println("There are ${input.size} elements in the file")

    // Part 1 - mix the file
    val part1File = GPSFile(input.mapIndexed { i, v -> Pair(v.toInt(), i) })
    repeat(part1File.size) { part1File.mix() }
    val one = part1File.getAfterZero(1000)
    val two = part1File.getAfterZero(2000)
    val three = part1File.getAfterZero(3000)
    println("one: $one, two: $two, three: $three, sum: ${one + two + three}")

    // Part 2 - apply the decryption ket and mix the file 10 times
    //val part2File =
}

typealias FileEntry = Pair<Int, Int>
class GPSFile(items: List<FileEntry>) : ArrayList<FileEntry>(items) {
    var nextIndexToMix = 0

    override operator fun get(index: Int): FileEntry {
        return super.get(normalizeIndex(index))
    }

    private fun normalizeIndex(i: Int): Int {
        return if (i >= size)
            i % size
        else if (i < 0)
            ((i % size) + size) % size
        else
            i
    }

    fun mix() {
        val toMove = first { it.second == nextIndexToMix }
        val curIndex = indexOf(toMove)
        remove(toMove)
        val dest = normalizeIndex(curIndex + toMove.first)
        add(dest, toMove)
        nextIndexToMix++
    }

    override fun toString(): String {
        return joinToString("") { item ->
            item.first.toString() + ", "
        }
    }

    fun getAfterZero(count: Int) = this[indexOf(find { it.first == 0 }) + count].first
}