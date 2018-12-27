package com.dreamteam.monopoly.game

import android.widget.TextView
import com.dreamteam.monopoly.GameActivity
import com.dreamteam.monopoly.R
import com.dreamteam.monopoly.game.data.GameData
import com.dreamteam.monopoly.game.data.GameData.startMoney
import com.dreamteam.monopoly.game.board.Board
import com.dreamteam.monopoly.game.data.ValuesData
import com.dreamteam.monopoly.game.player.Player
import com.dreamteam.monopoly.game.player.PlayerType

class GameManager(private val activity: GameActivity) {
    var mainBoard: Board = Board(GameData.boardGameCells, activity)
    var players: ArrayList<Player> = ArrayList()
    var suicidePlayers: ArrayList<Int> = ArrayList()
    var currentPlayerIndex: Int = 0
    var actionState: ActionState = ActionState.IDLE
    var currentInfo: Int = 0

    fun startAssignment(playersNames: HashMap<PlayerType, ArrayList<String>>) //adding text/players on map
    {
        if (playersNames[PlayerType.AI] != null) {
            for (string in playersNames[PlayerType.AI]!!) {
                addPlayer(string, PlayerType.AI)
                val playerStatsId = activity.resources.getIdentifier(activity.getString(R.string.playerStats) +
                        players.size.toString(), ValuesData.id, activity.packageName)
                val playerStats: TextView = activity.findViewById(playerStatsId)
                playerStats.text = string
                getPlayerByName(string)!!.setPlayerID(
                        players.indexOf(getPlayerByName(string)!!) + 1)
                activity.updPlayerMoney(getPlayerByName(string)!!)
            }
        }
        if (playersNames[PlayerType.PERSON] != null) {
            for (string in playersNames[PlayerType.PERSON]!!) {
                addPlayer(string, PlayerType.PERSON)
                val playerStatsId = activity.resources.getIdentifier(activity.getString(R.string.playerStats) +
                        players.size.toString(), ValuesData.id, activity.packageName)
                val playerStats: TextView = activity.findViewById(playerStatsId)
                playerStats.text = string
                getPlayerByName(string)!!.setPlayerID(
                        players.indexOf(getPlayerByName(string)!!) + 1)
                activity.updPlayerMoney(getPlayerByName(string)!!)
            }
        }
    }

    fun resetPlayersPositions(savedPositions: ArrayList<Int>) {
        if (savedPositions.size == players.size)
            for (i in 0 until players.size) {
                players[i].currentPosition = savedPositions[i]
                mainBoard.changeImagePlace(players[i])
            }
    }

    fun updateAllPositions() {
        for (p in players)
            mainBoard.changeImagePlace(p)
    }

    fun nextPlayerMove() {
        if (currentPlayerIndex < players.size - 1) currentPlayerIndex++
        else currentPlayerIndex = 0
    }

    fun getCurrentPlayer(): Player = players[currentPlayerIndex]

    fun getPlayerById(id: Int): Player {
        for (p in players)
            if (p.id == id)
                return p
        return getCurrentPlayer()
    }

    fun addPlayer(name: String) {
        if (checkExistingPlayer(name))
            players.add(Player(name, startMoney, PlayerType.PERSON, mainBoard))
    }

    fun addPlayers(playersData: List<String>) {
        for (name: String in playersData)
            if (checkExistingPlayer(name))
                players.add(Player(name, startMoney, PlayerType.PERSON, mainBoard))
    }

    fun addPlayer(name: String, type: PlayerType) {
        if (checkExistingPlayer(name))
            players.add(Player(name, startMoney, type, mainBoard))
    }

    fun addPlayer(name: String, startMoney: Int, type: PlayerType) {
        if (checkExistingPlayer(name))
            players.add(Player(name, startMoney, type, mainBoard))
    }

    private fun checkExistingPlayer(name: String): Boolean {
        for (p in players)
            if (p.name == name) return false
        return true
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        checkEndGame()
    }

    fun removePlayer(name: String) {
        players.remove(players.find { p -> p.name == name })
        checkEndGame()
    }

    fun removePlayer(index: Int) {
        players.removeAt(index)
        checkEndGame()
    }

    private fun checkEndGame() {
        if (players.size == 1) {
            activity.endGameAction(players[0])
        }
    }

    fun getPlayerByName(name: String): Player? {
        return players.find { p -> p.name == name }
    }

    enum class ActionState(val state: Int) {
        IDLE(0), BUY(1),
        SELL(2), BUY_SELL(3)
    }
}