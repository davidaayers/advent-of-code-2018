package day20

import readEntireFile
import shared.BaseMap
import shared.Point
import java.util.*

val mapDirs = mapOf(
    'N' to Point(0, -1),
    'S' to Point(0, 1),
    'E' to Point(1, 0),
    'W' to Point(-1, 0)
)

val oppositeDirs = mapOf(
    'N' to 'S',
    'S' to 'N',
    'E' to 'W',
    'W' to 'E'
)

fun main(args: Array<String>) {
    val testInputs = mapOf(
        3 to """^WNE$""",
        10 to """^ENWWW(NEEE|SSE(EE|N))$""",
        18 to """^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$""",
        23 to """^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$""",
        31 to """^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"""
    )

    testInputs.forEach { expected, directions ->
        val (answer, _) = buildMap(directions)
        print("Got $answer for $directions")
        if (answer != expected) {
            println(" <-- Wrong, expected $expected")
        } else {
            println(" *")
        }
        println()
    }

    val (answer, thousandPathRooms) = buildMap(readEntireFile("/day20/input.txt").trim().substring(1))

    println("Got $answer for puzzle input, part1")
    println("Got $thousandPathRooms for puzzle input, part2")

}

private fun buildMap(input: String): Pair<Int, Int> {
    val branchPoints = LinkedList<Room>() as Deque<Room>

    val start = Room(Point(0, 0))

    val rooms = mutableListOf(start)
    val ends = mutableListOf<Room>()

    var exploringFrom = start
    var thousandPathRooms = 0

    input.forEach { instruction ->
        when (instruction) {
            in "NSEW" -> {
                // add a room in that direction
                val dir = mapDirs[instruction]!!
                // move twice in dir to make room for the door between the rooms
                var newRoom = Room(exploringFrom.point + dir + dir)

                // see if this room already exists; if it does, just add a new connection
                val existingRoom = rooms.find { it == newRoom }
                if (existingRoom != null) {
                    newRoom = existingRoom
                } else {
                    rooms.add(newRoom)
                }

                // link the rooms via doors
                newRoom.maybeAddDoor(Door(oppositeDirs[instruction]!!, exploringFrom))
                exploringFrom.maybeAddDoor(Door(instruction, newRoom))

                // new room, path to start and count size
                if (existingRoom == null) {
                    // find the path from the start to this room
                    val newPath = path(newRoom, start)
                    val size = newPath.size
                    //println("Path from $newRoom to $start was $size")
                    if (size >= 1000) {
                        thousandPathRooms++
                    }
                }

                // explore from here
                exploringFrom = newRoom
            }
            '(' -> branchPoints.push(exploringFrom)
            '|' -> {
                ends.add(exploringFrom)
                exploringFrom = branchPoints.peek()
            }
            ')' -> {
                ends.add(exploringFrom)
                exploringFrom = branchPoints.pop()
            }
        }
    }

    if (!ends.contains(exploringFrom)) {
        ends.add(exploringFrom)
    }

    renderMap(rooms, start)

    val allPaths = ends.map { path(start, it) }
    val longestPath = allPaths.maxBy { it.size }!!

    return Pair(longestPath.size, thousandPathRooms)
}

fun renderMap(rooms: List<Room>, currentRoom: Room?) {
    // find the max of all directions
    val minX = rooms.map { it.point.x }.min()
    val maxX = rooms.map { it.point.x }.max()
    val minY = rooms.map { it.point.y }.min()
    val maxY = rooms.map { it.point.y }.max()

    val width = maxX!! - (minX!! - 1) + 2
    val height = maxY!! - (minY!! - 1) + 2

    val xOffset = 0 - minX + 1
    val yOffset = 0 - minY + 1

    val map = Day20Map(width, height)

    rooms.forEach { room ->
        val center = Point(room.point.x + xOffset, room.point.y + yOffset)

        // center of room
        if (room == currentRoom) {
            map.addFeature(center, 'X')
        } else {
            map.addFeature(center, '.')
        }

        // unknown doors
        //center.cardinalDirs().forEach {
        //    map.addFeature(it, '?')
        //}

        room.connectingRooms.forEach { door ->
            val doorPoint = center + mapDirs[door.dir]!!
            when {
                door.dir in "NS" -> map.addFeature(doorPoint, '-')
                door.dir in "EW" -> map.addFeature(doorPoint, '|')
            }
        }

    }

    println(map)
}

fun path(from: Room, to: Room): List<Room> {
    val visited = mutableSetOf<RoomNode>()
    val frontier = LinkedList<RoomNode>() as Deque<RoomNode>
    frontier.add(RoomNode(from.point, from))
    while (frontier.isNotEmpty()) {
        val exploring = frontier.poll()
        if (visited.contains(exploring)) continue
        visited.add(exploring)
        if (exploring.point == to.point) {
            // found it, return the path
            val path = mutableListOf<Room>()

            var node = exploring
            while (node.parent != null) {
                path.add(0, node.room)
                node = node.parent
            }

            return path
        }

        // add all connected rooms to the frontier
        frontier.addAll(exploring.room.connectingRooms.map {
            RoomNode(it.room.point, it.room, exploring)
        })
    }

    return mutableListOf()
}

class RoomNode(val point: Point, val room: Room) {
    var parent: RoomNode? = null

    constructor(location: Point, room: Room, parent: RoomNode? = null) : this(location, room) {
        this.parent = parent
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomNode

        if (point != other.point) return false

        return true
    }

    override fun hashCode(): Int {
        return point.hashCode()
    }
}

class Day20Map(width: Int, height: Int) : BaseMap(width, height) {
    override fun instantiateMap(width: Int, height: Int, bgChar: Char): BaseMap {
        return Day20Map(width, height)
    }
}

data class Room(val point: Point) {
    val connectingRooms = mutableListOf<Door>()

    fun maybeAddDoor(door: Door) {
        if (!connectingRooms.contains(door)) {
            connectingRooms.add(door)
        }
    }
}

data class Door(val dir: Char, val room: Room)