package com.nsa.edvoraassessmentapp.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.nsa.edvoraassessmentapp.R
import com.nsa.edvoraassessmentapp.adapters.MainAdapter
import com.nsa.edvoraassessmentapp.databinding.FilterLayoutBinding
import com.nsa.edvoraassessmentapp.databinding.FragmentHomeBinding
import com.nsa.edvoraassessmentapp.models.ProductModel
import com.nsa.edvoraassessmentapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private var main_list= arrayListOf<ProductModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    data class filterData(
        var product:String,
        var state:String,
        var city:String
    ){ }
    private var filter=filterData("","","")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {

            clearFilter()
            this.main_list.clear()
            this.main_list.addAll(it)
            binding.swipeRefresh.isRefreshing=false

            binding.progressBar.isVisible=false
            if(it.size==0){
                binding.tryAgain.isVisible=true
                Toast.makeText(context, "Api Response Is Empty!", Toast.LENGTH_SHORT).show()
            }
            gotProducts(it)

        })

        viewModel.getProducts()
        binding.apply {
            tryAgain.setOnClickListener {
            viewModel.getProducts()
            progressBar.isVisible=true
            it.isVisible=false
        }
            swipeRefresh.setOnRefreshListener {
                viewModel.getProducts()
            }

           filterDropDownTv.setOnClickListener {
               filterBtn.endIconDrawable=context?.getDrawable(R.drawable.ic_baseline_arrow_drop_up_24 )
               val dialog = Dialog(requireActivity())
               val filterLayoutBinding:FilterLayoutBinding =
                   FilterLayoutBinding.inflate(LayoutInflater.from(context),null,false)
               dialog.setContentView(filterLayoutBinding.root)
               dialog.setCancelable(true)
               val displayMetrics = DisplayMetrics()
               (activity as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
               val height = displayMetrics.heightPixels
               val width = displayMetrics.widthPixels
               dialog.window!!.setLayout((width*0.8).toInt(), (height * 0.5).toInt())
               dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
               dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



               val productAdapter =ArrayAdapter(requireContext(), R.layout.dropdown_item,   categorizedMap.keys.toMutableList())

               filterLayoutBinding.apply {

              filterProductTv.setAdapter(productAdapter)

               val state= LinkedHashSet<String>()
               main_list.forEach {
                   state.add(it.address.state)
               }

               val stateAdapter =
                   context?.let { it1 -> ArrayAdapter(it1, R.layout.dropdown_item,   state.toArray()) }
               filterStateTv.setAdapter(stateAdapter)


                filterProductTv.setDropDownBackgroundResource(R.drawable.drop_down_bg)
                filterStateTv.setDropDownBackgroundResource(R.drawable.drop_down_bg)
                filterCityTv.setDropDownBackgroundResource(R.drawable.drop_down_bg)

               if(!filter.product.isEmpty()){
                   filterProductTv.setText(filter.product,false)
                   state.clear()
                   setProductSelection(filter.product, filterLayoutBinding, state,false)
               }else {
                   if (!filter.state.isEmpty()) {
                       city.clear()
                       filterStateTv.setText(filter.state,false)
                       setSateSelection(filter.state, filterLayoutBinding, false)
                   }
               }

                filterProductTv.setOnItemClickListener { parent, view, position, id ->

                   val product=categorizedMap.keys.elementAt(position)
                    state.clear()
                   setProductSelection(product, filterLayoutBinding, state, true)
               }


                filterStateTv.setOnItemClickListener { parent, view, position, id ->

                   val state_name=state.elementAt(position)
                    city.clear()
                   setSateSelection(state_name, filterLayoutBinding, true)
               }

                filterCityTv.setOnItemClickListener { parent, view, position, id ->
                   filter.city=city.elementAt(position)
                   checkFilter();
               }
               }
               dialog.setOnCancelListener(object :DialogInterface.OnDismissListener,
                   DialogInterface.OnCancelListener {
                   override fun onDismiss(p0: DialogInterface?) {
                       filterBtn.endIconDrawable=context?.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24 )
                   }

                   override fun onCancel(p0: DialogInterface?) {
                       filterBtn.endIconDrawable=context?.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24 )
                   }

               })

               dialog.show()
           }
            filterBtn.setEndIconOnClickListener{
               filterDropDownTv.callOnClick()

            }
            clearFilterBtn.setOnClickListener{
                clearFilter()
            }

        }

    }

    private fun showToast(s: String) {
        Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show()
    }

    val city= LinkedHashSet<String>()

    private fun setSateSelection(
        state: String,
        filterLayoutBinding: FilterLayoutBinding,
        clearVal: Boolean
    ) {
        filter.state = state
        if(clearVal){
        filter.city = ""
        }
        filterLayoutBinding.filterCityTv.setText("City")
        checkFilter();
        main_list.forEach {

            if (it.address.state.equals(state)) {
                city.add(it.address.city)
            }
        }

        val cityAdapter =
            context?.let { it1 -> ArrayAdapter(it1, R.layout.dropdown_item, city.toArray()) }
        filterLayoutBinding.filterCityTv.setAdapter(cityAdapter)
        if(!filter.city.isEmpty()){
            filterLayoutBinding.filterCityTv.setText(filter.city,false)
            checkFilter()
        }
    }

    private fun setProductSelection(
        product: String,
        filterLayoutBinding: FilterLayoutBinding,
        state: LinkedHashSet<String>,
        clearVal: Boolean
    ) {
        filter.product = product
        if(clearVal){
        filter.city = ""
        filter.state = ""
        }
        filterLayoutBinding.filterStateTv.setText("State")
        filterLayoutBinding.filterCityTv.setText("City")
        checkFilter();
        val products = categorizedMap.get(product)
        state.clear()
        products?.forEach {
            state.add(it.address.state)
        }
        val stateAdapter =
            context?.let { it1 -> ArrayAdapter(it1, R.layout.dropdown_item, state.toArray()) }

        filterLayoutBinding.filterStateTv.setAdapter(stateAdapter)
        if(!filter.state.isEmpty()){
            filterLayoutBinding.filterStateTv.setText(filter.state,false)
            city.clear()
            setSateSelection(filter.state, filterLayoutBinding, false)
        }
    }

    private fun checkFilter() {
        filterMap.clear()
        val lst= arrayListOf<ProductModel>()
        if(!filter.product.isEmpty()){

           // categorizedMap.put(it.product_name,lst)
            if(filter.state.isEmpty()){
                categorizedMap.get(filter.product)?.let {
                    filterMap.put(filter.product, it)
                }
            }else{
                lst.clear()
                categorizedMap.get(filter.product)?.let {
                   it.forEach{
                       if(it.address.state.equals(filter.state)){
                           if(!filter.city.isEmpty()){
                               if(it.address.city.equals(filter.city)){
                                   lst.add(it)
                               }
                           }else{
                           lst.add(it)
                       }
                       }
                   }
                }
                filterMap.put(filter.product, lst)
            }
        }else{

            categorizedMap.keys.forEach{
                val producList= arrayListOf<ProductModel>()
                categorizedMap.get(it)?.let {
                    it.forEach{
                        if(it.address.state.equals(filter.state)){
                            if(!filter.city.isEmpty()){
                                if(it.address.city.equals(filter.city)){
                                    producList.add(it)
                                }
                            }else{
                                producList.add(it)
                            }
                        }
                    }
                }
                if(!producList.isEmpty()){
                filterMap.put(it, producList)
                }
            }
        }
        binding.mainRecyclerView.adapter= context?.let { MainAdapter(filterMap, it) }
    }
    private fun clearFilter(){
        filterMap.clear()
        city.clear()
        filter.city=""
        filter.state=""
        filter.product=""
        binding.mainRecyclerView.adapter= context?.let { MainAdapter(categorizedMap, it) }
    }

    private val categorizedMap=HashMap<String, List<ProductModel>>()
    private val filterMap=HashMap<String, List<ProductModel>>()

    private fun gotProducts(list: List<ProductModel>) {
        categorizedMap.clear()

        Log.e("TAG", "total: "+ list.size)
        list.forEach {
            if(categorizedMap.containsKey(it.product_name)){

                val dta = categorizedMap.get(it.product_name)
                val lst= arrayListOf<ProductModel>()
                lst.add(it)

                dta?.forEach {
                    lst.add(it)
                }
                categorizedMap.put(it.product_name,lst)

            }else{
                val lst= arrayListOf<ProductModel>()
                lst.add(it)
                categorizedMap.put(it.product_name,lst)
            }
        }
        binding.apply {
            mainRecyclerView.adapter= context?.let { MainAdapter(categorizedMap, it) }
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}

