package hws.hw1.task3

class PerformedCommandStorage : MutableList<Int> by ArrayList() {

    private enum class CommandName { PUSHFRONT, PUSHBACK, MOVE }

    private data class Command(val type: CommandName, val arg1: Int? = null, val arg2: Int? = null)

    private val commandStack = ArrayDeque<Command>()

    fun pushBack(number: Int) {
        this.add(number)
        commandStack.add(Command(CommandName.PUSHBACK, number))
    }

    fun pushFront(number: Int) {
        this.add(0, number)
        commandStack.add(Command(CommandName.PUSHFRONT, number))
    }

    fun move(from: Int, to: Int) {
        require(0 <= from && from < this.size && 0 <= to && to < this.size) {
            "at least one of the indexes is out of bounds"
        }

        this.add(if (from < to) (to + 1) else to, this[from])
        this.removeAt(if (from < to) from else (from + 1))

        commandStack.add(Command(CommandName.MOVE, from, to))
    }

    fun undo() {
        require(commandStack.isNotEmpty()) { "no commands on stack now" }
        val latestCommand = commandStack.last()
        commandStack.removeLast()

        when (latestCommand.type) {
            CommandName.PUSHFRONT -> this.removeFirst()
            CommandName.PUSHBACK -> this.removeLast()
            CommandName.MOVE -> this.move(latestCommand.arg2!!, latestCommand.arg1!!)
        }
    }
}
