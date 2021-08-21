package aschi2403.tsiy.screens.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentLegalNoticeBinding
import aschi2403.tsiy.screens.activities.MainActivity


class LegalNoticeFragment : Fragment() {
    private lateinit var binding: FragmentLegalNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_legal__notice, container, false
        )

        binding.MPAndroidChart.setOnClickListener {
            openBrowser("https://github.com/PhilJay/MPAndroidChart")
        }
        binding.icondialoglib.setOnClickListener {
            openBrowser("https://github.com/maltaisn/icondialoglib")
        }

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    private fun openBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}