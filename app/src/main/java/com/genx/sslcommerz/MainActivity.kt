package com.genx.sslcommerz

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.awesomedialog.*
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class MainActivity : AppCompatActivity() , SSLCTransactionResponseListener {

    lateinit var eTAmount: EditText
    lateinit var payButton: Button


    private var sslCommerzInitialization: SSLCommerzInitialization? = null
    var additionalInitializer: SSLCAdditionalInitializer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        eTAmount=findViewById(R.id.etAmountId)
        payButton=findViewById(R.id.payNowButtonId)


        payButton.setOnClickListener {
            val amount=eTAmount.text.toString().trim()
            if (amount.isNotEmpty()){
                sslSetUp(amount)
            }else{
                eTAmount.error="Error"
            }
        }
    }


    private fun sslSetUp(amount:String) {

        val currentTimestamp = System.currentTimeMillis()

        //ssl_commarz
        sslCommerzInitialization = SSLCommerzInitialization(
            "mobil5fe45035efe16",
            "mobil5fe45035efe16@ssl",
            amount.toDouble(), SSLCCurrencyType.BDT,
            "MYID$currentTimestamp",
            "yourProductType",
            SSLCSdkType.TESTBOX
        )

        additionalInitializer = SSLCAdditionalInitializer()
        additionalInitializer!!.valueA = "Value Option 1"
        additionalInitializer!!.valueB = "Value Option 1"
        additionalInitializer!!.valueC = "Value Option 1"
        additionalInitializer!!.valueD = "Value Option 1"




        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .addAdditionalInitializer(additionalInitializer)
            .buildApiCall(this)


    }

    override fun transactionFail(p0: String?) {

        AwesomeDialog
            .build(this)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .title("$p0")
            .icon(R.mipmap.ic_launcher)
            .onPositive("Continue") {

            }
    }

    override fun merchantValidationError(p0: String?) {
        AwesomeDialog
            .build(this)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .title("$p0")
            .icon(R.mipmap.ic_launcher)
            .onPositive("Continue") {

            }

    }

    override fun transactionSuccess(successInfo: SSLCTransactionInfoModel?) {

        if (successInfo != null) {


            Log.d("BODYDATA", successInfo.tranId)
            Log.d("BODYDATA", successInfo.status)

            AwesomeDialog
                .build(this)
                .position(AwesomeDialog.POSITIONS.CENTER)
                .title("Id:${successInfo.tranId} \nAmount: ${successInfo.amount} \nPayment Status:${successInfo.apiConnect}")
                .icon(R.mipmap.ic_launcher)
                .onPositive("Continue") {

                }

        }


    }
}