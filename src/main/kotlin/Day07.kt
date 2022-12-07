import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 7")

    // Setup - Read the file system
    val root = FSNode("/", NodeType.Directory, 0, null)
    var current = root
    var listing = false
    File("day7input").readLines().forEach { line ->
        if (listing) {
            if (line.first() == '$') listing = false
            else {
                val parts = line.split(" ")
                parts.first().toLongOrNull()?.let { size ->
                    val newNode = FSNode(parts.last(), NodeType.File, size, current)
                    current.contents.add(newNode)
                }
                if (parts.first() == "dir") {
                    val newNode = FSNode(parts.last(), NodeType.Directory, 0, current)
                    current.contents.add(newNode)
                }
            }
        }
        if (!listing) {
            if (line == "$ cd /") current = root
            else if (line == "$ ls") listing = true
            else if (line.startsWith("$ cd")) {
                val dirName = line.split(" ").last()
                if (dirName == "..") current = current.parent!!
                else current = current.contents.find { it.name == dirName }!!
            } else println("unknown command $line")
        }
    }


    // Part 1 - find the total size of all directories 100000 or smaller
    val part1 = root.dirs.filter { it.size <= 100000 }.sumOf { it.size }
    println(part1)

    // Part 2 - find the smallest directory to delete that will result enough free space
    val spaceNeeded = 30000000 - (70000000 - root.size)
    val part2 = root.dirs.filter { it.size >= spaceNeeded }.sortedByDescending { it.size }.last().size
    println(part2)
}

enum class NodeType { File, Directory }

class FSNode(val name:String, val type: NodeType, val sizeOnDisk: Long, val parent: FSNode?) {
    val contents = ArrayList<FSNode>()
    val size: Long get() = if (type == NodeType.File) sizeOnDisk else contents.sumOf { it.size }
    val dirs: List<FSNode>
        get() {
            return contents.filter { it.type == NodeType.Directory } +
                contents.filter { it.type == NodeType.Directory }.flatMap { it.dirs }
        }
}