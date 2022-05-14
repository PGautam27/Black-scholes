package com.example.blackscholes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blackscholes.ui.theme.BlackScholesTheme
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlackScholesTheme {
                val viewModel: AhoCorasick = AhoCorasick()
                val list by viewModel.list.observeAsState()
                val textValue = remember {
                    mutableStateOf("")
                }
                val listValue = remember {
                    mutableStateOf("")
                }
                val listValue1 = remember {
                    mutableListOf<String>()
                }
                val boolValue = remember {
                    mutableStateOf(false)
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Input your String")
                    Spacer(modifier = Modifier.padding(LocalConfiguration.current.screenHeightDp.dp / 45))
                    OutlinedTextField(
                        value = textValue.value,
                        onValueChange = { textValue.value = it })
                    Spacer(modifier = Modifier.padding(LocalConfiguration.current.screenHeightDp.dp / 30))
                    Text(text = "Input your required set of strings")
                    Spacer(modifier = Modifier.padding(LocalConfiguration.current.screenHeightDp.dp / 45))
                    OutlinedTextField(
                        value = listValue.value,
                        onValueChange = { listValue.value = it })
                    Spacer(modifier = Modifier.padding(LocalConfiguration.current.screenHeightDp.dp / 45))
                    Button(onClick = {
                        val li = listValue.value.split(" ".toRegex())
                        li.forEachIndexed { i, s ->
                            listValue1.add(i, s)
                        }
                        viewModel.inputValues(textValue.value, listValue1)
                    }) {
                        Text(text = "Click Me to view the words")
                    }
                    FlowRow(
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp),
                        mainAxisSpacing = 20.dp,
                        crossAxisAlignment = FlowCrossAxisAlignment.Center,
                        mainAxisAlignment = MainAxisAlignment.Center,
                        crossAxisSpacing = 5.dp,

                        ) {
                        if (list?.get(0)==""){
                            return@FlowRow
                        }
                        list?.forEach { it ->
                            text(it = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun text(it:String) {
    Text(text = it)
}