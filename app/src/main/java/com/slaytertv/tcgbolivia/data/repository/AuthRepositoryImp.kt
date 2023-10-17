package com.slaytertv.tcgbolivia.data.repository

import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.data.model.UserItem
import com.slaytertv.tcgbolivia.util.FireStoreCollection
import com.slaytertv.tcgbolivia.util.SharedPrefConstants
import com.slaytertv.tcgbolivia.util.UiState

//repository que trabaja junto con authrepository
class AuthRepositoryImp(
    //uasremos auth
    val auth: FirebaseAuth,
    //y firestore
    val database: FirebaseFirestore,
    //sharedpreferences
    val appPreferences: SharedPreferences,
    //gson
    val gson: Gson,
    val messagin:FirebaseMessaging
) : AuthRepository {

    //cuando alguien se registre pediremos el email y el password y los demas datos los colocaremos en user co UserItem
    override fun registerUser(
        email: String,
        password: String,
        user: UserItem, result: (UiState<String>) -> Unit
    ) {
        //cuando se cree y contenga el email y password
        auth.createUserWithEmailAndPassword(email,password)
            //y complete satisfactoriamente
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //
                    user.id = it.result.user?.uid ?: ""
                    //actualizamos los datos de usuario con la funcion
                    updateUserInfo(user) { state ->
                        //cuando el estado sea
                        when(state){
                            //satisfactorio mandamos mensaje indicando que se cumplio
                            is UiState.Sucess -> {
                                //
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener { x ->
                                        storeSession(id = it.result.user?.uid ?: "") {
                                            //if else para ver si falla el guardado  de session
                                            if(it == null){
                                                UiState.Sucess("User register successfully but session failed to store!")
                                            }else{
                                                result.invoke( UiState.Sucess("User register successfully!") )
                                            }
                                        }
                                    }
                                    ?.addOnFailureListener {xe->
                                        UiState.Failure(xe.toString())
                                    }
                            }
                            //y si falla otro error
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                            else -> {result.invoke(UiState.Failure("revise su conexion"))}
                        }
                    }
                }else{
                    //vamos a ver el tipo de error que nos puede ocurrir
                    try {
                        //cuando la autenticacion sea invalida
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        // uando el passw sea menor a 6 caracteres
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        //error de email
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        //cuand el email ya este registrado
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
    private fun mitoken(user: UserItem) {
        messagin.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            updatetoken(user,token)
        })
    }
    private fun updatetoken(user:UserItem, toek:String){
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        //pasamos el id q creara el dicumento y lo colocamos en nuestro documento a crear
        user.tokenmessag =toek
        document
            .update("tokenmessag",toek)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Log.w("TAG","error $it")
            }

    }

    //funcion para actualizar info del usuario
    override fun updateUserInfo(user: UserItem, result: (UiState<String>) -> Unit) {
        //colocamos el path que usaremos para crearlo
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        //pasamos el id q creara el dicumento y lo colocamos en nuestro documento a crear
        user.id = document.id
        document
            .set(user)
            //si se cumple
            .addOnSuccessListener {
                result.invoke(
                    UiState.Sucess("User has been update successfully")
                )
            }
            //si falla
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
    //funcion de login
    override fun loginUser(
        //pido email y passw para sacar los resultados
        email: String,
        password: String,
        result: (UiState<String>) -> Unit) {
        //pasamos los datros
        auth.signInWithEmailAndPassword(email,password)
            //si se completa
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val verification = auth.currentUser?.isEmailVerified
                    if(verification == true){
                        //nos manda mensaje de login successfully
                        storeSession(id = task.result.user?.uid ?: ""){
                            if (it == null){
                                result.invoke(UiState.Failure("Failed to store local session"))
                            }else{
                                mitoken(it)
                                result.invoke(UiState.Sucess("Login successfully!"))
                                /*if(it.statedate == "open") {
                                    result.invoke(UiState.Sucess("Login successfully!"))
                                }else{
                                    result.invoke(UiState.Failure("Renovar cuenta!!"))
                                }*/
                            }
                        }
                    }else{
                        result.invoke(UiState.Failure("Please verify you email!!"))
                    }
                }
            }.addOnFailureListener {
                //mensaje si falla
                result.invoke(UiState.Failure("Authentication failed, Check email and password"))
            }
    }

    //funcion si olvida password
    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Sucess("Email has been sent"))
                } else {
                    result.invoke(UiState.Failure(task.exception?.message))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email"))
            }
    }
    //si se desloguea
    override fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION,null).apply()
        result.invoke()
    }
    //revisar usu session
    override fun storeSession(id: String, result: (UserItem?) -> Unit) {
        //revisamos si el usuario estra en la bd
        database.collection(FireStoreCollection.USER).document(id)
            .get()
            .addOnCompleteListener {
                //si se cumple
                if (it.isSuccessful){
                    //creamos la var user para contener todos los datos de useritem
                    val user = it.result.toObject(UserItem::class.java)
                    //y mandamos la sesion como json al shared preference para guardarla
                    appPreferences.edit().putString(SharedPrefConstants.USER_SESSION,gson.toJson(user)).apply()
                    result.invoke(user)
                }else{
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }
    //obtenemos la sesion
    override fun getSession(result: (UserItem?) -> Unit) {
        val verification = auth.currentUser?.isEmailVerified
        if(verification == true){
            //variable con la sesion de ususario
            val user_str = appPreferences.getString(SharedPrefConstants.USER_SESSION,null)
            //si es igual a null
            if (user_str == null){
                result.invoke(null)
            }
            else{
                val user = gson.fromJson(user_str, UserItem::class.java)
                storeSession(id = user.id){
                    if (it == null){
                        result.invoke(null)
                    }else{
                        result.invoke(user)
                    }
                }
            }
        }
    }

    //anonymous
    override fun signInAnonymously(callback: (Boolean, String?) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message ?: "Error desconocido")
                }
            }
    }
    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}