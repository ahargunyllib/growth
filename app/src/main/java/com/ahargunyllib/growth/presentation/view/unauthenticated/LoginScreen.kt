package com.ahargunyllib.growth.presentation.view.unauthenticated


import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj


@Composable
fun LoginScreen(
    unauthenticatedNavController: NavController,
    rootNavController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    var passwordVisible by remember {mutableStateOf(false)}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Primary.color)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(160.dp),
            modifier = Modifier
                .fillMaxSize()
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { unauthenticatedNavController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painterResource(id = R.drawable.growth),
                    contentDescription = "Growth Logo",
                    modifier = Modifier
                        .width(120.dp)
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(
                        color = GrowthScheme.White.color,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )


            ){
                Column (
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp)
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()) {

                        Text(
                            "Selamat Datang",
                            style = GrowthTypography.HeadingL.textStyle.copy(fontWeight = FontWeight.Bold),
                        )
                        Text(
                            "Silahkan Login",
                            style = GrowthTypography.BodyM.textStyle,
                        )
                    }
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()) {

                        Text ("Email Address",
                            style = GrowthTypography.LabelL.textStyle
                        )
                        TextField(

                            value = email,
                            onValueChange = { email = it},
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = GrowthScheme.Disabled.color,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            placeholder = {Text("Email Address" , style = GrowthTypography.LabelL.textStyle , color = GrowthScheme.Disabled.color)},
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            textStyle = GrowthTypography.LabelL.textStyle,
                            colors = TextFieldDefaults.colors( // Kustomisasi warna
                                focusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah saat aktif
                                unfocusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah saat tidak aktif
                                disabledIndicatorColor = Color.Transparent, // Hilangkan garis bawah saat nonaktif
                                focusedContainerColor = GrowthScheme.White.color,
                                unfocusedContainerColor = GrowthScheme.White.color
                            )
                        )
                    }



                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()) {

                        Text ("Password",
                            style = GrowthTypography.LabelL.textStyle
                        )

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = GrowthScheme.Disabled.color,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            placeholder = { Text("Masukkan password Anda" , style = GrowthTypography.LabelL.textStyle, color = GrowthScheme.Disabled.color)  },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            textStyle = GrowthTypography.LabelL.textStyle,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedContainerColor = GrowthScheme.White.color,
                                unfocusedContainerColor = GrowthScheme.White.color
                            ),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, description)
                                }
                            }
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Forgot Password?",
                                style = GrowthTypography.LabelL.textStyle,
                                color = GrowthScheme.Disabled.color,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            )
                        }




                    }
                    Button(
                        onClick = {
                            rootNavController.navigate(RootNavObj.Authenticated.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrowthScheme.Fourth.color)
                    ) {
                        Text(
                            text = "Masuk",
                            style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                            color = GrowthScheme.White.color,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = GrowthScheme.Disabled.color,
                            thickness = 1.dp
                        )

                        Text(
                            text = "or",
                            style = GrowthTypography.BodyM.textStyle,
                            color = GrowthScheme.Disabled.color
                        )

                        Divider(
                            modifier = Modifier.weight(1f),
                            color = GrowthScheme.Disabled.color,
                            thickness = 1.dp
                        )
                    }

                    Button(
                        onClick = { //TODO Masuk Dengan Google
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GrowthScheme.Disabled.color, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrowthScheme.White.color)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google Icon",
                                tint = Color.Unspecified

                            )

                            Text(
                                text = "Masuk dengan Google",
                                style = GrowthTypography.BodyL.textStyle,
                                color = GrowthScheme.Black.color,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Belum punya akun? ",
                            style = GrowthTypography.BodyM.textStyle,
                            color = GrowthScheme.Black2.color
                        )
                        Text(
                            text = "Daftar di sini",
                            style = GrowthTypography.BodyM.textStyle.copy(fontWeight = FontWeight.Bold),
                            color = GrowthScheme.Primary.color,
                            modifier = Modifier.clickable {
                                unauthenticatedNavController.navigate(UnauthenticatedNavObj.Register.route)
                            }
                        )
                    }


                }

            }


        }
    }
}