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
    val input = """^ENWWW(NEEE|SSE(EE|N))$""".substring(1)

    val checkpoints = LinkedList<Room>() as Deque<Room>

    val start = Room(Point(0, 0))

    val rooms = mutableListOf(start)

    var exploringFrom = start

    for (instruction in input) {
        if (instruction in "NSEW") {
            // add a room in that direction
            val dir = mapDirs[instruction]!!
            // move twice in dir to make room for the door between the rooms
            val newRoom = Room(exploringFrom.point + dir + dir)
            newRoom.connectingRooms.add(Door(oppositeDirs[instruction]!!, exploringFrom))
            rooms.add(newRoom)
            exploringFrom.connectingRooms.add(Door(instruction, newRoom))
            exploringFrom = newRoom
        }
        if (instruction == '(') {
            break
        }
    }

    renderMap(rooms, exploringFrom)
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

        for (door in room.connectingRooms) {
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
}

data class Door(val dir: Char, val room: Room)