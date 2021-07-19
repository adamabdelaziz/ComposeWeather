package com.example.composeweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ComposeWeatherTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ComposeWeatherTheme {
//        Greeting("Android")
//    }
//}
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private val viewModel: WeatherViewModel by viewModels()
//
//    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
//    private lateinit var geocoder: Geocoder


//    @ExperimentalPermissionsApi
//    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



//
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        setContent {
//            ComposeWeatherTheme {
//
//                val navController = rememberNavController()
//
//                NavHost(navController, startDestination = NavigationScreen.SCREEN_WEATHER.name) {
//                    composable(NavigationScreen.SCREEN_WEATHER.name) {
//                        //Can Wrap animation here if I had one
//                        WeatherScreen(navController, viewModel, fusedLocationClient)
//
//                    }
//                    composable(NavigationScreen.SCREEN_SETTINGS.name) {
//                        SettingsScreen(navController)
//                    }
//                }

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        // IDs from nav menu xml go here
//        val appBarConfiguration = AppBarConfiguration(setOf())
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        supportActionBar?.hide()

            }
        }
