data class Card(val term: String, val definition: String, var mistakes: Int = 0) {

    override fun toString(): String {
        return "$term : $definition : $mistakes"
    }
}
