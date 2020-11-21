package dev.gowtham.nasapictures.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dev.gowtham.nasapictures.R
import timber.log.Timber
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: for debugging. remove on release
        Thread.setDefaultUncaughtExceptionHandler { paramThread, exception -> //Catch your exception
            // Without System.exit() this will not work.
            Timber.e(exception, "caught uncaught exception on thread - $paramThread.")
            exitProcess(2)
        }
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
