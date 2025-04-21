package com.example.favored

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.favored.databinding.FragmentFavoredBinding
import com.example.model.TranslationHistory
import com.example.ui.base.BaseFragment
import com.example.ui.translationHistory.TranslationHistoryAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoredFragment :
    BaseFragment<FragmentFavoredBinding, FavoredViewmodel>(),
    TranslationHistoryAdapter.IHistoryTranslationAdapterListener {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): FragmentFavoredBinding {
        return FragmentFavoredBinding.inflate(inflater, container, attachToParent)
    }

    override val viewModel: FavoredViewmodel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteTranslationHistory()

        lifecycleScope.launch {
            launch {
                viewModel.getFavoriteTranslationHistoryFlow.collect {
                    Log.d("AAAA", "it = $it")
                    binding.favoredTranslationHistory.updateTranslationHistory(it)
                }
            }
        }

        setUpFavoredTranslationHistory()
    }

    private fun setUpFavoredTranslationHistory() {
        binding.favoredTranslationHistory.iHistoryTranslationAdapter = this
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoredFragment().apply {
                arguments =
                    Bundle().apply {
                    }
            }

        const val TAG = "FavoredFragment"
    }

    override fun onClickFavorite(translatedWord: TranslationHistory) {
        TODO("Not yet implemented")
    }

    override fun onClickItem(translatedWord: TranslationHistory) {
        TODO("Not yet implemented")
    }

    override fun onDeleteHistoryItem(translatedWord: TranslationHistory) {
        viewModel.deleteTranslationHistory(translatedWord.id)
        // Optional: Show undo snackbar
        view?.let { view ->
            Snackbar.make(
                view,
                "Item is deleted",
                Snackbar.LENGTH_INDEFINITE,
            ).setAction("Undo") {
                // Undo the deletion
                viewModel.undoTranslationHistory(translatedWord)
            }.show()
        }
    }
}
