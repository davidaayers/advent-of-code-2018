package day24

fun main(args: Array<String>) {
    //val (immuneArmies, infectionArmies) = testData()
    val (immuneArmies, infectionArmies) = actualData()
    val allArmies = mutableListOf<Army>().apply {
        addAll(immuneArmies)
        addAll(infectionArmies)
    }

    val immuneSystemWon = false
    var boost = 0
    while (!immuneSystemWon) {
        println("boost = ${boost}")
        val copies = allArmies
            .map { it.copy() }
            .toMutableList()

        copies
            .filter { it.side == Side.IMMUNE_SYSTEM }
            .forEach {
                it.attackValue += boost
            }

        val allArmiesLeft = doCombat(copies, false)
        val armiesLeft = allArmiesLeft.groupingBy { it.side }.eachCount()
        if (armiesLeft[Side.IMMUNE_SYSTEM] != null && boost != 36) {
            val totalUnitsLeft = allArmiesLeft.sumBy { it.size }
            println("Immune system won with boost of $boost, total armies left $totalUnitsLeft")
            break
        }
        boost += 1
    }


}

private fun doCombat(allArmies: MutableList<Army>, debug: Boolean = false): MutableList<Army> {
    var round = 1
    while (true) {
        if (debug) println("---> Round $round start, status --->")
        allArmies.forEach {
            if (debug) println(it.desc())
        }

        // targeting first
        if (debug) println("----> Starting Target Selection, Round $round <----")
        val alreadyTargeted = mutableListOf<Army>()
        val battles = mutableListOf<Pair<Army, Army>>()

        val choseTargetOrder =
            allArmies.sortedWith(
                compareByDescending<Army> { it.size * it.attackValue }
                    .thenByDescending { it.initiative }
            )

        for (army in choseTargetOrder) {
            // find a target
            if (debug) print("Choosing Target for = ${army.desc()}")
            allArmies
                .filter { it.side != army.side }
                .filter { it !in alreadyTargeted }
                .filter { it.estimateDamageBy(army) > 0 }
                .sortedWith(
                    compareByDescending<Army> { it.estimateDamageBy(army) }
                        .thenByDescending { it.size * it.attackValue }
                        .thenByDescending { it.initiative }
                ).firstOrNull().let {
                    if (it != null) {
                        if (debug) println(", chose = ${it.desc()}")
                        battles.add(Pair(army, it))
                        alreadyTargeted.add(it)
                    }
                }
        }

        if (debug) println("----> Target selection complete <----")

        if (debug) println("----> Starting Battle round <----")
        // now resolve the battles
        battles
            .sortedWith(compareByDescending { it.first.initiative })
            .forEach { pair ->
                val attacker = pair.first
                val defender = pair.second
                defender.attackedBy(attacker, debug)
            }

        if (debug) println("----> Battle round complete <----")

        allArmies.removeIf { it.size == 0 }

        // if one side is dead, end combat
        val armiesLeft = allArmies.groupingBy { it.side }.eachCount()

        //println("armiesLeft = ${armiesLeft}\n\n")
        if (debug) println("\n")

        if (armiesLeft.size == 1) {
            val totalUnitsLeft = allArmies.sumBy { it.size }
            if (true) println("---> Final $round status --->")
            allArmies.forEach {
                if (true) println(it.desc())
            }

            if (true) println("totalUnitsLeft = $totalUnitsLeft")
            return allArmies
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

    val is2 = Army(Side.IMMUNE_SYSTEM, 2, 989, 1274, AttackTypes.SLASHING, 25, 3).apply {
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

fun actualData(): Pair<List<Army>, List<Army>> {
    // Immune System:
    // 3321 units each with 6178 hit points (immune to bludgeoning, fire) with an attack that does 18 bludgeoning damage at initiative 20
    val is1 = Army(Side.IMMUNE_SYSTEM, 1, 3321, 6178, AttackTypes.BLUDGEONING, 18, 20).apply {
        immunities.add(AttackTypes.BLUDGEONING)
        immunities.add(AttackTypes.FIRE)
    }

    // 4228 units each with 9720 hit points (weak to bludgeoning) with an attack that does 21 fire damage at initiative 10
    val is2 = Army(Side.IMMUNE_SYSTEM, 2, 4228, 9720, AttackTypes.FIRE, 21, 10).apply {
        weaknesses.add(AttackTypes.BLUDGEONING)
    }

    // 1181 units each with 5833 hit points (weak to bludgeoning; immune to slashing, cold) with an attack that does 44 cold damage at initiative 6
    val is3 = Army(Side.IMMUNE_SYSTEM, 3, 1181, 5833, AttackTypes.COLD, 44, 6).apply {
        weaknesses.add(AttackTypes.BLUDGEONING)
        immunities.add(AttackTypes.SLASHING)
        immunities.add(AttackTypes.COLD)
    }

    // 89 units each with 6501 hit points (weak to slashing, bludgeoning) with an attack that does 715 fire damage at initiative 1
    val is4 = Army(Side.IMMUNE_SYSTEM, 4, 89, 6501, AttackTypes.FIRE, 715, 1).apply {
        weaknesses.add(AttackTypes.SLASHING)
        weaknesses.add(AttackTypes.BLUDGEONING)
    }

    // 660 units each with 5241 hit points (weak to slashing) with an attack that does 75 radiation damage at initiative 11
    val is5 = Army(Side.IMMUNE_SYSTEM, 5, 660, 5241, AttackTypes.RADIATION, 75, 11).apply {
        weaknesses.add(AttackTypes.SLASHING)
    }

    // 3393 units each with 3576 hit points (immune to cold, radiation; weak to fire) with an attack that does 9 fire damage at initiative 3
    val is6 = Army(Side.IMMUNE_SYSTEM, 6, 3393, 3576, AttackTypes.FIRE, 9, 3).apply {
        weaknesses.add(AttackTypes.FIRE)
        immunities.add(AttackTypes.COLD)
        immunities.add(AttackTypes.RADIATION)
    }

    // 2232 units each with 5558 hit points (immune to slashing) with an attack that does 21 fire damage at initiative 7
    val is7 = Army(Side.IMMUNE_SYSTEM, 7, 2232, 5558, AttackTypes.FIRE, 21, 7).apply {
        immunities.add(AttackTypes.SLASHING)
    }

    // 4861 units each with 13218 hit points (weak to slashing, fire) with an attack that does 20 fire damage at initiative 14
    val is8 = Army(Side.IMMUNE_SYSTEM, 8, 4861, 13218, AttackTypes.FIRE, 20, 14).apply {
        weaknesses.add(AttackTypes.SLASHING)
        weaknesses.add(AttackTypes.FIRE)
    }

    // 3102 units each with 7657 hit points (immune to cold; weak to slashing) with an attack that does 24 radiation damage at initiative 17
    val is9 = Army(Side.IMMUNE_SYSTEM, 9, 3102, 7657, AttackTypes.RADIATION, 24, 17).apply {
        weaknesses.add(AttackTypes.SLASHING)
        immunities.add(AttackTypes.COLD)
    }

    // 8186 units each with 5664 hit points (weak to slashing) with an attack that does 6 bludgeoning damage at initiative 9
    val is10 = Army(Side.IMMUNE_SYSTEM, 10, 8186, 5664, AttackTypes.BLUDGEONING, 6, 9).apply {
        weaknesses.add(AttackTypes.SLASHING)
    }

    val immuneArmies = listOf(is1, is2, is3, is4, is5, is6, is7, is8, is9, is10)

    // Infection:
    // 931 units each with 32672 hit points (weak to slashing) with an attack that does 67 slashing damage at initiative 13
    val if1 = Army(Side.INFECTION, 1, 931, 32672, AttackTypes.SLASHING, 67, 13).apply {
        weaknesses.add(AttackTypes.SLASHING)
    }

    // 1328 units each with 40275 hit points (immune to fire, radiation) with an attack that does 54 bludgeoning damage at initiative 5
    val if2 = Army(Side.INFECTION, 2, 1328, 40275, AttackTypes.BLUDGEONING, 54, 5).apply {
        immunities.add(AttackTypes.FIRE)
        immunities.add(AttackTypes.RADIATION)
    }

    // 5620 units each with 43866 hit points (weak to radiation, fire) with an attack that does 12 cold damage at initiative 18
    val if3 = Army(Side.INFECTION, 3, 5620, 43866, AttackTypes.COLD, 12, 18).apply {
        weaknesses.add(AttackTypes.FIRE)
        weaknesses.add(AttackTypes.RADIATION)
    }

    // 3596 units each with 44288 hit points (immune to bludgeoning, fire) with an attack that does 22 slashing damage at initiative 8
    val if4 = Army(Side.INFECTION, 4, 3596, 44288, AttackTypes.SLASHING, 22, 8).apply {
        immunities.add(AttackTypes.FIRE)
        immunities.add(AttackTypes.BLUDGEONING)
    }

    // 85 units each with 15282 hit points (weak to cold, fire) with an attack that does 272 fire damage at initiative 15
    val if5 = Army(Side.INFECTION, 5, 85, 15282, AttackTypes.FIRE, 272, 15).apply {
        weaknesses.add(AttackTypes.FIRE)
        weaknesses.add(AttackTypes.COLD)
    }

    // 129 units each with 49924 hit points (weak to bludgeoning) with an attack that does 681 radiation damage at initiative 4
    val if6 = Army(Side.INFECTION, 6, 129, 49924, AttackTypes.RADIATION, 681, 4).apply {
        weaknesses.add(AttackTypes.BLUDGEONING)
    }

    // 5861 units each with 24179 hit points (weak to slashing) with an attack that does 8 cold damage at initiative 16
    val if7 = Army(Side.INFECTION, 7, 5861, 24179, AttackTypes.COLD, 8, 16).apply {
        weaknesses.add(AttackTypes.SLASHING)
    }

    // 3132 units each with 5961 hit points (immune to radiation) with an attack that does 3 slashing damage at initiative 19
    val if8 = Army(Side.INFECTION, 8, 3132, 5961, AttackTypes.SLASHING, 3, 19).apply {
        immunities.add(AttackTypes.RADIATION)
    }

    // 1336 units each with 56700 hit points (weak to bludgeoning, radiation) with an attack that does 69 radiation damage at initiative 12
    val if9 = Army(Side.INFECTION, 9, 1336, 56700, AttackTypes.RADIATION, 69, 12).apply {
        weaknesses.add(AttackTypes.RADIATION)
        weaknesses.add(AttackTypes.BLUDGEONING)
    }

    // 2611 units each with 28641 hit points with an attack that does 21 fire damage at initiative 2
    val if10 = Army(Side.INFECTION, 10, 2611, 28641, AttackTypes.FIRE, 21, 2)

    val infectionArmies = listOf(if1, if2, if3, if4, if5, if6, if7, if8, if9, if10)

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
    var attackValue: Int,
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

    fun attackedBy(otherArmy: Army, debug: Boolean = false): Int {
        val dmg = estimateDamageBy(otherArmy)

        if (dmg == 0) return 0

        var numKilled = dmg / hp

        if (numKilled > size) numKilled = size

        size -= numKilled

        if (debug) println("${otherArmy.desc()} attacks ${this.desc()}, killing $numKilled units with $dmg damage")

        return numKilled
    }
}