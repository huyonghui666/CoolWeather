package com.example.coolweather.UI.cityManagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R
import com.example.coolweather.UI.searchCity.SearchCityActivity


class CityManagement : AppCompatActivity(), View.OnClickListener,OnItemClickListener {

    private var sharedPreferences_added_city: SharedPreferences? = null
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var searchCity_recyclerview: RecyclerView? = null
    private val dataList= ArrayList<CityManagementCls>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_management)

        findViewById<View>(R.id.imv_search_city).setOnClickListener(this)
        findViewById<View>(R.id.searchcity_back).setOnClickListener(this)
        searchCity_recyclerview = findViewById<RecyclerView>(R.id.searchCity_recyclerview)


        // 注册回调
        activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                //先清空视图再初始化布局
                dataList.clear()
                initLayout()
            }
        }

        sharedPreferences_added_city = getSharedPreferences("added_city", MODE_PRIVATE)
        //适配器添加数据
        initLayout()

    }

    /**
     * 初始化布局
     */
    private fun initLayout() {
        // 获取所有数据并遍历
        val allEntries = sharedPreferences_added_city!!.all
        if (allEntries != null) {
            for ((key, value) in allEntries) {
                val storedString = value.toString()
                //以逗号分割，转换为List
                if (storedString != null) {
                    val retrievedList = storedString.split(",")
                    val cityManagementCls: CityManagementCls = CityManagementCls(
                        retrievedList[0],
                        "空气" + retrievedList[1] + " " + retrievedList[2] + "°/" + retrievedList[3] + "°",
                        retrievedList[4] + "°"
                        )
                    dataList.add(cityManagementCls)
                }
            }
        }
        val linearLayoutManager = LinearLayoutManager(this)
        searchCity_recyclerview!!.layoutManager = linearLayoutManager
        val adapter= CityManagementAdapter(this, dataList, this)
        searchCity_recyclerview!!.adapter = adapter
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.imv_search_city) {
            val intent = Intent(this, SearchCityActivity::class.java)
            activityResultLauncher!!.launch(intent)
        }
        if (v.id == R.id.searchcity_back) {
            finish()
        }
    }

    override fun onItemClicked(cityName: String?) {
        //通过城市名获得城市的信息，例如"宁远,良，19，3，14,114.234，25.232"
        val cityInfo = sharedPreferences_added_city?.getString(cityName, null)
        //转换为list
        val cityInfoList = cityInfo?.split(",")
        if (cityInfoList!=null){
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putString("lat", cityInfoList[6])
            bundle.putString("lon", cityInfoList[5])
            bundle.putString("city_name", cityName)
            resultIntent.putExtras(bundle)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}