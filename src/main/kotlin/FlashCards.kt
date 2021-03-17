import java.io.File
import StatementConstant.*
import StatementVariable.*
import kotlin.random.Random
import kotlin.system.exitProcess

class FlashCards(args: Array<String>): ListOfAction {

    private var pathOfExport: String? = null
    private var pathOfImport: String? = null
    private val cards = mutableListOf<Card>()
    private val log = mutableListOf<String>()

    init {
        if (args.contains("-export")) {
            pathOfExport = args[args.indexOf("-export") + 1]
        }
        if (args.contains("-import")) {
            pathOfImport = args[args.indexOf("-import") + 1]
        }
        /* If command-line argument -import IMPORT is passed, where IMPORT is the file name,
        * read the initial card set from the external file */
        if (pathOfImport != null) this.import()
    }

    private fun println(message: String) {
        log.add(message)
        kotlin.io.println(message)
    }

    private fun readLine(): String {
        val message = kotlin.io.readLine().toString()
        log.add(message)
        return message
    }

    fun menu() {

        println(InitialRequest.statement)
        var action = readLine()
        while (action in ActionRequest.statement) {
            when (action) {
                "add" -> add()
                "remove" -> remove()
                "import" -> import()
                "export" -> export()
                "ask" -> ask()
                "exit" -> exit()
                "log" -> log()
                "hardest card" -> hardestCard()
                "reset stats" -> resetStats()
                else -> println(ShitResponse.statement)
            }
            println("\n" + InitialRequest.statement)
            action = readLine()

            while (action !in ActionRequest.statement) {
                println("\n" + InitialRequest.statement)
                action = readLine()
            }
        }
    }

    /* create a new flashcard with a unique term and definition. After adding the card,
    * output the message The pair ("term":"definition") has been added, where "term" is
    * the term entered by the user and "definition" is the definition entered by the user. */
    override fun add() {
        println(TermRequest.statement)
        val term = readLine()
        if (term in cards.map { it.term }) {
            println(TermResponse.response(term))
        } else {
            println(DefinitionRequest.statement)
            val definition = readLine()
            if (definition in cards.map { it.definition }) {
                println(DefinitionResponse.response(definition))
            } else {
                cards.add(Card(term, definition))
                println(AddResponse.response(term, definition))
            }
        }
    }

    /* ask the user for the term of the card they want to remove with the message, and read
    * the user's input from the next line. If a matching flashcard exists, remove it from
    * the set and output the message. If there is no such flashcard in the set, output the message */
    override fun remove() {
        println(RemoveRequest.statement)
        val card = readLine()
        if (card in cards.map { it.term }) {
            cards.remove(cards.find { it.term == card })
            println(RemoveResponse.statement)
        } else {
            println(RemoveCardResponse.response(card))
        }
    }

    /* read the user's input from the next line, which is the file name, and import all the flashcards
    * written to this file. If there is no file with such name, print the message. After importing
    * the cards, print the message. The imported cards should be added to the ones that already exist in
    * the memory of the program. However, the imported cards have priority: if you import a card with
    * the name that already exists in the memory, the card from the file should overwrite the one in memory. */
    override fun import() {
        val file = if (pathOfImport != null) {
            File(pathOfImport)
        } else {
            println(FileRequest.statement)
            File(readLine())
        }

        if (!file.exists()) {
            println(FileResponse.statement)
        } else {
            val lines = file.readLines()
            for (line in lines) {
                val values = line.split(" : ")
                cards.find { it.term == values[0] }.let { cards.remove(it) }
                cards.add(Card(values[0], values[1], values[2].toInt()))
            }
            println(ImportResponse.response(lines.size.toString()))
        }
    }

    /* write all currently available flashcards into this file. */
    override fun export() {
        val file = if (pathOfExport != null) {
            File(pathOfExport)
        } else {
            println(FileRequest.statement)
            File(readLine())
        }

        if (file.exists()) {
            file.delete()
            file.createNewFile()
        }

        for (card in cards) {
            file.appendText(card.toString() + "\n")
        }
        println(ExportResponse.response(cards.size.toString()))
    }

    /* ask the user about the number of cards they want to be asked about and then prompt them for definitions,
    * like in the previous stage. */
    override fun ask() {
        println(AskRequest.statement)
        val countCards = readLine().toInt()

        repeat(countCards) {
            val index = Random.nextInt(0, cards.size)
            val card = cards[index]

            println(AskDefinitionRequest.response(card.term))

            when (val answer = readLine()) {
                card.definition -> println(Correct.statement)
                in cards.map { it.definition } -> {
                    val targetTerm = cards.first { it.definition == answer }.term
                    println(AskNoBadResponse.response(card.definition, targetTerm))
                    cards[index].mistakes++
                }
                else -> {
                    println(AskFalseResponse.response(card.definition))
                    cards[index].mistakes++
                }
            }
        }
    }

    /* ask the user where to save the log with the message, save all the lines that have been input in/output
    * to the console to the file, and print the message. */
    override fun log() {
        println(FileRequest.statement)
        val file = File(readLine())
        if (file.exists()) {
            file.delete()
            file.createNewFile()
        }

        for (mess in log) {
            file.appendText(mess + "\n")
        }
        println(LogSaved.statement)
    }

    /* print a string that contains the term of the card with the highest number of wrong answers, for example,
    * The hardest card is "term". You have N errors answering it. If there are several cards with the highest
    * number of wrong answers, print all of the terms: The hardest cards are "term_1", "term_2". If there are
    * no cards with errors in the user's answers, print the message There are no cards with errors. */
    override fun hardestCard() {
        if (cards.any { it.mistakes > 0 }) {
            val cardOfMaxError = cards.filter { card -> card.mistakes == cards.maxOf { it.mistakes } }
            val listOfCardTerm = cardOfMaxError.joinToString { it.term }
            println(HardestCardResponse.response(listOfCardTerm, cardOfMaxError[0].mistakes.toString()))
        } else {
            println(NoError.statement)
        }
    }

    /* set the count of mistakes to 0 for all the cards and output the message */
    override fun resetStats() {
        for (card in cards) {
            card.mistakes = 0
        }
        println(ResetStats.statement)
    }

    /* If command-line argument -export EXPORT is passed, where EXPORT is the file name, write all cards
    * that are in the program memory into this file after the user has entered exit */
    override fun exit() {
        if (pathOfExport != null) {
            val file = File(pathOfExport)
            if (file.exists()) {
                file.delete()
                file.createNewFile()
            }

            for (card in cards) {
                file.appendText(card.toString() + "\n")
            }
            println(ExportResponse.response(cards.size.toString()))
        }
        println(Exit.statement)
        exitProcess(1)
    }
}