package com.dicoding.bangkit.soreskin.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.bangkit.soreskin.databinding.FragmentSignInBinding
import com.dicoding.bangkit.soreskin.ui.MainViewModel
import com.dicoding.bangkit.soreskin.util.Extensions.isValidated
import com.dicoding.bangkit.soreskin.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAuth()
        signIn()
        signUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signIn() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (validateData(email, password)) {
                doSignIn(email, password)
            }
        }
    }

    private fun signUp() {
        binding.tvSignUp.setOnClickListener {
            val directions = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(directions)
        }
    }

    private fun doSignIn(email: String, password: String) {
        viewModel.signIn(email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    val directions =
                        SignInFragmentDirections.actionSignInFragmentToHomeFragment(result.data.email)
                    findNavController().navigate(directions)
                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateData(email: String, password: String): Boolean {
        return when {
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
            else -> true
        }
    }

    private fun checkAuth() {
        viewModel.auth.observe(viewLifecycleOwner) { result ->
            if (result.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Not logged in", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
                val directions =
                    SignInFragmentDirections.actionSignInFragmentToHomeFragment(result)
                findNavController().navigate(directions)
            }
        }
    }
}