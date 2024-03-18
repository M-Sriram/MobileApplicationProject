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

class LoginActivity : AppCompatActivity() {
    private var authentic: FirebaseAuth? = null
    private lateinit var btnRegis: Button
    private lateinit var btnLogin: Button
    private var email: EditText? = null
    private var password: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        authentic = FirebaseAuth.getInstance()
        btnRegis = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
        btnLogin.setOnClickListener {
            inputValidation()
        }
        btnRegis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    private fun inputValidation() {
        if(email!!.text.toString().trim()==""){
            Toast.makeText(this@LoginActivity,"Email required", Toast.LENGTH_SHORT).show()
            return
        }
        if(password!!.text.toString().trim()==""){
            Toast.makeText(this@LoginActivity,"Password required", Toast.LENGTH_SHORT).show()
            return
        }
        DoLogin(email!!.text.toString().trim(),password!!.text.toString().trim())
    }

    private fun DoLogin(trim: String, trim1: String) {
        authentic?.signInWithEmailAndPassword(trim,trim1)
            ?.addOnCompleteListener(this@LoginActivity) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, IntroActivity::class.java))
                } else {
                    Toast.makeText(this@LoginActivity, task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
            }
    }
}