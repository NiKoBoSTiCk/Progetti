package it.niko.mywatchlistandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.edUsernameSignup)
        val password = findViewById<EditText>(R.id.edPasswordSignup)
        val email = findViewById<EditText>(R.id.edEmailSignup)
        val signupBottom = findViewById<Button>(R.id.btnSignup)
        val loginLink = findViewById<TextView>(R.id.tvLogin)

        signupBottom.setOnClickListener {
            //TODO
            Toast.makeText(
                this@SignupActivity,
                "Welcome ${username.text}",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USERNAME", username.text.toString())
            startActivity(intent)
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}