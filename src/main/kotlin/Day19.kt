import java.io.File

fun main(args: Array<String>) {
    println("2022 Advent of Code day 19")

    // Setup - read the blueprints
    val blueprintRegex = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()
    val factories = File("day19test").readLines().map { line ->
        val match = blueprintRegex.matchEntire(line)!!
        Blueprint(match.groups[1]!!.value.toInt(),
            match.groups[2]!!.value.toInt(),
            match.groups[3]!!.value.toInt(),
            match.groups[4]!!.value.toInt(),
            match.groups[5]!!.value.toInt(),
            match.groups[6]!!.value.toInt(),
            match.groups[7]!!.value.toInt(),)
    }.map { Factory(it) }

    println("There are ${factories.size} factories")

    val startTime = System.currentTimeMillis()
    val result = factories[1].produceMax(ResourceType.Geode, 24, HashMap<ResourceType, Int>(), hashMapOf(Pair(ResourceType.Ore, 1)))
    val totalTime = System.currentTimeMillis() - startTime
    println("Mined $result resources in $totalTime ms")
}

enum class ResourceType { Ore, Clay, Obsidian, Geode, None }

class Blueprint (val id: Int,
        oreCostOre: Int,
        clayCostOre: Int,
        obsidianCostOre: Int,
        obsidianCostClay: Int,
        geodeCostOre: Int,
        geodeCostObsidian: Int
    ) {
    val oreCost = listOf(Pair(ResourceType.Ore, oreCostOre))
    val clayCost = listOf(Pair(ResourceType.Ore, clayCostOre))
    val obsidianCost = listOf(Pair(ResourceType.Ore, obsidianCostOre), Pair(ResourceType.Clay, obsidianCostClay))
    val geodeCost = listOf(Pair(ResourceType.Ore, geodeCostOre), Pair(ResourceType.Obsidian, geodeCostObsidian))
}

class Factory(blueprint: Blueprint) {
    val id = blueprint.id
    val robotCosts = mapOf(
        Pair(ResourceType.Ore, blueprint.oreCost),
        Pair(ResourceType.Clay, blueprint.clayCost),
        Pair(ResourceType.Obsidian, blueprint.obsidianCost),
        Pair(ResourceType.Geode, blueprint.geodeCost),
        Pair(ResourceType.None, listOf(Pair(ResourceType.None, 0))),
    )
    val maxNeeded = mapOf(
        Pair(ResourceType.Ore, robotCosts.maxOf { it.value.firstOrNull { it.first == ResourceType.Ore }?.second ?: 0 }),
        Pair(ResourceType.Clay, robotCosts.maxOf { it.value.firstOrNull { it.first == ResourceType.Clay }?.second ?: 0 }),
        Pair(ResourceType.Obsidian, robotCosts.maxOf { it.value.firstOrNull { it.first == ResourceType.Obsidian }?.second ?: 0 }),
        Pair(ResourceType.Geode, Int.MAX_VALUE),
        Pair(ResourceType.None, Int.MAX_VALUE),
    )

    fun produceMax(resourceType: ResourceType,
                   time: Int,
                   resources: HashMap<ResourceType, Int>,
                   robots: HashMap<ResourceType, Int>): Int {
//        println("Time Remaining: $time")
//        resources.forEach { (type, count) ->
//            println("$type: $count")
//        }
//        robots.forEach { (type, count) ->
//            println("$type robots: $count")
//        }

        if (time > 1) {
            // Filter out which robots to build based on the max needed based on the blueprint
            //var canBuild = ResourceType.values().toList()
            var canBuild = ResourceType.values().filter { robots.getOrDefault(it, 0) < maxNeeded.getValue(it) }
            // Filter out robots that cost too much
            canBuild = canBuild.filter { type ->
                robotCosts.getValue(type).all {
                    resources.getOrDefault(it.first, 0) >= it.second
                }
            }.toList()
            // Produce the resources for this interval
            robots.forEach { (type, count) ->
                resources[type] = resources.getOrDefault(type, 0) + count
            }
            // If there are only two intervals remaining, only consider building a robot for the desired resource
            //if (time == 2) canBuild = canBuild.filter { it == resourceType || it == ResourceType.None }
            // Make the recursive call to get the maximum resourceType generated from each choice
//            print("Building ")
//            canBuild.forEach { type -> print("$type, ") }
//            print("robots\n")
            return canBuild.map { type ->
                val newResources = HashMap(resources)
                val newRobots = HashMap(robots)
                robotCosts.getValue(type).forEach { cost ->
                    newResources[cost.first] = newResources.getOrDefault(cost.first, 0) - cost.second
                }
                newRobots[type] = newRobots.getOrDefault(type, 0) + 1
//                println("Building a $type robot")
//                readln()
                produceMax(resourceType, time - 1, newResources, newRobots)
            }.max()
        } else {
            // Just produce the resources for this final interval
            robots.forEach { (type, count) ->
                resources[type] = resources.getOrDefault(type, 0) + count
            }
            //readln()
            return resources.getOrDefault(resourceType, 0)
        }
    }
}
