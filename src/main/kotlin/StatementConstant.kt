
enum class StatementConstant(val statement: String) {
    InitialRequest("Input the action (add, remove, import, export, " +
            "ask, exit, log, hardest card, reset stats):"),
    ActionRequest("add, remove, import, export, ask, exit, log, hardest card, reset stats"),
    ShitResponse("Oh, shit! Input the correct action!"),
    TermRequest("The card:"),
    DefinitionRequest("The definition of the card:"),
    RemoveRequest("Which card?"),
    RemoveResponse("The card has been removed."),
    FileRequest("File name:"),
    FileResponse("File not found."),
    AskRequest("How many times to ask?"),
    Correct("Correct!"),
    LogSaved("The log has been saved."),
    NoError("There are no cards with errors."),
    ResetStats("Card statistics have been reset."),
    Exit("Bye bye!");
}