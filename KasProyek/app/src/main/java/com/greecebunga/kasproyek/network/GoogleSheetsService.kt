package com.greecebunga.kasproyek.network

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.greecebunga.kasproyek.data.Transaction

class GoogleSheetsService(private val sheetsService: Sheets) {
    private val spreadsheetId = "1wQf7lttp-kRlDWfe10eAlHTSurJ3JAq8uPVY7Rlzpg0"
    private val sheetName = "Transaksi"
    private val range = "$sheetName!A2:D"

    suspend fun appendRow(values: List<Any>) = withContext(Dispatchers.IO) {
        val valueRange = ValueRange().setValues(listOf(values))
        sheetsService.spreadsheets().values().append(spreadsheetId, range, valueRange)
            .setValueInputOption("USER_ENTERED")
            .execute()
    }

    suspend fun getAllRows(): List<List<Any>> = withContext(Dispatchers.IO) {
        val result = sheetsService.spreadsheets().values().get(spreadsheetId, range).execute()
        result.getValues() ?: emptyList()
    }

    suspend fun updateRow(transaction: Transaction) = withContext(Dispatchers.IO) {
        val valueRange = ValueRange().setValues(listOf(transaction.toSheetRow()))
        sheetsService.spreadsheets().values().update(spreadsheetId, range, valueRange)
            .setValueInputOption("USER_ENTERED")
            .execute()
    }

    suspend fun deleteRow(transaction: Transaction) = withContext(Dispatchers.IO) {
        // Note: Direct row deletion is complex in Sheets API
        // Consider marking as deleted or using batch updates
    }
}
