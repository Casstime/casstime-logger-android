package com.casstime.ec.logger.exemple

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.casstime.ec.logger.LogUtil
import com.casstime.ec.logger.LoggerConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val config = LoggerConfig()

        val dir = applicationContext.getExternalFilesDir("log")
        dir?.apply {
            if (!this.exists()){
                this.mkdir()
            }
        }

        config.setCachePath(dir?.absolutePath)
            .setConsoleOpen(true)
            .setCachePath(applicationContext.filesDir.absolutePath)
            .setNamePrefix(BuildConfig.BUILD_TYPE)

        LogUtil.initLogger(config)

        LogUtil.v("Log test")
        LogUtil.d("Log test")
        LogUtil.i("Log test")
        LogUtil.w("Log test")
        LogUtil.e("Log test")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
