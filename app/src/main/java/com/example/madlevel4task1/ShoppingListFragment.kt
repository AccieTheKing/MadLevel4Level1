package com.example.madlevel4task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shoppinglist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingListFragment : Fragment() {
    // the lateinit var tells kotlin that it's going to be initialized at a later point
    private lateinit var productRepository: ProductRepository
    private lateinit var productAdapter: ProductsAdapter
    private var productsList: ArrayList<Product> = arrayListOf()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shoppinglist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ProductRepository(requireContext())
        getShoppingListFromDatabase()

        initView()
    }

    /**
     * This method will use the main thread
     */
    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val products = withContext(Dispatchers.IO) {
                productRepository.getAllProduct()
            }

            this@ShoppingListFragment.productsList.clear()
            this@ShoppingListFragment.productsList.addAll(products)
            this@ShoppingListFragment.productAdapter.notifyDataSetChanged()
        }
    }

    /**
     * This method will initialize all the listeners and everything that needs to be done after the
     * view is created
     */
    private fun initView() {
//        productAdapter = ProductsAdapter(products)
//        rvProducts.adapter = productAdapter
//        rvProducts.layoutManager = LinearLayoutManager(activity)

        rvProducts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        createItemTouchHelper().attachToRecyclerView(rvProducts)

        // this is an alternative method to link the layoutManager and the adapter to each other
        rvProducts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = productAdapter
        }
        
    }

    /**
     * This method will handle touch movements
     */
    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            /**
             * This method is for the up and down movements
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            /**
             * This method is for the left and right movements
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = productsList[position]

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }

        return ItemTouchHelper(callback)
    }
}