package it.niko.mywatchlistandroid

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import it.niko.mywatchlistandroid.databinding.FragmentLoginBinding
import it.niko.mywatchlistandroid.model.LoginRequest
import it.niko.mywatchlistandroid.model.LoginResponse
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var authService: AuthService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authService = RetrofitInstance.getRetrofitInstance().create(AuthService::class.java)
        sessionManager = SessionManager(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etLoginUsername.text.toString()
                val password = etLoginPassword.text.toString()
                login(LoginRequest(username, password))
            }
            tvOrSignup.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }
        return binding.root
    }

    private fun login(loginRequest: LoginRequest) {
        val responseLiveData: LiveData<Response<LoginResponse>> = liveData {
            val response = authService.login(loginRequest)
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val token = it.body()?.token!!
                sessionManager.saveAuthToken(token)
                binding.apply {
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_seriesFragment)
                }
            }
            else {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
