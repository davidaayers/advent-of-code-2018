package day24

fun main(args: Array<String>) {
    val (immuneArmies, infectionArmies) = testData()
    val allArmies = mutableListOf<Army>().apply {
        addAll(immuneArmies)
        addAll(infectionArmies)
    }

    var endCombat = false
    var round = 1
    while (!endCombat) {
        println("---> Round $round start, status --->")
        allArmies.forEach {
            println(it.desc())
        }

        // targeting first
        println("----> Starting Target Selection, Round $round <----")
        val alreadyTargeted = mutableListOf<Army>()
        val battles = mutableListOf<Pair<Army, Army>>()

        val choseTargetOrder =
            allArmies.sortedWith(
                compareByDescending<Army> { it.size * it.attackValue }
                    .thenByDescending { it.initiative }
            )

        for (army in choseTargetOrder) {
            // find a target
            print("Choosing Target for = ${army.desc()}")
            allArmies
                .filter { it.side != army.side }
                .filter { it !in alreadyTargeted }
                .sortedWith(
                    compareByDescending<Army> { it.estimateDamageBy(army) }
                        .thenByDescending { it.size * it.attackValue }
                        .thenByDescending { it.initiative }
                ).firstOrNull().let {
                    if (it != null) {
                        println(", chose = ${it.desc()}")
                        battles.add(Pair(army, it))
                        alreadyTargeted.add(it)
                    } else {
                        println(", NO TARGET CHOSEN")
                    }

                }
        }

        println("----> Target selection complete <----")

        println("----> Starting Battle round <----")
        // now resolve the battles
        battles
            .sortedWith(compareByDescending { it.first.initiative })
            .forEach { pair ->
                val attacker = pair.first
                val defender = pair.second
                defender.attackedBy(attacker)
            }

        println("----> Battle round complete <----")

        allArmies.removeIf { it.size == 0 }

        // if one side is dead, end combat
        val armiesLeft = allArmies.groupingBy { it.side }.eachCount()

        //println("armiesLeft = ${armiesLeft}\n\n")
        println("\n")

        if (armiesLeft.size == 1) {
            val totalUnitsLeft = allArmies.sumBy { it.size }
            println("All armies left: $allArmies")
            println("totalUnitsLeft = $totalUnitsLeft")
            endCombat = true
        }
        round++
    }
}


fun testData(): Pair<List<Army>, List<Army>> {
    /*
    Immune System:
    17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
    989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
    */
    val is1 = Army(Side.IMMUNE_SYSTEM, 1, 17, 5390, AttackTypes.FIRE, 4507, 2).apply {
        weaknesses.add(AttackTypes.RADIATION)
        weaknesses.add(AttackTypes.BLUDGEONING)
    }

    val is2 = Army(Side.IMMUNE_SYSTEM, 2, 989, 1274, AttackTypes.SLASHING, 24, 3).apply {
        immunities.add(AttackTypes.FIRE)
        weaknesses.add(AttackTypes.BLUDGEONING)
        weaknesses.add(AttackTypes.SLASHING)
    }

    val immuneArmies = listOf(is1, is2)

    /*
    Infection:
    801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
    4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
     */
    val if1 = Army(Side.INFECTION, 1, 801, 4706, AttackTypes.BLUDGEONING, 116, 1).apply {
        weaknesses.add(AttackTypes.RADIATION)
    }

    val if2 = Army(Side.INFECTION, 2, 4485, 2961, AttackTypes.SLASHING, 12, 4).apply {
        immunities.add(AttackTypes.RADIATION)
        weaknesses.add(AttackTypes.FIRE)
        weaknesses.add(AttackTypes.COLD)
    }

    val infectionArmies = listOf(if1, if2)

    return Pair(immuneArmies, infectionArmies)

}

enum class AttackTypes {
    BLUDGEONING,
    FIRE,
    SLASHING,
    COLD,
    RADIATION
}

enum class Side {
    IMMUNE_SYSTEM,
    INFECTION
}

data class Army(
    val side: Side,
    val id: Int,
    var size: Int,
    val hp: Int,
    val attackType: AttackTypes,
    val attackValue: Int,
    val initiative: Int
) {
    val immunities = mutableListOf<AttackTypes>()
    val weaknesses = mutableListOf<AttackTypes>()

    fun desc(): String {
        return "$side group $id ($size units, $hp hp)"
    }

    fun estimateDamageBy(otherArmy: Army): Int {
        if (otherArmy.attackType in immunities) {
            return 0
        }
        val totalDmg = otherArmy.size * otherArmy.attackValue
        return if (otherArmy.attackType in weaknesses) totalDmg * 2 else totalDmg
    }

    fun attackedBy(otherArmy: Army): Int {
        val dmg = estimateDamageBy(otherArmy)

        if (dmg == 0) return 0

        var numKilled = dmg / hp

        if (numKilled > size) numKilled = size

        size -= numKilled

        println("${otherArmy.desc()} attacks ${this.desc()}, killing $numKilled units with $dmg damage")

        return numKilled
    }
}