package com.example.texteditorteam

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.texteditorteam.ui.theme.TextEditorTEAMTheme
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private val textState = mutableStateOf("")

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                readTextFromUri(uri)
            }
        }
    }

    private val saveFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                writeTextToUri(uri, textState.value)
            }
        }
    }

    private fun logLifecycle(method: String) {
        Log.d("LifecycleLogger:$method", "called at ${sdf.format(Date())}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
        setContent {
            TextEditorTEAMTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Text Editor") },
                            actions = {
                                IconButton(onClick = { openFile() }) {
                                    Icon(Icons.Default.Add, contentDescription = "Open File")
                                }
                                IconButton(onClick = { saveFile() }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Save File")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    TextEditor(modifier = Modifier.padding(innerPadding), textState = textState)
                }
            }
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        openFileLauncher.launch(intent)
    }

    private fun saveFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "new_file.txt")
        }
        saveFileLauncher.launch(intent)
    }

    private fun readTextFromUri(uri: Uri) {
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use {
            BufferedReader(InputStreamReader(it)).use {
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append('\n')
                }
            }
        }
        textState.value = stringBuilder.toString()
    }

    private fun writeTextToUri(uri: Uri, text: String) {
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use {
                    it.write(text.toByteArray())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifecycle("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifecycle("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        logLifecycle("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifecycle("onDestroy")
    }
}

@Composable
fun TextEditor(modifier: Modifier = Modifier, textState: MutableState<String>) {
    Column(modifier = modifier) {
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextEditorPreview() {
    TextEditorTEAMTheme {
        TextEditor(textState = remember { mutableStateOf("Hello!") })
    }
}
