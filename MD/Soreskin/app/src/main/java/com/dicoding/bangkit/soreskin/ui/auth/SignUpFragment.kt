package com.dicoding.bangkit.soreskin.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.bangkit.soreskin.databinding.FragmentSignUpBinding
import com.dicoding.bangkit.soreskin.ui.MainViewModel
import com.dicoding.bangkit.soreskin.util.Extensions.isValidated
import com.dicoding.bangkit.soreskin.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.getWindowInsetsController(requireActivity().window.decorView)?.isAppearanceLightStatusBars =
            true

        signUp()
        signIn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUp() {
        binding.btnSignUp.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirm = binding.etPasswordConfirm.text.toString()

            if (validateData(username, email, password, passwordConfirm)) {
                doSignUp(username, email, password)
            }
        }
    }

    private fun doSignUp(username: String, email: String, password: String) {
        viewModel.signUp(username, email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    val directions =
                        SignUpFragmentDirections.actionSignUpFragmentToHomeFragment(result.data.email)
                    findNavController().navigate(directions)
                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateData(
        username: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Boolean {
        return when {
            username.isEmpty() -> {
                binding.etlUsername.error = "Username cannot be null"
                binding.etlUsername.requestFocus()
                false
            }
            email.isEmpty() -> {
                binding.etlEmail.error = "Email cannot be empty"
                binding.etlEmail.requestFocus()
                false
            }
            !email.isValidated() -> {
                binding.etlEmail.error = "Invalid email"
                binding.etlEmail.requestFocus()
                false
            }
            password.isEmpty() -> {
                binding.etlPassword.error = "Password cannot be empty"
                binding.etlPassword.requestFocus()
                false
            }
            password.length < 6 -> {
                binding.etlPassword.error = "Password must be more than 6 characters"
                binding.etlPassword.requestFocus()
                false
            }
            passwordConfirm.isEmpty() -> {
                binding.etlPasswordConfirm.error = "Password confirmation cannot be empty"
                binding.etlPasswordConfirm.requestFocus()
                false
            }
            passwordConfirm != password -> {
                binding.etlPasswordConfirm.error = "Password does not match"
                binding.etlPasswordConfirm.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun signIn() {
        binding.tvSignIn.setOnClickListener { findNavController().popBackStack() }
    }
}