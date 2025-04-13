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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
        if (!isParsed) {
            viewModel.parseQRResult(qrResult)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        parsedResult?.let {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(dimensionResource(id = R.dimen.padding_x_large)),
            ) {
                Card(
                    shape = RoundedCornerShape(2),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = dimensionResource(id = R.dimen.elevation_small)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_x_large))
                        .align(Alignment.Center)

                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.scan_successful),
                        fontSize = dimensionResource(id = R.dimen.font_size_xx_large).value.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                    Card(
                        shape = RoundedCornerShape(2),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = dimensionResource(id = R.dimen.elevation_small)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding_x_large))
                    ) {
                        Column( modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_x_large))) {
                            ResultDetailCard(parsedResult = it)
                        }
                    }

                    Column( modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(
                                id = R.dimen.padding_x_large
                            )
                        ), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button( modifier =  Modifier.fillMaxWidth(), onClick = {
                            shareText(context, qrResult.rawValue)
                        }) {
                            Text(text = stringResource(id = R.string.share))
                        }

                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            copyToClipboard(context, qrResult.rawValue)
                        }) {
                            Text(text = stringResource(id = R.string.copy))
                        }
                    }
                }
//
//                Button(
//                    modifier = Modifier
//                        .height(dimensionResource(id = R.dimen.button_height_medium))
//                        .fillMaxWidth()
//                        .align(Alignment.BottomCenter),
//                    onClick = {
//                        navController.popBackStack()
//                    }) {
//                    Text(text = stringResource(id = R.string.scan_again))
//                }
            }
        }
    }
}

fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, context.getString(R.string.share_via))
    context.startActivity(shareIntent)
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
        .show()
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
