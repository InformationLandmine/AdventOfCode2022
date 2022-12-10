import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("2022 Advent of Code day 10")

    // Setup - Read the program
    val program = File("day10input").readLines().map { line ->
        line.split(" ").let { Pair(it.first(), it.last().toIntOrNull()) }
    }

    // Just convert each addx instruction to a noop and addx to keep the processor simple
    val modifiedProgram = program.flatMap { instr ->
        if (instr.first == "noop") listOf(instr)
        else listOf(Pair("noop", null), instr)
    }

    // Run the program
    val result = runModifiedProgram(modifiedProgram)

    // Part 1 - get the signal strengths during the specified cycles
    val signals = listOf(20, 60, 100, 140, 180, 220)
    val part1 = signals.sumOf { result.first[it - 1] * it }
    println("The sum of the six signal strngths is $part1")

    // Part 2 - print out the output
    result.second.windowed(40, 40).forEach { println(it) }
}

fun runModifiedProgram(program: List<Pair<String, Int?>>): Pair<ArrayList<Int>, String> {
    var x = 1
    var clock = 0
    var output = ""
    val xvals = ArrayList<Int>()
    while (clock <= program.lastIndex) {
        val instr = program[clock].first
        output += if (abs(x - (clock % 40)) <= 1) "#" else "."
        xvals.add(x)
        if  (instr == "addx") x += program[clock].second!!
        clock++
    }
    return Pair(xvals, output)
}
