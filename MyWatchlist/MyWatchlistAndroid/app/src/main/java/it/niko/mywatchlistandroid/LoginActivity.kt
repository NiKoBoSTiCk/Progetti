package it.niko.mywatchlistandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import it.niko.mywatchlistandroid.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                //TODO
                Toast.makeText(
                    this@LoginActivity,
                    "Welcome ${edUsernameLogin.text}",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("USERNAME", edUsernameLogin.text.toString())
                startActivity(intent)
            }
            tvSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }
}