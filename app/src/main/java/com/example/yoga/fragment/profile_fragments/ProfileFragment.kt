package com.example.yoga.fragment.profile_fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.yoga.LoginActivity
import com.example.yoga.R
import com.example.yoga.bindImageFromUrl
import com.example.yoga.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class ProfileFragment : Fragment() {
    private val ACCOUNT = "account"
    private var SIGNED_IN = "null"

    private val account by lazy {
        this.activity?.intent?.getParcelableExtra<GoogleSignInAccount?>(ACCOUNT)
    }

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        if (account != null){
            SIGNED_IN = "true"
            bindImageFromUrl(binding.displayImage, account?.photoUrl.toString())
            binding.displayName.text = "Hii, ${account?.displayName}"
            binding.emailId.text = account?.email
            binding.signOutButton.visibility = View.VISIBLE
            binding.signInButton.visibility = View.GONE
        } else{
            SIGNED_IN = "false"
            binding.displayImage.setImageResource(R.drawable.ic_account_bold)
            binding.displayName.text = "Guest"
            binding.emailId.visibility = View.GONE
            binding.signOutButton.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
        }

        binding.signOutButton.setOnClickListener{
            val intent = Intent(this.activity, LoginActivity::class.java).apply {
                putExtra("sign_in_status", SIGNED_IN)
            }
            startActivity(intent)
            this.activity?.finish()
        }

        binding.signInButton.setOnClickListener{
            val intent = Intent(this.activity, LoginActivity::class.java).apply {
                putExtra("sign_in_status", SIGNED_IN)
            }
            startActivity(intent)
            this.activity?.finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }
}