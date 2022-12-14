import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 13")

    // Setup - Read packets
    val signalPairs = File("day13input")
        .readLines()
        .filter { it.isNotEmpty() }
        .windowed(2, 2)
        .map {
            Pair(parsePacket(it.first().replace("10", "A")), parsePacket(it.last().replace("10", "A")))
        }

    // Part 1 - Get the sum of the correctly ordered signal packets
    val part1 = signalPairs.mapIndexed { i, pair ->
        if (pair.first < pair.second) i + 1 else 0
    }.sum()
    println("The sum of the correctly ordered packets is $part1")

    // Part 2 - Correctly order the packets
    val divisorPackets = listOf(parsePacket("[[2]]"), parsePacket("[[6]]"))
    val sortedPackets = (signalPairs.flatMap { listOf(it.first, it.second) } + divisorPackets).sortedBy { it }
    val part2 = (sortedPackets.indexOf(divisorPackets.first()) + 1) * (sortedPackets.indexOf(divisorPackets.last()) + 1)
    println(part2)
}

fun parsePacket(p: String): PacketValue {
    val values = ArrayDeque<PacketValue>()
    var current = PacketValue(ValueType.List)
    p.forEach { c ->
        when (c) {
            '[' -> {
                values.addFirst(current)
                current = PacketValue(ValueType.List)
            }
            ']' -> {
                val popped = values.removeFirst()
                popped.values.add(current)
                current = popped
            }
            ',' -> { }
            'A' -> { current.values.add(PacketValue(ValueType.Int, 10)) }
            else -> { current.values.add(PacketValue(ValueType.Int, c.digitToInt())) }
        }
    }
    return current.values.first()
}

enum class ValueType { Int, List }
enum class CompareResult(val result: Int) { Less(-1), Greater(1), Equal(0) }
class PacketValue(private val type: ValueType, private var value:Int = 0): Comparable<PacketValue> {
    val values = ArrayList<PacketValue>()

    override fun compareTo(other: PacketValue): Int {
        if (this.type == ValueType.Int && other.type == ValueType.Int)
            return if (this.value < other.value) CompareResult.Less.result
            else if (this.value > other.value) CompareResult.Greater.result
            else CompareResult.Equal.result
        if (this.type == ValueType.List && other.type == ValueType.List) {
            val zipped = values.zip(other.values)
            var firstPass = zipped.indexOfFirst { it.first.compareTo(it.second) == CompareResult.Less.result }
            if (firstPass < 0) firstPass = Int.MAX_VALUE
            var firstFail = zipped.indexOfFirst { it.first.compareTo(it.second) == CompareResult.Greater.result }
            if (firstFail < 0) firstFail = Int.MAX_VALUE
            return if (firstPass < firstFail) CompareResult.Less.result
            else if (firstPass > firstFail) CompareResult.Greater.result
            else if (this.values.size < other.values.size) CompareResult.Less.result
            else if (this.values.size > other.values.size) CompareResult.Greater.result
            else CompareResult.Equal.result
        }
        return if (this.type == ValueType.List && other.type == ValueType.Int) {
            val compareTo = PacketValue(ValueType.List)
            compareTo.values.add(other)
            compareTo(compareTo)
        } else {
            val compareTo = PacketValue(ValueType.List)
            compareTo.values.add(this)
            compareTo.compareTo(other)
        }
    }
}