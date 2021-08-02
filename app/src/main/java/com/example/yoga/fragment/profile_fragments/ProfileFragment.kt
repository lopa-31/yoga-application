package com.example.yoga.fragment.profile_fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.yoga.LoginActivity
import com.example.yoga.R
import com.example.yoga.utils.imageFromUrl
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val ACCOUNT = "account"
    private var SIGNED_IN = "null"

    private val account by lazy {
        this.activity?.intent?.getParcelableExtra<GoogleSignInAccount?>(ACCOUNT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        if (account != null){
            SIGNED_IN = "true"
            imageFromUrl(display_image, account?.photoUrl.toString())
            display_name.text = "Hii, ${account?.displayName}"
            email_id.text = account?.email
            sign_out_button.visibility = View.VISIBLE
            sign_in_button.visibility = View.GONE
        } else{
            SIGNED_IN = "false"
            display_image.setImageResource(R.drawable.ic_account_bold)
            display_name.text = "Guest"
            email_id.visibility = View.GONE
            sign_out_button.visibility = View.GONE
            sign_in_button.visibility = View.VISIBLE
        }

        sign_out_button.setOnClickListener{
            val intent = Intent(this.activity, LoginActivity::class.java).apply {
                putExtra("sign_in_status", SIGNED_IN)
            }
            startActivity(intent)
            this.activity?.finish()
        }

        sign_in_button.setOnClickListener{
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