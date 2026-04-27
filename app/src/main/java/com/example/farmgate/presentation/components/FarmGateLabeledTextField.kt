package com.example.farmgate.presentation.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun FarmGateLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 12
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = Color(0xFF1A1A1A)
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF8A8A8A)
            )
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(cornerRadius.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        enabled = enabled,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xFF1A1A1A)
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF1A1A1A),
            unfocusedTextColor = Color(0xFF1A1A1A),
            focusedPlaceholderColor = Color(0xFF8A8A8A),
            unfocusedPlaceholderColor = Color(0xFF8A8A8A),
            focusedBorderColor = Color(0xFF18D66B),
            unfocusedBorderColor = Color(0xFFD0D0D0),
            cursorColor = Color(0xFF18D66B)
        )
    )
}




@Preview(showBackground = true, backgroundColor = 0xFFF8F5F2) // Warm neutral background from your theme
@Composable
fun FarmGateLabeledTextFieldPreview() {
    var textValue by remember { mutableStateOf("") }
    var filledTextValue by remember { mutableStateOf("09123456789") }

    MaterialTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = Color(0xFFF8F5F2)
        ) {
            Column {
                // Example 1: Empty state
                FarmGateLabeledTextField(
                    label = "Phone Number",
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Enter your phone number",
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                    enabled = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Example 2: Filled state
                FarmGateLabeledTextField(
                    label = "Farm Name",
                    value = filledTextValue,
                    onValueChange = { filledTextValue = it },
                    placeholder = "Enter farm name",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    enabled = true
                )
            }
        }
    }
}