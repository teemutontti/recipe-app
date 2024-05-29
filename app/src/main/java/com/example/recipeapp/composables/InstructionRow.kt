package com.example.recipeapp.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.models.room.PersonalInstruction

@Composable
fun InstructionRow(
    index: Int,
    instruction: PersonalInstruction,
    handleDelete: ((Int) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = "${instruction.number}",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.width(32.dp).height(32.dp),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = instruction.step,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        if (handleDelete != null) {
            RemoveButton(index = index, onClick = { handleDelete(index) })
        }
    }
}
