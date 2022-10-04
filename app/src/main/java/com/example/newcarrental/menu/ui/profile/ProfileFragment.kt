package com.example.newcarrental.menu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.example.newcarrental.R
import com.example.newcarrental.databinding.FragmentProfileBinding
import com.example.newcarrental.favorites.FavoritesActivity
import com.example.newcarrental.login.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_right)
    }

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_contacts_btn.setOnClickListener { showContacts() }
        profile_favorites_btn.setOnClickListener { showFavorites() }
        profile_language_switch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                profile_language_switch.text = "Русский язык"
            } else {
                profile_language_switch.text = "English"
            }
        }
        profile_logout_btn.setOnClickListener { logout() }
    }

    private fun logout() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun showContacts() {
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_ring)
            .setTitle("Контакты")
            .setMessage(
                "Вы можете связаться с менеджером по номеру +7(777) 777-77-77. " +
                        "Или же приехать к наc в офис по адресам: " +
                        "\n\t\tМосква: ул. Новый Арбат, 36, стр. 3" +
                        "\n\t\tСочи: Автодром Сочи, ул. Триумфальная, д. 26." +
                        "\n\t\tЕкатеринбург: ул. Бориса Ельцина – 3, Паркинг сектор Б." +
                        "\nВ любой день недели с 9:00 до 21:00"
            )
            .setPositiveButton("ОК") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun showFavorites(){
        val intent = Intent(requireContext(), FavoritesActivity::class.java)
        startActivity(intent)
    }
}