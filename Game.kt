import java.util.LinkedList

// Visitor
class Game {

    private val frames = LinkedList()

    fun score(): Int {
        return frames.stream().mapToInt({ frame -> frame.score(this) }).sum()
    }

    fun addFrame(firstTry: Int, secondTry: Int) {
        val frame = Frame(firstTry, secondTry)
        add(frame)
    }

    fun addSpare(firstTry: Int) {
        val spare = Spare(firstTry)
        add(spare)
    }

    fun addStrike() {
        val strike = Strike()
        add(strike)
    }

    fun addExtra(roll: Int) {
        val extra = Extra(roll)
        add(extra)
    }

    private fun computeScoreForFrame(current: Frame): Int {
        return current.score()
    }

    private fun computeScoreForSpare(current: Spare): Int {
        val next = frameAfter(current)
        return current.score() + next.roll(1, this)
    }

    private fun computeScoreForStrike(current: Strike): Int {
        val next = frameAfter(current)
        return current.score() + next.roll(1, this) + next.roll(2, this)
    }

    private fun computeScoreForExtra(current: Extra): Int {
        return current.score()
    }

    private fun frameAfter(frame: Frame): Frame {
        val frameIndex = frames.indexOf(frame)

        if (frameIndex == -1) {
            throw IllegalArgumentException("Frame $frame does not exist.")
        }

        try {
            return frames.get(frameIndex + 1)
        } catch (exception: IndexOutOfBoundsException) {
            return EmptyFrame()
        }

    }

    private fun add(frame: Frame) {
        frames.add(frame)
    }

    // Elements
    internal class Frame(private val firstTry: Int, private val secondTry: Int) {

        fun firstTry(): Int {
            return firstTry
        }

        fun secondTry(): Int {
            return secondTry
        }

        fun score(): Int {
            return firstTry() + secondTry()
        }

        fun score(game: Game): Int {
            return game.computeScoreForFrame(this)
        }

        fun roll(roll: Int, game: Game): Int {
            if (roll == 1) {
                return firstTry()
            }

            return if (roll == 2) {
                secondTry()
            } else nextFrame(game).roll(roll - 2, game)

        }

        protected fun nextFrame(game: Game): Frame {
            return game.frameAfter(this)
        }
    }

    internal class Spare(_1: Int) : Frame(_1, 10 - _1) {

        @Override
        override fun score(game: Game): Int {
            return game.computeScoreForSpare(this)
        }

        @Override
        override fun nextFrame(game: Game): Frame {
            return game.frameAfter(this)
        }
    }

    internal class Strike : Frame(10, 0) {

        @Override
        override fun score(): Int {
            return 10
        }

        @Override
        override fun score(game: Game): Int {
            return game.computeScoreForStrike(this)
        }

        @Override
        override fun roll(roll: Int, game: Game): Int {
            return if (roll == 1) {
                10
            } else nextFrame(game).roll(roll - 1, game)

        }

        @Override
        override fun nextFrame(game: Game): Frame {
            return game.frameAfter(this)
        }

        @Override
        override fun secondTry(): Int {
            throw IllegalStateException("There isn't a second try for Strikes.")
        }
    }

    internal class Extra(_1: Int) : Frame(_1, 0) {

        @Override
        override fun score(): Int {
            return 0
        }

        @Override
        override fun score(game: Game): Int {
            return game.computeScoreForExtra(this)
        }

        @Override
        override fun roll(roll: Int, game: Game): Int {
            return if (roll == 1) {
                firstTry()
            } else nextFrame(game).roll(roll - 1, game)

        }

        @Override
        override fun nextFrame(game: Game): Frame {
            return game.frameAfter(this)
        }

        @Override
        override fun secondTry(): Int {
            throw IllegalStateException("There isn't a second try for Extra frames.")
        }
    }

    // Null object
    internal inner class EmptyFrame : Frame(0, 0) {

        @Override
        override fun nextFrame(game: Game): Frame {
            return this
        }

    }
}

