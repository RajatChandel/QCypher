package `in`.rchandel.qcypher.ui.screens.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.rchandel.qcypher.data.model.ParsedQRResult
import `in`.rchandel.qcypher.data.model.QRResult
import `in`.rchandel.qcypher.data.model.QRType
import `in`.rchandel.qcypher.di.DefaultResultParser
import `in`.rchandel.qcypher.domain.resultparser.ResultParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @DefaultResultParser private val resultParser: ResultParser
) : ViewModel() {

    private val _parsedResult = MutableStateFlow<ParsedQRResult?>(null)
    val parsedResult: StateFlow<ParsedQRResult?> = _parsedResult

    private val _isParsed = MutableStateFlow(savedStateHandle["isParsed"] ?: false)
    val isParsed: StateFlow<Boolean> = _isParsed

    fun parseQRResult(qrResult: QRResult) {
        if (!_isParsed.value) {
            viewModelScope.launch {
                val result = async {
                    resultParser.parseQrResult(qrResult)
                }
                _parsedResult.value = result.await()
                _isParsed.value = true
                savedStateHandle["isParsed"] = true
            }
        }
    }
}
