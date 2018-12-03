package day03

import readFileIntoLines

fun main(args: Array<String>) {
    //findNonOverlappingClaim(readFileIntoLines("/day03/small-input.txt"), 20, 20)
    findNonOverlappingClaim(readFileIntoLines("/day03/input.txt"), 1000, 1000)
}

private fun findNonOverlappingClaim(lines: List<String>, maxX: Int, maxY: Int) {
    val claims = lines.map {
        claim {
            input(it)
        }
    }

    // create our "cloth" array
    val cloth: Array<Array<ArrayList<Int>>> = Array(maxX) {
        Array(maxY) {
            arrayListOf<Int>()
        }
    }

    val claimsMap: Map<Int,Claim> = claims.map { it.claimId to it }.toMap()
    println("claimsMap = ${claimsMap}")

    // go through out claims and add them to the list of claims for each square "inch"
    claims.forEach { claim ->
        println("Examining claim: $claim")
        for (x in claim.x until claim.x + claim.width) {
            for (y in claim.y until claim.y + claim.height) {
                cloth[x][y].add(claim.claimId)
            }
        }
    }

    // print the claims, for funsies
    for (x in 0 until maxX) {
        for (y in 0 until maxY) {
            print("[${if (cloth[x][y].size == 0) " " else cloth[x][y].size.toString()}]")
        }
        println("")
    }

    // now find very inch that has only a single claim
    val inchesWithSingleClaims = cloth.flatMap { it.asList() }.filter { it.size == 1 }.map { it[0] }.toList()
    println(inchesWithSingleClaims)
    println("Found ${inchesWithSingleClaims.size} possibilities for non-overlapping claims")

    // now, go through each of those, and see if the claim overlaps anything else
    val winningClaim = findWinningClaim(inchesWithSingleClaims, claimsMap, cloth)

    if(winningClaim != null){
        println("Found Winning Claim = $winningClaim")
    }
}

private fun findWinningClaim(
    inchesWithSingleClaims: List<Int>,
    claimsMap: Map<Int, Claim>,
    cloth: Array<Array<ArrayList<Int>>>
): Claim? {
    inchesWithSingleClaims.forEach { claimId ->
        val claim = claimsMap[claimId]
        var found = true
        if (claim != null) {
            for (x in claim.x until claim.x + claim.width) {
                for (y in claim.y until claim.y + claim.height) {
                    val inch = cloth[x][y]
                    if (inch.size > 1) {
                        found = false
                    }
                }
            }
            if (found) {
                return claim
            }
        }
    }
    return null
}