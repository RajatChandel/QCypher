package `in`.rchandel.qcypher.ui.screens.result

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.rchandel.qcypher.R
import `in`.rchandel.qcypher.core.uicomponents.ContactCard
import `in`.rchandel.qcypher.core.uicomponents.EmailCard
import `in`.rchandel.qcypher.core.uicomponents.PhoneCard
import `in`.rchandel.qcypher.core.uicomponents.TextCard
import `in`.rchandel.qcypher.core.uicomponents.UpiCard
import `in`.rchandel.qcypher.core.uicomponents.UrlCard
import `in`.rchandel.qcypher.core.uicomponents.WifiCard
import `in`.rchandel.qcypher.data.model.ContactInfo
import `in`.rchandel.qcypher.data.model.ParsedEmail
import `in`.rchandel.qcypher.data.model.ParsedQRResult
import `in`.rchandel.qcypher.data.model.QRResult
import `in`.rchandel.qcypher.data.model.QRType
import `in`.rchandel.qcypher.data.model.UPIInfo
import `in`.rchandel.qcypher.data.model.WifiInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavController,
    qrResult: QRResult,
    viewModel: ScanResultViewModel = hiltViewModel()
) {
    val parsedResult by viewModel.parsedResult.collectAsState()
    val isParsed by viewModel.isParsed.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = qrResult) {
        if(!isParsed) {
            viewModel.parseQRResult(qrResult)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scan Result") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { contentPadding ->
        parsedResult?.let {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Scan Successful Image
                Image(
                    painter = painterResource(id = R.drawable.baseline_history_24), // Add an image in res/drawable
                    contentDescription = "Scan Successful",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(16.dp)
                )

                Text(text = "Scan Successful!", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // Card for scanned text
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = qrResult.rawValue,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card with Share and Copy Buttons
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            shareText(context, qrResult.rawValue)
                        }) {
                            Text(text = "Share")
                        }

                        Button(onClick = {
                            copyToClipboard(context, qrResult.rawValue)
                        }) {
                            Text(text = "Copy")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                ResultDetailCard(parsedResult = it)
                Spacer(modifier = Modifier.height(16.dp))
                // Back to Scanner Button
                Button(onClick = { }) {
                    Text(text = "Scan Again")
                }
            }
        }
    }
}

// Function to share text
fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share via")
    context.startActivity(shareIntent)
}

// Function to copy text to clipboard
fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Scanned QR Code", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}

@Composable
fun ResultDetailCard(parsedResult: ParsedQRResult) {
    when (parsedResult.type) {
        QRType.URL -> UrlCard(parsedResult.data as String)
        QRType.PHONE -> PhoneCard(parsedResult.data as String)
        QRType.EMAIL -> EmailCard(parsedResult.data as ParsedEmail)
        QRType.TEXT -> TextCard(parsedResult.data as String)
        QRType.WIFI -> WifiCard(parsedResult.data as WifiInfo)
        QRType.CONTACT -> ContactCard(parsedResult.data as ContactInfo)
        QRType.UPI -> UpiCard(info = parsedResult.data as UPIInfo)
    }
}
