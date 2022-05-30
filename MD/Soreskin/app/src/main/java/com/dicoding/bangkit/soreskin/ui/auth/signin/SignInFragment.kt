package com.dicoding.bangkit.soreskin.ui.auth.signin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dicoding.bangkit.soreskin.R
import com.dicoding.bangkit.soreskin.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val user: FirebaseUser? by lazy { auth.currentUser }

    private lateinit var googleSignInClient: GoogleSignInClient

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

        if (user != null) {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val resultSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    try {
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        Toast.makeText(
                            requireContext(),
                            "Google sign in failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val directions = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                    findNavController().navigate(directions)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireContext(),
                        "signInWithCredential:failure",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultSignIn.launch(signInIntent)
    }
}