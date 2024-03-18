package com.example.group1_mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private var authentic: FirebaseAuth? = null
    private lateinit var btnRegis: Button
    private lateinit var btnLogin: Button
    private var email: EditText? = null
    private var password: EditText? = null
    private var cPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        authentic = FirebaseAuth.getInstance()
        btnRegis = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
        cPassword = findViewById(R.id.loginCPassword)
        btnRegis.setOnClickListener {
            inputValidation()
        }
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun inputValidation() {
        if(email!!.text.toString().trim()==""){
            Toast.makeText(this@RegisterActivity,"Email required", Toast.LENGTH_SHORT).show()
            return
        }

        if(password!!.text.toString().trim()==""){
            Toast.makeText(this@RegisterActivity,"Password required", Toast.LENGTH_SHORT).show()
            return
        }
        if(cPassword!!.text.toString().trim()==""){
            Toast.makeText(this@RegisterActivity,"Confirm Password required", Toast.LENGTH_SHORT).show()
            return
        }
        DoRegister(email!!.text.toString().trim(),password!!.text.toString().trim())
    }

    private fun DoRegister(trim: String, trim1: String) {
        authentic?.createUserWithEmailAndPassword(trim,trim1)
            ?.addOnCompleteListener(this@RegisterActivity) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@RegisterActivity, CandidateActivity::class.java))
                } else {
                    Toast.makeText(this@RegisterActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}