package com.bme.surveysystemsupportedbyai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bme.surveysystemsupportedbyai.navigation.Graph
import com.bme.surveysystemsupportedbyai.navigation.RootNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            RootNavGraph(
                navController=navController,
                AuthState()
            )

        }
    }

    @Composable
    private fun  AuthState() : String {
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
//        if (isUserSignedOut) {
//            NavigateToSignInScreen()
//        } else {
//            /*if (viewModel.isEmailVerified) {
//                //NavigateToProfileScreen()
//            } else {
//                //NavigateToVerifyEmailScreen()
//            }*/
//            NavigateToHomeScreen()
//        }
        if(isUserSignedOut)
            return Graph.AUTH
        else return Graph.MAIN
    }
    @Composable
    private fun NavigateToSignInScreen() = navController.navigate(Graph.AUTH) {
        popUpTo(Graph.MAIN) {
            inclusive = true
        }
    }

    @Composable
    private fun NavigateToHomeScreen() = navController.navigate(Graph.MAIN) {
        popUpTo(Graph.AUTH) {
            inclusive = true
        }
    }

}

