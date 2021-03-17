enum class StatementVariable {
    TermResponse {
        override fun response(arg1: String, arg2: String): String {
            return "The card \"$arg1\" already exists."
        }
    },
    DefinitionResponse {
        override fun response(arg1: String, arg2: String): String {
            return "The definition \"$arg1\" already exists."
        }
    },
    AddResponse {
        override fun response(arg1: String, arg2: String): String {
            return "The pair (\"$arg1\":\"$arg2\") has been added."
        }
    },
    RemoveCardResponse {
        override fun response(arg1: String, arg2: String): String {
            return "Can't remove \"$arg1\": there is no such card."
        }
    },
    ImportResponse {
        override fun response(arg1: String, arg2: String): String {
            return "$arg1 cards have been loaded."
        }
    },
    ExportResponse {
        override fun response(arg1: String, arg2: String): String {
            return "$arg1 cards have been saved."
        }

    },
    AskDefinitionRequest {
        override fun response(arg1: String, arg2: String): String {
            return "Print the definition of \"$arg1\":"
        }
    },
    AskNoBadResponse {
        override fun response(arg1: String, arg2: String): String {
            return "Wrong. The right answer is \"$arg1\", but your definition is correct for \"$arg2\"."
        }
    },
    AskFalseResponse {
        override fun response(arg1: String, arg2: String): String {
            return "Wrong. The right answer is \"$arg1\"."
        }
    },
    HardestCardResponse {
        override fun response(arg1: String, arg2: String): String {
            val listOfCardTerm = arg1.split(", ")
            return if (listOfCardTerm.size > 1) {
                var response = "The hardest cards are "
                for (i in 0.. listOfCardTerm.size - 2) {
                    response += "\"${listOfCardTerm[i]}\", "
                }
                response += "\"${listOfCardTerm.last()}\". You have $arg2 errors answering them"

                response
            } else {
                "The hardest card is \"${listOfCardTerm[0]}\". You have $arg2 errors answering it."
            }
        }
    };

    abstract fun response(arg1: String, arg2: String = ""): String
}