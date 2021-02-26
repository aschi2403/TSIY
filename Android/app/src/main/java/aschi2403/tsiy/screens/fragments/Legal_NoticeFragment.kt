package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import aschi2403.tsiy.R

/**
 * A simple [Fragment] subclass.
 * Use the [Legal_NoticeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Legal_NoticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legal__notice, container, false)
    }
}