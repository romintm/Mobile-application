package com.example.teamwork_management.component.signin

import android.graphics.drawable.shapes.Shape
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teamwork_management.R

@Composable
fun SignInScreen(
    state: SignInState,
    loading: Boolean,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0296CD))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            Text(text = "Teamwork", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 60.sp)
            Text(text = "Management", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 60.sp)

            Spacer(modifier = Modifier.weight(0.1f)) // This spacer pushes the content to the bottom

            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp), // Add padding for left and right margin
                contentAlignment = Alignment.Center // Center the text horizontally
            ) {
                Text(
                    text = "Seems you are not logged in, use Google to sign in and start using our app!",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center // Center the text within the Box
                )
            }


            Spacer(modifier = Modifier.weight(0.5f))

            if(!loading){
                Button(
                    onClick = onSignInClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp) // Padding to avoid text/icon cutoff
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google Button",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Sign in with Google", fontWeight = FontWeight.Bold)
                    }
                }
            }else{
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = Color.White,
                    trackColor = Color.Black,
                )
            }

            Spacer(modifier = Modifier.weight(0.2f))
        }

    }
}