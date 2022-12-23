import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    println("2022 Advent of Code day 21")

    // Setup - Read the signal
    val monkeyNums = HashMap<String, MonkeyNumber>()
    File("day21input").readLines().forEach { line ->
        val idSplit = line.split(":")
        val id = idSplit.first()
        if (idSplit.last().trim().count { it == ' ' } > 0) {
            val contents = idSplit.last().trim().split(" ")
            val idLeft = contents.first().trim()
            val idRight = contents.last().trim()
            val op = when (contents[1].trim()) {
                "+" -> MonkeyOp.Add
                "-" -> MonkeyOp.Subtract
                "*" -> MonkeyOp.Multiply
                "/" -> MonkeyOp.Divide
                else -> MonkeyOp.None
            }
            val left = monkeyNums.getOrDefault(idLeft, MonkeyNumber(idLeft))
            monkeyNums[idLeft] = left
            val right = monkeyNums.getOrDefault(idRight, MonkeyNumber(idRight))
            monkeyNums[idRight] = right
            val monkeyNum = monkeyNums[id]
            if (monkeyNum != null) {
                monkeyNum.left = left
                monkeyNum.right = right
                monkeyNum.op = op
            } else {
                monkeyNums[id] = MonkeyNumber(id, null, left, right, op)
            }
        } else {
            val value = idSplit.last().trim().toLong()
            val monkeyNum = monkeyNums[id]
            if (monkeyNum != null) {
                monkeyNum.value = value
            } else {
                monkeyNums[id] = MonkeyNumber(id, value)
            }
        }
    }

    println(monkeyNums.count())

    // Part 1 - solve for the root monkey
    val root = monkeyNums.getValue("root")
    val part1 = root.getValue()
    println("Root yells $part1")

    // Part 2 - solve for humn
    // root.op = MonkeyOp.Equal
    // val part2Exp = root.asExpression()
    // println(part2Exp)
    val part2 = if (root.left!!.canReduce())
        root.right!!.solveHuman(root.left!!.getValue())
    else
        root.left!!.solveHuman(root.right!!.getValue())
    println("To balance the equation, humn = $part2")
}

enum class MonkeyOp { Add, Subtract, Divide, Multiply, Equal, None }
class MonkeyNumber(val id: String,
                   var value: Long? = null,
                   var left: MonkeyNumber? = null,
                   var right: MonkeyNumber? = null,
                   var op: MonkeyOp = MonkeyOp.None) {
    fun getValue(): Long {
        if (value != null) return value!!
        val leftVal = left?.getValue()?: 0
        val rightVal = right?.getValue()?: 0
        return when (op) {
            MonkeyOp.Add -> leftVal + rightVal
            MonkeyOp.Subtract -> leftVal - rightVal
            MonkeyOp.Multiply -> leftVal * rightVal
            MonkeyOp.Divide -> leftVal / rightVal
            MonkeyOp.Equal -> if (leftVal < rightVal) -1 else if (leftVal > rightVal) 1 else 0
            MonkeyOp.None -> 0
        }
    }

    fun asExpression(): String {
        if (id == "humn") return id
        if (value != null) return value!!.toString()
        val leftExp = left!!.asExpression()
        val rightExp = right!!.asExpression()
        val opString = when(op) {
            MonkeyOp.Add -> " + "
            MonkeyOp.Subtract -> " - "
            MonkeyOp.Multiply -> " * "
            MonkeyOp.Divide -> " / "
            MonkeyOp.Equal -> " = "
            MonkeyOp.None -> " ERROR "
        }
        return "($leftExp$opString$rightExp)"
    }

    fun canReduce(): Boolean {
        if (id == "humn") return false
        if (value != null) return true
        return (left!!.canReduce() && right!!.canReduce())
    }
    fun solveHuman(eq: Long): Long {
        if (id == "humn") return eq
        val humanOnLeft = right!!.canReduce()
        val operand = if (humanOnLeft) right!!.getValue() else left!!.getValue()
        val next = if (humanOnLeft) left!! else right!!
        if (op == MonkeyOp.Add) return next.solveHuman(eq - operand)
        if (op == MonkeyOp.Multiply) return next.solveHuman(eq / operand)
        if (humanOnLeft && op == MonkeyOp.Subtract) return next.solveHuman(eq + operand)
        if (humanOnLeft && op == MonkeyOp.Divide) return next.solveHuman(eq * operand)
        if (op == MonkeyOp.Subtract) return next.solveHuman((-1 * eq) + operand)
        if (op == MonkeyOp.Divide) return next.solveHuman(operand / eq)
        throw Exception("Got lost in the tree")
    }
}