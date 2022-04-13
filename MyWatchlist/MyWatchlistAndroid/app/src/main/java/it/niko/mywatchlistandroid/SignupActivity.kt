package it.niko.mywatchlistandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import it.niko.mywatchlistandroid.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSignup.setOnClickListener {
                //TODO
                Toast.makeText(
                    this@SignupActivity,
                    "Welcome ${edUsernameSignup.text}",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                intent.putExtra("USERNAME", edUsernameSignup.text.toString())
                startActivity(intent)
            }
            tvLogin.setOnClickListener {
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}