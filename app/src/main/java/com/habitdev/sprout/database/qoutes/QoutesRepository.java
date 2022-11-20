package com.habitdev.sprout.database.qoutes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QoutesRepository {

    FirebaseFirestore firebaseFirestore;
    List<Qoutes> qoutesList;

    public QoutesRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        qoutesList = new ArrayList<>();
    }

    public List<Qoutes> getQoutesList() {

        firebaseFirestore.collection("quotes").get().addOnCompleteListener(task -> {
            List<Qoutes> qoutes = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    qoutes.add(document.toObject(Qoutes.class));
                    Log.d("tag", document.getId() + " => " + document.getData());
                }
            } else {
                Log.d("tag", "Error getting documents: ", task.getException());
            }
            qoutesList.addAll(qoutes);
        });

//        firebaseFirestore.collection("quotes").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                List<Qoutes> qoutes = new ArrayList<>();
//                assert value != null;
//                for (QueryDocumentSnapshot documentSnapshot : value) {
//                    if (documentSnapshot != null)
//                        qoutes.add(documentSnapshot.toObject(Qoutes.class));
//                }
//                Log.d("tag", "Repository: Quotes: " + qoutes.toString());
//                for (Qoutes q: qoutes){
//                    qoutesList.add(q);
//                }
//            }
//        });

        Log.d("tag", "Repository: QuoteList: " + qoutesList.toString());
        return qoutesList;
    }

//    public MutableLiveData<List<Qoutes>> getQoutesListMutableLiveData() {
//        Log.i("tag", "getQoutesListMutableLiveData: ");
//
//        firebaseFirestore.collection("quotes").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                List<Qoutes> qoutesList = new ArrayList<>();
//
//                assert value != null;
//                for (QueryDocumentSnapshot documentSnapshot: value){
//                    if (documentSnapshot != null) qoutesList.add(documentSnapshot.toObject(Qoutes.class));
//                }
//                qoutesListMutableLiveData.postValue(qoutesList);
//            }
//        });
//        return qoutesListMutableLiveData;
//    }

//    //get blog from firebase firestore
//    public MutableLiveData<List<Blog>> getBlogListMutableLiveData() {
//        Log.i("TAG", "getBlogListMutableLiveData: ");
//        mFirestore.collection("Blog").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                List<Blog> blogList = new ArrayList<>();
//                for (QueryDocumentSnapshot doc : value) {
//                    if (doc != null) {
//                        blogList.add(doc.toObject(Blog.class));
//                    }
//                }
//                blogListMutableLiveData.postValue(blogList);
//            }
//        });
//        return blogListMutableLiveData;
//    }

    //delete blog

    //update blog
}
