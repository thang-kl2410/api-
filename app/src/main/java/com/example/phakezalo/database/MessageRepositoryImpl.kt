package com.example.phakezalo.database

import android.util.Log
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.database.repository.MessageRepository
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(): MessageRepository {
    override fun getMessages(id: String, callback: (ArrayList<Message>) -> Unit) {
        val messages = ArrayList<Message>()

        FirebaseInstance.dbRef().child(id).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val dataMss = snap.getValue(Message::class.java)
                            messages.add(dataMss!!)
                        }
                        messages.reverse()
                        callback(messages)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun sendMessage(isSend: Boolean, text: String, id: String, time: String, type: String) {
        val idMss = FirebaseInstance.dbRef().push().key!!.toString()

        FirebaseInstance.dbRef().child(id).child("endMessageTime")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val change = snapshot.getValue(Long::class.javaObjectType)
                    if (change != null) {
                        val updatesMap = HashMap<String, Any>()
                        updatesMap["beforeEndMessageTime"] = change
                        FirebaseInstance.dbRef().child(id).updateChildren(updatesMap)
                            .addOnSuccessListener {
                            }.addOnFailureListener {
                            }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        FirebaseInstance.dbRef().child(id).child("messages")
            .child(idMss)
            .setValue(Message(idMss,text,time,isSend,type))
            .addOnCompleteListener {
            }
            .addOnCanceledListener {
            }

        val updatesMap = HashMap<String, Any>()
        updatesMap["endMessageTime"] = System.currentTimeMillis()*(-1)
        FirebaseInstance.dbRef().child(id).updateChildren(updatesMap)
            .addOnSuccessListener {
            }.addOnFailureListener {
            }
    }

    override fun removeMessage(idUser: String, id: String) {
        val messages = ArrayList<Message>()
        FirebaseInstance.dbRef()
            .child(idUser)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val dataMss = snap.getValue(Message::class.java)
                            messages.add(dataMss!!)
                        }
                        //Update time message end
                        if(id == messages.last().id.toString()){
                            FirebaseInstance.dbRef().child(idUser).child("beforeEndMessageTime")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val change = snapshot.getValue(Long::class.javaObjectType)
                                        if (change != null) {
                                            val updatesMap = HashMap<String, Any>()
                                            updatesMap["endMessageTime"] = change
                                            Log.i("Error of firebase:", change.toString())
                                            FirebaseInstance.dbRef().child(idUser).updateChildren(updatesMap)
                                                .addOnSuccessListener {
                                                    Log.i("Error of firebase:", change.toString())
                                                }.addOnFailureListener {
                                                }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                        FirebaseInstance.dbRef().child(idUser).child("messages").child(id).removeValue()
                            .addOnSuccessListener {
                            }
                            .addOnCanceledListener {
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        /*FirebaseInstance.dbRef().child(idUser).child("messages").child(id).removeValue()
            .addOnSuccessListener {

            }
            .addOnCanceledListener {

            }*/
    }

    override fun getEndMessage(id: String, callback: (Message) -> Unit) {
        getMessages(id){
            if(it.isNotEmpty()){
                callback(it.first())
            }
        }
    }
}