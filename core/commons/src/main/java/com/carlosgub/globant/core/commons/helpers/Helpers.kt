package com.carlosgub.globant.core.commons.helpers

import android.content.Context
import android.widget.Toast
import com.carlosgub.globant.core.commons.sealed.GenericState

fun <T> showLoading(
    uiState: GenericState<T>
): Boolean = when (uiState) {
    is GenericState.Error -> false
    GenericState.Loading -> true
    GenericState.None -> false
    is GenericState.Success -> false
}

fun <T> showError(
    uiState: GenericState<T>,
    context: Context
): Unit = when (uiState) {
    is GenericState.Error -> Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
    else -> Unit
}

fun showError(
    error: String,
    context: Context
): Unit = Toast.makeText(context, error, Toast.LENGTH_SHORT).show()


fun <T> getDataFromUiState(
    uiState: GenericState<T>
): T? =
    if (uiState is GenericState.Success) {
        uiState.item
    } else {
        null
    }
