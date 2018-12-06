package com.example.administrator.newchat.custom

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.BottomInputLayoutBinding
import com.example.administrator.newchat.utilities.TEXT_MESSAGE

class ButtomInput:LinearLayout{

    private lateinit var binding:BottomInputLayoutBinding
    private var clientId:String? = null
    constructor(context: Context):super(context){
        init(context)
    }

    fun init(clientId:String){
        this.clientId = clientId
    }

    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        init(context)
    }

    private fun init(context: Context){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.bottom_input_layout,this,true)
        binding.inputSendText.setOnClickListener {
            val text = binding.centerText.text.toString()
            if (!TextUtils.isEmpty(text)){
                sendText(text)
                binding.centerText.setText("")
            }
        }
    }

    private val src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFgUlEQVR4Xu2bXWhcRRTHz7lLjMSWRh/8CvGjRAt9UkRQqUhrQREfVEwFa3wQGk3qZmbTkAR8cBEhoiQ7Zy+mkuqLUVGK7ZsQtJQKKQh98qGt1fpdKigkSBvNTXaOjCRlc7PJ3rl7P7JpFvYpZ879/387M7lzZgbhKv/gVe4fNgBs9IAKBLq7uzc1NDQoRNzNzLchYl32FGZmRPwVACY8zzswOjp6yW93mbF8Pn/N1NTUt4i4bZ31jjOlUule13Vny30tAyClfBMAXltn5v+3w8xvENHrqwIQQpxDxLvWIwAAOKOU2l6tB3B5gFLKMfDqEUhnZ2dTU1PT5UXtZk4gIuPnysc/BFBKqX0A6nICXPQgpfT/oEv8bADwde2NHhDnEBgYGNjied5TWutnAaANEVvNuASACwDwneM4R+bn54+6rvt3VHPOmhgCUspbAKCfmV9BxGurmPuHmd/TWg+5rvtnrSBSByCEeBIRPwWA62zMMPMlZn6uWCx+YdPOH5sqACmlBIBCWAMLr7K9SikVNkdqAIQQLyDieFjh5e201nuLxeInYXKlAiCbzW53HMesJzJhRPvbMPOc1voe13VP2+ZLBYCU8nMAeMZW7GrxzHyYiPbY5kwcgBDiJgC4GPUSemE+aFFKXbSBkAaAVxHRtREZNJaZ9xPRaNB4E5c4ACnl1wDwsI3IoLHMfJyIdgWNTwvANABssREZNJaZp4johqDxaQEoAcCSJaeN4CoT4SwRVXuTXJIi8SEghLiMiE1RmS7PY94OiWizTe7EAUgpfwKAO2xEBo1l5u+J6O6g8akMASHEEUR82kZk0FhmPkpEVu8XifcAIUQ3Ir4b1JRlXLdS6qBNmzQAbEPEszYig8aaYm2hUPghaHwqQ8A8VAjxFSI+aiO0WiwzTxDR49Xi/H9PvAcYAblcbqvW+jQiNtoKrhTPzLNa6zbXdX+3zZcKACOyp6fnecdxPrYVvAKAPUR0OEyu1AAsDIUcAAyHXRgt1AslERXDmE9tDigX29PT8wQifoaIm2xMMLMpjJpffsKm3ZqYA/wient7W0ul0iFEfCyIGTPhZTKZfSMjI78FiV8tJtUh4BeWy+UeYeadzPwAANxuyuIAoJnZlMV/RsRvEPF4oVA4UavxxfZrCkBUpmzyrBUAmMvldmitTUnrfgBoLvsaP1MAYJbR5nvKlNGVUiej2JRNFYDZEGHmAwCwFxFvtvnlmPkPABhHxIJtGaz8OakA6Ovru3Fubm4QAMy6oKaXIWb+FxFHM5nM0PDw8F82EFP5Nyil7DBbW1HXBJh5BgBeJqKPbCAk2gOEEIOIOGQj0CZ2oTI8qJR6O2i7RADk83lnenr6EAC8FFRYjXEHlVL7g0ySiQCQUpr9P7MPmNiHmQtE1FvtgbEDiHLRU82M/+/M3FFtTogVgJTyQWY+gYgNtuKjiDd7hoi4Uyk1uVK+2ABks9lGx3F+RMRbozBTQ44LMzMzd46Njc1VyhEbACHEACK+VYPwyJoycz8RvZMYgK6urusbGxt/AQCrGn1kjn2JzNJZa91a6WxRLD1ACJFDxJG4DIXJy8y9RLTsNEpcACYR8aEwQmNsM6mU2uHPHzmAuPb/awWz0vmByAFIKbMAELpGV6vR1dpXOj8QB4APAaAjTiM15B5XSr1Y3j4OAMcAwOqQQg2GrJoy8zEi2l0LALOro8vL2M3NzZl8Pn/lBLkQ4uwavk2y5D5Ae3t7pqWlZX4RSJDj8uZMzXkA2GqFvk6Cmfk8EbWVy610ZeaDBJe1SaN7Xym1b1UA/f39mz3POwUAVgcRknYS4nnnPM+7z39zrOJtkIVrc0VE3LVOrs196XleLtC1uRBk67pJXd8HioL8BoAoKNZzjv8A+YJ8bjSBfBUAAAAASUVORK5CYII="
    private fun sendText(content:String){
        check()
        val message = Message(0,clientId!!,content,CoreChat.userId!!, TEXT_MESSAGE,"me",true,src)
        CoreChat.sendText(message)
    }

    private fun check(){
        if (clientId == null){
            throw Throwable("the client id is not found")
        }

        if (CoreChat.userId==null){
            throw Throwable("the user id is not found")
        }
    }
}