import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 5")

    // Setup - read the crate map
    val input = File("day5input").readText().split("\n\n")

    // Parse the starting crate configuration
    val cratesInput = input.first().split("\n").reversed()
    val numStacks = cratesInput.first().trim().split(" ").last().toInt()
    val initialStacks = ArrayList<ArrayList<Char>>(numStacks)
    repeat(numStacks) { i -> initialStacks.add(arrayListOf()) }
    cratesInput.drop(1).forEach { line ->
        line.drop(1).windowed(1, 4).forEachIndexed { i, item ->
            if (item.isNotBlank()) initialStacks[i].add(0, item.first())
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

    // Part 1 - follow the instructions moving one crate at a time
    val stacks1 = initialStacks.map { ArrayList(it) }
    instructions.forEach { inst ->
        repeat(inst.first) {
            stacks1[inst.third - 1].add(0, stacks1[inst.second - 1].removeFirst())
        }
    }
    val part1 = stacks1.map { it.first() }.joinToString("")
    println("The items on top of each stack after part1 are $part1")

    // Part 2 - follow the instructions moving all crates at a time
    val stacks2 = ArrayList(initialStacks.map { ArrayList(it) })
    instructions.forEach { inst ->
        stacks2[inst.third - 1].addAll(0, stacks2[inst.second - 1].take(inst.first))
        stacks2[inst.second - 1] = ArrayList(stacks2[inst.second - 1].drop(inst.first))
    }
    val part2 = stacks2.map { it.firstOrNull() }.joinToString("")
    println("The items on top of each stack after part2 are $part2")
}
