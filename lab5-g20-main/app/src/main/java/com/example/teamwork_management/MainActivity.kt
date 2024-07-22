package com.example.teamwork_management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.teamwork_management.component.signin.GoogleAuthUiClient
import com.example.teamwork_management.component.signin.SignInScreen
import com.example.teamwork_management.component.signin.SignInViewModel
import com.example.teamwork_management.component.team.CreateTeamScreen
import com.example.teamwork_management.component.team.Navbar
import com.example.teamwork_management.component.team.PresentJoinTeam
import com.example.teamwork_management.component.team.ShowTeamDetailsPage
import com.example.teamwork_management.component.team.TabListTeam
import com.example.teamwork_management.component.team.TeamDetails
import com.example.teamwork_management.component.team.TeamMemberProfile
import com.example.teamwork_management.component.userProfile.PresentEditView
import com.example.teamwork_management.component.userProfile.PresentProfileView
import com.example.teamwork_management.models.ChatListViewModel
import com.example.teamwork_management.models.mainModel
import com.example.teamwork_management.ui.theme.TeamworkmanagementTheme
import com.example.teamwork_management.viewModels.TeamViewModel
import com.example.teamwork_management.viewModels.UserViewModel
import com.example.teamwork_management.viewModels.mainViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    var vm: mainViewModel = mainViewModel(mainModel(this))
    var atvm : TeamViewModel = TeamViewModel()
    var clvm: ChatListViewModel   = ChatListViewModel()
    var uvm : UserViewModel = UserViewModel
    var inviteTeamId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamworkmanagementTheme {
                val navController = rememberNavController()
//                val users by vm.getUsers().collectAsState(initial = listOf())
//                val tasks by vm.getTasks().collectAsState(initial = listOf())
//                val test by vm.getTest().collectAsState(initial = listOf())

                //Used to hide the navbar for non authenticated users
                var showNavbar by remember { mutableStateOf(true) }

                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        showNavbar = backStackEntry.destination.route != "sign_in"
                    }
                }
                Scaffold (
                    topBar = {
                        if (showNavbar) {
                            Navbar(
                                vm = vm,
                                leftButton = vm.leftButton.value,
                                rightButton = vm.rightButton.value,
                            )
                        }
                    }
                ) {
                    it ->
                    Column (
                        modifier = Modifier
                            .padding(it)
                    ) {

                        Row {
                            NavHost(navController = navController, startDestination = "sign_in" ){

//                                composable("invitePage",
//                                    deepLinks = listOf(
//                                        navDeepLink {
//                                            uriPattern = "https://www.teamworkmanagement.com/{myteam}"
//                                            action = Intent.ACTION_VIEW
//                                        }
//                                    ),
//                                    arguments = listOf(
//                                        navArgument("myteam") {
//                                            type = NavType.IntType
//                                            defaultValue = -1
//                                        }
//                                    )
//                                ){entry ->
//                                    val teamId = entry.arguments?.getInt("myteam")
//                                    if(teamId != null){
//                                        PresentJoinTeam(LocalContext.current.applicationContext, navController, vm, atvm, teamId)
//                                    }else{
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "Invalid link",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                        navController.navigate("sign_in")
//                                    }
//
//                                }

                                composable("invitePage"){entry ->
                                    if(inviteTeamId != -1){
                                        PresentJoinTeam(LocalContext.current.applicationContext, navController, vm, atvm, inviteTeamId)
                                    }else{
                                        Toast.makeText(
                                            applicationContext,
                                            "Invalid link",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("home")
                                    }

                                }

                                composable("sign_in",
                                    deepLinks = listOf(
                                        navDeepLink {
                                            uriPattern = "https://www.teamworkmanagement.com/{myteam}"
                                            action = Intent.ACTION_VIEW
                                        }
                                    ),
                                    arguments = listOf(
                                        navArgument("myteam") {
                                            type = NavType.IntType
                                            defaultValue = -1
                                        }
                                    )
                                ) { entry ->
                                    val teamId = entry.arguments?.getInt("myteam")
                                    val signInViewModel = viewModel<SignInViewModel>()
                                    val state by signInViewModel.state.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
                                    val loading by signInViewModel.loading.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)

                                    LaunchedEffect(key1 = Unit) {
                                        if(googleAuthUiClient.getSignedInUser() != null) {
//<<<<<<< HEAD
                                            navController.navigate("home")
                                            vm.setRightButton(
                                                "Profile",
                                                Icons.Default.AccountCircle,
                                                content = "Profile"
                                            ) {
                                                navController.navigate("profile_screen")
                                                vm.setRightButton("Edit", Icons.Default.Edit, content = "Edit profile") {
                                                    navController.navigate("edit_profile_screen")
                                                }
                                                vm.setTitle("Profile")
                                            }
                                            vm.setTitle("Teams")
//                                            vm.setLeftButton("Home", Icons.Default.Home, content = "Home") {}
                                            vm.setLeftButton("", null, ""){}
//=======
                                            if(teamId != -1){
                                                inviteTeamId = teamId!!
                                                navController.navigate("invitePage")
                                            }else{
                                                navController.navigate("home")
                                            }

//>>>>>>> 29a1e05c21264a5e3b381f1ac3d9b354be341c04
                                            signInViewModel.resetState()
                                        }
                                    }

                                    val launcher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                                        onResult = { result ->
                                            println("Checking result code: ${result.resultCode}, $RESULT_OK (RESULT_OK), $RESULT_CANCELED (RESULT_CANCELLED), $RESULT_FIRST_USER (RESULT_FIRST_USER")
                                            if(result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {
                                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )
                                                    signInViewModel.onSignInResult(signInResult)
                                                }
                                            }else{
                                                signInViewModel.setLoading(false)
                                                println("Sign-in was canceled with resultCode: ${result.resultCode}")
                                            }
                                        }
                                    )

                                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                                        if(state.isSignInSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Sign in successful",
                                                Toast.LENGTH_LONG
                                            ).show()
//<<<<<<< HEAD
                                            navController.navigate("home")
                                            vm.setRightButton(
                                                "Profile",
                                                Icons.Default.AccountCircle,
                                                content = "Profile"
                                            ) {
                                                navController.navigate("profile_screen")
                                                vm.setRightButton("Edit", Icons.Default.Edit, content = "Edit profile") {
                                                    navController.navigate("edit_profile_screen")
                                                }

                                                vm.setTitle("Profile")
                                            }
                                            vm.setTitle("Teams")
//                                            vm.setLeftButton("Home", Icons.Default.Home, content = "Home") {}
                                            vm.setLeftButton("", null, ""){}
//=======

                                            if(teamId != -1){
                                                inviteTeamId = teamId!!
                                                navController.navigate("invitePage")
                                            }else{
                                                navController.navigate("home")
                                            }
//>>>>>>> 29a1e05c21264a5e3b381f1ac3d9b354be341c04
                                            signInViewModel.resetState()
                                        }
                                    }

                                    SignInScreen(
                                        state = state,
                                        loading = loading,
                                        onSignInClick = {
                                            signInViewModel.setLoading(true)
                                            lifecycleScope.launch {
                                                val signInIntentSender = googleAuthUiClient.signIn()
                                                if (signInIntentSender != null) {
                                                    launcher.launch(
                                                        IntentSenderRequest.Builder(signInIntentSender).build()
                                                    )
                                                } else {
                                                    // Handle null result from signIn function
                                                    // For example, show a message to the user indicating that sign-in is not available
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign-in is not available at the moment, possible connection error",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    signInViewModel.setLoading(false)
                                                }
                                            }
                                        }
                                    )
                                }

                                composable("profile_screen"){
                                    PresentProfileView(context= LocalContext.current.applicationContext, navController = navController, mainvm = vm, uvm = UserViewModel, onSignOutClick = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            Toast.makeText(
                                                applicationContext,
                                                "Signed out",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            navController.navigate("sign_in")
                                        }
                                    })
                                }

                                composable("edit_profile_screen"){
                                    PresentEditView(context = LocalContext.current.applicationContext, navController = navController, mainvm = vm, uvm = UserViewModel)
                                }

                                composable("home") {
                                    TabListTeam(vm = atvm, navController = navController, mainvm = vm)
                                }

                                composable("team") {
                                    ShowTeamDetailsPage(mainPageNavController = navController, atvm = atvm, mainvm = vm, clvm = clvm, uvm = UserViewModel, context = LocalContext.current.applicationContext)
                                }

                                composable("memberProfile"){
                                    TeamMemberProfile(atvm, vm, navController = navController)
                                }
                                composable("create_team_screen"){
                                    CreateTeamScreen(vm = atvm, navController = navController, vm)
                                }
                                composable("create_team_screen"){
                                    CreateTeamScreen(vm = atvm, navController = navController, vm)
                                }
//                                composable("messages_list_screen"){
//                                    ChatListScreen(navController = navController, vm = clvm, mainPageNavController = navController)
//                                }
//                                composable("chat/{chatId}") { backStackEntry ->
//                                    val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
//                                    val chatListViewModel: ChatListViewModel = viewModel()
//                                    val chatViewModel: ChatViewModel = viewModel()
//                                    ChatScreen(chatId = chatId, clvm = chatListViewModel, vm = chatViewModel)
//                                }

                            }
                        }
                    }
                }
            }
        }
    }

}
