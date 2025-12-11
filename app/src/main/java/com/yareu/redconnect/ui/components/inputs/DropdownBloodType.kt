package com.yareu.redconnect.ui.components.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBloodType(
    selectedBloodType: String,
    onBloodTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Golongan Darah"
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedBloodType,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BurgundyPrimary,
                unfocusedBorderColor = Gray,
                focusedLabelColor = BurgundyPrimary,
                unfocusedLabelColor = Gray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Constants.BLOOD_TYPES.forEach { bloodType ->
                DropdownMenuItem(
                    text = { Text(bloodType) },
                    onClick = {
                        onBloodTypeSelected(bloodType)
                        expanded = false
                    }
                )
            }
        }
    }
}