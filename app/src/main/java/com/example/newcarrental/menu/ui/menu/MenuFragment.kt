package com.example.newcarrental.menu.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.newcarrental.R
import com.example.newcarrental.adapters.CatalogRVAdapter
import com.example.newcarrental.adapters.CategoryRVAdapter
import com.example.newcarrental.databinding.FragmentMenuBinding
import com.example.newcarrental.models.CarsModel
import com.example.newcarrental.order.OrderActivity
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_right)
    }

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[MenuViewModel::class.java]

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val cars: ArrayList<CarsModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo change to db
        for (i in 0 until 4) {
            cars.add(
                CarsModel(
                    "https://sun9-84.userapi.com/impg/eGP0PeZAiFgeHZb0W4wcEaAM3e4o8KXQFf-04Q/ha45EWNSbSw.jpg?size=1000x408&quality=96&sign=2c4b44a4d657ba18e8abe8ef993e7265&type=album",
                    "Porsche 911 Targa 4S",
                    36000,
                    650,
                    2020,
                    3.6
                )
            )
        }

        val categoryRVAdapter = CategoryRVAdapter(requireContext(), R.layout.item_category_rv, cars)

        menu_rv1.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        menu_rv2.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        menu_rv3.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        menu_rv4.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }

        menu_cat_btn1.setOnClickListener { catalogIntent(menu_cat_btn1) }
        menu_cat_btn2.setOnClickListener { catalogIntent(menu_cat_btn2) }
        menu_cat_btn3.setOnClickListener { catalogIntent(menu_cat_btn3) }
        menu_cat_btn4.setOnClickListener { catalogIntent(menu_cat_btn4) }

        categoryRVAdapter.clickListener = categoryItemClickListener

        catalog_back_btn.setOnClickListener { backIntent() }
    }

    private fun catalogIntent(view: View) {

        layout2.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(1000.toLong())
                .setListener(null)
        }
        layout1.visibility = View.INVISIBLE

        when (view.id) {
            R.id.menu_cat_btn1 -> {
                catalog_header_tv.text = "Внедорожники"
            }
            R.id.menu_cat_btn2 -> {
                catalog_header_tv.text = "Седаны"
            }
            R.id.menu_cat_btn3 -> {
                catalog_header_tv.text = "Спортакры"
            }
            R.id.menu_cat_btn4 -> {
                catalog_header_tv.text = "Кабриолеты"
            }
        }
        val catalogRVAdapter = CatalogRVAdapter(requireContext(), R.layout.item_catalog_rv, cars)

        catalog_rv.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = catalogRVAdapter
        }

        catalogRVAdapter.clickListener = catalogItemClickListener
    }

    private fun backIntent() {
        layout1.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(1000.toLong())
                .setListener(null)
        }
        layout2.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val catalogItemClickListener = object : CatalogRVAdapter.CatalogItemClickListener {
        override fun showOrder(name: String) {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }
    }

    private val categoryItemClickListener = object : CategoryRVAdapter.CategoryItemClickListener {
        override fun showOrder(name: String) {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }
    }
}