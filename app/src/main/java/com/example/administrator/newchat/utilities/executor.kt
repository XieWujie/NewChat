package com.example.administrator.newchat.utilities

import java.util.concurrent.Executors

private val EXECUTOR = Executors.newSingleThreadExecutor()

fun runOnNewThread(f:()->Unit){
    EXECUTOR.execute(f)
}






