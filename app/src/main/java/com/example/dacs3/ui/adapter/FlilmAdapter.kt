package com.example.dacs3.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dacs3.R
import com.example.dacs3.databinding.BottomSheetDialogBinding
import com.example.dacs3.module.Film
import com.example.dacs3.module.MyListMovie
import com.example.dacs3.ui.activity.DetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FlilmAdapter(
    private var list: List<Film>,
    private val isMainSlider: Boolean,
    private val contextParent: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var mAuth: FirebaseAuth

    inner class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
    }

    inner class viewHolderMain(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mAuth = FirebaseAuth.getInstance()


        if (viewType == 0) {
            val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_film, parent, false)

            return viewHolder(layout)
        } else {
            val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_slider, parent, false)

            return viewHolderMain(layout)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val film = list[position]
        val user = mAuth.currentUser

        if (holder.itemViewType == 0) {
            val viewHolder: viewHolder = holder as viewHolder

            Glide.with(contextParent)
                .load(film.imageUrl)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.animation_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(viewHolder.img)

            viewHolder.img.setOnClickListener {
                bottomSheetDialog = BottomSheetDialog(contextParent)


                val bottomSheetDialogLayout = LayoutInflater.from(contextParent)
                    .inflate(R.layout.bottom_sheet_dialog, null, false)

                val bindingBottomSheetDialog =
                    BottomSheetDialogBinding.bind(bottomSheetDialogLayout)

                bindingBottomSheetDialog.apply {
                    this.btnPlay.setOnClickListener {
                        bottomSheetDialog.dismiss()


                        val intent = Intent(contextParent, DetailActivity::class.java)
                        intent.putExtra("id", film.id)
                        intent.putExtra("category", film.category)

                        ContextCompat.startActivity(contextParent, intent, null)
                    }

                    this.btnAddList.setOnClickListener {

                        if (user != null) {
                            try {
                                val nameRef = user.email?.split("@")?.get(0)
//                                Toast.makeText(
//                                    contextParent,
//                                    "${film.id} ${film.category} ${film.imageUrl} $nameRef",
//                                    Toast.LENGTH_SHORT
//                                ).show()

                                val database = Firebase.database
                                val myRef = database.getReference("${nameRef.toString()}/myList")

                                myRef.child(film.id.toString()).setValue(
                                    MyListMovie(film.id, film.category, film.imageUrl),
                                    object : DatabaseReference.CompletionListener {
                                        override fun onComplete(
                                            error: DatabaseError?,
                                            ref: DatabaseReference
                                        ) {
                                            Toast.makeText(
                                                contextParent,
                                                "Thêm thành công vào danh sách yêu thích",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    })


                            } catch (e: Exception) {
                                val nameRef = user.email?.split(".")?.get(0)

                                val database = Firebase.database
                                val myRef = database.getReference("${nameRef.toString()}/myList")

                                myRef.child(film.id.toString())
                                    .setValue(MyListMovie(film.id, film.category, film.imageUrl),
                                        object : DatabaseReference.CompletionListener {
                                            override fun onComplete(
                                                error: DatabaseError?,
                                                ref: DatabaseReference
                                            ) {
                                                Toast.makeText(
                                                    contextParent,
                                                    "Thêm thành công vào danh sách yêu thích",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        })
                            }

                        }else{
                            Toast.makeText(contextParent, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
                        }

                    }

                    Glide.with(contextParent)
                        .load(film.imageUrl)
                        .placeholder(R.drawable.animation_loading)
                        .error(R.drawable.img_2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .into(this.image)

                    this.title.text = film.name
                    this.description.text = "Each day, Rin awakens in virtual reality and uses a tablet which controls the simulation to create a new, different, beautiful world for herself. Until one day, everything changes, and Rin comes to learn the true origins behind her life inside a simulation."
                }



                bottomSheetDialog.setContentView(bottomSheetDialogLayout)
                bottomSheetDialog.show()
            }
        }


        if (holder.itemViewType == 1) {
            val viewHolder: viewHolderMain = holder as viewHolderMain
//            viewHolder.img.setImageResource(film.image)
            Glide.with(contextParent)
                .load(film.imageUrl)
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.img_2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(viewHolder.img)


            viewHolder.img.setOnClickListener {
                bottomSheetDialog = BottomSheetDialog(contextParent)

                val bottomSheetDialogLayout = LayoutInflater.from(contextParent)
                    .inflate(R.layout.bottom_sheet_dialog, null, false)

                val bindingBottomSheetDialog =
                    BottomSheetDialogBinding.bind(bottomSheetDialogLayout)

                bindingBottomSheetDialog.apply {
                    this.btnPlay.setOnClickListener {
                        bottomSheetDialog.dismiss()

                        val intent = Intent(contextParent, DetailActivity::class.java)
                        intent.putExtra("id", film.id)
                        intent.putExtra("category", film.category)

                        ContextCompat.startActivity(contextParent, intent, null)
                    }

                    this.btnAddList.setOnClickListener {
                        Toast.makeText(
                            contextParent,
                            "${film.id} ${film.category} ${film.imageUrl}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    this.btnAddList.setOnClickListener {

                        if (user != null) {
                            try {
                                val nameRef = user.email?.split("@")?.get(0)
//                                Toast.makeText(
//                                    contextParent,
//                                    "${film.id} ${film.category} ${film.imageUrl} $nameRef",
//                                    Toast.LENGTH_SHORT
//                                ).show()

                                val database = Firebase.database
                                val myRef = database.getReference("${nameRef.toString()}/myList")

                                myRef.child(film.id.toString()).setValue(
                                    MyListMovie(film.id, film.category, film.imageUrl),
                                    object : DatabaseReference.CompletionListener {
                                        override fun onComplete(
                                            error: DatabaseError?,
                                            ref: DatabaseReference
                                        ) {
                                            Toast.makeText(
                                                contextParent,
                                                "Thêm thành công vào danh sách yêu thích",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    })


                            } catch (e: Exception) {
                                val nameRef = user.email?.split(".")?.get(0)

                                val database = Firebase.database
                                val myRef = database.getReference("${nameRef.toString()}/myList")

                                myRef.child(film.id.toString())
                                    .setValue(MyListMovie(film.id, film.category, film.imageUrl),
                                        object : DatabaseReference.CompletionListener {
                                            override fun onComplete(
                                                error: DatabaseError?,
                                                ref: DatabaseReference
                                            ) {
                                                Toast.makeText(
                                                    contextParent,
                                                    "Thêm thành công vào danh sách yêu thích",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        })
                            }

                        }else{
                            Toast.makeText(contextParent, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
                        }

                    }



                    Glide.with(contextParent)
                        .load(film.imageUrl)
                        .placeholder(R.drawable.animation_loading)
                        .error(R.drawable.img_2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .into(this.image)

                    this.title.text = film.name

                    this.description.text = film.description
                }



                bottomSheetDialog.setContentView(bottomSheetDialogLayout)
                bottomSheetDialog.show()
            }
        }

    }

    fun updateData(list: List<Film>) {
        this.list = list
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if (isMainSlider) {
            return 1
        } else {
            return 0
        }
    }
}