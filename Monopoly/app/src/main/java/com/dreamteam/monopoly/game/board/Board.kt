package com.dreamteam.monopoly.game.board

import android.app.Activity
import android.util.Log
import com.dreamteam.monopoly.game.GameData
import com.dreamteam.monopoly.game.board.cell.Cell
import com.dreamteam.monopoly.game.board.cell.GameCell
import com.dreamteam.monopoly.game.player.Player
import java.util.ArrayList
import android.support.constraint.ConstraintSet
import com.dreamteam.monopoly.R
import android.support.constraint.ConstraintLayout
import com.dreamteam.monopoly.GameActivity
import com.dreamteam.monopoly.game.GameManager


class Board(var gameWay: ArrayList<GameCell>) {

    private val gameWayLength: Int = gameWay.size

    fun movePlayer(newPositionIndex: Int, player: Player , activity: Activity): Cell {
        player.currentPosition = newPositionIndex
        while (player.currentPosition > gameWayLength - 1) {
            player.currentPosition -=  gameWayLength
            loopPassEvents(player)
        }
        changeImagePlace(player,activity)
        return gameWay[player.currentPosition]
    }

    private fun loopPassEvents(player: Player){
        player.earnMoney(GameData.loopMoney)
    }

    private  fun changeImagePlace(player: Player , activity: Activity)
    {
        var gameAct:GameActivity = activity as GameActivity // return current player's cell
        val currentPlayerIndex = gameAct.getGameManager().currentPlayerIndex;
        val constraintSet = ConstraintSet()
        val constraintLayout : ConstraintLayout = activity.findViewById(R.id.ConstraintLayout)
        constraintSet.clone(constraintLayout)
        val myId = activity.getResources().getIdentifier("button${player.currentPosition}", "id", activity.packageName)
        val myPlayer = activity.getResources().getIdentifier("Player${currentPlayerIndex+1}", "id", activity.packageName)
        if (currentPlayerIndex == 0 || currentPlayerIndex == 2)
        {
            constraintSet.connect(myPlayer, ConstraintSet.RIGHT, myId, ConstraintSet.RIGHT, 0)
            constraintSet.connect(myPlayer, ConstraintSet.LEFT, myId, ConstraintSet.LEFT, 0)
            if (currentPlayerIndex == 0) constraintSet.connect(myPlayer, ConstraintSet.TOP, myId, ConstraintSet.TOP, 0)
            else constraintSet.connect(myPlayer, ConstraintSet.BOTTOM, myId, ConstraintSet.BOTTOM, 0)
        }
        if (currentPlayerIndex == 1 || currentPlayerIndex == 3)
        {
            constraintSet.connect(myPlayer, ConstraintSet.TOP, myId, ConstraintSet.TOP, 0)
            constraintSet.connect(myPlayer, ConstraintSet.BOTTOM, myId, ConstraintSet.BOTTOM, 0)
            if (currentPlayerIndex == 1) constraintSet.connect(myPlayer, ConstraintSet.RIGHT, myId, ConstraintSet.RIGHT, 0)
            else constraintSet.connect(myPlayer, ConstraintSet.LEFT, myId, ConstraintSet.LEFT, 0)
        }
        constraintSet.applyTo(constraintLayout)
    }
}