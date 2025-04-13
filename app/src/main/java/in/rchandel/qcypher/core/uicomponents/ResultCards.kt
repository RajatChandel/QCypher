package `in`.rchandel.qcypher.core.uicomponents

import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.rchandel.qcypher.R
import `in`.rchandel.qcypher.data.model.ContactInfo
import `in`.rchandel.qcypher.data.model.ParsedEmail
import `in`.rchandel.qcypher.data.model.UPIInfo
import `in`.rchandel.qcypher.data.model.WifiInfo

@Composable
fun UrlCard(url: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Website", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(url, color = Color.Blue, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun PhoneCard(phone: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // You can use Context to trigger the dial intent here
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Phone Number", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = phone,
                color = Color(0xFF1E88E5),
                fontSize = 16.sp
            )
        }
    }
}

class ParsedEmailPreviewParameterProvider : PreviewParameterProvider<ParsedEmail> {
    override val values: Sequence<ParsedEmail>
        get() = sequenceOf(
            ParsedEmail(
                "theodore.roosevelt@altostrat.com",
                "Hello",
                "This is a test email."
            )
        )
}

@Preview(showBackground = true)
@Composable
fun EmailCard(@PreviewParameter(ParsedEmailPreviewParameterProvider::class) email: ParsedEmail) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                var mailTo = "mailto:" + email.emailAddress
                var subjectAvailable = false
                email.subject?.let {
                    subjectAvailable = true
                    mailTo = mailTo + "?&subject=" + Uri.encode(email.subject)
                }

                email.body?.let {
                    if (subjectAvailable) {
                        mailTo = mailTo + "&body=" + Uri.encode(email.body)
                    } else {
                        mailTo = mailTo + "?&body=" + Uri.encode(email.body)
                    }
                }
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(mailTo)
                }
                context.startActivity(intent)
            },
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.type_email), // Add "Email" to strings.xml
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = R.dimen.font_size_x_large).value.sp
            )

            KeyAndValueColumn(key = stringResource(id = R.string.to_heading), value = email.emailAddress)

            email.subject?.let {
            KeyAndValueColumn(key = stringResource(id = R.string.subject_heading), value = it)
            }

            email.body?.let {
                KeyAndValueColumn(key = stringResource(id = R.string.body_heading), value = it)
            }
        }
    }
}

@Composable
fun KeyAndValueColumn(
    key: String,
    value: String,
) {
    Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_x_large))) {
        Text(
            text = key, // Add "To" to strings.xml
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xx_small)))
        Text(
            text = value,
            fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
        )
    }
}

@Composable
fun TextCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Text", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(text)
        }
    }
}

@Composable
fun WifiCard(wifiInfo: WifiInfo) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Open Wi-Fi settings or handle the Wi-Fi connection logic
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Wi-Fi Network", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text("SSID: ${wifiInfo.ssid}", maxLines = 1, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Password: ${wifiInfo.password}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = {
                    // Copy the Wi-Fi password to the clipboard
                    clipboardManager.setText(AnnotatedString(wifiInfo.password))
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_content_copy_24),
                        contentDescription = "Copy Password"
                    )
                }
            }
        }
    }
}

@Composable
fun ContactCard(info: ContactInfo) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_INSERT).apply {
                    type = ContactsContract.Contacts.CONTENT_TYPE
                    putExtra(ContactsContract.Intents.Insert.NAME, info.name)
                    putExtra(ContactsContract.Intents.Insert.EMAIL, info.email)
                    putExtra(ContactsContract.Intents.Insert.JOB_TITLE, info.title)
                    putExtra(ContactsContract.Intents.Insert.COMPANY, info.organization)
                    putExtra(ContactsContract.Intents.Insert.NOTES, info.address)

                    // Add the first phone number, if available
                    info.phoneList
                        ?.firstOrNull()
                        ?.let { phone ->
                            putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                        }
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contact", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            info.name?.let { Text("Name: $it") }

            info.phoneList?.forEachIndexed { index, phone ->
                Text("Phone ${index + 1}: $phone")
            }

            info.email?.let { Text("Email: $it") }
            info.organization?.let { Text("Organization: $it") }
            info.title?.let { Text("Title: $it") }
            info.address?.let { Text("Address: $it") }
        }
    }
}

@Composable
fun UpiCard(info: UPIInfo) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val upiUri =
                    "upi://pay?pa=${info.payeeAddress}&pn=${info.payeeName}&am=${info.amount}&cu=${info.currency}&url=${info.url}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(upiUri))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("UPI Payment", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            info.payeeName?.let { Text("Payee Name: $it") }
            info.payeeAddress?.let { Text("Payee Address: $it") }
            info.amount?.let { Text("Amount: ₹$it") }
            info.currency?.let { Text("Currency: $it") }
            info.url?.let { Text("Merchant URL: $it") }
        }
    }
}

