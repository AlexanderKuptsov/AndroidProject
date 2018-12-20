package com.dreamteam.monopoly

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dreamteam.monopoly.helpers.showToast
import es.dmoral.toasty.Toasty
import maes.tech.intentanim.CustomIntent.customType


class MainActivity : AppCompatActivity(), View.OnClickListener {

    //image button
    private var buttonPlay: Button? = null
    private var buttonExit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideTopBar()

        //getting the button
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonExit = findViewById(R.id.buttonExit)

        //adding a click listener
        buttonPlay!!.setOnClickListener(this)

        buttonExit!!.setOnClickListener {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(buttonExit)
            finish()
            System.exit(0)
        }

    }

    override fun onResume() {
        super.onResume()
        hideTopBar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideTopBar()
    }

    private fun hideTopBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    @Override
    override fun onClick(v: View) {
        //starting game activity
        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(buttonPlay)
        //showToast(v, resources.getString(R.string.startGameToast))
        Toasty.normal(this, resources.getString(R.string.startGameToast),
                Toast.LENGTH_SHORT, resources.getDrawable(R.drawable.dice6)).show()
        intent = Intent(this, AmountOfPlayersActivity::class.java)
        startActivity(intent)
        customType(this, "bottom-to-up")
    }
}
