package com.dreamteam.monopoly

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.monopoly.game.GameManager
import com.dreamteam.monopoly.helpers.makeTinyAlert
import com.dreamteam.monopoly.helpers.showToast
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty
import maes.tech.intentanim.CustomIntent


class GameActivity : AppCompatActivity(), View.OnClickListener {

    private var buttonThrowDices: Button? = null
    private var gameManager: GameManager = GameManager(this)
    private var cellButtons: ArrayList<Button> = ArrayList(gameManager.mainBoard.gameWayLength)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val intent = intent
        val playersNames: ArrayList<String> = intent.getStringArrayListExtra("PlayersNames") //get info from AmountOfPlayers Activity
        startAssignment(playersNames)

        buttonThrowDices = findViewById(R.id.buttonThrowCubes)
        buttonThrowDices!!.setOnClickListener {
            val dices: Pair<Int, Int> = gameManager.getCurrentPlayer().throwDices(this)
            val cube1: ImageView = findViewById(R.id.cube1)
            val cube2: ImageView = findViewById(R.id.cube2)
            val drawCube1 = resources.getDrawable(resources
                    .getIdentifier("dice${dices.first}", "drawable", packageName))      //gettind first cube image
            val drawCube2 = resources.getDrawable(resources
                    .getIdentifier("dice${dices.second}", "drawable", packageName))     //gettind second cube image
            cube1.setImageDrawable(drawCube1)  //draw this pics
            cube2.setImageDrawable(drawCube2)

            buttonThrowDices!!.isEnabled = false    //make button throw dices unusable
            buttonThrowDices!!.visibility = View.INVISIBLE
            val handler = Handler()
            handler.postDelayed({
                cube1.setImageDrawable(null) //delete images dices pics
                cube2.setImageDrawable(null)
                gameManager.nextPlayerMove() //this code should be after action after throwing dice #Player.decision
                buttonThrowDices!!.isEnabled = true
                buttonThrowDices!!.visibility = View.VISIBLE
                Toasty.info(this, gameManager.getCurrentPlayer().name + " move", Toast.LENGTH_SHORT, true).show()
            }, 2000)
            //showToast(v!!, "Piu")
            //makeTinyAlert(this, gameManager.getCurrentPlayer().name + " move")
        }
    }

    @Override
    override fun onClick(v: View) {
        //starting game activity
        intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    @Override
    override fun finish() {
        super.finish()
        Alerter.hide()
        CustomIntent.customType(this, "up-to-bottom")
    }

    fun getGameManager(): GameManager = gameManager

    private fun startAssignment(playersNames: ArrayList<String>) //adding text/players on map
    {
        for (string in playersNames) {
            gameManager.addPlayer(string)
            val playerStatsId = resources.getIdentifier("playerStats${gameManager.players.size}", "id", packageName)
            val playerStats: TextView = findViewById(playerStatsId)
            playerStats.text = string
            val playerMoneyId = resources.getIdentifier("playerMoney${gameManager.players.size}", "id", packageName)
            val playerMoney: TextView = findViewById(playerMoneyId)
            playerMoney.text = gameManager.getPlayerByName(string)!!.money.toString()
        }

        for (i in 1..playersNames.size) {
            val myPlayerId = resources.getIdentifier("Player$i", "id", packageName)
            val playerImage: ImageView = findViewById(myPlayerId)
            playerImage.visibility = View.VISIBLE
        }
    }
}
