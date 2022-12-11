import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 11")

    // Setup - Configure the monkeys
    val monkeys = File("day11input").readText().split("\n\n").map { m ->
        val monkey = m.lines().map { it.trim() }
        val id = monkey[0].substringBefore(":").split(" ").last().toInt()
        val items = monkey[1].substringAfter(": ").split(", ").map { it.toLong() }
        val op = monkey[2].substringAfter("= old ").split(" ")
        val operand = op.last().toLongOrNull()?:0L
        val operation = if (operand != 0L) {
            when (op.first()) { "+" -> Operation.Add else -> Operation.Multiply }
        } else Operation.Square
        val divisor = monkey[3].substringAfter("divisible by ").toLong()
        val trueMonkey = monkey[4].substringAfter("throw to monkey ").toInt()
        val falseMonkey = monkey[5].substringAfter("throw to monkey ").toInt()
        Monkey(id, items, operation, operand, divisor, trueMonkey, falseMonkey)
    }

    // Part 1 - play 20 rounds and find the two most active monkeys
    repeat(20) { monkeys.playRound { i -> i / 3 } }
    val part1 = monkeys
        .sortedByDescending { it.inspectCount }
        .take(2)
        .fold(1L) { product, monkey -> product * monkey.inspectCount }
    println("Monkey business after 20 rounds is $part1")

    monkeys.reset()

    // Part 2 - play 10,000 rounds and find the two most active monkeys
    // Your worry level does not go down at all during this game
    val divisorProduct = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisor }
    repeat(10000) { monkeys.playRound { i -> i % divisorProduct } }
    val part2 = monkeys
        .sortedByDescending { it.inspectCount }
        .take(2)
        .fold(1L) { product, monkey -> product * monkey.inspectCount }
    println("Monkey business after 10,000 rounds and no decreasing worry is $part2")
}

fun List<Monkey>.reset() = forEach { it.reset() }

fun List<Monkey>.playRound(worryAdjust: (Long) -> Long) {
    for (monkey in this) {
        monkey.getItems().forEach { item ->
            val (who, newItem) = monkey.inspectItem(item, worryAdjust)
            this.first { it.id == who }.takeItem(newItem)
        }
    }
}

enum class Operation { Add, Multiply, Square }

class Monkey(
    val id: Int,
    private val initialItems: List<Long>,
    private val op: Operation,
    private val operand: Long,
    val divisor: Long,
    private val whenTrue: Int,
    private val whenFalse: Int
) {
    var inspectCount = 0
    private val items = ArrayList(initialItems)
    fun getItems() = items.toList()
    fun takeItem(item: Long) = items.add(item)
    fun inspectItem(item: Long, worryAdjust: (Long) -> Long): Pair<Int, Long> {
        // remove and inspect
        inspectCount++
        items.remove(item)
        val newLevel = worryAdjust(
            when (op) {
                Operation.Add -> item + operand
                Operation.Multiply -> item * operand
                Operation.Square -> item * item
            })
        return if (newLevel % divisor == 0L) Pair(whenTrue, newLevel) else Pair(whenFalse, newLevel)
    }
    fun reset() {
        inspectCount = 0
        items.clear()
        items.addAll(initialItems)
    }
}
