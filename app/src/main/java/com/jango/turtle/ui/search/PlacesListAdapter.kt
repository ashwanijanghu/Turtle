package com.jango.turtle.ui.search

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jango.turtle.AppExecutors
import com.jango.turtle.R
import com.jango.turtle.databinding.LayoutRowPlacesBinding
import com.jango.turtle.ui.common.DataBoundListAdapter
import com.jango.turtle.vo.Places


/**
 * Created by Ashwani on 11/06/18.
 * A RecyclerView adapter for [Places] class.
 */
class PlacesListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        val favListener: FavListener,
        private val placesClickCallback: ((Places) -> Unit)?
) : DataBoundListAdapter<Places, LayoutRowPlacesBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Places>() {
            override fun areItemsTheSame(oldItem: Places, newItem: Places): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Places, newItem: Places): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.venue.id == newItem.venue.id
            }
        }
) {

    override fun createBinding(parent: ViewGroup): LayoutRowPlacesBinding {
        val binding = DataBindingUtil.inflate<LayoutRowPlacesBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_row_places,
                parent,
                false,
                dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.place?.let {
                placesClickCallback?.invoke(it)
            }
        }
        binding.favIcon.setOnClickListener {
            binding.favIcon.isLiked = !binding.favIcon.isLiked
            binding.place?.let {
                if(binding.favIcon.isLiked) {
                    favListener.addFav(binding.place)
                } else{
                    favListener.removeFav(binding.place)
                }
            }
        }
        return binding
    }

    override fun bind(binding: LayoutRowPlacesBinding, item: Places) {
        binding.place = item
        binding.favIcon.isLiked = item.isFav
    }
}
