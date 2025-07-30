package com.example.recipeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.models.BarModel
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch

sealed interface BarScanState {
    data object Ideal : BarScanState
    data class ScanSuccess(val barStateModel: BarModel) : BarScanState
    data class Error(val error: String) : BarScanState
    data object Loading : BarScanState
}

class BarcodeScannerViewModel(application: Application): BaseViewModel(application) {
    private var _barScanState: MutableState<BarScanState?> = mutableStateOf(null)
    val barScanState get() = _barScanState.value

    init {
        setLoading(true)
        Log.d("AuthViewModel", "Base url: ${BuildConfig.BASE_URL}")
    }

    fun onBarCodeDetected(barcodes: List<Barcode>) {
        viewModelScope.launch {
            if (barcodes.isEmpty()) {
                _barScanState.value = BarScanState.Error("No barcode detected")
                return@launch
            }

            _barScanState.value = BarScanState.Loading

            barcodes.forEach { barcode ->
                barcode.rawValue?.let { barcodeValue ->
                    try {
                        Log.d("BARCODE", "Barcode value: $barcodeValue")
                        _barScanState.value = BarScanState.ScanSuccess(barStateModel = BarModel(barcodeValue))
                    } catch (e: Exception) {
                        Log.i("BARCODE", "onBarCodeDetected: $e", )
                        _barScanState.value = BarScanState.Error("Invalid JSON format in barcode")
                    }
                    return@launch
                }
            }
            _barScanState.value = BarScanState.Error("No valid barcode value")
        }
    }

    fun resetState() {
        _barScanState.value = BarScanState.Ideal
    }
}