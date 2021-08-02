package com.example.yoga

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private val ACCOUNT = "account"
    private var start = 0

    private val RC_SIGN_IN = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onResume() {
        super.onResume()

        val sharedPreferences = this.getSharedPreferences("DATA", 0)
        start = sharedPreferences.getInt("start", 0)

        val signInStatus = intent.getStringExtra("sign_in_status")

        if (signInStatus == "true") {
            start = 0
            signOut()
        } else if (signInStatus == "false") {
            start = 0
        }

        if (start != 0) {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(ACCOUNT, account)
            }
            startActivity(intent)
            this.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setOnClickListener {
            signIn()
        }

        skip_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            start = 1
            this.finish()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(ACCOUNT, account)
            }
            startActivity(intent)
            start = 1
            this.finish()
        } else {
            Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        val sharedPreferences = this.getSharedPreferences("DATA", 0)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putInt("start", start)
            apply()
        }
        super.onDestroy()
    }
}