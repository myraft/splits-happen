import java.lang.Character.isDigit
import java.lang.Character.getNumericValue

class LineParser {

    fun parse(line: String): Game {
        val lineWithoutHyphens = line.replace("-", "0")
        return parse(lineWithoutHyphens, 1)
    }


    private fun parse(frames: String, frame: Int): Game {
        val game = Game()
        parse(frames, game, frame)
        return game
    }

    private fun parse(frames: String, game: Game, frame: Int) {

        if (frames.isEmpty()) {
            return
        }

        val firstRoll = frames.charAt(0)

        if (isStrike(firstRoll)) {
            if (isExtra(frame)) {
                game.addExtra(10)
                parse(frames.substring(1), game, frame + 1)
            } else {
                game.addStrike()
                parse(frames.substring(1), game, frame + 1)
            }
        } else if (isDigit(firstRoll)) {
            val _1 = getNumericValue(firstRoll)

            if (isExtra(frame)) {
                game.addExtra(_1)
                parse(frames.substring(1), game, frame + 1)
            } else {
                val secondRoll = frames.charAt(1)

                if (isSpare(secondRoll)) {
                    game.addSpare(_1)
                    parse(frames.substring(2), game, frame + 1)
                } else {
                    val _2 = getNumericValue(secondRoll)
                    game.addFrame(_1, _2)
                    parse(frames.substring(2), game, frame + 1)
                }
            }
        } else {
            throw IllegalStateException(frames)
        }
    }

    private fun isStrike(roll: Char): Boolean {
        return roll == 'X'
    }

    private fun isExtra(frame: Int): Boolean {
        return frame > 10
    }

    private fun isSpare(roll: Char): Boolean {
        return roll == '/'
    }
}
