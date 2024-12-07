package com.automacorp

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.automacorp.RoomListActivity
import com.automacorp.ui.theme.AutomacorpTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AutomacorpTopAppBar(title: String? = null, returnAction: () -> Unit = {}) {
    val context = LocalContext.current // Get the current context

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    // Define the actions displayed on the right side of the app bar
    val actions: @Composable RowScope.() -> Unit = {
        IconButton(onClick = {
            // Navigate to RoomListActivity
            context.startActivity(Intent(context, RoomListActivity::class.java))
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_rooms), // Replace with your actual drawable resource
                contentDescription = stringResource(R.string.app_go_room_description)
            )
        }
        IconButton(onClick = {
            // Send email action
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:guillaume@dev-mind.fr") // Replace with your email address
                putExtra(Intent.EXTRA_SUBJECT, "Subject here")
                putExtra(Intent.EXTRA_TEXT, "Body here")
            }
            if (emailIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(emailIntent)
            } else {
                Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_mail), // Replace with your actual drawable resource
                contentDescription = stringResource(R.string.app_go_mail_description)
            )
        }
        IconButton(onClick = {
            // Open GitHub page action
            val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yourusername")) // Replace with your GitHub URL
            context.startActivity(githubIntent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_github), // Replace with your actual drawable resource
                contentDescription = stringResource(R.string.app_go_github_description)
            )
        }
        IconButton(onClick = {
            // Open web page action
            val webPageIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dev-mind.fr")) // Replace with your desired URL
            context.startActivity(webPageIntent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_web), // Replace with your actual drawable resource for web icon
                contentDescription = "Open Web Page"
            )
        }
    }

    if (title == null) {
        TopAppBar(
            title = { Text("") },
            colors = colors,
            actions = actions
        )
    } else {
        MediumTopAppBar(
            title = { Text(title) },
            colors = colors,
            navigationIcon = {
                IconButton(onClick = returnAction) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.app_go_back_description)
                    )
                }
            },
            actions = actions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AutomacorpTopAppBarHomePreview() {
    AutomacorpTheme {
        AutomacorpTopAppBar(null)
    }
}

@Preview(showBackground = true)
@Composable
fun AutomacorpTopAppBarPreview() {
    AutomacorpTheme {
        AutomacorpTopAppBar("A page")
    }
}