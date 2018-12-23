package shared

abstract class AStarNode<T>(val obj: T) : Comparable<T> {
    var parent: AStarNode<T>? = null
    var costFromStart: Float = 0.0F
    var estimatedCostToGoal: Float = 0.0F

    fun cost(): Float {
        return costFromStart + estimatedCostToGoal
    }

    override fun compareTo(other: T): Int {
        other as AStarNode<*>
        return this.cost().compareTo(other.cost())
    }

    abstract fun neighbors(): List<AStarNode<T>>
    abstract fun costToNeighbor(neighbor: AStarNode<T>): Float
    abstract fun estimatedCostToGoal(other: AStarNode<T>): Float
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int
}