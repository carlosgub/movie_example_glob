package com.carlosgub.globant.core.commons.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.carlosgub.globant.theme.theme.TextColor
import com.carlosgub.globant.theme.theme.TextColorDisabled
import com.carlosgub.globant.theme.theme.buttonNoElevation
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_7

@Composable
fun IMDbButton(
    onClick: () -> Unit,
    testName: String,
    isEnabled: Boolean,
    text: String,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            if (isEnabled) {
                onClick()
            }
        },
        enabled = isEnabled,
        elevation = buttonNoElevation,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent
        ),
        modifier = modifier
            .testTag(testName)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button,
            color = if (isEnabled) {
                Color.White
            } else {
                TextColorDisabled
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isEnabled) {
                        Color.Black
                    } else {
                        TextColor
                    },
                    shape = MaterialTheme.shapes.medium
                )
                .padding(
                    horizontal = spacing_7,
                    vertical = spacing_4
                )
        )
    }
}