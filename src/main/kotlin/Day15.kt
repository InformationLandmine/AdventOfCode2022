import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

fun main(args: Array<String>) {
    println("2022 Advent of Code day 15")

    // Setup - Read the sensor and beacons list
    val sensorRegex = """Sensor at x=(-*\d+), y=(-*\d+): closest beacon is at x=(-*\d+), y=(-*\d+)""".toRegex()
    val sensorMap = File("day15input").readLines().map { line ->
        sensorRegex.matchEntire(line)?.let {
            Pair(
                Point(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt()),
                Point(it.groups[3]!!.value.toInt(), it.groups[4]!!.value.toInt())
            )
        }
    }.filterNotNull()

    val sensors = sensorMap.map { Sensor(it.first, it.second) }
    val beaconLocations = sensors.map { it.beacon }.toSet()
    val testLine = 2000000
    val maxSize = 4000000
    val tFreqConstant = 4000000L

    // Part 1 - Count how many positions cannot contain a beacon on the given line
    val coverage = ArrayList(sensors.map { it.getCoverage(testLine) }.filter { it != IntRange.EMPTY }.sortedBy { it.first })
    val result = consolidateRanges(coverage)
    println("${result.sumOf { it.count() } - beaconLocations.count { it.y == testLine }} positions cannot contain a beacon")

    // Part 2 - find the location the beacon could be
    (0..maxSize).forEach { yLoc ->
        val coverage = ArrayList(sensors.map { it.getCoverage(yLoc) }.filter { it != IntRange.EMPTY }.sortedBy { it.first })
        val result = consolidateRanges(coverage)
        if (result.size > 1) {  // Assumes there are no other gaps in coverage, even outside the boundaries
            val xLoc = (result.first().last + 1).toLong()
            val part2 = xLoc * tFreqConstant + yLoc.toLong()
            println("The tuning frequency of the beacon is $part2")
        }
    }

}

fun consolidateRanges(ranges: ArrayList<IntRange>): List<IntRange> {
    val result = ArrayList<IntRange>()
    while (ranges.size > 1) {
        var combined = ranges.removeFirst()
        while (ranges.size > 0 && combined.overlaps(ranges.first())) {
            combined = combined.combine(ranges.removeFirst())
        }
        result.add(combined)
    }
    if (ranges.size > 0) result.add(ranges.removeFirst())
    return result
}

data class Sensor(val loc: Point, val beacon: Point) {
    private val distance = abs(loc.x - beacon.x) + abs(loc.y - beacon.y)
    fun getCoverage(y: Int): IntRange {
        val rangeSize = distance - abs(loc.y - y)
        if (rangeSize >= 0)
            return ((loc.x - rangeSize)..(loc.x + rangeSize))
        else
            return IntRange.EMPTY
    }
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return (other.first in first..last) ||
           (other.last in first..last) ||
           (other.first <= first && other.last >= last)
}

fun IntRange.combine(other: IntRange) = IntRange(min(first, other.first), max(last, other.last))

