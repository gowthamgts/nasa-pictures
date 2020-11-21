package dev.gowtham.nasapictures.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dev.gowtham.nasapictures.R
import dev.gowtham.nasapictures.databinding.ActivityMainBinding
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }

        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
