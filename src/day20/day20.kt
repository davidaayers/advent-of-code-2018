package day20

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
    val input = """^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$""".substring(1)
    //val input = readEntireFile("/day20/input.txt").trim().substring(1)

    val branchPoints = LinkedList<Room>() as Deque<Room>

    val start = Room(Point(0, 0))

    val rooms = mutableListOf(start)
    val ends = mutableListOf<Room>()

    var exploringFrom = start

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

                // explore from here
                exploringFrom = newRoom
            }
            '(' -> branchPoints.push(exploringFrom)
            '|' -> exploringFrom = branchPoints.peek()
            ')' -> {
                ends.add(exploringFrom)
                exploringFrom = branchPoints.pop()
            }
        }
    }

    renderMap(rooms, start)
    println("ends = $ends")
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