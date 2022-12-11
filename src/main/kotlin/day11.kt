fun main(args: Array<String>) {
    println("2022 Advent of Code day 11")

    // Setup - Configure the monkeys
//    val monkeys = listOf(
//        Monkey(0, arrayListOf(79, 98), Operation.Multiply, 19, 23, 2, 3),
//        Monkey(1, arrayListOf(54, 65, 75, 74), Operation.Add, 6,  19, 2, 0),
//        Monkey(2, arrayListOf(79, 60, 97), Operation.Square, 0,  13, 1, 3),
//        Monkey(3, arrayListOf(74), Operation.Add, 3,  17, 0, 1),
//    )
    // 23, 19, 13, 17
    // 96577

    val monkeys = listOf(
        Monkey(0, arrayListOf(83, 62, 93), Operation.Multiply, 17,2, 1, 6),
        Monkey(1, arrayListOf(90, 55), Operation.Add, 1, 17, 6, 3),
        Monkey(2, arrayListOf(91, 78, 80, 97, 79, 88), Operation.Add, 3, 19, 7, 5),
        Monkey(3, arrayListOf(64, 80, 83, 89, 59), Operation.Add, 5, 3, 7, 2),
        Monkey(4, arrayListOf(98, 92, 99, 51), Operation.Square, 0,5, 0, 1),
        Monkey(5, arrayListOf(68, 57, 95, 85, 98, 75, 98, 75), Operation.Add, 2, 13, 4, 0),
        Monkey(6, arrayListOf(74), Operation.Add, 4, 7, 3, 2),
        Monkey(7, arrayListOf(68, 64, 60, 68, 87, 80, 82), Operation.Multiply, 19, 11, 4, 5),
    )
    // 2, 17, 19, 3, 5, 13, 7, 11
    // 9699690

    repeat(10000) {
        monkeys.playRound()
    }
    monkeys.forEach { println(it.inspectCount) }
    val part1 = monkeys.sortedByDescending { it.inspectCount }.take(2).fold(1L) { product, monkey -> product * monkey.inspectCount }
    println(part1)
}

enum class Operation { Add, Multiply, Square }

class Monkey (val id: Int, val items: ArrayList<Long>, val op: Operation, val operand: Int, val test: Long, val whenTrue: Int, val whenFalse: Int) {
    var inspectCount = 0L
    fun takeItem(item: Long) {
        items.add(item)
    }
    fun inspectItem(item: Long): Pair<Int, Long> {
        // inspect
        inspectCount++
        val newLevel = when (op) {
            Operation.Add -> item + operand
            Operation.Multiply -> item * operand
            Operation.Square -> item * item
        } % 9699690
        // test and throw
        items.remove(item)
        return if (newLevel % test == 0L) Pair(whenTrue, newLevel) else Pair(whenFalse, newLevel)
    }
}

fun List<Monkey>.playRound() {
    for (monkey in this) {
        monkey.items.toList().forEach { item ->
            val (who, newItem) = monkey.inspectItem(item)
            this.first { it.id == who }.takeItem(newItem)
        }
    }
}