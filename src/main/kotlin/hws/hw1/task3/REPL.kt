package hws.hw1.task3

import kotlin.system.exitProcess

class REPL {

    enum class REPLCommandNames { PUSHBACK, PUSHFRONT, UNDO, MOVE, PRINT, HELP, EXIT }
    data class REPLCommand(var commandName: REPLCommandNames, var arg1: Int = 0, var arg2: Int = 0)

    private val storangeInstance = PerformedCommandStorage()
    private val helpMessage =
        """Available commands:
            |  pushback <int>     appends the number to the list
            |  pushfront <int>    adds the number to the front of the list
            |  undo               undoes the latest action
            |  print              prints the list
            |  move <int> <int>   moves the element from one place to another
            |  help               shows this message
            |  exit               exits the repl
        """.trimMargin()

    fun run() {
        while (true) {
            print(">>> ")
            val parsedCommand = parseRawCommand(readln())
            executeCommand(parsedCommand)
        }
    }

    private fun parseCommandName(commandNameStr: String): REPLCommandNames = when (commandNameStr) {
        "pushback" -> REPLCommandNames.PUSHBACK
        "pushfront" -> REPLCommandNames.PUSHFRONT
        "undo" -> REPLCommandNames.UNDO
        "move" -> REPLCommandNames.MOVE
        "print" -> REPLCommandNames.PRINT
        "exit" -> REPLCommandNames.EXIT
        else -> REPLCommandNames.HELP
    }

    companion object {
        const val ONE_ARGUEMENT = 2
        const val TWO_ARGUEMENTS = 3
    }

    private fun parseRawCommand(rawCommand: String): REPLCommand {
        val splitted = rawCommand.trim().split(" ")

        val commandName = parseCommandName(splitted.first())
        val resultingCommand = REPLCommand(commandName)

        if ((commandName == REPLCommandNames.PUSHBACK || commandName == REPLCommandNames.PUSHFRONT) &&
            splitted.size == ONE_ARGUEMENT
        ) {
            val arg = splitted[1].toIntOrNull()
            if (arg != null)
                resultingCommand.arg1 = arg
            else
                resultingCommand.commandName = REPLCommandNames.HELP
        } else if (commandName == REPLCommandNames.MOVE && splitted.size == TWO_ARGUEMENTS) {
            val arg1 = splitted[1].toIntOrNull()
            val arg2 = splitted[2].toIntOrNull()
            if (arg1 != null && arg2 != null) {
                resultingCommand.arg1 = arg1
                resultingCommand.arg2 = arg2
            } else {
                resultingCommand.commandName = REPLCommandNames.HELP
            }
        } else {
            resultingCommand.commandName = REPLCommandNames.HELP
        }

        return resultingCommand
    }

    private fun executeCommand(command: REPLCommand) {
        try {
            when (command.commandName) {
                REPLCommandNames.PUSHFRONT -> storangeInstance.pushFront(command.arg1)
                REPLCommandNames.PUSHBACK -> storangeInstance.pushBack(command.arg1)
                REPLCommandNames.MOVE -> storangeInstance.move(command.arg1, command.arg2)
                REPLCommandNames.PRINT -> println(storangeInstance.joinToString { it.toString() })
                REPLCommandNames.UNDO -> storangeInstance.undo()
                REPLCommandNames.EXIT -> exitProcess(0)
                REPLCommandNames.HELP -> println(helpMessage)
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
}
