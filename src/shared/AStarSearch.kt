package shared

import java.util.*

class AStarSearch<T> {
    fun findPath(start: AStarNode<T>, goal: AStarNode<T>): List<AStarNode<T>> {
        val frontier = PriorityQueue<AStarNode<T>>() as Queue<AStarNode<T>>
        val visited = mutableSetOf<AStarNode<T>>()

        start.estimatedCostToGoal = start.estimatedCostToGoal(goal)

        frontier.add(start)

        while (frontier.isNotEmpty()) {
            val exploring = frontier.poll()

            if (exploring.sameAs(goal)) {
                return constructPathFrom(exploring)
            }

            val neighbors = exploring.neighbors()
            for (neighbor in neighbors) {
                if (neighbor in frontier) continue

                val costFromStart = exploring.costFromStart + exploring.costToNeighbor(neighbor)
                val existing = visited.firstOrNull { it == neighbor }

                if (existing != null) {
                    // see if this node has a shorter distance then the one previously
                    // visited, if so, replace it
                    if (costFromStart < existing.costFromStart) {
                        visited.remove(existing)
                    }

                }

                neighbor.parent = exploring
                neighbor.costFromStart = costFromStart
                neighbor.estimatedCostToGoal = neighbor.estimatedCostToGoal(goal)
                frontier.add(neighbor)
            }

            visited.add(exploring)
        }


        return listOf()
    }

    private fun constructPathFrom(someNode: AStarNode<T>): List<AStarNode<T>> {
        val path = mutableListOf<AStarNode<T>>()

        var node = someNode
        while (node.parent != null) {
            path.add(0, node)
            node = node.parent!!
        }

        // add the start node, which will be the last node assigned to "node"
        path.add(0,node)

        return path
    }
}