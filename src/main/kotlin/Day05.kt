import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 5")

    // Setup - read the crate map
    val input = File("day5input").readText().split("\n\n")

    // Parse the starting crate configuration
    val cratesInput = input.first().split("\n").reversed()
    val numCrates = cratesInput.first().trim().split(" ").last().toInt()
    val crates = ArrayList<MutableList<Char>>(numCrates)
    repeat(numCrates) { i -> crates.add(mutableListOf()) }
    cratesInput.drop(1).forEach { line ->
        line.drop(1).windowed(1, 4).forEachIndexed { i, item ->
            if (item.isNotBlank()) crates[i].add(0, item.first())
        }
    }

    // Parse the instructions
    val INSTRUCTIONS_REGEX = """move (\d+) from (\d+) to (\d+)""".toRegex()
    val instructionsInput = input.last().trim().split("\n")
    val instructions = instructionsInput.map {
        with(INSTRUCTIONS_REGEX.matchEntire(it)!!) {
            Triple(groups[1]?.value?.toInt()!!, groups[2]?.value?.toInt()!!, groups[3]?.value?.toInt()!!)
        }
    }

    // Part 1 - follow the instructions and get the top crates
    instructions.forEach { inst ->
        repeat(inst.first) {
            crates[inst.third - 1].add(0, crates[inst.second - 1].removeFirst())
        }
    }
    val part1 = crates.map { it.first() }.joinToString("")
    println(part1)

    // Part 2 - follow the instructions and get the top crates
    instructions.forEach { inst ->
        crates[inst.third - 1].addAll(0, crates[inst.second - 1].take(inst.first))
        crates[inst.second - 1] = crates[inst.second - 1].drop(inst.first).toMutableList()
    }
    val part2 = crates.map { it.firstOrNull() }.joinToString("")
    println(part2)
}
