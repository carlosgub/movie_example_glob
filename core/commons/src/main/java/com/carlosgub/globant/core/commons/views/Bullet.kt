package com.carlosgub.globant.core.commons.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.spacing_1_2
import com.carlosgub.globant.theme.theme.spacing_3
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6

@Composable
fun IMDBTitle(modifier: Modifier = Modifier, title: String) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (bullet, titleRef) = createRefs()
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(spacing_1_2)
                .height(spacing_6)
                .background(PrimaryColor)
                .constrainAs(bullet) {
                    start.linkTo(parent.start, spacing_6)
                    top.linkTo(parent.top, spacing_4)
                }
        )
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(titleRef) {
                linkTo(
                    start = bullet.end,
                    startMargin = spacing_3,
                    end = parent.end,
                    endMargin = spacing_6
                )
                top.linkTo(bullet.top)
                bottom.linkTo(bullet.bottom)
                width = Dimension.fillToConstraints
            }
        )
    }

}