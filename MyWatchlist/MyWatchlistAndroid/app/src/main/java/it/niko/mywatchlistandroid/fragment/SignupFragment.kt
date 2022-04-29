package it.niko.mywatchlistandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import it.niko.mywatchlistandroid.R
import it.niko.mywatchlistandroid.RetrofitInstance
import it.niko.mywatchlistandroid.databinding.FragmentSignupBinding
import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.SignupRequest
import it.niko.mywatchlistandroid.services.AuthService
import retrofit2.Response

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authService = RetrofitInstance.getRetrofitInstance().create(AuthService::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.apply {
            btnSignup.setOnClickListener {
                val username = etSignupUsername.text.toString()
                val email = etSignupEmail.text.toString()
                val password = etSignupPassword.text.toString()
                signup(SignupRequest(email, username, password))
            }
            tvOrLogin.setOnClickListener {
                it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
        return binding.root
    }

    private fun signup(signupRequest: SignupRequest) {
        val responseLiveData: LiveData<Response<MessageResponse>> = liveData {
            val response = authService.signup(signupRequest)
            emit(response)
        }
        responseLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.body()?.message, Toast.LENGTH_SHORT).show()
        }
        view?.findNavController()?.navigate(R.id.action_signupFragment_to_loginFragment)
    }
}