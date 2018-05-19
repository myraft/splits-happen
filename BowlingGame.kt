class BowlingGame(private val parser: LineParser) {

    fun computeScore(line: String): Int {
        val game = parser.parse(line)
        return game.score()
    }
}
